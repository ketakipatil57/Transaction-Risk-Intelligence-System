package com.pict.dto;


import com.pict.Entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;

    private Role role;


}
