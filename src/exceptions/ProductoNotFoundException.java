package exceptions;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class ProductoNotFoundException extends Exception {
    /**
     * Excepcion que nos informa que no se ha encontrado un producto
     */
    public ProductoNotFoundException() {
        super("Producto no encontrado");
    }
}
