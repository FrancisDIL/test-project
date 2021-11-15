package com.incentro.myservice.application.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.incentro.myservice.application.model.AppResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomOauthExceptionSerializer extends StdSerializer<CustomOauthException> {

    public CustomOauthExceptionSerializer() {
        super(CustomOauthException.class);
    }

    @Override
    public void serialize(CustomOauthException value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<String> messages = new ArrayList<>();
        messages.addAll(Arrays.asList(value.getOAuth2ErrorCode(), value.getMessage()));
        messages.remove("invalid_request");
        AppResponse appResponse = new AppResponse(value.getHttpErrorCode(), messages);
        if (value.getAdditionalInformation() != null) {
            appResponse.setData(value.getAdditionalInformation());
        }
        jsonGenerator.writeObject(appResponse);
    }
}
