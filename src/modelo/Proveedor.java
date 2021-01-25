/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Esta clase se encargará de facilitarnos productos(ropa y zapatillas).
 *
 * @author Fredy
 */
@XmlRootElement

public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    //id del proveedor
    private Long idProveedor;

    private String nombre;
    //tipo de proveedor 

    private TipoProducto tipo;
    //empresa a la que pertenece el proveedor

    private String empresa;
    //correo del proveedor

    private String email;
    //telefono del proveedor

    private String telefono;
    //descripcion del proveedor
    private String descripcion;
    //Fecha alta del proveedor
    private Date fechaAlta;
    //administrador encargado del proveedor
    private Administrador administrador;

    //lista de productos ofrecidos por el proveedor
    private Set<Producto> productos;

    public Proveedor(Long idProveedor, String nombre, TipoProducto tipo, String empresa, String email, String telefono, String descripcion, Administrador administrador, Set<Producto> productos) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.tipo = tipo;
        this.empresa = empresa;
        this.email = email;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.administrador = administrador;
        this.productos = productos;
    }

    public Proveedor() {

    }

    /**
     * Retorna el administrador encargado
     *
     * @return administrador
     */
    @XmlTransient
    public Administrador getAdministrador() {
        return administrador;
    }

    /**
     * Añadir administrador
     *
     * @param administrador
     */
    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    /**
     * Devuelve lista de productos ofrecidas por el proveedor
     *
     * @return lista de productos
     */
    @XmlTransient
    public Set<Producto> getProductos() {
        return productos;
    }

    /**
     * Añade lista de productos ofrecidas por el proveedor
     *
     * @param productos
     */
    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    /**
     * Devuelve el id del proveedor
     *
     * @return id
     */
    public Long getIdProveedor() {
        return idProveedor;
    }

    /**
     * Inserta el id del proveedor
     *
     * @param idProveedor
     */
    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    /**
     * Devuelve el tipo de proveedor
     *
     * @return tipo
     */
    public TipoProducto getTipo() {
        return tipo;
    }

    /**
     * Inserta el tipo de proveedor
     *
     * @param tipo
     */
    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve el nombre de la empresa ala que pertenece el proveedor
     *
     * @return empresa
     */
    public String getEmpresa() {
        return empresa;
    }

    /**
     * Inserta el nombre de la empresa ala que pertenece el proveedor
     *
     * @param empresa
     */
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    /**
     * Devuelve el correo del proveedor
     *
     * @return correo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Inserta el correo del proveedor
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuel el telefono del proveedor
     *
     * @return telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Inserta el telefono del proveedor
     *
     * @param telefono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve la descripcion del proveedor
     *
     * @return descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Inserta la descripcion del proveedor
     *
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve el nombre del proveedor
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Inserta el nombre del proveedor
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    
    
}
