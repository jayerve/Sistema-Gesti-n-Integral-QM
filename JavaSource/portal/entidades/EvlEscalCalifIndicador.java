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
@Table(name = "evl_escal_calif_indicador", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlEscalCalifIndicador.findAll", query = "SELECT e FROM EvlEscalCalifIndicador e"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByIdeEveci", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.ideEveci = :ideEveci"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByDetalleEveci", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.detalleEveci = :detalleEveci"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByPorIniEveci", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.porIniEveci = :porIniEveci"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByPorFinEveci", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.porFinEveci = :porFinEveci"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByNivelCumpleEveci", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.nivelCumpleEveci = :nivelCumpleEveci"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByActivoEveci", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.activoEveci = :activoEveci"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByUsuarioIngre", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByFechaIngre", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByUsuarioActua", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByFechaActua", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByHoraIngre", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlEscalCalifIndicador.findByHoraActua", query = "SELECT e FROM EvlEscalCalifIndicador e WHERE e.horaActua = :horaActua")})
public class EvlEscalCalifIndicador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_eveci", nullable = false)
    private Integer ideEveci;
    @Size(max = 100)
    @Column(name = "detalle_eveci", length = 100)
    private String detalleEveci;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "por_ini_eveci", precision = 12, scale = 2)
    private BigDecimal porIniEveci;
    @Column(name = "por_fin_eveci", precision = 12, scale = 2)
    private BigDecimal porFinEveci;
    @Column(name = "nivel_cumple_eveci")
    private Integer nivelCumpleEveci;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_eveci", nullable = false)
    private boolean activoEveci;
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

    public EvlEscalCalifIndicador() {
    }

    public EvlEscalCalifIndicador(Integer ideEveci) {
        this.ideEveci = ideEveci;
    }

    public EvlEscalCalifIndicador(Integer ideEveci, boolean activoEveci) {
        this.ideEveci = ideEveci;
        this.activoEveci = activoEveci;
    }

    public Integer getIdeEveci() {
        return ideEveci;
    }

    public void setIdeEveci(Integer ideEveci) {
        this.ideEveci = ideEveci;
    }

    public String getDetalleEveci() {
        return detalleEveci;
    }

    public void setDetalleEveci(String detalleEveci) {
        this.detalleEveci = detalleEveci;
    }

    public BigDecimal getPorIniEveci() {
        return porIniEveci;
    }

    public void setPorIniEveci(BigDecimal porIniEveci) {
        this.porIniEveci = porIniEveci;
    }

    public BigDecimal getPorFinEveci() {
        return porFinEveci;
    }

    public void setPorFinEveci(BigDecimal porFinEveci) {
        this.porFinEveci = porFinEveci;
    }

    public Integer getNivelCumpleEveci() {
        return nivelCumpleEveci;
    }

    public void setNivelCumpleEveci(Integer nivelCumpleEveci) {
        this.nivelCumpleEveci = nivelCumpleEveci;
    }

    public boolean getActivoEveci() {
        return activoEveci;
    }

    public void setActivoEveci(boolean activoEveci) {
        this.activoEveci = activoEveci;
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
        hash += (ideEveci != null ? ideEveci.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlEscalCalifIndicador)) {
            return false;
        }
        EvlEscalCalifIndicador other = (EvlEscalCalifIndicador) object;
        if ((this.ideEveci == null && other.ideEveci != null) || (this.ideEveci != null && !this.ideEveci.equals(other.ideEveci))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlEscalCalifIndicador[ ideEveci=" + ideEveci + " ]";
    }
    
}
