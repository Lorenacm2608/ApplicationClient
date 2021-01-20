package exceptions;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class VendedorNotFoundException extends Exception{

    /**
     * Excepcion que nos informa que no se ha encontrado el vendedor
     */
    public VendedorNotFoundException() {
        super("No se ha encontrado el vendedor");
    }
}