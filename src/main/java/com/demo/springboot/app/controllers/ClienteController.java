package com.demo.springboot.app.controllers;

import com.demo.springboot.app.models.entity.Cliente;
import com.demo.springboot.app.models.service.IClienteService;
import com.demo.springboot.app.models.service.UploadFileServiceImpl;
import com.demo.springboot.app.util.paginator.PageRender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.Collection;
import java.util.Map;

@Controller
public class ClienteController {

    @Autowired
    private IClienteService clienteService;
    @Autowired
    private UploadFileServiceImpl uploadFileService;

//    @Secured("ROLE_USER")
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename){
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                .body(recurso);
    }

//    @Secured("ROLE_USER")
    @GetMapping(value = "/ver/{id}")
    public String verDetalle(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash){
        Cliente cliente = clienteService.findOne(id);
        if(cliente==null){
            flash.addFlashAttribute("error", "El cliente no existe");
            return "redirect:/listar";

        }
        model.put("cliente", cliente);
        model.put("titulo", "Detalle cliente " + cliente.getNombre());
        return "ver";
    }
    @GetMapping(value = {"/listar", "/"})
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        Pageable pageRequest = (Pageable) PageRequest.of(page, 7);
        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de cliente");

        return "form";
    }
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam("file")MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
        if(result.hasErrors()){
            model.addAttribute("titulo", "Formulario de cliente");
            return "form";
        }
        if(!foto.isEmpty()){
            if(cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0){
                uploadFileService.delete(cliente.getFoto());
            }
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(foto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            flash.addFlashAttribute("info", "Se ha cargado correctamente '" + uniqueFilename + "'");
            cliente.setFoto(uniqueFilename);


        }
        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito" : "Cliente creado con éxito";
        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash){
        Cliente cliente = null;
        if(id>0){
            cliente = clienteService.findOne(id);
            //System.out.println("El clientes es: " + cliente);
            if(cliente == null){
                flash.addFlashAttribute("error", "El cliente no existe");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID no puede ser cero");
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        return "form";
    }
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash){
        if(id>0){
            Cliente cliente = clienteService.findOne(id);
            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con éxito");
            if(cliente.getFoto() != null && uploadFileService.delete(cliente.getFoto())){
                flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con éxito");
            }
        }
        return "redirect:/listar";
    }

    private boolean hasRole(String role){
        SecurityContext context = SecurityContextHolder.getContext();
        if(context == null){
            return false;
        }
        Authentication auth = context.getAuthentication();
        if(auth == null){
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for(GrantedAuthority authority: authorities){
            if(role.equals(authority.getAuthority())){
                return true;
            }
        }
        return false;
    }
}

