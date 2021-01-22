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
 *
 * @author Fredy
 */
public class DisponibilidadCell extends TableCell<Producto, Date> {

    //se crea un objeto DatePicker
    private DatePicker fecha;

    public DisponibilidadCell() {

    }

    /**
     *
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
     *
     */

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getDate().toString());
        setGraphic(null);
    }

    /**
     *
     * @param item
     * @param empty
     */

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        //   fecha.setDisable(empty || item.toLocalDateTime().toLocalDate().isBefore(LocalDate.now()));
        //    fecha.setEditable(false);
        if (empty) {
            setText(null);
            setGraphic(null);
            // System.out.println("Estoy Aqui 1");

        } else {
            if (isEditing()) {
                if (fecha != null) {
                    //      System.out.println("Estoy Aqui 2");

                    fecha.setValue(getDate());
                }
                setText(null);
                setGraphic(fecha);
            } else {

                //   System.out.println("Estoy Aqui 4");
                /*   if (LocalDate.now().isAfter(getDate())) {
                        fecha.setDisable(true);
                        System.out.println("Estoy Aqui 3");
                    }*/
                // fecha.setDisable(false);
                setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                setGraphic(null);
            }

        }

    }

    /**
     *
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
            //   System.out.println("Son iguales");
            return LocalDate.now();
        } else {
            //  System.out.println("Son no lo son");
            return getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

}
