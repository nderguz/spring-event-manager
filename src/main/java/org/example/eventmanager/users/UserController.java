package org.example.eventmanager.users;

import lombok.AllArgsConstructor;
import org.example.eventmanager.users.entities.UserDto;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public void createUser(){

    }

    @GetMapping("/{userId}")
    public void getUserInfo(@PathVariable Long userId){

    }

    @PostMapping("/auth")
    public void userAuthentication(@RequestBody UserDto userDto){

    }
}
