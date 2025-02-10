package org.example.java4_asm_backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.java4_asm_backend.dto.FavoriteReportDTO;
import org.example.java4_asm_backend.dto.FavoriteUserDTO;
import org.example.java4_asm_backend.dto.SharedFriendDTO;
import org.example.java4_asm_backend.model.Favorite;
import org.example.java4_asm_backend.model.Share;
import org.example.java4_asm_backend.model.Video;
import org.example.java4_asm_backend.repository.FavoriteRepository;
import org.example.java4_asm_backend.repository.ShareRepository;
import org.example.java4_asm_backend.repository.VideoRepository;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReportService {

    private final VideoRepository videoRepository = new VideoRepository();
    private final FavoriteRepository favoriteRepository = new FavoriteRepository();
    private final ShareRepository shareRepository = new ShareRepository();

    /**
     * Báo cáo danh sách video được yêu thích.
     */
    public List<FavoriteReportDTO> getFavoritesReport() {
        List<Video> videos = videoRepository.findAll();
        return videos.stream()
                .map(video -> {
                    List<Favorite> favorites = favoriteRepository.findByVideo(video);
                    if (favorites.isEmpty()) return null;

                    long count = favorites.size();
                    Date latestDate = favorites.stream()
                            .max(Comparator.comparing(Favorite::getLikedDate))
                            .map(Favorite::getLikedDate)
                            .orElse(null);
                    Date oldestDate = favorites.stream()
                            .min(Comparator.comparing(Favorite::getLikedDate))
                            .map(Favorite::getLikedDate)
                            .orElse(null);

                    return new FavoriteReportDTO(video.getTitle(), count, latestDate, oldestDate);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Báo cáo người dùng yêu thích theo tiêu đề video.
     */
    public List<FavoriteUserDTO> getFavoriteUsersByVideo(String videoTitle) {
        Video video = videoRepository.findByTitle(videoTitle);
        if (video == null) {
            return Collections.emptyList(); // Nếu không tìm thấy video, trả về danh sách rỗng
        }
        List<Favorite> favorites = favoriteRepository.findByVideo(video);
        return favorites.stream()
                .map(favorite -> {
                    var user = favorite.getUser();
                    return new FavoriteUserDTO(
                            user.getId(),
                            user.getFullname(),
                            user.getEmail(),
                            favorite.getLikedDate()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Báo cáo bạn bè được chia sẻ theo tiêu đề video.
     */
    public List<SharedFriendDTO> getSharedFriendsByVideo(String videoTitle) {
        Video video = videoRepository.findByTitle(videoTitle);
        if (video == null) {
            return Collections.emptyList(); // Nếu không tìm thấy video, trả về danh sách rỗng
        }
        List<Share> shares = shareRepository.findByVideo(video);
        return shares.stream()
                .map(share -> {
                    var user = share.getUser();
                    return new SharedFriendDTO(
                            user.getFullname(),
                            user.getEmail(),
                            share.getEmail(),
                            share.getShareDate()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách tiêu đề video.
     */
    public List<String> getVideoTitles() {
        List<String> titles = videoRepository.getTitles();
        if(titles.isEmpty()) return Collections.emptyList();
        return titles;
    }
}
