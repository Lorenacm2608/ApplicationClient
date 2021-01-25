package exceptions;
/**
 *
 * @author Lorena Cáceres Manuel
 * 
 */
public class ErrorBDException extends Exception  {
    /**
     * Excepcion de error en la base de datos
     */
    public ErrorBDException(){
        super("Error en la Base de Datos");
    }
    
}
