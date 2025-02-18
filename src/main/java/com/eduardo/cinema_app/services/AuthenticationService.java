package com.eduardo.cinema_app.services;

import com.eduardo.cinema_app.config.TokenService;
import com.eduardo.cinema_app.domain.Customer;
import com.eduardo.cinema_app.dtos.request.AuthenticationDTO;
import com.eduardo.cinema_app.dtos.request.RegisterDTO;
import com.eduardo.cinema_app.exceptions.AuthenticationFailureException;
import com.eduardo.cinema_app.exceptions.UserAlreadyExistsException;
import com.eduardo.cinema_app.repositories.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(CustomerRepository customerRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Customer loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findByEmail(email);
    }

    public String login(AuthenticationDTO authenticationDTO) {
        var customer = loadUserByUsername(authenticationDTO.email());

        if (customer == null || !passwordEncoder.matches(authenticationDTO.password(), customer.getPassword())) {
            throw new AuthenticationFailureException("Email ou senha inválidos.");
        }
        return tokenService.generateToken(customer);
    }

    public void register(RegisterDTO registerDTO) {
        String email = registerDTO.email();
        if (customerRepository.findByEmail(email) != null) {
            throw new UserAlreadyExistsException("O usuário com o e-mail" + registerDTO.email() + " já existe.");
        }
        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        var newUser = new Customer(registerDTO.name(), email, encryptedPassword);
        customerRepository.save(newUser);
    }
}

