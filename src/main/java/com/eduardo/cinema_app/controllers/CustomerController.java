package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.AuthenticationDTO;
import com.eduardo.cinema_app.dtos.request.RegisterDTO;
import com.eduardo.cinema_app.dtos.response.LoginResponseDTO;
import com.eduardo.cinema_app.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class CustomerController {

    private final AuthenticationService service;

    public CustomerController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody AuthenticationDTO authenticationDTO) {
        String token = service.login(authenticationDTO);
        return ResponseEntity.ok().body(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO dto) {
        service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
