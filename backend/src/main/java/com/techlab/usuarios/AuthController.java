package com.techlab.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para autenticación y gestión de sesiones
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:5501",
        "http://127.0.0.1:5501" })
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * POST /api/auth/login
     * Inicia sesión de un usuario
     * 
     * @param credenciales mapa con email y password
     * @return usuario autenticado con token de sesión
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");

        if (email == null || password == null) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Email y contraseña son requeridos");
            return ResponseEntity.badRequest().body(error);
        }

        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        if (!usuario.isActivo()) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Usuario desactivado");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        // Verificar contraseña (en producción debería usar BCrypt)
        if (!usuario.getPassword().equals(password)) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Contraseña incorrecta");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        // Crear respuesta con datos del usuario (sin password)
        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getId());
        response.put("nombre", usuario.getNombre());
        response.put("apellido", usuario.getApellido());
        response.put("email", usuario.getEmail());
        response.put("rol", usuario.getRol());
        response.put("telefono", usuario.getTelefono());
        response.put("direccion", usuario.getDireccion());

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/register
     * Registra un nuevo usuario
     * 
     * @param datos datos del nuevo usuario
     * @return usuario creado
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> datos) {
        String email = datos.get("email");

        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(email) != null) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "El email ya está registrado");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(datos.get("nombre"));
        nuevoUsuario.setApellido(datos.get("apellido"));
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(datos.get("password")); // En producción: BCrypt
        nuevoUsuario.setRol("USUARIO"); // Por defecto es usuario normal
        nuevoUsuario.setTelefono(datos.get("telefono"));
        nuevoUsuario.setDireccion(datos.get("direccion"));

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // Respuesta sin password
        Map<String, Object> response = new HashMap<>();
        response.put("id", usuarioGuardado.getId());
        response.put("nombre", usuarioGuardado.getNombre());
        response.put("apellido", usuarioGuardado.getApellido());
        response.put("email", usuarioGuardado.getEmail());
        response.put("rol", usuarioGuardado.getRol());
        response.put("mensaje", "Usuario registrado exitosamente");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/auth/logout
     * Cierra sesión del usuario
     * 
     * @return mensaje de confirmación
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Sesión cerrada exitosamente");
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/auth/verify/{userId}
     * Verifica si un usuario existe y está activo
     * 
     * @param userId ID del usuario
     * @return datos del usuario
     */
    @GetMapping("/verify/{userId}")
    public ResponseEntity<?> verify(@PathVariable int userId) {
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);

        if (usuario == null || !usuario.isActivo()) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Usuario no válido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getId());
        response.put("nombre", usuario.getNombre());
        response.put("apellido", usuario.getApellido());
        response.put("email", usuario.getEmail());
        response.put("rol", usuario.getRol());

        return ResponseEntity.ok(response);
    }
}
