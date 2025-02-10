package org.example.java4_asm_backend.controller.admin;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.dto.PaginatedResponse;
import org.example.java4_asm_backend.service.UserService;

import java.util.List;

@Path("/admin/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    // Lấy danh sách người dùng với phân trang
    @GET
    public Response findAll(@QueryParam("page") @DefaultValue("1") int page,
                            @QueryParam("size") @DefaultValue("5") int size) {
        try {
            List<User> users = userService.findUsersWithPage(page, size);
            long totalUsers = userService.countUsers();
            int totalPages = (int) Math.ceil((double) totalUsers / size);



            return Response.ok()
                    .entity(new PaginatedResponse<>(users, page, totalPages))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching users.")
                    .build();
        }
    }

    // Lấy thông tin người dùng theo ID
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") String id) {
        try {
            User user = userService.findById(id);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found.")
                        .build();
            }
            return Response.ok(user).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching the user.")
                    .build();
        }
    }

    // Thêm người dùng mới
    @POST
    public Response create(User user) {
        try {
            if (!isFormValid(user)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("All fields are required.")
                        .build();
            }
            if (userService.findById(user.getId()) != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("User ID already exists.")
                        .build();
            }
            boolean success = userService.save(user);
            if (success) {
                return Response.status(Response.Status.CREATED)
                        .entity("User created successfully.")
                        .build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create user.")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while creating the user.")
                    .build();
        }
    }

    // Cập nhật thông tin người dùng
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, User user) {
        try {
            if (!isFormValid(user)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("All fields are required.")
                        .build();
            }
            user.setId(id); // Đảm bảo ID từ URL
            boolean success = userService.save(user);
            if (success) {
                return Response.ok("User updated successfully.").build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while updating the user.")
                    .build();
        }
    }

    // Xóa người dùng theo ID
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            User user = userService.findById(id); // Tìm user trước để có thể xác định lý do
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User with ID " + id + " does not exist.")
                        .build();
            }
            if (user.getAdmin()) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Cannot delete admin user with ID " + id + ".")
                        .build();
            }
            boolean success = userService.clearUserIfNotAdmin(id);
            if (success) {
                return Response.ok("User with ID " + id + " deleted successfully.").build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred while deleting user with ID " + id + ".")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while deleting the user with ID " + id + ".")
                    .build();
        }
    }


    // Kiểm tra thông tin hợp lệ
    private boolean isFormValid(User user) {
        return user.getId() != null && !user.getId().isEmpty()
                && user.getFullname() != null && !user.getFullname().isEmpty()
                && user.getEmail() != null && !user.getEmail().isEmpty()
                && user.getPassword() != null && !user.getPassword().isEmpty();
    }
}
