package com.cliente.demo.controller;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    // Esta anotación busca la clave "mensaje" en tu archivo product-service.yml de GitHub
    @Value("${mensaje:Mensaje por defecto si no encuentra el de GitHub}")
    private String mensaje;

    @GetMapping("/test")
    public String test() {
        return "El microservicio de productos dice: " + mensaje;
    }

    @GetMapping("/status")
    public String status() {
        return "Servicio de productos operando correctamente";
    }
}
