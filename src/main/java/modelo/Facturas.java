/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author crest
 */
@Entity
@Table(name = "facturas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Facturas.findAll", query = "SELECT f FROM Facturas f"),
    @NamedQuery(name = "Facturas.findByIdFactura", query = "SELECT f FROM Facturas f WHERE f.idFactura = :idFactura"),
    @NamedQuery(name = "Facturas.findByFechaFactura", query = "SELECT f FROM Facturas f WHERE f.fechaFactura = :fechaFactura"),
    @NamedQuery(name = "Facturas.facturasCliente", query = "SELECT f FROM Facturas f WHERE f.idCliente = :usuario ORDER BY f.fechaFactura DESC")})
public class Facturas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdFactura")
    private Integer idFactura;
    @Column(name = "FechaFactura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFactura;
    @JoinColumn(name = "IdCliente", referencedColumnName = "IdUsuario")
    @ManyToOne(optional = false)
    private Usuarios idCliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFactura")
    private List<Infofacturas> infofacturasList;

    public Facturas() {
    }

    public Facturas(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Usuarios getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Usuarios idCliente) {
        this.idCliente = idCliente;
    }

    @XmlTransient
    public List<Infofacturas> getInfofacturasList() {
        return infofacturasList;
    }

    public void setInfofacturasList(List<Infofacturas> infofacturasList) {
        this.infofacturasList = infofacturasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFactura != null ? idFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facturas)) {
            return false;
        }
        Facturas other = (Facturas) object;
        if ((this.idFactura == null && other.idFactura != null) || (this.idFactura != null && !this.idFactura.equals(other.idFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Facturas[ idFactura=" + idFactura + " ]";
    }
    
}
