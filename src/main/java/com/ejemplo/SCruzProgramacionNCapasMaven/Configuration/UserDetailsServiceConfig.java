package com.ejemplo.SCruzProgramacionNCapasMaven.Configuration;

import com.ejemplo.SCruzProgramacionNCapasMaven.Repository.UsuarioRepository;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
public class UserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            Usuario usuario = usuarioRepository.findByUsername(username);
            if (usuario == null) {
                throw new UsernameNotFoundException("Usuario: " + username + " no encontrado o invalido! ");
            }
            if (usuario.getEstatus() != 1) {
                throw new DisabledException("El Usuario " + username + " se encuentra inactivo");
            }

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRoll().getRoll().toUpperCase()));

            return new org.springframework.security.core.userdetails.User(
                    usuario.getUsername(), usuario.getPassword(), authorities);
        };
    }
}
