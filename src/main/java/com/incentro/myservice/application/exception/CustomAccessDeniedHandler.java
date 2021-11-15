package com.incentro.myservice.application.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incentro.myservice.application.model.AppResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger LOG = Logger.getLogger("CustomAccessDeniedHandler");
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LOG.log(Level.WARNING, accessDeniedException.getMessage(), accessDeniedException);
        AppResponse appResponse = new AppResponse(403, accessDeniedException.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), appResponse);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}