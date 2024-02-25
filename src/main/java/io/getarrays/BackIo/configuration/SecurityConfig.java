package io.getarrays.BackIo.configuration;

import io.getarrays.BackIo.filter.CustomAuthorizationFilter;
import io.getarrays.BackIo.handler.CustomAccessDeniedHandler;
import io.getarrays.BackIo.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private static final String[] PUBLIC_URLS = {"/user/verify/account/**", "/user/register/**", "/user/update/password/**", "/user/login/**", "/user/verify/code/**", "/user/resetpassword/**", "/user/verify/password/**", "/user/refresh/token/**", "/user/image/**"};
    // "/user/update/**"
    private final UserDetailsService userDetailsService;
    private final CustomAuthorizationFilter customAuthorizationFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()).cors(AbstractHttpConfigurer::disable);
        http
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(STATELESS));
        http
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(PUBLIC_URLS).permitAll());
        http
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests.requestMatchers(HttpMethod.DELETE,"/user/delete/**").hasAnyAuthority("DELETE:USER"));
        http
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests.requestMatchers(HttpMethod.DELETE,"/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER"));
        http
                .exceptionHandling(serverHttpSecurity ->
                        serverHttpSecurity.accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint));
        http
                .authorizeHttpRequests((authz) -> {
                    authz.anyRequest().authenticated();
                });
        http
                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public  AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }
}
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//    private final BCryptPasswordEncoder encoder;
//    private final CustomAccessDeniedHandler customAccessDeniedHandler;
//    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//    private static final String[] PUBLIC_URLS = {"/user/login/**", "/user/verify/code/**", "/user/resetpassword/**"};
//    private final UserDetailsService userDetailsService;
//    private final CustomAuthorizationFilter customAuthorizationFilter;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()).cors(cors -> cors.disable())
//                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
//                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers(PUBLIC_URLS).permitAll()
//                        .requestMatchers(HttpMethod.DELETE,"/user/delete/**").hasAnyAuthority("DELETE:USER")
//                        .requestMatchers(HttpMethod.DELETE,"/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER")
//                        .anyRequest().authenticated())
//                .exceptionHandling(serverHttpSecurity -> serverHttpSecurity
//                        .accessDeniedHandler(customAccessDeniedHandler)
//                        .authenticationEntryPoint(customAuthenticationEntryPoint))
//                .addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(){
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(encoder);
//        return new ProviderManager(authProvider);
//    }
//}
