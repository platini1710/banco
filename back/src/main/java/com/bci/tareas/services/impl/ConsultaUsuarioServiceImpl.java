package com.bci.tareas.services.impl;

import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.model.Usuario;
import com.bci.tareas.repositorio.UsuarioDataRestRepository;
import com.bci.tareas.services.ConsultaUsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConsultaUsuarioServiceImpl  implements ConsultaUsuarioService {
    private UsuarioDataRestRepository usuarioDataRestRepository;

    @Override
    public UsuarioDTO findUsuario(Long id) {
        return null;
    }

    @Override
    public List<Usuario> findEmail(String email) {
        return List.of();
    }

    @Override
    public List<UsuarioDTO> findAllUsuario() {
        return List.of();
    }
}
