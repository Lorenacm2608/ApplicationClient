/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import exceptions.ErrorServerException;
import factory.UsuarioFactory;
import implementation.UsuarioManagerImplementation;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import modelo.Usuario;
import seguridad.Seguridad;
import validar.Validar;

/**
 * FXML Controller class
 *
 * @author Fredy
 */
public class RecuperarContraseniaController {

    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lblCorreoElectronico;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnVerificar;
    @FXML
    private PasswordField pfNuevaContrasenia;
    @FXML
    private PasswordField pfRepetirContrasenia;
    @FXML
    private TextField tfCodigoTemporal;
    @FXML
    private Label lblCodigoTemporal;
    @FXML
    private Label lblNuevaContrasenia;
    @FXML
    private Label lblRepetirContrasenia;
    @FXML
    private Label lblActualizarContrasenia;
    @FXML
    private Hyperlink hlReenviarCodigo;
    private Usuario usuario;
    private Stage stage = new Stage();
    private static final Logger LOG = Logger.getLogger("controllers.RecuperarContraseniaController");
    String codigoTemporal = "";
    Alert alert;

    /**
     * Recibe el escenario
     *
     * @return stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Establece el escenario.
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Establece un Usuario
     *
     * @param usuario Usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Inicia el escenario
     *
     * @param root Clase Parent
     */
    public void initStage(Parent root) {

        LOG.log(Level.INFO, "Ventana Recuperar Contraseña");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("¡Recuperar Contraseña!");
        stage.setResizable(false);
        stage.setOnCloseRequest(this::handleWindowClose);
        stage.setOnShowing(this::handleWindowShowing);
        btnAceptar.setOnAction(this::btnAceptarClick);
        btnAceptar.setTooltip(new Tooltip("Pulse para aceptar "));
        btnGuardar.setOnAction(this::btnGuardarClick);
        btnGuardar.setTooltip(new Tooltip("Pulse para guardar "));
        btnCancelar.setOnAction(this::btnCancelarClick);
        btnCancelar.setTooltip(new Tooltip("Pulse para cancelar "));
        btnVerificar.setOnAction(this::btnVerificarClick);
        btnVerificar.setTooltip(new Tooltip("Pulse para verificar "));
        hlReenviarCodigo.setOnAction(this::hlReenviarCodigoClick);
        hlReenviarCodigo.setTooltip(new Tooltip("Reenviar código "));
        //Direccion de email

        System.out.println(usuario.getEmail());
        tfCodigoTemporal.textProperty().addListener(this::txtChanged);
        pfNuevaContrasenia.textProperty().addListener(this::pfContraseniaChanged);
        pfRepetirContrasenia.textProperty().addListener(this::pfContraseniaChanged);
        stage.show();
    }

    /**
     * Al cerrar la ventana, saldrá un mensaje de confirmacion
     *
     * @param event, WindowEvent
     */
    private void handleWindowClose(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("LogIn");
        alert.setContentText("¿Estas seguro que quieres salir de la ventana?");
        Optional<ButtonType> respuesta = alert.showAndWait();

        if (respuesta.get() == ButtonType.OK) {
            LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
            stage.hide();
        } else {
            LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
            event.consume();
        }
    }

    /**
     * Configura los eventos al iniciar la ventana)
     *
     * @param event WindowEvent
     */
    private void handleWindowShowing(WindowEvent event) {
        LOG.log(Level.INFO, "Beginning LoginController::handleWindowShowing");

        lblCorreoElectronico.setText(prepararEmail(usuario.getEmail()));
        btnGuardar.setDisable(true);
        btnVerificar.setVisible(false);
        lblCodigoTemporal.setVisible(false);
        lblNuevaContrasenia.setVisible(false);
        lblRepetirContrasenia.setVisible(false);
        pfNuevaContrasenia.setVisible(false);
        pfRepetirContrasenia.setVisible(false);
        tfCodigoTemporal.setVisible(false);
        lblActualizarContrasenia.setVisible(false);
        hlReenviarCodigo.setVisible(false);

    }

    /**
     * Método que atualiza la contraseña y nos redirecciona a otra ventana
     *
     * @param event
     */
    private void btnGuardarClick(ActionEvent event) {
        if (pfNuevaContrasenia.getText().equals(pfRepetirContrasenia.getText())) {
            LOG.log(Level.INFO, "Ventana LOGIN");
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Guardar Cambios");
            alert.setContentText("¿Estas seguro que quieres guardar los cambios?");
            Optional<ButtonType> respuesta = alert.showAndWait();

            if (respuesta.get() == ButtonType.OK) {
                UsuarioManagerImplementation usuarioMI = (UsuarioManagerImplementation) new UsuarioFactory().getUsuarioManagerImplementation();
                usuario.setPassword(pfRepetirContrasenia.getText());
                LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
                try {
                    usuarioMI.edit(usuario);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn.fxml"));
                    Parent root = (Parent) loader.load();
                    LogInController controller = ((LogInController) loader.getController());
                    controller.initStage(root);
                    stage.hide();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
                } catch (ErrorServerException ex) {
                    LOG.log(Level.SEVERE, "ErrorServerException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                }

            } else {
                LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
                event.consume();
            }

        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Las contraseñas introducidas no coinciden");
            alert.showAndWait();
            pfNuevaContrasenia.requestFocus();
            pfNuevaContrasenia.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color:rgba(255,0,0,1);");
            pfNuevaContrasenia.setText("");
            pfRepetirContrasenia.setText("");

        }
    }

    /**
     * Genera la contraseña aleartoria y además estable valores a los
     * componentes
     *
     * @param event
     */
    private void btnAceptarClick(ActionEvent event) {
        UsuarioManagerImplementation usuarioMI = (UsuarioManagerImplementation) new UsuarioFactory().getUsuarioManagerImplementation();
        Alert alert;

        try {
            //Generá la contraseña
            codigoTemporal = Seguridad.generarContrasenia();
            tfCodigoTemporal.setVisible(true);
            lblCodigoTemporal.setVisible(true);
            hlReenviarCodigo.setVisible(true);
            btnVerificar.setDisable(true);
            btnVerificar.setVisible(true);
            tfCodigoTemporal.requestFocus();
            //Generamos una contraseña aleartoria y la enviamos al correo
            usuarioMI.enviarMensajeEmail(Seguridad.encriptarContrasenia(usuario.getEmail()), Seguridad.encriptarContrasenia(codigoTemporal));
            btnAceptar.setDisable(true);
        } catch (Exception e) {

        }
    }

    /**
     * Cancela la operacción de recuperar contraseña
     *
     * @param event
     */
    private void btnCancelarClick(ActionEvent event) {
        LOG.log(Level.INFO, "Ventana LOGIN");
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("LogIn");
        alert.setContentText("¿Estas seguro que quieres cancelar la recuperación de tu cuenta?");
        Optional<ButtonType> respuesta = alert.showAndWait();

        if (respuesta.get() == ButtonType.OK) {
            LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn.fxml"));
                Parent root = (Parent) loader.load();
                LogInController controller = ((LogInController) loader.getController());
                controller.initStage(root);
                stage.hide();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
            }
        } else {
            LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
            event.consume();
        }
    }

    /**
     * HyperLink se pondrá visible cuando el usuario reciba un correo (Reenvio
     * de la contraseña generada)
     *
     * @param event
     */
    private void hlReenviarCodigoClick(ActionEvent event) {

        UsuarioManagerImplementation usuarioMI = (UsuarioManagerImplementation) new UsuarioFactory().getUsuarioManagerImplementation();
        codigoTemporal = Seguridad.generarContrasenia();
        tfCodigoTemporal.setText("");
        try {
            pfNuevaContrasenia.setStyle("-fx-focus-color: #039ED3; -fx-faint-focus-color: #039ED322;");
            usuarioMI.enviarMensajeEmail(Seguridad.encriptarContrasenia(usuario.getEmail()), Seguridad.encriptarContrasenia(codigoTemporal));
            tfCodigoTemporal.requestFocus();
        } catch (Exception e) {

        }
    }

    /**
     * Establece algunas propiedades en los componentes
     *
     * @param event
     */
    private void btnVerificarClick(ActionEvent event) {
        if (codigoTemporal.equals(tfCodigoTemporal.getText())) {
            btnVerificar.setDisable(true);
            hlReenviarCodigo.setDisable(true);
            hlReenviarCodigo.setVisible(false);
            lblNuevaContrasenia.setVisible(true);
            lblRepetirContrasenia.setVisible(true);
            pfNuevaContrasenia.setVisible(true);
            pfRepetirContrasenia.setVisible(true);
            pfNuevaContrasenia.requestFocus();
            btnAceptar.setDisable(true);
            tfCodigoTemporal.setDisable(true);
            lblActualizarContrasenia.setVisible(true);

        } else {
            tfCodigoTemporal.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color:rgba(255,0,0,1);");
            tfCodigoTemporal.requestFocus();
        }

    }

    /**
     * Verificamos si ambas contraseñas coinciden
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void pfContraseniaChanged(ObservableValue observable, String oldValue, String newValue) {
        Validar.addTextLimiter(pfNuevaContrasenia, 30);
        Validar.addTextLimiter(pfRepetirContrasenia, 30);
        if (!pfNuevaContrasenia.getText().trim().equals("")) {
            if (!pfRepetirContrasenia.getText().trim().equals("")) {
                if (Validar.isValidPatternContrasena(pfNuevaContrasenia) && (Validar.isValidPatternContrasena(pfRepetirContrasenia))) {
                    btnGuardar.setDisable(false);
                } else {
                    btnGuardar.setDisable(true);

                }
            }
        }
    }

    /**
     * Añadimos escuchadores y definimos la longitud de los caracteres
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void txtChanged(ObservableValue observable, String oldValue, String newValue) {
        Validar.addTextLimiter(tfCodigoTemporal, 10);
        if (!tfCodigoTemporal.getText().trim().equals("")) {
            boolean isValidUsuario = Validar.isValidNombre(tfCodigoTemporal);
            if (isValidUsuario) {
                btnVerificar.setDisable(false);
            } else {
                btnVerificar.setDisable(true);

            }
        }
    }

    /**
     * Devuelve email censurado (a partir del 4 caracter se reeemplazará con "*"
     * hasta la "@" )
     *
     * @param email
     * @return
     */
    private String prepararEmail(String email) {
        String mostrar = email.substring(0, 4) + email.substring(4, email.indexOf("@")).replaceAll("[a-z]", "*")
                + email.substring(email.indexOf("@"), email.length());

        return mostrar;
    }

}
