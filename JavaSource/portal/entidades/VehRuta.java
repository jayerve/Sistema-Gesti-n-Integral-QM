/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author abecerra
 */
@Entity
@Table(name = "veh_ruta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehRuta.findAll", query = "SELECT v FROM VehRuta v"),
    @NamedQuery(name = "VehRuta.findByIdeVerut", query = "SELECT v FROM VehRuta v WHERE v.ideVerut = :ideVerut"),
    @NamedQuery(name = "VehRuta.findByDetalleVerut", query = "SELECT v FROM VehRuta v WHERE v.detalleVerut = :detalleVerut"),
    @NamedQuery(name = "VehRuta.findByActivoVerut", query = "SELECT v FROM VehRuta v WHERE v.activoVerut = :activoVerut"),
    @NamedQuery(name = "VehRuta.findByUsuarioIngre", query = "SELECT v FROM VehRuta v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehRuta.findByFechaIngre", query = "SELECT v FROM VehRuta v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehRuta.findByHoraIngre", query = "SELECT v FROM VehRuta v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehRuta.findByUsuarioActua", query = "SELECT v FROM VehRuta v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehRuta.findByFechaActua", query = "SELECT v FROM VehRuta v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehRuta.findByHoraActua", query = "SELECT v FROM VehRuta v WHERE v.horaActua = :horaActua")})
public class VehRuta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_verut")
    private Long ideVerut;
    @Column(name = "detalle_verut")
    private String detalleVerut;
    @Column(name = "activo_verut")
    private Boolean activoVerut;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideVerut")
    private List<VehSolicitudRuta> vehSolicitudRutaList;

    public VehRuta() {
    }

    public VehRuta(Long ideVerut) {
        this.ideVerut = ideVerut;
    }

    public Long getIdeVerut() {
        return ideVerut;
    }

    public void setIdeVerut(Long ideVerut) {
        this.ideVerut = ideVerut;
    }

    public String getDetalleVerut() {
        return detalleVerut;
    }

    public void setDetalleVerut(String detalleVerut) {
        this.detalleVerut = detalleVerut;
    }

    public Boolean getActivoVerut() {
        return activoVerut;
    }

    public void setActivoVerut(Boolean activoVerut) {
        this.activoVerut = activoVerut;
    }

    public String getUsuarioIngre() {
        return usuarioIngre;
    }

    public void setUsuarioIngre(String usuarioIngre) {
        this.usuarioIngre = usuarioIngre;
    }

    public Date getFechaIngre() {
        return fechaIngre;
    }

    public void setFechaIngre(Date fechaIngre) {
        this.fechaIngre = fechaIngre;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public String getUsuarioActua() {
        return usuarioActua;
    }

    public void setUsuarioActua(String usuarioActua) {
        this.usuarioActua = usuarioActua;
    }

    public Date getFechaActua() {
        return fechaActua;
    }

    public void setFechaActua(Date fechaActua) {
        this.fechaActua = fechaActua;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    @XmlTransient
    public List<VehSolicitudRuta> getVehSolicitudRutaList() {
        return vehSolicitudRutaList;
    }

    public void setVehSolicitudRutaList(List<VehSolicitudRuta> vehSolicitudRutaList) {
        this.vehSolicitudRutaList = vehSolicitudRutaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVerut != null ? ideVerut.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehRuta)) {
            return false;
        }
        VehRuta other = (VehRuta) object;
        if ((this.ideVerut == null && other.ideVerut != null) || (this.ideVerut != null && !this.ideVerut.equals(other.ideVerut))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehRuta[ ideVerut=" + ideVerut + " ]";
    }
    
}
