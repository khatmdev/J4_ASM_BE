package org.example.java4_asm_backend.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.java4_asm_backend.dto.PaginatedResponse;
import org.example.java4_asm_backend.dto.VideoDTO;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.model.Video;
import org.example.java4_asm_backend.service.UserService;
import org.example.java4_asm_backend.service.VideoService;
import org.example.java4_asm_backend.utils.JWTUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/videos")
public class VideoViewsController {

    @Inject
    private VideoService videoService;

    @Inject
    private UserService userService;

    private static final int VIDEOS_PER_PAGE = 6; // Số video mỗi trang

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideos(@QueryParam("page") @DefaultValue("1") int page,
                              @HeaderParam("Authorization") String authHeader) {
        try {
            // Decode token nếu có
            String userId = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring("Bearer ".length());
                userId = JWTUtil.decodeToken(token);
            }

            User currentUser = null;
            if (userId != null) {
                currentUser = userService.findById(userId);
                userService.refresh(currentUser);
            }

            // Tính vị trí bắt đầu
            int start = (page - 1) * VIDEOS_PER_PAGE;

            // Lấy danh sách video
            List<Video> videos = videoService.getVideosByPage(start, VIDEOS_PER_PAGE);

            // Lấy danh sách video user đã like nếu user đăng nhập
            List<String> likedVideoIds = currentUser != null
                    ? currentUser.getFavorites().stream()
                    .map(favorite -> favorite.getVideo().getId())
                    .collect(Collectors.toList())
                    : Collections.emptyList();

            // Gắn trạng thái isLiked cho từng video
            List<VideoDTO> videoDTOs = videos.stream()
                    .map(video -> new VideoDTO(video, likedVideoIds.contains(video.getId())))
                    .collect(Collectors.toList());

            // Tính tổng số trang
            int totalVideos = videoService.getTotalVideoCount();
            int totalPages = (int) Math.ceil((double) totalVideos / VIDEOS_PER_PAGE);

            // Đính kèm email
            String email = currentUser != null && currentUser.getEmail() != null
                    ? currentUser.getEmail()
                    : "example@example.com";

            // Tạo phản hồi
            Map<String, Object> response = new HashMap<>();
            response.put("email", email); // Thêm email vào response
            response.put("data", new PaginatedResponse(videoDTOs, page, totalPages));

            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving videos").build();
        }
    }




    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVideoDetails(@PathParam("id") String videoId,
                                    @HeaderParam("Authorization") String authHeader) {
        try {
            // Giải mã token để lấy thông tin user
            String token = authHeader != null ? authHeader.substring("Bearer ".length()) : null;
            String userId = token != null ? JWTUtil.decodeToken(token) : null;

            User currentUser = userId != null ? userService.findById(userId) : null;

            // Lấy thông tin video
            Video video = videoService.findById(videoId);
            if (video == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Video not found").build();
            }

            // Kiểm tra trạng thái like
            boolean isLiked = currentUser != null && currentUser.getFavorites().stream()
                    .anyMatch(favorite -> favorite.getVideo().getId().equals(videoId));

            // Tăng view
            long views = video.getViews();
            views++;
            video.setViews(views);
            videoService.save(video);

            // Trả về thông tin video kèm trạng thái isLiked
            VideoDTO videoDTO = new VideoDTO(video, isLiked);
            return Response.ok(videoDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving video details").build();
        }
    }
}

