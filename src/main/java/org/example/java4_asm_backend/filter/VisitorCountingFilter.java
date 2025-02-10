package org.example.java4_asm_backend.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

@WebFilter("/api/*") // Áp dụng cho tất cả các yêu cầu
public class VisitorCountingFilter implements Filter {

    private static final String VISITOR_COOKIE_NAME = "visitorTracked";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần khởi tạo thêm
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            ServletContext context = httpRequest.getServletContext();
            String requestURI = httpRequest.getRequestURI();
            System.out.println("viewer: " + requestURI);
            // Bỏ qua request đến /api/visitors
            if (requestURI.contains("/api/visitors")) {
                chain.doFilter(request, response);
                return;
            }

            boolean isNewVisitor = true;

            // Kiểm tra cookie để xác định người dùng đã được tính chưa
            if (httpRequest.getCookies() != null) {
                isNewVisitor = Arrays.stream(httpRequest.getCookies())
                        .noneMatch(cookie -> VISITOR_COOKIE_NAME.equals(cookie.getName()));
            }

            // Nếu là khách truy cập mới, tăng bộ đếm và đặt cookie
            if (isNewVisitor) {
                synchronized (context) { // Đồng bộ hóa để tránh xung đột
                    Integer visitors = (Integer) context.getAttribute("visitors");
                    if (visitors == null) {
                        visitors = 0;
                    }
                    visitors++;
                    context.setAttribute("visitors", visitors);
                }

                // Đặt cookie để ghi nhận khách truy cập
                Cookie visitorCookie = new Cookie(VISITOR_COOKIE_NAME, "true");
                visitorCookie.setMaxAge(3600); // Cookie tồn tại 1 giờ
                visitorCookie.setPath("/");
                httpResponse.addCookie(visitorCookie);
            }
        }

        // Tiếp tục xử lý request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Không cần xử lý khi hủy bộ lọc
    }
}
