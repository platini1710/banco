package com.bci.tareas.controllers;

import com.bci.tareas.dto.Login;
import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.security.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/consulta/usuario")


public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager,JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> login(@RequestBody Login request) {
// 1. El AuthenticationManager recibe usuario y pwd
        logger.info("rut ==> " +request.rut());
        logger.info("pwd " +   request.password());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.rut(),
                        request.password()
                )
        );
        logger.info("salio de aca",2);
        // Si la ejecución llega aquí, significa que el usuario existe en la BD
        // 2. Si existe y es válido, se genera el JWT usando el JwtService
        RespuestaDTO response=new RespuestaDTO();
        response.setToken(jwtService.generateToken(authentication.getName()));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }








}
