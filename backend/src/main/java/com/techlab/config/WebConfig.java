package com.techlab.config; 
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permitir acceso desde cualquier puerto de desarrollo local
        registry.addMapping("/**") // Aplica a todas las URL de la API
                .allowedOrigins(
                        "http://localhost:5500",
                        "http://127.0.0.1:5500",
                        "http://localhost:5501", // ✅ Agregamos el puerto que sale en tus logs
                        "http://127.0.0.1:5501" // ✅ Agregamos la IP con el puerto de tus logs
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}