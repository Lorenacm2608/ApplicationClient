package exceptions;
/**
 *
 * @author Lorena Cáceres Manuel
 * 
 */
public class UsuarioNotFoundException extends Exception {
    /**
     * Excepcion de usuario introducido no encontrado
     */
    public UsuarioNotFoundException(){
        super("El usuario introducido no se ha encontrado");
    }
}
