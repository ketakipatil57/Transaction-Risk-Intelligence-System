package com.pict.Controller;

import com.pict.Service.UserService;
import com.pict.dto.LoginRequestDTO;
import com.pict.dto.LoginResponseDTO;
import com.pict.dto.RegisterRequestDTO;
import com.pict.dto.UserResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }


    // Register request
    @PostMapping("/register")
    public UserResponseDTO registerUser(@RequestBody RegisterRequestDTO registerRequestDTO){
        return userService.registerUser(registerRequestDTO);
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        return userService.loginUser(loginRequestDTO);
    }

    @GetMapping("/users/{userId}")
    public UserResponseDTO getUserProfile(@PathVariable Long userId){
        return userService.getUserProfile(userId);
    }
}
