/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;



/**
 *
 * @author Fredy
 */
public class ProductoCell extends TableCell<Producto, byte[]> {

    private final ImageView imageView = new ImageView();

    public ProductoCell() {
    }

    @Override
    protected void updateItem(byte[] item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            imageView.setImage(convertToJavaFXImage(item, 1, 1));
            setGraphic(imageView);
        }
        this.setItem(item);
    }

    private Image getImagenByte(byte[] imagenBytes) {
        try {
            byte[] bytearray = imagenBytes;
            BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytearray));

            Image i = SwingFXUtils.toFXImage(imag, null);
            return i;
        } catch (IOException ex) {
            //
        }
        return null;
    }

    private static Image convertToJavaFXImage(byte[] raw, final int width, final int height) {
        WritableImage image = new WritableImage(width, height);
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(raw);
            BufferedImage read = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(read, null);
        } catch (IOException ex) {

        }
        return image;
    }

}
