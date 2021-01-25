/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import client.VendedorRESTClient;
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
    public void edit(Vendedor vendedor) throws ClientErrorException {
        try {
            webClient.edit(vendedor);

        } catch (Exception e) {

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
    public List<Reserva> findAllReservas() throws ClientErrorException {
        List<Reserva> reservas = null;
        try {
            reservas = webClient.findAllReservas(new GenericType<List<Reserva>>() {
            });

        } catch (Exception e) {
            LOGGER.severe("findAllReservas:" + e.getMessage());
        }
        return reservas;
    }

    @Override
    public List<Proveedor> getProveedoresProducto() throws ClientErrorException {
        List<Proveedor> proveedores = null;
        try {
            proveedores = webClient.getProveedoresProducto(new GenericType<List<Proveedor>>() {
            });
            for (Proveedor p : proveedores) {
                System.out.println(p.toString());
            }
        } catch (Exception e) {
            LOGGER.severe("getProveedoresProducto:" + e.getMessage());
        }
        return proveedores;
    }

    @Override
    public List<Vendedor> findAllVendedores() throws ClientErrorException {
        List<Vendedor> vendedores = null;
        try {
            vendedores = webClient.findAllVendedores(new GenericType<List<Vendedor>>() {
            });

        } catch (Exception e) {
            LOGGER.severe("findAllVendedores:" + e.getMessage());
        }
        return vendedores;
    }

    @Override
    public void remove(String id) throws ClientErrorException {
        try {
            webClient.remove(id);
        } catch (Exception e) {

        }

    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
