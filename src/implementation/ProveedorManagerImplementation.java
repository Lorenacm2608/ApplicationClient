package implementation;

import client.ProveedorRESTClient;
import exceptions.ErrorServerException;
import exceptions.ProductoNotFoundException;
import exceptions.ProveedorNotFoundException;
import exceptions.ProveedorYaExisteException;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import manager.ProveedorManager;
import modelo.Proveedor;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class ProveedorManagerImplementation implements ProveedorManager {

    private static final Logger LOG = Logger.getLogger(ProveedorManagerImplementation.class.getName());

    private ProveedorRESTClient webClient;

    @Override
    public void remove(String id) throws ClientErrorException, ErrorServerException {
        try {
            webClient = new ProveedorRESTClient();
            webClient.remove(id);
        } catch (ClientErrorException e) {
            if (e.getCause() instanceof ConnectException) {
                LOG.log(Level.SEVERE, "ErrorServerException");
                throw new ErrorServerException();
            }
        }
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void edit(Proveedor proveedor) throws ClientErrorException, ErrorServerException {
        webClient = new ProveedorRESTClient();
        try {
            webClient.edit(proveedor);
        } catch (ClientErrorException e) {
            if (e.getCause() instanceof ConnectException) {
                LOG.log(Level.SEVERE, "ErrorServerException");
                throw new ErrorServerException();
            }
        }
    }

    @Override
    public Proveedor find(Proveedor proveedor, String id) throws ClientErrorException, ProveedorNotFoundException, ErrorServerException {
        proveedor = null;
        try {
            proveedor = webClient.find(Proveedor.class, id);
        } catch (ClientErrorException e) {
            if (e.getCause() instanceof ConnectException) {
                LOG.log(Level.SEVERE, "ErrorServerException");
                throw new ErrorServerException();
            }
        }
        return proveedor;
    }

    @Override
    public Proveedor getProductos(Proveedor proveedor, String id) throws  ErrorServerException, ProductoNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Proveedor proveedor) throws ErrorServerException {
        try {
            webClient = new ProveedorRESTClient();
            webClient.create(proveedor);
        } catch (ClientErrorException e) {
            if (e.getCause() instanceof ConnectException) {
                LOG.log(Level.SEVERE, "ErrorServerException");
                throw new ErrorServerException();
            } 
        }
    }

}
