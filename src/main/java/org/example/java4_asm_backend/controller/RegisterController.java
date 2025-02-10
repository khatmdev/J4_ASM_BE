package org.example.java4_asm_backend.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.service.UserService;

@Path("/register")
public class RegisterController {

    @Inject
    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(User user) {

        // Kiểm tra email đã tồn tại
        if (userService.findByEmail(user.getEmail()) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\": \"Email đã tồn tại.\"}")
                    .build();
        }

        // Kiểm tra username đã tồn tại
        if (userService.findById(user.getId()) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"message\": \"Username đã tồn tại.\"}")
                    .build();
        }

        // Tạo user mới
        if(userService.save(user)){
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Đăng ký thành công!\"}")
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"message\": \"Something went wrong!\"}")
                .build();

    }
}
