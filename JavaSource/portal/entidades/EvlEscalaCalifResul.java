/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "evl_escala_calif_resul", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlEscalaCalifResul.findAll", query = "SELECT e FROM EvlEscalaCalifResul e"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByIdeEvecr", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.ideEvecr = :ideEvecr"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByDetalleEvecr", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.detalleEvecr = :detalleEvecr"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByPorInicioEvecr", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.porInicioEvecr = :porInicioEvecr"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByPorFinEvecr", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.porFinEvecr = :porFinEvecr"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByActivoEvecr", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.activoEvecr = :activoEvecr"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByUsuarioIngre", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByFechaIngre", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByUsuarioActua", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByFechaActua", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByHoraIngre", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlEscalaCalifResul.findByHoraActua", query = "SELECT e FROM EvlEscalaCalifResul e WHERE e.horaActua = :horaActua")})
public class EvlEscalaCalifResul implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evecr", nullable = false)
    private Integer ideEvecr;
    @Size(max = 50)
    @Column(name = "detalle_evecr", length = 50)
    private String detalleEvecr;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "por_inicio_evecr", precision = 12, scale = 2)
    private BigDecimal porInicioEvecr;
    @Column(name = "por_fin_evecr", precision = 12, scale = 2)
    private BigDecimal porFinEvecr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evecr", nullable = false)
    private boolean activoEvecr;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
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

    public EvlEscalaCalifResul() {
    }

    public EvlEscalaCalifResul(Integer ideEvecr) {
        this.ideEvecr = ideEvecr;
    }

    public EvlEscalaCalifResul(Integer ideEvecr, boolean activoEvecr) {
        this.ideEvecr = ideEvecr;
        this.activoEvecr = activoEvecr;
    }

    public Integer getIdeEvecr() {
        return ideEvecr;
    }

    public void setIdeEvecr(Integer ideEvecr) {
        this.ideEvecr = ideEvecr;
    }

    public String getDetalleEvecr() {
        return detalleEvecr;
    }

    public void setDetalleEvecr(String detalleEvecr) {
        this.detalleEvecr = detalleEvecr;
    }

    public BigDecimal getPorInicioEvecr() {
        return porInicioEvecr;
    }

    public void setPorInicioEvecr(BigDecimal porInicioEvecr) {
        this.porInicioEvecr = porInicioEvecr;
    }

    public BigDecimal getPorFinEvecr() {
        return porFinEvecr;
    }

    public void setPorFinEvecr(BigDecimal porFinEvecr) {
        this.porFinEvecr = porFinEvecr;
    }

    public boolean getActivoEvecr() {
        return activoEvecr;
    }

    public void setActivoEvecr(boolean activoEvecr) {
        this.activoEvecr = activoEvecr;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEvecr != null ? ideEvecr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlEscalaCalifResul)) {
            return false;
        }
        EvlEscalaCalifResul other = (EvlEscalaCalifResul) object;
        if ((this.ideEvecr == null && other.ideEvecr != null) || (this.ideEvecr != null && !this.ideEvecr.equals(other.ideEvecr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlEscalaCalifResul[ ideEvecr=" + ideEvecr + " ]";
    }
    
}
