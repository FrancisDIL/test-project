package com.incentro.myservice.oauth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ForgotPasswordRequest {

    @NotBlank(message = "{oauth.email.empty}")
    private String email;
}
