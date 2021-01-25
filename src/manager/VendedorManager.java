/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import exceptions.ErrorServerException;
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

    public void edit(Vendedor vendedor) throws ErrorServerException;

    public Vendedor find(String id) throws ErrorServerException;

    public void create(Vendedor vendedor) throws ErrorServerException;

    public List<Reserva> findAllReservas() throws ErrorServerException;

    public List<Proveedor> getProveedoresProducto() throws ErrorServerException;

    public List<Vendedor> findAllVendedores() throws ErrorServerException;

    public void remove(String id) throws ErrorServerException;

    public void close();

}
