package com.demo.springboot.app.models.dao;

import com.demo.springboot.app.models.entity.Cliente;
import com.demo.springboot.app.models.entity.Role;
import com.demo.springboot.app.models.entity.Usuario;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IRoleDao extends PagingAndSortingRepository<Cliente, Long> {
    public void save(Role role);
}
