package com.dss.spring.data.rest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class RootUriController {

    // Maneja la ruta raíz "/"
    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "index"; // Devuelve index.html desde la carpeta templates
    }
}
