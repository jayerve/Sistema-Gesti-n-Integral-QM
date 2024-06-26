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
@Table(name = "tes_correo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesCorreo.findAll", query = "SELECT t FROM TesCorreo t"),
    @NamedQuery(name = "TesCorreo.findByIdeTecor", query = "SELECT t FROM TesCorreo t WHERE t.ideTecor = :ideTecor"),
    @NamedQuery(name = "TesCorreo.findByDetalleTecor", query = "SELECT t FROM TesCorreo t WHERE t.detalleTecor = :detalleTecor"),
    @NamedQuery(name = "TesCorreo.findByNotificacionTecor", query = "SELECT t FROM TesCorreo t WHERE t.notificacionTecor = :notificacionTecor"),
    @NamedQuery(name = "TesCorreo.findByActivoTecorr", query = "SELECT t FROM TesCorreo t WHERE t.activoTecorr = :activoTecorr"),
    @NamedQuery(name = "TesCorreo.findByUsuarioIngre", query = "SELECT t FROM TesCorreo t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesCorreo.findByFechaIngre", query = "SELECT t FROM TesCorreo t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesCorreo.findByHoraIngre", query = "SELECT t FROM TesCorreo t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesCorreo.findByUsuarioActua", query = "SELECT t FROM TesCorreo t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesCorreo.findByFechaActua", query = "SELECT t FROM TesCorreo t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesCorreo.findByHoraActua", query = "SELECT t FROM TesCorreo t WHERE t.horaActua = :horaActua")})
public class TesCorreo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tecor", nullable = false)
    private Long ideTecor;
    @Size(max = 100)
    @Column(name = "detalle_tecor", length = 100)
    private String detalleTecor;
    @Column(name = "notificacion_tecor")
    private Boolean notificacionTecor;
    @Column(name = "activo_tecorr")
    private Boolean activoTecorr;
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

    public TesCorreo() {
    }

    public TesCorreo(Long ideTecor) {
        this.ideTecor = ideTecor;
    }

    public Long getIdeTecor() {
        return ideTecor;
    }

    public void setIdeTecor(Long ideTecor) {
        this.ideTecor = ideTecor;
    }

    public String getDetalleTecor() {
        return detalleTecor;
    }

    public void setDetalleTecor(String detalleTecor) {
        this.detalleTecor = detalleTecor;
    }

    public Boolean getNotificacionTecor() {
        return notificacionTecor;
    }

    public void setNotificacionTecor(Boolean notificacionTecor) {
        this.notificacionTecor = notificacionTecor;
    }

    public Boolean getActivoTecorr() {
        return activoTecorr;
    }

    public void setActivoTecorr(Boolean activoTecorr) {
        this.activoTecorr = activoTecorr;
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
        hash += (ideTecor != null ? ideTecor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesCorreo)) {
            return false;
        }
        TesCorreo other = (TesCorreo) object;
        if ((this.ideTecor == null && other.ideTecor != null) || (this.ideTecor != null && !this.ideTecor.equals(other.ideTecor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesCorreo[ ideTecor=" + ideTecor + " ]";
    }
    
}
