/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author crest
 */
@Entity
@Table(name = "consultas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consultas.findAll", query = "SELECT c FROM Consultas c"),
    @NamedQuery(name = "Consultas.findByIdConsulta", query = "SELECT c FROM Consultas c WHERE c.idConsulta = :idConsulta"),
    @NamedQuery(name = "Consultas.findByFechaConsulta", query = "SELECT c FROM Consultas c WHERE c.fechaConsulta = :fechaConsulta"),
    @NamedQuery(name = "Consultas.findByDescripcion", query = "SELECT c FROM Consultas c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Consultas.buscarConsultaAnimal", query = "SELECT c FROM Consultas c WHERE c.idMascota = :idMascota ORDER BY c.fechaConsulta DESC")})
public class Consultas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdConsulta")
    private Integer idConsulta;
    @Column(name = "FechaConsulta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConsulta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "Valoracion")
    private int valoracion;
    @JoinColumn(name = "IdMascota", referencedColumnName = "IdMascota")
    @ManyToOne(optional = false)
    private Mascotas idMascota;
    @JoinColumn(name = "IdEmpleado", referencedColumnName = "IdUsuario")
    @ManyToOne(optional = false)
    private Usuarios idEmpleado;

    public Consultas() {
    }

    public Consultas(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Consultas(Integer idConsulta, String descripcion) {
        this.idConsulta = idConsulta;
        this.descripcion = descripcion;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Date getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(Date fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getValoracion() {
        return valoracion;
    }
    
    public void setValoracion(int val) {
        this.valoracion = val;
    }

    public Mascotas getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Mascotas idMascota) {
        this.idMascota = idMascota;
    }

    public Usuarios getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Usuarios idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsulta != null ? idConsulta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consultas)) {
            return false;
        }
        Consultas other = (Consultas) object;
        if ((this.idConsulta == null && other.idConsulta != null) || (this.idConsulta != null && !this.idConsulta.equals(other.idConsulta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Consultas[ idConsulta=" + idConsulta + " ]";
    }
    
}
