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
@Table(name = "gth_liquidacion_viatico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthLiquidacionViatico.findAll", query = "SELECT g FROM GthLiquidacionViatico g"),
    @NamedQuery(name = "GthLiquidacionViatico.findByIdeGtliv", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.ideGtliv = :ideGtliv"),
    @NamedQuery(name = "GthLiquidacionViatico.findByNroDiasGtliv", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.nroDiasGtliv = :nroDiasGtliv"),
    @NamedQuery(name = "GthLiquidacionViatico.findByValorGtliv", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.valorGtliv = :valorGtliv"),
    @NamedQuery(name = "GthLiquidacionViatico.findByActivoGtliv", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.activoGtliv = :activoGtliv"),
    @NamedQuery(name = "GthLiquidacionViatico.findByUsuarioIngre", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthLiquidacionViatico.findByFechaIngre", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthLiquidacionViatico.findByUsuarioActua", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthLiquidacionViatico.findByFechaActua", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthLiquidacionViatico.findByHoraIngre", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthLiquidacionViatico.findByHoraActua", query = "SELECT g FROM GthLiquidacionViatico g WHERE g.horaActua = :horaActua")})
public class GthLiquidacionViatico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtliv", nullable = false)
    private Integer ideGtliv;
    @Column(name = "nro_dias_gtliv")
    private Integer nroDiasGtliv;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_gtliv", precision = 12, scale = 3)
    private BigDecimal valorGtliv;
    @Column(name = "activo_gtliv")
    private Boolean activoGtliv;
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
    @JoinColumn(name = "ide_gtvia", referencedColumnName = "ide_gtvia")
    @ManyToOne
    private GthViaticos ideGtvia;
    @JoinColumn(name = "ide_gtnzv", referencedColumnName = "ide_gtnzv")
    @ManyToOne
    private GthNivelZonaViatico ideGtnzv;

    public GthLiquidacionViatico() {
    }

    public GthLiquidacionViatico(Integer ideGtliv) {
        this.ideGtliv = ideGtliv;
    }

    public Integer getIdeGtliv() {
        return ideGtliv;
    }

    public void setIdeGtliv(Integer ideGtliv) {
        this.ideGtliv = ideGtliv;
    }

    public Integer getNroDiasGtliv() {
        return nroDiasGtliv;
    }

    public void setNroDiasGtliv(Integer nroDiasGtliv) {
        this.nroDiasGtliv = nroDiasGtliv;
    }

    public BigDecimal getValorGtliv() {
        return valorGtliv;
    }

    public void setValorGtliv(BigDecimal valorGtliv) {
        this.valorGtliv = valorGtliv;
    }

    public Boolean getActivoGtliv() {
        return activoGtliv;
    }

    public void setActivoGtliv(Boolean activoGtliv) {
        this.activoGtliv = activoGtliv;
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

    public GthViaticos getIdeGtvia() {
        return ideGtvia;
    }

    public void setIdeGtvia(GthViaticos ideGtvia) {
        this.ideGtvia = ideGtvia;
    }

    public GthNivelZonaViatico getIdeGtnzv() {
        return ideGtnzv;
    }

    public void setIdeGtnzv(GthNivelZonaViatico ideGtnzv) {
        this.ideGtnzv = ideGtnzv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtliv != null ? ideGtliv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthLiquidacionViatico)) {
            return false;
        }
        GthLiquidacionViatico other = (GthLiquidacionViatico) object;
        if ((this.ideGtliv == null && other.ideGtliv != null) || (this.ideGtliv != null && !this.ideGtliv.equals(other.ideGtliv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthLiquidacionViatico[ ideGtliv=" + ideGtliv + " ]";
    }
    
}
