package com.techlab.productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de productos
 * Expone los endpoints de la API para operaciones CRUD de productos
 * Demuestra arquitectura REST y respuestas HTTP
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = { "http://localhost:5500", "http://127.0.0.1:5500" })
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * GET /api/productos
     * Obtiene todos los productos activos
     * 
     * @return lista de productos en formato JSON
     */
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }

    /**
     * GET /api/productos/{id}
     * Obtiene un producto por su ID
     * 
     * @param id ID del producto
     * @return producto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable int id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(producto);
    }

    /**
     * GET /api/productos/categoria/{categoria}
     * Busca productos por categoría
     * 
     * @param categoria categoría a buscar
     * @return lista de productos de esa categoría
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable String categoria) {
        List<Producto> productos = productoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    /**
     * GET /api/productos/buscar?nombre={nombre}
     * Busca productos por nombre
     * 
     * @param nombre nombre o parte del nombre
     * @return lista de productos encontrados
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    /**
     * GET /api/productos/mas-vendidos
     * Obtiene los productos más vendidos
     * 
     * @return lista de los 3 productos más vendidos
     */
    @GetMapping("/mas-vendidos")
    public ResponseEntity<List<Producto>> obtenerProductosMasVendidos() {
        List<Producto> productos = productoService.obtenerProductosMasVendidos();
        return ResponseEntity.ok(productos);
    }

    /**
     * POST /api/productos
     * Crea un nuevo producto
     * 
     * @param producto datos del producto en formato JSON
     * @return producto creado con código HTTP 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        Producto productoCreado = productoService.crearProducto(producto);
        return new ResponseEntity<>(productoCreado, HttpStatus.CREATED);
    }

    /**
     * PUT /api/productos/{id}
     * Actualiza un producto existente
     * 
     * @param id       ID del producto a actualizar
     * @param producto datos actualizados
     * @return producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable int id,
            @Valid @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    /**
     * DELETE /api/productos/{id}
     * Elimina un producto (eliminación lógica)
     * 
     * @param id ID del producto a eliminar
     * @return respuesta sin contenido (204 No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/productos/{id}/stock/{cantidad}
     * Verifica si hay stock disponible
     * 
     * @param id       ID del producto
     * @param cantidad cantidad solicitada
     * @return objeto JSON con resultado de la verificación
     */
    @GetMapping("/{id}/stock/{cantidad}")
    public ResponseEntity<Map<String, Object>> verificarStock(
            @PathVariable int id,
            @PathVariable int cantidad) {
        boolean hayStock = productoService.verificarStock(id, cantidad);
        Producto producto = productoService.obtenerProductoPorId(id);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("productoId", id);
        respuesta.put("productoNombre", producto.getNombre());
        respuesta.put("cantidadSolicitada", cantidad);
        respuesta.put("stockDisponible", producto.getStock());
        respuesta.put("hayStock", hayStock);

        return ResponseEntity.ok(respuesta);
    }

    /**
     * GET /api/productos/stock-bajo?minimo={minimo}
     * Obtiene productos con stock bajo
     * 
     * @param minimo stock mínimo considerado bajo (default: 10)
     * @return lista de productos con stock bajo
     */
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> buscarProductosConStockBajo(
            @RequestParam(defaultValue = "10") int minimo) {
        List<Producto> productos = productoService.buscarProductosConStockBajo(minimo);
        return ResponseEntity.ok(productos);
    }
}
