/*
 * To change this template, choose Tools | Templates
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "tes_direccion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesDireccion.findAll", query = "SELECT t FROM TesDireccion t"),
    @NamedQuery(name = "TesDireccion.findByIdeTedir", query = "SELECT t FROM TesDireccion t WHERE t.ideTedir = :ideTedir"),
    @NamedQuery(name = "TesDireccion.findByDetalleTedir", query = "SELECT t FROM TesDireccion t WHERE t.detalleTedir = :detalleTedir"),
    @NamedQuery(name = "TesDireccion.findByNotificacionTedir", query = "SELECT t FROM TesDireccion t WHERE t.notificacionTedir = :notificacionTedir"),
    @NamedQuery(name = "TesDireccion.findByReferenciaTedir", query = "SELECT t FROM TesDireccion t WHERE t.referenciaTedir = :referenciaTedir"),
    @NamedQuery(name = "TesDireccion.findByActivoTedir", query = "SELECT t FROM TesDireccion t WHERE t.activoTedir = :activoTedir"),
    @NamedQuery(name = "TesDireccion.findByUsuarioIngre", query = "SELECT t FROM TesDireccion t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesDireccion.findByFechaIngre", query = "SELECT t FROM TesDireccion t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesDireccion.findByHoraIngre", query = "SELECT t FROM TesDireccion t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesDireccion.findByUsuarioActua", query = "SELECT t FROM TesDireccion t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesDireccion.findByFechaActua", query = "SELECT t FROM TesDireccion t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesDireccion.findByHoraActua", query = "SELECT t FROM TesDireccion t WHERE t.horaActua = :horaActua")})
public class TesDireccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tedir", nullable = false)
    private Long ideTedir;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "detalle_tedir", nullable = false, length = 50)
    private String detalleTedir;
    @Basic(optional = false)
    @NotNull
    @Column(name = "notificacion_tedir", nullable = false)
    private boolean notificacionTedir;
    @Size(max = 100)
    @Column(name = "referencia_tedir", length = 100)
    private String referenciaTedir;
    @Column(name = "activo_tedir")
    private Boolean activoTedir;
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
    @JoinColumn(name = "ide_tepro", referencedColumnName = "ide_tepro")
    @ManyToOne
    private TesProveedor ideTepro;

    public TesDireccion() {
    }

    public TesDireccion(Long ideTedir) {
        this.ideTedir = ideTedir;
    }

    public TesDireccion(Long ideTedir, String detalleTedir, boolean notificacionTedir) {
        this.ideTedir = ideTedir;
        this.detalleTedir = detalleTedir;
        this.notificacionTedir = notificacionTedir;
    }

    public Long getIdeTedir() {
        return ideTedir;
    }

    public void setIdeTedir(Long ideTedir) {
        this.ideTedir = ideTedir;
    }

    public String getDetalleTedir() {
        return detalleTedir;
    }

    public void setDetalleTedir(String detalleTedir) {
        this.detalleTedir = detalleTedir;
    }

    public boolean getNotificacionTedir() {
        return notificacionTedir;
    }

    public void setNotificacionTedir(boolean notificacionTedir) {
        this.notificacionTedir = notificacionTedir;
    }

    public String getReferenciaTedir() {
        return referenciaTedir;
    }

    public void setReferenciaTedir(String referenciaTedir) {
        this.referenciaTedir = referenciaTedir;
    }

    public Boolean getActivoTedir() {
        return activoTedir;
    }

    public void setActivoTedir(Boolean activoTedir) {
        this.activoTedir = activoTedir;
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

    public TesProveedor getIdeTepro() {
        return ideTepro;
    }

    public void setIdeTepro(TesProveedor ideTepro) {
        this.ideTepro = ideTepro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTedir != null ? ideTedir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesDireccion)) {
            return false;
        }
        TesDireccion other = (TesDireccion) object;
        if ((this.ideTedir == null && other.ideTedir != null) || (this.ideTedir != null && !this.ideTedir.equals(other.ideTedir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesDireccion[ ideTedir=" + ideTedir + " ]";
    }
    
}
