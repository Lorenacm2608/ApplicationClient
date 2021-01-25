/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import implementation.ProductoManagerImplementation;
import manager.ProductoManager;
import client.ProductoRESTClient;

/**
 *
 *  @author 2dam
 */
public class ProductoFactory {
    public ProductoManager getProductoManagerImplementation (){
        return new ProductoManagerImplementation();
    }
}
