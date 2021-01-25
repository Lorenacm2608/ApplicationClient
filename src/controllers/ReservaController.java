/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import implementation.ClienteImplementation;
import client.ClienteRESTClient;
import implementation.ProductoManagerImplementation;
import client.ProductoRESTClient;
import implementacion.ReservaImplementation;
import client.ReservaRESTClient;
import exceptions.ErrorBDException;
import exceptions.ErrorServerException;
import exceptions.ProductoExistenteException;
import modelo.Cliente;
import modelo.EstadoReserva;
import modelo.Producto;
import modelo.Reserva;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static java.util.Optional.empty;
import java.util.ResourceBundle;
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
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javax.ws.rs.ClientErrorException;
import manager.ClienteManager;
import manager.ProductoManager;
import manager.ReservaManager;
import modelo.FechaEntregaCell;
import modelo.Usuario;


/**
 * FXML Controller class
 *
 * @author 2dam
 */
public class ReservaController implements Initializable {
    
    private static final Logger LOG = Logger.getLogger("controllers.ReservaController");
    
    @FXML
    private Pane pnReserva;
    @FXML
    private Label lblReservas;
    @FXML
    private TableView<Reserva> tbReservas;
    @FXML
    private TableColumn<Reserva, Cliente> tcCliente;
    @FXML
    private TableColumn<Reserva, Producto> tcProducto;
    @FXML
    private TableColumn<Reserva,Integer> tcCantidad;
    @FXML
    private TableColumn<Reserva, String> tcDescripcion;
    @FXML
    private TableColumn<Reserva, EstadoReserva> tcEstado;
    @FXML
    private TableColumn<Reserva, Date> tcFecha;
    @FXML
    private TableColumn<Reserva, Date> tcReserva;
    @FXML
    private TableColumn<Reserva, Date> tcEntrega;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnInsertar;
    @FXML
    private Button btnBorrar;
    @FXML
    private TextField tfBuscar;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuPerfil;
    @FXML
    private MenuItem menuSalir;
    @FXML
    private Menu menuAyuda;
    @FXML
    private MenuItem menuAbout;

    private Stage stage = new Stage();
    private ChoiceBox<Cliente> clienteBox = new ChoiceBox<>();
    private ChoiceBox<Producto> productoBox = new ChoiceBox<>();
    private Integer stock;
    
    private ObservableList<Reserva> masterData = FXCollections.observableArrayList();
    private ObservableList<Cliente> masterClientes = FXCollections.observableArrayList();
    private ObservableList<Producto> masterProductos = FXCollections.observableArrayList();
    
    private Reserva reserva;
    private Producto producto = new Producto();
    
    private ReservaManager reservaManager;
    private ProductoManager productoManager;
    private ClienteManager clienteManager;
    
    private Alert alert;
    
    public ReservaController() {

    }
    
    /*
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
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
    
    public void setReservaRESTClient(ReservaManager reserva){
        this.reservaManager=reserva;
    }
    
    public ReservaManager getReservaRESTClient(){
        return this.reservaManager;
    }
    
    /**
     * Inicia el escenario
     *
     * @param root, clase parent
     */
    public void initStage(Parent root) {
        LOG.log(Level.INFO, "Ventana Reserva");
        Scene scene = new Scene(root);
        tbReservas.setItems(FXCollections.observableArrayList(masterData));
        iniciarColumnasTabla();
        imagenBotones();
        
        stage.setScene(scene);
        stage.setTitle("Reserva");
        stage.setResizable(false);
        stage.setOnCloseRequest(this::handleWindowClose);
        stage.setOnShowing(this::handleWindowShowing);
        
        btnInsertar.setOnAction(this::btnAltaReserva);
        btnBorrar.setOnAction(this::borrarReserva);
        tfBuscar.textProperty().addListener(this::buscar);
        tbReservas.setEditable(true);
        
        
        
        

        stage.show();
    }
    

    /**
     * Al cerrar la ventana, saldrá un mensaje de confirmacion
     *
     * @param event, WindowEvent
     */
    private void handleWindowClose(WindowEvent event) {
        LOG.log(Level.INFO, "Beginning ReservaController::handleWindowClose");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Cliente");
        alert.setContentText("¿Estas seguro de cerrar la ventana?");
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
        LOG.log(Level.INFO, "Beginning ReservController::handleWindowShowing");
        btnInsertar.setDisable(true);
        btnBorrar.setDisable(true);
        
    }
    
    /**
     * Inicializa la tabla de reserva
     */
    private void iniciarColumnasTabla() {
        seleccionarReserva();
        //convertimos la tabla editable.
        tbReservas.setEditable(true);
        datosTabla();
        datosProducto();
        datosCliente();
        //Nombre del cliente
        tcCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        tcCliente.setCellFactory(ChoiceBoxTableCell.
                    forTableColumn(masterClientes));
        tcCliente.addEventHandler(TableColumn.<Reserva, Cliente>editCommitEvent(),
                    event -> actualizarCliente(event));
        //Modelo del producto
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        tcProducto.setCellFactory(ChoiceBoxTableCell.
                    forTableColumn(masterProductos));
        tcProducto.addEventHandler(TableColumn.<Reserva, Producto>editCommitEvent(),
                    event -> actualizarProducto(event));
        //cantidad de la reserva
        tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad")); 
        tcCantidad.setCellFactory(TextFieldTableCell.<Reserva, Integer>forTableColumn(new IntegerStringConverter()));
        tcCantidad.setOnEditCommit((TableColumn.CellEditEvent<Reserva, Integer> data) -> {
            //Establecemos que el dato que se introduzca en la celda debe cumplir un patrón
                try {
                    LOG.log(Level.INFO, "Nueva cantidad: {0}", data.getNewValue());
                    LOG.log(Level.INFO, "Antigua cantidad: {0}", data.getOldValue());
                    //Implementacion del ProveedorRESTClient
                    reservaManager = (ReservaImplementation) new factory.ReservaFactory().getReservaImplementation();
                    //Devuelve el dato de la fila
                    Reserva res = data.getRowValue();
                    //Añadimos el nuevo valor a la fila
                    res.setCantidad(data.getNewValue());
                    //Llamamos al método edit para asi poder modificar el nombre del proveedor
                    reservaManager.edit(res);
                    //Mostramos los datos actualizados en la TableView
                    datosTabla();
                } catch (ClientErrorException ex) {
                    LOG.log(Level.SEVERE, "ClientErrorException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Reserva");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                }catch(NumberFormatException ex){
                    LOG.log(Level.SEVERE, "NumberFormatException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Reserva");
                    alert.setHeaderText("No has metido un numero en la cantidad.");
                    alert.showAndWait();
                }
        });
        
        //Descripcion de la reserva, esta es editable.
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));    
        tcDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        tcDescripcion.setOnEditCommit((TableColumn.CellEditEvent<Reserva, String> data) -> {
            //Establecemos que el dato que se introduzca en la celda debe cumplir un patrón
            if (!Pattern.matches("^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ\\s]+$", data.getNewValue())) {
                //En el caso de que no se cumpla el patrón. Saldrá un alerta informandonos del error
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Reserva");
                alert.setHeaderText("Error al introducir la descripcion de la reserva");
                alert.setContentText("Introduzca un caracter válido");

                alert.showAndWait();
                tbReservas.refresh();
            } else {
                try {
                    LOG.log(Level.INFO, "Nueva Descripcion: {0}", data.getNewValue());
                    LOG.log(Level.INFO, "Antigua Descripcion: {0}", data.getOldValue());
                    //Implementacion del ProveedorRESTClient
                    reservaManager = (ReservaImplementation) new factory.ReservaFactory().getReservaImplementation();
                    //Devuelve el dato de la fila
                    Reserva res = data.getRowValue();
                    //Añadimos el nuevo valor a la fila
                    res.setDescripcion(data.getNewValue());
                    //Llamamos al método edit para asi poder modificar el nombre del proveedor
                    reservaManager.edit(res);
                    //Mostramos los datos actualizados en la TableView
                    datosTabla();
                } catch (ClientErrorException ex) {
                    LOG.log(Level.SEVERE, "ClientErrorException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Reserva");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                }
            }
        });
        //Estado de la reserva, esta es editable. AÑADIR UN LISTNER A CADA ESTADO CUANDO PONESMOS EL OBVSERVAVLE ..
        tcEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            tcEstado.setCellFactory(ChoiceBoxTableCell.
                    forTableColumn(EstadoReserva.CANCELADA, EstadoReserva.CONFIRMADA, EstadoReserva.EXPIRADA, EstadoReserva.REALIZADA));
            tcEstado.addEventHandler(TableColumn.<Reserva, EstadoReserva>editCommitEvent(),
                    event -> {
            try {
                actualizarEstado(event);
            } catch (ProductoExistenteException ex) {
                Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //Fecha de reserva
        tcReserva.setCellValueFactory(new PropertyValueFactory<>("fechaReserva"));    
        //Fecha de entrega de la reserva.
        tcEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));
        
        tcEntrega.setCellFactory(
                new Callback<TableColumn<Reserva, Date>, TableCell<Reserva, Date>>() {
            //Este método indica que la TableCell incluye un DatePicker
            @Override
            public TableCell<Reserva, Date> call(TableColumn<Reserva, Date> arg0
            ) {
                return new FechaEntregaCell();
            }
        }
        );
        tcEntrega.setOnEditCommit(data
                -> {
            try {
                LOG.log(Level.INFO, "Nueva FechaEntrega: {0}", data.getNewValue());
                LOG.log(Level.INFO, "Antigua FechaEntrega: {0}", data.getOldValue());
                //Implementación del ReservaRESTClient
                reservaManager = (ReservaImplementation) new factory.ReservaFactory().getReservaImplementation();
                //Devuelve el dato de la fila
                Reserva fechanueva = data.getRowValue();
                //Añadimos el nuevo valor a la fila
                fechanueva.setFechaEntrega(data.getNewValue());
                //Llamamos al método edit para asi poder modificar la fecha de entrega de la reserva
                reservaManager.edit(fechanueva);
                //Mostramos los datos actualizados en la TableView
                datosTabla();
            } catch (ClientErrorException ex) {
                LOG.log(Level.SEVERE, "ClientErrorException");
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Reserva");
                alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                alert.showAndWait();
            }
        });
    }
    
   //CONFIGURACIÓN DE LOS DATOS 
    /**
     * Muestra los datos de la base de datos en la TableView
     */
    private void datosTabla() {
        try {
            //Implementación del ReservaRESTClient
            reservaManager = (ReservaImplementation) new factory.ReservaFactory().getReservaImplementation();
            //Llamamos al método findReservas para asi poder llenar la tabla con las reservas que hay dentro de la base de datos
            masterData = FXCollections.observableArrayList(reservaManager.findReservas());
            //Establecemos los datos del ObservableList dentro de la TableView
            tbReservas.setItems(masterData);
            //Recorremos el ArrayList Observable de Reservas.
            for (Reserva res : masterData) {
                LOG.log(Level.INFO, "Lista de Reservas: {0}", masterData);
            }
            //Añadimos esas reservas dentro de la TableView
            //tbReservas.setItems(masterData);
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Reserva");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        
    }
    }
    /**
     * Muestra los datos de la base de datos en la TableView
     */
    private void datosCliente() {
        try {
            //Implementación del ReservaRESTClient
            clienteManager = (ClienteImplementation) new factory.ClienteFactory().getClienteImplementation();
            //Llamamos al método findReservas para asi poder llenar la tabla con las reservas que hay dentro de la base de datos
            masterClientes = FXCollections.observableArrayList(clienteManager.findCliente());
            //Añadimos esas reservas dentro de la TableView
            //tbReservas.setItems(masterData);
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Cliente");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        
    }
    }
    
    /**
     * Muestra los datos de la base de datos en la TableView
     */
    private void datosProducto() {
        try {
            //Implementación del ReservaRESTClient
            productoManager = (ProductoManagerImplementation) new factory.ProductoFactory().getProductoManagerImplementation();
            //Llamamos al método findReservas para asi poder llenar la tabla con las reservas que hay dentro de la base de datos
            masterProductos = FXCollections.observableArrayList(productoManager.findAllProductosAsc());
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Producto");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        
    }
    }
    
    /**
     * Nos permite seleccionar a un proveedor de la tabla y este controla que el
     * botón BorrarProveedor y ActualizarProveedor esté habilitado o
     * deshabilitado
     */
    private void seleccionarReserva() {
        tbReservas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (reserva != null) {
                        btnInsertar.setDisable(true);
                        btnBorrar.setDisable(true);
                    } else {
                        btnInsertar.setDisable(false);
                        btnBorrar.setDisable(false);
                    }
                });
    }
    
     /**
     * Borra la reserva seleccionado de la tabla
     *
     * @param event
     */
    private void borrarReserva(ActionEvent event) {
        //Mensaje de confirmación para el borrado
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Borrado de Reserva");
        alert.setContentText("¿Estas seguro de borrar esta reserva?");
        Optional<ButtonType> respuesta = alert.showAndWait();
        //En el caso de pulsar el boton Aceptar, borraremos la reserva seleccionado
        if (respuesta.get() == ButtonType.OK) {
            LOG.log(Level.INFO, "Has pulsado el boton Aceptar");
                try {
                    Reserva reserva = tbReservas.getSelectionModel().getSelectedItem();
                    tbReservas.getItems().remove(reserva);
                    reserva.setCliente(null);
                    reserva.setProducto(null);
                    reservaManager.edit(reserva);
                    reservaManager.remove(reserva.getId().toString());
                    //Mostramos los datos actualizados en la tabla
                    datosTabla();
                    LOG.log(Level.INFO, "Se ha borrado la reserva");
                } catch (ClientErrorException ex) {
                    LOG.log(Level.SEVERE, "ClientErrorException");
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Reserva");
                    alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
                    alert.showAndWait();
                }  
        } else {
            LOG.log(Level.INFO, "Has pulsado el boton Cancelar");
            //Desaparece el alerta
            event.consume();
        }
    }
    
    /**
     * Busca la reserva  con el id
     *
     * @param event
     */
    private void buscar(ObservableValue observable, String oldValue, String newValue) {

        
        FilteredList<Reserva> filteredData = new FilteredList<>(masterData, p -> true);

			filteredData.setPredicate(person -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
                                Long comparar =  Long.parseLong(lowerCaseFilter);
				if (person.getId() == (comparar)) {
                                   return true;
				} else {
				return false; // Does not match.
                                }

			});

        SortedList<Reserva> sortedData = new SortedList<>(filteredData);
		
	
	sortedData.comparatorProperty().bind(tbReservas.comparatorProperty());
		
	
	tbReservas.setItems(sortedData);
    }
    
    /**
     * Nos dara la opcion de introducir una nueva reserva.
     *
     * @param event ActionEvent
     */
    private void btnAltaReserva(ActionEvent event) {
        try {
            LocalDate fechaHoy = LocalDate.now();
            LocalDate fechaMas = fechaHoy.plusDays(10); 
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date date = Date.from(fechaHoy.atStartOfDay(defaultZoneId).toInstant());
            Date dateMas = Date.from(fechaMas.atStartOfDay(defaultZoneId).toInstant());
            //Instanciamos una nueva reserva dandole valores por defecto
            Reserva nuevaReserva = new Reserva();
            //Añadimos por defecto que el Cliente va a ser null
            nuevaReserva.setCliente(null);
            //Añadimos por defecto que el Producto va a ser null
            nuevaReserva.setProducto(null);
            //Añadimos por defecto que  el cantidad está 0
            nuevaReserva.setCantidad(0);
            //Añadimos por defecto que la descripcion está vacia
            nuevaReserva.setDescripcion("");
            //Añadimos por defecto que el estado está confirmado
            nuevaReserva.setEstado(EstadoReserva.CONFIRMADA);
            //Añadimos por defecto que hoy sea la fecha reserva
            nuevaReserva.setFechaReserva(date);
            //Añadimos por defecto que la fecha reserva + 10 dias sea la fecha entrega.
            nuevaReserva.setFechaEntrega(dateMas);
            //Implementación del ReservaImplementation
            reservaManager = (ReservaImplementation) new factory.ReservaFactory().getReservaImplementation();
            //Llamamos al método create para crear una nueva reserva
            reservaManager.create(nuevaReserva);
            //Añadimos una nueva reserva dentro del listareserva ( masterData )
            
            datosTabla();
            

        } catch (ClientErrorException ex) {
            Logger.getLogger(ReservaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    /**
     * Nos permite actualizar/cambiar el tipo de producto dentro de la tabla
     * editable
     *
     * @param event
     */
    private void actualizarEstado(TableColumn.CellEditEvent<Reserva, EstadoReserva> data) throws ProductoExistenteException {
        try {
            LocalDate fechaHoy = LocalDate.now();
            LocalDate fechaMas = fechaHoy.plusDays(10); 
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date dateMas = Date.from(fechaMas.atStartOfDay(defaultZoneId).toInstant());
            LOG.log(Level.INFO, "Nuevo Estado: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Estado: {0}", data.getOldValue());
            //Implementación del ReservaRESTClient
            reservaManager = (ReservaImplementation) new factory.ReservaFactory().getReservaImplementation();
            productoManager = (ProductoManagerImplementation) new factory.ProductoFactory().getProductoManagerImplementation();
            //Devuelve el dato de la fila
            Reserva res = data.getRowValue();
            //Añadimos el nuevo valor a la fila
            res.setEstado(data.getNewValue());
            //Buscamos el producto de la reserva 
            producto = productoManager.find(producto, res.getProducto().getId().toString());
            //Dependiendo del estado de la reserva se le sumara o restara stock a los productos.
            if(data.getNewValue().equals(EstadoReserva.CONFIRMADA)){
                stock = producto.getStock() - res.getCantidad();
                producto.setStock(stock);
                res.setFechaEntrega(dateMas);
            }if(data.getNewValue().equals(EstadoReserva.EXPIRADA)){
                stock = res.getCantidad() + producto.getStock();
                producto.setStock(stock);
                res.setFechaEntrega(dateMas);
            }if(data.getNewValue().equals(EstadoReserva.CANCELADA)){
                stock = res.getCantidad() + producto.getStock();
                producto.setStock(stock);
                res.setFechaEntrega(null);
            }if(data.getNewValue().equals(EstadoReserva.REALIZADA)){
                res.setFechaEntrega(dateMas);
            }
            
            //Llamamos al método edit para asi poder modificar el estado de la Reserva
            reservaManager.edit(res);
            //Llamamos al método edit para asi poder modificar el stock del producto.
            productoManager.edit(producto);
            //Mostramos los datos actualizados en la TableView
            datosTabla();
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Reserva");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }
    }

   private void actualizarProducto(TableColumn.CellEditEvent<Reserva, Producto> data) {
        try {
            LOG.log(Level.INFO, "Nuevo Producto: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo Producto: {0}", data.getOldValue());
            //Implementación del ReservaRESTClient
            productoManager = (ProductoManagerImplementation) new factory.ProductoFactory().getProductoManagerImplementation();
            //Devuelve el dato de la fila
            Reserva res = data.getRowValue();
            //Añadimos el nuevo valor a la fila
            res.setProducto(data.getNewValue());
            //Llamamos al método edit para asi poder modificar el estado de la Reserva
            reservaManager.edit(res);
            //Mostramos los datos actualizados en la TableView
            datosTabla();
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Producto de reserva");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }
    }
   
   private void actualizarCliente(TableColumn.CellEditEvent<Reserva, Cliente> data) {
        try {
            LOG.log(Level.INFO, "Nuevo cliente: {0}", data.getNewValue());
            LOG.log(Level.INFO, "Antiguo cliente: {0}", data.getOldValue());
            //Implementación del ReservaRESTClient
            clienteManager = (ClienteImplementation) new factory.ClienteFactory().getClienteImplementation();
            //Devuelve el dato de la fila
            Reserva res = data.getRowValue();
            //Añadimos el nuevo valor a la fila
            res.setCliente(data.getNewValue());
            //Llamamos al método edit para asi poder modificar el estado de la Reserva
            reservaManager.edit(res);
            //Mostramos los datos actualizados en la TableView
            datosTabla();
        } catch (ClientErrorException ex) {
            LOG.log(Level.SEVERE, "ClientErrorException");
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Cliente de reserva");
            alert.setHeaderText("Imposible conectar. Inténtelo más tarde");
            alert.showAndWait();
        }
    }
    
    /**
     * MenuItem que nos redirige hacia la ventana de LogIn y cierra la ventana
     * actual
     *
     * @param event
     */
    @FXML
    private void configMenuSalir(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Cliente");
        alert.setContentText("¿Estas seguro de cerrar la ventana?");
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
     * MenuItem que muestra un alerta informandonos de la conexión actual del
     * usuario
     *
     * @param event
     */
    @FXML
    private void configMenuSobreNosotros(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Sobre nosotros");
        alert.setHeaderText("Somos una tienda que se dedica a vender zapatillas y ropa de deporte, pero como estamos en unas condiciones por la pandemia mundial, \n" +
        "hemos decidido actuar de la siguiente manera; reservar desde la aplicacion del telefono movil, ir a la tienda \n" +
        "respetando todas las medidas de seguridad y recoger la reserva hecha desde el movil.");
        alert.showAndWait();
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
        btnInsertar.setGraphic(new ImageView(imageAlta));
        btnBorrar.setGraphic(new ImageView(imageBorrar));

    }
    
}