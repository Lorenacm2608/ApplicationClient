/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Moroni Entidad Usuario relacionado con gestiona Proveedor
 */
@XmlRootElement
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
    *Id del usuario
     */
    private Long id_usuario;

    /*
    * Login del usuario
     */

    private String login;

    /*
    * Email del usuario
     */
    
    private String email;

    /*
    * Nombre completo del usuario
     */
    
    private String fullname;

    /*
    * Estado del usuario
     */
    
    private EstadoUsuario status;

    /*
    * Privilegio del usuario
     */
    
    private PrivilegioUsuario privilege;

    /*
    * Contraseña del usuario
     */
    
    private String password;

    /*
    * Ultimo acceso del usuario
     */
    
    private Date lastAccess;

    /*
    * Ultimo cambio de contraseña del usuario
     */
    private String lastPasswordChange;

    
    private String direccion;
    
    private Integer telefono;

    /**
     * Devuelve la direccion del usuario
     *
     * @return direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la direccion del usuario
     *
     * @param direccion
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve el telefono del usuario
     *
     * @return telefono
     */
    public Integer getTelefono() {
        return telefono;
    }

    /**
     * Establece el telefono del usuario
     *
     * @param telefono
     */
    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    /*
     * Devuelve el valor de login para Usuario
     * @return el valor de login
     */
    public String getLogin() {
        return login;
    }

    /*
     * Establece el valor de login para Usuario
     * @param de login del valor login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /*
     * Devuelve el valor de email para Usuario
     * @return el valor de email
     */
    public String getEmail() {
        return email;
    }

    /*
     * Establece el valor de email para Usuario
     * @param de email del valor email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * Devuelve el valor de fullname para Usuario
     * @return el valor de fullname
     */
    public String getFullname() {
        return fullname;
    }

    /*
     * Establece el valor de fullname para Usuario
     * @param de fullname del valor fullname
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /*
     * Devuelve el valor de status para Usuario
     * @return el valor de status
     */
    public EstadoUsuario getStatus() {
        return status;
    }

    /*
     * Establece el valor de status para Usuario
     * @param de status del valor status
     */
    public void setStatus(EstadoUsuario status) {
        this.status = status;
    }

    /*
     * Devuelve el valor de privilege para Usuario
     * @return el valor de privilege
     */
    public PrivilegioUsuario getPrivilege() {
        return privilege;
    }

    /*
     * Establece el valor de privilege para Usuario
     * @param de privilege del valor privilege
     */
    public void setPrivilege(PrivilegioUsuario privilege) {
        this.privilege = privilege;
    }

    /*
     * Devuelve el valor de password para Usuario
     * @return el valor de password
     */
    public String getPassword() {
        return password;
    }

    /*
     * Establece el valor de password para Usuario
     * @param de password del valor password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /*
     * Devuelve el valor de lastaccess para Usuario
     * @return el valor de lastaccess
     */
    public Date getLastAccess() {
        return lastAccess;
    }

    /*
     * Establece el valor de lastaccess para Usuario
     * @param de lastaccess del valor lastaccess
     */
    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    /*
     * Devuelve el valor de lastpaswordchange para Usuario
     * @return el valor de lastpasswordchange
     */
    public String getLastPasswordChange() {
        return lastPasswordChange;
    }

    /*
     * Establece el valor de lastpasswordchange para Usuario
     * @param de lastpasswordchange del valor lastpasswordchange
     */
    public void setLastPasswordChange(String lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    /*
     * Devuelve el valor de id_usuario para Usuario
     * @return el valor de id_usuario
     */
    public Long getId_usuario() {
        return id_usuario;
    }

    /*
     * Establece el valor de id_usuario para Usuario
     * @param de id_usuario del valor id_usuario
     */
    public void setId_usuario(Long id_usuario) {
        this.id_usuario = id_usuario;
    }

    /*
    * Implementacion del metodo Hashcode para la entidad
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id_usuario != null ? id_usuario.hashCode() : 0);
        return hash;
    }

    /*
    * Este metodo compara 2 entidades Usuario.
    * Esta implementacion compara el campo id
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id_usuario == null && other.id_usuario != null) || (this.id_usuario != null && !this.id_usuario.equals(other.id_usuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "flyshoes.entity.Usuario[ id=" + id_usuario + " ]";
    }

}
