package com.incentro.myservice.application.exception;


import com.incentro.myservice.application.service.Translator;

import java.util.Collections;
import java.util.Map;

public class AppInputErrorException extends IllegalArgumentException {

    public AppInputErrorException() {
    }

    private Integer objectIndex;

    public AppInputErrorException(String s, Object... args) {
        super(Translator.toLocale(s, args));
    }

    public AppInputErrorException(Integer objectIndex, String s, Object... args) {
        super(Translator.toLocale(s, args));
        this.objectIndex = objectIndex;
    }

    public AppInputErrorException(String message, Throwable cause, Object... args) {
        super(Translator.toLocale(message, args), cause);
    }

    public AppInputErrorException(Throwable cause) {
        super(cause);
    }

    public Map<String, Integer> getData() {
        if (this.objectIndex != null) {
            return Collections.singletonMap("index", objectIndex);
        }
        return null;
    }
}
