package com.dss.spring.data.rest;


import org.springframework.stereotype.Service;
import com.dss.spring.data.rest.Order;
import com.dss.spring.data.rest.OrderItem;
import com.dss.spring.data.rest.OrderRepo;
import com.dss.spring.data.rest.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepository;

    public OrderServiceImpl(OrderRepo orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        Double totalAmount = 0.0;
        List<OrderItem> items = order.getOrderItems();

        for (OrderItem item : items) {
            Integer quantity = item.getQuantity();
            if (quantity == null) {
                quantity = 1; // Valor por defecto si es null
                item.setQuantity(quantity);
            }

            totalAmount += item.getProductPrice() * quantity;
            item.setOrder(order);
        }

        order.setTotalAmount(totalAmount);
        order.setStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada: " + id));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll(); // Recupera todas las órdenes
    }
}
