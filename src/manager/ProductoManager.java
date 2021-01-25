/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.ProductoExistenteException;
import java.util.Collection;
import javax.ws.rs.ClientErrorException;
import modelo.Producto;

/**
 *
 * @author 2dam
 */
public interface ProductoManager {

    public Collection<Producto> findAllRopa() throws ClientErrorException, ErrorBDException, ErrorServerException;

    ;

    public void edit(Producto producto) throws ProductoExistenteException, ClientErrorException, ProductoExistenteException, ErrorBDException, ErrorServerException;

    ;

    public Collection<Producto> findAllProductosAsc() throws ClientErrorException, ErrorBDException, ErrorServerException;

    ;

    public Producto find(String id) throws ClientErrorException, ErrorBDException, ErrorServerException;

    ;

    public void create(Producto producto) throws ProductoExistenteException, ProductoExistenteException, ClientErrorException, ErrorBDException, ErrorServerException;

    ;

    public Collection<Producto> findAllProductosDesc() throws ClientErrorException;

    public Collection<Producto> findAllZapatillas() throws ClientErrorException;

    public void remove(String id) throws ClientErrorException, ErrorBDException, ErrorServerException;

    ;

    public void close();

}
