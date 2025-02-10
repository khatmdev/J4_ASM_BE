package org.example.java4_asm_backend.controller.admin;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.java4_asm_backend.dto.PaginatedResponse;
import org.example.java4_asm_backend.model.Video;
import org.example.java4_asm_backend.service.VideoService;

import java.util.List;

@Path("/admin/videos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VideoController {

    @Inject
    private VideoService videoService;

    private static final int VIDEOS_PER_PAGE = 10; // Số video mỗi trang

    // Get paginated video list (default 10 items per page)
    @GET
    public Response getVideos(@QueryParam("page") @DefaultValue("1") int page) {
        // Tính vị trí bắt đầu
        int start = (page - 1) * VIDEOS_PER_PAGE;

        // Tính tổng số trang
        int totalVideos = videoService.getTotalVideoCount();
        int totalPages = (int) Math.ceil((double) totalVideos / VIDEOS_PER_PAGE);

        List<Video> videos = videoService.getVideosByPage(start, VIDEOS_PER_PAGE);
        return Response.ok(new PaginatedResponse<>(videos, page, totalPages)).build();
    }

    // Get a specific video by ID
    @GET
    @Path("/{id}")
    public Response getVideoById(@PathParam("id") String id) {
        Video video = videoService.findById(id);
        if (video == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Video not found for ID: " + id).build();
        }
        return Response.ok(video).build();
    }

    // Create a new video
    @POST
    public Response createVideo(Video video) {
        if (videoService.findById(video.getId()) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("A video with ID " + video.getId() + " already exists.").build();
        }
        videoService.save(video);
        return Response.status(Response.Status.CREATED).entity(video).build();
    }

    // Update an existing video
    @PUT
    @Path("/{id}")
    public Response updateVideo(@PathParam("id") String id, Video video) {
        Video existingVideo = videoService.findById(id);
        if (existingVideo == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Video not found for ID: " + id).build();
        }
        existingVideo.setTitle(video.getTitle());
        existingVideo.setViews(video.getViews());
        existingVideo.setDescription(video.getDescription());
        existingVideo.setActive(video.isActive());

        videoService.save(existingVideo);
        return Response.ok(existingVideo).build();
    }

    // Delete a video
    @DELETE
    @Path("/{id}")
    public Response deleteVideo(@PathParam("id") String id) {
        Video existingVideo = videoService.findById(id);
        if (existingVideo == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Video not found for ID: " + id).build();
        }
        videoService.deleteById(id);
        return Response.noContent().build();
    }
}
