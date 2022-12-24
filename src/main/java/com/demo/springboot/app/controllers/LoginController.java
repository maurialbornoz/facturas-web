package com.demo.springboot.app.controllers;

import com.demo.springboot.app.models.entity.Role;
import com.demo.springboot.app.models.entity.Usuario;
import com.demo.springboot.app.models.service.IRoleService;
import com.demo.springboot.app.models.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRoleService roleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, Model model, Principal principal, RedirectAttributes flash){
        if(principal != null){ // ya se inició sesión
            flash.addFlashAttribute("info", "Ya ha iniciado sesión");
            return "redirect:/";
        }
        if(error != null){
            model.addAttribute("error", "Credenciales inválidas");
        }
        if(logout != null){
            model.addAttribute("success", "Sesión cerrada");
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid Usuario usuario, RedirectAttributes flash, Model model){
        if(usuarioService.findByEmail(usuario.getEmail()) != null){
            flash.addFlashAttribute("error", "El email ya esta registrado");
            return "redirect:/register";
        }
        if(usuarioService.findByUsername(usuario.getUsername()) != null){
            flash.addFlashAttribute("error", "El nombre de usuario ya está registrado");
            return "redirect:/register";
        }

        usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
        usuario.setEnabled(true);
        List<Role> roles = new ArrayList<Role>();
        Role role = new Role();
        role.setAuthority("ROLE_ADMIN");
        roles.add(role);
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        model.addAttribute("success", "Cuenta creada con éxito. Inicie sesión.");
        return "login";
    }
}
