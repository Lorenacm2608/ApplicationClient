/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import client.VendedorRESTClient;
import exceptions.ErrorServerException;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;
import manager.VendedorManager;
import modelo.Proveedor;
import modelo.Reserva;
import modelo.Vendedor;

/**
 *
 * @author Fredy Vargas Flores
 */
public class VendedorManagerImplementacion implements VendedorManager {

    private final VendedorRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("VendedorManagerImplementacion");

    public VendedorManagerImplementacion() {
        webClient = new VendedorRESTClient();
    }

    @Override
    public void edit(Vendedor vendedor) throws ErrorServerException {
        try {
            webClient.edit(vendedor);

        } catch (ClientErrorException e) {
            LOGGER.severe("VendedorManagerImplementacion: edit " + e.getMessage());
            throw new ErrorServerException();
        }

    }

    @Override
    public Vendedor find(String id) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Vendedor vendedor) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Reserva> findAllReservas() throws ErrorServerException {
        List<Reserva> reservas = null;
        try {
            reservas = webClient.findAllReservas(new GenericType<List<Reserva>>() {
            });

        } catch (ClientErrorException e) {
            LOGGER.severe("VendedorManagerImplementacion: findAllReservas " + e.getMessage());
            throw new ErrorServerException();
        }
        return reservas;
    }

    @Override
    public List<Proveedor> getProveedoresProducto() throws ErrorServerException {
        List<Proveedor> proveedores = null;
        try {
            proveedores = webClient.getProveedoresProducto(new GenericType<List<Proveedor>>() {
            });
        } catch (ClientErrorException e) {
            LOGGER.severe("VendedorManagerImplementacion: getProveedoresProducto " + e.getMessage());
            throw new ErrorServerException();
        }
        return proveedores;
    }

    @Override
    public List<Vendedor> findAllVendedores() throws ErrorServerException {
        List<Vendedor> vendedores = null;
        try {
            vendedores = webClient.findAllVendedores(new GenericType<List<Vendedor>>() {
            });

        } catch (ClientErrorException e) {
            LOGGER.severe("VendedorManagerImplementacion: findAllVendedores " + e.getMessage());
            throw new ErrorServerException();
        }
        return vendedores;
    }

    @Override
    public void remove(String id) throws ErrorServerException {
        try {
            webClient.remove(id);
        } catch (ClientErrorException e) {
            LOGGER.severe("UsuarioManagerImplmentation: find " + e.getMessage());
            throw new ErrorServerException();
        }

    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
