package controllers;

import exceptions.DeleteException;
import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.InsertException;
import exceptions.UpdateException;
import exceptions.VendedorNotFoundException;
import exceptions.VendedorYaExisteException;
import implementation.AdministradorManagerImplementation;
import implementation.VendedorManagerImplementation;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;
import javax.persistence.EntityManager;
import javax.ws.rs.ClientErrorException;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import manager.AdministradorManager;
import manager.VendedorManager;
import modelo.Administrador;
import modelo.EstadoUsuario;
import static modelo.EstadoUsuario.ENABLED;
import static modelo.PrivilegioUsuario.VENDEDOR;
import modelo.Proveedor;
import modelo.Usuario;
import modelo.Vendedor;

/**
 * FXML Controller class
 *
 * @author Lorena Cáceres Manuel
 */
public class InicioAdministradorVendedorController {

    private static final Logger LOG = Logger.getLogger(InicioAdministradorVendedorController.class.getName());

    private Stage stage = new Stage();
    @FXML
    private Pane pnInicioAdminVend;
    @FXML
    private TableView<Vendedor> tbVendedores;
    @FXML
    private TableColumn<Vendedor, String> colUsuario;
    @FXML
    private TableColumn<Vendedor, String> colEmail;
    @FXML
    private TableColumn<Vendedor, String> colNombre;
    @FXML
    private TableColumn<Vendedor, EstadoUsuario> colEstado;
    @FXML
    private TableColumn<Vendedor, Date> colUltimoAcceso;
    @FXML
    private TableColumn<Vendedor, String> colUltimaContrasenia;
    @FXML
    private TableColumn<Vendedor, String> colDireccion;
    @FXML
    private TableColumn<Vendedor, Integer> colTelefono;
    @FXML
    private TableColumn<Vendedor, String> colDni;
    @FXML
    private TableColumn<Vendedor, Integer> colSalario;
    @FXML
    private TableColumn<Vendedor, String> colTienda;
    @FXML
    private Label lblVendedor;
    @FXML
    private VBox Vbox;
    @FXML
    private Button btnAltaVendedor;
    @FXML
    private Button btnBorrarVendedor;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField txtBuscarVendedor;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuPerfil;
    @FXML
    private Menu menuProveedor;
    @FXML
    private MenuItem menuAdministrador;
    @FXML
    private MenuItem menuProveedores;
    @FXML
    private MenuItem menuSalir;

    private VendedorManager vendedorManager;
    private AdministradorManager administradorManager;
    private EntityManager entityManager;
    private ObservableList<Vendedor> listvendedores = FXCollections.observableArrayList();
    private Vendedor vendedor;
    private Usuario usuario;
    private Alert alert;

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
        LOG.log(Level.INFO, "Ventana Inicio de Administrador (Vendedor)");
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
        btnAltaVendedor.setOnAction(this::btnAltaVendedorClick);
        btnAltaVendedor.setTooltip(new Tooltip("Pulse para dar de alta un nuevo vendedor "));
        btnBorrarVendedor.setOnAction(this::btnBorrarVendedorClick);
        btnBorrarVendedor.setTooltip(new Tooltip("Pulse para borrar el vendedor selecionado "));
        //Indicamos que el textField va a tener un metodo asociado
        txtBuscarVendedor.textProperty().addListener(this::txtBuscarVendedorNombre);
        //Mostramos el stage
        stage.show();

    }

    /**
     * Al cerrar la ventana, saldrá un mensaje de confirmacion
     *
     * @param event, WindowEvent
     */
    private void handleWindowClose(WindowEvent event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorVendedorController::handleWindowClose");
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
        LOG.log(Level.INFO, "Beginning InicioAdministradorVendedorController::handleWindowShowing");
        btnBorrarVendedor.setDisable(true);

    }

    /**
     * Valida los textos introducidos
     *
     * @param observable Observa los cambios
     * @param oldValue Valor antiguo
     * @param newValue Valor nuevo
     */
    private void txtChanged(ObservableValue observable, String oldValue, String newValue) {

    }
    
    private void btnAltaVendedorClick(ActionEvent event) {
        try {
            
            LocalDate fechaHoy = LocalDate.now();
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date date = Date.from(fechaHoy.atStartOfDay(defaultZoneId).toInstant());

            //Instanciamos un nuevo vendedor dandole valores por defecto
            Vendedor nuevoVendedor = new Vendedor();
            //Añadimos por defecto que el administrador va a ser null
            nuevoVendedor.setAdministrador(null);
            //Añadimos por defecto que el dni está vacio
            nuevoVendedor.setDni("");
            //Añadimos por defecto que  el salario es 0
            nuevoVendedor.setSalario(0);
            //Añadimos por defecto que el login está vacio
            nuevoVendedor.setLogin("");
            //Añadimos por defecto que el email está vacio
            nuevoVendedor.setEmail("");
            //Añadimos por defecto que el nombre está vacio
            nuevoVendedor.setFullname("");
            //Añadimos por defecto que el estado del vendedor va a ser ENABLED
            nuevoVendedor.setStatus(ENABLED);
            //Añadimos por defecto que la privilegio del vendedor será VENDEDOR
            nuevoVendedor.setPrivilege(VENDEDOR);
            //Añadimos por defecto que la password del vendedor esta vacio
            nuevoVendedor.setPassword("");
            //Añadimos por defecto que el ultimo acceso esta vacio
            nuevoVendedor.setLastAccess(date);
            //Añadimos por defecto que el ultimo cambio de contraseña esta vacio
            nuevoVendedor.setLastPasswordChange("");
            //Implementación del VendedorRESTClient
            vendedorManager = (VendedorManagerImplementation) new factory.VendedorFactory().getVendedorManagerImplementation();
            //Llamamos al método create para asi poder crear un nuevo vendedor
            vendedorManager.create(nuevoVendedor);
            //Añadimos en nuevo vendedor dentro del listvendedores (ObservableList)
            listvendedores.add(nuevoVendedor);
            int row = listvendedores.size() - 1;

            // Seleccionamos la nueva fila
            tbVendedores.requestFocus();
            tbVendedores.getSelectionModel().select(row);
            tbVendedores.getFocusModel().focus(row);
            //}
/*
            //Posicion actual
        TablePosition pos = tbVendedores.getFocusModel().getFocusedCell();
        //
        tbVendedores.getSelectionModel().clearSelection();

        Vendedor nuevoVendedor = new Vendedor();
        tbVendedores.getItems().add(nuevoVendedor);

        int row = tbVendedores.getItems().size() - 1;
        tbVendedores.getSelectionModel().select(row, pos.getTableColumn());
        tbVendedores.scrollTo(nuevoVendedor);
            */
        } catch (ClientErrorException ex) {
            Logger.getLogger(InicioAdministradorVendedorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Borra el vendedor seleccionado de la tabla de vendedores
     *
     * @param event
     */
    private void btnBorrarVendedorClick(ActionEvent event) {
        //Mensaje de confirmación para el borrado
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Borrado de Vendedor");
        alert.setContentText("¿Estas seguro de borrar este vendedor?");
        Optional<ButtonType> respuesta = alert.showAndWait();
        //En el caso de pulsar el boton Aceptar, borraremos el vendedor seleccionado
        if (respuesta.get() == ButtonType.OK) {
            LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
            //Capturamos el indice del vendedor seleccionado y borro su item asociado de la tabla
            int vendedorIndex = tbVendedores.getSelectionModel().getSelectedIndex();
            if (vendedorIndex >= 0) {
                try {
                    //Implementación del VendedorRESTClient
                    vendedorManager = (VendedorManagerImplementation) new factory.VendedorFactory().getVendedorManagerImplementation();
                    //Llamamos al método remove para asi poder eliminar el vendedor que está seleccionado
                    vendedorManager.remove(tbVendedores.getSelectionModel().getSelectedItem().getIdVendedor().toString());
                    //Mostramos los datos actualizados en la tabla
                    datosTabla();
                    LOG.log(Level.INFO, "Se ha borrado un vendedor");
                } catch (ClientErrorException ex) {
                    LOG.log(Level.SEVERE, "ClientErrorException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                }
            } else {
                //En el caso de no seleccionar un vendedor. Saldrá un alerta
                Alert alerta = new Alert(AlertType.WARNING);
                alerta.setTitle("Atención");
                alerta.setHeaderText("Vendedor no seleccionado");
                alerta.setContentText("Por favor, selecciona un vendedor de la tabla");
                alerta.showAndWait();
            }
        } else {
            LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
            //Desaparece el alerta
            event.consume();
        }
    }
    
    private void imagenBotones() {
        //Creamos un objeto y en él guardaremos la ruta donde se encuentra las imagenes para los botones
        URL linkAlta = getClass().getResource("/img/usuario.png");
        URL linkBorrar = getClass().getResource("/img/eliminar.png");
        URL linkActualizar = getClass().getResource("/img/refrescar.png");

        //Instanciamos una imagen pasándole la ruta de las imagenes y las medidas del boton 
        Image imageAlta = new Image(linkAlta.toString(), 32, 32, false, true);
        Image imageBorrar = new Image(linkBorrar.toString(), 32, 32, false, true);
        Image imageActualizar = new Image(linkActualizar.toString(), 32, 32, false, true);

        //Añadimos la imagen a los botones que deban llevar icono
        btnAltaVendedor.setGraphic(new ImageView(imageAlta));
        btnBorrarVendedor.setGraphic(new ImageView(imageBorrar));
    }


    /**
     * Inicializa la tabla de vendedores
     */
    private void iniciarColumnasTabla() {
        seleccionarVendedor();
        //Hacemos que la tabla sea editable
        tbVendedores.setEditable(true);
        //Rellenamos la tabla con los vendedores
        datosTabla();
        //Definimos las celdas de la tabla, incluyendo que algunas pueden ser editables
        //Usuario del vendedor
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("login"));
        //Indicamos que la celda puede cambiar a un TextField
        colUsuario.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna descripción 
        colUsuario.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Usuario: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Usuario: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setLogin(data.getNewValue());
        });
        //Email del vendedor
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        //Indicamos que la celda puede cambiar a un TextField
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna email
        colEmail.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Email: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Email: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setEmail(data.getNewValue());

        });
        //Nombre del vendedor 
        colNombre.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        //Indicamos que la celda puede cambiar a un TextField
        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna nombre
        colNombre.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            if (!Pattern.matches("^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s]+$", data.getNewValue())
                    || data.getNewValue().equalsIgnoreCase("")) {
                //En el caso de que no se cumpla el patrón. Saldrá un alerta informandonos del error
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Vendedor");
                alert.setHeaderText("Error al introducir el nombre del vendedor");
                alert.setContentText("Introduzca un caracter válido");

                alert.showAndWait();
                tbVendedores.refresh();
            } else {
                try {
                    LOG.log(Level.INFO, "Nuevo Nombre: {0}", data.getNewValue());
                    LOG.log(Level.INFO, "Antiguo Nombre: {0}", data.getOldValue());
                    //Implementacion del VendedorRESTClient
                    vendedorManager = (VendedorManagerImplementation) new factory.VendedorFactory().getVendedorManagerImplementation();
                    //Devuelve el dato de la fila
                    Vendedor v = data.getRowValue();
                    //Añadimos el nuevo valor a la fila
                    v.setFullname(data.getNewValue());
                    //Llamamos al método edit para asi poder modificar el nombre del vendedor
                    vendedorManager.edit(v);
                    //Mostramos los datos actualizados en la TableView
                    datosTabla();
                } catch (ClientErrorException ex) {
                    LOG.log(Level.SEVERE, "ClientErrorException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                }
            }
        });
        //Estado del vendedor
        colEstado.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEstado.setCellFactory(ChoiceBoxTableCell.
                forTableColumn(EstadoUsuario.ENABLED, EstadoUsuario.DISABLED));
        colEstado.addEventHandler(TableColumn.<Vendedor, EstadoUsuario>editCommitEvent(),
                event -> actualizarEstadoVendedor(event));
        //Ultimo acceso del vendedor
        colUltimoAcceso.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));
        //Ultima contraseña del vendedor
        colUltimaContrasenia.setCellValueFactory(new PropertyValueFactory<>("lastPasswordChange"));
        //Direcciom del vendedor
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        //Indicamos que la celda puede cambiar a un TextField
        colDireccion.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna direccion
        colDireccion.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            LOG.log(Level.INFO, "Nueva Direccion: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antigua Direccion: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setDireccion(data.getNewValue());

        });
        //Telefono del vendedor
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        //Indicamos que la celda puede cambiar a un TextField
        colTelefono.setCellFactory(TextFieldTableCell.<Vendedor, Integer>forTableColumn(new IntegerStringConverter()));
        //Aceptamos la edición de la celda de la columna telefono
        colTelefono.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, Integer> data) -> {
            LOG.log(Level.INFO, "Nuevo Telefono: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Telefono: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setTelefono(data.getNewValue());

        });
        //Dni del vendedor
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        //Indicamos que la celda puede cambiar a un TextField
        colDni.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna dni
        colDni.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Dni: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Dni: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setDni(data.getNewValue());

        });
        //Salario del vendedor
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        //Indicamos que la celda puede cambiar a un TextField
        colSalario.setCellFactory(TextFieldTableCell.<Vendedor, Integer>forTableColumn(new IntegerStringConverter()));
        //Aceptamos la edición de la celda de la columna salario
        colSalario.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, Integer> data) -> {
            LOG.log(Level.INFO, "Nuevo Salario: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Salario: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setSalario(data.getNewValue());

        });
        //Tienda del vendedor
        colTienda.setCellValueFactory(new PropertyValueFactory<>("tienda"));
        //Indicamos que la celda puede cambiar a un TextField
        colTienda.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna tienda
        colTienda.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            //Establecemos que el dato que se introduzca en la celda debe cumplir un patrón
            if (!Pattern.matches("^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s]+$", data.getNewValue())
                    || data.getNewValue().equalsIgnoreCase("")) {
                //En el caso de que no se cumpla el patrón. Saldrá un alerta informandonos del error
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Vendedor");
                alert.setHeaderText("Error al introducir la tienda");
                alert.setContentText("Introduzca un caracter válido");

                alert.showAndWait();
                tbVendedores.refresh();
            } else {
                try {
                    LOG.log(Level.INFO, "Nueva Tienda: {0}", data.getNewValue());
                    LOG.log(Level.INFO, "Antigua Tienda: {0}", data.getOldValue());
                    //Implementacion del VendedorRESTClient
                    vendedorManager = (VendedorManagerImplementation) new factory.VendedorFactory().getVendedorManagerImplementation();
                    //Devuelve el dato de la fila
                    Vendedor v = data.getRowValue();
                    //Añadimos el nuevo valor a la fila
                    v.setTienda(data.getNewValue());
                    //Llamamos al método edit para asi poder modificar la tienda del vendedor
                    vendedorManager.edit(v);
                    //Mostramos los datos actualizados en la TableView
                    datosTabla();
                } catch (ClientErrorException ex) {
                    LOG.log(Level.SEVERE, "ClientErrorException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Administrador");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                }
            }
        });
        
    }
    
    /**
     * Nos permite seleccionar a un vendedor de la tabla y este controla que el
     * botón BorrarVendedor y ActualizarVendedor esté habilitado o
     * deshabilitado
     */
    private void seleccionarVendedor() {
        tbVendedores.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (vendedor != null) {
                        btnBorrarVendedor.setDisable(true);
                    } else {
                        btnBorrarVendedor.setDisable(false);
                    }
                });
    }
    
    //CONFIGURACIÓN DEL MENÚ
    /**
     * MenuItem que muestra un alerta informandonos de la conexión actual del
     * usuario
     *
     * @param event
     */
    @FXML
    private void configMenuAdministrador(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información del Administrador");
        alert.setHeaderText("Usuario: Administrador");

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd-MMM-aaaa");
        String fechaComoCadena = sdf.format(new Date());
        alert.setContentText(fechaComoCadena);
        alert.showAndWait();
    }

    /**
     * MenuItem que nos redirige hacia la ventana de LogIn y cierra la ventana
     * actual
     *
     * @param event
     */
    @FXML
    private void configMenuSalir(ActionEvent event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorVendedorController::handleWindowClose");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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
     *
     */
    private void datosTabla() {
        try {
            administradorManager = (AdministradorManagerImplementation) new factory.AdministradorFactory().getAdministradorManagerImplementation();
            listvendedores = FXCollections.observableArrayList(administradorManager.getVendedores());
            tbVendedores.setItems(listvendedores);
            //Recorremos el ArrayList Observable de vendedores
            for (Vendedor v : listvendedores) {
                LOG.log(Level.INFO, "Lista de Vendedores"
                        + ": {0}", listvendedores);
            }
            //Añadimos esos vendedores dentro de la TableView
            tbVendedores.setItems(listvendedores);
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }catch (ErrorServerException ex) {
            LOG.log(Level.SEVERE, "ErrorBDException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }catch (ErrorBDException ex) {
            LOG.log(Level.SEVERE, "ErrorBDException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }
        
        //tbVendedores.setItems(FXCollections.observableArrayList(listvendedores));
    }
    
    /**
     *
     * @param event
     */
    private void txtBuscarVendedorNombre(ObservableValue observable, String oldValue, String newValue) {
    FilteredList<Vendedor> filteredData = new FilteredList<>(listvendedores, u -> true);

        filteredData.setPredicate(vendedor -> {
            //Cuando el TextField de búsqueda esté vacío, mostrará todos los vendedores
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            //Ponemos el valor en minúsculas
            String lowerCaseFilter = newValue.toLowerCase();
            //Buscamos al vendedor usando el nombre
            if (vendedor.getFullname().toLowerCase().contains(lowerCaseFilter)) {
                return true; //Vendedor encontrado(s)
            }

            return false; // Vendedor no encontrado(s)
        });
        // Convertimos la FilteredList en una SortedList.
        SortedList<Vendedor> sortedData = new SortedList<>(filteredData);

        // Se vincula el comparador SortedList al de la TableView.
        sortedData.comparatorProperty().bind(tbVendedores.comparatorProperty());

        // Añade los datos filtrados a la tabla
        tbVendedores.setItems(sortedData);
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private void actualizarEstadoVendedor(TableColumn.CellEditEvent<Vendedor, EstadoUsuario> data) {
        try {
            LOG.log(Level.INFO, "Nuevo Estado de Vendedor: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Estado de Vendedor: {0}", data.getOldValue());
            vendedorManager = (VendedorManagerImplementation) new factory.VendedorFactory().getVendedorManagerImplementation();
            //Devuelve el dato de la fila
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la fila
            v.setStatus(data.getNewValue());
            vendedorManager.edit(v);
            datosTabla();
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Administrador");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }

    }
    
    /**
     * MenuItem que nos redirige hacia la ventana de InicioAdministrador del
     * lado vendedor y cierra la ventana actual
     *
     * @param event
     */
    @FXML
    private void configMenuProveedores(ActionEvent event) {
        LOG.log(Level.INFO, "Ventana Inicio de Administrador (Proveedor)");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inicioAdministrador_proveedor.fxml"));

            Parent root = (Parent) loader.load();

            InicioAdministradorProveedorController controller = ((InicioAdministradorProveedorController) loader.getController());
            controller.initStage(root);
            stage.hide();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
        }
    }
    
    public void setAdministradorRESTClient(AdministradorManager administrador) {
        this.administradorManager = administrador;
    }

    public AdministradorManager getAdministradorRESTClient() {
        return this.administradorManager;
    }

    void setUsuario(Usuario usuario) {
        this.usuario=usuario;
    }
}