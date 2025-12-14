package com.techlab.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.techlab.productos.Producto;
import com.techlab.productos.Bebida;
import com.techlab.productos.Comida;
import com.techlab.productos.ProductoRepository;
import com.techlab.usuarios.Usuario;
import com.techlab.usuarios.UsuarioRepository;
import java.time.LocalDate;

@Configuration
public class DatosIniciales {

        // Define el tamaño de imagen deseado
        private final String IMAGE_SIZE = "300/200";

        @Bean
        CommandLineRunner initDatabase(ProductoRepository productoRepo, UsuarioRepository usuarioRepo) {
                return args -> {
                        // Solo carga datos si la BD está vacía
                        if (productoRepo.count() == 0) {
                                System.out.println("📦 Cargando datos iniciales con imágenes de LoremFlickr...");

                                // --- PRODUCTOS DE ELECTRÓNICA (Usando LoremFlickr con tags) ---

                                Producto p1 = new Producto("Laptop Dell XPS 15",
                                                "Laptop de alto rendimiento con procesador Intel i7", 1299.99,
                                                "Electrónica", 15);
                                p1.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/laptop,computer");

                                Producto p2 = new Producto("Mouse Logitech MX Master 3", "Mouse inalámbrico ergonómico",
                                                99.99, "Accesorios", 50);
                                p2.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/mouse,wireless");

                                Producto p3 = new Producto("Teclado Mecánico Keychron K2",
                                                "Teclado mecánico retroiluminado", 89.99, "Accesorios", 30);
                                p3.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/keyboard,mechanical");

                                Producto p4 = new Producto("Monitor Samsung 27'' 4K",
                                                "Monitor 4K UHD con tecnología HDR", 349.99, "Electrónica", 20);
                                p4.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/monitor,4k");

                                // --- BEBIDAS (Usando LoremFlickr con tags) ---

                                Bebida b1 = new Bebida("Coca Cola", "Gaseosa sabor cola", 1.50, "Bebidas", 100, 2.0,
                                                "Gaseosa", true);
                                b1.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/coke,soda");

                                Bebida b2 = new Bebida("Agua Mineral", "Agua mineral natural", 0.80, "Bebidas", 150,
                                                1.5, "Agua", false);
                                b2.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/water,bottle");

                                Bebida b3 = new Bebida("Jugo de Naranja", "Jugo natural de naranja", 2.50, "Bebidas",
                                                80, 1.0, "Jugo", false);
                                b3.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/orange,juice");

                                // --- COMIDAS (Usando LoremFlickr con tags) ---

                                Comida c1 = new Comida("Pan Integral", "Pan integral artesanal", 2.99, "Alimentos", 40,
                                                LocalDate.now().plusDays(5), 0.5, false, "Panadería");
                                c1.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/bread,wholewheat");

                                Comida c2 = new Comida("Queso Gouda", "Queso holandés maduro", 8.99, "Alimentos", 25,
                                                LocalDate.now().plusDays(30), 0.3, true, "Lácteos");
                                c2.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/cheese,gouda");

                                Comida c3 = new Comida("Manzanas Rojas", "Manzanas rojas frescas", 3.50, "Alimentos",
                                                60,
                                                LocalDate.now().plusDays(10), 1.0, true, "Frutas");
                                c3.setUrlImagen("https://loremflickr.com/" + IMAGE_SIZE + "/apples,fruit");

                                // Guardar todo
                                productoRepo.save(p1);
                                productoRepo.save(p2);
                                productoRepo.save(p3);
                                productoRepo.save(p4);
                                productoRepo.save(b1);
                                productoRepo.save(b2);
                                productoRepo.save(b3);
                                productoRepo.save(c1);
                                productoRepo.save(c2);
                                productoRepo.save(c3);

                                System.out.println("✅ 10 productos creados");
                        }

                        // Crear usuario de prueba
                        if (usuarioRepo.count() == 0) {
                                Usuario u1 = new Usuario("Juan", "Pérez", "juan.perez@email.com", "555-1234",
                                                "Calle Principal 123");
                                Usuario u2 = new Usuario("María", "González", "maria.gonzalez@email.com", "555-5678",
                                                "Avenida Central 456");
                                usuarioRepo.save(u1);
                                usuarioRepo.save(u2);
                                System.out.println("✅ 2 usuarios creados");
                        }

                        System.out.println("✅ Datos iniciales cargados correctamente");
                };
        }
}