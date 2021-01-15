/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import modelo.Reserva;

/**
 *
 * @author 2dam
 */
public interface ReservaManager {

    public <T> T findReservasCanceladas(Class<T> responseType) throws ClientErrorException;

    public void edit(Object requestEntity) throws ClientErrorException;

    public <T> T findReservasConfirmadas(Class<T> responseType) throws ClientErrorException;

    public <T> T find(Class<T> responseType, String id) throws ClientErrorException;

    public void create(Object requestEntity) throws ClientErrorException;

    public <T> T findReservasRealizadas(Class<T> responseType) throws ClientErrorException;
    
    public List <Reserva> findReservas(GenericType responseType) throws ClientErrorException;
    /*
    public <T> T Eliminar(Class<T> responseType, String id) throws ClientErrorException;
*/
    public void remove(String id) throws ClientErrorException;

    public void close();

}
