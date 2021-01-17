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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import modelo.Administrador;
import modelo.EstadoUsuario;
import modelo.Proveedor;
import modelo.TipoProducto;
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
    private Menu menuVendedor;
    @FXML
    private MenuItem menuAdministrador;
    @FXML
    private MenuItem menuVendedores;
    @FXML
    private MenuItem menuSalir;

    private EntityManager entityManager;
    private List<Vendedor> vendedores = new ArrayList<>();
    private Usuario usuario;
    private Vendedor vendedor;

    /**
     * Establece un Usuario
     *
     * @param usuario Usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        LOG.log(Level.INFO, "Ventana Inicio de Administrador (Vendedor)");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Administrador");
        stage.setResizable(false);
        opcionesMenu();

        //Pane pane = new Pane();
        //MenuBar
        /*
         menuBar = new MenuBar();
        //Menus 
         menuPerfil = new Menu("Perfil");
         menuProveedor = new Menu("Proveedor");
        //MenuItem
         menuProveedores = new MenuItem("Lista de proveedores");
         menuSalir = new MenuItem("Salir");
         menuAdministrador = new MenuItem("Administrador");
        //Añadimos las acciones
        menuSalir.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                LOG.log(Level.INFO, "Se ha pulsado el MenuItem -- Salir");
                stage.close();
            }
        });
        menuProveedores.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                LOG.log(Level.INFO, "Se ha pulsado el MenuItem -- Lista de proveedores");
            }
        });
        menuAdministrador.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                LOG.log(Level.INFO, "Se ha pulsado el MenuItem -- Administrador");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Administrador");
                alert.setHeaderText(null);
                alert.setContentText("Informacion del administrador");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
            }
        });
        //Añadimos los menus dentro del menuBar 
        menuBar.getMenus().addAll(menuPerfil, menuProveedor);
        //Añadimos el menuItem dentro del menu 
        menuPerfil.getItems().addAll(menuAdministrador, menuSalir);
        menuProveedor.getItems().add(menuProveedores);
        //pane.setTop(menuBar);
         */
        //Indicamos las imagenes de los botones
        imagenBotones();
        //Indicamos el metodo que se encargara de la apariencia del stage cuando se cierre la ventana
        stage.setOnCloseRequest(this::handleWindowClose);
        //Indicamos el metodo que se encargara de la apariencia del stage cuando se inicie la ventana
        stage.setOnShowing(this::handleWindowShowing);
        //Indicamos las acciones de cada boton 
        btnAltaVendedor.setOnAction(this::btnAltaVendedorClick);
        btnBorrarVendedor.setOnAction(this::borrarVendedor);
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
    
    private int incrementarID() {
        int vendedorID = tbVendedores.getSelectionModel().getSelectedIndex();
        vendedorID++;
        return vendedorID;
    }

    private void btnAltaVendedorClick(ActionEvent event) {
        //Posicion actual
        TablePosition pos = tbVendedores.getFocusModel().getFocusedCell();
        //
        tbVendedores.getSelectionModel().clearSelection();

        Vendedor nuevoVendedor = new Vendedor();
        tbVendedores.getItems().add(nuevoVendedor);

        int row = tbVendedores.getItems().size() - 1;
        tbVendedores.getSelectionModel().select(row, pos.getTableColumn());
        tbVendedores.scrollTo(nuevoVendedor);
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

    private void opcionesMenu() {
        /* //Barra de menú
        MenuBar menuBar = new MenuBar();
        //Opción del menú -- Perfil
        Menu menuPerfil = new Menu();
        //Añadimos el menuItem Administrador a la opción de menú de Perfil
        MenuItem menuAdministrador = new MenuItem("Administrador");
        menuPerfil.getItems().add(menuAdministrador);
        //Opción del menú -- Salir
        Menu menuSalir = new Menu();
        
        //Añadimos a la barra de menú las opciones creadas
        menuBar.getMenus().addAll(menuPerfil, menuSalir);

        return menuBar;*/

 /* //BorderPane root = new BorderPane();
        //MenuBar
        MenuBar menuBar = new MenuBar();
        //Menus 
        Menu menuPerfil = new Menu("Perfil");
        Menu menuProveedor = new Menu("Proveedor");
        //MenuItem
        MenuItem menuProveedores = new MenuItem("Lista de proveedores");
        MenuItem menuSalir = new MenuItem("Salir");
        MenuItem menuAdministrador = new MenuItem("Administrador");
        //Añadimos las acciones
        menuSalir.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                LOG.log(Level.INFO, "Se ha pulsado el MenuItem -- Salir");
                stage.close();
            }
        });
        menuProveedores.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                LOG.log(Level.INFO, "Se ha pulsado el MenuItem -- Lista de proveedores");
            }
        });
        menuAdministrador.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                LOG.log(Level.INFO, "Se ha pulsado el MenuItem -- Administrador");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Administrador");
                alert.setHeaderText(null);
                alert.setContentText("Informacion del administrador");
                alert.initStyle(StageStyle.UTILITY);
                alert.showAndWait();
            }
        });
        //Añadimos los menus dentro del menuBar 
        menuBar.getMenus().addAll(menuPerfil, menuProveedor);
        //Añadimos el menuItem dentro del menu 
        menuPerfil.getItems().addAll(menuAdministrador, menuSalir);
        menuProveedor.getItems().add(menuProveedores);
        root.setTop(menuBar);
        Scene scene = new Scene(root);
        stage.setTitle("Administrador");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
         */
    }
/**
     * Borra el proveedor seleccionado de la tabla de vendedores
     *
     * @param event
     */
    private void borrarVendedor(ActionEvent event) {
        //Confirmar mensaje
        //Capturamos el indice del proveedor seleccionado y borro su item asociado de la tabla
        int vendedorSeleccionado = tbVendedores.getSelectionModel().getSelectedIndex();
        if (vendedorSeleccionado >= 0) {
            //Borramos el proveedor
            LOG.log(Level.INFO, "Se ha borrado un proveedor");
            tbVendedores.getItems().remove(vendedorSeleccionado);

        } else {
            //En el caso de no seleccionar un proveedor. Saldrá un alerta
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atención");
            alerta.setHeaderText("Vendedor no seleccionado");
            alerta.setContentText("Por favor, selecciona un vendedor de la tabla");
            alerta.showAndWait();

        }
    }

    /**
     * Inicializa la tabla de proveedores
     */
    private void iniciarColumnasTabla() {
        int id = incrementarID();
        seleccionarVendedor();
        //Hacemos que la tabla sea editable
        tbVendedores.setEditable(true);
        //Rellenamos la tabla con los proveedores
        //proveedores.addAll(getProveedores());
        datosTabla();
        //Definimos las celdas de la tabla, incluyendo que algunas pueden ser editables
        //Id del proveedor
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("login"));
        //Nombre del proveedor
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        //Indicamos que la celda puede cambiar a un TextField
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna descripción 
        colEmail.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Email: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Email: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setEmail(data.getNewValue());

        });
        //Tipo de producto 
        colNombre.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        //Indicamos que la celda puede cambiar a un TextField
        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna email 
        colNombre.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Nombre: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Nombre: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setFullname(data.getNewValue());

        });
        //Empresa del proveedor
        colEstado.setCellValueFactory(new PropertyValueFactory<>("status"));
        //Indicamos que la celda puede cambiar a un TextField
        colEstado.setCellFactory(ChoiceBoxTableCell.
                forTableColumn(EstadoUsuario.ENABLED, EstadoUsuario.DISABLED));
        colEstado.addEventHandler(TableColumn.<Vendedor, EstadoUsuario>editCommitEvent(),
                event -> actualizarEstadoVendedor(event));
        //Email del proveedor
        colUltimoAcceso.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));
        //Ultima contraseña del vendedor
        colUltimaContrasenia.setCellValueFactory(new PropertyValueFactory<>("lastPasswordChange"));
        //Email del proveedor
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        //Indicamos que la celda puede cambiar a un TextField
        colDireccion.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna email 
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
        //Aceptamos la edición de la celda de la columna email 
        colTelefono.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, Integer> data) -> {
            LOG.log(Level.INFO, "Nuevo Telefono: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Telefono: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setTelefono(data.getNewValue());

        });
        //Email del proveedor
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        //Indicamos que la celda puede cambiar a un TextField
        colDni.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna email 
        colDni.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            LOG.log(Level.INFO, "Nuevo Dni: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Dni: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setDni(data.getNewValue());

        });
        //Email del proveedor
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        //Indicamos que la celda puede cambiar a un TextField
        colSalario.setCellFactory(TextFieldTableCell.<Vendedor, Integer>forTableColumn(new IntegerStringConverter()));
        //Aceptamos la edición de la celda de la columna email 
        colSalario.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, Integer> data) -> {
            LOG.log(Level.INFO, "Nuevo Salario: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Salario: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setSalario(data.getNewValue());

        });
        //Email del proveedor
        colTienda.setCellValueFactory(new PropertyValueFactory<>("tienda"));
        //Indicamos que la celda puede cambiar a un TextField
        colTienda.setCellFactory(TextFieldTableCell.forTableColumn());
        //Aceptamos la edición de la celda de la columna email 
        colTienda.setOnEditCommit((TableColumn.CellEditEvent<Vendedor, String> data) -> {
            LOG.log(Level.INFO, "Nueva Tienda: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antigua Tienda: {0}", data.getOldValue());
            //Devuelve el dato de la celda
            Vendedor v = data.getRowValue();
            //Añadimos el nuevo valor a la celda
            v.setTienda(data.getNewValue());

        });
        
    }
    
    /**
     * Nos permite seleccionar a un proveedor de la tabla y este controla que el
     * botón BorrarProveedor y ActualizarProveedor esté habilitado o
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
     *
     */
    private void datosTabla() {
        /*
        Vendedor vendedor = new Vendedor();
        Administrador administrador = new Administrador();
        vendedor.setIdVendedor(Long.valueOf(1));
        vendedor.setLogin("Lucas");
        vendedor.setDni("123456789");
        vendedor.setSalario(1900);
        vendedor.setTienda("Nike");
        vendedor.setEmail("lucas@gmail.com");
        vendedor.setTelefono(927500299);
        vendedor.setAdministrador(administrador);

        ObservableList<Vendedor> datos = FXCollections.observableArrayList(vendedor);
        tbVendedores.setItems(datos);
*/
        tbVendedores.setItems(FXCollections.observableArrayList(vendedores));
    }
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private void actualizarEstadoVendedor(TableColumn.CellEditEvent<Vendedor, EstadoUsuario> event) {
        Vendedor vendedor = event.getRowValue();
        EstadoUsuario estado = event.getNewValue();
        vendedor.setStatus(event.getNewValue());
    }
}
