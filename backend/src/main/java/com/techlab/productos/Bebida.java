package com.techlab.productos;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Subclase Bebida que extiende de Producto.
 * Demuestra el concepto de HERENCIA en POO.
 * Añade atributos específicos como volumen y tipo de bebida.
 */
@Entity
@DiscriminatorValue("BEBIDA")
public class Bebida extends Producto {

    @Column(name = "volumen_litros")
    private double volumenLitros;

    @Column(name = "tipo_bebida", length = 50)
    private String tipoBebida; // Ej: "Gaseosa", "Jugo", "Agua", "Alcoholica"

    @Column(name = "tiene_gas")
    private boolean tieneGas;

    // Constructores
    public Bebida() {
        super();
    }

    public Bebida(String nombre, String descripcion, double precio, String categoria,
            int stock, double volumenLitros, String tipoBebida, boolean tieneGas) {
        super(nombre, descripcion, precio, categoria, stock);
        this.volumenLitros = volumenLitros;
        this.tipoBebida = tipoBebida;
        this.tieneGas = tieneGas;
    }

    // Getters y Setters específicos
    public double getVolumenLitros() {
        return volumenLitros;
    }

    public void setVolumenLitros(double volumenLitros) {
        this.volumenLitros = volumenLitros;
    }

    public String getTipoBebida() {
        return tipoBebida;
    }

    public void setTipoBebida(String tipoBebida) {
        this.tipoBebida = tipoBebida;
    }

    public boolean isTieneGas() {
        return tieneGas;
    }

    public void setTieneGas(boolean tieneGas) {
        this.tieneGas = tieneGas;
    }

    /**
     * Método que demuestra POLIMORFISMO
     * Sobrescribe el método toString() de la clase padre
     */
    @Override
    public String toString() {
        return "Bebida{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", precio=" + getPrecio() +
                ", volumen=" + volumenLitros + "L" +
                ", tipo='" + tipoBebida + '\'' +
                ", tieneGas=" + tieneGas +
                ", stock=" + getStock() +
                '}';
    }
}
