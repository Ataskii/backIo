package io.backQL.BackIo.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.backQL.BackIo.domain.UserPrincipal;
import io.backQL.BackIo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    public static final String $_JWT_SECRET = "${jwt.secret}";
    private static final String GET_ARRAYS_LLC = "Get Arrays, LLC";
    private static final String CUSTOMER_MANAGEMENT_SERVICE = "Customer Management Service";
    private static long ACCESS_TOKEN_EXPIRATION_TIME = 432_000_000;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 432_000_000;
    private static final String AUTHORITIES = "authorities";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    @Value($_JWT_SECRET)
    private String secret;
    private final UserService userService;

    public static void breakToken(Long id) {
        ACCESS_TOKEN_EXPIRATION_TIME = 0;
    }


    public String createAccessToken(UserPrincipal userPrincipal) {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(CUSTOMER_MANAGEMENT_SERVICE)
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUser().getId().toString()).withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public String createRefreshToken(UserPrincipal userPrincipal) {
    String[] claims = getClaimsFromUser(userPrincipal);
    return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(CUSTOMER_MANAGEMENT_SERVICE)
            .withIssuedAt(new Date()).withSubject(userPrincipal.getUser().getId().toString())
            .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public Long getSubject(String token, HttpServletRequest request) {
        try{
            return Long.valueOf(getJWTVerifier().verify(token).getSubject());
        }catch(TokenExpiredException exception){
            request.setAttribute("expiredMessage", exception.getMessage());
            throw exception;
        }catch (InvalidClaimException exception){
            request.setAttribute("invalidClaim", exception.getMessage());
            throw exception;
        }catch (Exception exception) {
            throw exception;
        }
    }
    public String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }
    public List<GrantedAuthority>getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(toList());
    }
    public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken userpasswordAuthToken = new UsernamePasswordAuthenticationToken(this.userService.getUserById(userId), null, authorities);
        userpasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return userpasswordAuthToken;
    }
    public boolean isTokenValid(Long userId, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return !Objects.isNull(userId) && !isTokenExpired(verifier, token);
    }
    public String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }
    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }
    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try{
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
        }catch (JWTVerificationException exception) {
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
            }
        return verifier;
    }
}
