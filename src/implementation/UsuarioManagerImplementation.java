/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import client.UsuarioRESTClient;
import exceptions.AutenticacionFallidaException;
import exceptions.ErrorBDException;
import exceptions.ErrorEnviarEmailException;
import exceptions.ErrorServerException;
import exceptions.UsuarioNotFoundException;
import exceptions.UsuarioYaExisteException;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import manager.UsuarioManager;
import modelo.Usuario;

/**
 *
 * @author Fredy
 */
public class UsuarioManagerImplementation implements UsuarioManager {

    private UsuarioRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("UsuarioManagerImplmentation");

    public UsuarioManagerImplementation() {
        webClient = new UsuarioRESTClient();
    }

    @Override
    public void edit(Usuario usuario) throws ClientErrorException {
        try {
            webClient.edit(usuario);
        } catch (Exception e) {

        }
    }

    @Override
    public Usuario find(String id) throws ClientErrorException {
        Usuario usuario = null;
        try {
            usuario = webClient.find(Usuario.class, id);
        } catch (Exception e) {
            LOGGER.severe("find: ClientErrorException");

        }
        return usuario;
    }

    @Override
    public Usuario usuarioByLogin(String login, String pass) throws AutenticacionFallidaException {
        Usuario usuario = null;
        try {
            usuario = webClient.usuarioByLogin(Usuario.class, login, pass);
        } catch (Exception e) {
            LOGGER.severe("usuarioLogin: AutenticacionFallidaException");
            throw new AutenticacionFallidaException();
        }
        return usuario;
    }

    @Override
    public Usuario usuarioLogin(String login) throws UsuarioNotFoundException, ErrorServerException {
        Usuario usuario = null;
        try {
            usuario = webClient.usuarioLogin(Usuario.class, login);
        } catch (Exception e) {

            if (e.getCause() instanceof ConnectException) {
                LOGGER.log(Level.SEVERE, "usuarioLogin: ErrorServerException   {0}", e.getMessage());
                throw new ErrorServerException();
            } else {
                LOGGER.log(Level.SEVERE, "usuarioLogin: UsuarioNoEncontradoException   {0}", e.getMessage());
                throw new UsuarioNotFoundException();
            }
        }
        return usuario;
    }

    @Override
    public void create(Usuario usuario) throws ClientErrorException, UsuarioYaExisteException, ErrorServerException, ErrorBDException {
        try {
            webClient = new UsuarioRESTClient();
            webClient.create(usuario);
        } catch (Exception e) {
            if (e.getCause() instanceof ConnectException) {
                LOGGER.log(Level.SEVERE, "createUsuario: ErrorServerException {0}", e.getMessage());
                throw new ErrorServerException();
            } else{
                LOGGER.log(Level.SEVERE, "createUsuario: UsuarioYaExisteException {0}", e.getMessage());
                throw new UsuarioYaExisteException();
            }

        }

    }

    @Override
    public void remove(String id) throws ClientErrorException {
        try {
            webClient.remove(id);
        } catch (Exception e) {

        }
    }

    @Override
    public void close() {
        webClient.close();
    }

    @Override
    public Usuario enviarMensajeEmail(String email, String pass) throws ErrorEnviarEmailException {
        Usuario usuario = null;
        try {
            usuario = webClient.enviarMensajeEmail(Usuario.class, email, pass);
        } catch (Exception e) {
            LOGGER.severe("enviarMensajeEmail: ErrorEnviarEmailExcpetion");
            throw new ErrorEnviarEmailException();
        }
        return usuario;
    }

}
