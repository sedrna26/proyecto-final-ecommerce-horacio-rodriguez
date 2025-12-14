package com.techlab.productos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Objects;

/**
 * Clase base Producto que representa un producto en el sistema de E-commerce.
 * Utiliza tipos de datos fundamentales: int, double, String, boolean
 * Incluye validaciones y anotaciones JPA para mapeo a base de datos.
 */
@Entity
@Table(name = "productos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_producto", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("PRODUCTO")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Positive(message = "El precio debe ser mayor a 0")
    @Column(nullable = false)
    private double precio;

    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false, length = 50)
    private String categoria;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private int stock;

    @Column(name = "url_imagen")
    private String urlImagen;

    @Column(nullable = false)
    private boolean activo = true;

    // Constructores
    public Producto() {
    }

    public Producto(String nombre, String descripcion, double precio, String categoria, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
        this.activo = true;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Método que verifica si hay stock disponible
     * Demuestra uso de operadores relacionales (>=)
     * 
     * @param cantidad cantidad solicitada
     * @return true si hay suficiente stock, false en caso contrario
     */
    public boolean hayStockDisponible(int cantidad) {
        return this.stock >= cantidad && this.activo;
    }

    /**
     * Método que reduce el stock del producto
     * Demuestra uso de operadores aritméticos (-)
     * 
     * @param cantidad cantidad a reducir
     */
    public void reducirStock(int cantidad) {
        this.stock = this.stock - cantidad;
    }

    /**
     * Método que aumenta el stock del producto
     * Demuestra uso de operadores aritméticos (+)
     * 
     * @param cantidad cantidad a aumentar
     */
    public void aumentarStock(int cantidad) {
        this.stock = this.stock + cantidad;
    }

    /**
     * Calcula el subtotal para una cantidad específica
     * Demuestra uso de operadores aritméticos (*)
     * 
     * @param cantidad cantidad de productos
     * @return subtotal calculado
     */
    public double calcularSubtotal(int cantidad) {
        return this.precio * cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Producto producto = (Producto) o;
        return id == producto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", categoria='" + categoria + '\'' +
                ", activo=" + activo +
                '}';
    }
}
