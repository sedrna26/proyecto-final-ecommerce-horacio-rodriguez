package com.techlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal que inicia la aplicación Spring Boot del E-commerce
 * 
 * @SpringBootApplication combina:
 *                        - @Configuration: Marca la clase como fuente de
 *                        definiciones de beans
 *                        - @EnableAutoConfiguration: Habilita la configuración
 *                        automática de Spring Boot
 *                        - @ComponentScan: Escanea componentes en el paquete
 *                        com.techlab y sus subpaquetes
 */
@SpringBootApplication
public class EcommerceApplication {

    /**
     * Método main que inicia la aplicación
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
        System.out.println("==============================================");
        System.out.println("🚀 E-commerce API iniciada correctamente");
        System.out.println("📍 URL: http://localhost:8080");
        System.out.println("📖 Documentación: http://localhost:8080/api");
        System.out.println("==============================================");
    }
}
