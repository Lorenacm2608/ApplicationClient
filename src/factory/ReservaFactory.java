/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factory;

import implementacion.ReservaImplementation;
import client.ReservaRESTClient;
import manager.ReservaManager;

/**
 *
 *  @author 2dam
 */
public class ReservaFactory {
    public ReservaManager getReservaImplementation (){
        return new ReservaImplementation();
    }
    
}
