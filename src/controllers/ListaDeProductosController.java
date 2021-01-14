/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import modelo.Producto;
import modelo.Usuario;

/**
 * FXML Controller class
 *
 * @author Lorena
 */
public class ListaDeProductosController {

    private static final Logger LOG = Logger.getLogger(ListaDeProductosController.class.getName());

    private Stage stage;

    @FXML
    private AnchorPane apInicioProductos;
    @FXML
    private BorderPane bpInicioProductos;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuPerfil;
    @FXML
    private MenuItem menuModificarCliente;
    @FXML
    private MenuItem menuSalir;
    @FXML
    private Menu menuReservas;
    @FXML
    private MenuItem menuVerReservas;
    @FXML
    private TextField tfBuscarProducto;
    @FXML
    private Button btnBuscar;
    @FXML
    private ScrollPane spProductos;
    @FXML
    private GridPane gpProductos;

    private List<Producto> productos = new ArrayList<>();

    private Usuario usuario;

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

    private List<Producto> getProductos() throws IOException {
        List<Producto> productos = new ArrayList<>();
        Producto producto;
        String dirName = "C:\\Users\\loren\\Desktop\\Reto2\\PruebaVentanasFX\\src\\prueba";
        for (int i = 0; i < 20; i++) {

            producto = new Producto();
            producto.setPrecio(179.99f);
            producto.setModelo("Nike Air Max 97");
            producto.setImagen(extractBytes("C:\\Users\\loren\\Desktop\\Reto2\\PruebaVentanasFX\\src\\prueba"));
            productos.add(producto);
        }
        return productos;
    }

    /**
     * Método para extraer los bytes de una imagen y poder mostrarla
     *
     * @param path
     * @return
     * @throws IOException
     */
    public byte[] extractBytes(String path) throws IOException {
        // abrimos la imagen
        File imgPath = new File(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
        BufferedImage img = ImageIO.read(new File(path, "97.jpg"));
        ImageIO.write(img, "jpg", baos);
        baos.flush();
        //  String base64String = Base64.encode(baos.toByteArray());
        baos.close();

        return (baos.toByteArray());
    }

    /**
     * Inicia el escenario
     *
     * @param root, clase parent
     */
    public void initStage(Parent root) {
        LOG.log(Level.INFO, "Ventana Lista de Productos (Cliente)");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Lista de productos");
        stage.setResizable(false);

        stage.show();
        try {
            int columna = 0;
            int fila = 1;
            productos.addAll(getProductos());
            for (int i = 0; i < productos.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("ProductoItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ProductoItemController productoItemController = fxmlLoader.getController();
                productoItemController.setData(productos.get(i));
                if (columna == 3) {
                    columna = 0;
                    fila++;
                }
                gpProductos.add(anchorPane, columna++, fila);

                gpProductos.setMinWidth(Region.USE_COMPUTED_SIZE);
                gpProductos.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gpProductos.setMaxWidth(Region.USE_PREF_SIZE);

                gpProductos.setMinHeight(Region.USE_COMPUTED_SIZE);
                gpProductos.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gpProductos.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));

            }

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Se ha producido un error");
        }
    }

    /**
     * MenuItem que nos redirige hacia la ventana de Modificar usuario y cierra
     * la ventana actual
     *
     * @param event
     */
    @FXML
    private void configModificarUsuario(ActionEvent event) {
        LOG.log(Level.INFO, "Ventana Modificar Usuario");
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
     * MenuItem que nos redirige hacia la ventana de Reservas y cierra la
     * ventana actual
     *
     * @param event
     */
    @FXML
    private void configListaReservas(ActionEvent event) {

        LOG.log(Level.INFO, "Ventana Lista de Reservas");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Reserva.fxml"));

            Parent root = (Parent) loader.load();

            /*ReservaController controller = ((ReservaController) loader.getController());
            controller.initStage(root);*/
            stage.hide();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Se ha producido un error de E/S");
        }
    }

}
