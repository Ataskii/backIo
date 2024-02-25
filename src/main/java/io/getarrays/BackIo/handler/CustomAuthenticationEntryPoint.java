package io.getarrays.BackIo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getarrays.BackIo.domain.HttpResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // SOMETHING WRONG WITH THIS
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        HttpResponse httpResponse = HttpResponse.builder()
//                .timeStamp(now().toString())
//                .reason("You need to log in to see this page")
//                .status(UNAUTHORIZED)
//                .statusCode(UNAUTHORIZED.value())
//                .build();
//
//          ObjectMapper mapper = new ObjectMapper();
//          String body = mapper.writeValueAsString(authException.getMessage());
//
////          response.getWriter().write(body);
//          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//          response.setContentType(APPLICATION_JSON_VALUE);
//          response.setContentType(APPLICATION_JSON_VALUE);
//          response.setStatus(UNAUTHORIZED.value());
//
//          try (PrintWriter out = response.getWriter()) {
//            out.write(body);
//            out.flush();
//        } catch (IOException exception){
//            log.error("failed to write response: {} ", exception.getMessage());
//        }
//    }
//}
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(now().toString())
                .reason("You need to log in to see this page")
                .status(UNAUTHORIZED)
                .statusCode(UNAUTHORIZED.value())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(httpResponse); // Use the httpResponse object

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());

        try (PrintWriter out = response.getWriter()) {
            out.write(body);
            out.flush();
        } catch (IOException exception){
            log.error("failed to write response: {} ", exception.getMessage());
        }
    }
    }