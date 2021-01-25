package exceptions;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class ClienteYaExisteException extends Exception {

    /**
     * Excepcion que nos informa que el cliente ya existe
     */
    public ClienteYaExisteException() {
        super("Cliente ya existe");
    }
}
