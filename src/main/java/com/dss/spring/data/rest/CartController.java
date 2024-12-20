package com.dss.spring.data.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Mostrar el contenido del carrito
    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        return "cart"; // Renderiza cart.html
    }

    // Añadir un producto al carrito
    @PostMapping("/add/{id}")
    public String addProductToCart(@PathVariable Long id,RedirectAttributes redirectAttributes) {
        Product product = productService.getProductById(id);
        if (product != null) {
            cartService.addProductToCart(product);
            redirectAttributes.addFlashAttribute("message", product.getName() + " ha sido añadido a su carrito");
        }
        return "redirect:/products"; // Redirige al carrito después de añadir un producto
    }

    // Eliminar un producto del carrito
    @PostMapping("/remove/{id}")
    public String removeProductFromCart(@PathVariable Long id) {
        cartService.removeProductFromCart(id);
        return "redirect:/cart"; // Redirige al carrito después de eliminar un producto
    }

    @PostMapping("/pay")
    public String payCart(Authentication authentication, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
        String username = authentication.getName();
        List<Product> cartItems = cartService.getCartItems(); // Obtener los productos del carrito

        boolean paymentSuccess = cartService.processPayment(username);

        if (paymentSuccess) {
            generatePdf(cartItems, response); // Generar la factura si el pago es exitoso
            cartService.clearCart(); // Vaciar el carrito después del pago
            redirectAttributes.addFlashAttribute("message", "Pago realizado con éxito. Se ha generado la factura.");
            return "redirect:/products?reload=true";
        } else {
           
            return "redirect:/products";
        }
    }

    // Método para generar la factura en PDF
    private void generatePdf(List<Product> cartItems, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=factura.pdf");

        PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        // Añadir título
        Paragraph title = new Paragraph("Factura de Compra")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        document.add(new Paragraph("Fecha: " + java.time.LocalDate.now()).setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("\n"));

        // Crear tabla para los productos
        Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2}))
                .useAllAvailableWidth();

        // Encabezados de la tabla
        table.addHeaderCell(new Cell().add(new Paragraph("Producto").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Precio").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        double totalAmount = 0.0;

        // Rellenar la tabla con los productos
        for (Product product : cartItems) {
            table.addCell(new Cell().add(new Paragraph(product.getName())));
            table.addCell(new Cell().add(new Paragraph(String.format("$%.2f", product.getPrice()))));
            totalAmount += product.getPrice();
        }

        document.add(table);
        document.add(new Paragraph("\n"));

        // Mostrar el total
        Paragraph total = new Paragraph(String.format("Total: $%.2f", totalAmount))
                .setBold()
                .setFontSize(14)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(total);

        document.add(new Paragraph("\nGracias por tu compra!").setTextAlignment(TextAlignment.CENTER));

        document.close(); // Cerrar el documento
    }

}