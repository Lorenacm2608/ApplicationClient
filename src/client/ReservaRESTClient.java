/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.List;
import java.util.ResourceBundle;
import static javafx.scene.input.KeyCode.T;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import manager.ReservaManager;
import modelo.Reserva;

/**
 * Jersey REST client generated for REST resource:ReservaFacadeREST
 * [reserva]<br>
 * USAGE:
 * <pre>
 *        ReservaRESTClient client = new ReservaRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Fredy
 */
public class ReservaRESTClient implements ReservaManager {

    private WebTarget webTarget;
    private Client client;
    private ResourceBundle rb = ResourceBundle.getBundle("config.parametros");
    private final String BASE_URI = rb.getString("RESTful.baseURI");

    public ReservaRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("reserva");
    }

    public <T> T findReservasCanceladas(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("findReservasCanceladas");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void edit(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public <T> T findReservasConfirmadas(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("findReservasConfirmadas");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
    //igual quitar las Ts y poner List <Reservas>   y luego en el generic quitar la <T> y tambien el return, return list<Reservas>
    public List <Reserva> findReservas(GenericType responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("findReservas");
        return (List <Reserva>) resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
    /*
    public  <T> T Eliminar(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("DeleteReservas");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }*/
    
    
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void create(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public <T> T findReservasRealizadas(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("findReservasRealizadas");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void remove(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }
    

    public void close() {
        client.close();
    }

   
    public <T> T Eliminar(Class<T> responseType, String id) throws ClientErrorException {
      WebTarget resource = webTarget;
        resource = resource.path("DeleteReservas");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

}
