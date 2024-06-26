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
@Table(name = "veh_solicitud_estado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehSolicitudEstado.findAll", query = "SELECT v FROM VehSolicitudEstado v"),
    @NamedQuery(name = "VehSolicitudEstado.findByIdeVetes", query = "SELECT v FROM VehSolicitudEstado v WHERE v.ideVetes = :ideVetes"),
    @NamedQuery(name = "VehSolicitudEstado.findByDetalleVetes", query = "SELECT v FROM VehSolicitudEstado v WHERE v.detalleVetes = :detalleVetes"),
    @NamedQuery(name = "VehSolicitudEstado.findByActivoVetes", query = "SELECT v FROM VehSolicitudEstado v WHERE v.activoVetes = :activoVetes"),
    @NamedQuery(name = "VehSolicitudEstado.findByUsuarioIngre", query = "SELECT v FROM VehSolicitudEstado v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehSolicitudEstado.findByFechaIngre", query = "SELECT v FROM VehSolicitudEstado v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehSolicitudEstado.findByHoraIngre", query = "SELECT v FROM VehSolicitudEstado v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehSolicitudEstado.findByUsuarioActua", query = "SELECT v FROM VehSolicitudEstado v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehSolicitudEstado.findByFechaActua", query = "SELECT v FROM VehSolicitudEstado v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehSolicitudEstado.findByHoraActua", query = "SELECT v FROM VehSolicitudEstado v WHERE v.horaActua = :horaActua")})
public class VehSolicitudEstado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vetes")
    private Long ideVetes;
    @Column(name = "detalle_vetes")
    private String detalleVetes;
    @Column(name = "activo_vetes")
    private Boolean activoVetes;
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
    @OneToMany(mappedBy = "ideVetes")
    private List<VehSolicitud> vehSolicitudList;

    public VehSolicitudEstado() {
    }

    public VehSolicitudEstado(Long ideVetes) {
        this.ideVetes = ideVetes;
    }

    public Long getIdeVetes() {
        return ideVetes;
    }

    public void setIdeVetes(Long ideVetes) {
        this.ideVetes = ideVetes;
    }

    public String getDetalleVetes() {
        return detalleVetes;
    }

    public void setDetalleVetes(String detalleVetes) {
        this.detalleVetes = detalleVetes;
    }

    public Boolean getActivoVetes() {
        return activoVetes;
    }

    public void setActivoVetes(Boolean activoVetes) {
        this.activoVetes = activoVetes;
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
    public List<VehSolicitud> getVehSolicitudList() {
        return vehSolicitudList;
    }

    public void setVehSolicitudList(List<VehSolicitud> vehSolicitudList) {
        this.vehSolicitudList = vehSolicitudList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVetes != null ? ideVetes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehSolicitudEstado)) {
            return false;
        }
        VehSolicitudEstado other = (VehSolicitudEstado) object;
        if ((this.ideVetes == null && other.ideVetes != null) || (this.ideVetes != null && !this.ideVetes.equals(other.ideVetes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehSolicitudEstado[ ideVetes=" + ideVetes + " ]";
    }
    
}
