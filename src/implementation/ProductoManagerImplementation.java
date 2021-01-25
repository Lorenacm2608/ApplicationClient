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
    public Collection<Producto> findAllRopa() throws ClientErrorException, ErrorBDException, ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Producto producto) throws ProductoExistenteException, ErrorServerException {
        try {
            webClient.edit(producto);
        } catch (Exception e) {
            if (e instanceof ConnectException) {
                LOGGER.severe("ProductoManagerImplementation: edit " + e.getMessage());
                throw new ErrorServerException();
            }
        }
    }

    @Override
    public Collection<Producto> findAllProductosAsc() throws  ErrorBDException, ErrorServerException {
        List<Producto> productos = null;
        try {
            productos = webClient.findAllProductosAsc(new GenericType<List<Producto>>() {
            });

        } catch (Exception e) {
            if (e instanceof ConnectException) {
                LOGGER.severe("ProductoManagerImplementation: findAllProductosAsc " + e.getMessage());
                throw new ErrorServerException();
            }
        }
        return productos;
    }

    @Override
    public Producto find(String id) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Producto producto) throws ProductoExistenteException,  ErrorServerException {
        try {
            webClient.create(producto);
        } catch (Exception e) {
            if (e instanceof ConnectException) {
                LOGGER.severe("ProductoManagerImplementation: create " + e.getMessage());
                throw new ErrorServerException();
            }
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
    public void remove(String id) throws ConnectException, ErrorBDException, ErrorServerException {
        try {
            webClient.remove(id);
        } catch (Exception e) {
            if (e instanceof ConnectException) {
                LOGGER.severe("ProductoManagerImplementation: remove " + e.getMessage());
                throw new ErrorServerException();
            }
        }
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
