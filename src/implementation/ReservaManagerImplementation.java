/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import client.ReservaRESTClient;
import exceptions.ErrorServerException;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import manager.ReservaManager;
import modelo.Reserva;

/**
 *
 * @author Fredy
 */
public class ReservaManagerImplementation implements ReservaManager {

    private final ReservaRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("ReservaManagerImplementation");

    public ReservaManagerImplementation() {
        webClient = new ReservaRESTClient();
    }

    @Override
    public List<Reserva> findReservasCanceladas() throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Reserva reserva) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Reserva> findReservasConfirmadas() throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Reserva find(String id) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Reserva reserva) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Reserva> findReservasRealizadas() throws ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Reserva> findReservas() throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) throws ErrorServerException {
        try {
            webClient.remove(id);
        } catch (ClientErrorException e) {
            LOGGER.severe("ReservaManagerImplementation: remove " + e.getMessage());
            throw new ErrorServerException();
        }
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
