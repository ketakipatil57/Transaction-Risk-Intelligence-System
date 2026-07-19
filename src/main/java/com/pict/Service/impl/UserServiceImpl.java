package com.pict.Service.impl;

import com.pict.Entity.Role;
import com.pict.Entity.User;
import com.pict.Repository.UserRepo;
import com.pict.Security.JwtUtil;
import com.pict.Service.UserService;
import com.pict.dto.LoginRequestDTO;
import com.pict.dto.LoginResponseDTO;
import com.pict.dto.RegisterRequestDTO;
import com.pict.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO registerRequest){

    if(userRepo.existsByEmail(registerRequest.getEmail())){
        throw new RuntimeException("Email already exists");
    }
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepo.save(user);


    UserResponseDTO response = new UserResponseDTO();
    response.setId(user.getId());
    response.setEmail(user.getEmail());
    response.setName(user.getName());
    response.setRole(user.getRole());

    return response;
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequest){
        //Checking weather user is present in db or not
        Optional<User> user = userRepo.findByEmail(loginRequest.getEmail());

        if(!(user.isPresent())){
            throw new RuntimeException("User not registered");
        }

        // verifying password
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())){
            throw new RuntimeException("Invalid Password");
        }
        // generating token
        String jwtToken = jwtUtil.generateToken(user.get());

        User existingUser = user.get();
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(jwtToken);
        response.setId(existingUser.getId());
        response.setName(existingUser.getName());
        response.setRole(existingUser.getRole());

        return response;
    }

    @Override
    public UserResponseDTO getUserProfile(Long userId) {

        User existingUser = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not Found"));

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(existingUser.getId());
        userResponseDTO.setName(existingUser.getName());
        userResponseDTO.setEmail(existingUser.getEmail());
        userResponseDTO.setRole(existingUser.getRole());

        return userResponseDTO;

    }
}
