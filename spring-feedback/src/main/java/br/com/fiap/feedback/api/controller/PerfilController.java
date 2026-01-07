package br.com.fiap.feedback.api.controller;

import br.com.fiap.feedback.api.doc.PerfilDocController;
import br.com.fiap.feedback.api.dto.PerfilDTO;
import br.com.fiap.feedback.api.dto.PerfilPermissaoDTO;
import br.com.fiap.feedback.api.dto.PerfilPermissaoInsert;
import br.com.fiap.feedback.application.service.PerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/perfis")
@PreAuthorize("hasRole('PERFIL')")
public class PerfilController implements PerfilDocController {

    private final PerfilService service;

    public PerfilController(PerfilService service) {
        this.service = service;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PerfilDTO>> buscarTodos() {

        var dtos = service.getAllPerfis();
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @Override
    @GetMapping("/{perfilId}")
    public ResponseEntity<PerfilDTO> buscarPorId(@PathVariable Long perfilId) {

        PerfilDTO dto = service.buscarPerfilById(perfilId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping
    public ResponseEntity<PerfilDTO> cadastrar(@RequestBody String descricao){

        var dtoCriado = service.cadastrar(descricao);

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoCriado);
    }

    @Override
    @PutMapping("/{perfilId}")
    public ResponseEntity<PerfilDTO> atualizar(@PathVariable(required = true) Long perfilId, @RequestBody String descricao) {

        var dto = service.atualizarPerfil(perfilId, descricao);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Override
    @DeleteMapping("/{perfilId}")
    public ResponseEntity<String> deletar(@PathVariable Long perfilId) {

        service.deletarPerfil(perfilId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Perfil deletado com sucesso.");
    }

    @Override
    @GetMapping("/{perfilId}/permissoes")
    public ResponseEntity<List<PerfilPermissaoDTO>> listarPermissoes(
            @PathVariable(required = true) Long perfilId) {

        var permissoes =  service.buscarPermissoes(perfilId);
        return ResponseEntity.status(HttpStatus.OK).body(permissoes);
    }

    @Override
    @PostMapping("/{perfilId}/permissoes")
    public ResponseEntity<Void> adicionarPermissoes(
            @PathVariable(required = true) Long perfilId,
            @RequestBody Set<PerfilPermissaoInsert> permissoes) {

        service.adicionarPermissoes(perfilId, permissoes);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @DeleteMapping("/{perfilId}/permissoes")
    public ResponseEntity<Void> removerPermissoes(
            @PathVariable(required = true)  Long perfilId,
            @RequestBody Set<String> permissoes) {
        service.removerPermissoes(perfilId, permissoes);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
