
package exceptions;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class EmailNoExisteException extends Exception{
    /**
     * Excepcion de email no encontrado
     */
    public EmailNoExisteException() {
        super("Email no encontrado");
    }
    
 
}
