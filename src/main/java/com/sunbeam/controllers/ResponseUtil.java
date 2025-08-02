package com.sunbeam.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@Data
public class ResponseUtil<T> {
    private String status;
    private String message;
    private T data;

    public static <T> ResponseUtil<T> apiSuccess(T data) {
        return new ResponseUtil<>("success", null, data);
    }

    public static <T> ResponseUtil<T> apiError(String message) {
        return new ResponseUtil<>("error", message, null);
    }
}