/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import implementation.ProveedorManagerImplementation;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class ProveedorFactory {
    public ProveedorManagerImplementation getProveedorManagerImplementation (){
        return new ProveedorManagerImplementation();
    }
}
