package com.ejemplo.SCruzProgramacionNCapasMaven.Configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class SessionTimeoutFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);

        boolean sessionExpired = (session == null || session.getAttribute("SPRING_SECURITY_CONTEXT") == null);

        boolean isAjaxRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (sessionExpired && isAjaxRequest) {
            response.setStatus(440); // Código personalizado para sesión expirada
            return;
        }

        chain.doFilter(req, res);
    }
}
