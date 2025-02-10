package org.example.java4_asm_backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.java4_asm_backend.model.Favorite;
import org.example.java4_asm_backend.repository.FavoriteRepository;

@ApplicationScoped
public class FavoriteService extends AbstractService<Favorite, Long, FavoriteRepository>{

    public FavoriteService() {
        super(new FavoriteRepository());
    }

    public Favorite findByVideoAndUser(String videoId, String userId) {
        return repository.findByVideoAndUser(videoId, userId);
    }

    public boolean removeFavoriteUnlike(String videoId, String userId) {
        return repository.removeFavoriteUnlike(videoId, userId);
    }
}
