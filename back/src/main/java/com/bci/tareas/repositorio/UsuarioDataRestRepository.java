package com.bci.tareas.repositorio;

import com.bci.tareas.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioDataRestRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByRut(Long rut);

    public List<Usuario> findAll();

    public void deleteById(Long rut);
    @Query("from Usuario u where u.rut=:rut and u.password=:password and activo=1")
    public List<Usuario> findUserPwd(@Param("rut") Long rut, String password);

    @Query("from Usuario u where u.email=:email")
    public List<Usuario> findEmail(@Param("email") String email);


}