/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.util.List;
import javax.ws.rs.ClientErrorException;
import modelo.Proveedor;
import modelo.Reserva;
import modelo.Vendedor;

/**
 *
 * @author 2dam
 */
public interface VendedorManager {

    public void edit(Object requestEntity) throws ClientErrorException;

    public Vendedor find(String id) throws ClientErrorException;

    public void create(Vendedor vendedor) throws ClientErrorException;

    public List<Reserva> findAllReservas() throws ClientErrorException;

    public List<Proveedor> getProveedoresProducto() throws ClientErrorException;

    public void remove(String id) throws ClientErrorException;

    public void close();

}
