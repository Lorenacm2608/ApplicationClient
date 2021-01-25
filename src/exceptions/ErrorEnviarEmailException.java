/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Fredy
 */
public class ErrorEnviarEmailException extends Exception{
     /**
     * Excepcion al no poder enviar el email
     */
    public ErrorEnviarEmailException(){
        super("No se ha podido enviar el email");
    }
}
