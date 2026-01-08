package br.com.fiap.feedback.api.controller;

import br.com.fiap.feedback.api.dto.UsuarioResponseDTO;
import br.com.fiap.feedback.application.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PublicController {

    private UsuarioService service;

    public PublicController(UsuarioService s){
        this.service =s;
    }

    @GetMapping("/public/health")
    public ResponseEntity<String> checkAppHealth() {

        return ResponseEntity.status(HttpStatus.OK).body("OK - Rodando!");
    }

    @GetMapping("/public/usuarios")
    public ResponseEntity<String> getUsuarios() {
        List<UsuarioResponseDTO> allUsuarios = service.getAllUsuarios();
        return ResponseEntity.status(HttpStatus.OK).body("OK - usuarios! <"+allUsuarios.size()+">");
    }

}
