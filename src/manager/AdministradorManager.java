/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import exceptions.DeleteException;
import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.InsertException;
import exceptions.ProveedorNotFoundException;
import exceptions.SelectException;
import exceptions.UpdateException;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.WebTarget;
import modelo.Proveedor;
import modelo.Vendedor;

/**
 *
 * @author 2dam
 */
public interface AdministradorManager {

    public List<Vendedor> getVendedores() throws ClientErrorException, ErrorBDException, ErrorServerException;

    public void edit(Object requestEntity) throws ClientErrorException, UpdateException, ErrorBDException, ErrorServerException;

    public List<Proveedor> getProveedores() throws ClientErrorException, ErrorBDException, ErrorServerException;

    public <T> T find(Class<T> responseType, String id) throws ClientErrorException, SelectException, ErrorBDException, ErrorServerException;

    public void create(Object requestEntity) throws ClientErrorException, InsertException, ErrorBDException, ErrorServerException;

    public void remove(String id) throws ClientErrorException, ProveedorNotFoundException, DeleteException, ErrorBDException, ErrorServerException;

    public void close();

}
