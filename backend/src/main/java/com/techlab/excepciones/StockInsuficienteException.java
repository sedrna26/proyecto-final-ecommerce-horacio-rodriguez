package com.techlab.excepciones;

/**
 * Excepción personalizada que se lanza cuando no hay suficiente stock de un
 * producto
 * Demuestra el manejo de EXCEPCIONES personalizadas en Java
 */
public class StockInsuficienteException extends RuntimeException {

    private int productoId;
    private String productoNombre;
    private int stockDisponible;
    private int cantidadSolicitada;

    /**
     * Constructor con mensaje simple
     * 
     * @param mensaje mensaje descriptivo del error
     */
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con detalles completos
     * 
     * @param mensaje            mensaje descriptivo
     * @param productoId         ID del producto
     * @param productoNombre     nombre del producto
     * @param stockDisponible    stock actual disponible
     * @param cantidadSolicitada cantidad que se intentó solicitar
     */
    public StockInsuficienteException(String mensaje, int productoId, String productoNombre,
            int stockDisponible, int cantidadSolicitada) {
        super(mensaje);
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    /**
     * Constructor con causa
     * 
     * @param mensaje mensaje descriptivo
     * @param causa   excepción que causó este error
     */
    public StockInsuficienteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    // Getters
    public int getProductoId() {
        return productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    /**
     * Genera un mensaje detallado del error
     * 
     * @return mensaje formateado con todos los detalles
     */
    public String getMensajeDetallado() {
        if (productoNombre != null) {
            return String.format(
                    "Stock insuficiente para el producto '%s' (ID: %d). " +
                            "Stock disponible: %d, Cantidad solicitada: %d",
                    productoNombre, productoId, stockDisponible, cantidadSolicitada);
        }
        return getMessage();
    }
}
