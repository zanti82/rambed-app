package Rambed360.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

      @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // en origin ponemos la url de railway
                        .allowedOrigins("http://localhost:5173",  "https://rambed-app.vercel.app")
                        .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)

                    // Tiempo en segundos que el navegador cachea la configuracion CORS
                        .maxAge(3600);
            }
        };
    }
    
}
