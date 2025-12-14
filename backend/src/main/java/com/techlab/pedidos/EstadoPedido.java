package com.techlab.pedidos;

/**
 * Enum que representa los posibles estados de un pedido
 * Demuestra uso de tipos enumerados en Java
 */
public enum EstadoPedido {
    PENDIENTE("Pendiente de procesamiento"),
    EN_PROCESO("En proceso de preparación"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
