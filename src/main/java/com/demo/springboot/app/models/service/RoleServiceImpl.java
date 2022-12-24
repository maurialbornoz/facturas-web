package com.demo.springboot.app.models.service;

import com.demo.springboot.app.models.dao.IRoleDao;
import com.demo.springboot.app.models.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements IRoleService{

    @Autowired
    private IRoleDao roleDao;
    @Override
    @Transactional
    public void save(Role role) {
        roleDao.save(role);
    }
}
