package com.dss.spring.data.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;
    

    @GetMapping
    public String showAdminPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin"; // Asegúrate de que existe `admin.html`
    }
    @GetMapping("/download-db-sql")
    public ResponseEntity<byte[]> exportProducts() {
        String sqlScript = productService.exportProductsToSQL(); // Exportar productos a SQL
        String filename = "products_export.sql";

        // Guardar el archivo SQL en el sistema de archivos
        try {
        	productService.saveSQLToFile(sqlScript, filename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al guardar el archivo: " + e.getMessage()).getBytes());
        }

        // Preparar la respuesta para descargar el archivo
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/sql");

        return new ResponseEntity<>(sqlScript.getBytes(), headers, HttpStatus.OK);
    }
}