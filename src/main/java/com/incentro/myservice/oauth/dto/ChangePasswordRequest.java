package com.incentro.myservice.oauth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "{oauth.email.empty}")
    private String email;
    @NotBlank(message = "{oauth.password.empty}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\p{P}\\p{S}]).{8,}", message = "{oauth.password.strength}")
    private String password;
    @NotBlank(message = "{oauth.old.password.empty}")
    private String oldPassword;
}
