/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "evl_factor_evaluacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlFactorEvaluacion.findAll", query = "SELECT e FROM EvlFactorEvaluacion e"),
    @NamedQuery(name = "EvlFactorEvaluacion.findByIdeEvfae", query = "SELECT e FROM EvlFactorEvaluacion e WHERE e.ideEvfae = :ideEvfae"),
    @NamedQuery(name = "EvlFactorEvaluacion.findByDetalleEvfae", query = "SELECT e FROM EvlFactorEvaluacion e WHERE e.detalleEvfae = :detalleEvfae"),
    @NamedQuery(name = "EvlFactorEvaluacion.findByActivoEvfae", query = "SELECT e FROM EvlFactorEvaluacion e WHERE e.activoEvfae = :activoEvfae"),
    @NamedQuery(name = "EvlFactorEvaluacion.findByUsuarioIngre", query = "SELECT e FROM EvlFactorEvaluacion e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlFactorEvaluacion.findByFechaIngre", query = "SELECT e FROM EvlFactorEvaluacion e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlFactorEvaluacion.findByUsuarioActua", query = "SELECT e FROM EvlFactorEvaluacion e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlFactorEvaluacion.findByFechaActua", query = "SELECT e FROM EvlFactorEvaluacion e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlFactorEvaluacion.findByHoraIngre", query = "SELECT e FROM EvlFactorEvaluacion e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlFactorEvaluacion.findByHoraActua", query = "SELECT e FROM EvlFactorEvaluacion e WHERE e.horaActua = :horaActua")})
public class EvlFactorEvaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evfae", nullable = false)
    private Integer ideEvfae;
    @Size(max = 100)
    @Column(name = "detalle_evfae", length = 100)
    private String detalleEvfae;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evfae", nullable = false)
    private boolean activoEvfae;
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
    @OneToMany(mappedBy = "ideEvfae")
    private List<EvlCompetenciaRelevancia> evlCompetenciaRelevanciaList;
    @OneToMany(mappedBy = "ideEvfae")
    private List<EvlOtraCompetencia> evlOtraCompetenciaList;
    @OneToMany(mappedBy = "ideEvfae")
    private List<CmpFactorCompetencia> cmpFactorCompetenciaList;
    @OneToMany(mappedBy = "ideEvfae")
    private List<EvlGrupoFactor> evlGrupoFactorList;
    @OneToMany(mappedBy = "ideEvfae")
    private List<EvlResultado> evlResultadoList;
    @OneToMany(mappedBy = "ideEvfae")
    private List<EvlActividadPuesto> evlActividadPuestoList;

    public EvlFactorEvaluacion() {
    }

    public EvlFactorEvaluacion(Integer ideEvfae) {
        this.ideEvfae = ideEvfae;
    }

    public EvlFactorEvaluacion(Integer ideEvfae, boolean activoEvfae) {
        this.ideEvfae = ideEvfae;
        this.activoEvfae = activoEvfae;
    }

    public Integer getIdeEvfae() {
        return ideEvfae;
    }

    public void setIdeEvfae(Integer ideEvfae) {
        this.ideEvfae = ideEvfae;
    }

    public String getDetalleEvfae() {
        return detalleEvfae;
    }

    public void setDetalleEvfae(String detalleEvfae) {
        this.detalleEvfae = detalleEvfae;
    }

    public boolean getActivoEvfae() {
        return activoEvfae;
    }

    public void setActivoEvfae(boolean activoEvfae) {
        this.activoEvfae = activoEvfae;
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

    public List<EvlCompetenciaRelevancia> getEvlCompetenciaRelevanciaList() {
        return evlCompetenciaRelevanciaList;
    }

    public void setEvlCompetenciaRelevanciaList(List<EvlCompetenciaRelevancia> evlCompetenciaRelevanciaList) {
        this.evlCompetenciaRelevanciaList = evlCompetenciaRelevanciaList;
    }

    public List<EvlOtraCompetencia> getEvlOtraCompetenciaList() {
        return evlOtraCompetenciaList;
    }

    public void setEvlOtraCompetenciaList(List<EvlOtraCompetencia> evlOtraCompetenciaList) {
        this.evlOtraCompetenciaList = evlOtraCompetenciaList;
    }

    public List<CmpFactorCompetencia> getCmpFactorCompetenciaList() {
        return cmpFactorCompetenciaList;
    }

    public void setCmpFactorCompetenciaList(List<CmpFactorCompetencia> cmpFactorCompetenciaList) {
        this.cmpFactorCompetenciaList = cmpFactorCompetenciaList;
    }

    public List<EvlGrupoFactor> getEvlGrupoFactorList() {
        return evlGrupoFactorList;
    }

    public void setEvlGrupoFactorList(List<EvlGrupoFactor> evlGrupoFactorList) {
        this.evlGrupoFactorList = evlGrupoFactorList;
    }

    public List<EvlResultado> getEvlResultadoList() {
        return evlResultadoList;
    }

    public void setEvlResultadoList(List<EvlResultado> evlResultadoList) {
        this.evlResultadoList = evlResultadoList;
    }

    public List<EvlActividadPuesto> getEvlActividadPuestoList() {
        return evlActividadPuestoList;
    }

    public void setEvlActividadPuestoList(List<EvlActividadPuesto> evlActividadPuestoList) {
        this.evlActividadPuestoList = evlActividadPuestoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEvfae != null ? ideEvfae.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlFactorEvaluacion)) {
            return false;
        }
        EvlFactorEvaluacion other = (EvlFactorEvaluacion) object;
        if ((this.ideEvfae == null && other.ideEvfae != null) || (this.ideEvfae != null && !this.ideEvfae.equals(other.ideEvfae))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlFactorEvaluacion[ ideEvfae=" + ideEvfae + " ]";
    }
    
}
