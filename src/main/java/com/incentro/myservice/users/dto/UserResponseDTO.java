package com.incentro.myservice.users.dto;

import com.incentro.myservice.users.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private boolean active;
    private User.Role role;
}
