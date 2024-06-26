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
@Table(name = "evl_competencia_relevancia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlCompetenciaRelevancia.findAll", query = "SELECT e FROM EvlCompetenciaRelevancia e"),
    @NamedQuery(name = "EvlCompetenciaRelevancia.findByIdeEvcor", query = "SELECT e FROM EvlCompetenciaRelevancia e WHERE e.ideEvcor = :ideEvcor"),
    @NamedQuery(name = "EvlCompetenciaRelevancia.findByDetalleEvcor", query = "SELECT e FROM EvlCompetenciaRelevancia e WHERE e.detalleEvcor = :detalleEvcor"),
    @NamedQuery(name = "EvlCompetenciaRelevancia.findByActivoEvcor", query = "SELECT e FROM EvlCompetenciaRelevancia e WHERE e.activoEvcor = :activoEvcor"),
    @NamedQuery(name = "EvlCompetenciaRelevancia.findByUsuarioIngre", query = "SELECT e FROM EvlCompetenciaRelevancia e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlCompetenciaRelevancia.findByFechaIngre", query = "SELECT e FROM EvlCompetenciaRelevancia e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlCompetenciaRelevancia.findByUsuarioActua", query = "SELECT e FROM EvlCompetenciaRelevancia e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlCompetenciaRelevancia.findByFechaActua", query = "SELECT e FROM EvlCompetenciaRelevancia e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlCompetenciaRelevancia.findByHoraIngre", query = "SELECT e FROM EvlCompetenciaRelevancia e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlCompetenciaRelevancia.findByHoraActua", query = "SELECT e FROM EvlCompetenciaRelevancia e WHERE e.horaActua = :horaActua")})
public class EvlCompetenciaRelevancia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evcor", nullable = false)
    private Integer ideEvcor;
    @Size(max = 1000)
    @Column(name = "detalle_evcor", length = 1000)
    private String detalleEvcor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evcor", nullable = false)
    private boolean activoEvcor;
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
    @JoinColumn(name = "ide_evrel", referencedColumnName = "ide_evrel")
    @ManyToOne
    private EvlRelevancia ideEvrel;
    @JoinColumn(name = "ide_evfae", referencedColumnName = "ide_evfae")
    @ManyToOne
    private EvlFactorEvaluacion ideEvfae;
    @JoinColumn(name = "ide_cmdec", referencedColumnName = "ide_cmdec")
    @ManyToOne
    private CmpDetalleCompetencia ideCmdec;

    public EvlCompetenciaRelevancia() {
    }

    public EvlCompetenciaRelevancia(Integer ideEvcor) {
        this.ideEvcor = ideEvcor;
    }

    public EvlCompetenciaRelevancia(Integer ideEvcor, boolean activoEvcor) {
        this.ideEvcor = ideEvcor;
        this.activoEvcor = activoEvcor;
    }

    public Integer getIdeEvcor() {
        return ideEvcor;
    }

    public void setIdeEvcor(Integer ideEvcor) {
        this.ideEvcor = ideEvcor;
    }

    public String getDetalleEvcor() {
        return detalleEvcor;
    }

    public void setDetalleEvcor(String detalleEvcor) {
        this.detalleEvcor = detalleEvcor;
    }

    public boolean getActivoEvcor() {
        return activoEvcor;
    }

    public void setActivoEvcor(boolean activoEvcor) {
        this.activoEvcor = activoEvcor;
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

    public EvlRelevancia getIdeEvrel() {
        return ideEvrel;
    }

    public void setIdeEvrel(EvlRelevancia ideEvrel) {
        this.ideEvrel = ideEvrel;
    }

    public EvlFactorEvaluacion getIdeEvfae() {
        return ideEvfae;
    }

    public void setIdeEvfae(EvlFactorEvaluacion ideEvfae) {
        this.ideEvfae = ideEvfae;
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
        hash += (ideEvcor != null ? ideEvcor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlCompetenciaRelevancia)) {
            return false;
        }
        EvlCompetenciaRelevancia other = (EvlCompetenciaRelevancia) object;
        if ((this.ideEvcor == null && other.ideEvcor != null) || (this.ideEvcor != null && !this.ideEvcor.equals(other.ideEvcor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlCompetenciaRelevancia[ ideEvcor=" + ideEvcor + " ]";
    }
    
}
