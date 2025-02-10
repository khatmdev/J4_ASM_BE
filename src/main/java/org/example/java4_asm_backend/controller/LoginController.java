package org.example.java4_asm_backend.controller;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.service.UserService;
import org.example.java4_asm_backend.utils.JWTUtil;

import java.util.HashMap;
import java.util.Map;

@Path("/login")
public class LoginController {
    @Inject
    private UserService userService;

    @Context
    private HttpServletRequest request;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Map<String, String> credentials) {
        try {
            // Kiểm tra dữ liệu đầu vào
            if (!isValidCredentials(credentials)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("message", "Username and password cannot be null or empty"))
                        .build();
            }

            String username = credentials.get("username");
            String password = credentials.get("password");

            // Tìm kiếm người dùng dựa trên username hoặc email
            User userLogin = userService.findById(username);
            if (userLogin == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("message", "User does not exist"))
                        .build();
            }

            // Kiểm tra mật khẩu
            if (!userLogin.getPassword().equals(password)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Map.of("message", "Incorrect password"))
                        .build();
            }

            // Tạo JWT token
            String token = JWTUtil.generateToken(username);


            // Tạo response thành công
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Welcome " + userLogin.getFullname() + "!");
            response.put("token", token);
            response.put("role", userLogin.getAdmin());

            return Response.ok(response).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("message", "An error occurred while processing the login request"))
                    .build();
        }
    }

    // Kiểm tra tính hợp lệ của thông tin đăng nhập
    private boolean isValidCredentials(Map<String, String> credentials) {
        if (credentials == null) return false;
        String username = credentials.get("username");
        String password = credentials.get("password");
        return username != null && !username.trim().isEmpty()
                && password != null && !password.trim().isEmpty();
    }
}
