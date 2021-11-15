package com.incentro.myservice.users.service;

import com.incentro.myservice.users.dto.AddUserDTO;
import com.incentro.myservice.users.dto.EditUserDTO;
import com.incentro.myservice.users.dto.UserResponseDTO;

import java.util.List;

public interface UsersService {

    UserResponseDTO addUser(AddUserDTO addUserDTO);

    UserResponseDTO editUser(EditUserDTO editUserDTO);

    void deleteUser(Long userId);

    UserResponseDTO getUserById(Long userId);

    List<UserResponseDTO> getUsers();

}
