package com.bci.tareas.controllers;

import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.exception.EmailYaExisteException;
import com.bci.tareas.exception.RecursoNoEncontradoException;
import com.bci.tareas.exception.RecursoYaExisteException;
import com.bci.tareas.helper.Constantes;
import com.bci.tareas.services.ConsultaUsuarioService;
import com.bci.tareas.services.RegistraUsuarioServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.UUID;


@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/registro/usuario")
@RequiredArgsConstructor
public class ControllerRegistation {


    private final RegistraUsuarioServices registraUsuarioServices;
    private final ConsultaUsuarioService consultaUsuarioService;
    private final PasswordEncoder passwordEncoder;
    DateTimeFormatter ZDT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z");
    private static final Logger logger = LoggerFactory.getLogger(ControllerRegistation.class);




    private void validarDuplicidad(UsuarioDTO usuarioDTO) {
        // Verificar si el UUID ya existe
    //    if (consultaUsuarioService.findUsuario(usuarioDTO.) != null) {
    //        throw new RecursoYaExisteException(Constantes.YA_EXISTE);
  //      }

        // Verificar si el Email ya existe

        if (!consultaUsuarioService.findEmail(usuarioDTO.getEmail()).isEmpty()) {
            throw new EmailYaExisteException(Constantes.EMAIL_EXIST);
        }

    }

    @Transactional
    public RespuestaDTO processSignUp(UsuarioDTO usuarioDTO) {
        // 1. Generar el UUID basado en el ID original
        logger.info("Id : {}", usuarioDTO.getRut());
      String passwordEncriptada = passwordEncoder.encode(usuarioDTO.getPassword());
     //   usuarioDTO.setId(uuid);
        usuarioDTO.setPassword(passwordEncriptada);
        // 2. Validaciones de Negocio (Duplicidad)
        validarDuplicidad(usuarioDTO);

        // 3. Preparar datos adicionales (como el teléfono)
        //if (usuarioDTO.getPhone() != null) {
   //         usuarioDTO.getPhone().setId(usuarioDTO.getId());
      //  }

        // 4. Guardar en la base de datos
        // Aquí llamarías a tu lógica de persistencia real
        return registraUsuarioServices.saveUsuario(usuarioDTO);
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RespuestaDTO> saveUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        logger.info("grabar tareas");

        RespuestaDTO response = processSignUp(usuarioDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

}
