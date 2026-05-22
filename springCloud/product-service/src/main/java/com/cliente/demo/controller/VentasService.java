package com.cliente.demo.controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ventas")
public class VentasService {
    @GetMapping("/test")
    public String test() {
        return "El microservicio de ventas estan ok " ;
    }


}
