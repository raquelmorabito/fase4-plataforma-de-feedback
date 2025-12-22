package br.com.fiap.feedback.application.service;

import br.com.fiap.gh.jpa.entities.UsuarioEntity;
import br.com.fiap.gh.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthenticationService(AuthenticationManager authManager, JwtService jwtService, UsuarioService usuarioService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    public String authenticate(String username, String password) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String jwt = jwtService.generateToken((UserDetails) auth.getPrincipal());

        return jwt;
    }

    public String getAuthUsername() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            // em alguns casos principal é só o nome
            return principal.toString();
        }
    }

    public UsuarioEntity getAuthenticatedUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            return usuarioService.getUsuarioByLogin( ((UserDetails) principal).getUsername() );
        } else {
            // em alguns casos principal é só o nome
            if(principal != null)
                return usuarioService.getUsuarioByLogin(principal.toString());
        }

        return null;
    }

    public String generateNewToken(String username) {
        return jwtService.generateToken(username);
    }

//    public void validarToken(String authorization) {
//        try {
//            String token = authorization.replace("Bearer ", "");
//            jwtService.isTokenValid(authorization, null);
//        } catch (JwtException e) {
//            throw new JwtException("Token inválido ou expirado");
//        }
//    }
}
