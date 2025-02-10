package org.example.java4_asm_backend.controller.admin;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.java4_asm_backend.dto.FavoriteReportDTO;
import org.example.java4_asm_backend.dto.FavoriteUserDTO;
import org.example.java4_asm_backend.dto.SharedFriendDTO;
import org.example.java4_asm_backend.service.ReportService;

import java.util.List;

@Path("/admin/report")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportController {

    @Inject
    private ReportService reportService;

    @GET
    @Path("/favorites")
    public Response getFavoritesReport() {
        try {
            List<FavoriteReportDTO> reports = reportService.getFavoritesReport();
            return Response.ok(reports).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching favorites report: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/favorite-users")
    public Response getFavoriteUsersReport(@QueryParam("videoTitle") String videoTitle) {
        if (videoTitle == null || videoTitle.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing or invalid videoTitle parameter")
                    .build();
        }

        try {
            List<FavoriteUserDTO> users = reportService.getFavoriteUsersByVideo(videoTitle);
            return Response.ok(users).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching favorite users report: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/shared-friends")
    public Response getSharedFriendsReport(@QueryParam("videoTitle") String videoTitle) {
        if (videoTitle == null || videoTitle.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing or invalid videoTitle parameter")
                    .build();
        }

        try {
            List<SharedFriendDTO> friends = reportService.getSharedFriendsByVideo(videoTitle);
            return Response.ok(friends).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching shared friends report: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/video-titles")
    public Response getVideoTitles() {
        try {
            List<String> titles = reportService.getVideoTitles();
            return Response.ok(titles).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching video titles: " + e.getMessage())
                    .build();
        }
    }
}
