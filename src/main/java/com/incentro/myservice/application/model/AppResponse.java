package com.incentro.myservice.application.model;

import com.incentro.myservice.application.service.Translator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AppResponse {
    // HTTP status questionCode
    private HttpStatus status;

    private int status_code;

    //List of constructed messages
    private List<String> messages;

    private Object data;

    public AppResponse() {
    }
    public AppResponse(HttpStatus status, Object data) {
        this.status = status;
        this.status_code = status.value();
        this.messages = new ArrayList<>();
        this.data = data;
    }

    public AppResponse(HttpStatus status, List<String> messages) {
        this.status = status;
        this.status_code = status.value();
        this.messages = messages;
    }

    public AppResponse(HttpStatus status, List<String> messages, Object data) {
        this.status = status;
        this.status_code = status.value();
        this.messages = messages;
        this.data = data;
    }

    public AppResponse(HttpStatus status, String message) {
        //change http status to 403
        if (message.equalsIgnoreCase("Access is denied")) {
            status = HttpStatus.FORBIDDEN;
        }
        this.status = status;
        this.status_code = status.value();
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        if (message.contains(".")) {
            this.messages.add(Translator.toLocale(message, null));
        } else {
            switch (message) {
                case "Access is denied":
                    this.messages.add(Translator.toLocale("oauth.access.denied", null));
                    break;
                case "Access token expired":
                    this.messages.add(Translator.toLocale("oauth.access.token.expired", null));
                    break;
                case "Bad credentials":
                    this.messages.add(Translator.toLocale("oauth.access.bad.credentials", null));
                    break;
                case "Full authentication is required to access this resource":
                    this.messages.add(Translator.toLocale("oauth.authentication.required", null));
                    break;
                default:
                    this.messages.add(message);
                    break;
            }
        }
    }

    public AppResponse(HttpStatus status, String message, Object data) {
        //change http status to 403
        if (message.equalsIgnoreCase("Access is denied")) {
            status = HttpStatus.FORBIDDEN;
        }
        this.data = data;
        this.status = status;
        this.status_code = status.value();
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        if (message.contains(".")) {
            this.messages.add(Translator.toLocale(message, null));
        } else {
            switch (message) {
                case "Access is denied":
                    this.messages.add(Translator.toLocale("oauth.access.denied", null));
                    break;
                case "Access token expired":
                    this.messages.add(Translator.toLocale("oauth.access.token.expired", null));
                    break;
                case "Bad credentials":
                    this.messages.add(Translator.toLocale("oauth.access.bad.credentials", null));
                    break;
                case "Full authentication is required to access this resource":
                    this.messages.add(Translator.toLocale("oauth.authentication.required", null));
                    break;
                default:
                    this.messages.add(message);
                    break;
            }
        }
    }

    public AppResponse(int status_code, String message) {
        //change http status to 403
        if (message.equalsIgnoreCase("Access is denied")) {
            status_code = 403;
        }
        switch (status_code) {
            case 200:
                this.status = HttpStatus.OK;
                break;
            case 400:
                this.status = HttpStatus.BAD_REQUEST;
                break;
            case 401:
                this.status = HttpStatus.UNAUTHORIZED;
                break;
            case 403:
                this.status = HttpStatus.FORBIDDEN;
                break;
            case 500:
                this.status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }
        this.status_code = status_code;
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        if (message.contains(".")) {
            this.messages.add(Translator.toLocale(message, null));
        } else {
            switch (message) {
                case "Access is denied":
                    this.messages.add(Translator.toLocale("oauth.access.denied", null));
                    break;
                case "Access token expired":
                    this.messages.add(Translator.toLocale("oauth.access.token.expired", null));
                    break;
                case "Bad credentials":
                    this.messages.add(Translator.toLocale("oauth.access.bad.credentials", null));
                    break;
                case "Full authentication is required to access this resource":
                    this.messages.add(Translator.toLocale("oauth.authentication.required", null));
                    break;
                default:
                    this.messages.add(message);
                    break;
            }
        }
    }

    public AppResponse(int status_code, List<String> messages) {
        switch (status_code) {
            case 200:
                this.status = HttpStatus.OK;
                break;
            case 400:
                this.status = HttpStatus.BAD_REQUEST;
                break;
            case 401:
                this.status = HttpStatus.UNAUTHORIZED;
                break;
            case 403:
                this.status = HttpStatus.FORBIDDEN;
                break;
            case 500:
                this.status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }
        this.status_code = status_code;
        this.messages = messages;
    }
}
