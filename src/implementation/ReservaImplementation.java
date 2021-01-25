/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementacion;

import client.ReservaRESTClient;
import exceptions.ErrorBDException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import manager.ReservaManager;
import modelo.Reserva;


/**
 *
 * @author Fredy
 */
public class ReservaImplementation implements ReservaManager {

    private ReservaRESTClient webClient;
    private static final Logger LOG = Logger.getLogger(ReservaImplementation.class.getName());
    

    public ReservaImplementation() {
        webClient = new ReservaRESTClient();
    }

    public <T> T findReservasCanceladas(Class<T> responseType) throws ClientErrorException {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <T> T findReservasConfirmadas(Class<T> responseType) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  
    public Reserva find(Reserva reserva, String id) throws ClientErrorException {
       reserva = null;
        try {
            reserva = webClient.find(Reserva.class, id);
        } catch (ClientErrorException e) {
            LOG.log(Level.SEVERE, "ClientErrorException");
        }
        return reserva;
    }

    public void create(Reserva reserva) throws ClientErrorException {
        webClient = new ReservaRESTClient();
        webClient.create(reserva);
    }

    public <T> T findReservasRealizadas(Class<T> responseType) throws ClientErrorException {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void remove(String id) throws ClientErrorException {
         try {
            webClient.remove(id);
        } catch (ClientErrorException e) {
            LOG.log(Level.SEVERE, "ClientErrorException");
        }
    }

    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Reserva> findReservas() throws ClientErrorException {
        List<Reserva> reserva = null;
        reserva = webClient.findReservas(new GenericType<List<Reserva>>() {
        });
        for (Reserva res : reserva) {
            LOG.log(Level.INFO, "Reservas: {0}", res);
        }
        return reserva;
    }
    
    @Override
    public void edit(Reserva reserva) throws ClientErrorException{
        webClient.edit(reserva);
    }

}   
