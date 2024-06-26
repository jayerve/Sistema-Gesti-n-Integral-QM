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
@Table(name = "evl_resultado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlResultado.findAll", query = "SELECT e FROM EvlResultado e"),
    @NamedQuery(name = "EvlResultado.findByIdeEvres", query = "SELECT e FROM EvlResultado e WHERE e.ideEvres = :ideEvres"),
    @NamedQuery(name = "EvlResultado.findByResultadoEvres", query = "SELECT e FROM EvlResultado e WHERE e.resultadoEvres = :resultadoEvres"),
    @NamedQuery(name = "EvlResultado.findByPesoFactorEvres", query = "SELECT e FROM EvlResultado e WHERE e.pesoFactorEvres = :pesoFactorEvres"),
    @NamedQuery(name = "EvlResultado.findByActivoEvres", query = "SELECT e FROM EvlResultado e WHERE e.activoEvres = :activoEvres"),
    @NamedQuery(name = "EvlResultado.findByUsuarioIngre", query = "SELECT e FROM EvlResultado e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlResultado.findByFechaIngre", query = "SELECT e FROM EvlResultado e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlResultado.findByHoraIngre", query = "SELECT e FROM EvlResultado e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlResultado.findByUsuarioActua", query = "SELECT e FROM EvlResultado e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlResultado.findByFechaActua", query = "SELECT e FROM EvlResultado e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlResultado.findByHoraActua", query = "SELECT e FROM EvlResultado e WHERE e.horaActua = :horaActua")})
public class EvlResultado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evres", nullable = false)
    private Integer ideEvres;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "resultado_evres", precision = 12, scale = 2)
    private BigDecimal resultadoEvres;
    @Column(name = "peso_factor_evres", precision = 12, scale = 2)
    private BigDecimal pesoFactorEvres;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evres", nullable = false)
    private boolean activoEvres;
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
    @JoinColumn(name = "ide_evfae", referencedColumnName = "ide_evfae")
    @ManyToOne
    private EvlFactorEvaluacion ideEvfae;
    @JoinColumn(name = "ide_eveva", referencedColumnName = "ide_eveva")
    @ManyToOne
    private EvlEvaluadores ideEveva;

    public EvlResultado() {
    }

    public EvlResultado(Integer ideEvres) {
        this.ideEvres = ideEvres;
    }

    public EvlResultado(Integer ideEvres, boolean activoEvres) {
        this.ideEvres = ideEvres;
        this.activoEvres = activoEvres;
    }

    public Integer getIdeEvres() {
        return ideEvres;
    }

    public void setIdeEvres(Integer ideEvres) {
        this.ideEvres = ideEvres;
    }

    public BigDecimal getResultadoEvres() {
        return resultadoEvres;
    }

    public void setResultadoEvres(BigDecimal resultadoEvres) {
        this.resultadoEvres = resultadoEvres;
    }

    public BigDecimal getPesoFactorEvres() {
        return pesoFactorEvres;
    }

    public void setPesoFactorEvres(BigDecimal pesoFactorEvres) {
        this.pesoFactorEvres = pesoFactorEvres;
    }

    public boolean getActivoEvres() {
        return activoEvres;
    }

    public void setActivoEvres(boolean activoEvres) {
        this.activoEvres = activoEvres;
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

    public EvlFactorEvaluacion getIdeEvfae() {
        return ideEvfae;
    }

    public void setIdeEvfae(EvlFactorEvaluacion ideEvfae) {
        this.ideEvfae = ideEvfae;
    }

    public EvlEvaluadores getIdeEveva() {
        return ideEveva;
    }

    public void setIdeEveva(EvlEvaluadores ideEveva) {
        this.ideEveva = ideEveva;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEvres != null ? ideEvres.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlResultado)) {
            return false;
        }
        EvlResultado other = (EvlResultado) object;
        if ((this.ideEvres == null && other.ideEvres != null) || (this.ideEvres != null && !this.ideEvres.equals(other.ideEvres))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlResultado[ ideEvres=" + ideEvres + " ]";
    }
    
}
