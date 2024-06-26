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
@Table(name = "veh_solicitud_archivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehSolicitudArchivo.findAll", query = "SELECT v FROM VehSolicitudArchivo v"),
    @NamedQuery(name = "VehSolicitudArchivo.findByIdeVesoa", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.ideVesoa = :ideVesoa"),
    @NamedQuery(name = "VehSolicitudArchivo.findByDetalleVesoa", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.detalleVesoa = :detalleVesoa"),
    @NamedQuery(name = "VehSolicitudArchivo.findByFechaVesoa", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.fechaVesoa = :fechaVesoa"),
    @NamedQuery(name = "VehSolicitudArchivo.findByArchivoVesoa", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.archivoVesoa = :archivoVesoa"),
    @NamedQuery(name = "VehSolicitudArchivo.findByActivoVesoa", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.activoVesoa = :activoVesoa"),
    @NamedQuery(name = "VehSolicitudArchivo.findByUsuarioIngre", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehSolicitudArchivo.findByFechaIngre", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehSolicitudArchivo.findByUsuarioActua", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehSolicitudArchivo.findByFechaActua", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehSolicitudArchivo.findByHoraIngre", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehSolicitudArchivo.findByHoraActua", query = "SELECT v FROM VehSolicitudArchivo v WHERE v.horaActua = :horaActua")})
public class VehSolicitudArchivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vesoa")
    private Integer ideVesoa;
    @Column(name = "detalle_vesoa")
    private String detalleVesoa;
    @Column(name = "fecha_vesoa")
    @Temporal(TemporalType.DATE)
    private Date fechaVesoa;
    @Column(name = "archivo_vesoa")
    private String archivoVesoa;
    @Column(name = "activo_vesoa")
    private Boolean activoVesoa;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @JoinColumn(name = "ide_vesol", referencedColumnName = "ide_vesol")
    @ManyToOne
    private VehSolicitud ideVesol;

    public VehSolicitudArchivo() {
    	detalleVesoa="";
    	fechaVesoa=new Date();
    	activoVesoa=true;
    	ideVesoa=0;
    }

    public VehSolicitudArchivo(Integer ideVesoa) {
        this.ideVesoa = ideVesoa;
    }

    public Integer getIdeVesoa() {
        return ideVesoa;
    }

    public void setIdeVesoa(Integer ideVesoa) {
        this.ideVesoa = ideVesoa;
    }

    public String getDetalleVesoa() {
        return detalleVesoa;
    }

    public void setDetalleVesoa(String detalleVesoa) {
        this.detalleVesoa = detalleVesoa;
    }

    public Date getFechaVesoa() {
        return fechaVesoa;
    }

    public void setFechaVesoa(Date fechaVesoa) {
        this.fechaVesoa = fechaVesoa;
    }

    public String getArchivoVesoa() {
        return archivoVesoa;
    }

    public void setArchivoVesoa(String archivoVesoa) {
        this.archivoVesoa = archivoVesoa;
    }

    public Boolean getActivoVesoa() {
        return activoVesoa;
    }

    public void setActivoVesoa(Boolean activoVesoa) {
        this.activoVesoa = activoVesoa;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
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
        hash += (ideVesoa != null ? ideVesoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehSolicitudArchivo)) {
            return false;
        }
        VehSolicitudArchivo other = (VehSolicitudArchivo) object;
        if ((this.ideVesoa == null && other.ideVesoa != null) || (this.ideVesoa != null && !this.ideVesoa.equals(other.ideVesoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.VehSolicitudArchivo[ ideVesoa=" + ideVesoa + " ]";
    }
    
}
