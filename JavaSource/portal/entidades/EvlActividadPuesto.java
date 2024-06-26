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
@Table(name = "evl_actividad_puesto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlActividadPuesto.findAll", query = "SELECT e FROM EvlActividadPuesto e"),
    @NamedQuery(name = "EvlActividadPuesto.findByIdeEvacp", query = "SELECT e FROM EvlActividadPuesto e WHERE e.ideEvacp = :ideEvacp"),
    @NamedQuery(name = "EvlActividadPuesto.findByIndicadorEvacp", query = "SELECT e FROM EvlActividadPuesto e WHERE e.indicadorEvacp = :indicadorEvacp"),
    @NamedQuery(name = "EvlActividadPuesto.findByMetaEvacp", query = "SELECT e FROM EvlActividadPuesto e WHERE e.metaEvacp = :metaEvacp"),
    @NamedQuery(name = "EvlActividadPuesto.findByCumplidoEvacp", query = "SELECT e FROM EvlActividadPuesto e WHERE e.cumplidoEvacp = :cumplidoEvacp"),
    @NamedQuery(name = "EvlActividadPuesto.findByPorCumplidoEvacp", query = "SELECT e FROM EvlActividadPuesto e WHERE e.porCumplidoEvacp = :porCumplidoEvacp"),
    @NamedQuery(name = "EvlActividadPuesto.findByNivelCumplidoEvacp", query = "SELECT e FROM EvlActividadPuesto e WHERE e.nivelCumplidoEvacp = :nivelCumplidoEvacp"),
    @NamedQuery(name = "EvlActividadPuesto.findByActivoEvacp", query = "SELECT e FROM EvlActividadPuesto e WHERE e.activoEvacp = :activoEvacp"),
    @NamedQuery(name = "EvlActividadPuesto.findByUsuarioIngre", query = "SELECT e FROM EvlActividadPuesto e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlActividadPuesto.findByFechaIngre", query = "SELECT e FROM EvlActividadPuesto e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlActividadPuesto.findByHoraIngre", query = "SELECT e FROM EvlActividadPuesto e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlActividadPuesto.findByUsuarioActua", query = "SELECT e FROM EvlActividadPuesto e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlActividadPuesto.findByFechaActua", query = "SELECT e FROM EvlActividadPuesto e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlActividadPuesto.findByHoraActua", query = "SELECT e FROM EvlActividadPuesto e WHERE e.horaActua = :horaActua")})
public class EvlActividadPuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evacp", nullable = false)
    private Integer ideEvacp;
    @Size(max = 100)
    @Column(name = "indicador_evacp", length = 100)
    private String indicadorEvacp;
    @Column(name = "meta_evacp")
    private Integer metaEvacp;
    @Column(name = "cumplido_evacp")
    private Integer cumplidoEvacp;
    @Column(name = "por_cumplido_evacp")
    private Integer porCumplidoEvacp;
    @Column(name = "nivel_cumplido_evacp")
    private Integer nivelCumplidoEvacp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evacp", nullable = false)
    private boolean activoEvacp;
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
    @JoinColumn(name = "ide_cmdec", referencedColumnName = "ide_cmdec")
    @ManyToOne
    private CmpDetalleCompetencia ideCmdec;

    public EvlActividadPuesto() {
    }

    public EvlActividadPuesto(Integer ideEvacp) {
        this.ideEvacp = ideEvacp;
    }

    public EvlActividadPuesto(Integer ideEvacp, boolean activoEvacp) {
        this.ideEvacp = ideEvacp;
        this.activoEvacp = activoEvacp;
    }

    public Integer getIdeEvacp() {
        return ideEvacp;
    }

    public void setIdeEvacp(Integer ideEvacp) {
        this.ideEvacp = ideEvacp;
    }

    public String getIndicadorEvacp() {
        return indicadorEvacp;
    }

    public void setIndicadorEvacp(String indicadorEvacp) {
        this.indicadorEvacp = indicadorEvacp;
    }

    public Integer getMetaEvacp() {
        return metaEvacp;
    }

    public void setMetaEvacp(Integer metaEvacp) {
        this.metaEvacp = metaEvacp;
    }

    public Integer getCumplidoEvacp() {
        return cumplidoEvacp;
    }

    public void setCumplidoEvacp(Integer cumplidoEvacp) {
        this.cumplidoEvacp = cumplidoEvacp;
    }

    public Integer getPorCumplidoEvacp() {
        return porCumplidoEvacp;
    }

    public void setPorCumplidoEvacp(Integer porCumplidoEvacp) {
        this.porCumplidoEvacp = porCumplidoEvacp;
    }

    public Integer getNivelCumplidoEvacp() {
        return nivelCumplidoEvacp;
    }

    public void setNivelCumplidoEvacp(Integer nivelCumplidoEvacp) {
        this.nivelCumplidoEvacp = nivelCumplidoEvacp;
    }

    public boolean getActivoEvacp() {
        return activoEvacp;
    }

    public void setActivoEvacp(boolean activoEvacp) {
        this.activoEvacp = activoEvacp;
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

    public CmpDetalleCompetencia getIdeCmdec() {
        return ideCmdec;
    }

    public void setIdeCmdec(CmpDetalleCompetencia ideCmdec) {
        this.ideCmdec = ideCmdec;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEvacp != null ? ideEvacp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlActividadPuesto)) {
            return false;
        }
        EvlActividadPuesto other = (EvlActividadPuesto) object;
        if ((this.ideEvacp == null && other.ideEvacp != null) || (this.ideEvacp != null && !this.ideEvacp.equals(other.ideEvacp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlActividadPuesto[ ideEvacp=" + ideEvacp + " ]";
    }
    
}
