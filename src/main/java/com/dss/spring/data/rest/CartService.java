package com.dss.spring.data.rest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private List<Product> cartItems = new ArrayList<>();

    // Obtener todos los productos del carrito
    public List<Product> getCartItems() {
        return new ArrayList<>(cartItems); // Retornar una copia de la lista
    }

    // Añadir un producto al carrito
    public void addProductToCart(Product product) {
        cartItems.add(product);
    }

    // Eliminar un producto del carrito por su ID
    public void removeProductFromCart(Long productId) {
        cartItems.removeIf(product -> product.getId().equals(productId));
    }

    // Vaciar el carrito (opcional)
    public void clearCart() {
        cartItems.clear();
    }



	public boolean processPayment(String username) {
		// TODO Auto-generated method stub
		return true;   //Como no gestionamos ningun tipo de bd de usuarios con su "Dinero simulamos su comportamiento siempre pudiendo pagar.
	}
}