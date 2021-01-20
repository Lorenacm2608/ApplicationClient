package exceptions;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class ProveedorNotFoundException extends Exception {

    /**
     * Excepcion que nos informa que no se ha encontrado el proveedor
     */
    public ProveedorNotFoundException() {
        super("Proveedor no encontrado");
    }
}