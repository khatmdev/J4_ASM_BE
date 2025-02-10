package org.example.java4_asm_backend.controller;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;

@Path("/visitors")
public class VisitorController {
    @Context
    private ServletContext context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVisitorCount() {
        Integer visitors = (Integer) context.getAttribute("visitors");
        if (visitors == null) {
            visitors = 0;
        }
        return Response.ok(Collections.singletonMap("visitors", visitors)).build();
    }
}
