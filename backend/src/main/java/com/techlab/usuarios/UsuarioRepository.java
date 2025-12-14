package com.techlab.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repositorio para la entidad Usuario
 * Proporciona métodos para acceder y manipular datos de usuarios
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su email
     * Usado por el AuthController para login
     * 
     * @param email email del usuario
     * @return usuario si existe, null en caso contrario
     */
    Usuario findByEmail(String email);

    /**
     * Verifica si existe un usuario con ese email
     * 
     * @param email email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios por estado (activo/inactivo)
     * 
     * @param activo estado del usuario
     * @return lista de usuarios con ese estado
     */
    List<Usuario> findByActivo(boolean activo);

    /**
     * Busca usuarios por nombre o apellido (búsqueda parcial)
     * 
     * @param nombre   nombre a buscar
     * @param apellido apellido a buscar
     * @return lista de usuarios encontrados
     */
    List<Usuario> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre, String apellido);
}
