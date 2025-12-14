package com.techlab.pedidos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.techlab.excepciones.RecursoNoEncontradoException;
import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.excepciones.ValidacionException;
import com.techlab.productos.Producto;
import com.techlab.productos.ProductoService;
import com.techlab.usuarios.Usuario;
import com.techlab.usuarios.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Servicio de negocio para gestión de pedidos
 * Contiene la lógica compleja de creación de pedidos con validación de stock
 * Demuestra uso de COLECCIONES (Map, List), operadores y manejo de EXCEPCIONES
 */
@Service
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository,
            ProductoService productoService,
            UsuarioService usuarioService) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los pedidos
     * 
     * @return lista de todos los pedidos
     */
    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     * Obtiene un pedido por su ID
     * 
     * @param id ID del pedido
     * @return pedido encontrado
     * @throws RecursoNoEncontradoException si no se encuentra
     */
    public Pedido obtenerPedidoPorId(int id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido", "id", id));
    }

    /**
     * Obtiene todos los pedidos de un usuario
     * Demuestra uso de COLECCIONES (List)
     * 
     * @param usuarioId ID del usuario
     * @return lista de pedidos del usuario
     */
    public List<Pedido> obtenerPedidosPorUsuario(int usuarioId) {
        // Verifica que el usuario existe
        usuarioService.obtenerUsuarioPorId(usuarioId);
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Crea un nuevo pedido
     * Demuestra uso de COLECCIONES (Map), operadores ARITMÉTICOS y manejo de
     * EXCEPCIONES
     * 
     * @param usuarioId            ID del usuario que realiza el pedido
     * @param productosConCantidad Map con ID de producto como key y cantidad como
     *                             value
     * @return pedido creado
     * @throws RecursoNoEncontradoException si no existe usuario o producto
     * @throws StockInsuficienteException   si no hay suficiente stock
     */
    public Pedido crearPedido(int usuarioId, Map<Integer, Integer> productosConCantidad) {
        // Demuestra manejo de EXCEPCIONES con try-catch
        try {
            // Obtiene el usuario
            Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);

            // Validación: verifica que el pedido no esté vacío
            // Usa operadores LÓGICOS (||) y RELACIONALES (==)
            if (productosConCantidad == null || productosConCantidad.isEmpty()) {
                throw new ValidacionException("El pedido debe contener al menos un producto");
            }

            // Crea el pedido
            Pedido pedido = new Pedido(usuario);

            // Recorre el Map de productos (demuestra uso de COLECCIONES)
            for (Map.Entry<Integer, Integer> entry : productosConCantidad.entrySet()) {
                int productoId = entry.getKey();
                int cantidad = entry.getValue();

                // Validación: cantidad debe ser positiva (operador RELACIONAL >)
                if (cantidad <= 0) {
                    throw new ValidacionException("La cantidad debe ser mayor a 0");
                }

                // Obtiene el producto
                Producto producto = productoService.obtenerProductoPorId(productoId);

                // Validación: verifica stock disponible
                // Usa operador RELACIONAL (>=) y LÓGICO (!)
                if (!producto.hayStockDisponible(cantidad)) {
                    // Lanza EXCEPCIÓN personalizada con detalles
                    throw new StockInsuficienteException(
                            "Stock insuficiente",
                            producto.getId(),
                            producto.getNombre(),
                            producto.getStock(),
                            cantidad);
                }

                // Crea la línea de pedido
                LineaPedido linea = new LineaPedido(producto, cantidad);
                pedido.agregarLinea(linea);

                // Reduce el stock del producto (operador ARITMÉTICO -)
                productoService.reducirStock(productoId, cantidad);
            }

            // Calcula el total del pedido (usa operadores ARITMÉTICOS)
            pedido.recalcularTotal();

            // Guarda el pedido
            Pedido pedidoGuardado = pedidoRepository.save(pedido);

            return pedidoGuardado;

        } catch (RecursoNoEncontradoException | StockInsuficienteException | ValidacionException e) {
            // Re-lanza las excepciones conocidas
            throw e;
        } catch (Exception e) {
            // Captura cualquier otra excepción y lanza una ValidacionException
            throw new ValidacionException("Error al crear el pedido: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza el estado de un pedido
     * 
     * @param pedidoId    ID del pedido
     * @param nuevoEstado nuevo estado
     * @return pedido actualizado
     */
    public Pedido actualizarEstadoPedido(int pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = obtenerPedidoPorId(pedidoId);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    /**
     * Cancela un pedido y restaura el stock
     * Demuestra operadores LÓGICOS (!=) y manipulación de COLECCIONES
     * 
     * @param pedidoId ID del pedido a cancelar
     * @return pedido cancelado
     */
    public Pedido cancelarPedido(int pedidoId) {
        Pedido pedido = obtenerPedidoPorId(pedidoId);

        // Validación: solo se pueden cancelar pedidos pendientes o en proceso
        // Usa operadores LÓGICOS (!=, &&)
        if (pedido.getEstado() != EstadoPedido.PENDIENTE
                && pedido.getEstado() != EstadoPedido.EN_PROCESO) {
            throw new ValidacionException(
                    "Solo se pueden cancelar pedidos en estado PENDIENTE o EN_PROCESO");
        }

        // Restaura el stock de los productos (usa COLECCIONES y operadores ARITMÉTICOS)
        for (LineaPedido linea : pedido.getLineas()) {
            productoService.aumentarStock(
                    linea.getProducto().getId(),
                    linea.getCantidad());
        }

        // Cambia el estado a CANCELADO
        pedido.setEstado(EstadoPedido.CANCELADO);

        return pedidoRepository.save(pedido);
    }

    /**
     * Busca pedidos por estado
     * 
     * @param estado estado del pedido
     * @return lista de pedidos con ese estado
     */
    public List<Pedido> buscarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    /**
     * Calcula el total gastado por un usuario
     * Demuestra uso de COLECCIONES, operadores ARITMÉTICOS (+) y programación
     * funcional
     * 
     * @param usuarioId ID del usuario
     * @return total gastado
     */
    public double calcularTotalGastadoPorUsuario(int usuarioId) {
        List<Pedido> pedidos = obtenerPedidosPorUsuario(usuarioId);

        // Usa programación funcional con streams y operador ARITMÉTICO (+)
        return pedidos.stream()
                .filter(p -> p.getEstado() != EstadoPedido.CANCELADO)
                .mapToDouble(Pedido::getTotal)
                .sum();
    }

    /**
     * Cuenta la cantidad de pedidos por estado
     * Demuestra uso de operadores RELACIONALES (==) y COLECCIONES
     * 
     * @param usuarioId ID del usuario
     * @param estado    estado a contar
     * @return cantidad de pedidos
     */
    public long contarPedidosPorEstado(int usuarioId, EstadoPedido estado) {
        List<Pedido> pedidos = obtenerPedidosPorUsuario(usuarioId);

        // Usa programación funcional con operador RELACIONAL (==)
        return pedidos.stream()
                .filter(p -> p.getEstado() == estado)
                .count();
    }
}
