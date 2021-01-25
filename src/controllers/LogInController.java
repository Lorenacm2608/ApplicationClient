package controllers;

import exceptions.AutenticacionFallidaException;
import exceptions.ErrorServerException;
import exceptions.UsuarioNotFoundException;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import factory.UsuarioFactory;
import implementation.UsuarioManagerImplementation;
import java.util.Optional;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javax.ws.rs.ClientErrorException;
import manager.UsuarioManager;
import modelo.Usuario;
import seguridad.Seguridad;
import validar.Validar;

/**
 * FXML Controller class
 *
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

    @FXML
    private Pane pnPrincipal;

    @FXML
    private Hyperlink hlContraseniaOlvidada;

    private Stage stage = new Stage();
    private Usuario usuario;
    private UsuarioManager usuarioManager;

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
        hlContraseniaOlvidada.setOnAction(this::hlContraseniaOlvidadClick);
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
        hlContraseniaOlvidada.setVisible(false);
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
        if (oldValue != newValue) {
            hlContraseniaOlvidada.setVisible(false);
        }
        if (!txtUsuario.getText().trim().equals("") && !txtContrasena.getText().trim().equals("")) {

            boolean isValidUsuario = Validar.isValidUsuario(txtUsuario);

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
        LOG.log(Level.INFO, "btnIniciarClick");
        usuario = new Usuario();
        usuario.setLogin(txtUsuario.getText());
        usuario.setPassword(txtContrasena.getText());
        UsuarioManagerImplementation usuarioMi = (UsuarioManagerImplementation) new UsuarioFactory().getUsuarioManagerImplementation();
        Alert alert;

        try {
            System.out.println(usuario.getPassword());
            //El usuario(login) se encuentra en la base de datos
            usuario = usuarioMi.usuarioLogin(usuario.getLogin());
            System.out.println(usuario.getEmail());
            //El usuario ya se encuentra en la base de datos ahora comprobamos su contraseña
            usuarioMi.usuarioByLogin(usuario.getLogin(), Seguridad.encriptarContrasenia(txtContrasena.getText()));
            System.out.println(usuario.getLastAccess());
            FXMLLoader loader = null;
            Parent root = null;
            switch (usuario.getPrivilege()) {
                case ADMINISTRADOR:
                    loader = new FXMLLoader(getClass().getResource("/view/inicioAdministrador_proveedor.fxml"));
                    root = (Parent) loader.load();
                    InicioAdministradorProveedorController administradorC = ((InicioAdministradorProveedorController) loader.getController());
                    administradorC.setUsuario(usuario);
                    administradorC.initStage(root);
                    break;
                case VENDEDOR:
                    loader = new FXMLLoader(getClass().getResource("/view/InicioVendedorProducto.fxml"));
                    root = (Parent) loader.load();
                    InicioVendedorProductoController vendedorC = ((InicioVendedorProductoController) loader.getController());
                    vendedorC.setUsuario(usuario);
                    vendedorC.initStage(root);
            }

            stage.hide();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Se ha producido un error de E/S");

        } catch (AutenticacionFallidaException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login ERROR");
            alert.setHeaderText("Contraseña Incorrecta");
            alert.showAndWait();
            //txtContrasena.setStyle("-fx-faint-focus-color: transparent; -fx-focus-color:rgba(255,0,0,1);");
            txtContrasena.setText("");
            txtContrasena.requestFocus();
            hlContraseniaOlvidada.setVisible(true);
        } catch (ErrorServerException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login ERROR");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ClientErrorException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login ERROR");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (UsuarioNotFoundException ex) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login ERROR");
            alert.setHeaderText("No se ha encontrado el usuario introducido");
            alert.showAndWait();
            txtUsuario.setText("");
            txtContrasena.setText("");
        }
    }

    /**
     * Dirige a la ventana SignUp y la inicializa
     *
     * @param event ActionEvent
     */
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

    private void hlContraseniaOlvidadClick(ActionEvent event) {
        LOG.log(Level.INFO, "Ventana Contaseña Olvidada");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RecuperarContrasenia.fxml"));
            Parent root = (Parent) loader.load();
            RecuperarContraseniaController recuperarContraseniaC = ((RecuperarContraseniaController) loader.getController());
            recuperarContraseniaC.setUsuario(usuario);
            recuperarContraseniaC.initStage(root);
            stage.hide();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
        }
    }

}