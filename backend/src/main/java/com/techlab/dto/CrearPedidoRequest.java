package com.techlab.dto;

import java.util.Map;

/**
 * DTO (Data Transfer Object) para crear pedidos
 * Demuestra el patrón DTO para transferencia de datos
 */
public class CrearPedidoRequest {

    private int usuarioId;
    private Map<Integer, Integer> productos; // productoId -> cantidad

    public CrearPedidoRequest() {
    }

    public CrearPedidoRequest(int usuarioId, Map<Integer, Integer> productos) {
        this.usuarioId = usuarioId;
        this.productos = productos;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Map<Integer, Integer> getProductos() {
        return productos;
    }

    public void setProductos(Map<Integer, Integer> productos) {
        this.productos = productos;
    }
}
