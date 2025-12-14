package com.techlab.pedidos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de pedidos
 * Demuestra arquitectura REST y manejo de peticiones HTTP complejas
 */
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = { "http://localhost:5500", "http://127.0.0.1:5500" })
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * GET /api/pedidos
     * Obtiene todos los pedidos
     * 
     * @return lista de pedidos
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodosLosPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /api/pedidos/{id}
     * Obtiene un pedido por su ID
     * 
     * @param id ID del pedido
     * @return pedido encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable int id) {
        Pedido pedido = pedidoService.obtenerPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * POST /api/pedidos
     * Crea un nuevo pedido
     * Ejemplo de JSON esperado:
     * {
     * "usuarioId": 1,
     * "productos": {
     * "1": 2,
     * "3": 1
     * }
     * }
     * Donde "1": 2 significa producto con ID=1, cantidad=2
     * 
     * @param datos datos del pedido (usuarioId y productos)
     * @return pedido creado con código HTTP 201
     */
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Map<String, Object> datos) {
        // Extrae el usuarioId del JSON
        int usuarioId = (Integer) datos.get("usuarioId");

        // Extrae el Map de productos con cantidades
        @SuppressWarnings("unchecked")
        Map<String, Integer> productosStr = (Map<String, Integer>) datos.get("productos");

        // Convierte las keys de String a Integer (IDs de productos)
        Map<Integer, Integer> productos = new java.util.HashMap<>();
        for (Map.Entry<String, Integer> entry : productosStr.entrySet()) {
            try {
                // Demuestra manejo de EXCEPCIONES con NumberFormatException
                int productoId = Integer.parseInt(entry.getKey());
                int cantidad = entry.getValue();
                productos.put(productoId, cantidad);
            } catch (NumberFormatException e) {
                // Si la conversión falla, lanza una excepción descriptiva
                throw new com.techlab.excepciones.ValidacionException(
                        "ID de producto inválido: " + entry.getKey());
            }
        }

        // Crea el pedido
        Pedido pedido = pedidoService.crearPedido(usuarioId, productos);
        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
    }

    /**
     * GET /api/usuarios/{usuarioId}/pedidos
     * Obtiene todos los pedidos de un usuario específico
     * 
     * @param usuarioId ID del usuario
     * @return lista de pedidos del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorUsuario(@PathVariable int usuarioId) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(usuarioId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * PUT /api/pedidos/{id}/estado
     * Actualiza el estado de un pedido
     * Ejemplo de JSON: { "estado": "ENVIADO" }
     * 
     * @param id    ID del pedido
     * @param datos datos con el nuevo estado
     * @return pedido actualizado
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable int id,
            @RequestBody Map<String, String> datos) {
        String estadoStr = datos.get("estado");
        EstadoPedido nuevoEstado = EstadoPedido.valueOf(estadoStr);
        Pedido pedido = pedidoService.actualizarEstadoPedido(id, nuevoEstado);
        return ResponseEntity.ok(pedido);
    }

    /**
     * POST /api/pedidos/{id}/cancelar
     * Cancela un pedido y restaura el stock
     * 
     * @param id ID del pedido a cancelar
     * @return pedido cancelado
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable int id) {
        Pedido pedido = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * GET /api/pedidos/estado/{estado}
     * Busca pedidos por estado
     * 
     * @param estado estado del pedido (PENDIENTE, EN_PROCESO, etc.)
     * @return lista de pedidos con ese estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> buscarPorEstado(@PathVariable String estado) {
        EstadoPedido estadoPedido = EstadoPedido.valueOf(estado);
        List<Pedido> pedidos = pedidoService.buscarPorEstado(estadoPedido);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * GET /api/pedidos/usuario/{usuarioId}/total
     * Calcula el total gastado por un usuario
     * 
     * @param usuarioId ID del usuario
     * @return objeto con el total gastado
     */
    @GetMapping("/usuario/{usuarioId}/total")
    public ResponseEntity<Map<String, Double>> calcularTotalGastado(@PathVariable int usuarioId) {
        double total = pedidoService.calcularTotalGastadoPorUsuario(usuarioId);
        return ResponseEntity.ok(Map.of("totalGastado", total));
    }
}
