/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validar;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author Nadir Essadi
 */
public class Validar {

    /**
     * Compara el texto con la estrucutura valida de un email.
     *
     * @param tf Texto recibido
     * @return b True correcto, falso incorrecto
     */
    public static boolean IsValidEmail(TextField tf) {
        boolean b = false;
        String pattern = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        if (tf.getText().matches(pattern)) {
            b = true;
        }
        return b;
    }

    /**
     * Valida correcta la estructura
     *
     * @param tf Texto recibido
     * @return b, lb, errorMessage o noError.
     */
    public static boolean isValidEmail(TextField tf) {
        boolean b = true;
        if (!IsValidEmail(tf)) {
            tf.setStyle("-fx-text-inner-color: red;");
        } else {

        }

        return b;
    }

    /**
     * Valida si las dos contraseña son iguales.
     *
     * @param tf Contraseña recibido
     * @return b, lb, errorMessage o noError
     */
    public static boolean isValidContrasena(PasswordField tf, PasswordField tff) {
        boolean b = true;
        if (!(tf.getText().equals(tff.getText()))) {
            b = false;
            tf.setStyle("-fx-text-inner-color: red;");
        } else {

        }
        return b;
    }

    /**
     * Limita el numero de caracteres introducidos
     *
     * @param tf Texto recibido
     * @param treinta limite
     */
    public static void addTextLimiter(TextField tf, int treinta) {

        if (tf.getText().length() > treinta) {
            String s = tf.getText().substring(0, treinta);
            tf.setText(s);
        }
    }

    /**
     * Limita el numero de caracteres introducidos
     *
     * @param tf Contraseña recibido
     * @param treinta Tamañano maximo
     */
    public static void addTextLimiterPass(PasswordField tf, int treinta) {

        if (tf.getText().length() > treinta) {
            String s = tf.getText().substring(0, treinta);
            tf.setText(s);
        }
    }

    /**
     * Limita el numero de caracteres introducidos
     *
     * @param tf Texto recibido
     * @param cincuenta Tamaño maximo
     */
    public static void addTextLimiterGrande(TextField tf, int cincuenta) {

        if (tf.getText().length() > cincuenta) {
            String s = tf.getText().substring(0, cincuenta);
            tf.setText(s);
        }
    }

    /**
     * Validar que el texto es alfanumerico
     *
     * @param tf Texto recibido
     * @return b true correcto, false incorrecto
     */
    public static boolean IsValid(TextField tf) {
        boolean b = false;
        String pattern = "^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ]+$";
        if (tf.getText().matches(pattern)) {
            b = true;
        }
        return b;
    }

    /**
     * Validar la estructura
     *
     * @param tf Texto recibido
     * @return tf, lb, errorMssage, noError
     */
    public static boolean isValid(TextField tf) {
        boolean b = true;
        if (!IsValid(tf)) {
            b = false;
            tf.setStyle("-fx-text-inner-color: red;");
        } else {
        }
        return b;
    }
}
