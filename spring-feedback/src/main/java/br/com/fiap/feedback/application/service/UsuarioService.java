package br.com.fiap.feedback.application.service;

import br.com.fiap.feedback.api.dto.*;
import br.com.fiap.gh.jpa.entities.UsuarioEntity;
import br.com.fiap.gh.jpa.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final PerfilService perfilService;
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(PerfilService perfilService, UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.perfilService = perfilService;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponseDTO> getAllUsuarios() {

        var usuarios = repository.findAll();

        return usuarios.stream()
                .map(u -> new UsuarioResponseDTO(
                        u.getId(),
                        u.getNome(),
                        u.getLogin(),
                        u.getEmail(),
                        u.getPerfis().stream().map(p -> p.getPerfil().getDescricao()).collect(Collectors.toSet())
                )).toList();
    }


    public UsuarioResponseDTO getUsuarioById(Long usuarioId) {

        var user = repository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return new UsuarioResponseDTO(
                user.getId(),
                user.getNome(),
                user.getLogin(),
                user.getEmail(),
                user.getPerfis().stream().map(p -> p.getPerfil().getDescricao()).collect(Collectors.toSet())
        );
    }

    public UsuarioResponseDTO cadastrarUsuario(UsuarioInsertDTO usuarioDTO) {
        var entidadeUsuario = convertToEntity(usuarioDTO);
        entidadeUsuario.setSenha(passwordEncoder.encode(usuarioDTO.senha()));
        repository.save(entidadeUsuario);

        usuarioDTO.perfis().forEach( descricao -> {
            var perfil = perfilService.buscarPerfilPorNome(descricao);
            entidadeUsuario.addPerfil(perfil);
        });

        repository.save(entidadeUsuario);

        return UsuarioResponseDTO.create(entidadeUsuario);
    }

    public UsuarioResponseDTO atualizarUsuario(UsuarioUpdateDTO usuarioDTO, Long usuarioId) {
        var usuario = repository.findById(usuarioId).orElseThrow(()
                -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(usuarioDTO.nome());
        usuario.setLogin(usuarioDTO.login());
        usuario.setEmail(usuarioDTO.email());

        repository.save(usuario);

        return UsuarioResponseDTO.create(usuario);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public void mudarSenha(MudarSenhaDTO mudarSenhaDTO, Long usuarioId) {
        var usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(mudarSenhaDTO.senhaAntiga(), usuario.getSenha())) {
            throw new RuntimeException("Senha antiga incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(mudarSenhaDTO.senhaNova()));
        repository.save(usuario);
    }


    private UsuarioEntity convertToEntity(UsuarioInsertDTO usuarioDTO) {

        var usuario =  new UsuarioEntity();
        usuario.setNome(usuarioDTO.nome());
        usuario.setLogin(usuarioDTO.login());
        usuario.setEmail(usuarioDTO.email());
        usuario.setSenha(usuarioDTO.senha());
        return usuario;
    }

    public List<PerfilDTO> buscarPerfis(Long usuarioId) {
        var usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));

        return usuario.getPerfis().stream()
                .map( up -> PerfilDTO.create(up.getPerfil()))
                .toList();
    }

    public void adicionarPerfis(Long usuarioId, Set<String> perfis) {

        var usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));

        perfis.forEach( descricao -> {
            var perfil = perfilService.buscarPerfilPorNome(descricao);
            usuario.addPerfil(perfil);
        });

        repository.save(usuario);
    }

    public void removerPerfis(Long usuarioId, Set<String> perfis) {
        var usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));

        perfis.forEach( descricao -> {
            var perfil = perfilService.buscarPerfilPorNome(descricao);
            usuario.removerPerfil(perfil);
        });

        repository.save(usuario);
    }

    public UsuarioEntity getUsuarioByLogin(String login) {
        return repository.findByLogin(login).orElse(
                 null
        );
    }
}
