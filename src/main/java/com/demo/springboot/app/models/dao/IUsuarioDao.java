package com.demo.springboot.app.models.dao;

import com.demo.springboot.app.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
    public Usuario findByUsername(String username);

    public Optional<Usuario> findByEmail(String email);
}
