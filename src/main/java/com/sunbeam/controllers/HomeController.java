package com.sunbeam.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sunbeam.response.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity<ApiResponse> home() {
        ApiResponse apiResponse = new ApiResponse("E-commerce multi-vendor system", true);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

	
}