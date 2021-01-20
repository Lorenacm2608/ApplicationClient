/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import implementation.AdministradorManagerImplementation;

/**
 *
 * @author 2dam
 */
public class AdministradorFactory {
    public AdministradorManagerImplementation getAdministradorManagerImplementation (){
        return new AdministradorManagerImplementation();
    }
}