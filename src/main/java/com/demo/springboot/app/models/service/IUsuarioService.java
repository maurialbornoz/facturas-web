package com.demo.springboot.app.models.service;

import com.demo.springboot.app.models.entity.Usuario;

public interface IUsuarioService {

    public void save(Usuario usuario);

    public Usuario findOne(Long id);

    public Usuario findByEmail(String email);

    public Usuario findByUsername(String username);
}
