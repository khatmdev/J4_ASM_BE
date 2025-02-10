package org.example.java4_asm_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.java4_asm_backend.model.Video;

public class VideoDTO {
    private String id;
    private String title;
    private long views;
    private String description;
    private boolean active;
    private boolean isLiked;

    public VideoDTO(Video video, boolean isLiked) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.views = video.getViews();
        this.description = video.getDescription();
        this.active = video.isActive();
        this.isLiked = isLiked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "VideoDTO{" +
                "isLiked=" + isLiked +
                ", active=" + active +
                ", description='" + description + '\'' +
                ", views=" + views +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
