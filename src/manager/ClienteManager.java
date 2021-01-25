/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.WebTarget;
import modelo.Cliente;

/**
 *
 *   @author 2dam
 */
public interface ClienteManager {

    
    public void edit(Cliente cliente) throws ClientErrorException;

    public <T> T findAllProductosAsc(Class<T> responseType) throws ClientErrorException;

    public <T> T find(Class<T> responseType, String id) throws ClientErrorException;
    
    public List<Cliente> findCliente() throws ClientErrorException;

    public <T> T findReserva(Class<T> responseType, String id) throws ClientErrorException;

    public void create(Object requestEntity) throws ClientErrorException;

    public void remove(String id) throws ClientErrorException;

    public void close();
}
