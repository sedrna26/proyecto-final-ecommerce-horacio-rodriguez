package com.techlab.pedidos;

import jakarta.persistence.*;
import com.techlab.usuarios.Usuario;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Pedido que representa un pedido en el sistema
 * Demuestra uso de COLECCIONES (ArrayList), colaboración de clases
 * y operadores aritméticos para cálculos
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado;

    @Column(nullable = false)
    private double total;

    // Relación con LineaPedido - Demuestra uso de COLECCIONES (List/ArrayList)
    @JsonManagedReference
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LineaPedido> lineas = new ArrayList<>();

    @Column(length = 500)
    private String observaciones;

    // Constructores
    public Pedido() {
        this.fechaPedido = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
        this.lineas = new ArrayList<>();
        this.total = 0.0;
    }

    public Pedido(Usuario usuario) {
        this();
        this.usuario = usuario;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedido> lineas) {
        this.lineas = lineas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Agrega una línea de pedido a la lista
     * Demuestra manipulación de COLECCIONES (ArrayList)
     * 
     * @param linea línea de pedido a agregar
     */
    public void agregarLinea(LineaPedido linea) {
        this.lineas.add(linea);
        linea.setPedido(this);
        recalcularTotal();
    }

    /**
     * Elimina una línea de pedido de la lista
     * Demuestra manipulación de COLECCIONES (ArrayList)
     * 
     * @param linea línea de pedido a eliminar
     */
    public void eliminarLinea(LineaPedido linea) {
        this.lineas.remove(linea);
        linea.setPedido(null);
        recalcularTotal();
    }

    /**
     * Calcula el total del pedido sumando todos los subtotales de las líneas
     * Demuestra uso de operadores ARITMÉTICOS (+, +=) y COLECCIONES
     * 
     * @return total del pedido
     */
    public double calcularTotal() {
        double totalCalculado = 0.0;
        // Recorre la colección de líneas
        for (LineaPedido linea : lineas) {
            // Usa operador aritmético de suma acumulativa (+=)
            totalCalculado += linea.calcularSubtotal();
        }
        return totalCalculado;
    }

    /**
     * Recalcula y actualiza el total del pedido
     */
    public void recalcularTotal() {
        this.total = calcularTotal();
    }

    /**
     * Cuenta la cantidad total de productos en el pedido
     * Demuestra uso de COLECCIONES y operadores aritméticos
     * 
     * @return cantidad total de productos
     */
    public int contarProductos() {
        int cantidadTotal = 0;
        for (LineaPedido linea : lineas) {
            cantidadTotal += linea.getCantidad();
        }
        return cantidadTotal;
    }

    /**
     * Verifica si el pedido está vacío
     * Demuestra operadores LÓGICOS y RELACIONALES (==)
     * 
     * @return true si no tiene líneas, false en caso contrario
     */
    public boolean estaVacio() {
        return this.lineas.isEmpty() || this.lineas.size() == 0;
    }

    /**
     * Verifica si el pedido puede ser modificado
     * Demuestra operadores LÓGICOS (||) y RELACIONALES (==)
     * 
     * @return true si está en estado PENDIENTE, false en caso contrario
     */
    public boolean puedeSerModificado() {
        return this.estado == EstadoPedido.PENDIENTE || this.estado == EstadoPedido.EN_PROCESO;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", usuario=" + (usuario != null ? usuario.getNombreCompleto() : "N/A") +
                ", fechaPedido=" + fechaPedido +
                ", estado=" + estado +
                ", total=" + total +
                ", cantidadLineas=" + lineas.size() +
                '}';
    }
}
