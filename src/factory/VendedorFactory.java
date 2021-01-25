/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import manager.VendedorManager;
import client.VendedorRESTClient;
import implementation.VendedorManagerImplementation;

/**
 *
 * @author Moroni
 */
public class VendedorFactory {
    public VendedorManagerImplementation getVendedorManagerImplementation (){
        return new VendedorManagerImplementation();
    }
}
