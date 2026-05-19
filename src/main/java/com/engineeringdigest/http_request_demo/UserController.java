package com.engineeringdigest.http_request_demo;

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
public class UserController {

    private final Map<Integer, User> userStorage = new HashMap<>();

    private final AtomicInteger id = new AtomicInteger();

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public List<User> getUsers(@RequestHeader String a){
        System.out.println(a);
        return new ArrayList<>(userStorage.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") int userId){
        if(!userStorage.keySet().contains(userId)){
            throw new ResourceNotFoundException(
                    "User with id " + userId + " not found");
        }
        return ResponseEntity.ok().header("something","good").body(userStorage.get(userId));
    }

    @PostMapping
    private UserResponse createUser(@RequestBody @Valid UserRequest userRequest){ // jackson
        User user = userMapper.toUser(userRequest);
        int id = this.id.getAndIncrement();
        user.setId(id);
        userStorage.put(id, user);
        UserResponse userResponse = userMapper.toUserResponse(user);
        return userResponse;
    }

    @PutMapping
    public User replaceUser(@RequestParam int id, @RequestBody User user){
        user.setId(id);
        userStorage.put(id, user);
        return userStorage.get(id);
    }

    @PatchMapping
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
    public ResponseEntity<Void> deleteUser(@PathVariable int id){
        User user = userStorage.remove(id);
        return ResponseEntity.noContent().build();
    }


}
