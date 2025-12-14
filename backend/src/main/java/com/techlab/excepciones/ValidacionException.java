package com.techlab.excepciones;

/**
 * Excepción que se lanza cuando hay un error de validación de negocio
 */
public class ValidacionException extends RuntimeException {

    /**
     * Constructor con mensaje
     * 
     * @param mensaje mensaje de error
     */
    public ValidacionException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * 
     * @param mensaje mensaje de error
     * @param causa   causa del error
     */
    public ValidacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
