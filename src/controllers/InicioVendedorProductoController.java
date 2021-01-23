/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import implementation.ProductoManagerImplementation;
import implementation.VendedorManagerImplementacion;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
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
import modelo.DisponibilidadCell;
import modelo.Producto;
import modelo.Proveedor;
import modelo.TipoProducto;
import modelo.Usuario;
import modelo.Vendedor;
import validar.Validar;

/**
 * FXML Controller class
 *
 * @author Fredy
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
    private MenuItem miRegistrarProducto;
    @FXML
    private MenuItem miSalir;
    @FXML
    private Menu mReservas;
    @FXML
    private MenuItem miVerReservas;
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

    private Producto productoSelecionado;
    private Usuario usuario;
    private Stage stage = new Stage();
    private static final Logger LOG = Logger.getLogger("controllers.InicioAdministradorProductoController");
    private Alert alert;
    private ObservableList<Producto> productos;
    private ProductoManagerImplementation productoMI;
    private final ObservableList<String> tallas = FXCollections.observableArrayList(
            "XS", "S", "M", "L", "XL", "36", "37", "38", "39", "40", "41", "42", "44", "45", "46");
    private ObservableList<Proveedor> proveedores;
    private Set<Vendedor> vendedores;

    //LOS PRODUCTOS QUE TENGAN EL MISMO NOMBRE,PROVEEDOR Y TALLA MENSAJE AL USUARIO YA EXISTE Y QUE SOLO ACTUALICE EL PRECIO
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
        /* btnCancelar.setOnAction(this::btnCancelarClick);
        btnCancelar.setTooltip(new Tooltip("Pulse para cancelar "));
        btnVerificar.setOnAction(this::btnVerificarClick);
        btnVerificar.setTooltip(new Tooltip("Pulse para verificar "));
        hlReenviarCodigo.setOnAction(this::hlReenviarCodigoClick);
        hlReenviarCodigo.setTooltip(new Tooltip("Reenviar código "));
        //Direccion de email

        lblCorreoElectronico.setText(prepararEmail(usuario.getEmail()));
        System.out.println(usuario.getEmail());
        tfCodigoTemporal.textProperty().addListener(this::txtChanged);
        pfNuevaContrasenia.textProperty().addListener(this::pfContraseniaChanged);
        pfRepetirContrasenia.textProperty().addListener(this::pfContraseniaChanged);
        //  txtUsuario.textProperty().addListener(this::txtChanged);
        //  txtContrasena.textProperty().addListener(this::txtChanged);
        //  hlRegistrarse.setOnAction(this::hlRegistrarseClick);
        //  hlContraseniaOlvidada.setOnAction(this::hlContraseniaOlvidadClick);*/
        stage.show();
    }

    /**
     * Al cerrar la ventana, saldrá un mensaje de confirmacion
     *
     * @param event, WindowEvent
     */
    private void handleWindowClose(WindowEvent event) {
        /*
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
        }*/
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

    /**
     * Insertar nuevo producto
     *
     * @param event
     */
    private void btnNuevoClick(ActionEvent event) {
        LOG.log(Level.INFO, "Beginning LoginController::handleWindowShowing");
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Producto producto = new Producto();
        producto.setDescripcion("");
        producto.setDisponibilidad(date);
        producto.setModelo("");
        producto.setPrecio(0f);
        producto.setStock(0);
        producto.setTalla("No definido");
        producto.setProveedor(null);
        producto.setVendedores(vendedores);
        try{
            productoMI.create(producto);
            getAllProductos();
        }catch(Exception e){
            
        }
    }

    /**
     * Borrar producto
     *
     * @param event
     */
    private void btnBorrarClick(ActionEvent event) {
        LOG.log(Level.INFO, "Beginning LoginController::handleWindowShowing");/*
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
         */
    }

    /**
     * Cargamos todos los productos del servidor a nuestra colección
     */
    private void getAllProductos() {
        productoMI = (ProductoManagerImplementation) new factory.ProductoFactory().getProductoManagerImplementation();
        ObservableList<Producto> productoServidor = null;
        try {
            productoServidor = FXCollections.observableArrayList(productoMI.findAllProductosAsc());
            tvProductos.setItems(productoServidor);
            productos = productoServidor;
            System.out.println(productoServidor.size());
        } catch (Exception e) {
            LOG.severe("InicioAdministradorProductoController:getAllProductos");
        }

    }

    /**
     * Cargamos todos los proveedores del servidor a nuestra colección
     */
    private List<Proveedor> getAllProveedores() {
        VendedorManagerImplementacion vendedorMI = (VendedorManagerImplementacion) new factory.VendedorFactory().getVendedorManagerImplementacion();
        ObservableList<Proveedor> vendedorServidor = null;
        try {
            vendedorServidor = FXCollections.observableArrayList(vendedorMI.getProveedoresProducto());
            System.out.println(vendedorServidor.size());
        } catch (Exception e) {
            LOG.severe("InicioAdministradorProductoController:getAllProductos");
        }
        return vendedorServidor;

    }

    /**
     * Cargamos la colección a nuestra tabla
     */
    private void manejoTablaProducto() {
        seleccionarProducto();
        productoMI = (ProductoManagerImplementation) new factory.ProductoFactory().getProductoManagerImplementation();

        tcReferencia.setCellValueFactory(new PropertyValueFactory<>("id"));

        tcProducto.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tcProducto.setCellFactory(TextFieldTableCell.forTableColumn());
        tcProducto.addEventHandler(TableColumn.<Producto, String>editCommitEvent(),
                event -> actualizarModelo(event));

        tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tcStock.setCellFactory(TextFieldTableCell.<Producto, Integer>forTableColumn(new IntegerStringConverter()));
        tcStock.addEventHandler(TableColumn.<Producto, Integer>editCommitEvent(),
                event -> actualizarStock(event));

        tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tcPrecio.setCellFactory(TextFieldTableCell.<Producto, Float>forTableColumn(new FloatStringConverter()));
        tcPrecio.addEventHandler(TableColumn.<Producto, Float>editCommitEvent(),
                event -> actualizarPrecio(event));

        //choicebox
        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tcTipo.setCellFactory(ChoiceBoxTableCell.
                forTableColumn(TipoProducto.ROPA, TipoProducto.ZAPATILLAS));
        tcTipo.addEventHandler(TableColumn.<Producto, TipoProducto>editCommitEvent(),
                event -> actualizarTipoRopa(event));

        //choicebox
        tcTalla.setCellValueFactory(new PropertyValueFactory<>("talla"));
        tcTalla.setCellFactory(ChoiceBoxTableCell.
                forTableColumn(tallas));
        tcTalla.addEventHandler(TableColumn.<Producto, String>editCommitEvent(),
                event -> actualizarTalla(event));

        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tcDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        tcDescripcion.addEventHandler(TableColumn.<Producto, String>editCommitEvent(),
                event -> actualizarDescripcion(event));

        //Ojito con esto
        tcProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        tcProveedor.setCellValueFactory((TableColumn.CellDataFeatures<Producto, String> param) -> new SimpleObjectProperty<>(param.getValue().getProveedor().getEmpresa()));
        //  tcProveedor.setCellFactory(ChoiceBoxTableCell.
        //      forTableColumn(proveedores));
        //  tcProveedor.setCellValueFactory((TableColumn.CellDataFeatures<Producto, String> param) -> new SimpleObjectProperty<>(param.getValue().getProveedor().getEmpresa()));
        tcProveedor.addEventHandler(TableColumn.<Producto, String>editCommitEvent(),
                event -> actualizarProveedor(event));

        // tcProveedor.setCellValueFactory((TableColumn.CellDataFeatures<Producto, String> param) -> new SimpleObjectProperty<>(param.getValue().getProveedor().getEmpresa()));
        //  tcProveedor.setOnEditCommit(valor -> {
        //      System.out.println("Nuevo : " + valor.getNewValue());
        //      System.out.println("Anterior : " + valor.getOldValue());
        //   Reserva reserva = valor.getRowValue();
        //       System.out.println("id de reserva " + reserva.getId());
        //          reserva.setDescripcion(valor.getNewValue());
        //  });
        //fecha
        tcDisponibilidad.setCellValueFactory(new PropertyValueFactory<>("disponibilidad"));
        tcDisponibilidad.setCellFactory(new Callback<TableColumn<Producto, Date>, TableCell<Producto, Date>>() {
            @Override
            public TableCell<Producto, Date> call(TableColumn<Producto, Date> arg0) {
                return new DisponibilidadCell();
            }
        });
        tcDisponibilidad.addEventHandler(TableColumn.<Producto, Date>editCommitEvent(),
                event -> actualizarDisponibilidad(event));

        // tvProductos.setItems(productos);
    }

    /**
     * Valida y actualiza el tipo del producto
     *
     * @param event
     */
    private void actualizarTipoRopa(TableColumn.CellEditEvent<Producto, TipoProducto> event) {

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
        try {
            producto.setTipo(tipoRopa);
            producto.setTalla("No definido");
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

        if (Validar.isNumber(event.getNewValue())) {
            if (event.getRowValue().getTipo().equals(TipoProducto.ZAPATILLAS)) {
                actualizandoTalla(event.getRowValue(), event.getNewValue());
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Validación");
                alert.setHeaderText("El campo talla es incorrecto");
                alert.showAndWait();
                tvProductos.refresh();
            }
        } else if (event.getRowValue().getTipo().equals(TipoProducto.ROPA)) {
            actualizandoTalla(event.getRowValue(), event.getNewValue());
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validación");
            alert.setHeaderText("El campo talla es incorrecto");
            alert.showAndWait();
            tvProductos.refresh();
        }

    }

    /**
     * Actualiza el producto en el servidor
     *
     * @param producto
     * @param talla
     */
    private void actualizandoTalla(Producto producto, String talla) {
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

        if (Validar.longitudCadenaSinEspacio(event.getNewValue()) > 3) {

            if (!Validar.isNumber(event.getNewValue())) {
                if (Validar.isValidCadena(event.getNewValue())) {
                    actualizandoModelo(event.getRowValue(), event.getNewValue());
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
        try {
            producto.setModelo(modelo);
            productoMI.edit(producto);
        } catch (Exception e) {
        }
    }

    /**
     * Valida y actualiza el campo stock
     *
     * @param event
     */
    private void actualizarStock(TableColumn.CellEditEvent<Producto, Integer> event) {

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
        try {
            producto.setStock(stock);
            productoMI.edit(producto);
        } catch (Exception e) {
        }
    }

    /**
     * Valida y actualiza el precio del producto
     *
     * @param event
     */
    private void actualizarPrecio(TableColumn.CellEditEvent<Producto, Float> event) {

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
        try {
            producto.setPrecio(precio);
            productoMI.edit(producto);
        } catch (Exception e) {
        }
    }

    /**
     * Valida y actualiza la descripción
     *
     * @param event
     */
    private void actualizarDescripcion(TableColumn.CellEditEvent<Producto, String> event) {

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
        try {
            producto.setDescripcion(descripcion);
            productoMI.edit(producto);
            getAllProductos();
        } catch (Exception e) {

        }
    }

    /**
     * Realizará la búqueda por referncia(id) ó por producto
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void tfBuscarChanged(ObservableValue observable, String oldValue, String newValue) {
        FilteredList<Producto> filtrado = new FilteredList<>(productos, (Producto p) -> true);
        filtrado.setPredicate(producto -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String minuscula = newValue.toLowerCase();
            if (Validar.isNumber(newValue)) {
                if (producto.getId().toString().toLowerCase().contains(minuscula)) {
                    return true;
                }
            } else if (producto.getModelo().toLowerCase().contains(minuscula)) {
                return true;
            }
            return false;
        });
        SortedList<Producto> productosFiltrados = new SortedList<>(filtrado);
        productosFiltrados.comparatorProperty().bind(tvProductos.comparatorProperty());
        tvProductos.setItems(productosFiltrados);
    }

    /**
     * validará y actualizará la disponibilidad
     *
     * @param event
     */
    private void actualizarDisponibilidad(TableColumn.CellEditEvent<Producto, Date> event) {

        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
     * Actualiza la fecha de disponibilidad
     *
     * @param producto
     * @param date
     */
    private void actualizandoDisponibilidad(Producto producto, Date date) {
        try {
            LOG.info(date + " del datepicker");
            LOG.info(producto.getDisponibilidad().toString());
            producto.setDisponibilidad(date);
            productoMI.edit(producto);

            getAllProductos();
        } catch (Exception e) {

        }
    }

    /**
     * Metodo que se encargará de las operaciones que se deben realizar cuando
     * un producto este seleccionado.
     */
    private void seleccionarProducto() {
        // productoSelecionado = tvProductos.getSelectionModel().getSelectedItem();
        tvProductos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (productoSelecionado != null) {
                        btnBorrar.setDisable(true);
                    } else {
                        btnBorrar.setDisable(false);
                    }
                });
    }

    private void actualizarProveedor(TableColumn.CellEditEvent<Producto, String> event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
