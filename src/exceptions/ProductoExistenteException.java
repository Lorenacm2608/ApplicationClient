package exceptions;
/**
 *
 * @author Fredy
 */
public class ProductoExistenteException extends Exception {
    /**
     * Excepcion el producto ya existe
     */
    public ProductoExistenteException(){
        super("El producto ya existe");
    }
}
