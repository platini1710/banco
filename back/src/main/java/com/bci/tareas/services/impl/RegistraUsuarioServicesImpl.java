package com.bci.tareas.services.impl;

import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.repositorio.PhoneDataRestRepository;
import com.bci.tareas.repositorio.UsuarioDataRestRepository;
import com.bci.tareas.services.RegistraUsuarioServices;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistraUsuarioServicesImpl implements RegistraUsuarioServices {

	private UsuarioDataRestRepository usuarioDataRestRepository;

	private PhoneDataRestRepository phoneDataRestRepository;
	private static final Logger logger = LoggerFactory.getLogger(RegistraUsuarioServicesImpl.class);

    @Override
    public RespuestaDTO saveUsuario(UsuarioDTO usuario) {
        return null;
    }
}