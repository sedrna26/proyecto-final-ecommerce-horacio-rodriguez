package com.techlab.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad LineaPedido
 */
@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Integer> {

    /**
     * Busca todas las líneas de un pedido específico
     * 
     * @param pedidoId ID del pedido
     * @return lista de líneas del pedido
     */
    List<LineaPedido> findByPedidoId(int pedidoId);

    /**
     * Busca todas las líneas que contienen un producto específico
     * 
     * @param productoId ID del producto
     * @return lista de líneas con ese producto
     */
    List<LineaPedido> findByProductoId(int productoId);
}
