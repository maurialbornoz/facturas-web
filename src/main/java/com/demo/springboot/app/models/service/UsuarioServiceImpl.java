package com.demo.springboot.app.models.service;

import com.demo.springboot.app.models.dao.IUsuarioDao;
import com.demo.springboot.app.models.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    @Transactional
    public void save(Usuario usuario) {
        usuarioDao.save(usuario);

    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findOne(Long id) {
        return usuarioDao.findById(id).orElse(null);
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioDao.findByEmail(email).orElse(null);
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioDao.findByUsername(username);
    }
}
