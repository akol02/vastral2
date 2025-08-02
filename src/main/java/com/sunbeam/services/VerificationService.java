package com.sunbeam.services;

import com.sunbeam.entities.VerificationCode;

public interface VerificationService {
    VerificationCode createVerificationCode(String otp, String email);
}