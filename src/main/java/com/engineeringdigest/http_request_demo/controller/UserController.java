package com.engineeringdigest.http_request_demo.controller;

import com.engineeringdigest.http_request_demo.dto.UserPatchRequest;
import com.engineeringdigest.http_request_demo.dto.UserRequest;
import com.engineeringdigest.http_request_demo.dto.UserResponse;
import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.mapper.UserMapper;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/users")
@Tag(name = "USER APIs", description = "Endpoints for managing users")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users in the system")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a single user for the given ID. Throws 404 if not found.")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") int userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotFoundException(
                    "User with id " + userId + " not found");
        }
        return ResponseEntity.ok().header("something", "good").body(user);
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Accepts name and age, validates them, and creates a new user")
    private UserResponse createUser(@RequestBody @Valid UserRequest userRequest) { // jackson
        User user = userMapper.toUser(userRequest);
        return userMapper.toUserResponse(userRepository.saveUser(user));
    }

    @PutMapping
    @Operation(summary = "Replace a user", description = "Completely replaces user data for the given ID")
    public User replaceUser(@RequestParam int id, @RequestBody @Valid UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        user.setId(id);
        userRepository.update(user);
        return user;
    }

    @PatchMapping
    @Operation(summary = "Partially update a user", description = "Updates only the provided fields for the given user ID")
    public User updateUser(@RequestParam int id, @RequestBody @Valid UserPatchRequest userRequest) {
        User existingUser = userRepository.findById(id);
        User newUser = userMapper.toUser(userRequest);
        if (newUser.getAge() > 0) {
            existingUser.setAge(newUser.getAge());
        }
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            existingUser.setName(newUser.getName());
        }
        userRepository.update(existingUser);
        return existingUser;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes the user with the given ID")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userRepository.delete(id);
        return ResponseEntity.noContent().build();
    }


}
