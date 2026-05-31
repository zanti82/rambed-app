package Rambed360.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Filtro JWT que intercepta cada peticion
    private final JwtFilter jwtFilter;
    

    // Constructor explicito con la dependencia del filtro
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
        
    }

    // Configura las reglas de seguridad de la aplicacion
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Desactiva CSRF porque usamos JWT y no sesiones
        http.csrf(csrf -> csrf.disable());

        
        http.cors(cors -> cors.configure(http)); //cors

        // Configura las rutas publicas y protegidas
        http.authorizeHttpRequests(auth -> auth

            // Permite las peticiones OPTIONS sin autenticacion para el preflight de CORS
            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

            // La ruta de login es publica
            .requestMatchers("/api/auth/login").permitAll()

            // Solo ADMIN puede gestionar vendedores, clientes y referencias
            .requestMatchers("/api/vendedores/**").hasRole("ADMIN")
            .requestMatchers("/api/referencias/**").hasRole("ADMIN")
            .requestMatchers("/api/inventario/**").hasAnyRole("ADMIN", "VENDEDOR")
            .requestMatchers("/api/clientes/**").hasAnyRole("ADMIN", "VENDEDOR")
            .requestMatchers("/api/facturas/**").hasAnyRole("ADMIN", "VENDEDOR")
            .requestMatchers("/api/factura-detalle/**").hasAnyRole("ADMIN", "VENDEDOR")
            .requestMatchers("/api/devoluciones/**").hasAnyRole("ADMIN", "VENDEDOR")
            .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "VENDEDOR")
            .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
            .requestMatchers("/api/auth/login", "/api/auth/hash").permitAll()
            

            // Cualquier otra ruta requiere autenticacion
            .anyRequest().authenticated()
        );

        // Configura la politica de sesiones como stateless porque usamos JWT
        http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Agrega el filtro JWT antes del filtro de autenticacion de Spring
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para encriptar contrasenas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para manejar la autenticacion
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173")); // URL de tu React
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}