package com.techlab.excepciones;

/**
 * Excepción que se lanza cuando no se encuentra un recurso solicitado
 * Demuestra manejo de EXCEPCIONES personalizadas
 */
public class RecursoNoEncontradoException extends RuntimeException {

    private String recurso;
    private String campo;
    private Object valor;

    /**
     * Constructor con mensaje simple
     * 
     * @param mensaje mensaje del error
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con detalles del recurso no encontrado
     * 
     * @param recurso tipo de recurso (ej: "Producto", "Usuario")
     * @param campo   campo usado para buscar (ej: "id", "email")
     * @param valor   valor buscado
     */
    public RecursoNoEncontradoException(String recurso, String campo, Object valor) {
        super(String.format("%s no encontrado con %s: '%s'", recurso, campo, valor));
        this.recurso = recurso;
        this.campo = campo;
        this.valor = valor;
    }

    // Getters
    public String getRecurso() {
        return recurso;
    }

    public String getCampo() {
        return campo;
    }

    public Object getValor() {
        return valor;
    }
}
