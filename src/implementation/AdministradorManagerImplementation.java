/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import client.AdministradorRESTClient;
import exceptions.DeleteException;
import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.InsertException;
import exceptions.ProveedorNotFoundException;
import exceptions.SelectException;
import exceptions.UpdateException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import manager.AdministradorManager;
import modelo.Proveedor;
import modelo.Vendedor;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class AdministradorManagerImplementation implements AdministradorManager {

    private AdministradorRESTClient webClient;
    private static final Logger LOG = Logger.getLogger(AdministradorManagerImplementation.class.getName());

    public AdministradorManagerImplementation() {
        webClient = new AdministradorRESTClient();
    }

    @Override
    public List<Vendedor> getVendedores() throws ClientErrorException, ErrorBDException, ErrorServerException {
        List<Vendedor> vendedores = null;
        vendedores = webClient.getVendedores(new GenericType<List<Vendedor>>() {
        });
        for (Vendedor v : vendedores) {
            LOG.log(Level.INFO, "Vendedores: {0}", v);
        }
        return vendedores;
    }

    @Override
    public void edit(Object requestEntity) throws ClientErrorException, UpdateException, ErrorBDException, ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Proveedor> getProveedores() throws ClientErrorException, ErrorBDException, ErrorServerException {
        List<Proveedor> proveedores = null;
        proveedores = webClient.getProveedores(new GenericType<List<Proveedor>>() {
        });
        for (Proveedor p : proveedores) {
            LOG.log(Level.INFO, "Proveedores: {0}", p);
        }
        return proveedores;
    }

    @Override
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException, SelectException, ErrorBDException, ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Object requestEntity) throws ClientErrorException, InsertException, ErrorBDException, ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) throws ClientErrorException, ProveedorNotFoundException, DeleteException, ErrorBDException, ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T getProveedores(Class<T> responseType) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}