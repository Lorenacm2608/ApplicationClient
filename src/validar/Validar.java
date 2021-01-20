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
     * Valida la estructura correcta del email
     *
     * @param txtEmail
     * @return b, lb, errorMessage o noError.
     */
    public static boolean isValidEmail(TextField txtEmail) {
        boolean b = true;
        if (!IsValidEmail(txtEmail)) {
            b = false;
            txtEmail.setStyle("-fx-border-color:red; -fx-border-width:2px;");
        } else {
            txtEmail.setStyle("-fx-border-color:rgb(189 189 189);");
        }
        return b;
    }

    /**
     * Valida si las dos contraseña son iguales.
     *
     * @param txtContrasena Contraseña recibido
     * @param txtConfirmarContrasena Contraseña recibido
     * @return b
     */
    public static boolean isValidContrasena(PasswordField txtContrasena, PasswordField txtConfirmarContrasena) {
        boolean b = true;
        if (!(txtContrasena.getText().equals(txtContrasena.getText()))) {
            b = false;
            txtContrasena.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            txtConfirmarContrasena.setStyle("-fx-border-color:red; -fx-border-width:2px;");
        } else {
            txtContrasena.setStyle("-fx-border-color:rgb(189 189 189);");
            txtConfirmarContrasena.setStyle("-fx-border-color:rgb(189 189 189);");
        }
        return b;
    }

    /**
     * Limita el numero de caracteres introducidos
     *
     * @param tf Texto recibido
     * @param treinta Tamaño maximo
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
     * @param txtUsuario Texto recibido
     * @return b true correcto, false incorrecto
     */
    public static boolean isValidUsuario(TextField txtUsuario) {
        boolean b = false;
        String pattern = "^[a-zA-Z0-9]+$";
        if (txtUsuario.getText().matches(pattern)) {
            b = true;
            txtUsuario.setStyle("-fx-border-color:rgb(189 189 189);");
        } else {
            txtUsuario.setStyle("-fx-border-color:red; -fx-border-width:2px;");
        }
        return b;
    }

    /**
     * Validar que el texto es alfanumerico
     *
     * @param txtNombre Texto recibido
     * @return b true correcto, false incorrecto
     */
    public static boolean isValidNombre(TextField txtNombre) {
        boolean b = false;
        String pattern = "^[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ]+$";
        if (txtNombre.getText().matches(pattern)) {
            b = true;
            txtNombre.setStyle("-fx-border-color:rgb(189 189 189);");
        } else {
            txtNombre.setStyle("-fx-border-color:red; -fx-border-width:2px;");
        }
        return b;
    }

    /**
     * Validar el texto de la contraseña este correcto
     *
     * @param txtNombre Texto recibido
     * @return b true correcto, false incorrecto
     */
    public static boolean isValidPatternContrasena(PasswordField txtContrasena) {
        boolean b = false;
        String pattern ="^[a-zA-Z0-9*@.,_-]+$";
        if (txtContrasena.getText().matches(pattern)) {
            b = true;
            txtContrasena.setStyle("-fx-border-color:rgb(189 189 189);");
        } else {
            txtContrasena.setStyle("-fx-border-color:red; -fx-border-width:2px;");
        }
        return b;
    }
    
    public static boolean isValidDNI(String dni){
        String LetraMayuscula;
        if(dni.length() != 9 || Character.isLetter(dni.charAt(8)) == false){
            return false;
        }
            LetraMayuscula = (dni.substring(8)).toUpperCase();
            if(soloNumeros(dni) == true && letraDNI(dni).equals(LetraMayuscula)){
                return true;
            }else{
                return false;
            }
    }
    
    private static boolean soloNumeros(String dni){
        int i,j;
        String numero="";
        String miDNI="";
        String[] unoNueve= {"0","1","2","3","4","5","6","7","8","9"};
                for(i=0;i < dni.length() - 1;i++){
                    numero=dni.substring(i,i+1);
                    for(j=0;j < unoNueve.length;j++){
                        if(numero.equals(unoNueve[j])){
                        miDNI += unoNueve[j];
                    }
                    }
                }
                if(miDNI.length() != 8){
                    return false;
                } else{
                    return true;
                }
    }
    
    private static String letraDNI(String dni){
        int miDNI= Integer.parseInt(dni.substring(0,8));
        int resto=0;
        String miLetra="";
        String[] asignacionLetra= {"T","R","W","A","G","M","Y","F","P","D","X","B","N","J","Z","S","Q","V","H","L","C","K","E"};
        resto=miDNI%23;
        miLetra=asignacionLetra[resto];
        return miLetra;
    }
    }
