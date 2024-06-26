/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author abecerra
 */
@Entity
@Table(name = "veh_solicitud_ocupante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehSolicitudOcupante.findAll", query = "SELECT v FROM VehSolicitudOcupante v"),
    @NamedQuery(name = "VehSolicitudOcupante.findByIdeVesoc", query = "SELECT v FROM VehSolicitudOcupante v WHERE v.ideVesoc = :ideVesoc"),
    @NamedQuery(name = "VehSolicitudOcupante.findByActivoVesoc", query = "SELECT v FROM VehSolicitudOcupante v WHERE v.activoVesoc = :activoVesoc"),
    @NamedQuery(name = "VehSolicitudOcupante.findByUsuarioIngre", query = "SELECT v FROM VehSolicitudOcupante v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehSolicitudOcupante.findByFechaIngre", query = "SELECT v FROM VehSolicitudOcupante v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehSolicitudOcupante.findByHoraIngre", query = "SELECT v FROM VehSolicitudOcupante v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehSolicitudOcupante.findByUsuarioActua", query = "SELECT v FROM VehSolicitudOcupante v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehSolicitudOcupante.findByFechaActua", query = "SELECT v FROM VehSolicitudOcupante v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehSolicitudOcupante.findByHoraActua", query = "SELECT v FROM VehSolicitudOcupante v WHERE v.horaActua = :horaActua")})
public class VehSolicitudOcupante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vesoc")
    private Integer ideVesoc;
    @Column(name = "activo_vesoc")
    private Boolean activoVesoc;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_vesol", referencedColumnName = "ide_vesol")
    @ManyToOne
    private VehSolicitud ideVesol;

    public VehSolicitudOcupante() {
    	activoVesoc=true;
    }

    public VehSolicitudOcupante(Integer ideVesoc) {
        this.ideVesoc = ideVesoc;
    }

    public Integer getIdeVesoc() {
        return ideVesoc;
    }

    public void setIdeVesoc(Integer ideVesoc) {
        this.ideVesoc = ideVesoc;
    }

    public Boolean getActivoVesoc() {
        return activoVesoc;
    }

    public void setActivoVesoc(Boolean activoVesoc) {
        this.activoVesoc = activoVesoc;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public VehSolicitud getIdeVesol() {
        return ideVesol;
    }

    public void setIdeVesol(VehSolicitud ideVesol) {
        this.ideVesol = ideVesol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVesoc != null ? ideVesoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehSolicitudOcupante)) {
            return false;
        }
        VehSolicitudOcupante other = (VehSolicitudOcupante) object;
        if ((this.ideVesoc == null && other.ideVesoc != null) || (this.ideVesoc != null && !this.ideVesoc.equals(other.ideVesoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehSolicitudOcupante[ ideVesoc=" + ideVesoc + " ]";
    }
    
}
