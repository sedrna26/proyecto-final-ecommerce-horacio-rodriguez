package com.techlab.pedidos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.techlab.productos.Producto;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Clase LineaPedido que representa un producto dentro de un pedido
 * Demuestra colaboración de clases y uso de operadores aritméticos
 */
@Entity
@Table(name = "lineas_pedido")
public class LineaPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private double precioUnitario;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // Constructores
    public LineaPedido() {
    }

    public LineaPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            this.precioUnitario = producto.getPrecio();
        }
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * Calcula el subtotal de esta línea de pedido
     * Demuestra uso de operadores ARITMÉTICOS (*)
     * 
     * @return subtotal = precio unitario * cantidad
     */
    public double calcularSubtotal() {
        return this.precioUnitario * this.cantidad;
    }

    /**
     * Obtiene el nombre del producto de esta línea
     * 
     * @return nombre del producto
     */
    public String getNombreProducto() {
        return producto != null ? producto.getNombre() : "N/A";
    }

    @Override
    public String toString() {
        return "LineaPedido{" +
                "id=" + id +
                ", producto=" + getNombreProducto() +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + calcularSubtotal() +
                '}';
    }
}
