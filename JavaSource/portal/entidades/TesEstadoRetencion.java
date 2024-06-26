/*
 * To change this template, choose Tools | Templates
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "tes_estado_retencion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesEstadoRetencion.findAll", query = "SELECT t FROM TesEstadoRetencion t"),
    @NamedQuery(name = "TesEstadoRetencion.findByIdeTeesr", query = "SELECT t FROM TesEstadoRetencion t WHERE t.ideTeesr = :ideTeesr"),
    @NamedQuery(name = "TesEstadoRetencion.findByDetalleTeesr", query = "SELECT t FROM TesEstadoRetencion t WHERE t.detalleTeesr = :detalleTeesr"),
    @NamedQuery(name = "TesEstadoRetencion.findByActivoTeesr", query = "SELECT t FROM TesEstadoRetencion t WHERE t.activoTeesr = :activoTeesr"),
    @NamedQuery(name = "TesEstadoRetencion.findByUsuarioIngre", query = "SELECT t FROM TesEstadoRetencion t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesEstadoRetencion.findByFechaIngre", query = "SELECT t FROM TesEstadoRetencion t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesEstadoRetencion.findByHoraIngre", query = "SELECT t FROM TesEstadoRetencion t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesEstadoRetencion.findByUsuarioActua", query = "SELECT t FROM TesEstadoRetencion t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesEstadoRetencion.findByFechaActua", query = "SELECT t FROM TesEstadoRetencion t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesEstadoRetencion.findByHoraActua", query = "SELECT t FROM TesEstadoRetencion t WHERE t.horaActua = :horaActua")})
public class TesEstadoRetencion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_teesr", nullable = false)
    private Long ideTeesr;
    @Size(max = 50)
    @Column(name = "detalle_teesr", length = 50)
    private String detalleTeesr;
    @Column(name = "activo_teesr")
    private Boolean activoTeesr;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideTeesr")
    private List<TesDatosRetencion> tesDatosRetencionList;

    public TesEstadoRetencion() {
    }

    public TesEstadoRetencion(Long ideTeesr) {
        this.ideTeesr = ideTeesr;
    }

    public Long getIdeTeesr() {
        return ideTeesr;
    }

    public void setIdeTeesr(Long ideTeesr) {
        this.ideTeesr = ideTeesr;
    }

    public String getDetalleTeesr() {
        return detalleTeesr;
    }

    public void setDetalleTeesr(String detalleTeesr) {
        this.detalleTeesr = detalleTeesr;
    }

    public Boolean getActivoTeesr() {
        return activoTeesr;
    }

    public void setActivoTeesr(Boolean activoTeesr) {
        this.activoTeesr = activoTeesr;
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

    public List<TesDatosRetencion> getTesDatosRetencionList() {
        return tesDatosRetencionList;
    }

    public void setTesDatosRetencionList(List<TesDatosRetencion> tesDatosRetencionList) {
        this.tesDatosRetencionList = tesDatosRetencionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTeesr != null ? ideTeesr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesEstadoRetencion)) {
            return false;
        }
        TesEstadoRetencion other = (TesEstadoRetencion) object;
        if ((this.ideTeesr == null && other.ideTeesr != null) || (this.ideTeesr != null && !this.ideTeesr.equals(other.ideTeesr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesEstadoRetencion[ ideTeesr=" + ideTeesr + " ]";
    }
    
}
