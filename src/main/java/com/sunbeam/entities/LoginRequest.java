package com.sunbeam.entities;

import org.springframework.stereotype.Component;
import lombok.Data;

// This class seems redundant with com.sunbeam.request.LoginRequest. 
// It's better to use the one in the 'request' package. 
// Keeping it for now to avoid breaking existing (though flawed) logic you provided.
@Component
@Data
public class LoginRequest {
    private String email;
    private String password;
}