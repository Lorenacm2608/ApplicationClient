package implementation;

import client.AdministradorRESTClient;
import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import java.net.ConnectException;
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
        try {
            vendedores = webClient.getVendedores(new GenericType<List<Vendedor>>() {
            });
            for (Vendedor v : vendedores) {
                LOG.log(Level.INFO, "Vendedores: {0}", v);
            }
        } catch (ClientErrorException e) {
            if (e.getCause() instanceof ConnectException) {
                throw new ErrorServerException();
            } else {
                throw new ErrorBDException();
            }
        }
        return vendedores;
    }

    @Override
    public void edit(Object requestEntity) throws ClientErrorException, ErrorBDException, ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Proveedor> getProveedores() throws ClientErrorException, ErrorBDException, ErrorServerException {
        List<Proveedor> proveedores = null;
        try {
            proveedores = webClient.getProveedores(new GenericType<List<Proveedor>>() {
            });
            for (Proveedor p : proveedores) {
                LOG.log(Level.INFO, "Proveedores: {0}", p);
            }
        } catch (ClientErrorException e) {
            if (e.getCause() instanceof ConnectException) {
                throw new ErrorServerException();
            } else {
                throw new ErrorBDException();
            }
        }
        return proveedores;
    }

    @Override
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException, ErrorBDException, ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Object requestEntity) throws ClientErrorException, ErrorBDException, ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(String id) throws ClientErrorException, ErrorBDException, ErrorServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
