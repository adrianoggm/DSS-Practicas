package com.dss.spring.data.rest;

import com.dss.spring.data.rest.Order;
import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrderById(Long id);
    List<Order> getAllOrders();
    // Otros métodos si son necesarios
}
