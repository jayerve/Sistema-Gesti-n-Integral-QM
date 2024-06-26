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
@Table(name = "spr_factor_ponderacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprFactorPonderacion.findAll", query = "SELECT s FROM SprFactorPonderacion s"),
    @NamedQuery(name = "SprFactorPonderacion.findByIdeSpfap", query = "SELECT s FROM SprFactorPonderacion s WHERE s.ideSpfap = :ideSpfap"),
    @NamedQuery(name = "SprFactorPonderacion.findByIdeGegro", query = "SELECT s FROM SprFactorPonderacion s WHERE s.ideGegro = :ideGegro"),
    @NamedQuery(name = "SprFactorPonderacion.findByDetalleSpfap", query = "SELECT s FROM SprFactorPonderacion s WHERE s.detalleSpfap = :detalleSpfap"),
    @NamedQuery(name = "SprFactorPonderacion.findByPuntosSpfap", query = "SELECT s FROM SprFactorPonderacion s WHERE s.puntosSpfap = :puntosSpfap"),
    @NamedQuery(name = "SprFactorPonderacion.findByActivoSpfap", query = "SELECT s FROM SprFactorPonderacion s WHERE s.activoSpfap = :activoSpfap"),
    @NamedQuery(name = "SprFactorPonderacion.findByUsuarioIngre", query = "SELECT s FROM SprFactorPonderacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprFactorPonderacion.findByFechaIngre", query = "SELECT s FROM SprFactorPonderacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprFactorPonderacion.findByHoraIngre", query = "SELECT s FROM SprFactorPonderacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprFactorPonderacion.findByUsuarioActua", query = "SELECT s FROM SprFactorPonderacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprFactorPonderacion.findByFechaActua", query = "SELECT s FROM SprFactorPonderacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprFactorPonderacion.findByHoraActua", query = "SELECT s FROM SprFactorPonderacion s WHERE s.horaActua = :horaActua")})
public class SprFactorPonderacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spfap", nullable = false)
    private Integer ideSpfap;
    @Column(name = "ide_gegro")
    private Integer ideGegro;
    @Size(max = 100)
    @Column(name = "detalle_spfap", length = 100)
    private String detalleSpfap;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puntos_spfap", precision = 12, scale = 2)
    private BigDecimal puntosSpfap;
    @Column(name = "activo_spfap")
    private Boolean activoSpfap;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @JoinColumn(name = "ide_spfac", referencedColumnName = "ide_spfac")
    @ManyToOne
    private SprFactor ideSpfac;

    public SprFactorPonderacion() {
    }

    public SprFactorPonderacion(Integer ideSpfap) {
        this.ideSpfap = ideSpfap;
    }

    public Integer getIdeSpfap() {
        return ideSpfap;
    }

    public void setIdeSpfap(Integer ideSpfap) {
        this.ideSpfap = ideSpfap;
    }

    public Integer getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(Integer ideGegro) {
        this.ideGegro = ideGegro;
    }

    public String getDetalleSpfap() {
        return detalleSpfap;
    }

    public void setDetalleSpfap(String detalleSpfap) {
        this.detalleSpfap = detalleSpfap;
    }

    public BigDecimal getPuntosSpfap() {
        return puntosSpfap;
    }

    public void setPuntosSpfap(BigDecimal puntosSpfap) {
        this.puntosSpfap = puntosSpfap;
    }

    public Boolean getActivoSpfap() {
        return activoSpfap;
    }

    public void setActivoSpfap(Boolean activoSpfap) {
        this.activoSpfap = activoSpfap;
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

    public SprFactor getIdeSpfac() {
        return ideSpfac;
    }

    public void setIdeSpfac(SprFactor ideSpfac) {
        this.ideSpfac = ideSpfac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpfap != null ? ideSpfap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprFactorPonderacion)) {
            return false;
        }
        SprFactorPonderacion other = (SprFactorPonderacion) object;
        if ((this.ideSpfap == null && other.ideSpfap != null) || (this.ideSpfap != null && !this.ideSpfap.equals(other.ideSpfap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprFactorPonderacion[ ideSpfap=" + ideSpfap + " ]";
    }
    
}
