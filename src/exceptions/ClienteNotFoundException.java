package exceptions;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class ClienteNotFoundException extends Exception {

    /**
     * Excepcion para informar que el cliente no se ha encontrado
     */
    public ClienteNotFoundException() {
        super("Cliente no encontrado");
    }
}
