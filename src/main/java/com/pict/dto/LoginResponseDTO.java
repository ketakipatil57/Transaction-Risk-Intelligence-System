package com.pict.dto;

import com.pict.Entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {

    private String token;

    private Long id;

    private String name;

    private Role role;
}
