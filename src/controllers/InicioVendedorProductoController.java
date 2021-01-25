/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import implementation.ClienteManagerImplementation;
import implementation.ProductoManagerImplementation;
import implementation.ReservaManagerImplementation;
import implementation.VendedorManagerImplementacion;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import modelo.Cliente;
import modelo.DisponibilidadCell;
import modelo.Producto;
import modelo.Proveedor;
import modelo.Reserva;
import modelo.TipoProducto;
import modelo.Usuario;
import modelo.Vendedor;
import validar.Validar;

/**
 * FXML Controller class
 *
 * @author Fredy Vargas Flores
 */
public class InicioVendedorProductoController {

    @FXML
    private AnchorPane apInicioVendedor;
    @FXML
    private BorderPane bpInicioVendedor;
    @FXML
    private MenuBar mbMenu;
    @FXML
    private Menu mProductos;
    @FXML
    private MenuItem miSalir;
    @FXML
    private Menu mReservas;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Producto> tvProductos;
    @FXML
    private TableColumn<Producto, Long> tcReferencia;
    @FXML
    private TableColumn<Producto, String> tcProducto;
    @FXML
    private TableColumn<Producto, Integer> tcStock;
    @FXML
    private TableColumn<Producto, Float> tcPrecio;
    @FXML
    private TableColumn<TipoProducto, TipoProducto> tcTipo;
    @FXML
    private TableColumn<Producto, String> tcTalla;
    @FXML
    private TableColumn<Producto, String> tcDescripcion;
    @FXML
    private TableColumn<Producto, String> tcProveedor;
    @FXML
    private TableColumn<Producto, Date> tcDisponibilidad;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnBorrar;
    @FXML
    private MenuItem miActualizar;
    @FXML
    private MenuItem miGestionarReservas;
    //Producto selecionado
    private Producto productoSelecionado;
    private Usuario usuario;
    private Stage stage = new Stage();
    private static final Logger LOG = Logger.getLogger("controllers.InicioAdministradorProductoController");
    //Alert para que en todo momento el usuario(vendedor) este informado
    private Alert alert;
    //Una colección de todos los productos
    private ObservableList<Producto> productos;
    //La implementación del producto
    private ProductoManagerImplementation productoMI;
    //Una colección final de todas las tallas (ROPAS)
    private final ObservableList<String> tallasRopa = FXCollections.observableArrayList("XS", "S", "M", "L", "XL");
    //Una colección final de todas las tallas (ZAPATILLAS)
    private final ObservableList<String> tallasZapatillas = FXCollections.observableArrayList("36", "37", "38", "39", "40", "41", "42", "44", "45", "46");
    //Una colección  de todos los Proveedores
    private Set<Proveedor> proveedores;
    //Una colección personalizada de todos los Proveedores (ROPA ó AMBOS)
    private ObservableList<String> proveedoresZapatillas;
    //Una colección personalizada de todos los Proveedores (ZAPATILLAS ó AMBOS)
    private ObservableList<String> proveedoresRopa;
    //El proveedor siguiente es el proveedor por (defecto)
    private Proveedor flyshoes;
    //Una colección  de vendedores
    private Set<Vendedor> vendedores;

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
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::initStage");
        LOG.log(Level.INFO, "Ventana Gestión de Productos");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Flayshoes Productos");
        stage.setResizable(false);
        stage.setOnCloseRequest(this::handleWindowClose);
        stage.setOnShowing(this::handleWindowShowing);
        btnNuevo.setOnAction(this::btnNuevoClick);
        btnNuevo.setTooltip(new Tooltip("Pulse para dar de alta un nuevo producto "));
        btnBorrar.setOnAction(this::btnBorrarClick);
        btnBorrar.setTooltip(new Tooltip("Pulse para borrar el producto selecionado "));
        tfBuscar.textProperty().addListener(this::tfBuscarChanged);
        // vendedores.add((Vendedor) usuario);
        //Indicamos las imagenes de los botones
        imagenBotones();
        stage.show();
    }

    /**
     * Al cerrar la ventana, saldrá un mensaje de confirmacion
     *
     * @param event, WindowEvent
     */
    private void handleWindowClose(WindowEvent event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::handleWindowClose");
        alert = new Alert(Alert.AlertType.CONFIRMATION);
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
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::handleWindowShowing");
        btnBorrar.setDisable(true);
        tvProductos.setEditable(true);
        //actualiza y rellena la tabla con datos del servidor
        getAllProductos();
        manejoTablaProducto();

    }

    @FXML
    private void miActualizarClick(ActionEvent event) {
        //Actualizamos la tabla
        getAllProductos();
    }

    /**
     * Método para salir de la aplicación
     *
     * @param event
     */
    @FXML
    private void miSalirClick(ActionEvent event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::miSalirClick");
        alert = new Alert(Alert.AlertType.CONFIRMATION);
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
     * Método para que nos redireccionará a la ventana de reservas.
     *
     * @param event
     */
    @FXML
    private void miGestionarReservasClick(ActionEvent event) {
        /*   LOG.log(Level.INFO, "Ventana Login");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn.fxml"));
            Parent root = (Parent) loader.load();
            LogInController controller = ((LogInController) loader.getController());
            controller.initStage(root);
            stage.hide();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
        }*/
    }

    /**
     * Insertar un nuevo producto con algunos campos vacios(no nulos) para que
     * luego el usuario pueda modificarlos
     *
     * @param event
     */
    private void btnNuevoClick(ActionEvent event) {

        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::btnNuevoClick");
        //Desactivamos el boton borrar
        btnBorrar.setDisable(true);
        //Vamos a crear un producto con algunos campos vacios(NO NULOS)
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Producto producto = new Producto();
        producto.setDescripcion("");
        producto.setDisponibilidad(date);
        producto.setModelo("");
        producto.setPrecio(0f);
        producto.setStock(0);
        producto.setTipo(TipoProducto.ROPA);
        producto.setTalla("No definido");
        producto.setProveedor(flyshoes);
        producto.setVendedores(vendedores);
        try {
            productoMI.create(producto);
            getAllProductos();

        } catch (Exception e) {

        }
    }

    /**
     * Borrar producto
     *
     * @param event
     */
    private void btnBorrarClick(ActionEvent event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::btnBorrarClick");
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Borrar Producto");
        alert.setContentText("¿Estas seguro de que quieres borrar el producto seleccionado?");
        Optional<ButtonType> respuesta = alert.showAndWait();

        if (respuesta.get() == ButtonType.OK) {
            LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
            tvProductos.getSelectionModel().clearSelection();

            ReservaManagerImplementation reservaMI = (ReservaManagerImplementation) new factory.ReservaFactory().getReservaManagerImplmentation();
            VendedorManagerImplementacion vendedorMI = (VendedorManagerImplementacion) new factory.VendedorFactory().getVendedorManagerImplementacion();
            ObservableList<Reserva> reservas = FXCollections.observableArrayList(vendedorMI.findAllReservas());
            //El producto está reservado?
            if (!estaReservado(reservas, productoSelecionado.getId())) {
                System.out.println(reservas.size());
                borrandoProducto();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("Borrar Producto");
                alert.setContentText("¡El producto está reservado! ¿Quieres poner a 0 el stock del producto? (Recomendado)");
                Optional<ButtonType> respuestaForzar = alert.showAndWait();
                if (respuestaForzar.get() == ButtonType.OK) {
                    LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
                    actualizandoStock(productoSelecionado, 0);
                } else {
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Borrar Producto");
                    alert.setContentText("¡Se procederá a borrar el producto de todas las reservas!\n ¿Estás seguro que quieres continuar con el procedimiento?\n (NO Recomendado)");
                    Optional<ButtonType> respuestaForzado = alert.showAndWait();
                    if (respuestaForzado.get() == ButtonType.OK) {
                        LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
                        borrandoVendedorProducto(vendedorMI, productoSelecionado);
                        borrandoReservas(reservaMI, reservas, productoSelecionado);
                        borrandoProducto();
                    } else {
                        LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
                        event.consume();
                    }
                }
                LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
                event.consume();
            }

        } else {
            LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
            event.consume();
        }
    }

    /**
     * Cargamos todos los productos del servidor a nuestra colección
     */
    private void getAllProductos() {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::getAllProductos");
        productoMI = (ProductoManagerImplementation) new factory.ProductoFactory().getProductoManagerImplementation();
        ObservableList<Producto> productosServidor = null;
        try {
            productosServidor = FXCollections.observableArrayList(productoMI.findAllProductosAsc());
            tvProductos.setItems(productosServidor);
            productos = productosServidor;
            getAllProveedores();
            tvProductos.refresh();
        } catch (Exception e) {
            LOG.severe("getAllProductos:Exception");
        }

    }

    /**
     * Cargamos todos los proveedores del servidor a nuestra colección
     */
    private void getAllProveedores() {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::getAllProveedores");
        VendedorManagerImplementacion vendedorMI = (VendedorManagerImplementacion) new factory.VendedorFactory().getVendedorManagerImplementacion();
        ObservableList<Proveedor> proveedoresServidor = null;
        try {
            proveedoresServidor = FXCollections.observableArrayList(vendedorMI.getProveedoresProducto());
            cargarProveedores(proveedoresServidor);

        } catch (Exception e) {
            LOG.severe("getAllProveedores:Exception");
        }
    }

    /**
     * Cargamos la colección a nuestra tabla
     */
    private void manejoTablaProducto() {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::manejoTablaProducto");
        seleccionarProducto();
        productoMI = (ProductoManagerImplementation) new factory.ProductoFactory().getProductoManagerImplementation();

        //Referencia de producto(Id)
        tcReferencia.setCellValueFactory(new PropertyValueFactory<>("id"));

        //Gestión del precio del producto
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tcProducto.setCellFactory(TextFieldTableCell.forTableColumn());
        tcProducto.addEventHandler(TableColumn.<Producto, String>editCommitEvent(),
                event -> actualizarModelo(event));

        //Gestión del precio del producto
        tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tcStock.setCellFactory(TextFieldTableCell.<Producto, Integer>forTableColumn(new IntegerStringConverter()));
        tcStock.addEventHandler(TableColumn.<Producto, Integer>editCommitEvent(),
                event -> actualizarStock(event));

        //Gestión del precio del producto
        tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tcPrecio.setCellFactory(TextFieldTableCell.<Producto, Float>forTableColumn(new FloatStringConverter()));
        tcPrecio.addEventHandler(TableColumn.<Producto, Float>editCommitEvent(),
                event -> actualizarPrecio(event));

        //Gestión del tipo del producto (Selección)
        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tcTipo.setCellFactory(ChoiceBoxTableCell.
                forTableColumn(TipoProducto.ROPA, TipoProducto.ZAPATILLAS));
        tcTipo.addEventHandler(TableColumn.<Producto, TipoProducto>editCommitEvent(),
                event -> actualizarTipoRopa(event));

        //Gestión de la talla del producto (Selección)
        tcTalla.setCellValueFactory(new PropertyValueFactory<>("talla"));
        tcTalla.addEventHandler(TableColumn.<Producto, String>editCommitEvent(),
                event -> actualizarTalla(event));

        //Gestión de la descripción del producto 
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tcDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        tcDescripcion.addEventHandler(TableColumn.<Producto, String>editCommitEvent(),
                event -> actualizarDescripcion(event));

        //Gestión del proveedor del producto (Selección)
        tcProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        tcProveedor.setCellValueFactory((TableColumn.CellDataFeatures<Producto, String> param) -> new SimpleObjectProperty<>(param.getValue().getProveedor().getEmpresa()));
        tcProveedor.addEventHandler(TableColumn.<Proveedor, String>editCommitEvent(),
                event -> actualizarProveedor(event));

        //Gestión de la disponibilidad del producto (DatePicker)
        tcDisponibilidad.setCellValueFactory(new PropertyValueFactory<>("disponibilidad"));
        tcDisponibilidad.setCellFactory(new Callback<TableColumn<Producto, Date>, TableCell<Producto, Date>>() {
            @Override
            public TableCell<Producto, Date> call(TableColumn<Producto, Date> arg0) {
                return new DisponibilidadCell();
            }
        });
        tcDisponibilidad.addEventHandler(TableColumn.<Producto, Date>editCommitEvent(),
                event -> actualizarDisponibilidad(event));

    }

    /**
     * Valida y actualiza el tipo del producto
     *
     * @param event
     */
    private void actualizarTipoRopa(TableColumn.CellEditEvent<Producto, TipoProducto> event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizarTipoRopa");

        if (!event.getNewValue().equals(event.getOldValue())) {
            actualizandoTipoRopa(event.getRowValue(), event.getNewValue());
            tvProductos.refresh();

        }
    }

    /**
     * Actualiza el tipo de producto en el servidor
     *
     * @param producto
     * @param tipoRopa
     */
    private void actualizandoTipoRopa(Producto producto, TipoProducto tipoRopa) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizandoTipoRopa");
        try {
            producto.setTipo(tipoRopa);
            producto.setTalla("No definido");
            producto.setProveedor(flyshoes);
            productoMI.edit(producto);
            getAllProductos();
        } catch (Exception e) {

        }
    }

    /**
     * Comprueba que los campos sean correctos
     *
     * @param event
     */
    private void actualizarTalla(TableColumn.CellEditEvent<Producto, String> event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizarTalla");
        Producto comprobar = event.getRowValue();
        comprobar.setTalla(event.getNewValue());
        if (!productoExistente(comprobar)) {
            actualizandoTalla(event.getRowValue(), event.getNewValue());
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validación");
            alert.setHeaderText("El producto ya se encuentra registrado");
            alert.showAndWait();
        }
    }

    /**
     * Comprueba si el producto ya existe datos tomados en cuenta
     * (id,modelo,talla y proveedor)
     *
     * @param comprobarProducto
     * @return
     */
    private boolean productoExistente(Producto comprobarProducto) {
        //UN PRODUCTO YA EXISTE CUANDO EL PROVEEDOR, TALLA, TIPOROPA Y EL PRODUCTO(modelo) SON EL MISMO
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::productoExistente");
        for (Producto p : productos) {
            if ((Validar.cadenaSinEspacio(comprobarProducto.getModelo()).equalsIgnoreCase(Validar.cadenaSinEspacio(p.getModelo())))
                    && (Validar.cadenaSinEspacio(comprobarProducto.getProveedor().getEmpresa()).equalsIgnoreCase(Validar.cadenaSinEspacio(p.getProveedor().getEmpresa())))
                    && (comprobarProducto.getTalla().equalsIgnoreCase(p.getTalla())) && (comprobarProducto.getTipo() == (p.getTipo()))
                    && (p.getId() != comprobarProducto.getId())) {
                getAllProductos();
                return true;
            }
        }
        return false;
    }

    /**
     * Actualiza el producto en el servidor
     *
     * @param producto
     * @param talla
     */
    private void actualizandoTalla(Producto producto, String talla) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizandoTalla");
        try {
            producto.setTalla(talla);
            productoMI.edit(producto);
            getAllProductos();
        } catch (Exception e) {

        }
    }

    //CONFIGURACIÓN DE IMAGENES 
    /**
     * Añade las imagenes de los botones
     */
    private void imagenBotones() {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::imagenBotones");
        //Creamos un objeto y en él guardaremos la ruta donde se encuentra las imagenes para los botones
        URL linkAlta = getClass().getResource("/img/producto.png");
        URL linkBorrar = getClass().getResource("/img/eliminar.png");

        //Instanciamos una imagen pasándole la ruta de las imagenes y las medidas del boton 
        Image imageNuevo = new Image(linkAlta.toString(), 32, 32, false, true);
        Image imageBorrar = new Image(linkBorrar.toString(), 32, 32, false, true);

        //Añadimos la imagen a los botones que deban llevar icono
        btnNuevo.setGraphic(new ImageView(imageNuevo));
        btnBorrar.setGraphic(new ImageView(imageBorrar));

    }

    /**
     * valida y actualiza el campo modelo de producto
     *
     * @param event
     */
    private void actualizarModelo(TableColumn.CellEditEvent<Producto, String> event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizarModelo");

        if (Validar.longitudCadenaSinEspacio(event.getNewValue()) > 3) {

            if (!Validar.isNumber(event.getNewValue())) {
                if (Validar.isValidCadena(event.getNewValue())) {
                    Producto comprobar = event.getRowValue();
                    comprobar.setModelo(event.getNewValue());
                    if (!productoExistente(comprobar)) {
                        actualizandoModelo(event.getRowValue(), event.getNewValue());
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Validación");
                        alert.setHeaderText("El producto ya se encuentra registrado");
                        alert.showAndWait();
                    }
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Validación");
                    alert.setHeaderText("El campo producto tiene carateres extraños");
                    alert.showAndWait();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Validación");
                alert.setHeaderText("El campo producto no puede estar compuesto solo por números");
                alert.showAndWait();

            }

        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validación");
            alert.setHeaderText("El campo producto es demasiado corto");
            alert.showAndWait();
        }
        tvProductos.refresh();
    }

    /**
     * Actualiza el campo modelo del producto
     *
     * @param producto
     * @param modelo
     */
    private void actualizandoModelo(Producto producto, String modelo) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizandoModelo");
        try {
            producto.setModelo(modelo);
            productoMI.edit(producto);
            getAllProductos();
        } catch (Exception e) {
        }
    }

    /**
     * Valida y actualiza el campo stock
     *
     * @param event
     */
    private void actualizarStock(TableColumn.CellEditEvent<Producto, Integer> event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizarStock");

        if (Validar.isNumber(event.getNewValue().toString())) {
            if (event.getNewValue() >= 0) {
                actualizandoStock(event.getRowValue(), event.getNewValue());
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Validación");
                alert.setHeaderText("El campo stock no puede ser un número menor a cero");
                alert.showAndWait();
            }
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validación");
            alert.setHeaderText("El campo stock tiene que ser un número");
            alert.showAndWait();
        }
        tvProductos.refresh();
    }

    /**
     * Actualiza el campo stock del producto
     *
     * @param producto
     * @param stock
     */
    private void actualizandoStock(Producto producto, Integer stock) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizandoStock");
        try {
            producto.setStock(stock);
            productoMI.edit(producto);
            getAllProductos();
        } catch (Exception e) {
        }
    }

    /**
     * Valida y actualiza el precio del producto
     *
     * @param event
     */
    private void actualizarPrecio(TableColumn.CellEditEvent<Producto, Float> event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizarPrecio");

        if (Validar.isNumberFloat(event.getNewValue().toString())) {
            if (event.getNewValue() >= 0) {
                actualizandoPrecio(event.getRowValue(), event.getNewValue());
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Validación");
                alert.setHeaderText("El campo precio no puede ser un número menor a cero");
                alert.showAndWait();
            }
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validación");
            alert.setHeaderText("El campo precio tiene que ser un número");
            alert.showAndWait();

        }
        tvProductos.refresh();
    }

    /**
     * Actualiza el campo precio del producto
     *
     * @param producto
     * @param precio
     */
    private void actualizandoPrecio(Producto producto, Float precio) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizandoPrecio");
        try {
            producto.setPrecio(precio);
            productoMI.edit(producto);
            getAllProductos();
        } catch (Exception e) {
        }
    }

    /**
     * Valida y actualiza la descripción
     *
     * @param event
     */
    private void actualizarDescripcion(TableColumn.CellEditEvent<Producto, String> event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizarDescripcion");

        if (Validar.isValidCadena(event.getNewValue())) {
            if (!Validar.isNumber(event.getNewValue())) {
                if (Validar.longitudCadenaSinEspacio(event.getNewValue()) > 5) {
                    actualizandoDescripcion(event.getRowValue(), event.getNewValue());
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Validación");
                    alert.setHeaderText("El campo descripción es demasiado corto");
                    alert.showAndWait();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Validación");
                alert.setHeaderText("El campo descripción no puede estar compuesto sólo por números");
                alert.showAndWait();
            }
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validación");
            alert.setHeaderText("El campo descripción tiene carateres extraños");
            alert.showAndWait();
            tvProductos.refresh();
        }
    }

    /**
     * Actualiza la descripción del producto
     *
     * @param producto
     * @param descripcion
     */
    private void actualizandoDescripcion(Producto producto, String descripcion) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizandoDescripcion");
        try {
            producto.setDescripcion(descripcion);
            productoMI.edit(producto);
            getAllProductos();
        } catch (Exception e) {

        }
    }

    /**
     * Realizará la búqueda por referencia(id) ó por producto(modelo)
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void tfBuscarChanged(ObservableValue observable, String oldValue, String newValue) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::tfBuscarChanged");
        //Desactivamos el boton borrar
        btnBorrar.setDisable(true);
        //Vamos a crear una lista filtrada con los datos introducidos por el usuario(Vendedor)
        FilteredList<Producto> filtrado = new FilteredList<>(productos, (Producto p) -> true);
        filtrado.setPredicate(producto -> {
            //cuando el campo este vacio no habrá ningún cambio
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            //El dato nuevo lo convertimos en minúsculas para realizar una búsqueda más efectiva
            String minuscula = newValue.toLowerCase();
            //Este es el momento en el cuál pregunto si el dato introducido es un número
            if (Validar.isNumber(newValue)) {
                //Una vez que sé que es un número realizo una búsqueda por id
                if (producto.getId().toString().toLowerCase().contains(minuscula)) {
                    return true;
                }
                //Sé que no es un número ahora realizo la búsqueda por producto(modelo)
            } else if (producto.getModelo().toLowerCase().contains(minuscula)) {
                return true;
            }
            return false;
        });
        //Añadimos la lista filtrada a unba colección
        SortedList<Producto> productosFiltrados = new SortedList<>(filtrado);
        productosFiltrados.comparatorProperty().bind(tvProductos.comparatorProperty());
        //Cargamos la colección a la tabla de productos
        tvProductos.setItems(productosFiltrados);
    }

    /**
     * validará y actualizará la disponibilidad del producto
     *
     * @param event
     */
    private void actualizarDisponibilidad(TableColumn.CellEditEvent<Producto, Date> event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizarDisponibilidad");

        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        //Valida que la fecha no ser menor a la de hoy
        if (date.before(event.getNewValue())) {
            actualizandoDisponibilidad(event.getRowValue(), event.getNewValue());
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validación");
            alert.setHeaderText("la fecha no puede ser menor a la de hoy");
            alert.showAndWait();
            tvProductos.refresh();
        }
    }

    /**
     * Actualiza la fecha de disponibilidad del producto
     *
     * @param producto
     * @param date
     */
    private void actualizandoDisponibilidad(Producto producto, Date date) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizandoDisponibilidad");
        try {
            producto.setDisponibilidad(date);
            productoMI.edit(producto);
            getAllProductos();
        } catch (Exception e) {
            LOG.log(Level.INFO, "Beginning actualizandoDisponibilidad::Exception");
        }
    }

    /**
     * Método que se encargará de las operaciones que se deben realizar cuándo
     * un producto este seleccionado.
     */
    private void seleccionarProducto() {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::seleccionarProducto");
        //Establecemos un escuchador a la tabla productos
        tvProductos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    //Si la selección no es nula relizará las siguientes operaciones
                    if (newValue != null) {
                        //Activamos el boton borrar
                        btnBorrar.setDisable(false);
                        //Almacenamos la selección en una variable
                        productoSelecionado = newValue;
                        //Aprovecho esta selección para establecer valores en los campos TALLA y PROVEEDOR (ChoiceBox)
                        if (observable.getValue().getTipo().equals(TipoProducto.ROPA)) {
                            tcTalla.setCellFactory(ChoiceBoxTableCell.
                                    forTableColumn(tallasRopa));
                            tcProveedor.setCellFactory(ChoiceBoxTableCell.
                                    forTableColumn(proveedoresRopa));
                        } else {
                            tcTalla.setCellFactory(ChoiceBoxTableCell.
                                    forTableColumn(tallasZapatillas));
                            tcProveedor.setCellFactory(ChoiceBoxTableCell.
                                    forTableColumn(proveedoresZapatillas));
                        }
                    } else {
                        //Desactivamos el boton borrar
                        btnBorrar.setDisable(true);
                    }
                });
        // tvProductos.refresh();
    }

    /**
     * Valida y actualiza el proveedor del producto
     *
     * @param event
     */
    private void actualizarProveedor(TableColumn.CellEditEvent<Proveedor, String> event) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizarProveedor");
        Producto comprobar = productoSelecionado;
        Proveedor proveedorSelecionado = getProveedor(event.getNewValue());
        comprobar.setProveedor(proveedorSelecionado);
        //Validamos si el producto no existe
        if (!productoExistente(comprobar)) {
            actualizandoProveedor(comprobar);
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validación");
            alert.setHeaderText("El producto ya se encuentra registrado");
            alert.showAndWait();
        }
    }

    /**
     * Obtener datos del proveedor
     *
     * @param empresa
     * @return
     */
    private Proveedor getProveedor(String empresa) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::getProveedor");
        Proveedor proveedor = null;
        for (Proveedor p : proveedores) {
            if (p.getEmpresa().equals(empresa)) {
                proveedor = p;
                break;
            }
        }
        return proveedor;
    }

    /**
     * Este método se encargará de rellenar todos los nombres de las
     * empresas(Proveedores) y también cargar al proveedor FLYSHOES
     *
     * @param proveedoresServidor
     */
    private void cargarProveedores(ObservableList<Proveedor> proveedoresServidor) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::cargarProveedores");
        proveedores = new HashSet<>();
        proveedoresRopa = FXCollections.observableArrayList();
        proveedoresZapatillas = FXCollections.observableArrayList();

        for (Proveedor p : proveedoresServidor) {
            proveedores.add(p);
            if (p.getEmpresa().equalsIgnoreCase("Flyshoes")) {
                flyshoes = p;
            }
            //Hice dos colecciones para que la selección del vendedor sea mas comoda (ejm Si el tipo de ropa es "ROPA" que muestre solo los proveedores que nos proveen de "ROPA" ó "AMBOS")  
            if (p.getTipo().equals(TipoProducto.ROPA) || (p.getTipo().equals(TipoProducto.AMBAS))) {
                if (!comprobarEmpresa(p.getEmpresa(), proveedoresRopa)) {
                    proveedoresRopa.add(p.getEmpresa());
                }
            } else {
                if (!comprobarEmpresa(p.getEmpresa(), proveedoresZapatillas)) {
                    proveedoresZapatillas.add(p.getEmpresa());
                }
            }
        }
    }

    /**
     * Comprobar si la empresa ya existe en el array de proveedores
     *
     * @param empresa
     * @return
     */
    private boolean comprobarEmpresa(String empresa, ObservableList<String> proveedoresRopaZpatillas) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::comprobarEmpresa");
        for (String emp : proveedoresRopaZpatillas) {
            if (emp.equalsIgnoreCase(empresa)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vamos a actualizar el proveedor del producto
     *
     * @param producto
     */
    private void actualizandoProveedor(Producto producto) {
        LOG.log(Level.INFO, "Beginning InicioAdministradorProductoController::actualizandoProveedor");
        try {
            productoMI.edit(producto);
        } catch (Exception e) {
            LOG.log(Level.INFO, "Beginning actualizandoProveedor::Exception");
        }

    }

    /**
     * Verificar si el producto esta reservado
     *
     * @param reservas
     * @param id
     * @return boolean
     */
    private boolean estaReservado(ObservableList<Reserva> reservas, Long id) {

        for (Reserva r : reservas) {
            System.out.println(id + " buscandooo");
            System.out.println(r.getProducto().getId() + " servidor");
            if (r.getProducto().getId().equals(id)) {
                System.out.println(r.getProducto().getId() + " entroo");
                return true;
            }
        }
        return false;
    }

    /**
     * Borrando el producto del servidor
     */
    private void borrandoProducto() {
        try {

            productoMI.remove(productoSelecionado.getId().toString());
            getAllProductos();
        } catch (Exception e) {

        }
    }

    /**
     * Borrando las reservas del servidor
     *
     * @param reservas
     * @param productoSelecionado
     */
    private void borrandoReservas(ReservaManagerImplementation reservaMI, ObservableList<Reserva> reservas, Producto productoSelecionado) {
        try {
            ClienteManagerImplementation clienteMI = (ClienteManagerImplementation) new factory.ClienteFactory().getClienteManagerImplementation();
            Cliente clienteAux;

            for (Reserva r : reservas) {
                if (r.getProducto().getId().equals(productoSelecionado.getId())) {
                    reservaMI.remove(r.getId().toString());
                    System.out.println("borrando ");
                }
            }
            tvProductos.refresh();

        } catch (Exception e) {

        }
    }

    /**
     * Borrando el enlace de vendedor con producto
     *
     * @param vendedorMI
     * @param productoSelecionado
     */
    private void borrandoVendedorProducto(VendedorManagerImplementacion vendedorMI, Producto productoSelecionado) {
        //Lista de vendedores del servidor
        try {
            ObservableList<Vendedor> vendedoresServidor = FXCollections.observableArrayList(vendedorMI.findAllVendedores());
            for (Vendedor v : vendedoresServidor) {
                v.getProductos().remove(productoSelecionado);
                vendedorMI.edit(v);
            }
        } catch (Exception e) {

        }
    }

}
