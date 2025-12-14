package com.techlab.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST para gestión de usuarios
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = { "http://localhost:5500", "http://127.0.0.1:5500" })
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * GET /api/usuarios
     * Obtiene todos los usuarios activos
     * 
     * @return lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * GET /api/usuarios/{id}
     * Obtiene un usuario por su ID
     * 
     * @param id ID del usuario
     * @return usuario encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable int id) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    /**
     * GET /api/usuarios/email/{email}
     * Busca un usuario por email
     * 
     * @param email email del usuario
     * @return usuario encontrado
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerUsuarioPorEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    /**
     * POST /api/usuarios
     * Crea un nuevo usuario
     * 
     * @param usuario datos del usuario
     * @return usuario creado
     */
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioCreado = usuarioService.crearUsuario(usuario);
        return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
    }

    /**
     * PUT /api/usuarios/{id}
     * Actualiza un usuario existente
     * 
     * @param id      ID del usuario
     * @param usuario datos actualizados
     * @return usuario actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable int id,
            @Valid @RequestBody Usuario usuario) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * DELETE /api/usuarios/{id}
     * Elimina un usuario (eliminación lógica)
     * 
     * @param id ID del usuario
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable int id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/usuarios/buscar?termino={termino}
     * Busca usuarios por nombre o apellido
     * 
     * @param termino término de búsqueda
     * @return lista de usuarios encontrados
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscarUsuarios(@RequestParam String termino) {
        List<Usuario> usuarios = usuarioService.buscarUsuarios(termino);
        return ResponseEntity.ok(usuarios);
    }
}
