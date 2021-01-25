package exceptions;

/**
 *
 * @author Lorena Cáceres Manuel
 * 
 */
public class EmailYaExisteException extends Exception {
    /**
     * Excepcion de email en uso
     */
    public  EmailYaExisteException(){
    super("Email en uso");
}
}
