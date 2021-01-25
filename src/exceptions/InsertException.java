package exceptions;

/**
 * Excepción que se lanza cuando ocurre algun problema a la hora de crear
 *
 * @author Lorena Cáceres Manuel
 */
public class InsertException extends Exception {

    /**
     * Constructor vacio
     */
    public InsertException() {
    }

    /**
     * Constructor el cual recibe un mensaje detallado
     *
     * @param message
     */
    public InsertException(String message) {
        super(message);
    }
}
