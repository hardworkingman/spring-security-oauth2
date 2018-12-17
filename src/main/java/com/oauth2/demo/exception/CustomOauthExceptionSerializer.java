package com.oauth2.demo.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class CustomOauthExceptionSerializer extends StdSerializer<CustomOauthException> {
    private static final long serialVersionUID = 14950419187441467L;

    public CustomOauthExceptionSerializer() {
        super(CustomOauthException.class);
    }

    @Override
    public void serialize(CustomOauthException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        log.warn("CustomOauthException = " + value);
        gen.writeStartObject();
        gen.writeNumberField("code", value.getHttpErrorCode());
        gen.writeStringField("message", value.getMessage());
//        gen.writeNullField("data");
        if (value.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                log.warn("key = " + key);
                log.warn("add = " + add);
                gen.writeStringField(key, add);
            }
        }
        gen.writeEndObject();
    }
}

