package com.sunbeam.request;

import com.sunbeam.models.UserRole;
import lombok.Data;

@Data
public class LoginOtpRequest {
    private String email;
    private UserRole role;
}