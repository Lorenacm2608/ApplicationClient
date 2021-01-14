package controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
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
import javax.persistence.EntityManager;
import modelo.Administrador;
import modelo.Proveedor;
import modelo.TipoProducto;
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
    private MenuItem menuAdministrador;
    @FXML
    private Menu menuVendedor;
    @FXML
    private MenuItem menuSalir;
    @FXML
    private MenuItem menuVendedores;
    @FXML
    private TableView<Proveedor> tbProveedor;
    @FXML
    private TableColumn<Proveedor, Long> tcId;
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
    private TableColumn<Administrador, Long> tcAdmin;

    private EntityManager entityManager;
    private List<Proveedor> proveedores = new ArrayList<>();
    private Usuario usuario;
    private Proveedor proveedor;
    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscar;

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
        btnBorrarProveedor.setOnAction(this::borrarProveedor);
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

    private int incrementarID() {
        int proveedorID = tbProveedor.getSelectionModel().getSelectedIndex();
        proveedorID++;
        return proveedorID;
    }

    /**
     * Inicializa la tabla de proveedores
     */
    private void iniciarColumnasTabla() {
        int id = incrementarID();
        seleccionarProveedor();
        //Hacemos que la tabla sea editable
        tbProveedor.setEditable(true);
        //Rellenamos la tabla con los proveedores
        //proveedores.addAll(getProveedores());
        datosTabla();
        //Definimos las celdas de la tabla, incluyendo que algunas pueden ser editables
        //Id del proveedor
        tcId.setCellValueFactory(new PropertyValueFactory<>("idProveedor"));
        //tcId.setEditable(false);
        //tcId.setId(Long.valueOf(id));
        //Nombre del proveedor
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        tcNombre.setOnEditCommit((TableColumn.CellEditEvent<Proveedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Nombre: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Nombre: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Proveedor p = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            p.setNombre(data.getNewValue());
        });
        //Tipo de producto 
        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tcTipo.setCellFactory(ChoiceBoxTableCell.
                forTableColumn(TipoProducto.ROPA, TipoProducto.ZAPATILLAS));
        tcTipo.addEventHandler(TableColumn.<Proveedor, TipoProducto>editCommitEvent(),
                event -> actualizarTipoProducto(event));
        //Empresa del proveedor
        tcEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        tcEmpresa.setCellFactory(TextFieldTableCell.forTableColumn());
        tcEmpresa.setOnEditCommit((TableColumn.CellEditEvent<Proveedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Empresa: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antigua Empresa: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Proveedor p = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            p.setEmpresa(data.getNewValue());
        });
        //Email del proveedor
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        //Indicamos que la celda puede cambiar a un TextField
        tcEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna email 
        tcEmail.setOnEditCommit((TableColumn.CellEditEvent<Proveedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Email: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Email: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Proveedor p = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            p.setEmail(data.getNewValue());

        });
        //Teléfono del proveedor
        tcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        //Indicamos que la celda puede cambiar a un TextField
        tcTelefono.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna teléfono 
        tcTelefono.setOnEditCommit((TableColumn.CellEditEvent<Proveedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Telefono: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Telefono: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Proveedor p = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            p.setTelefono(data.getNewValue());

        });
        //Descripción del proveedor
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        //Indicamos que la celda puede cambiar a un TextField
        tcDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna descripción 
        tcDescripcion.setOnEditCommit((TableColumn.CellEditEvent<Proveedor, String> data) -> {
            LOG.log(Level.INFO, "Nueva Descripción: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Descripción: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Proveedor p = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            p.setDescripcion(data.getNewValue());

        });
        //Administrador asociado con el proveedor 
        tcAdmin.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
        //Añadimos las celdas dentro de la tabla de Proveedores (tbProveedor)
        proveedores.forEach((p) -> {
            tbProveedor.getItems().add(p);
        });
    }

    /**
     * Nos permite seleccionar a un proveedor de la tabla y este controla que el
     * botón BorrarProveedor y ActualizarProveedor esté habilitado o
     * deshabilitado
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
     *
     */
    private void datosTabla() {
        /*
        Proveedor proveedor = new Proveedor();
        Administrador administrador = new Administrador();
        proveedor.setIdProveedor(Long.valueOf(1));
        proveedor.setNombre("Lucas");
        proveedor.setTipo(ROPA);
        proveedor.setEmpresa("Nike");
        proveedor.setEmail("lucas@gmail.com");
        proveedor.setTelefono("927500299");
        proveedor.setDescripcion("Producto muy recomendado");
        proveedor.setAdministrador(administrador);

        ObservableList<Proveedor> datos = FXCollections.observableArrayList(proveedor);
        tbProveedor.setItems(datos);*/
        
        //Query queryPersonaFindAll = entityManager.createNamedQuery("Proveedor.find");
        //proveedores = queryPersonaFindAll.getResultList();
        tbProveedor.setItems(FXCollections.observableArrayList(proveedores));
        //tbProveedor.getSelectionModel().selectedIndexProperty().addListener(this::mostrarDatosTabla);

    }
    

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /* private List<Proveedor> getProveedores() {
        Proveedor proveedor;
        Administrador administrador;
        for (int i = 0; i < 10; i++) {
            proveedor = new Proveedor();
            administrador = new Administrador();
            //Administrador
            administrador.setId_usuario(Long.valueOf(i));
            //administrador.setId_usuario(Long.valueOf(1));
            //Proveedor
            proveedor.setIdProveedor(Long.valueOf(i));
            proveedor.setNombre("Lucas");
            proveedor.setTipo(ROPA);
            proveedor.setEmpresa("Nike");
            proveedor.setEmail("lucas@gmail.com");
            proveedor.setTelefono("927500299");
            proveedor.setDescripcion("Producto muy recomendado");
            proveedor.setAdministrador(administrador);
            //proveedor.setAdministrador(administrador.setId_usuario(Long.valueOf(i)));
            proveedores.add(proveedor);
        }

        return proveedores;
    }*/
    //CONFIGURACIÓN DE BOTONES
    /**
     * Nos permite redirigirnos hacia la ventana de SignUpProveedorView
     *
     * @param event ActionEvent
     */
    private void btnAltaProveedorClick(ActionEvent event) {
        //Posicion actual
        TablePosition pos = tbProveedor.getFocusModel().getFocusedCell();
        //
        tbProveedor.getSelectionModel().clearSelection();

        Proveedor nuevoProveedor = new Proveedor();
        tbProveedor.getItems().add(nuevoProveedor);

        int row = tbProveedor.getItems().size() - 1;
        tbProveedor.getSelectionModel().select(row, pos.getTableColumn());
        tbProveedor.scrollTo(nuevoProveedor);
    }

    /**
     * Borra el proveedor seleccionado de la tabla de proveedores
     *
     * @param event
     */
    private void borrarProveedor(ActionEvent event) {
        //Capturamos el indice del proveedor seleccionado y borro su item asociado de la tabla
        int proveedorSeleccionado = tbProveedor.getSelectionModel().getSelectedIndex();
        if (proveedorSeleccionado >= 0) {
            //Borramos el proveedor
            LOG.log(Level.INFO, "Se ha borrado un proveedor");
            tbProveedor.getItems().remove(proveedorSeleccionado);

        } else {
            //En el caso de no seleccionar un proveedor. Saldrá un alerta
            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Atención");
            alerta.setHeaderText("Proveedor no seleccionado");
            alerta.setContentText("Por favor, selecciona un proveedor de la tabla");
            alerta.showAndWait();

        }
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

            /*InicioAdministradorVendedorController controller = ((InicioAdministradorVendedorController) loader.getController());
            controller.initStage(root);*/
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

    private void actualizarTipoProducto(TableColumn.CellEditEvent<Proveedor, TipoProducto> event) {
        Proveedor proveedor = event.getRowValue();
        TipoProducto tipo = event.getNewValue();
        proveedor.setTipo(event.getNewValue());
    }

}
