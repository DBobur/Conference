package com.example.dto;

import com.example.entity.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {
    private String email;
    private String username;
    private String password;
    private List<Role> roleList;
}
