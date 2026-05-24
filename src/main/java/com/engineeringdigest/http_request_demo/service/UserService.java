package com.engineeringdigest.http_request_demo.service;

import com.engineeringdigest.http_request_demo.dto.UserPatchRequest;
import com.engineeringdigest.http_request_demo.dto.UserRequest;
import com.engineeringdigest.http_request_demo.dto.UserResponse;
import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.mapper.UserMapper;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.UserRepository;
import com.engineeringdigest.http_request_demo.security.AccessControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccessControl accessControl;

    public List<User> getAllUsers() {
        accessControl.allowOnlyAdmin();
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        log.info("Total users found: {}", users.size());
        return users;
    }

    public User getUserById(int userId){
        accessControl.allowSameUserOrAdmin(userId);
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("User not found with id: {}", userId);
            throw new ResourceNotFoundException(
                    "User with id " + userId + " not found");
        }
        log.info("User found: {}", user.getName());
        return user;
    }

    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating new user with name: {}", userRequest.getName());
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        UserResponse response = userMapper.toUserResponse(userRepository.save(user));
        log.info("User created successfully with id: {}", response.getId());
        return response;
    }

    public User replaceUser(int id, UserRequest userRequest) {
        accessControl.allowSameUserOrAdmin(id);
        log.info("Replacing user with id: {}", id);
        User user = userMapper.toUser(userRequest);
        user.setId(id);
        return userRepository.save(user);
    }

    public User updateUser(int id, UserPatchRequest userRequest) {
        accessControl.allowSameUserOrAdmin(id);
        log.info("Updating user with id: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        User newUser = userMapper.toUser(userRequest);
        if (newUser.getAge() > 0) {
            existingUser.setAge(newUser.getAge());
        }
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            existingUser.setName(newUser.getName());
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(int id) {
        accessControl.allowOnlyAdmin();
        log.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    public List<User> searchUsers(String name) {
        accessControl.allowOnlyAdmin();
        log.info("Searching users with name containing: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name);
    }


}
