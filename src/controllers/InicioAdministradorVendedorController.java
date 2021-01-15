package controllers;

import java.net.URL;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javax.persistence.EntityManager;
import modelo.Administrador;
import modelo.EstadoUsuario;
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
    private TableColumn<Vendedor, String> colId;
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
    private TableColumn<Vendedor, String> colDni;
    @FXML
    private TableColumn<Vendedor, String> colDireccion;
    @FXML
    private TableColumn<Vendedor, String> colTienda;
    @FXML
    private TableColumn<Vendedor, Integer> colSalario;
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
        imagenBotones();
        stage.setOnCloseRequest(this::handleWindowClose);
        stage.setOnShowing(this::handleWindowShowing);
        btnAltaVendedor.setOnAction(this::btnAltaVendedorClick);
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
        LOG.log(Level.INFO, "Se ha borrado un proveedor");
        tbVendedores.getItems().removeAll(tbVendedores.getSelectionModel().getSelectedItem());
    }

    /**
     *
     * @param event
     */
    private void actualizarVendedor(ActionEvent event) {
        LOG.log(Level.INFO, "Confirmación de guardado de cambios");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);

        alert.setTitle("Administrador");
        alert.setContentText("¿Estas seguro de guardar los cambios?");
        Optional<ButtonType> respuesta = alert.showAndWait();

        if (respuesta.get() == ButtonType.OK) {
            LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
            event.consume();
        } else {
            LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
            event.consume();
        }
    }
    
    /**
     * Inicializa la tabla de proveedores
     */
    private void iniciarColumnasTabla() {
        /*
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
        colEmail.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        //Tipo de producto 
        colNombre.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        //Empresa del proveedor
        colEstado.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        //Email del proveedor
        colUltimoAcceso.setCellValueFactory(new PropertyValueFactory<>("email"));
        //Email del proveedor
        colDni.setCellValueFactory(new PropertyValueFactory<>("email"));
        //Email del proveedor
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("email"));
        //Email del proveedor
        colTienda.setCellValueFactory(new PropertyValueFactory<>("email"));
        //Email del proveedor
        colSalario.setCellValueFactory(new PropertyValueFactory<>("email"));
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
        */
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
    }
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
