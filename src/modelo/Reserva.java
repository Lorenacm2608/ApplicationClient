package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Esta clase se encargará sobre la gestión de reservas
 *
 * @author Fredy Vargas Flores
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identificador de la reserva
     */
    private Long id;

    //producto contiene la reserva
    public Producto producto;
    //descripcion de la reserva
    private String descripcion;
    //estado de la reserva

    private EstadoReserva estado;

    //cliente  de la reserva
    private Cliente cliente;
    //cantidad del producto

    private Integer cantidad;
    //Fecha de reserva
    
    private Date fechaReserva;
    //Fecha de entrega prevista
    
    private Date fechaEntrega;
    
    
    
    /**
     * Devuelve la id de la reserva
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece la id de la reserva
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve la descripcion de la reserva
     *
     * @return descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Inserta la descripcion de la reserva
     *
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve el estado de la reserva
     *
     * @return estado
     */
    public EstadoReserva getEstado() {
        return estado;
    }

    /**
     * Inserta el estado de la reserva
     *
     * @param estado
     */
    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    /**
     * Devuelve el cliente que ha realizado dicha reserva
     *
     * @return cliente
     */
    // @XmlTransient
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Añade al cliente de la reserva
     *
     * @param cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Devuelve del producto
     *
     * @return producto
     */
    // @XmlTransient
    public Producto getProducto() {
        return producto;
    }

    /**
     * Inserta el producto a la reserva
     *
     * @param producto
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Devuelve la cantidad del producto
     *
     * @return cantidad
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * Inserta la cantidad del producto
     *
     * @param cantidad
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Devuelve la fecha de la reserva
     *
     * @return
     */
    public Date getFechaReserva() {
        return fechaReserva;
    }

    /**
     * Inserta la fecha de reserva
     *
     * @param fechaReserva
     */
    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
    /**
     * Devuelve la fecha de la entrega prevista
     *
     * @return fechaEntrega
     */
    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * Inserta la fecha de la entrega prevista
     *
     * @param fechaEntrega
     */
    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}
