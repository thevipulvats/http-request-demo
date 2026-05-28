package com.engineeringdigest.http_request_demo.service;

import com.engineeringdigest.http_request_demo.dto.UserListResponse;
import com.engineeringdigest.http_request_demo.dto.UserPatchRequest;
import com.engineeringdigest.http_request_demo.dto.UserRequest;
import com.engineeringdigest.http_request_demo.dto.UserResponse;
import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.mapper.UserMapper;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;


    @Cacheable(value = "users", key = "'all'")
    public UserListResponse getAllUsers() {
        log.info("Fetching all users");
        List<UserResponse> users = userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
        return new UserListResponse(users);
    }

    // user::8
    @Cacheable(value = "user", key = "#userId")
    public UserResponse getUserById(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("User not found with id: {}", userId);
            throw new ResourceNotFoundException(
                    "User with id " + userId + " not found");
        }
        log.info("User found: {}", user.getName());
        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating new user with name: {}", userRequest.getName());
        User user = userMapper.toUser(userRequest);
        UserResponse response = userMapper.toUserResponse(userRepository.save(user));
        log.info("User created successfully with id: {}", response.getId());
        return response;
    }


    @CachePut(value = "user", key = "#id")
    public UserResponse replaceUser(int id, UserRequest userRequest) {
        log.info("Replacing user with id: {}", id);
        User user = userMapper.toUser(userRequest);
        user.setId(id);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @CachePut(value = "user", key = "#id")
    public UserResponse updateUser(int id, UserPatchRequest userRequest) {
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
        return userMapper.toUserResponse(userRepository.save(existingUser));
    }

    @CacheEvict(value = "user", key = "#id")
    public void deleteUser(int id) {
        log.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    public List<User> searchUsers(String name) {
        log.info("Searching users with name containing: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name);
    }


}
