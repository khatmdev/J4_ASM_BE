package org.example.java4_asm_backend;

import jakarta.ws.rs.ApplicationPath;
import org.example.java4_asm_backend.filter.CORSFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

@ApplicationPath("/api")
public class BackendApplication extends ResourceConfig {
    public BackendApplication() {
        // Scan package chứa REST resources
        packages("org.example.java4_asm_backend.controller").register(CORSFilter.class);
    }
}
