package com.incentro.myservice.users.web;

import com.incentro.myservice.application.model.AppResponse;
import com.incentro.myservice.users.service.UsersService;
import com.incentro.myservice.users.dto.AddUserDTO;
import com.incentro.myservice.users.dto.EditUserDTO;
import com.incentro.myservice.users.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
@Tag(name = "users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> addUser(@RequestBody @Valid AddUserDTO addUserDTO) {
        UserResponseDTO user = usersService.addUser(addUserDTO);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, user);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> editUser(@RequestBody @Valid EditUserDTO editUserDTO) {
        UserResponseDTO user = usersService.editUser(editUserDTO);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, user);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> deleteUser(@PathVariable(value = "userId") Long userId) {
        usersService.deleteUser(userId);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, "user.account.deleted.successful");
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> getUser(@PathVariable(value = "userId") Long userId) {
        UserResponseDTO user = usersService.getUserById(userId);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, user);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse> getAllUsers() {
        List<UserResponseDTO> users = usersService.getUsers();
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, users);
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }
}
