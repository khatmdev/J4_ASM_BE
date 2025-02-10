package org.example.java4_asm_backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.java4_asm_backend.model.Video;
import org.example.java4_asm_backend.repository.VideoRepository;

import java.util.List;

@ApplicationScoped
public class VideoService extends AbstractService<Video, String, VideoRepository> {

    public VideoService() {
        super(new VideoRepository());
    }

    // Exception class for service-specific errors
    public static class VideoServiceException extends RuntimeException {
        public VideoServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Find videos by a keyword in their title.
     *
     * @param keyword the keyword to search for (must not be null or empty)
     * @return a list of videos whose titles contain the keyword
     * @throws IllegalArgumentException if the keyword is null or empty
     * @throws VideoServiceException     if any error occurs during the search
     */
    public List<Video> findByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be null or empty.");
        }
        try {
            return repository.findByKeyword(keyword);
        } catch (Exception e) {
            throw new VideoServiceException("Failed to find videos by keyword: " + keyword, e);
        }
    }

    /**
     * Get videos by page for pagination purposes.
     *
     * @param start the starting index
     * @param limit the number of records to retrieve
     * @return a list of videos for the specified page
     * @throws VideoServiceException if any error occurs during the retrieval
     */
    public List<Video> getVideosByPage(int start, int limit) {
        if (start < 0 || limit <= 0) {
            throw new IllegalArgumentException("Start index must not be negative, and limit must be greater than zero.");
        }
        try {
            return repository.getVideosByPage(start, limit);
        } catch (Exception e) {
            throw new VideoServiceException("Failed to retrieve videos by page", e);
        }
    }

    /**
     * Get the total number of videos in the system.
     *
     * @return the total count of videos
     * @throws VideoServiceException if any error occurs during the count
     */
    public int getTotalVideoCount() {
        try {
            return repository.getTotalVideoCount();
        } catch (Exception e) {
            throw new VideoServiceException("Failed to count total videos", e);
        }
    }
}
