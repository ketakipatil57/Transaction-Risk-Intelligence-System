package com.pict.Service;

import com.pict.dto.LoginRequestDTO;
import com.pict.dto.LoginResponseDTO;
import com.pict.dto.RegisterRequestDTO;
import com.pict.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO   registerUser(RegisterRequestDTO registerRequest);

    LoginResponseDTO loginUser(LoginRequestDTO loginRequest);

    UserResponseDTO getUserProfile(Long userId);
}
