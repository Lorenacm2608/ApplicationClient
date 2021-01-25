package controllers;

import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.ProveedorYaExisteException;
import implementation.AdministradorManagerImplementation;
import implementation.ProveedorManagerImplementation;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.ws.rs.ClientErrorException;
import manager.AdministradorManager;
import manager.ProveedorManager;
import modelo.FechaAltaCell;
import modelo.Proveedor;
import modelo.TipoProducto;
import static modelo.TipoProducto.ROPA;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author Lorena Cáceres Manuel
 */
public class InicioAdministradorProveedorController {

    private static final Logger LOG = Logger.getLogger(InicioAdministradorProveedorController.class.getName());

    private Stage stage;
    @FXML
    private Pane pnInicioAdminProv;
    @FXML
    private Label lblProveedor;
    @FXML
    private VBox Vbox;
    @FXML
    private HBox Hbox;
    @FXML
    private Button btnAltaProveedor;
    @FXML
    private Button btnBorrarProveedor;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuPerfil;
    @FXML
    private Menu menuVendedor;
    @FXML
    private MenuItem menuSalir;
    @FXML
    private MenuItem menuVendedores;
    @FXML
    private TableView<Proveedor> tbProveedor;
    @FXML
    private TableColumn<Proveedor, String> tcNombre;
    @FXML
    private TableColumn<TipoProducto, TipoProducto> tcTipo;
    @FXML
    private TableColumn<Proveedor, String> tcEmpresa;
    @FXML
    private TableColumn<Proveedor, String> tcEmail;
    @FXML
    private TableColumn<Proveedor, String> tcTelefono;
    @FXML
    private TableColumn<Proveedor, String> tcDescripcion;
    @FXML
    private TableColumn<Proveedor, Date> tcFechaAlta;

    private ProveedorManager proveedorManager;
    private AdministradorManager administradorManager;
    private Usuario usuario;
    private ObservableList<Proveedor> listProveedores = FXCollections.observableArrayList();
    private Proveedor proveedor;
    private Alert alert;
    @FXML
    private TextField tfBuscar;

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
        //Indicamos las columnas que debe tener la tabla y como deben ser
        iniciarColumnasTabla();
        //Configuración de la ventana
        LOG.log(Level.INFO, "Ventana Inicio de Administrador (Proveedor)");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Administrador");
        stage.setResizable(false);
        //Indicamos las imagenes de los botones
        imagenBotones();
        //Indicamos el metodo que se encargara de la apariencia del stage cuando se cierre la ventana
        stage.setOnCloseRequest(this::handleWindowClose);
        //Indicamos el metodo que se encargara de la apariencia del stage cuando se inicie la ventana
        stage.setOnShowing(this::handleWindowShowing);
        //Indicamos las acciones de cada boton 
        btnAltaProveedor.setOnAction(this::btnAltaProveedorClick);
        btnBorrarProveedor.setOnAction(this::btnBorrarProveedorClick);
        //Indicamos que el textField va a tener un metodo asociado
        tfBuscar.textProperty().addListener(this::tfBuscarProveedorPorEmpresa);
        //Mostramos el stage
        stage.show();

    }

    /**
     * Al cerrar la ventana, saldrá un mensaje de confirmacion
     *
     * @param event, WindowEvent
     */
    private void handleWindowClose(WindowEvent event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProveedorController::handleWindowClose");
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
        LOG.log(Level.INFO, "Beginning InicioAdministradorProveedorController::handleWindowShowing");
        //Indicamos que el boton borrarProveedor tiene que estar desactivado
        btnBorrarProveedor.setDisable(true);
    }

    /**
     * Inicializa la tabla de proveedores con sus respectivas validaciones en
     * las celdas y excepciones en el caso de que haya algún fallo
     */
    private void iniciarColumnasTabla() {
        seleccionarProveedor();
        //Hacemos que la tabla sea editable
        tbProveedor.setEditable(true);
        //Rellenamos la tabla con los proveedores
        datosTabla();
        //Definimos las celdas de la tabla, incluyendo que algunas pueden ser editables

        //Nombre del proveedor
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        tcNombre.setOnEditCommit((TableColumn.CellEditEvent<Proveedor, String> data) -> {
            //Establecemos que el dato que se introduzca en la celda debe cumplir un patrón
            if (!Pattern.matches("^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s]+$", data.getNewValue())
                    || data.getNewValue().equalsIgnoreCase("")) {
                //En el caso de que no se cumpla el patrón. Saldrá un alerta informandonos del error
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Proveedor");
                alert.setHeaderText("Error al introducir el nombre del proveedor");
                alert.setContentText("Introduzca un caracter válido");

                alert.showAndWait();
                tbProveedor.refresh();
            } else {
                try {
                    LOG.log(Level.INFO, "Nuevo Nombre: {0}", data.getNewValue());
                    LOG.log(Level.INFO, "Antiguo Nombre: {0}", data.getOldValue());
                    //Implementacion del ProveedorRESTClient
                    proveedorManager = (ProveedorManagerImplementation) new factory.ProveedorFactory().getProveedorManagerImplementation();
                    //Devuelve el dato de la fila
                    Proveedor p = data.getRowValue();
                    //Añadimos el nuevo valor a la fila
                    p.setNombre(data.getNewValue());
                    //Llamamos al método edit para asi poder modificar el nombre del proveedor
                    proveedorManager.edit(p);
                    //Mostramos los datos actualizados en la TableView
                    datosTabla();
                } catch (ClientErrorException ex) {
                    LOG.log(Level.SEVERE, "ClientErrorException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                } catch (ErrorBDException ex) {
                    LOG.log(Level.SEVERE, "ErrorBDException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                } catch (ErrorServerException ex) {
                    LOG.log(Level.SEVERE, "ErrorServerException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                }
            }
        });

        //Tipo de producto 
        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        //Indicamos que la celda puede cambiar a un ChoiceBox
        tcTipo.setCellFactory(ChoiceBoxTableCell.
                forTableColumn(TipoProducto.ROPA, TipoProducto.ZAPATILLAS, TipoProducto.AMBAS));
        //Le añadimos un evento y le indicamos que se realizará en ActualizarTipoProducto 
        tcTipo.addEventHandler(TableColumn.<Proveedor, TipoProducto>editCommitEvent(),
                event -> actualizarTipoProducto(event));

        //Empresa del proveedor
        tcEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        //Indicamos que la celda puede cambiar a un TextField
        tcEmpresa.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna empresa 
        tcEmpresa.setOnEditCommit((TableColumn.CellEditEvent<Proveedor, String> data) -> {
            //Establecemos que el dato que se introduzca en la celda debe cumplir un patrón
            if (!Pattern.matches("^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s]+$", data.getNewValue())
                    || data.getNewValue().equalsIgnoreCase("")) {
                //En el caso de que no se cumpla el patrón. Saldrá un alerta informandonos del error
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Proveedor");
                alert.setHeaderText("Error al introducir la empresa");
                alert.setContentText("Introduzca un caracter válido");

                alert.showAndWait();
                tbProveedor.refresh();
            } else {
                try {
                    //Comprueba que la empresa no esté registrada dentro de la base de datos
                    boolean repetida = encontrarEmpresa(data);
                    if (!repetida) {
                        LOG.log(Level.INFO, "Nueva Empresa: {0}", data.getNewValue());
                        LOG.log(Level.INFO, "Antigua Empresa: {0}", data.getOldValue());
                        //Implementacion del ProveedorRESTClient
                        proveedorManager = (ProveedorManagerImplementation) new factory.ProveedorFactory().getProveedorManagerImplementation();
                        //Devuelve el dato de la fila
                        Proveedor p = data.getRowValue();
                        //Añadimos el nuevo valor a la fila
                        p.setEmpresa(data.getNewValue());
                        //Llamamos al método edit para asi poder modificar la empresa del proveedor
                        proveedorManager.edit(p);
                        //Mostramos los datos actualizados en la TableView
                        datosTabla();

                    }

                } catch (ClientErrorException ex) {
                    LOG.log(Level.SEVERE, "ClientErrorException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                } catch (ErrorBDException ex) {
                    LOG.log(Level.SEVERE, "ErrorBDException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                } catch (ErrorServerException ex) {
                    LOG.log(Level.SEVERE, "ErrorServerException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                } catch (ProveedorYaExisteException ex) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Proveedor");
                    alert.setHeaderText("Error al introducir un proveedor");
                    alert.setContentText("El proveedor de esta empresa ya esta dado de alta");
                    alert.showAndWait();
                    tbProveedor.refresh();
                }
            }
        }
        );

        //Email del proveedor
        tcEmail.setCellValueFactory(
                new PropertyValueFactory<>("email"));
        //Indicamos que la celda puede cambiar a un TextField
        tcEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna email 
        tcEmail.setOnEditCommit(
                (TableColumn.CellEditEvent<Proveedor, String> data) -> {
                    //Establecemos que el dato que se introduzca en la celda debe cumplir un patrón
                    if (!Pattern.matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", data.getNewValue())
                    || data.getNewValue().equalsIgnoreCase("")) {
                        //En el caso de que no se cumpla el patrón. Saldrá un alerta informandonos del error
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Proveedor");
                        alert.setHeaderText("Error al introducir el email");
                        alert.setContentText("Introduzca un caracter válido");

                        alert.showAndWait();
                        tbProveedor.refresh();
                    } else {
                        try {
                            LOG.log(Level.INFO, "Nuevo Email: {0}", data.getNewValue());
                            LOG.log(Level.INFO, "Antiguo Email: {0}", data.getOldValue());
                            //Implementación del ProveedorRESTClient
                            proveedorManager = (ProveedorManagerImplementation) new factory.ProveedorFactory().getProveedorManagerImplementation();
                            //Devuelve el dato de la fila
                            Proveedor p = data.getRowValue();
                            //Añadimos el nuevo valor a la fila
                            p.setEmail(data.getNewValue());
                            //Llamamos al método edit para asi poder modificar el email del proveedor
                            proveedorManager.edit(p);
                            //Mostramos los datos actualizados en la TableView
                            datosTabla();
                        } catch (ClientErrorException ex) {
                            LOG.log(Level.SEVERE, "ClientErrorException");
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Administrador");
                            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                            alert.showAndWait();
                        } catch (ErrorBDException ex) {
                            LOG.log(Level.SEVERE, "ErrorBDException");
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Administrador");
                            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                            alert.showAndWait();
                        } catch (ErrorServerException ex) {
                            LOG.log(Level.SEVERE, "ErrorServerException");
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Administrador");
                            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                            alert.showAndWait();
                        }
                    }
                }
        );

        //Teléfono del proveedor
        tcTelefono.setCellValueFactory(
                new PropertyValueFactory<>("telefono"));
        //Indicamos que la celda puede cambiar a un TextField
        tcTelefono.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna teléfono 
        tcTelefono.setOnEditCommit(
                (TableColumn.CellEditEvent<Proveedor, String> data) -> {
                    //Establecemos que el dato que se introduzca en la celda debe cumplir un patrón
                    if (!Pattern.matches("\\d{9,11}", data.getNewValue())
                    || data.getNewValue().equalsIgnoreCase("")) {
                        //En el caso de que no se cumpla el patrón. Saldrá un alerta informandonos del error
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Proveedor");
                        alert.setHeaderText("Error al introducir el teléfono");
                        alert.setContentText("Introduzca un caracter válido");

                        alert.showAndWait();
                        tbProveedor.refresh();

                    } else {
                        try {
                            LOG.log(Level.INFO, "Nuevo Teléfono: {0}", data.getNewValue());
                            LOG.log(Level.INFO, "Antiguo Teléfono: {0}", data.getOldValue());
                            //Implementación del ProveedorRESTClient
                            proveedorManager = (ProveedorManagerImplementation) new factory.ProveedorFactory().getProveedorManagerImplementation();
                            //Devuelve el dato de la fila
                            Proveedor p = data.getRowValue();
                            //Añadimos el nuevo valor a la fila
                            p.setTelefono(data.getNewValue());
                            //Llamamos al método edit para asi poder modificar el teléfono del proveedor
                            proveedorManager.edit(p);
                            //Mostramos los datos actualizados en la TableView
                            datosTabla();
                        } catch (ClientErrorException ex) {
                            LOG.log(Level.SEVERE, "ClientErrorException");
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Administrador");
                            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                            alert.showAndWait();
                        } catch (ErrorBDException ex) {
                            LOG.log(Level.SEVERE, "ErrorBDException");
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Administrador");
                            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                            alert.showAndWait();
                        } catch (ErrorServerException ex) {
                            LOG.log(Level.SEVERE, "ErrorServerException");
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Administrador");
                            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                            alert.showAndWait();
                        }
                    }

                }
        );

        //Descripción del proveedor
        tcDescripcion.setCellValueFactory(
                new PropertyValueFactory<>("descripcion"));
        //Indicamos que la celda puede cambiar a un TextField
        tcDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna descripción 
        tcDescripcion.setOnEditCommit(
                (TableColumn.CellEditEvent<Proveedor, String> data) -> {
                    //Establecemos que el dato que se introduzca en la celda debe cumplir un patrón
                    if (!Pattern.matches("^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s]+$", data.getNewValue())
                    || data.getNewValue().equalsIgnoreCase("")) {
                        //En el caso de que no se cumpla el patrón. Saldrá un alerta informandonos del error
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Proveedor");
                        alert.setHeaderText("Error al introducir la descripción");
                        alert.setContentText("Introduzca un caracter válido");

                        alert.showAndWait();
                        tbProveedor.refresh();
                    } else {
                        try {
                            LOG.log(Level.INFO, "Nueva Descripción: {0}", data.getNewValue());
                            LOG.log(Level.INFO, "Antigua Descripción: {0}", data.getOldValue());
                            //Implementación del ProveedorRESTClient
                            proveedorManager = (ProveedorManagerImplementation) new factory.ProveedorFactory().getProveedorManagerImplementation();
                            //Devuelve el dato de la fila
                            Proveedor p = data.getRowValue();
                            //Añadimos el nuevo valor a la fila
                            p.setDescripcion(data.getNewValue());
                            //Llamamos al método edit para asi poder modificar la descripción del proveedor
                            proveedorManager.edit(p);
                            //Mostramos los datos actualizados en la TableView
                            datosTabla();
                        } catch (ClientErrorException ex) {
                            LOG.log(Level.SEVERE, "ClientErrorException");
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Administrador");
                            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                            alert.showAndWait();
                        } catch (ErrorBDException ex) {
                            LOG.log(Level.SEVERE, "ErrorBDException");
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Administrador");
                            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                            alert.showAndWait();
                        } catch (ErrorServerException ex) {
                            LOG.log(Level.SEVERE, "ErrorServerException");
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Administrador");
                            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                            alert.showAndWait();
                        }
                    }
                }
        );

        //Fecha de alta del proveedor
        tcFechaAlta.setCellValueFactory(
                new PropertyValueFactory<>("fechaAlta"));
        //Indicamos que la celda puede cambiar a una TableCell
        tcFechaAlta.setCellFactory(
                new Callback<TableColumn<Proveedor, Date>, TableCell<Proveedor, Date>>() {
            //Este método indica que la TableCell incluye un DatePicker
            @Override
            public TableCell<Proveedor, Date> call(TableColumn<Proveedor, Date> arg0
            ) {
                return new FechaAltaCell();
            }
        }
        );
        //Aceptamos la edición de la celda de la columna fechaAlta 
        tcFechaAlta.setOnEditCommit(data
                -> {
            try {
                LOG.log(Level.INFO, "Nueva FechaAlta: {0}", data.getNewValue());
                LOG.log(Level.INFO, "Antigua FechaAlta: {0}", data.getOldValue());
                //Implementación del ProveedorRESTClient
                proveedorManager = (ProveedorManagerImplementation) new factory.ProveedorFactory().getProveedorManagerImplementation();
                //Devuelve el dato de la fila
                Proveedor p = data.getRowValue();
                //Añadimos el nuevo valor a la fila
                p.setFechaAlta(data.getNewValue());
                //Llamamos al método edit para asi poder modificar la fecha de alta del proveedor
                proveedorManager.edit(p);
                //Mostramos los datos actualizados en la TableView
                datosTabla();
            } catch (ClientErrorException ex) {
                LOG.log(Level.SEVERE, "ClientErrorException");
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Administrador");
                alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                alert.showAndWait();
            } catch (ErrorBDException ex) {
                LOG.log(Level.SEVERE, "ErrorBDException");
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Administrador");
                alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                alert.showAndWait();
            } catch (ErrorServerException ex) {
                LOG.log(Level.SEVERE, "ErrorServerException");
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Administrador");
                alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                alert.showAndWait();
            }
        }
        );

    }

    /**
     * Nos permite actualizar/cambiar el tipo de producto dentro de la tabla
     * editable
     *
     * @param event
     */
    private void actualizarTipoProducto(TableColumn.CellEditEvent<Proveedor, TipoProducto> data) {
        try {
            LOG.log(Level.INFO, "Nuevo Tipo de Producto: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Tipo de Producto: {0}", data.getOldValue());
            //Implementación del ProveedorRESTClient
            proveedorManager = (ProveedorManagerImplementation) new factory.ProveedorFactory().getProveedorManagerImplementation();
            //Devuelve el dato de la fila
            Proveedor p = data.getRowValue();
            //Añadimos el nuevo valor a la fila
            p.setTipo(data.getNewValue());
            //Llamamos al método edit para asi poder modificar el tipo de producto que nos proporciona el proveedor
            proveedorManager.edit(p);
            //Mostramos los datos actualizados en la TableView
            datosTabla();
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ErrorBDException ex) {
            LOG.log(Level.SEVERE, "ErrorBDException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ErrorServerException ex) {
            LOG.log(Level.SEVERE, "ErrorServerException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }

    }

    /**
     * Nos permite seleccionar a un proveedor de la tabla y este controla que el
     * botón BorrarProveedor esté habilitado o deshabilitado
     */
    private void seleccionarProveedor() {
        tbProveedor.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (proveedor != null) {
                        btnBorrarProveedor.setDisable(true);
                    } else {
                        btnBorrarProveedor.setDisable(false);
                    }
                });
    }

    //CONFIGURACIÓN DE LOS DATOS 
    /**
     * Muestra los datos de la base de datos en la TableView
     */
    private void datosTabla() {
        try {
            //Implementación del AdministradorRESTClient
            administradorManager = (AdministradorManagerImplementation) new factory.AdministradorFactory().getAdministradorManagerImplementation();
            //Llamamos al método getProveedores para asi poder llenar la tabla con los proveedores que hay dentro de la base de datos
            listProveedores = FXCollections.observableArrayList(administradorManager.getProveedores());
            //Establecemos los datos del ObservableList dentro de la TableView
            tbProveedor.setItems(listProveedores);
            //Comprobamos el ArrayList Observable de proveedores
            for (Proveedor p : listProveedores) {
                LOG.log(Level.INFO, "Lista de Proveedores: {0}", listProveedores);
            }
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ErrorBDException ex) {
            LOG.log(Level.SEVERE, "ErrorBDException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ErrorServerException ex) {
            LOG.log(Level.SEVERE, "ErrorServerException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }
    }

    //CONFIGURACIÓN DE BOTONES
    /**
     * Nos permite dar de alta a un proveedor, nos genera datos predeterminados
     * y posteriormente el administrador podrá cambiarlos y se almacenarán en la
     * base de datos
     *
     * @param event ActionEvent
     */
    private void btnAltaProveedorClick(ActionEvent event) {
        //Definimos en un LocalDate la fecha actual
        LocalDate fechaHoy = LocalDate.now();
        //Establecemos la zona horaria
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //Pasamos de LocalDate a Date en una variable conocida como "date"
        Date date = Date.from(fechaHoy.atStartOfDay(defaultZoneId).toInstant());
        //Instanciamos un nuevo proveedor dandole valores por defecto
        Proveedor nuevoProveedor = new Proveedor();
        //Añadimos por defecto que la descripción está vacia
        nuevoProveedor.setDescripcion("");
        //Añadimos por defecto que  el email está vacio
        nuevoProveedor.setEmail("");
        //Añadimos por defecto que la empresa está vacia
        nuevoProveedor.setEmpresa("");
        //Añadimos por defecto que el nombre está vacio
        nuevoProveedor.setNombre("");
        //Añadimos por defecto que el teléfono está vacio
        nuevoProveedor.setTelefono("");
        //Añadimos por defecto que el tipo del producto va a ser ROPA
        nuevoProveedor.setTipo(ROPA);
        //Añadimos por defecto que la fecha de alta será el dia de actual
        nuevoProveedor.setFechaAlta(date);
        try {
            //Implementación del ProveedorRESTClient
            proveedorManager = (ProveedorManagerImplementation) new factory.ProveedorFactory().getProveedorManagerImplementation();
            //Llamamos al método create para asi poder crear un nuevo proveedor
            proveedorManager.create(nuevoProveedor);
            datosTabla();
        } catch (ErrorBDException ex) {
            LOG.log(Level.SEVERE, "ErrorBDException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ErrorServerException ex) {
            LOG.log(Level.SEVERE, "ErrorServerException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        } catch (ProveedorYaExisteException ex) {
            LOG.log(Level.SEVERE, "ProveedorYaExisteException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Proveedor ya existe");
            alert.showAndWait();
        }
    }

    /**
     * Borra el proveedor seleccionado de la tabla de proveedores, saldrá una
     * alerta para la confirmación del borrado.
     *
     * En el caso de no seleccionar ningún proveedor, saldrá una alerta
     * indicando que seleccionemos un proveedor para proceder a su borrado
     *
     * @param event
     */
    private void btnBorrarProveedorClick(ActionEvent event) {
        //Mensaje de confirmación para el borrado
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Borrado de Proveedor");
        alert.setContentText("¿Estas seguro de borrar este proveedor?");
        Optional<ButtonType> respuesta = alert.showAndWait();
        //En el caso de pulsar el boton Aceptar, borraremos el proveedor seleccionado
        if (respuesta.get() == ButtonType.OK) {
            LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
            //Capturamos el indice del proveedor seleccionado y borro su item asociado de la tabla
            int proveedorIndex = tbProveedor.getSelectionModel().getSelectedIndex();
            if (proveedorIndex >= 0) {
                try {
                    //Implementación del ProveedorRESTClient
                    proveedorManager = (ProveedorManagerImplementation) new factory.ProveedorFactory().getProveedorManagerImplementation();
                    //Llamamos al método remove para asi poder eliminar el proveedor que está seleccionado
                    proveedorManager.remove(tbProveedor.getSelectionModel().getSelectedItem().getIdProveedor().toString());
                    //Mostramos los datos actualizados en la tabla
                    datosTabla();
                    LOG.log(Level.INFO, "Se ha borrado un proveedor");
                } catch (ClientErrorException ex) {
                    LOG.log(Level.SEVERE, "ClientErrorException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                } catch (ErrorBDException ex) {
                    LOG.log(Level.SEVERE, "ErrorBDException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                } catch (ErrorServerException ex) {
                    LOG.log(Level.SEVERE, "ErrorServerException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                }
            } else {
                //En el caso de no seleccionar un proveedor. Saldrá un alerta
                Alert alerta = new Alert(AlertType.WARNING);
                alerta.setTitle("Atención");
                alerta.setHeaderText("Proveedor no seleccionado");
                alerta.setContentText("Por favor, selecciona un proveedor de la tabla");
                alerta.showAndWait();
            }
        } else {
            LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
            //Desaparece el alerta
            event.consume();
        }
    }

    /**
     * Comprueba que la empresa que se va a introducir no esté registrada ya en
     * la base de datos
     *
     * @param data
     * @return
     */
    private boolean encontrarEmpresa(TableColumn.CellEditEvent<Proveedor, String> data) throws ProveedorYaExisteException {
        boolean repetido = false;
        for (int i = 0; i < listProveedores.size(); i++) {
            if (data.getNewValue().equalsIgnoreCase(listProveedores.get(i).getEmpresa())) {
                repetido = true;
                throw new ProveedorYaExisteException();
            }
        }
        return repetido;
    }

    //CONFIGURACIÓN DEL TEXTFIELD DE BÚSQUEDA
    /**
     * Este método nos permite filtrar los proveedores a partir del nombre de la
     * empresa, para ello usaremos el textField 'tfBuscar'
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void tfBuscarProveedorPorEmpresa(ObservableValue observable, String oldValue, String newValue) {
        FilteredList<Proveedor> filteredData = new FilteredList<>(listProveedores, u -> true);

        filteredData.setPredicate(proveedor -> {
            //Cuando el TextField de búsqueda esté vacío, mostrará todos los proveedores
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            //Ponemos el valor en minúsculas
            String lowerCaseFilter = newValue.toLowerCase();
            //Buscamos al proveedor usando el nombre de la empresa
            if (proveedor.getEmpresa().toLowerCase().contains(lowerCaseFilter)) {
                return true; //Proveedor encontrado(s)
            }

            return false; // Proveedor no encontrado(s)
        });
        // Convertimos la FilteredList en una SortedList.
        SortedList<Proveedor> sortedData = new SortedList<>(filteredData);

        // Se vincula el comparador SortedList al de la TableView.
        sortedData.comparatorProperty().bind(tbProveedor.comparatorProperty());

        // Añade los datos filtrados a la tabla
        tbProveedor.setItems(sortedData);
    }

    /**
     * MenuItem que nos redirige hacia la ventana de LogIn y cierra la ventana
     * actual
     *
     * @param event
     */
    @FXML
    private void configMenuSalir(ActionEvent event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProveedorController::handleWindowClose");
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Administrador");
        alert.setContentText("¿Estas seguro de confirmar la acción?");
        Optional<ButtonType> respuesta = alert.showAndWait();

        if (respuesta.get() == ButtonType.OK) {
            LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
            LOG.log(Level.INFO, "Ventana Login");
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
     * MenuItem que nos redirige hacia la ventana de InicioAdministrador del
     * lado vendedor y cierra la ventana actual
     *
     * @param event
     */
    @FXML
    private void configMenuVendedores(ActionEvent event) {
        LOG.log(Level.INFO, "Ventana Inicio de Administrador (Vendedor)");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InicioAdministrador_vendedor.fxml"));

            Parent root = (Parent) loader.load();

            InicioAdministradorVendedorController controller = ((InicioAdministradorVendedorController) loader.getController());
            controller.initStage(root);
            stage.hide();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
        }
    }

    //CONFIGURACIÓN DE IMAGENES 
    /**
     * Añade las imagenes de los botones
     */
    private void imagenBotones() {
        //Creamos un objeto y en él guardaremos la ruta donde se encuentra las imagenes para los botones
        URL linkAlta = getClass().getResource("/img/usuario.png");
        URL linkBorrar = getClass().getResource("/img/eliminar.png");

        //Instanciamos una imagen pasándole la ruta de las imagenes y las medidas del boton 
        Image imageAlta = new Image(linkAlta.toString(), 32, 32, false, true);
        Image imageBorrar = new Image(linkBorrar.toString(), 32, 32, false, true);

        //Añadimos la imagen a los botones que deban llevar icono
        btnAltaProveedor.setGraphic(new ImageView(imageAlta));
        btnBorrarProveedor.setGraphic(new ImageView(imageBorrar));

    }

}
