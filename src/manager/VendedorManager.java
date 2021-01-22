/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.InsertException;
import exceptions.UpdateException;
import exceptions.VendedorNotFoundException;
import exceptions.VendedorYaExisteException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.WebTarget;
import modelo.Vendedor;

/**
 *
 * @author 2dam
 */
public interface VendedorManager {

    public void edit(Vendedor vendedor) throws ClientErrorException, UpdateException, ErrorBDException, ErrorServerException, VendedorNotFoundException;

    public <T> T find(Class<T> responseType, String id) throws ClientErrorException;

    public void create(Vendedor vendedor) throws ClientErrorException, InsertException, VendedorYaExisteException, ErrorBDException, ErrorServerException;

    public <T> T findAllReservas(Class<T> responseType) throws ClientErrorException;

    public void remove(String id) throws ClientErrorException;

    public void close();

}
