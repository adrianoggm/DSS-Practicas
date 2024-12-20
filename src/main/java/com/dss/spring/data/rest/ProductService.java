package com.dss.spring.data.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service 
public class ProductService {

    @Autowired
    private ProductRepo productRepository;

    // Obtener todos los productos
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Obtener producto por ID
    public Product getProductById(Long id) {
    	return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Guardar o actualizar producto
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    //Obtener los productos por nombre
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // Eliminar producto por ID
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public String exportProductsToSQL() {
        List<Product> products = productRepository.findAll(); // Obtener todos los productos
        StringBuilder sqlBuilder = new StringBuilder();

        // Generar script SQL para cada producto
        for (Product product : products) {
            String sql = String.format("INSERT INTO products (id, name, price) VALUES (%d, '%s', %.2f);\n",
                    product.getId(), product.getName().replace("'", "''"), product.getPrice());
            sqlBuilder.append(sql);
        }

        return sqlBuilder.toString(); // Devolver el script SQL
    }

    // Método para guardar el script SQL en un archivo
    public void saveSQLToFile(String sql, String filename) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(sql); // Escribir el script en el archivo
        }
    }
}