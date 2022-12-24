package com.demo.springboot.app.models.service;

import com.demo.springboot.app.models.dao.IUsuarioDao;
import com.demo.springboot.app.models.entity.Role;
import com.demo.springboot.app.models.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {
    @Autowired
    private IUsuarioDao usuarioDao;

    private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);
        if(usuario == null){
            logger.info("Error: No existe el usuario " + username);
            throw new UsernameNotFoundException("Usuario " + username + "no existe");
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(Role role: usuario.getRoles()){
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        if(authorities.isEmpty()){
            logger.info("Error: " + username + "no tiene roles");
            throw new UsernameNotFoundException(username + "no tiene roles");
        }

        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);

    }
}
