/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import exceptions.ErrorServerException;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import modelo.Reserva;

/**
 *
 * @author Fredy Vargas Flores
 */
public interface ReservaManager {

    public List<Reserva> findReservasCanceladas() throws ClientErrorException;

    public void edit(Reserva reserva) throws ClientErrorException;

    public List<Reserva> findReservasConfirmadas() throws ClientErrorException;

    public Reserva find(Reserva reserva, String id) throws ClientErrorException;

    public void create(Reserva reserva) throws ClientErrorException;

    public List<Reserva> findReservasRealizadas() throws ErrorServerException;

    public List<Reserva> findReservas() throws ClientErrorException;

    public void remove(String id) throws ErrorServerException;

    public void close();
}
