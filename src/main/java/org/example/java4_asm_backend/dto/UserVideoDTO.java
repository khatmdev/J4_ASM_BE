package org.example.java4_asm_backend.dto;

import org.example.java4_asm_backend.model.Video;

import java.util.List;

public class UserVideoDTO {
    private String fullname;
    private String email;
    private List<Video> videoList;

    public UserVideoDTO(String fullname, String email, List<Video> videoList) {
        this.fullname = fullname;
        this.email = email;
        this.videoList = videoList;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
