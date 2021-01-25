package exceptions;
/**
 *
 * @author Lorena Cáceres Manuel
 * 
 */
public class ErrorServerException extends Exception{
    /**
     * Excepcion de error de comunicacion con el servidor
     */
    public ErrorServerException(){
        super("Error de comunicacion con el servidor");
    }
}
