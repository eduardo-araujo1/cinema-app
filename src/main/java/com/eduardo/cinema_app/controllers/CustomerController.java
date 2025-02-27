package com.eduardo.cinema_app.controllers;

import com.eduardo.cinema_app.dtos.request.AuthenticationDTO;
import com.eduardo.cinema_app.dtos.request.RegisterDTO;
import com.eduardo.cinema_app.dtos.response.LoginResponseDTO;
import com.eduardo.cinema_app.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth", description = "Gerencia a autenticação e registro de usuários.")
public class CustomerController {

    private final AuthenticationService service;

    public CustomerController(AuthenticationService service) {
        this.service = service;
    }

    @Operation(summary = "Realizar Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso. Retorna o token de autenticação."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@Valid @RequestBody AuthenticationDTO authenticationDTO) {
        var loginResponse = service.login(authenticationDTO);
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "Criar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Usuário com o email fornecido já existe"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDTO dto) {
        service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
