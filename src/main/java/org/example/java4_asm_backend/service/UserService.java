package org.example.java4_asm_backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.java4_asm_backend.model.User;
import org.example.java4_asm_backend.repository.UserRepository;

import java.util.List;

@ApplicationScoped
public class UserService extends AbstractService<User, String, UserRepository>{

    public UserService() {
        super(new UserRepository());
    }

    // Exception class for service-specific errors
    public static class UserServiceException extends RuntimeException {
        public UserServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Find a user by their email.
     * This method searches for a user by the provided email and returns the corresponding User object.
     *
     * @param email the email address to search for (must not be null or empty)
     * @return the User object associated with the email, or null if no user is found
     * @throws IllegalArgumentException if the email is null or empty
     * @throws UserServiceException     if there is any issue when fetching the user from the repository
     */
    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email must not be null or empty.");
        }
        try {
            return repository.findByEmail(email);
        } catch (Exception e) {
            throw new UserServiceException("Failed to find user by email: " + email, e);
        }
    }

    /**
     * Find users with pagination.
     *
     * @param pageNumber the page number (must be > 0)
     * @param pageSize   the page size (must be > 0)
     * @return a list of users in the specified page
     */
    public List<User> findUsersWithPage(int pageNumber, int pageSize) {
        if (pageNumber <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page number and size must be greater than zero.");
        }
        try {
            return repository.findUsersWithPage(pageNumber, pageSize);
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch users with pagination", e);
        }
    }

    /**
     * Count all users in the system.
     *
     * @return the total number of users
     */
    public long countUsers() {
        try {
            return repository.countUsers();
        } catch (Exception e) {
            throw new UserServiceException("Failed to count users", e);
        }
    }

    /**
     * Check if the email is already taken.
     *
     * @param email the email to check
     * @return true if email is already in use, false otherwise
     */
    public boolean isEmailTaken(String email) {
        return repository.findByEmail(email) != null;
    }

    public boolean clearUserIfNotAdmin(String userId) {
        User user = findById(userId); // Tìm user theo ID
        if (user == null || user.getAdmin()) {
            return false; // Không thể xóa nếu user không tồn tại hoặc là admin
        }
        return repository.deleteById(userId);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
