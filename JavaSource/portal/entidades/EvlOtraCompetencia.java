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
@Table(name = "evl_otra_competencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlOtraCompetencia.findAll", query = "SELECT e FROM EvlOtraCompetencia e"),
    @NamedQuery(name = "EvlOtraCompetencia.findByIdeEvotc", query = "SELECT e FROM EvlOtraCompetencia e WHERE e.ideEvotc = :ideEvotc"),
    @NamedQuery(name = "EvlOtraCompetencia.findByNivelCumplidoEvotc", query = "SELECT e FROM EvlOtraCompetencia e WHERE e.nivelCumplidoEvotc = :nivelCumplidoEvotc"),
    @NamedQuery(name = "EvlOtraCompetencia.findByActivoEvotc", query = "SELECT e FROM EvlOtraCompetencia e WHERE e.activoEvotc = :activoEvotc"),
    @NamedQuery(name = "EvlOtraCompetencia.findByUsuarioIngre", query = "SELECT e FROM EvlOtraCompetencia e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlOtraCompetencia.findByFechaIngre", query = "SELECT e FROM EvlOtraCompetencia e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlOtraCompetencia.findByHoraIngre", query = "SELECT e FROM EvlOtraCompetencia e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlOtraCompetencia.findByUsuarioActua", query = "SELECT e FROM EvlOtraCompetencia e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlOtraCompetencia.findByFechaActua", query = "SELECT e FROM EvlOtraCompetencia e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlOtraCompetencia.findByHoraActua", query = "SELECT e FROM EvlOtraCompetencia e WHERE e.horaActua = :horaActua")})
public class EvlOtraCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evotc", nullable = false)
    private Integer ideEvotc;
    @Column(name = "nivel_cumplido_evotc")
    private Integer nivelCumplidoEvotc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evotc", nullable = false)
    private boolean activoEvotc;
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
    @JoinColumn(name = "ide_evrel", referencedColumnName = "ide_evrel")
    @ManyToOne
    private EvlRelevancia ideEvrel;
    @JoinColumn(name = "ide_evnid", referencedColumnName = "ide_evnid")
    @ManyToOne
    private EvlNivelDesarrollo ideEvnid;
    @JoinColumn(name = "ide_evfae", referencedColumnName = "ide_evfae")
    @ManyToOne
    private EvlFactorEvaluacion ideEvfae;
    @JoinColumn(name = "ide_eveva", referencedColumnName = "ide_eveva")
    @ManyToOne
    private EvlEvaluadores ideEveva;
    @JoinColumn(name = "ide_cmdec", referencedColumnName = "ide_cmdec")
    @ManyToOne
    private CmpDetalleCompetencia ideCmdec;

    public EvlOtraCompetencia() {
    }

    public EvlOtraCompetencia(Integer ideEvotc) {
        this.ideEvotc = ideEvotc;
    }

    public EvlOtraCompetencia(Integer ideEvotc, boolean activoEvotc) {
        this.ideEvotc = ideEvotc;
        this.activoEvotc = activoEvotc;
    }

    public Integer getIdeEvotc() {
        return ideEvotc;
    }

    public void setIdeEvotc(Integer ideEvotc) {
        this.ideEvotc = ideEvotc;
    }

    public Integer getNivelCumplidoEvotc() {
        return nivelCumplidoEvotc;
    }

    public void setNivelCumplidoEvotc(Integer nivelCumplidoEvotc) {
        this.nivelCumplidoEvotc = nivelCumplidoEvotc;
    }

    public boolean getActivoEvotc() {
        return activoEvotc;
    }

    public void setActivoEvotc(boolean activoEvotc) {
        this.activoEvotc = activoEvotc;
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

    public EvlRelevancia getIdeEvrel() {
        return ideEvrel;
    }

    public void setIdeEvrel(EvlRelevancia ideEvrel) {
        this.ideEvrel = ideEvrel;
    }

    public EvlNivelDesarrollo getIdeEvnid() {
        return ideEvnid;
    }

    public void setIdeEvnid(EvlNivelDesarrollo ideEvnid) {
        this.ideEvnid = ideEvnid;
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
        hash += (ideEvotc != null ? ideEvotc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlOtraCompetencia)) {
            return false;
        }
        EvlOtraCompetencia other = (EvlOtraCompetencia) object;
        if ((this.ideEvotc == null && other.ideEvotc != null) || (this.ideEvotc != null && !this.ideEvotc.equals(other.ideEvotc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlOtraCompetencia[ ideEvotc=" + ideEvotc + " ]";
    }
    
}
