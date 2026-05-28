package com.engineeringdigest.http_request_demo.controller;

import com.engineeringdigest.http_request_demo.dto.UserListResponse;
import com.engineeringdigest.http_request_demo.dto.UserPatchRequest;
import com.engineeringdigest.http_request_demo.dto.UserRequest;
import com.engineeringdigest.http_request_demo.dto.UserResponse;
import com.engineeringdigest.http_request_demo.exception.ResourceNotFoundException;
import com.engineeringdigest.http_request_demo.mapper.UserMapper;
import com.engineeringdigest.http_request_demo.model.User;
import com.engineeringdigest.http_request_demo.repository.UserRepository;
import com.engineeringdigest.http_request_demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
@RequiredArgsConstructor
public class UserController {


    private  final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users in the system")
    public UserListResponse getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a single user for the given ID. Throws 404 if not found.")
    public ResponseEntity<UserResponse> getUser(@PathVariable(name = "id") int userId) {
        return ResponseEntity.ok()
                .header("something", "good")
                .body(userService.getUserById(userId));
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Accepts name and age, validates them, and creates a new user")
    public UserResponse createUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping
    @Operation(summary = "Replace a user", description = "Completely replaces user data for the given ID")
    public UserResponse replaceUser(@RequestParam int id, @RequestBody @Valid UserRequest userRequest) {
        return userService.replaceUser(id, userRequest);
    }

    @PatchMapping
    @Operation(summary = "Partially update a user", description = "Updates only the provided fields for the given user ID")
    public UserResponse updateUser(@RequestParam int id, @RequestBody @Valid UserPatchRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes the user with the given ID")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search users by name", description = "Returns users whose name contains the search term")
    public List<User> searchUsers(@RequestParam String name) {
        return userService.searchUsers(name);
    }


}
