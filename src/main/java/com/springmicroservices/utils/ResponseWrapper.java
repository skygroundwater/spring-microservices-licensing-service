package com.springmicroservices.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class ResponseWrapper {

    private Object data;
    private Object metadata;
    private List<ErrorMessage> errors;

    public ResponseWrapper(Object data, Object metadata, List<ErrorMessage> errors) {
        super();
        this.data = data;
        this.metadata = metadata;
        this.errors = errors;
    }
}