package org.example.java4_asm_backend.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.service.UserService;
import org.example.java4_asm_backend.utils.JWTUtil;
import java.io.IOException;

@WebFilter("/*") // Áp dụng cho tất cả các endpoint
public class AuthFilter implements Filter {

    private UserService userService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = new UserService();
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Lấy URI của request
        String uri = req.getRequestURI();

        // Nếu không phải endpoint cần bảo mật, bỏ qua kiểm tra
        if (!uri.contains("/admin/")) {
            chain.doFilter(request, response);
            return;
        }

        // Lấy token từ header Authorization
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            respondWithJson(res, HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        // Trích xuất token
        String token = authHeader.substring(7);
        try {
            // Kiểm tra tính hợp lệ của token
            if (!JWTUtil.isTokenValid(token)) {
                respondWithJson(res, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }

            // Giải mã token để lấy userId
            String userId = JWTUtil.decodeToken(token);

            // Tìm thông tin người dùng từ UserService
            User user = userService.findById(userId);

            if (user == null) {
                respondWithJson(res, HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return;
            }

            // Kiểm tra quyền truy cập vào /admin/*
            if (uri.startsWith("/admin/") && !user.getAdmin()) {
                respondWithJson(res, HttpServletResponse.SC_FORBIDDEN, "Access denied. Admin role required.");
                return;
            }

            // Tiếp tục xử lý request
            chain.doFilter(request, response);

        } catch (Exception e) {
            respondWithJson(res, HttpServletResponse.SC_UNAUTHORIZED, "Token verification failed");
        }
    }

    /**
     * Gửi phản hồi JSON với mã lỗi và thông báo.
     */
    private void respondWithJson(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
