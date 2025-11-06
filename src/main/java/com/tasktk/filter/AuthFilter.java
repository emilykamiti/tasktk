package com.tasktk.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {

    // Public API endpoints that don't require authentication
    private final List<String> PUBLIC_API_ENDPOINTS = Arrays.asList(
            "/rest/auth/login",
            "/rest/auth/register",
            "/rest/auth/logout",
            "/rest/public/"
    );

    // API endpoints accessible to MEMBER role
    private final List<String> MEMBER_API_ENDPOINTS = Arrays.asList(
            "/rest/tasks",
            "/rest/activities",
            "/rest/user/profile"
    );

    // API endpoints accessible to MANAGER role
    private final List<String> MANAGER_API_ENDPOINTS = Arrays.asList(
            "/rest/teams",
            "/rest/team/manage",
            "/rest/tasks/assign",
            "/rest/reports"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Set CORS headers for all responses
        setCorsHeaders(httpRequest, httpResponse);

        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Check if it's an API call
        if (path.startsWith("/rest/")) {
            handleApiRequest(httpRequest, httpResponse, chain, path, session);
        } else {
            // For non-API requests, just continue
            chain.doFilter(request, response);
        }
    }

    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");

        // Allow your React app origin
        if (origin != null && (origin.equals("http://localhost:3000") || origin.equals("https://localhost:3000"))) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        } else {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, Authorization, X-Requested-With, X-Auth-Token");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    private void handleApiRequest(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain chain, String path, HttpSession session)
            throws IOException, ServletException {

        // Check if it's a public endpoint
        if (isPublicEndpoint(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Check authentication
        if (session == null || session.getAttribute("loggedInId") == null) {
            sendUnauthorizedError(response, "Authentication required");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        Long userId = (Long) session.getAttribute("loggedInId");

        // Check authorization based on role
        if (hasAccess(userRole, path)) {
            chain.doFilter(request, response);
        } else {
            sendForbiddenError(response, "Insufficient permissions");
        }
    }

    private boolean isPublicEndpoint(String path) {
        return PUBLIC_API_ENDPOINTS.stream().anyMatch(path::startsWith);
    }

    private boolean hasAccess(String userRole, String path) {
        if (userRole == null) return false;

        switch (userRole.toUpperCase()) {
            case "ADMIN":
                return true; // Admin has access to all API endpoints

            case "MANAGER":
                return isManagerEndpoint(path) || isMemberEndpoint(path);

            case "MEMBER":
                return isMemberEndpoint(path);

            default:
                return false;
        }
    }

    private boolean isMemberEndpoint(String path) {
        return MEMBER_API_ENDPOINTS.stream().anyMatch(path::startsWith);
    }

    private boolean isManagerEndpoint(String path) {
        return MANAGER_API_ENDPOINTS.stream().anyMatch(path::startsWith);
    }

    private void sendUnauthorizedError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\": false, \"message\": \"" + message + "\"}");
    }

    private void sendForbiddenError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\": false, \"message\": \"" + message + "\"}");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}