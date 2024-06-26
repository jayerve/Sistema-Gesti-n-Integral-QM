/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "spr_detalle_solicitud_puesto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findAll", query = "SELECT s FROM SprDetalleSolicitudPuesto s"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByIdeSpdsp", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.ideSpdsp = :ideSpdsp"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByIdeSucu", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.ideSucu = :ideSucu"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByNroPuestoSolicSpdsp", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.nroPuestoSolicSpdsp = :nroPuestoSolicSpdsp"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByNroPuestoAprobSpdsp", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.nroPuestoAprobSpdsp = :nroPuestoAprobSpdsp"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByValorPuestoSpdsp", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.valorPuestoSpdsp = :valorPuestoSpdsp"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByAprobadoSpdsp", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.aprobadoSpdsp = :aprobadoSpdsp"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByDesiertoSpdsp", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.desiertoSpdsp = :desiertoSpdsp"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByActivoSpdsp", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.activoSpdsp = :activoSpdsp"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByUsuarioIngre", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByFechaIngre", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByHoraIngre", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByUsuarioActua", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByFechaActua", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprDetalleSolicitudPuesto.findByHoraActua", query = "SELECT s FROM SprDetalleSolicitudPuesto s WHERE s.horaActua = :horaActua")})
public class SprDetalleSolicitudPuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spdsp", nullable = false)
    private Integer ideSpdsp;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "nro_puesto_solic_spdsp")
    private Integer nroPuestoSolicSpdsp;
    @Column(name = "nro_puesto_aprob_spdsp")
    private Integer nroPuestoAprobSpdsp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_puesto_spdsp", precision = 12, scale = 2)
    private BigDecimal valorPuestoSpdsp;
    @Column(name = "aprobado_spdsp")
    private Integer aprobadoSpdsp;
    @Column(name = "desierto_spdsp")
    private Integer desiertoSpdsp;
    @Column(name = "activo_spdsp")
    private Boolean activoSpdsp;
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
    @OneToMany(mappedBy = "ideSpdsp")
    private List<SprPerfilCompetencia> sprPerfilCompetenciaList;
    @OneToMany(mappedBy = "ideSpdsp")
    private List<SprPerfilExperiencia> sprPerfilExperienciaList;
    @OneToMany(mappedBy = "ideSpdsp")
    private List<SprPerfilFormacionAcademica> sprPerfilFormacionAcademicaList;
    @JoinColumn(name = "ide_spsop", referencedColumnName = "ide_spsop")
    @ManyToOne
    private SprSolicitudPuesto ideSpsop;
    @JoinColumn(name = "ide_gegca", referencedColumnName = "ide_gegca")
    @ManyToOne
    private GenGrupoCargoArea ideGegca;
    @OneToMany(mappedBy = "ideSpdsp")
    private List<SprResponsableCalificacion> sprResponsableCalificacionList;
    @OneToMany(mappedBy = "ideSpdsp")
    private List<SprCargoContratacion> sprCargoContratacionList;
    @OneToMany(mappedBy = "ideSpdsp")
    private List<SprPostulante> sprPostulanteList;
    @OneToMany(mappedBy = "ideSpdsp")
    private List<SprResultado> sprResultadoList;
    @OneToMany(mappedBy = "ideSpdsp")
    private List<SprCargoPresupuesto> sprCargoPresupuestoList;
    @OneToMany(mappedBy = "ideSpdsp")
    private List<SprCargoDesierto> sprCargoDesiertoList;

    public SprDetalleSolicitudPuesto() {
    }

    public SprDetalleSolicitudPuesto(Integer ideSpdsp) {
        this.ideSpdsp = ideSpdsp;
    }

    public Integer getIdeSpdsp() {
        return ideSpdsp;
    }

    public void setIdeSpdsp(Integer ideSpdsp) {
        this.ideSpdsp = ideSpdsp;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Integer getNroPuestoSolicSpdsp() {
        return nroPuestoSolicSpdsp;
    }

    public void setNroPuestoSolicSpdsp(Integer nroPuestoSolicSpdsp) {
        this.nroPuestoSolicSpdsp = nroPuestoSolicSpdsp;
    }

    public Integer getNroPuestoAprobSpdsp() {
        return nroPuestoAprobSpdsp;
    }

    public void setNroPuestoAprobSpdsp(Integer nroPuestoAprobSpdsp) {
        this.nroPuestoAprobSpdsp = nroPuestoAprobSpdsp;
    }

    public BigDecimal getValorPuestoSpdsp() {
        return valorPuestoSpdsp;
    }

    public void setValorPuestoSpdsp(BigDecimal valorPuestoSpdsp) {
        this.valorPuestoSpdsp = valorPuestoSpdsp;
    }

    public Integer getAprobadoSpdsp() {
        return aprobadoSpdsp;
    }

    public void setAprobadoSpdsp(Integer aprobadoSpdsp) {
        this.aprobadoSpdsp = aprobadoSpdsp;
    }

    public Integer getDesiertoSpdsp() {
        return desiertoSpdsp;
    }

    public void setDesiertoSpdsp(Integer desiertoSpdsp) {
        this.desiertoSpdsp = desiertoSpdsp;
    }

    public Boolean getActivoSpdsp() {
        return activoSpdsp;
    }

    public void setActivoSpdsp(Boolean activoSpdsp) {
        this.activoSpdsp = activoSpdsp;
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

    public List<SprPerfilCompetencia> getSprPerfilCompetenciaList() {
        return sprPerfilCompetenciaList;
    }

    public void setSprPerfilCompetenciaList(List<SprPerfilCompetencia> sprPerfilCompetenciaList) {
        this.sprPerfilCompetenciaList = sprPerfilCompetenciaList;
    }

    public List<SprPerfilExperiencia> getSprPerfilExperienciaList() {
        return sprPerfilExperienciaList;
    }

    public void setSprPerfilExperienciaList(List<SprPerfilExperiencia> sprPerfilExperienciaList) {
        this.sprPerfilExperienciaList = sprPerfilExperienciaList;
    }

    public List<SprPerfilFormacionAcademica> getSprPerfilFormacionAcademicaList() {
        return sprPerfilFormacionAcademicaList;
    }

    public void setSprPerfilFormacionAcademicaList(List<SprPerfilFormacionAcademica> sprPerfilFormacionAcademicaList) {
        this.sprPerfilFormacionAcademicaList = sprPerfilFormacionAcademicaList;
    }

    public SprSolicitudPuesto getIdeSpsop() {
        return ideSpsop;
    }

    public void setIdeSpsop(SprSolicitudPuesto ideSpsop) {
        this.ideSpsop = ideSpsop;
    }

    public GenGrupoCargoArea getIdeGegca() {
        return ideGegca;
    }

    public void setIdeGegca(GenGrupoCargoArea ideGegca) {
        this.ideGegca = ideGegca;
    }

    public List<SprResponsableCalificacion> getSprResponsableCalificacionList() {
        return sprResponsableCalificacionList;
    }

    public void setSprResponsableCalificacionList(List<SprResponsableCalificacion> sprResponsableCalificacionList) {
        this.sprResponsableCalificacionList = sprResponsableCalificacionList;
    }

    public List<SprCargoContratacion> getSprCargoContratacionList() {
        return sprCargoContratacionList;
    }

    public void setSprCargoContratacionList(List<SprCargoContratacion> sprCargoContratacionList) {
        this.sprCargoContratacionList = sprCargoContratacionList;
    }

    public List<SprPostulante> getSprPostulanteList() {
        return sprPostulanteList;
    }

    public void setSprPostulanteList(List<SprPostulante> sprPostulanteList) {
        this.sprPostulanteList = sprPostulanteList;
    }

    public List<SprResultado> getSprResultadoList() {
        return sprResultadoList;
    }

    public void setSprResultadoList(List<SprResultado> sprResultadoList) {
        this.sprResultadoList = sprResultadoList;
    }

    public List<SprCargoPresupuesto> getSprCargoPresupuestoList() {
        return sprCargoPresupuestoList;
    }

    public void setSprCargoPresupuestoList(List<SprCargoPresupuesto> sprCargoPresupuestoList) {
        this.sprCargoPresupuestoList = sprCargoPresupuestoList;
    }

    public List<SprCargoDesierto> getSprCargoDesiertoList() {
        return sprCargoDesiertoList;
    }

    public void setSprCargoDesiertoList(List<SprCargoDesierto> sprCargoDesiertoList) {
        this.sprCargoDesiertoList = sprCargoDesiertoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpdsp != null ? ideSpdsp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprDetalleSolicitudPuesto)) {
            return false;
        }
        SprDetalleSolicitudPuesto other = (SprDetalleSolicitudPuesto) object;
        if ((this.ideSpdsp == null && other.ideSpdsp != null) || (this.ideSpdsp != null && !this.ideSpdsp.equals(other.ideSpdsp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprDetalleSolicitudPuesto[ ideSpdsp=" + ideSpdsp + " ]";
    }
    
}
