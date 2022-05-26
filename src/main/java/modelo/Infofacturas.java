/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author crest
 */
@Entity
@Table(name = "infofacturas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Infofacturas.findAll", query = "SELECT i FROM Infofacturas i"),
    @NamedQuery(name = "Infofacturas.findByIdInfo", query = "SELECT i FROM Infofacturas i WHERE i.idInfo = :idInfo"),
    @NamedQuery(name = "Infofacturas.findByCantidad", query = "SELECT i FROM Infofacturas i WHERE i.cantidad = :cantidad"),
    @NamedQuery(name = "Infofacturas.findByPrecio", query = "SELECT i FROM Infofacturas i WHERE i.precio = :precio")})
public class Infofacturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdInfo")
    private Integer idInfo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Cantidad")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Precio")
    private float precio;
    @JoinColumn(name = "IdFactura", referencedColumnName = "IdFactura")
    @ManyToOne(optional = false)
    private Facturas idFactura;
    @JoinColumn(name = "IdProducto", referencedColumnName = "IdProducto")
    @ManyToOne(optional = false)
    private Productos idProducto;

    public Infofacturas() {
    }

    public Infofacturas(Integer idInfo) {
        this.idInfo = idInfo;
    }

    public Infofacturas(Integer idInfo, int cantidad, float precio) {
        this.idInfo = idInfo;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Integer getIdInfo() {
        return idInfo;
    }

    public void setIdInfo(Integer idInfo) {
        this.idInfo = idInfo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Facturas getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Facturas idFactura) {
        this.idFactura = idFactura;
    }

    public Productos getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Productos idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInfo != null ? idInfo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Infofacturas)) {
            return false;
        }
        Infofacturas other = (Infofacturas) object;
        if ((this.idInfo == null && other.idInfo != null) || (this.idInfo != null && !this.idInfo.equals(other.idInfo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Infofacturas[ idInfo=" + idInfo + " ]";
    }
    
}
