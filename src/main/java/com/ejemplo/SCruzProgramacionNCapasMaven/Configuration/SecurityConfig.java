package com.ejemplo.SCruzProgramacionNCapasMaven.Configuration;

import com.ejemplo.SCruzProgramacionNCapasMaven.DAO.UsuarioJPADAOImplementation;
import com.ejemplo.SCruzProgramacionNCapasMaven.JPA.Usuario;
import com.ejemplo.SCruzProgramacionNCapasMaven.Repository.UsuarioRepository;
import jakarta.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public JWTAuthenticacationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticacationFilter(jwtTokenProvider, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String jwt = jwtTokenProvider.generateToken(authentication.getName());

            response.setHeader("Authorization", "Bearer " + jwt);
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 30);
            response.addCookie(cookie);

            request.getSession().setAttribute("jwt", jwt);

            response.sendRedirect("/usuario");
        };
    }

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                // Login y logout siempre públicos
                .requestMatchers("usuario/acceso-denegado", "/login", "/logout", "/usuario/login").permitAll()
                // Vista principal para los tres roles
                .requestMatchers("/usuario").hasAnyRole("PROGRAMADOR", "ADMINISTRADOR", "ANALISTA")
                // Vista de carga masiva GET (subir el archivo)
                .requestMatchers(HttpMethod.GET, "/usuario/cargamasiva").hasAnyRole("PROGRAMADOR", "ADMINISTRADOR", "ANALISTA")
                // Procesar carga masiva GET solo PROGRAMADOR y ADMINISTRADOR
                //.requestMatchers(HttpMethod.POST, "/usuario/cargamasiva/Procesar").hasAnyAuthority("PROGRAMADOR", "ADMINISTRADOR")
                // POST para subir archivo solo PROGRAMADOR y ADMINISTRADOR
                .requestMatchers(HttpMethod.POST, "/usuario/cargamasiva").hasAnyRole("PROGRAMADOR", "ADMINISTRADOR")
                // Cualquier GET solo PROGRAMADOR y ANALISTA
                .requestMatchers(HttpMethod.GET, "/usuario/**").hasAnyRole("PROGRAMADOR", "ANALISTA")
                // Permiso total para PROGRAMADOR
                .requestMatchers("/usuario/**").hasRole("PROGRAMADOR")
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/usuario/login")
                .loginProcessingUrl("/login")
                .successHandler(jwtAuthenticationSuccessHandler())
                .failureUrl("/usuario/login?error")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/usuario/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                )
                .sessionManagement(session -> session
                .invalidSessionUrl("/login?timeout")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                )
                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
            response.sendRedirect(request.getContextPath() + "/usuario/acceso-denegado");
        }));

        return httpSecurity.build();
    }
}
//    @Bean
//    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
//        return username -> {
//            Usuario usuario = usuarioRepository.findByUsername(username);
//            if (usuario == null) {
//                throw new UsernameNotFoundException("Usuario: " + username + " no encontrado o invalido! ");
//            }
//            if (usuario.getEstatus() != 1) {
//                throw new DisabledException("El Usuario " + username + " se encuentra inactivo");
//            }
//
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRoll().getRoll().toUpperCase()));
//
//            return new org.springframework.security.core.userdetails.User(
//                    usuario.getUsername(), usuario.getPassword(), authorities);
//        };
//    }
//    USUARIOS EN CONSOLA
//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder encoder) {
//
//        UserDetails programador = User.builder()
//                .username("Sebastian")
//                .password(encoder.encode("passwordprogram"))
//                .authorities("PROGRAMADOR")
//                .build();
//
//        UserDetails administrador = User.builder()
//                .username("Alejandro")
//                .password(encoder.encode("passwordadmin"))
//                .authorities("ADMINISTRADOR")
//                .build();
//
//        UserDetails analista = User.builder()
//                .username("Kevin")
//                .password(encoder.encode("passwordanalist"))
//                .authorities("ANALISTA")
//                .build();
//
//        return new InMemoryUserDetailsManager(programador, administrador, analista);
//    

