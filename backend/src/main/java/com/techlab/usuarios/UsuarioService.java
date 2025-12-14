package com.techlab.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techlab.excepciones.RecursoNoEncontradoException;
import com.techlab.excepciones.ValidacionException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de negocio para gestión de usuarios
 */
@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene todos los usuarios activos
     * 
     * @return lista de usuarios activos
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findByActivo(true);
    }

    /**
     * Obtiene un usuario por su ID
     * 
     * @param id ID del usuario
     * @return usuario encontrado
     * @throws RecursoNoEncontradoException si no se encuentra
     */
    public Usuario obtenerUsuarioPorId(int id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", "id", id));
    }

    /**
     * Busca un usuario por email
     * 
     * @param email email del usuario
     * @return usuario encontrado
     * @throws RecursoNoEncontradoException si no se encuentra
     */
    public Usuario obtenerUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new RecursoNoEncontradoException("Usuario", "email", email);
        }
        return usuario;
    }

    /**
     * Crea un nuevo usuario
     * 
     * @param usuario usuario a crear
     * @return usuario creado con ID generado
     * @throws ValidacionException si el email ya existe
     */
    public Usuario crearUsuario(Usuario usuario) {
        // Valida que el email no esté registrado
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ValidacionException("Ya existe un usuario con el email: " + usuario.getEmail());
        }

        // Establece valores por defecto
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente
     * 
     * @param id                 ID del usuario a actualizar
     * @param usuarioActualizado datos actualizados
     * @return usuario actualizado
     */
    public Usuario actualizarUsuario(int id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = obtenerUsuarioPorId(id);

        // Verifica si el email cambió y si ya existe
        if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail())
                && usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
            throw new ValidacionException("Ya existe un usuario con el email: " + usuarioActualizado.getEmail());
        }

        // Actualiza los campos
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());

        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Elimina un usuario (eliminación lógica)
     * 
     * @param id ID del usuario a eliminar
     */
    public void eliminarUsuario(int id) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    /**
     * Busca usuarios por nombre o apellido
     * 
     * @param termino término de búsqueda
     * @return lista de usuarios encontrados
     */
    public List<Usuario> buscarUsuarios(String termino) {
        return usuarioRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                termino, termino);
    }
}
