/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import client.ClienteRESTClient;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import manager.ClienteManager;
import modelo.Cliente;

/**
 * Jersey REST client generated for REST resource:ClienteFacadeREST
 * [cliente]<br>
 * USAGE:
 * <pre>
 *        ClienteRESTClient client = new ClienteRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Fredy
 */
public class ClienteImplementation implements ClienteManager {

    private ClienteRESTClient webClient;
    private static final Logger LOG = Logger.getLogger(ClienteImplementation.class.getName());

    public ClienteImplementation() {
        webClient = new ClienteRESTClient();
    }

    public void edit(Cliente cliente) throws ClientErrorException {
        webClient = new ClienteRESTClient();
        webClient.edit(cliente);
    }

    public <T> T findAllProductosAsc(Class<T> responseType) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <T> T find(Class<T> responseType, String id) throws ClientErrorException {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <T> T findReserva(Class<T> responseType, String id) throws ClientErrorException {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void create(Object requestEntity) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void remove(String id) throws ClientErrorException {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void close() {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Cliente> findCliente() throws ClientErrorException {
        List<Cliente> cliente = null;
        cliente = webClient.findCliente(new GenericType<List<Cliente>>() {
        });
        for (Cliente res : cliente) {
            LOG.log(Level.INFO, "Clientes: {0}", res);
        }
        return cliente;
    }

}
