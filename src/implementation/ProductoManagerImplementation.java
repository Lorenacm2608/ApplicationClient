/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import client.ProductoRESTClient;
import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.ProductoExistenteException;
import java.net.ConnectException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import manager.ProductoManager;
import modelo.Producto;

/**
 *
 * @author Fredy Vargas Flores
 */
public class ProductoManagerImplementation implements ProductoManager {

    private ProductoRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("ProductoManagerImplementation");

    public ProductoManagerImplementation() {
        webClient = new ProductoRESTClient();
    }

    @Override
    public Collection<Producto> findAllRopa() throws ClientErrorException , ErrorBDException, ErrorServerException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Producto producto) throws ProductoExistenteException, ClientErrorException,ErrorServerException {
        try {
            webClient.edit(producto);
        } catch (Exception e) {
            if(e.getCause() instanceof ConnectException){
                LOGGER.severe("ProductoManagerImplementation: ErrorServerException   " + e.getMessage());
                throw new ErrorServerException();
            }
            LOGGER.severe("edit:" + e.getMessage());
        }
    }

    @Override
    public Collection<Producto> findAllProductosAsc() throws ClientErrorException, ErrorBDException, ErrorServerException {
        List<Producto> productos = null;
        try {
            productos = webClient.findAllProductosAsc(new GenericType<List<Producto>>() {
            });

        } catch (Exception e) {
            LOGGER.severe("findAllProductosAsc:" + e.getMessage());
        }
        return productos;
    }

    @Override
    public Producto find(String id) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Producto producto) throws ProductoExistenteException, ClientErrorException {
        try {
            webClient.create(producto);
        } catch (Exception e) {
            LOGGER.severe("create:" + e.getMessage());
        }

    }

    @Override
    public Collection<Producto> findAllProductosDesc() throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Producto> findAllZapatillas() throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) throws ClientErrorException, ErrorBDException, ErrorServerException {
        try {
            webClient.remove(id);
        } catch (Exception e) {
            LOGGER.severe("remove:" + e.getMessage());
        }
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
