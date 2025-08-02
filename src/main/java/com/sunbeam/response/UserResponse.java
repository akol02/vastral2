package com.sunbeam.response;

// Note: This class is very similar to Response.java and could be consolidated.
// Kept as is to match the original structure you provided.
public class UserResponse<T> {
    private AppStatus status;
    private T data;
    private String message;

    public UserResponse(AppStatus status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public UserResponse() {}

    public AppStatus getStatus() { return status; }
    public void setStatus(AppStatus status) { this.status = status; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static <T> UserResponse<T> success(T data) {
        return new UserResponse<>(AppStatus.SUCCESS, data, null);
    }

    public static <T> UserResponse<T> success(T data, String message) {
        return new UserResponse<>(AppStatus.SUCCESS, data, message);
    }

    public static <T> UserResponse<T> error(String message) {
        return new UserResponse<>(AppStatus.ERROR, null, message);
    }
}