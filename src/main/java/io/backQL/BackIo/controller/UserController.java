package io.backQL.BackIo.controller;

import io.backQL.BackIo.domain.HttpResponse;
import io.backQL.BackIo.domain.UserPrincipal;
import io.backQL.BackIo.domain.Userr;
import io.backQL.BackIo.dto.userDto;
import io.backQL.BackIo.enumeration.EventType;
import io.backQL.BackIo.event.NewUserEvent;
import io.backQL.BackIo.exception.ApiException;
import io.backQL.BackIo.form.*;
import io.backQL.BackIo.provider.TokenProvider;
import io.backQL.BackIo.service.RoleService;
import io.backQL.BackIo.service.UserService;
import io.backQL.BackIo.utils.SmsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static io.backQL.BackIo.dto.userDtoMapper.toUser;
import static io.backQL.BackIo.utils.ExceptionUtils.processError;
import static io.backQL.BackIo.utils.UserUtils.*;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;
@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final RoleService roleService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ApplicationEventPublisher publisher;


    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginForm loginForm) {
//        Authentication authentication = authenticationManager.authenticate(unauthenticated(loginForm.getEmail(), loginForm.getPassword()));
//        userDto user = userService.getUserByEmail(loginForm.getEmail());
//        userDto user = getAuthenticatedUserReturnUserPrincipal(authentication);
        userDto user = authenticate(loginForm.getEmail(), loginForm.getPassword().toString());
        return user.getIsUsingMfa() ? sendVarificationUrl(user) : sendResponse(user);
}

    private userDto authenticate(String email, String password) {
        try {
            if (null != userService.getUserByEmail(email)) {
                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT));

            }
            Authentication authentication = authenticationManager.authenticate(unauthenticated(email, password));
            userDto loggedinuser = getLoggedInUser(authentication);
            if (!loggedinuser.getIsUsingMfa()) {
                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_SUCCESS));
            }
            return loggedinuser;
        } catch (Exception exception) {
            publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_FAILURE));
            processError(request, response, exception);
            throw new ApiException(exception.getMessage());
        }
    }
    private ResponseEntity<HttpResponse> sendResponse(userDto user) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("token_Refresh", tokenProvider.createRefreshToken(getUserPrinciple(user)),
                                 "user", user,
                                 "token_Access", tokenProvider.createAccessToken(getUserPrinciple(user))))
                        .message("Login success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    private UserPrincipal getUserPrinciple(userDto user){
        return new UserPrincipal(toUser(userService.getUserByEmail(user.getEmail())),
                roleService.getRoleByUserId(user.getId()));
    }
    private ResponseEntity<HttpResponse> sendVarificationUrl(userDto user) {
        userService.sendVarificationCode(user);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user))
                        .message("varification code sent to " + user.getEmail() + " please verify")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PostMapping("/register")
    public ResponseEntity<HttpResponse> saveUser(@RequestBody @Valid Userr user) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        userDto userDto = userService.createUser(user);
        return ResponseEntity.created(getUri()).body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userDto))
                        .message("User created.")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @GetMapping("/profile")
    public ResponseEntity<HttpResponse> getProfile(Authentication authentication) {
        userDto user = userService.getUserByEmail(authentication.getName());
        System.out.println(authentication);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user, "roles", roleService.getRoles()))
                        .message("Profile Retrived")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PatchMapping("/update")
    public ResponseEntity<HttpResponse> updateUser(@RequestBody @Valid UpdateForm user) throws InterruptedException {
//        TimeUnit.SECONDS.sleep(3);    // Puts delay to whole function useful for frontend.
        userDto updatedUser = userService.updateUserDetails(user);
        publisher.publishEvent(new NewUserEvent(updatedUser.getEmail(), EventType.PROFILE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", updatedUser))
                        .message("Profile updated")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Email is sended, check your mail box to reset your password.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @GetMapping("/verify/password/{key}")
    public ResponseEntity<HttpResponse> verifyPassword(@PathVariable("key") String key) {
        userDto user = userService.verifyPasswordKey(key);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user))
                        .message("Please enter a new password.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
//    @PostMapping("/resetpassword/{key}/{newpassword}/{confirmpassword}")
//    public ResponseEntity<HttpResponse> verifyNewPasswordUrl(@PathVariable("key") String key, @PathVariable("newpassword") String newPassword, @PathVariable("confirmpassword") String confirmPassword) {
//        userService.renewPassword(key, newPassword, confirmPassword);
//        return ResponseEntity.ok().body(
//                HttpResponse.builder()
//                        .timeStamp(now().toString())
//                        .message("Passport reset successful.")
//                        .status(HttpStatus.OK)
//                        .statusCode(HttpStatus.OK.value())
//                        .build()
//        );
//    }

    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        userDto user = userService.verifyCode(email, code);
        publisher.publishEvent(new NewUserEvent(user.getEmail(), EventType.LOGIN_ATTEMPT_SUCCESS));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", user,
                                 "token_Refresh", tokenProvider.createRefreshToken(getUserPrinciple(user)),
                                 "token_Access", tokenProvider.createAccessToken(getUserPrinciple(user))))
                        .message("Login success")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PatchMapping("/update/password")
    public ResponseEntity<HttpResponse> updatePassword(Authentication authentication, @RequestBody @Valid UpdatePasswordForm form) {
        userDto userDto = getAuthenticatedUserReturnUserPrincipal(authentication);
        userService.updatePassword(userDto.getId(), form.getCurrentPassword(), form.getNewPassword(), form.getConfirmPassword());
        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), EventType.PASSWORD_UPDATE));
        return ResponseEntity.ok().body(
            HttpResponse.builder()
                    .timeStamp(now().toString())
                    .message("Password updated successfully")
                    .status(OK)
                    .statusCode(OK.value())
                    .build()
        );
    }

    @PostMapping("/update/settings")
    public ResponseEntity<HttpResponse> updateAccountSettings(Authentication authentication, @RequestBody @Valid SettingsForm form) {
        userDto userDto = getAuthenticatedUserReturnUserDto(authentication);
        userService.updateAccountSettings(userDto.getId(), form.getEnabled(), form.getNotLocked());
        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), EventType.ACCOUNT_SETTINGS_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .data(of("Enabled", userDto.getEnabled(), "If account is not locked", userDto.getIsNotLocked()))
                        .timeStamp(now().toString())
                        .message("Account settings are updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PatchMapping("/update/role/{roleName}")
    public ResponseEntity<HttpResponse> updateUserRole(Authentication authentication, @PathVariable("roleName") String roleName) {
        userDto userDto = getAuthenticatedUserReturnUserDto(authentication);
        userService.updateUserRole(userDto.getId(),roleName);
        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), EventType.ROLE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("user", userService.getUserById(userDto.getId()), "roles", roleService.getRoles()))
                        .message("User role is updated.")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }


//userService.verifyAccountKey(key).getEnabled() ? "Account already verified." : "Account verified."
    @GetMapping("/verify/account/{key}")
    public ResponseEntity<HttpResponse> verifyAccount(@PathVariable("key") String key) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message(userService.verifyAccountKey(key).getEnabled() ? "Account already verified." : "Account verified")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/togglemfa")
    public ResponseEntity<HttpResponse> toggleMfa(Authentication authentication) {
        userDto userDto = userService.toggleMfa(getAuthenticatedUserReturnUserDto(authentication).getEmail());
        String status = null;
        SmsUtils.sendSms(userDto.getPhone(), "Multi-factor authentication: " + status);
        if (userDto.getIsUsingMfa().booleanValue()){ status = "Activated"; } else {status = "Deactivate";}
        publisher.publishEvent(new NewUserEvent(userDto.getEmail(), EventType.MFA_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Multi-factor authentication: " + status)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PostMapping("/update/image")
    public ResponseEntity<HttpResponse> updateProfileImage(Authentication authentication, @RequestParam("image")MultipartFile image) {
        userDto user = userService.getUserByEmail(getAuthenticatedUserReturnUserDto(authentication).getEmail());
        userService.updateUserImage(user, image);
        publisher.publishEvent(new NewUserEvent(user.getEmail(), EventType.PROFILE_PICTURE_UPDATE));
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Profile image updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @PostMapping("/resetpassword/new")
    public ResponseEntity<HttpResponse> resetPassword(Authentication authentication, @RequestBody @Valid ResetPasswordForm form) {
        userDto user = userService.getUserById(getAuthenticatedUserReturnUserDto(authentication).getId());
        userService.resetPassword(user, form.oldPassword, form.newPassword, form.confirmNewPassword);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .message("Password reset successful.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @GetMapping("/image/{imagename}")
    public byte[] getImage(@PathVariable("imagename") String file) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/Images" + file));
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<HttpResponse> refreshToken(HttpServletRequest request) {
        if (isHeaderTokenValid(request)) {
            String token = request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length());
            userDto user = userService.getUserById(tokenProvider.getSubject(token, request));
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .data(of("user", user,
                                     "access_token", tokenProvider.createAccessToken(getUserPrinciple(user)),
                                     "refresh_token", token))
                            .message("Token refreshed.")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build());
        } else {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .timeStamp(now().toString())
                            .reason("Refresh token missing or invalid.")
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build());
        }
    }
    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
//        return ResponseEntity.badRequest().body(
                return new ResponseEntity<>(HttpResponse.builder()
                        .timeStamp(now().toString())
                        .reason("There is no mapping to " + request.getMethod().toLowerCase() + " this path on the server.")
                        .status(HttpStatus.NOT_FOUND)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build(), NOT_FOUND);
    }
    private URI getUri() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/get/<userId>").toUriString());
    }
    private boolean isHeaderTokenValid(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) !=null
                && request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX)
                && tokenProvider.isTokenValid(
                tokenProvider.getSubject(
                        request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()), request),
                request.getHeader(AUTHORIZATION).substring(TOKEN_PREFIX.length()));
    }
}
