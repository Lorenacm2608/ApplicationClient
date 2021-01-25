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
import exceptions.ProductoNotFoundException;
import exceptions.ProveedorNotFoundException;
import exceptions.ProveedorYaExisteException;
import exceptions.SelectException;
import exceptions.UpdateException;
import javax.ws.rs.ClientErrorException;
import modelo.Proveedor;

/**
 *
 * @author 2dam
 */
public interface ProveedorManager {

    public void edit(Proveedor proveedor) throws ClientErrorException, ErrorBDException, ErrorServerException ;

    public Proveedor find(Proveedor proveedor, String id) throws ClientErrorException, ProveedorNotFoundException, ErrorBDException, ErrorServerException;

    public Proveedor getProductos(Proveedor proveedor, String id) throws ErrorBDException, ErrorServerException, ProductoNotFoundException;

    public void create(Proveedor proveedor) throws ClientErrorException, ProveedorYaExisteException, ErrorBDException, ErrorServerException;

    public void remove(String id) throws ClientErrorException, ErrorBDException, ErrorServerException;

    public void close();

}
