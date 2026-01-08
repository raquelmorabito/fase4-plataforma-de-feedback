package br.com.fiap.gh.security;

import br.com.fiap.gh.jpa.entities.UsuarioEntity;
import br.com.fiap.gh.jpa.entities.UsuarioPerfilEntity;
import br.com.fiap.gh.jpa.entities.PerfilPermissaoEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDetailsCustom implements UserDetails {

    UsuarioEntity usuario;

    public UserDetailsCustom(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        Set<UsuarioPerfilEntity> perfis = usuario.getPerfis();

        for (UsuarioPerfilEntity perfil : perfis) {

            var perfilDescricao = perfil.getPerfil().getDescricao().toUpperCase();
            authorities.add(new SimpleGrantedAuthority("ROLE_"+perfilDescricao));

            Set<PerfilPermissaoEntity> transacoes = perfil.getPerfil().getPermissoes();

            for(PerfilPermissaoEntity pt : transacoes) {

                var prefix_recurso = pt.getPermissao().getRecurso().toUpperCase();
                // Adiciona a role base
                authorities.add(new SimpleGrantedAuthority("ROLE_" + prefix_recurso));

                // Adiciona as permissões específicas, se forem true
                if (pt.isView())
                    authorities.add(new SimpleGrantedAuthority("ROLE_VIEW_"+prefix_recurso));

                if (pt.isInsert())
                    authorities.add(new SimpleGrantedAuthority("ROLE_INSERT_" + prefix_recurso));

                if (pt.isUpdate())
                    authorities.add(new SimpleGrantedAuthority("ROLE_UPDATE_" + prefix_recurso));

                if (pt.isDelete())
                    authorities.add(new SimpleGrantedAuthority("ROLE_DELETE_" + prefix_recurso));
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {

        return usuario.getSenha();
    }

    @Override
    public String getUsername() {

        return usuario.getLogin();
    }

    public UsuarioEntity getUsuario(){
        return  usuario;
    }
}
