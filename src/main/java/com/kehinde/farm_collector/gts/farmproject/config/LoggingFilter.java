package com.kehinde.farm_collector.gts.farmproject.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Enumeration;
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Instant startTime = Instant.now();
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        try {
            logRequestDetails(request, correlationId);
            filterChain.doFilter(request, response);
            logResponseDetails(response,startTime, correlationId);
        } finally {
            MDC.clear();
        }
    }

    private void logRequestDetails(HttpServletRequest request, String correlationId) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();

        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String paramValue = request.getParameter(paramName);
                params.append(paramName).append("=").append(paramValue).append("&");
            }
            if (params.length() > 0) {
                params.deleteCharAt(params.length() - 1); // Remove the last "&"
            }
        }

        // Log incoming request details with correlationId
        logger.info("Incoming Request: [{}] {} {} Params: [{}]", correlationId, request.getMethod(), request.getRequestURI(), params.toString());
    }

    private void logResponseDetails(HttpServletResponse response, Instant startTime, String correlationId) {
        long duration = Instant.now().toEpochMilli() - startTime.toEpochMilli();
        StringBuilder responseLog = new StringBuilder();
        responseLog.append("Outgoing Response: [").append(correlationId)
                .append("] Status ").append(response.getStatus())
                .append(" | Duration: ").append(duration).append(" ms");

        // Optionally log response headers if exists
        responseLog.append(" | Headers: {");
        response.getHeaderNames().forEach(headerName -> {
            String headerValue = response.getHeader(headerName);
            responseLog.append(headerName).append(": ").append(headerValue).append(", ");
        });
        if (!response.getHeaderNames().isEmpty()) {
            responseLog.setLength(responseLog.length() - 2);  // Remove last comma
        }
        responseLog.append("}");

        logger.info(responseLog.toString());
    }
}
