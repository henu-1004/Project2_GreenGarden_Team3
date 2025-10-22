package kr.co.greengarden.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class RedirectToSslipFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String host = req.getServerName();
        // 만약 사용자가 IP로 접속했다면 sslip.io 주소로 리다이렉트
        if ("3.35.197.89".equals(host)) {
            String redirectUrl = "http://3.35.197.89.sslip.io:8080" + req.getRequestURI();
            if (req.getQueryString() != null) {
                redirectUrl += "?" + req.getQueryString();
            }
            res.sendRedirect(redirectUrl);
            return;
        }

        chain.doFilter(request, response);
    }
}
