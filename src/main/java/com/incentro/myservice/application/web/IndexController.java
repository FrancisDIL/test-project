package com.incentro.myservice.application.web;

import com.incentro.myservice.application.model.AppResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("index")
@Tag(name = "Index")
public class IndexController {

    @Hidden
    @GetMapping(value = "/test/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<AppResponse> testAdministrator() {
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, "Test Endpoint");
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @Hidden
    @GetMapping(value = "/test/member", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<AppResponse> testManager() {
        AppResponse apiOkResponse = new AppResponse(HttpStatus.OK, "Test Endpoint");
        return new ResponseEntity<>(apiOkResponse, new HttpHeaders(), HttpStatus.OK);
    }
}
