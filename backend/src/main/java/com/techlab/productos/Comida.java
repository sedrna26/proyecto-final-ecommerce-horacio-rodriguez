package com.techlab.productos;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Subclase Comida que extiende de Producto.
 * Demuestra el concepto de HERENCIA en POO.
 * Añade atributos específicos como fecha de vencimiento y peso.
 */
@Entity
@DiscriminatorValue("COMIDA")
public class Comida extends Producto {

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "peso_kg")
    private double pesoKg;

    @Column(name = "requiere_refrigeracion")
    private boolean requiereRefrigeracion;

    @Column(name = "tipo_comida", length = 50)
    private String tipoComida; // Ej: "Panadería", "Lácteos", "Carnes", "Verduras"

    // Constructores
    public Comida() {
        super();
    }

    public Comida(String nombre, String descripcion, double precio, String categoria,
            int stock, LocalDate fechaVencimiento, double pesoKg,
            boolean requiereRefrigeracion, String tipoComida) {
        super(nombre, descripcion, precio, categoria, stock);
        this.fechaVencimiento = fechaVencimiento;
        this.pesoKg = pesoKg;
        this.requiereRefrigeracion = requiereRefrigeracion;
        this.tipoComida = tipoComida;
    }

    // Getters y Setters específicos
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public boolean isRequiereRefrigeracion() {
        return requiereRefrigeracion;
    }

    public void setRequiereRefrigeracion(boolean requiereRefrigeracion) {
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    /**
     * Verifica si el producto está próximo a vencer (menos de 7 días)
     * Demuestra uso de operadores lógicos (&&) y relacionales (<)
     * 
     * @return true si está por vencer, false en caso contrario
     */
    public boolean estaPorVencer() {
        if (fechaVencimiento == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        LocalDate limiteVencimiento = hoy.plusDays(7);
        return fechaVencimiento.isBefore(limiteVencimiento) && !fechaVencimiento.isBefore(hoy);
    }

    /**
     * Verifica si el producto está vencido
     * Demuestra uso de operadores relacionales (<)
     * 
     * @return true si está vencido, false en caso contrario
     */
    public boolean estaVencido() {
        if (fechaVencimiento == null) {
            return false;
        }
        return fechaVencimiento.isBefore(LocalDate.now());
    }

    /**
     * Método que demuestra POLIMORFISMO
     * Sobrescribe hayStockDisponible para considerar la fecha de vencimiento
     */
    @Override
    public boolean hayStockDisponible(int cantidad) {
        // Usa operadores lógicos (&&, !)
        return super.hayStockDisponible(cantidad) && !estaVencido();
    }

    /**
     * Método que demuestra POLIMORFISMO
     * Sobrescribe el método toString() de la clase padre
     */
    @Override
    public String toString() {
        return "Comida{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", precio=" + getPrecio() +
                ", peso=" + pesoKg + "kg" +
                ", tipo='" + tipoComida + '\'' +
                ", vencimiento=" + fechaVencimiento +
                ", refrigeración=" + requiereRefrigeracion +
                ", stock=" + getStock() +
                '}';
    }
}
