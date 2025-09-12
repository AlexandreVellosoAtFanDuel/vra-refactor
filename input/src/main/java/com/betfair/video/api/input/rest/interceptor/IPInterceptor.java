package com.betfair.video.api.input.rest.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class IPInterceptor implements HandlerInterceptor {

    private static final String X_IP = "X-IP";
    private static final String X_IPS = "X-IPS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(X_IP, cleanHeaderValue(request.getHeader(X_IP)));
        request.setAttribute(X_IPS, cleanHeaderValue(request.getHeader(X_IPS)));

        return true;
    }

    public static String cleanHeaderValue(String value) {
        if (value == null) {
            return null;
        }

        return value.replace("\n", " ")
                .replace("\r", " ");
    }
}
