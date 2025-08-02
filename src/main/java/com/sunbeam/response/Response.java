package com.sunbeam.response;

enum AppStatus {
    SUCCESS, ERROR
}

public class Response<T> {
    private AppStatus status;
    private T data;
    private String message;

    public Response(AppStatus status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public AppStatus getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(AppStatus.SUCCESS, data, null);
    }

    public static <T> Response<T> success(T data, String message) {
        return new Response<>(AppStatus.SUCCESS, data, message);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(AppStatus.ERROR, null, message);
    }
}