package com.smartcampus.dto;

import com.smartcampus.models.User.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
