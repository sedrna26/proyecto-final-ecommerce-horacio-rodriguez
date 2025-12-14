package com.techlab.productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techlab.excepciones.RecursoNoEncontradoException;
import com.techlab.excepciones.ValidacionException;

import java.util.List;

/**
 * Servicio de negocio para gestión de productos
 * Contiene la lógica de negocio y validaciones
 * Demuestra encapsulación de lógica y uso de excepciones
 */
@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Obtiene todos los productos activos
     * 
     * @return lista de productos activos
     */
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findByActivo(true);
    }

    /**
     * Obtiene un producto por su ID
     * Demuestra manejo de EXCEPCIONES
     * 
     * @param id ID del producto
     * @return producto encontrado
     * @throws RecursoNoEncontradoException si no se encuentra
     */
    public Producto obtenerProductoPorId(int id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", "id", id));
    }

    /**
     * Busca productos por categoría
     * 
     * @param categoria categoría a buscar
     * @return lista de productos de esa categoría
     */
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoriaAndActivo(categoria, true);
    }

    /**
     * Obtiene los productos más vendidos
     * 
     * @return lista de los 3 productos más vendidos
     */
    public List<Producto> obtenerProductosMasVendidos() {
        return productoRepository.findTopProductosMasVendidos();
    }

    /**
     * Busca productos por nombre (búsqueda parcial)
     * 
     * @param nombre nombre o parte del nombre
     * @return lista de productos encontrados
     */
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Crea un nuevo producto
     * Demuestra validaciones y manejo de excepciones
     * 
     * @param producto producto a crear
     * @return producto creado con ID generado
     * @throws ValidacionException si hay errores de validación
     */
    public Producto crearProducto(Producto producto) {
        // Validaciones de negocio
        validarProducto(producto);

        // Establece valores por defecto
        producto.setActivo(true);

        // Guarda en la base de datos
        return productoRepository.save(producto);
    }

    /**
     * Actualiza un producto existente
     * 
     * @param id                  ID del producto a actualizar
     * @param productoActualizado datos actualizados
     * @return producto actualizado
     * @throws RecursoNoEncontradoException si no existe el producto
     */
    public Producto actualizarProducto(int id, Producto productoActualizado) {
        // Verifica que el producto existe
        Producto productoExistente = obtenerProductoPorId(id);

        // Validaciones
        validarProducto(productoActualizado);

        // Actualiza los campos
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setCategoria(productoActualizado.getCategoria());
        productoExistente.setStock(productoActualizado.getStock());
        productoExistente.setUrlImagen(productoActualizado.getUrlImagen());

        return productoRepository.save(productoExistente);
    }

    /**
     * Elimina un producto (eliminación lógica)
     * 
     * @param id ID del producto a eliminar
     */
    public void eliminarProducto(int id) {
        Producto producto = obtenerProductoPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    /**
     * Verifica si hay stock disponible de un producto
     * Demuestra uso de operadores RELACIONALES (>=)
     * 
     * @param productoId ID del producto
     * @param cantidad   cantidad solicitada
     * @return true si hay stock, false en caso contrario
     */
    public boolean verificarStock(int productoId, int cantidad) {
        try {
            Producto producto = obtenerProductoPorId(productoId);
            return producto.hayStockDisponible(cantidad);
        } catch (RecursoNoEncontradoException e) {
            return false;
        }
    }

    /**
     * Reduce el stock de un producto
     * Demuestra uso de operadores ARITMÉTICOS (-)
     * 
     * @param productoId ID del producto
     * @param cantidad   cantidad a reducir
     * @throws RecursoNoEncontradoException si no existe el producto
     * @throws ValidacionException          si no hay suficiente stock
     */
    public void reducirStock(int productoId, int cantidad) {
        Producto producto = obtenerProductoPorId(productoId);

        // Validación con operador RELACIONAL (<)
        if (producto.getStock() < cantidad) {
            throw new ValidacionException(
                    String.format("Stock insuficiente para el producto '%s'. Disponible: %d, Solicitado: %d",
                            producto.getNombre(), producto.getStock(), cantidad));
        }

        // Operación aritmética (-)
        producto.reducirStock(cantidad);
        productoRepository.save(producto);
    }

    /**
     * Aumenta el stock de un producto
     * 
     * @param productoId ID del producto
     * @param cantidad   cantidad a aumentar
     */
    public void aumentarStock(int productoId, int cantidad) {
        Producto producto = obtenerProductoPorId(productoId);
        producto.aumentarStock(cantidad);
        productoRepository.save(producto);
    }

    /**
     * Valida los datos de un producto
     * Demuestra uso de operadores LÓGICOS (&&, ||) y RELACIONALES (<=, ==)
     * 
     * @param producto producto a validar
     * @throws ValidacionException si hay errores de validación
     */
    private void validarProducto(Producto producto) {
        // Operadores LÓGICOS (==, ||)
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new ValidacionException("El nombre del producto es obligatorio");
        }

        // Operadores RELACIONALES (<=)
        if (producto.getPrecio() <= 0) {
            throw new ValidacionException("El precio debe ser mayor a 0");
        }

        // Operadores RELACIONALES (<) y LÓGICOS (&&)
        if (producto.getStock() < 0) {
            throw new ValidacionException("El stock no puede ser negativo");
        }

        // Operadores LÓGICOS (==, ||)
        if (producto.getCategoria() == null || producto.getCategoria().trim().isEmpty()) {
            throw new ValidacionException("La categoría es obligatoria");
        }
    }

    /**
     * Busca productos con stock bajo (menos de cierta cantidad)
     * 
     * @param stockMinimo stock mínimo considerado bajo
     * @return lista de productos con stock bajo
     */
    public List<Producto> buscarProductosConStockBajo(int stockMinimo) {
        List<Producto> todosLosProductos = productoRepository.findByActivo(true);
        // Usa programación funcional con streams y operadores relacionales
        return todosLosProductos.stream()
                .filter(p -> p.getStock() < stockMinimo)
                .toList();
    }
}
