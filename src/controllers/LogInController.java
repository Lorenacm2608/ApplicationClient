package controllers;

import client.UsuarioRESTClient;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import factory.UsuarioFactory;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import manager.UsuarioManager;
import modelo.Usuario;
import seguridad.Seguridad;
import validar.Validar;

/**
 * FXML Controller class
 *
 * @author Moroni Collazos Fiestas
 */


public class LogInController {

    private static final Logger LOG = Logger.getLogger("controllers.LogInController");
    private final int treinta = 30;
    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private Button btnIniciar;
    @FXML
    private Hyperlink hlRegistrarse;
    @FXML
    private ImageView ivLogo;
    
    private Stage stage = new Stage();
    private Usuario usuario;

    public LogInController() {

    }

    /**
     * Recibe el escenario
     *
     * @return stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Establece el escenario
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Inicia el escenario
     *
     * @param root, clase parent
     */
    public void initStage(Parent root) {
        //Añadimos la imagen del logo de la aplicacion
        ivLogo.setImage(new Image("/img/logo.png"));
        LOG.log(Level.INFO, "Ventana LOGIN");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("¡Bienvenido a Flyshoes!");
        stage.setResizable(false);
        stage.setOnCloseRequest(this::handleWindowClose);
        stage.setOnShowing(this::handleWindowShowing);
        btnIniciar.setOnAction(this::btnIniciarClick);
        btnIniciar.setTooltip(new Tooltip("Pulse para iniciar sesion "));

        txtUsuario.textProperty().addListener(this::txtChanged);
        txtContrasena.textProperty().addListener(this::txtChanged);
        hlRegistrarse.setOnAction(this::hlRegistrarseClick);
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
        alert.setContentText("¿Estas seguro que quieres salir de la aplicación?");
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
        btnIniciar.setDisable(true);
        LOG.log(Level.INFO, "Beginning LoginController::handleWindowShowing");

    }

    /**
     * Valida los textos introducidos
     *
     * @param observable Observa los cambios
     * @param oldValue Valor antiguo
     * @param newValue Valor nuevo
     */
    private void txtChanged(ObservableValue observable, String oldValue, String newValue) {
        Validar.addTextLimiter(txtUsuario, treinta);
        Validar.addTextLimiterPass(txtContrasena, treinta);
        if (!txtUsuario.getText().trim().equals("") && !txtContrasena.getText().trim().equals("")) {
            boolean isValidUsuario = Validar.isValid(txtUsuario);

            txtContrasena.setStyle("-fx-focus-color: #039ED3; -fx-faint-focus-color: #039ED322;");

            if (isValidUsuario) {
                btnIniciar.setDisable(false);
            } else {
                btnIniciar.setDisable(true);

            }
        }
        if (txtUsuario.getText().trim().equals("") || txtContrasena.getText().trim().equals("")) {

            btnIniciar.setDisable(true);

        }
    }

    /**
     * Dirige a la ventana LogOut, la inicializa y envia usuario
     *
     * @param event ActionEvent
     */
    private void btnIniciarClick(ActionEvent event) {
        LOG.log(Level.INFO, "Ventana ");
        usuario = new Usuario();
        usuario.setLogin(txtUsuario.getText());
        usuario.setPassword(txtContrasena.getText());
        UsuarioManager usuarioM = new UsuarioFactory().getUsuarioRESTClient();
        Alert alert;
        try {
            //usuario = usuarioM.ClusuarioByLogin(Usuario.class, usuario.getLogin(), Seguridad.encriptarContrasenia(usuario.getPassword()));
            UsuarioRESTClient usuarioR = (UsuarioRESTClient) usuarioM;
            usuario = usuarioR.usuarioByLogin(Usuario.class, usuario.getLogin(), Seguridad.encriptarContrasenia(usuario.getPassword()));
            System.out.println(usuario.getLastAccess());
            FXMLLoader loader = null;
            Parent root = null;
            switch (usuario.getPrivilege()) {
                case ADMINISTRADOR:
                    loader = new FXMLLoader(getClass().getResource("/view/InicioAdministrador_vendedor.fxml"));
                    root = (Parent) loader.load();
                    InicioAdministradorVendedorController administradorC = ((InicioAdministradorVendedorController) loader.getController());
                    administradorC.setUsuario(usuario);
                    administradorC.initStage(root);
                    break;
                case VENDEDOR:
                    loader = new FXMLLoader(getClass().getResource("/view/InicioVendedor.fxml"));
                    root = (Parent) loader.load();
                    InicioVendedorController vendedorC = ((InicioVendedorController) loader.getController());
                    vendedorC.setUsuario(usuario);
                    vendedorC.initStage(root);
                    break;
                case CLIENTE:
                    loader = new FXMLLoader(getClass().getResource("/view/ListaDeProductos.fxml"));
                    root = (Parent) loader.load();
                    ListaDeProductosController listaDeProductosC = ((ListaDeProductosController) loader.getController());
                    listaDeProductosC.setUsuario(usuario);
                    listaDeProductosC.initStage(root);
            }

            stage.hide();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Se ha producido un error de E/S");

            /* } catch (AutenticacionFallidaException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Contraseña Incorrecta");
            alert.showAndWait();
            lblErrorContrasena.setText("Contraseña Incorrecta");
            lblErrorContrasena.setVisible(true);
            txtContrasena.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color:rgba(255,0,0,1);");
            txtContrasena.setText("");
            txtContrasena.requestFocus();
            //setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");

        } catch (ErrorBDException ex) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("LOGIN");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ErrorServerException ex) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("LOGIN");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (UsuarioNoEncontradoException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se ha encontrado el usuario introducido");
            alert.showAndWait();
            txtUsuario.setText("");
            txtContrasena.setText("");
             */
        }

    }

    /**
     * Dirige a la ventana SignUp y la inicializa
     *
     * @param event ActionEvent
     */
    private void hlRegistrarseClick(ActionEvent event) {
        LOG.log(Level.INFO, "Ventana LOGOUT");
        usuario = new Usuario();
        usuario.setFullname(txtUsuario.getText());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));

            Parent root = (Parent) loader.load();

            SignUpController controller = ((SignUpController) loader.getController());
            controller.initStage(root);
            stage.hide();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
        }
    }

}