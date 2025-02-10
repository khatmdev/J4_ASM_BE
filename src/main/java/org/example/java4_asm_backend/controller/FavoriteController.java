package org.example.java4_asm_backend.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.example.java4_asm_backend.dto.UserVideoDTO;
import org.example.java4_asm_backend.dto.VideoDTO;
import org.example.java4_asm_backend.model.Favorite;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.model.Video;
import org.example.java4_asm_backend.service.FavoriteService;
import org.example.java4_asm_backend.service.UserService;
import org.example.java4_asm_backend.service.VideoService;
import org.example.java4_asm_backend.utils.JWTUtil;
import org.example.java4_asm_backend.utils.Mailer;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/favorites")
public class FavoriteController {

    @Inject
    private UserService userService;

    @Inject
    private FavoriteService favoriteService;

    @Inject
    private VideoService videoService;

    @GET
    @Produces("application/json")
    public Response getFavorites(@HeaderParam("Authorization") String authHeader) {
        String token = authHeader.substring("Bearer ".length());

        try {
            // Giải mã token và lấy thông tin người dùng
            String userId = JWTUtil.decodeToken(token);
            User currentUser = userService.findById(userId);

            if (currentUser == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            // Lấy danh sách video yêu thích (loại bỏ trùng lặp)
            List<Video> videosFavorites = currentUser.getFavorites().stream()
                    .map(favorite -> favorite.getVideo())
                    .distinct()
                    .collect(Collectors.toList());

            return Response.ok().entity(new UserVideoDTO(currentUser.getFullname(), currentUser.getEmail(), videosFavorites)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token.").build();
        }


    }

    @PUT
    @Path("/like/{videoId}")
    @Consumes("application/json")
    public Response likeVideo(@PathParam("videoId") String videoId, @HeaderParam("Authorization") String authHeader) {
        String token = authHeader.substring("Bearer ".length());
        String userId = JWTUtil.decodeToken(token);
        User currentUser = userService.findById(userId);

        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }

        Video video = videoService.findById(videoId);
        if (video == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Video not found").build();
        }

        // Kiểm tra nếu đã like rồi thì bỏ qua
        boolean alreadyLiked = currentUser.getFavorites().stream()
                .anyMatch(fav -> fav.getVideo().getId().equals(videoId));
        if (alreadyLiked) {
            return Response.status(Response.Status.CONFLICT).entity("Video already liked").build();
        }

        System.out.println("Bắt đầu lưu fav vào database");
        Favorite favorite = new Favorite();
        favorite.setVideo(video);
        favorite.setUser(currentUser);
        favorite.setLikedDate(new Date());
        if(favoriteService.save(favorite)){
            System.out.println("Save successful");
        } else {
            System.out.println("Save failed");
        }
        userService.refresh(currentUser);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/unlike/{videoId}")
    @Consumes("application/json")
    public Response unlikeVideo(@HeaderParam("Authorization") String authHeader, @PathParam("videoId") String videoId) {
        String token = authHeader.substring("Bearer ".length());
        // Giải mã token và lấy thông tin người dùng
        String userId = JWTUtil.decodeToken(token);
        User currentUser = userService.findById(userId);

        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }

        Favorite favorite = favoriteService.findByVideoAndUser(videoId, userId);
        if (favorite == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Favorite not found").build();
        }
        favoriteService.removeFavoriteUnlike(videoId, userId);
        return Response.noContent().build();
    }

    @POST
    @Path("/share")
    @Consumes("application/json")
    public Response shareFavorite(Map<String, String> request, @HeaderParam("Authorization") String authHeader) {
        String token = authHeader.substring("Bearer ".length());
        // Giải mã token và lấy thông tin người dùng
        String userId = JWTUtil.decodeToken(token);
        User currentUser = userService.findById(userId);

        String videoId = request.get("videoId");
        String recipientEmails = request.get("recipientEmails");
        String appPassword = request.get("appPassword");

        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
        }

        // Gửi email
        String subject = "Chia sẻ video";
        String body = "Người dùng đã chia sẻ video với bạn: <br/>"
                + "<a href='https://www.youtube.com/watch?v=" + videoId + "'>Xem video tại đây</a>";

        List<String> emailList = List.of(recipientEmails.split(",\\s*"));
        Mailer.sendEmailsAsync(currentUser.getEmail(), appPassword, emailList, subject, body);

        return Response.ok().build();
    }
}
