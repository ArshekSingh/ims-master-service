package com.sts.ims.controller;

import com.sts.ims.request.LoginRequest;
import com.sts.ims.request.RegisterRequest;
import com.sts.ims.exception.ObjectNotFoundException;
import com.sts.ims.response.Response;
import com.sts.ims.service.serviceImpl.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        log.info("Request initiated to register with details {}", request);
        Response response = service.register(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) throws ObjectNotFoundException {
        log.info("Request initiated to login by user {}", request);
        Response response = service.login(request);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
