package com.eduardo.cinema_app.service;

import com.eduardo.cinema_app.config.TokenService;
import com.eduardo.cinema_app.domain.Customer;
import com.eduardo.cinema_app.dtos.request.AuthenticationDTO;
import com.eduardo.cinema_app.dtos.request.RegisterDTO;
import com.eduardo.cinema_app.enums.Role;
import com.eduardo.cinema_app.exceptions.AuthenticationFailureException;
import com.eduardo.cinema_app.exceptions.UserAlreadyExistsException;
import com.eduardo.cinema_app.repositories.CustomerRepository;
import com.eduardo.cinema_app.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void testLogin_SuccessfulAuthentication() {
        String name = "Eduardo teste";
        String email = "edu@teste.com.br";
        String password = "!Edusenha123";
        String encodedPassword = "encodedPassword";
        LocalDateTime createdCustomer = LocalDateTime.now();

        Customer customer = new Customer(1L, name, email, password, Role.ROLE_USER, createdCustomer);

        when(customerRepository.findByEmail(email)).thenReturn(customer);
        when(passwordEncoder.matches(password, customer.getPassword())).thenReturn(true);
        when(tokenService.generateToken(any(Customer.class))).thenReturn("generatedToken");

        String token = authenticationService.login(new AuthenticationDTO(email, password));

        assertNotNull(token);
        verify(tokenService).generateToken(customer);
    }

    @Test
    void testLogin_InvalidCredentials() {
        String email = "test@example.com";
        String password = "!Edusenha123";
        Customer customer = null;

        when(customerRepository.findByEmail(email)).thenReturn(customer);

        assertThrows(AuthenticationFailureException.class, () -> {
            authenticationService.login(new AuthenticationDTO(email, password));
        });
    }

    @Test
    void testRegister_NewUser() {
        RegisterDTO registerDTO = new RegisterDTO("Eduardo", "edu@teste.com", "!Edusenha123");

        when(customerRepository.findByEmail(registerDTO.email())).thenReturn(null);
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("encodedpassword");
        authenticationService.register(registerDTO);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());

        Customer savedCustomer = captor.getValue();

        assertEquals(registerDTO.name(), savedCustomer.getName());
        assertEquals(registerDTO.email(), savedCustomer.getEmail());
        assertEquals("encodedpassword", savedCustomer.getPassword());
    }

    @Test
    void testRegister_UserAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO("Teste", "teste@example.com", "!Password123");

        when(customerRepository.findByEmail(registerDTO.email())).thenReturn(new Customer());

        assertThrows(UserAlreadyExistsException.class, () ->{
            authenticationService.register(registerDTO);
        });

        verify(customerRepository, never()).save(any(Customer.class));
    }
}
