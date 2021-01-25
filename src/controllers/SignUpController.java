package controllers;

import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.UsuarioYaExisteException;
import implementation.UsuarioManagerImplementation;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.ClientErrorException;
import manager.UsuarioManager;
import modelo.EstadoUsuario;
import modelo.PrivilegioUsuario;
import modelo.Usuario;
import validar.Validar;

/**
 * FXML Controller class
 *
 * @author Lorena Cáceres Manuel
 */
public class SignUpController {

    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnRegistrarse;
    @FXML
    private PasswordField pfContrasenia;
    @FXML
    private PasswordField pfConfirmarContrasenia;
    @FXML
    private AnchorPane apSignUp;
    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfCorreoElectronico;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfTelefono;

    private final int treinta = 30;
    private final int cincuenta = 50;
    private final int caracteresMax = 250;
    private Usuario usuario;
    private UsuarioManager usuarioManager;
    private Alert alert;
    private Stage stage = new Stage();
    private static final Logger LOG = Logger.getLogger("controllers.SignUpController");

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
     * Devuelve un usuario
     *
     * @return usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Inicia el escenario
     *
     * @param root Clase Parent
     */
    public void initStage(Parent root) {
        LOG.log(Level.INFO, "Ventana SignUp");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("¡Bienvenido a Flyshoes!");
        stage.setResizable(false);
        //Indicamos el metodo que se encargara de la apariencia del stage cuando se cierre la ventana
        stage.setOnCloseRequest(this::handleWindowClose);
        //Indicamos el metodo que se encargara de la apariencia del stage cuando se inicie la ventana
        stage.setOnShowing(this::handleWindowShowing);
        btnRegistrarse.setOnAction(this::btnRegistrarseClick);
        btnCancelar.setOnAction(this::btnCancelarClick);
        pfConfirmarContrasenia.textProperty().addListener(this::txtChanged);
        tfUsuario.textProperty().addListener(this::txtChanged);
        tfNombre.textProperty().addListener(this::txtChanged);
        tfDireccion.textProperty().addListener(this::txtChanged);
        tfTelefono.textProperty().addListener(this::txtChanged);
        tfCorreoElectronico.textProperty().addListener(this::txtChanged);
        pfContrasenia.textProperty().addListener(this::txtChanged);

        stage.show();
    }

    /**
     * Al cerrar la ventana, saldrá un mensaje de confirmacion
     *
     * @param event, WindowEvent
     */
    private void handleWindowClose(WindowEvent event) {
        LOG.log(Level.INFO, "Beginning SignUpController::handleWindowClose");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Administrador");
        alert.setContentText("¿Estas seguro de confirmar la acción?");
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
     * Configura los eventos al iniciar la ventana
     *
     * @param event WindowEvent
     */
    private void handleWindowShowing(WindowEvent event) {
        LOG.log(Level.INFO, "Beginning SignUpController::handleWindowShowing");
        //Indicamos que el boton registrase tiene que estar desactivado y el boton cancelar tiene que estar activado
        btnRegistrarse.setDisable(true);
        btnCancelar.setDisable(false);
    }

    /**
     * Introduciendo los datos necesarios en los campos de textos, se habilitará
     * el boton de Registrar y este nos permitirá añadir un nuevo usuario dentro
     * de la base de datos
     *
     * @param event
     */
    private void btnRegistrarseClick(ActionEvent event) {
        //Controlamos con un booleano si el alta se ha realizado correctamente
        boolean alta = false;
        //Definimos en un LocalDate la fecha actual
        LocalDate fechaHoy = LocalDate.now();
        //Establecemos la zona horaria
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //Pasamos de LocalDate a Date en una variable conocida como "date"
        Date date = Date.from(fechaHoy.atStartOfDay(defaultZoneId).toInstant());
        try {
            LOG.log(Level.INFO, "btnRegistrarClick");
            //Creamos un nuevo Objeto de usuario
            Usuario nuevoUsuario = null;
            //Instanciamos el objeto de usuario
            nuevoUsuario = new Usuario();
            //Añadimos que el Login del usuario será el que esta escrito en el tfUsuario de la aplicación
            nuevoUsuario.setLogin(tfUsuario.getText());
            //nuevoUsuario.setPassword(Seguridad.encriptarContrasenia(pfContrasenia.getText()));
            //Añadimos que la contraseña del usuario será la que esta escrita en el pfContrasenia de la aplicación
            nuevoUsuario.setPassword(pfContrasenia.getText());
            //Añadimos que el Teléfono del usuario será el que esta escrito en el tfTelefono de la aplicación
            nuevoUsuario.setTelefono(Integer.parseInt(tfTelefono.getText()));
            //Añadimos que el email del usuario será el que esta escrito en el tfCorreoElectronico de la aplicación
            nuevoUsuario.setEmail(tfCorreoElectronico.getText());
            //Añadimos que la direccion del usuario será la que esta escrita en el tfDireccion de la aplicación
            nuevoUsuario.setDireccion(tfDireccion.getText());
            //Añadimos que el nombre del usuario será el que esta escrito en el tfNombre de la aplicación
            nuevoUsuario.setFullname(tfNombre.getText());
            //Datos puestos por defecto
            //Establecemos que la última contraseña es la que se ha añadido el pfContrasenia
            nuevoUsuario.setLastPasswordChange(pfConfirmarContrasenia.getText());
            //Establecemos que la última conexión es la fecha de la actualidad
            nuevoUsuario.setLastAccess(date);
            //Establecemos que el Estado del usuario está habilitado ya que se acaba de registrar
            nuevoUsuario.setStatus(EstadoUsuario.ENABLED);
            //Establecemos que el Privilegio del usuario va a ser un "Vendedor"
            nuevoUsuario.setPrivilege(PrivilegioUsuario.VENDEDOR);
            //Implementación del UsuarioRESTClient
            usuarioManager = (UsuarioManagerImplementation) new factory.UsuarioFactory().getUsuarioManagerImplementation();
            //Llamamos al método create para asi poder crear un nuevo proveedor
            usuarioManager.create(nuevoUsuario);
            //Establecemos que el alta se ha realizado correctamente
            alta = true;
            if (!alta) {
                //Alerta informandonos de que no se ha podido dar de alta al usuario
                LOG.log(Level.SEVERE, "Usuario no se ha dado de alta");
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Administrador");
                alert.setHeaderText("Usuario no se ha podido dar de alta");
                alert.showAndWait();
            } else {
                //Alerta informandonos de que el usuario se ha dado con exito
                LOG.log(Level.INFO, "Usuario dado de alta con éxito");
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Administrador");
                alert.setHeaderText("Usuario dado de alta con éxito");
                alert.showAndWait();
                //Nos redirige hacia la Ventana de LogIn para poder acceder con el nuevo usuario creado
                LOG.log(Level.INFO, "Ventana LogIn");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn.fxml"));

                    Parent root = (Parent) loader.load();
                    LogInController controller = ((LogInController) loader.getController());
                    controller.initStage(root);
                    stage.hide();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
                }

            }
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (UsuarioYaExisteException ex) {
            LOG.log(Level.SEVERE, "UsuarioYaExisteException");
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("No se ha podido registrar el usuario, ya está dado de alta");
            alert.showAndWait();
        } catch (ErrorServerException ex) {
            LOG.log(Level.SEVERE, "ErrorServerException");
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ErrorBDException ex) {
            LOG.log(Level.SEVERE, "ErrorBDException");
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }

    }

    /**
     * Dirige a la ventana LogIn y la inicializa
     *
     * @param event ActionEvent
     */
    private void btnCancelarClick(ActionEvent event) {
        LOG.log(Level.INFO, "Ventana LogIn");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn.fxml"));

            Parent root = (Parent) loader.load();
            LogInController controller = ((LogInController) loader.getController());
            controller.initStage(root);
            stage.hide();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
        }

    }

    /**
     * Valida los textos introducidos
     *
     * @param observable Observa los cambios
     * @param oldValue Valor antiguo
     * @param newValue Valor nuevo
     */
    private void txtChanged(ObservableValue observable, String oldValue, String newValue) {
        Validar.addTextLimiter(tfUsuario, treinta);
        Validar.addTextLimiterPass(pfContrasenia, treinta);
        Validar.addTextLimiterPass(pfConfirmarContrasenia, treinta);
        Validar.addTextLimiter(tfNombre, cincuenta);
        Validar.addTextLimiter(tfCorreoElectronico, cincuenta);
        Validar.addTextLimiter(tfDireccion, caracteresMax);

        if (!tfUsuario.getText().trim().equals("") && !pfContrasenia.getText().trim().equals("") && !tfNombre.getText().trim().equals("")
                && !tfCorreoElectronico.getText().trim().equals("") && !tfDireccion.getText().trim().equals("")
                && !pfConfirmarContrasenia.getText().trim().equals("") && !tfTelefono.getText().trim().equals("")) {

            boolean isValidUsuario = Validar.isValidUsuario(tfUsuario);
            boolean isValidIgualContrasena = Validar.isValidContrasena(pfContrasenia, pfConfirmarContrasenia);
            boolean isValidNombre = Validar.isValidNombre(tfNombre);
            boolean isValidDireccion = Validar.isValidNombre(tfDireccion);
            boolean isValidTelefono = Validar.isValidTelefono(tfTelefono);
            boolean isValidContrasena = Validar.isValidPatternContrasena(pfContrasenia);
            boolean isValidConfirmarContrasena = Validar.isValidPatternContrasena(pfConfirmarContrasenia);
            boolean isValidEmail = Validar.isValidEmail(tfCorreoElectronico);

            if (isValidUsuario && isValidIgualContrasena && isValidNombre && isValidDireccion
                    && isValidTelefono && isValidContrasena && isValidConfirmarContrasena && isValidEmail) {
                btnRegistrarse.setDisable(false);
            } else {
                btnRegistrarse.setDisable(true);

            }
        }
        if (tfUsuario.getText().trim().equals("") || pfContrasenia.getText().trim().equals("")
                || tfCorreoElectronico.getText().trim().equals("")
                || tfNombre.getText().trim().equals("") || pfConfirmarContrasenia.getText().trim().equals("")
                || tfTelefono.getText().trim().equals("") || tfDireccion.getText().trim().equals("")) {

            btnRegistrarse.setDisable(true);

        }
    }

}
