/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

/**
 *
 * @author Lorena Cáceres Manuel
 */
public class FechaAltaCell extends TableCell<Proveedor, Date> {

    private static final Logger LOG = Logger.getLogger(FechaAltaCell.class.getName());

    private DatePicker fecha;

    /**
     * Nos permite elegir la fecha con la que inicializamos el DatePicker
     */
    public FechaAltaCell() {

    }

    /**
     * Pasar de un estado sin edición a un estado de edición
     */
    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(fecha);
        }
    }

    /**
     * Pasa de un estado de edición a un estado sin edición, sin guardar ningun
     * cambio
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getDate().toString());
        setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        //Representa si la celda está vacía o no.
        if (empty) {
            setText(null);
            setGraphic(null);
            LOG.log(Level.INFO, "Celda cargada pero no abierta");
        } else {
            //Comprueba si la celda se encuentra en su estado de edición o no.
            if (isEditing()) {
                if (fecha != null) {
                    //Establece el valor de la fecha
                    fecha.setValue(getDate());
                }
                setText(null);
                setGraphic(fecha);
            } else {
                LOG.log(Level.INFO, "Preparada para ser editada");
                setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                setGraphic(null);
            }

        }
      

    }

    /**
     * Inicializamos el DatePicker
     */
    private void createDatePicker() {
        LOG.log(Level.INFO, "Creamos el DatePicker");
        fecha = new DatePicker(getDate());
        //Establecemos el ancho del DatePicker
        fecha.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        /*Establecemos el ACTION que realiza, en este caso, devuelve una fecha
        y hora dependiendo de la zona horaria*/
        fecha.setOnAction((value) -> {
            commitEdit(Date.from(fecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        });
    }

    /**
     * Metodo que nos devuelve la fecha Actual
     *
     */
    private LocalDate getDate() {
        if (getItem() == null) {
            LOG.log(Level.INFO, "Devuelve la fecha actual");
            return LocalDate.now();
        } else {
            LOG.log(Level.INFO, "Fecha que recibe");
            return getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

}
