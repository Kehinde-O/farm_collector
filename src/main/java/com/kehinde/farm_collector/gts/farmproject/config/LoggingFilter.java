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
import java.util.UUID;

@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Generate a unique ID for the request
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        // Log request details
        logger.info("Incoming Request: [{}] {} {}", correlationId, request.getMethod(), request.getRequestURI());

        // Proceed with the next filter in the chain
        filterChain.doFilter(request, response);

        // Log response details
        logger.info("Outgoing Response: [{}] Status {}", correlationId, response.getStatus());

        // Clear the MDC after the request is complete
        MDC.clear();
    }
}
