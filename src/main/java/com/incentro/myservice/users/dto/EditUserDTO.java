package com.incentro.myservice.users.dto;

import com.incentro.myservice.users.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class EditUserDTO {

    @NotNull(message = "{id.empty}")
    private Long id;

    @NotBlank(message = "{first.name.empty}")
    @Size(max = 200, message = "{first.name.maximum.size}")
    private String firstName;

    @NotBlank(message = "{last.name.empty}")
    @Size(max = 200, message = "{last.name.maximum.size}")
    private String lastName;

    @NotBlank(message = "{phone.number.empty}")
    @Size(max = 12, message = "{phone.number.maximum.size}")
    @Pattern(regexp = "^[0-9]*", message = "{phone.number.invalid}")
    private String phoneNumber;

    @NotBlank(message = "{email.empty}")
    @Size(max = 250, message = "{email.maximum.size}")
    @Email(message = "{email.invalid}")
    private String email;

    @NotNull(message = "{active.empty}")
    private boolean active;

    @NotNull(message = "{role.empty}")
    private User.Role role;
}
