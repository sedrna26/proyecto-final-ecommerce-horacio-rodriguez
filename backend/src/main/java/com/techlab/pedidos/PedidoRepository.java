package com.techlab.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.techlab.usuarios.Usuario;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Pedido
 * Proporciona métodos para acceder y manipular datos de pedidos
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    /**
     * Busca todos los pedidos de un usuario
     * 
     * @param usuario usuario dueño de los pedidos
     * @return lista de pedidos del usuario
     */
    List<Pedido> findByUsuario(Usuario usuario);

    /**
     * Busca pedidos por ID de usuario
     * 
     * @param usuarioId ID del usuario
     * @return lista de pedidos del usuario
     */
    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId ORDER BY p.fechaPedido DESC")
    List<Pedido> findByUsuarioId(int usuarioId);

    /**
     * Busca pedidos por estado
     * 
     * @param estado estado del pedido
     * @return lista de pedidos con ese estado
     */
    List<Pedido> findByEstado(EstadoPedido estado);

    /**
     * Busca pedidos entre dos fechas
     * 
     * @param fechaInicio fecha de inicio
     * @param fechaFin    fecha de fin
     * @return lista de pedidos en ese rango
     */
    List<Pedido> findByFechaPedidoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Busca pedidos de un usuario con un estado específico
     * 
     * @param usuario usuario
     * @param estado  estado del pedido
     * @return lista de pedidos filtrados
     */
    List<Pedido> findByUsuarioAndEstado(Usuario usuario, EstadoPedido estado);

    /**
     * Cuenta la cantidad de pedidos de un usuario
     * 
     * @param usuario usuario
     * @return cantidad de pedidos
     */
    long countByUsuario(Usuario usuario);
}
