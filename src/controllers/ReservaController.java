/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import factory.ReservaFactory;
import modelo.Cliente;
import modelo.EstadoReserva;
import modelo.Producto;
import modelo.Reserva;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.core.GenericType;
import manager.ReservaManager;
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
    private TableColumn<Reserva, Cliente> tcIdCliente;
    @FXML
    private TableColumn<Reserva, Producto> tcIdProducto;
    @FXML
    private TableColumn<Reserva,Integer> tcCantidad;
    @FXML
    private TableColumn<Reserva, String> tcDescripcion;
    @FXML
    private TableColumn<Reserva, EstadoReserva> tcEstado;
    @FXML
    private TableColumn<Reserva, Date> tcFecha;
    @FXML
    private TableColumn<Reserva, Timestamp> tcReserva;
    @FXML
    private TableColumn<Reserva, Timestamp> tcEntrega;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnInsertar;
    @FXML
    private Button btnBorrar;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField tfBuscar;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuCliente;
    @FXML
    private MenuItem menuPerfil;
    @FXML
    private MenuItem menuSalir;
    @FXML
    private Menu menuAyuda;
    @FXML
    private MenuItem menuAbout;

    private Stage stage = new Stage();
    private List<Reserva> reservas;
    private ObservableList<Reserva> masterData = FXCollections.observableArrayList();
    private Reserva reserva;
    private ReservaManager reservaManager;
    private Usuario usuario;
    
    public ReservaController() {

    }
    
    /**
     * Establece un Usuario
     *
     * @param usuario Usuario
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        getReservas();
        tbReservas.setItems(FXCollections.observableArrayList(masterData));
        iniciarColumnasTabla();
        
        stage.setScene(scene);
        stage.setTitle("Reserva");
        stage.setResizable(false);
        stage.setOnCloseRequest(this::handleWindowClose);
        stage.setOnShowing(this::handleWindowShowing);
        
        tfBuscar.textProperty().addListener(this::txtChanged);
        btnInsertar.setOnAction(this::btnAltaReserva);
        btnBorrar.setOnAction(this::borrarReserva);
        btnBuscar.setOnAction(this::buscar);
        
        tbReservas.setEditable(true);
        
        
        
        

        stage.show();
    }
    

    /**
     * Al cerrar la ventana, saldrá un mensaje de confirmacion
     *
     * @param event, WindowEvent
     */
    private void handleWindowClose(WindowEvent event) {
        LOG.log(Level.INFO, "Beginning InicioReservaController::handleWindowClose");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Cliente");
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
        btnInsertar.setDisable(true);
        btnBorrar.setDisable(true);
        btnBuscar.setDisable(true);
    }
    private void txtChanged(ObservableValue observable, String oldValue, String newValue) {
        if(!tfBuscar.getText().trim().equals("")){
            btnBuscar.setDisable(false);
        }else{
            btnBuscar.setDisable(true);
        }
    }
    
    /**
     * Inicializa la tabla de reserva
     */
    private void iniciarColumnasTabla() {
        seleccionarReserva();
        //convertimos la tabla editable.
        tbReservas.setEditable(true);

        //Id del cliente
        tcIdCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        //id del producto
        tcIdProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        //cantidad de la reserva
        tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad")); 
        //Descripcion de la reserva, esta es editable.
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));    
        tcDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
        tcDescripcion.setOnEditCommit((TableColumn.CellEditEvent<Reserva, String> des) -> {
            LOG.log(Level.INFO, "Nuevo Email: {0}", des.getNewValue());
            LOG.log(Level.INFO, "Antiguo Email: {0}", des.getOldValue());
            //Devuelve el dato de la celda
            Reserva res = des.getRowValue();
            //Añadimos el nuevo valor a la celda
            res.setDescripcion(des.getNewValue());
        });
        //Estado de la reserva, esta es editable.
        tcEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            tcEstado.setCellFactory(ChoiceBoxTableCell.
                    forTableColumn(EstadoReserva.CANCELADA, EstadoReserva.CONFIRMADA, EstadoReserva.EXPIRADA, EstadoReserva.REALIZADA));
            tcEstado.addEventHandler(TableColumn.<Reserva, EstadoReserva>editCommitEvent(),
                    event -> actualizarReservaEstado(event));
        //Fecha de reserva
        tcReserva.setCellValueFactory(new PropertyValueFactory<>("fechaReserva"));    
        //Fecha de entrega de la reserva.
        tcEntrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntrega"));  
        
    }
    
    /**
     * Recoge los datos de Reserva.
     */
    private void getReservas() {
        this.masterData= FXCollections.observableArrayList(getReservaRESTClient().findReservas(new GenericType<List<Reserva>>(){}));
    }
    

    /*
    *Usamos este metodo para actualizar el Estado de la reserva.
    */
    private void actualizarReservaEstado(TableColumn.CellEditEvent<Reserva, EstadoReserva> event) {
        System.out.println("Estoy aca la reserva es " + event);
        System.out.println((EstadoReserva) event.getNewValue());
        System.out.println((EstadoReserva) event.getOldValue());
        reserva = event.getRowValue();
        EstadoReserva estado = event.getNewValue();
        System.out.println("Estado: " + estado.toString() + reserva.getId() + reserva.getDescripcion() + "Esto es de reserva: " + reserva.getEstado().toString());
        reserva.setEstado(event.getNewValue());
        System.out.println(reserva.getId() + reserva.getDescripcion() + "Esto es de reserva: " + reserva.getEstado().toString()+" fecha de entrega es "+reserva.getFechaEntrega());
        
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
        //Capturamos el indice de la reserva seleccionado y borro su item asociado de la tabla
        int ReservaSeleccionada = tbReservas.getSelectionModel().getSelectedIndex();
        if (ReservaSeleccionada >= 0) {
            //Borramos el proveedor
            LOG.log(Level.INFO, "Se ha borrado una reserva");
            tbReservas.getItems().remove(ReservaSeleccionada);
         //   String t = String.valueOf(ReservaSeleccionada);
      //      this.masterData= FXCollections.observableArrayList(getReservaRESTClient().Eliminar(responseType, t));
        } else {
            //En el caso de no seleccionar una reserva. Saldrá un alerta
            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Atención");
            alerta.setHeaderText("Reserva no seleccionado");
            alerta.setContentText("Por favor, selecciona una Reserva de la tabla");
            alerta.showAndWait();

        }
    }
    
    /**
     * Borra la reserva seleccionado de la tabla
     *
     * @param event
     */
    private void buscar(ActionEvent event) {

        
        FilteredList<Reserva> filteredData = new FilteredList<>(masterData, p -> true);
        tfBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (person.getProducto().equals(lowerCaseFilter)) {
                                   return true;
				} else {
				return false; // Does not match.
                                }

			});
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
        TablePosition pos = tbReservas.getFocusModel().getFocusedCell();
        tbReservas.getSelectionModel().clearSelection();

        Reserva nuevaReserva = new Reserva();
        tbReservas.getItems().add(nuevaReserva);
        
        int row = tbReservas.getItems().size() - 1;
        tbReservas.getSelectionModel().select(row, pos.getTableColumn());
        tbReservas.scrollTo(nuevaReserva);
    }
     
    /**
     * MenuItem que muestra un alerta informandonos de la conexión actual del
     * usuario
     *
     * @param event
     */
    @FXML
    private void configMenuCliente(ActionEvent event) {
        Usuario usuario = new Usuario();
        usuario.setLogin("Nadirtxu");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Información del Cliente");
        alert.setHeaderText("Usuario: "+usuario.getLogin());

        SimpleDateFormat sdf = new SimpleDateFormat("hh: mm dd-MMM-aaaa");
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
        LOG.log(Level.INFO, "Closing application.");
        //Closes application.
        Platform.exit();
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

 
    
}
