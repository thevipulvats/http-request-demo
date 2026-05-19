package com.engineeringdigest.http_request_demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

    private final Map<Integer, User> userStorage = new HashMap<>();

    private final AtomicInteger id = new AtomicInteger();

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users in the system")
    public List<User> getUsers(){
        return new ArrayList<>(userStorage.values());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a single user for the given ID. Throws 404 if not found.")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") int userId){
        if(!userStorage.keySet().contains(userId)){
            throw new ResourceNotFoundException(
                    "User with id " + userId + " not found");
        }
        return ResponseEntity.ok().header("something","good").body(userStorage.get(userId));
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Accepts name and age, validates them, and creates a new user")
    private UserResponse createUser(@RequestBody @Valid UserRequest userRequest){ // jackson
        User user = userMapper.toUser(userRequest);
        int id = this.id.getAndIncrement();
        user.setId(id);
        userStorage.put(id, user);
        UserResponse userResponse = userMapper.toUserResponse(user);
        return userResponse;
    }

    @PutMapping
    @Operation(summary = "Replace a user", description = "Completely replaces user data for the given ID")
    public User replaceUser(@RequestParam int id, @RequestBody User user){
        user.setId(id);
        userStorage.put(id, user);
        return userStorage.get(id);
    }

    @PatchMapping
    @Operation(summary = "Partially update a user", description = "Updates only the provided fields for the given user ID")
    public User updateUser(@RequestParam int id, @RequestBody User user){
        if(user.getAge() > 0){
            userStorage.get(id).setAge(user.getAge());
        }
        if(user.getName() != null && !"".equals(user.getName())){
            userStorage.get(id).setName(user.getName());
        }
        return userStorage.get(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes the user with the given ID")
    public ResponseEntity<Void> deleteUser(@PathVariable int id){
        User user = userStorage.remove(id);
        return ResponseEntity.noContent().build();
    }


}
