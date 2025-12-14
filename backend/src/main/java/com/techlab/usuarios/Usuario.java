package com.techlab.usuarios;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.techlab.pedidos.Pedido;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Usuario que representa un usuario del sistema E-commerce
 * Demuestra uso de colecciones (List) y relaciones entre entidades
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false, length = 100)
    private String apellido;

    @Email(message = "Email inválido")
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Column(nullable = false, length = 20)
    private String rol = "USUARIO"; // ADMIN o USUARIO

    @Column(length = 20)
    private String telefono;

    @Column(length = 200)
    private String direccion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private boolean activo = true;

    // Relación con Pedidos - Demuestra uso de COLECCIONES (List)
    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    // Constructores
    public Usuario() {
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
        this.pedidos = new ArrayList<>();
    }

    public Usuario(String nombre, String apellido, String email, String telefono, String direccion) {
        this();
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    /**
     * Método helper para agregar un pedido a la lista
     * Demuestra manipulación de COLECCIONES (ArrayList)
     * 
     * @param pedido pedido a agregar
     */
    public void agregarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setUsuario(this);
    }

    /**
     * Obtiene el nombre completo del usuario
     * Demuestra concatenación de Strings
     * 
     * @return nombre completo
     */
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

    /**
     * Cuenta el total de pedidos realizados por el usuario
     * Demuestra uso de colecciones y sus métodos
     * 
     * @return cantidad de pedidos
     */
    public int contarPedidos() {
        return this.pedidos.size();
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                ", totalPedidos=" + pedidos.size() +
                '}';
    }
}
