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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

/**
 * Esta clase se encargará de mostrarnos en un DatePicker la fecha recibida como
 * parámetro
 *
 * @author Fredy Vargas Flores
 */
public class DisponibilidadCell extends TableCell<Producto, Date> {

    //se crea un objeto DatePicker
    private DatePicker fecha;

    //Se crea un constructor vacio
    public DisponibilidadCell() {
    }

    /**
     * El método startEdit() inicia la edición y si la celda es nula inicializa
     * el DatePicker con el método createDatePicker()
     */
    @Override
    public void startEdit() {

        super.startEdit();
        if (fecha == null) {
            createDatePicker();
        }
        setText(null);
        setGraphic(fecha);

        /*
        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(fecha);
        }*/
    }

    /**
     * El método cancelEdit() llama al de la clase superior y maneja el
     * DatePicker
     *
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        //Se cancela la edicción pero mantenemos la fecha recibida por parámetro
        setText(getDate().toString());
        //Desactivamos la vista
        setGraphic(null);
    }

    /**
     * El método updateItem llama a la clase superior y actualiza la
     * visualización de la celda mostrando el DatePicker()
     *
     * @param item
     * @param empty
     */
    @Override
    public void updateItem(Date item, boolean empty) {
        //Llamamos al método super.updateItem(item, empty) para poder configurar correctamente las propiedades
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (isEditing()) {
                if (fecha != null) {
                    //Actualizamos la fecha recibida y la pondremos en el DatePicker
                    fecha.setValue(getDate());
                }
                setText(null);
                //La mostramos
                setGraphic(fecha);
            } else {
                //Establecemos el formato de la fecha 
                setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                //Desactivamos la vista
                setGraphic(null);
            }

        }

    }

    /**
     * Establecemos la fecha que hemos recibido como parámetro
     */
    private void createDatePicker() {
        fecha = new DatePicker(getDate());
        fecha.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        fecha.setOnAction((value) -> {
            commitEdit(Date.from(fecha.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        });
    }

    /**
     *
     * @return
     */
    private LocalDate getDate() {
        if (getItem() == null) {
            //Si la fecha es nula devuelve la fecha actual(LocalDate)
            return LocalDate.now();
        } else {
            //  System.out.println("Son no lo son");
            return getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

}
