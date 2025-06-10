package com.epam.filter;

import com.epam.exception.AuthenticationException;
import com.epam.utility.AuthUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC_GET_PATHS = Set.of("/trainers", "/trainees", "/swagger-ui.html");
    private static final Set<String> PUBLIC_POST_PATHS = Set.of("/auth/login", "/trainees/register");

    private final AuthUtil authUtil;

    public AuthFilter(AuthUtil authUtil) {
        this.authUtil = authUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (("GET".equalsIgnoreCase(method) && PUBLIC_GET_PATHS.contains(path)) ||
                ("POST".equalsIgnoreCase(method) && PUBLIC_POST_PATHS.contains(path))) {
            chain.doFilter(request, response);
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, httpRequest.getHeader(HttpHeaders.AUTHORIZATION));

            authUtil.validateUser(headers);
            chain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }
}
