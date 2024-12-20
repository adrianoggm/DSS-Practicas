package com.dss.spring.data.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dss.spring.data.rest.Order;

public interface OrderRepo extends JpaRepository<Order, Long> {
    // Métodos adicionales si son necesarios
}

