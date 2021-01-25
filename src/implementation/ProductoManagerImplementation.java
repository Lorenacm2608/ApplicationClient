/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import client.ProductoRESTClient;
import exceptions.ProductoExistenteException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import manager.ProductoManager;
import modelo.Producto;

/**
 *
 * @author Fredy
 */
public class ProductoManagerImplementation implements ProductoManager {

    private ProductoRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("ProductoManagerImplementation");
    
    public  ProductoManagerImplementation() {
        webClient = new ProductoRESTClient();
    }

    @Override
    public Collection<Producto> findAllRopa() throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void edit(Producto producto) throws ClientErrorException {
        webClient = new ProductoRESTClient();
        webClient.edit(producto);
    }

    @Override
    public List<Producto> findAllProductosAsc() throws ClientErrorException {
     List<Producto> productos=null;
        try{
            productos= webClient.findAllProductosAsc(new GenericType<List<Producto>>(){});
            for(Producto p:productos)
            System.out.println(p.toString());
        }catch(Exception e){
            LOGGER.severe("findAllProductosAsc:"+ e.getMessage());
        } 
    return productos;
    }

    @Override
    public Producto find(Producto producto, String id) throws ClientErrorException {
        producto = null;
        try {
            producto = webClient.find(Producto.class, id);
        } catch (ClientErrorException e) {
            LOGGER.log(Level.SEVERE, "ClientErrorException");
        }
        return producto;
    }

    @Override
    public void create(Producto producto) throws ProductoExistenteException, ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void remove(String id) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
