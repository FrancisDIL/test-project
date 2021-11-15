package com.incentro.myservice.oauth.web;

import com.incentro.myservice.application.model.AppResponse;
import com.incentro.myservice.oauth.dto.ChangePasswordRequest;
import com.incentro.myservice.oauth.dto.ForgotPasswordRequest;
import com.incentro.myservice.oauth.dto.TokenChangePasswordRequest;
import com.incentro.myservice.oauth.service.OauthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("oauth")
@Tag(name = "oauth")
public class OauthController {

    private OauthService oauthService;

    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @PostMapping(value = "/forgot-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        oauthService.forgotPassword(forgotPasswordRequest);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, "oauth.reset.token.sent");
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), apiOkResponse.getStatus());
    }

    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        oauthService.changePassword(changePasswordRequest);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, "oauth.change.password");
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), apiOkResponse.getStatus());
    }

    @PostMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid TokenChangePasswordRequest tokenChangePasswordRequest) {

        oauthService.resetPasswordByToken(tokenChangePasswordRequest);
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, "oauth.reset.password");
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), apiOkResponse.getStatus());
    }
}
