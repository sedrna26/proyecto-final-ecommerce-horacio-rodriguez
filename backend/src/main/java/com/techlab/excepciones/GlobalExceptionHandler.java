package com.techlab.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la API REST
 * Captura las excepciones y devuelve respuestas HTTP apropiadas
 * Demuestra manejo de EXCEPCIONES con try/catch a nivel global
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción StockInsuficienteException
     * Devuelve HTTP 400 Bad Request
     * 
     * @param ex excepción capturada
     * @return respuesta con detalles del error
     */
    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> manejarStockInsuficiente(StockInsuficienteException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "Stock Insuficiente");
        cuerpo.put("mensaje", ex.getMensajeDetallado());

        // Agrega detalles adicionales si están disponibles
        if (ex.getProductoNombre() != null) {
            Map<String, Object> detalles = new HashMap<>();
            detalles.put("productoId", ex.getProductoId());
            detalles.put("productoNombre", ex.getProductoNombre());
            detalles.put("stockDisponible", ex.getStockDisponible());
            detalles.put("cantidadSolicitada", ex.getCantidadSolicitada());
            cuerpo.put("detalles", detalles);
        }

        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja la excepción RecursoNoEncontradoException
     * Devuelve HTTP 404 Not Found
     * 
     * @param ex excepción capturada
     * @return respuesta con detalles del error
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.NOT_FOUND.value());
        cuerpo.put("error", "Recurso No Encontrado");
        cuerpo.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(cuerpo, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepción ValidacionException
     * Devuelve HTTP 400 Bad Request
     * 
     * @param ex excepción capturada
     * @return respuesta con detalles del error
     */
    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(ValidacionException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "Error de Validación");
        cuerpo.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de validación de Bean Validation (@Valid)
     * Devuelve HTTP 400 Bad Request con detalles de cada campo inválido
     * 
     * @param ex excepción de validación
     * @return respuesta con errores por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacionArgumento(MethodArgumentNotValidException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        Map<String, String> errores = new HashMap<>();

        // Extrae los errores de validación de cada campo
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensajeError = error.getDefaultMessage();
            errores.put(nombreCampo, mensajeError);
        });

        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "Error de Validación");
        cuerpo.put("errores", errores);

        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones generales no capturadas específicamente
     * Devuelve HTTP 500 Internal Server Error
     * 
     * @param ex excepción capturada
     * @return respuesta genérica de error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarExcepcionGeneral(Exception ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.INTERNAL_SERVER_ERROR.value());
        cuerpo.put("error", "Error Interno del Servidor");
        cuerpo.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(cuerpo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja NumberFormatException cuando se intenta convertir un String a número
     * Demuestra manejo de excepciones de conversión de tipos
     * 
     * @param ex excepción capturada
     * @return respuesta con error de formato
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String, Object>> manejarFormatoNumero(NumberFormatException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("estado", HttpStatus.BAD_REQUEST.value());
        cuerpo.put("error", "Error de Formato");
        cuerpo.put("mensaje", "El valor proporcionado no es un número válido");

        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }
}
