/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import implementation.ProductoManagerImplementation;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import javax.imageio.ImageIO;
import modelo.DisponibilidadCell;
import modelo.Producto;
import modelo.ProductoCell;
import modelo.TipoProducto;
import modelo.Usuario;

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
    private TableColumn<Producto, byte[]> tcImagen;
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

    private Usuario usuario;
    private Stage stage = new Stage();
    private static final Logger LOG = Logger.getLogger("controllers.InicioAdministradorProductoController");
    private Alert alert;
    private ObservableList<Producto> productos;
    private ProductoManagerImplementation productoMI;
    private final ObservableList<String> tallN = FXCollections.observableArrayList(
            "36", "37", "38", "39", "40", "41", "42", "44", "45", "46");
    private final ObservableList<String> tallaS = FXCollections.observableArrayList(
            "XS", "S", "M", "L", "XL");

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
        productos = (ObservableList<Producto>) getAllProductos();
        //for(Producto p:productos)
        //    System.out.println(p.toString());
        manejoTablaProducto();

        /*
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
     * Insertar nuevo producto
     *
     * @param event
     */
    private void btnNuevoClick(ActionEvent event) {
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
    private List<Producto> getAllProductos() {
        productoMI = (ProductoManagerImplementation) new factory.ProductoFactory().getProductoManagerImplementation();
        ObservableList<Producto> productoServidor = null;
        try {
           productoServidor = FXCollections.observableArrayList(productoMI.findAllProductosAsc());
            System.out.println(productoServidor.size());
        } catch (Exception e) {
            LOG.severe("InicioAdministradorProductoController:getAllProductos");
        }
        return productoServidor;

    }

    /**
     * Cargamos la colección a nuestra tabla
     */
    private void manejoTablaProducto() {

        productoMI = (ProductoManagerImplementation) new factory.ProductoFactory().getProductoManagerImplementation();
        //Columnas
        //  ImageView uno = new ImageView(new Image(this.getClass().getResourceAsStream("nik.jpg")));
        //  productos.forEach((p) -> {
        //      p.setImagen(extractBytes("E:\\reto2\\ApplicationClient\\src\\controllers\\"));
        //  });
        /*
        byte[] bytearray = producto.getImagen();
        BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytearray));

        Image i = SwingFXUtils.toFXImage(imag, null);
        ivImagen.setImage(i);
         */

        tcImagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));
        tcImagen.setCellFactory(new Callback<TableColumn<Producto, byte[]>, TableCell<Producto, byte[]>>() {
           @Override
            public TableCell<Producto, byte[]> call(TableColumn<Producto, byte[]> param) {
               return new ProductoCell(); //To change body of generated methods, choose Tools | Templates.
            }
        });

        /*
        tcDisponibilidad.setCellFactory(new Callback<TableColumn<Producto, Date>, TableCell<Producto, Date>>() {
            @Override
            public TableCell<Producto, Date> call(TableColumn<Producto, Date> arg0) {
                return new DisponibilidadCell();
            }
        });
         */
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tcProducto.setCellFactory(TextFieldTableCell.forTableColumn());
        tcProducto.setOnEditCommit(valor -> {
            System.out.println("Nuevo : " + valor.getNewValue());
            System.out.println("Anterior : " + valor.getOldValue());
            //   Reserva reserva = valor.getRowValue();
            //       System.out.println("id de reserva " + reserva.getId());
            //          reserva.setDescripcion(valor.getNewValue());
        });

        tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tcStock.setCellFactory(TextFieldTableCell.<Producto, Integer>forTableColumn(new IntegerStringConverter()));
        tcStock.setOnEditCommit(valor -> {
            System.out.println("Nuevo : " + valor.getNewValue());
            System.out.println("Anterior : " + valor.getOldValue());
            //   Reserva reserva = valor.getRowValue();
            //       System.out.println("id de reserva " + reserva.getId());
            //          reserva.setDescripcion(valor.getNewValue());
        });

        tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tcPrecio.setCellFactory(TextFieldTableCell.<Producto, Float>forTableColumn(new FloatStringConverter()));
        tcPrecio.setOnEditCommit(valor -> {
            System.out.println("Nuevo : " + valor.getNewValue());
            System.out.println("Anterior : " + valor.getOldValue());
            //   Reserva reserva = valor.getRowValue();
            //       System.out.println("id de reserva " + reserva.getId());
            //          reserva.setDescripcion(valor.getNewValue());
        });

        //choicebox
        tcTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tcTipo.setCellFactory(ChoiceBoxTableCell.
                forTableColumn(TipoProducto.ROPA, TipoProducto.ZAPATILLAS));
        tcTipo.addEventHandler(TableColumn.<Producto, TipoProducto>editCommitEvent(),
                event -> actualizarTipoRopa(event));

        //choicebox
        tcTalla.setCellValueFactory(new PropertyValueFactory<>("talla"));
        tcTalla.setCellFactory(ChoiceBoxTableCell.
                forTableColumn("36", "37", "38", "39", "40", "41", "42", "44", "45", "46", "XS", "S", "M", "L", "XL"));
        tcTalla.addEventHandler(TableColumn.<Producto, String>editCommitEvent(),
                event -> actualizarTalla(event));
        /*
        https://coderanch.com/t/703498/java/Tableview-show-combobox-edit
        setCellFactory(ComboBoxTableCell.forTableColumn(radioList));
        columnRadiopharmaceutical.setOnEditCommit(t ->{
            t.getRowValue().setRadiopharmaceutical(t.getNewValue());
            columnSupplier.getTableView().requestFocus();
            addTabl
         */

        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tcDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        tcDescripcion.setOnEditCommit(valor -> {
            System.out.println("Nuevo : " + valor.getNewValue());
            System.out.println("Anterior : " + valor.getOldValue());
            //   Reserva reserva = valor.getRowValue();
            //       System.out.println("id de reserva " + reserva.getId());
            //          reserva.setDescripcion(valor.getNewValue());
        });

        //Ojito con esto
        tcProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        tcProveedor.setCellValueFactory((TableColumn.CellDataFeatures<Producto, String> param) -> new SimpleObjectProperty<>(param.getValue().getProveedor().getEmpresa()));
        tcProveedor.setOnEditCommit(valor -> {
            System.out.println("Nuevo : " + valor.getNewValue());
            System.out.println("Anterior : " + valor.getOldValue());
            //   Reserva reserva = valor.getRowValue();
            //       System.out.println("id de reserva " + reserva.getId());
            //          reserva.setDescripcion(valor.getNewValue());
        });

        //fecha
        tcDisponibilidad.setCellValueFactory(new PropertyValueFactory<>("disponibilidad"));
        tcDisponibilidad.setCellFactory(new Callback<TableColumn<Producto, Date>, TableCell<Producto, Date>>() {
            @Override
            public TableCell<Producto, Date> call(TableColumn<Producto, Date> arg0) {
                return new DisponibilidadCell();
            }
        });
        tcDisponibilidad.setOnEditCommit(value -> {
            System.out.println("Nuevo : " + value.getNewValue());
            System.out.println("Anterior : " + value.getOldValue());
            Producto producto = value.getRowValue();
            System.out.println("id de reserva " + producto.getId());
            producto.setDisponibilidad(value.getNewValue());
        });

        tvProductos.setItems(productos);

    }

    private void actualizarTipoRopa(TableColumn.CellEditEvent<Producto, TipoProducto> event) {
        System.out.println("Estoy aca la reserva es " + event);
        System.out.println((TipoProducto) event.getNewValue());
        System.out.println((TipoProducto) event.getOldValue());
        //   Reserva reserva = event.getRowValue();
        //  EstadoReserva estado = event.getNewValue();
        //  System.out.println("Estado: " + estado.toString() + reserva.getId() + reserva.getDescripcion() + "Esto es de reserva: " + reserva.getEstado().toString());
        //  reserva.setEstado(event.getNewValue());
        //   System.out.println(reserva.getId() + reserva.getDescripcion() + "Esto es de reserva: " + reserva.getEstado().toString()+" fecha de entrega es "+reserva.getFechaEntrega());
    }

    private void actualizarTalla(TableColumn.CellEditEvent<Producto, String> event) {
        System.out.println("Estoy aca la reserva es " + event);
        System.out.println(event.getNewValue());
        System.out.println(event.getOldValue());
        //   Reserva reserva = event.getRowValue();
        //  EstadoReserva estado = event.getNewValue();
        //  System.out.println("Estado: " + estado.toString() + reserva.getId() + reserva.getDescripcion() + "Esto es de reserva: " + reserva.getEstado().toString());
        //  reserva.setEstado(event.getNewValue());
        //   System.out.println(reserva.getId() + reserva.getDescripcion() + "Esto es de reserva: " + reserva.getEstado().toString()+" fecha de entrega es "+reserva.getFechaEntrega());
    }

    public byte[] extractBytes(String path) {
        // abrimos la imagen
        File imgPath = new File(path);
        ByteArrayOutputStream baos = null;
        try {
            BufferedImage img = ImageIO.read(new File(path, "n.png"));
            ImageIO.write(img, "png", baos);
            baos.flush();
            //   String base64String = Base64.encode(baos.toByteArray());
            baos.close();
        } catch (IOException e) {
        }
        return (baos.toByteArray());
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
}
