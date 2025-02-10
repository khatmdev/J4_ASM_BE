package org.example.java4_asm_backend.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.repository.UserRepository;
import org.example.java4_asm_backend.utils.Mailer;

import java.util.Map;
import java.util.Random;

@Path("/forgot-password")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ForgotPasswordController {

    private final UserRepository userRepository = new UserRepository(); // Thay bằng CDI nếu có

    @POST
    public Response forgotPassword(Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\": \"Email không được để trống.\"}")
                    .build();
        }

        if (!userRepository.existsByEmail(email)) { // Kiểm tra email tồn tại
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\": \"Email chưa được đăng ký. Vui lòng đăng ký trước.\"}")
                    .build();
        }

        String newPassword = generateRandomPassword();
        User user = userRepository.findByEmail(email);
        boolean sent = Mailer.send(email, "Mật khẩu mới của bạn", "Mật khẩu mới: " + newPassword
                 + "Tài khoản: " + user.getId());

        if (!sent) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Không thể gửi email. Vui lòng thử lại sau.\"}")
                    .build();
        }

        // TODO: Lưu mật khẩu mới vào cơ sở dữ liệu
        user.setPassword(newPassword);
        userRepository.save(user);
        userRepository.refresh(user);
        return Response.ok("{\"message\": \"Email đã được gửi thành công.\"}").build();
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
