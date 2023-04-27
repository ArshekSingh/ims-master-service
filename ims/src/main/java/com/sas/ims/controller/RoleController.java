package com.sas.ims.controller;

import com.sas.ims.service.RoleService;
import com.sas.tokenlib.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/getRoles")
    public ResponseEntity<Object> getRoles() {
        Response response = roleService.getRoles();
        return new ResponseEntity<>(response, response.getStatus());
    }
}
