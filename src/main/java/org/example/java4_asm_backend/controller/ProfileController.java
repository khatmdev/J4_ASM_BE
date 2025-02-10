package org.example.java4_asm_backend.controller;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.service.UserService;
import org.example.java4_asm_backend.utils.JWTUtil;

@Path("/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfileController {

    @Inject
    private UserService userService;

    @Context
    private HttpServletRequest request;

    @GET
    public Response getProfile(@HeaderParam("Authorization") String authHeader) {
        // Kiểm tra Authorization header có chứa token hay không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Authorization token is missing or invalid.").build();
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            // Giải mã token và lấy thông tin người dùng
            String username = JWTUtil.decodeToken(token);
            User currentUser = userService.findById(username);

            if (currentUser == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found.").build();
            }

            return Response.ok(currentUser).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token.").build();
        }
    }

    @PUT
    public Response updateProfile(@HeaderParam("Authorization") String authHeader, User updatedUser) {
        // Kiểm tra Authorization header có chứa token hay không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Authorization token is missing or invalid.").build();
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            // Giải mã token và lấy thông tin người dùng
            String username = JWTUtil.decodeToken(token);
            User currentUser = userService.findById(username);

            if (currentUser == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found.").build();
            }

            // Kiểm tra email có bị trùng không
            if (userService.isEmailTaken(updatedUser.getEmail())) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Email is already taken.").build();
            }

            updatedUser.setId(currentUser.getId());
            updatedUser.setAdmin(currentUser.getAdmin());
            boolean isUpdated = userService.save(updatedUser);

            if (isUpdated) {
                return Response.ok("Profile updated successfully.").build();
            }

            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update profile.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token.").build();
        }
    }

    @GET
    @Path("/email-check")
    public Response checkEmail(@QueryParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required.").build();
        }

        boolean isTaken = userService.isEmailTaken(email);
        return Response.ok(new EmailCheckResponse(isTaken)).build();
    }

    private static class EmailCheckResponse {
        private boolean exists;

        public EmailCheckResponse(boolean exists) {
            this.exists = exists;
        }

        public boolean isExists() {
            return exists;
        }

        public void setExists(boolean exists) {
            this.exists = exists;
        }
    }
}
