package com.engineeringdigest.http_request_demo.service;

import com.engineeringdigest.http_request_demo.dto.UserPatchRequest;
import com.engineeringdigest.http_request_demo.dto.UserRequest;
import com.engineeringdigest.http_request_demo.dto.UserResponse;
import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.mapper.UserMapper;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper;

    public List<User> getAllUsers() {
        String cacheKey = "user:all";
        String dataFromRedis = redisTemplate.opsForValue().get(cacheKey);
        if(dataFromRedis != null){
            return objectMapper.readValue(dataFromRedis, new TypeReference<List<User>>() {});
        }
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(users), Duration.ofMinutes(10));
        log.info("Total users found: {}", users.size());
        return users;
    }

    public User getUserById(int userId) {
        String cacheKey = "user:" + userId;
        String dataFomRedis = redisTemplate.opsForValue().get(cacheKey);
        if(dataFomRedis != null){
            return objectMapper.readValue(dataFomRedis, User.class);
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("User not found with id: {}", userId);
            throw new ResourceNotFoundException(
                    "User with id " + userId + " not found");
        }
        redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(user), Duration.ofMinutes(10));
        log.info("User found: {}", user.getName());
        return user;
    }

    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating new user with name: {}", userRequest.getName());
        User user = userMapper.toUser(userRequest);
        UserResponse response = userMapper.toUserResponse(userRepository.save(user));
        log.info("User created successfully with id: {}", response.getId());
        return response;
    }

    public User replaceUser(int id, UserRequest userRequest) {
        log.info("Replacing user with id: {}", id);
        User user = userMapper.toUser(userRequest);
        user.setId(id);
        return userRepository.save(user);
    }

    public User updateUser(int id, UserPatchRequest userRequest) {
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
        log.info("Deleting user with id: {}", id);
        userRepository.deleteById(id);
    }

    public List<User> searchUsers(String name) {
        log.info("Searching users with name containing: {}", name);
        return userRepository.findByNameContainingIgnoreCase(name);
    }


}
