package modelo;

import java.io.Serializable;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/*
 * Entidad Vendedor relacionado con gestiona Producto y maneja Reserva
 */
@XmlRootElement
public class Vendedor extends Usuario implements Serializable {

    /*
     * Vendedor se relaciona con Cliente por OneToMany
     */
   
    private Set<Cliente> cliente;

    /*
    *Vendedor se relaciona con Producto por ManyToMany
     */
    
    private Set<Producto> productos;
    
    //id del vendedor
    private Long idVendedor;

    public Long getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Long idVendedor) {
        this.idVendedor = idVendedor;
    }
    
    private Administrador administrador;

    private static final long serialVersionUID = 1L;

    /*
    * Dni del vendedor
     */
    
    private String dni;

    /*
    * Salario del vendedor
     */
    
    private Integer salario;

    /*
    * Tienda del vendedor
     */
    
    private String tienda;

    /**
     * Devuelve una lista de productos
     *
     * @return productos
     */
    @XmlTransient
    public Set<Producto> getProductos() {
        return productos;
    }

    /**
     * Establece una lista de productos
     *
     * @param productos
     */
    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    /**
     * Devuelve el dni del vendedor
     *
     * @return dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el dni del producto
     *
     * @param dni
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Devuelve el salario del vendedor
     *
     * @return salario
     */
    public Integer getSalario() {
        return salario;
    }

    /**
     * Establece el salario del vendedor
     *
     * @param salario
     */
    public void setSalario(Integer salario) {
        this.salario = salario;
    }

    /**
     * Devuelve la tienda en la que trabaja el vendedor
     *
     * @return tienda
     */
    public String getTienda() {
        return tienda;
    }

    /**
     * Establece la tienda en la que trabaja el vendedor
     *
     * @param tienda
     */
    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    /**
     * Devuelve una lista de clientes
     *
     * @return lista de clientes
     */
    @XmlTransient
    public Set<Cliente> getCliente() {
        return cliente;
    }

    /**
     * Establece una lista de clientes
     *
     * @param cliente
     */
    public void setCliente(Set<Cliente> cliente) {
        this.cliente = cliente;
    }

    /**
     * devuelve el administrador
     *
     * @return administrador
     */
    @XmlTransient
    public Administrador getAdministrador() {
        return administrador;
    }

    /**
     * establece el administrador
     *
     * @param administrador
     */
    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    @Override
    public String toString() {
        return "flyshoes.entity.Vendedor[ id=" + dni + " ]";
    }

}
