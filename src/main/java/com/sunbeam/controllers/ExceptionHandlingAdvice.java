package com.sunbeam.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingAdvice {
    @ExceptionHandler(Throwable.class)
    public ResponseUtil<?> handleException(Throwable ex) {
        return ResponseUtil.apiError(ex.getMessage());
    }
}