package com.eduardo.cinema_app.repositories;

import com.eduardo.cinema_app.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);
}
