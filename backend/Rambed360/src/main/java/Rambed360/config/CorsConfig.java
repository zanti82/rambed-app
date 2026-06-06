package Rambed360.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
public class CorsConfig {

    // Bean de CORS que Spring Security y Spring MVC comparten
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Crea la configuracion de CORS
        CorsConfiguration configuracion = new CorsConfiguration();

        // Define los origenes permitidos
        configuracion.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "https://rambed-app.vercel.app"
        ));

        // Define los metodos permitidos
        configuracion.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // Permite todos los headers incluyendo Authorization para JWT
        configuracion.setAllowedHeaders(List.of("*"));

        // Permite credenciales necesario para JWT en headers
        configuracion.setAllowCredentials(true);

        // Tiempo en segundos que el browser cachea la configuracion
        configuracion.setMaxAge(3600L);

        // Aplica esta configuracion a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuracion);

        return source;
    }
}
