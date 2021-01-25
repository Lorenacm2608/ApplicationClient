package exceptions;
/**
 *
 * @author Lorena Cáceres Manuel
 * 
 */
public class UsuarioYaExisteException extends Exception {
    /**
     * Excepcion de usuario ya dado de alta
     */
    public UsuarioYaExisteException (){
        super("Usuario ya dado de alta");
    }
}
