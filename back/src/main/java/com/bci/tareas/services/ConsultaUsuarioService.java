package com.bci.tareas.services;

import java.util.List;

import com.bci.tareas.dto.RespuestaDTO;
import com.bci.tareas.dto.UsuarioDTO;
import com.bci.tareas.model.Usuario;




public interface  ConsultaUsuarioService {

	public UsuarioDTO findUsuario(Long id) ;
	public List<Usuario> findEmail(String email) ;
	public List<UsuarioDTO> findAllUsuario() ;

}
