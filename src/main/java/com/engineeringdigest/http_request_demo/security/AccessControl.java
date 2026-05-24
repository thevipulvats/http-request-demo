package com.engineeringdigest.http_request_demo.security;

import com.engineeringdigest.http_request_demo.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccessControl {

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public boolean isAdmin(User user) {
        return "ADMIN".equals(user.getRole());
    }

    /** Logged-in user must match userId, or be an admin. */
    public void allowSameUserOrAdmin(int userId) {
        User loggedInUser = getLoggedInUser();
        if (loggedInUser.getId() != userId && !isAdmin(loggedInUser)) {
            log.warn("User {} tried to access data of user {}", loggedInUser.getId(), userId);
            throw new AccessDeniedException("You can only access your own data");
        }
    }

    /** Logged-in user must be an admin. */
    public void allowOnlyAdmin() {
        User loggedInUser = getLoggedInUser();
        if (!isAdmin(loggedInUser)) {
            log.warn("User {} attempted admin-only action", loggedInUser.getId());
            throw new AccessDeniedException("Admin access required");
        }
    }
}
