package com.salary.plus.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.web.rest.errors.BadRequestAlertException;
import java.net.http.HttpResponse;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ObjectMapperUtil {

    private final ObjectMapper objectMapper;

    public ObjectMapperUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> String toRequestBody(T requestDto) {
        return toJson(requestDto);
    }

    public <T> String toJson(T requestDto) {
        try {
            return objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            log.error("toJson error - {}", e.getMessage(), e);
            throw new BadRequestAlertException("Not found user", "userManagement", "idexists");
        }
    }

    public <T> String toJsonInUpperCase(T requestDto) {
        return toJson(requestDto).toUpperCase();
    }

    public <T> T toResponse(HttpResponse<String> response, Class<T> clazz) throws JsonProcessingException {
        return toResponse(response.body(), clazz);
    }

    public <T> T toResponse(String body, Class<T> clazz) throws JsonProcessingException {
        return readValue(body, clazz);
    }

    public <T> T readValue(String body, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(body, clazz);
    }

    public <T> T readValue(String body, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        return objectMapper.readValue(body, valueTypeRef);
    }

    public <T> T convertValue(Map<String, Object> data, Class<T> resultClass) {
        return objectMapper.convertValue(data, resultClass);
    }
}
