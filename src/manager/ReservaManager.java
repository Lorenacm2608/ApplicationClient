/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import exceptions.ErrorBDException;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import modelo.Reserva;

/**
 *
 *  @author Nadir
 */
public interface ReservaManager {

    public <T> T findReservasCanceladas(Class<T> responseType) throws ClientErrorException;

    public void edit(Reserva reserva) throws ClientErrorException;

    public <T> T findReservasConfirmadas(Class<T> responseType) throws ClientErrorException;

    public Reserva find(Reserva reserva, String id) throws ClientErrorException;

    public void create(Reserva reserva) throws ClientErrorException;

    public <T> T findReservasRealizadas(Class<T> responseType) throws ClientErrorException;
    
    public List<Reserva> findReservas() throws ClientErrorException;

    public void remove (String id) throws ClientErrorException;

    public void close();

}