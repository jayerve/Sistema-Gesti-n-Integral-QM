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
@Table(name = "afi_estado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiEstado.findAll", query = "SELECT a FROM AfiEstado a"),
    @NamedQuery(name = "AfiEstado.findByIdeAfest", query = "SELECT a FROM AfiEstado a WHERE a.ideAfest = :ideAfest"),
    @NamedQuery(name = "AfiEstado.findByDetalleAfest", query = "SELECT a FROM AfiEstado a WHERE a.detalleAfest = :detalleAfest"),
    @NamedQuery(name = "AfiEstado.findByPorcentajeAfest", query = "SELECT a FROM AfiEstado a WHERE a.porcentajeAfest = :porcentajeAfest"),
    @NamedQuery(name = "AfiEstado.findByActivoAfest", query = "SELECT a FROM AfiEstado a WHERE a.activoAfest = :activoAfest"),
    @NamedQuery(name = "AfiEstado.findByUsuarioIngre", query = "SELECT a FROM AfiEstado a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiEstado.findByFechaIngre", query = "SELECT a FROM AfiEstado a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiEstado.findByHoraIngre", query = "SELECT a FROM AfiEstado a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiEstado.findByUsuarioActua", query = "SELECT a FROM AfiEstado a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiEstado.findByFechaActua", query = "SELECT a FROM AfiEstado a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiEstado.findByHoraActua", query = "SELECT a FROM AfiEstado a WHERE a.horaActua = :horaActua")})
public class AfiEstado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_afest", nullable = false)
    private Integer ideAfest;
    @Size(max = 50)
    @Column(name = "detalle_afest", length = 50)
    private String detalleAfest;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_afest", precision = 10, scale = 2)
    private BigDecimal porcentajeAfest;
    @Column(name = "activo_afest")
    private Boolean activoAfest;
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

    public AfiEstado() {
    }

    public AfiEstado(Integer ideAfest) {
        this.ideAfest = ideAfest;
    }

    public Integer getIdeAfest() {
        return ideAfest;
    }

    public void setIdeAfest(Integer ideAfest) {
        this.ideAfest = ideAfest;
    }

    public String getDetalleAfest() {
        return detalleAfest;
    }

    public void setDetalleAfest(String detalleAfest) {
        this.detalleAfest = detalleAfest;
    }

    public BigDecimal getPorcentajeAfest() {
        return porcentajeAfest;
    }

    public void setPorcentajeAfest(BigDecimal porcentajeAfest) {
        this.porcentajeAfest = porcentajeAfest;
    }

    public Boolean getActivoAfest() {
        return activoAfest;
    }

    public void setActivoAfest(Boolean activoAfest) {
        this.activoAfest = activoAfest;
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
        hash += (ideAfest != null ? ideAfest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiEstado)) {
            return false;
        }
        AfiEstado other = (AfiEstado) object;
        if ((this.ideAfest == null && other.ideAfest != null) || (this.ideAfest != null && !this.ideAfest.equals(other.ideAfest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiEstado[ ideAfest=" + ideAfest + " ]";
    }
    
}
