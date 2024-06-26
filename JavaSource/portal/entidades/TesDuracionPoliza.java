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
@Table(name = "tes_duracion_poliza", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesDuracionPoliza.findAll", query = "SELECT t FROM TesDuracionPoliza t"),
    @NamedQuery(name = "TesDuracionPoliza.findByIdeTedup", query = "SELECT t FROM TesDuracionPoliza t WHERE t.ideTedup = :ideTedup"),
    @NamedQuery(name = "TesDuracionPoliza.findByDetalleTedup", query = "SELECT t FROM TesDuracionPoliza t WHERE t.detalleTedup = :detalleTedup"),
    @NamedQuery(name = "TesDuracionPoliza.findByActivoTedup", query = "SELECT t FROM TesDuracionPoliza t WHERE t.activoTedup = :activoTedup"),
    @NamedQuery(name = "TesDuracionPoliza.findByUsuarioIngre", query = "SELECT t FROM TesDuracionPoliza t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesDuracionPoliza.findByFechaIngre", query = "SELECT t FROM TesDuracionPoliza t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesDuracionPoliza.findByHoraIngre", query = "SELECT t FROM TesDuracionPoliza t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesDuracionPoliza.findByUsuarioActua", query = "SELECT t FROM TesDuracionPoliza t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesDuracionPoliza.findByFechaActua", query = "SELECT t FROM TesDuracionPoliza t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesDuracionPoliza.findByHoraActua", query = "SELECT t FROM TesDuracionPoliza t WHERE t.horaActua = :horaActua")})
public class TesDuracionPoliza implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tedup", nullable = false)
    private Long ideTedup;
    @Size(max = 50)
    @Column(name = "detalle_tedup", length = 50)
    private String detalleTedup;
    @Column(name = "activo_tedup")
    private Boolean activoTedup;
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

    public TesDuracionPoliza() {
    }

    public TesDuracionPoliza(Long ideTedup) {
        this.ideTedup = ideTedup;
    }

    public Long getIdeTedup() {
        return ideTedup;
    }

    public void setIdeTedup(Long ideTedup) {
        this.ideTedup = ideTedup;
    }

    public String getDetalleTedup() {
        return detalleTedup;
    }

    public void setDetalleTedup(String detalleTedup) {
        this.detalleTedup = detalleTedup;
    }

    public Boolean getActivoTedup() {
        return activoTedup;
    }

    public void setActivoTedup(Boolean activoTedup) {
        this.activoTedup = activoTedup;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTedup != null ? ideTedup.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesDuracionPoliza)) {
            return false;
        }
        TesDuracionPoliza other = (TesDuracionPoliza) object;
        if ((this.ideTedup == null && other.ideTedup != null) || (this.ideTedup != null && !this.ideTedup.equals(other.ideTedup))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesDuracionPoliza[ ideTedup=" + ideTedup + " ]";
    }
    
}
