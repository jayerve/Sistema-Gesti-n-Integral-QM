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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "cmp_detalle_competencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CmpDetalleCompetencia.findAll", query = "SELECT c FROM CmpDetalleCompetencia c"),
    @NamedQuery(name = "CmpDetalleCompetencia.findByIdeCmdec", query = "SELECT c FROM CmpDetalleCompetencia c WHERE c.ideCmdec = :ideCmdec"),
    @NamedQuery(name = "CmpDetalleCompetencia.findByDetalleCmdec", query = "SELECT c FROM CmpDetalleCompetencia c WHERE c.detalleCmdec = :detalleCmdec"),
    @NamedQuery(name = "CmpDetalleCompetencia.findByActivoCmdec", query = "SELECT c FROM CmpDetalleCompetencia c WHERE c.activoCmdec = :activoCmdec"),
    @NamedQuery(name = "CmpDetalleCompetencia.findByUsuarioIngre", query = "SELECT c FROM CmpDetalleCompetencia c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CmpDetalleCompetencia.findByFechaIngre", query = "SELECT c FROM CmpDetalleCompetencia c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CmpDetalleCompetencia.findByUsuarioActua", query = "SELECT c FROM CmpDetalleCompetencia c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CmpDetalleCompetencia.findByFechaActua", query = "SELECT c FROM CmpDetalleCompetencia c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CmpDetalleCompetencia.findByHoraIngre", query = "SELECT c FROM CmpDetalleCompetencia c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CmpDetalleCompetencia.findByHoraActua", query = "SELECT c FROM CmpDetalleCompetencia c WHERE c.horaActua = :horaActua")})
public class CmpDetalleCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cmdec", nullable = false)
    private Integer ideCmdec;
    @Size(max = 4000)
    @Column(name = "detalle_cmdec", length = 4000)
    private String detalleCmdec;
    @Column(name = "activo_cmdec")
    private Boolean activoCmdec;
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
    @OneToMany(mappedBy = "ideCmdec")
    private List<SprPerfilCompetencia> sprPerfilCompetenciaList;
    @OneToMany(mappedBy = "ideCmdec")
    private List<EvlCompetenciaRelevancia> evlCompetenciaRelevanciaList;
    @OneToMany(mappedBy = "ideCmdec")
    private List<SprPerfilExperiencia> sprPerfilExperienciaList;
    @JoinColumn(name = "ide_gegca", referencedColumnName = "ide_gegca")
    @ManyToOne
    private GenGrupoCargoArea ideGegca;
    @JoinColumn(name = "ide_cmfac", referencedColumnName = "ide_cmfac")
    @ManyToOne
    private CmpFactorCompetencia ideCmfac;
    @OneToMany(mappedBy = "ideCmdec")
    private List<EvlOtraCompetencia> evlOtraCompetenciaList;
    @OneToMany(mappedBy = "ideCmdec")
    private List<SprPerfilFormacionAcademica> sprPerfilFormacionAcademicaList;
    @OneToMany(mappedBy = "ideCmdec")
    private List<CppCapacitaRequerida> cppCapacitaRequeridaList;
    @OneToMany(mappedBy = "ideCmdec")
    private List<EvlActividadPuesto> evlActividadPuestoList;

    public CmpDetalleCompetencia() {
    }

    public CmpDetalleCompetencia(Integer ideCmdec) {
        this.ideCmdec = ideCmdec;
    }

    public Integer getIdeCmdec() {
        return ideCmdec;
    }

    public void setIdeCmdec(Integer ideCmdec) {
        this.ideCmdec = ideCmdec;
    }

    public String getDetalleCmdec() {
        return detalleCmdec;
    }

    public void setDetalleCmdec(String detalleCmdec) {
        this.detalleCmdec = detalleCmdec;
    }

    public Boolean getActivoCmdec() {
        return activoCmdec;
    }

    public void setActivoCmdec(Boolean activoCmdec) {
        this.activoCmdec = activoCmdec;
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

    public List<SprPerfilCompetencia> getSprPerfilCompetenciaList() {
        return sprPerfilCompetenciaList;
    }

    public void setSprPerfilCompetenciaList(List<SprPerfilCompetencia> sprPerfilCompetenciaList) {
        this.sprPerfilCompetenciaList = sprPerfilCompetenciaList;
    }

    public List<EvlCompetenciaRelevancia> getEvlCompetenciaRelevanciaList() {
        return evlCompetenciaRelevanciaList;
    }

    public void setEvlCompetenciaRelevanciaList(List<EvlCompetenciaRelevancia> evlCompetenciaRelevanciaList) {
        this.evlCompetenciaRelevanciaList = evlCompetenciaRelevanciaList;
    }

    public List<SprPerfilExperiencia> getSprPerfilExperienciaList() {
        return sprPerfilExperienciaList;
    }

    public void setSprPerfilExperienciaList(List<SprPerfilExperiencia> sprPerfilExperienciaList) {
        this.sprPerfilExperienciaList = sprPerfilExperienciaList;
    }

    public GenGrupoCargoArea getIdeGegca() {
        return ideGegca;
    }

    public void setIdeGegca(GenGrupoCargoArea ideGegca) {
        this.ideGegca = ideGegca;
    }

    public CmpFactorCompetencia getIdeCmfac() {
        return ideCmfac;
    }

    public void setIdeCmfac(CmpFactorCompetencia ideCmfac) {
        this.ideCmfac = ideCmfac;
    }

    public List<EvlOtraCompetencia> getEvlOtraCompetenciaList() {
        return evlOtraCompetenciaList;
    }

    public void setEvlOtraCompetenciaList(List<EvlOtraCompetencia> evlOtraCompetenciaList) {
        this.evlOtraCompetenciaList = evlOtraCompetenciaList;
    }

    public List<SprPerfilFormacionAcademica> getSprPerfilFormacionAcademicaList() {
        return sprPerfilFormacionAcademicaList;
    }

    public void setSprPerfilFormacionAcademicaList(List<SprPerfilFormacionAcademica> sprPerfilFormacionAcademicaList) {
        this.sprPerfilFormacionAcademicaList = sprPerfilFormacionAcademicaList;
    }

    public List<CppCapacitaRequerida> getCppCapacitaRequeridaList() {
        return cppCapacitaRequeridaList;
    }

    public void setCppCapacitaRequeridaList(List<CppCapacitaRequerida> cppCapacitaRequeridaList) {
        this.cppCapacitaRequeridaList = cppCapacitaRequeridaList;
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
        hash += (ideCmdec != null ? ideCmdec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CmpDetalleCompetencia)) {
            return false;
        }
        CmpDetalleCompetencia other = (CmpDetalleCompetencia) object;
        if ((this.ideCmdec == null && other.ideCmdec != null) || (this.ideCmdec != null && !this.ideCmdec.equals(other.ideCmdec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CmpDetalleCompetencia[ ideCmdec=" + ideCmdec + " ]";
    }
    
}
