package exceptions;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class ProductoYaExisteException extends Exception {

    /**
     * Excepcion que nos informa que el producto no se ha
     * podido registrar porque ya existe
     */
    public ProductoYaExisteException() {
        super("El producto registrado ya existe");
    }
}
