/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import client.VendedorRESTClient;
import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.InsertException;
import exceptions.ProveedorNotFoundException;
import exceptions.SelectException;
import exceptions.UpdateException;
import exceptions.VendedorNotFoundException;
import exceptions.VendedorYaExisteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import manager.ProveedorManager;
import manager.VendedorManager;
import modelo.Proveedor;
import modelo.Vendedor;

/**
 *
 * @author Moroni
 */
public class VendedorManagerImplementation implements VendedorManager {

    private static final Logger LOG = Logger.getLogger(VendedorManagerImplementation.class.getName());

    private VendedorRESTClient webClient;

    @Override
    public void remove(String id) throws ClientErrorException {
        try {
            webClient = new VendedorRESTClient();
            webClient.remove(id);
        } catch (ClientErrorException e) {
            LOG.log(Level.SEVERE, "ClientErrorException");
        }
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void edit(Vendedor vendedor) throws ClientErrorException, UpdateException, ErrorBDException, ErrorServerException, ProveedorNotFoundException {
        webClient = new VendedorRESTClient();
        webClient.edit(vendedor);
    }

    public Vendedor find(Vendedor vendedor, String id) throws ClientErrorException, SelectException, ProveedorNotFoundException, ErrorBDException, ErrorServerException {
        vendedor = null;
        try {
            vendedor = webClient.find(Vendedor.class, id);
        } catch (ClientErrorException e) {
            LOG.log(Level.SEVERE, "ClientErrorException");
        }
        return vendedor;
    }

    public Vendedor getVendedores(Vendedor vendedor, String id) throws ErrorBDException, ErrorServerException, VendedorNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param vendedor
     * @throws ClientErrorException
     * @throws InsertException
     * @throws VendedorYaExisteException
     * @throws ErrorBDException
     * @throws ErrorServerException
     */
    @Override
    public void create(Vendedor vendedor) throws ClientErrorException, InsertException, VendedorYaExisteException, ErrorBDException, ErrorServerException {
        webClient = new VendedorRESTClient();
        webClient.create(vendedor);
    }

    @Override
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public <T> T findAllVendedores(Class<T> responseType) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T findAllReservas(Class<T> responseType) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Object requestEntity) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
