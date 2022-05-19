/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author crest
 */
@Entity
@Table(name = "animales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Animales.findAll", query = "SELECT a FROM Animales a"),
    @NamedQuery(name = "Animales.findByIdAnimal", query = "SELECT a FROM Animales a WHERE a.idAnimal = :idAnimal"),
    @NamedQuery(name = "Animales.findByTipoAnimal", query = "SELECT a FROM Animales a WHERE a.tipoAnimal = :tipoAnimal")})
public class Animales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdAnimal")
    private Integer idAnimal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "TipoAnimal")
    private String tipoAnimal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAnimal")
    private List<Mascotas> mascotasList;

    public Animales() {
    }

    public Animales(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public Animales(Integer idAnimal, String tipoAnimal) {
        this.idAnimal = idAnimal;
        this.tipoAnimal = tipoAnimal;
    }

    public Integer getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Integer idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(String tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    @XmlTransient
    public List<Mascotas> getMascotasList() {
        return mascotasList;
    }

    public void setMascotasList(List<Mascotas> mascotasList) {
        this.mascotasList = mascotasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnimal != null ? idAnimal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Animales)) {
            return false;
        }
        Animales other = (Animales) object;
        if ((this.idAnimal == null && other.idAnimal != null) || (this.idAnimal != null && !this.idAnimal.equals(other.idAnimal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Animales[ idAnimal=" + idAnimal + " ]";
    }
    
}
