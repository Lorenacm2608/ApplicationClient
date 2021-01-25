package exceptions;

/**
 * Excepción que se lanza cuando ocurre algun problema a la hora de actualizar
 *
 * @author Lorena Cáceres Manuel
 */
public class UpdateException extends Exception {

    /**
     * Constructor vacío
     */
    public UpdateException() {
    }

    /**
     * Constructor el cual recibe un mensaje detallado
     *
     * @param message
     */
    public UpdateException(String message) {
        super(message);
    }
}
