package com.techlab.productos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad Producto
 * Extiende JpaRepository que proporciona métodos CRUD automáticos
 * Demuestra uso de Spring Data JPA para acceso a datos
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    /**
     * Busca productos por categoría
     * Método de consulta derivado (Query Method)
     * 
     * @param categoria categoría a buscar
     * @return lista de productos de esa categoría
     */
    List<Producto> findByCategoria(String categoria);

    /**
     * Busca productos activos
     * 
     * @param activo estado del producto
     * @return lista de productos activos o inactivos
     */
    List<Producto> findByActivo(boolean activo);

    /**
     * Obtiene los productos más vendidos
     * Query nativa que cuenta las ventas por producto y retorna los top
     * 
     * @return lista de productos ordenados por cantidad vendida
     */
    @Query(value = "SELECT p.* FROM productos p " +
            "INNER JOIN lineas_pedido lp ON p.id = lp.producto_id " +
            "GROUP BY p.id " +
            "ORDER BY SUM(lp.cantidad) DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Producto> findTopProductosMasVendidos();

    /**
     * Busca productos por nombre (búsqueda parcial, ignorando mayúsculas)
     * 
     * @param nombre nombre o parte del nombre
     * @return lista de productos encontrados
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca productos con stock disponible
     * Consulta JPQL personalizada
     * 
     * @param stockMinimo stock mínimo requerido
     * @return lista de productos con stock >= stockMinimo
     */
    @Query("SELECT p FROM Producto p WHERE p.stock >= :stockMinimo AND p.activo = true")
    List<Producto> findProductosConStock(int stockMinimo);

    /**
     * Busca productos por rango de precio
     * 
     * @param precioMin precio mínimo
     * @param precioMax precio máximo
     * @return lista de productos en ese rango de precio
     */
    List<Producto> findByPrecioBetween(double precioMin, double precioMax);

    /**
     * Busca productos de una categoría que estén activos
     * Demuestra combinación de criterios
     * 
     * @param categoria categoría del producto
     * @param activo    estado del producto
     * @return lista de productos filtrados
     */
    List<Producto> findByCategoriaAndActivo(String categoria, boolean activo);
}
