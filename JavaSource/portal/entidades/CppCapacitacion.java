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
@Table(name = "cpp_capacitacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppCapacitacion.findAll", query = "SELECT c FROM CppCapacitacion c"),
    @NamedQuery(name = "CppCapacitacion.findByIdeCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.ideCpcap = :ideCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByFechaInicioCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.fechaInicioCpcap = :fechaInicioCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByFechaFinCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.fechaFinCpcap = :fechaFinCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByDuracionCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.duracionCpcap = :duracionCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByNroCertificacionCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.nroCertificacionCpcap = :nroCertificacionCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByNroDisponibilidadCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.nroDisponibilidadCpcap = :nroDisponibilidadCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByFechaCertificacionCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.fechaCertificacionCpcap = :fechaCertificacionCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByObservacionCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.observacionCpcap = :observacionCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByValorInversionCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.valorInversionCpcap = :valorInversionCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByActivoCpcap", query = "SELECT c FROM CppCapacitacion c WHERE c.activoCpcap = :activoCpcap"),
    @NamedQuery(name = "CppCapacitacion.findByUsuarioIngre", query = "SELECT c FROM CppCapacitacion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppCapacitacion.findByFechaIngre", query = "SELECT c FROM CppCapacitacion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppCapacitacion.findByHoraIngre", query = "SELECT c FROM CppCapacitacion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppCapacitacion.findByUsuarioActua", query = "SELECT c FROM CppCapacitacion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppCapacitacion.findByFechaActua", query = "SELECT c FROM CppCapacitacion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppCapacitacion.findByHoraActua", query = "SELECT c FROM CppCapacitacion c WHERE c.horaActua = :horaActua")})
public class CppCapacitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpcap", nullable = false)
    private Integer ideCpcap;
    @Column(name = "fecha_inicio_cpcap")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioCpcap;
    @Column(name = "fecha_fin_cpcap")
    @Temporal(TemporalType.DATE)
    private Date fechaFinCpcap;
    @Column(name = "duracion_cpcap")
    private Integer duracionCpcap;
    @Size(max = 50)
    @Column(name = "nro_certificacion_cpcap", length = 50)
    private String nroCertificacionCpcap;
    @Size(max = 50)
    @Column(name = "nro_disponibilidad_cpcap", length = 50)
    private String nroDisponibilidadCpcap;
    @Column(name = "fecha_certificacion_cpcap")
    @Temporal(TemporalType.DATE)
    private Date fechaCertificacionCpcap;
    @Size(max = 4000)
    @Column(name = "observacion_cpcap", length = 4000)
    private String observacionCpcap;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_inversion_cpcap", precision = 12, scale = 2)
    private BigDecimal valorInversionCpcap;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpcap", nullable = false)
    private boolean activoCpcap;
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
    @OneToMany(mappedBy = "ideCpcap")
    private List<CppEvaluacionEvento> cppEvaluacionEventoList;
    @OneToMany(mappedBy = "ideCpcap")
    private List<CppPagoCapacitacion> cppPagoCapacitacionList;
    @JoinColumn(name = "ide_getpr", referencedColumnName = "ide_getpr")
    @ManyToOne
    private GenTipoPeriodo ideGetpr;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;
    @JoinColumn(name = "ide_cptcc", referencedColumnName = "ide_cptcc")
    @ManyToOne
    private CppTipoCursoCapacita ideCptcc;
    @JoinColumn(name = "ide_cpprm", referencedColumnName = "ide_cpprm")
    @ManyToOne
    private CppPresupuestoMensual ideCpprm;
    @JoinColumn(name = "ide_cpplc", referencedColumnName = "ide_cpplc")
    @ManyToOne
    private CppPlanCapacitacion ideCpplc;
    @JoinColumn(name = "ide_cpmoc", referencedColumnName = "ide_cpmoc")
    @ManyToOne
    private CppModalidadCapacita ideCpmoc;
    @JoinColumn(name = "ide_cpluc", referencedColumnName = "ide_cpluc")
    @ManyToOne
    private CppLugarCapacita ideCpluc;
    @JoinColumn(name = "ide_cpesc", referencedColumnName = "ide_cpesc")
    @ManyToOne
    private CppEstadoCapacitacion ideCpesc;
    @OneToMany(mappedBy = "ideCpcap")
    private List<CppLogistica> cppLogisticaList;
    @OneToMany(mappedBy = "ideCpcap")
    private List<CppParticipantes> cppParticipantesList;
    @OneToMany(mappedBy = "ideCpcap")
    private List<CppInstructores> cppInstructoresList;
    @OneToMany(mappedBy = "ideCpcap")
    private List<CppEvaluacionTaller> cppEvaluacionTallerList;
    @OneToMany(mappedBy = "ideCpcap")
    private List<CppEvaluacionJefe> cppEvaluacionJefeList;
    @OneToMany(mappedBy = "ideCpcap")
    private List<CppEvaluacionPersepcion> cppEvaluacionPersepcionList;
    @OneToMany(mappedBy = "ideCpcap")
    private List<CppEjecucion> cppEjecucionList;

    public CppCapacitacion() {
    }

    public CppCapacitacion(Integer ideCpcap) {
        this.ideCpcap = ideCpcap;
    }

    public CppCapacitacion(Integer ideCpcap, boolean activoCpcap) {
        this.ideCpcap = ideCpcap;
        this.activoCpcap = activoCpcap;
    }

    public Integer getIdeCpcap() {
        return ideCpcap;
    }

    public void setIdeCpcap(Integer ideCpcap) {
        this.ideCpcap = ideCpcap;
    }

    public Date getFechaInicioCpcap() {
        return fechaInicioCpcap;
    }

    public void setFechaInicioCpcap(Date fechaInicioCpcap) {
        this.fechaInicioCpcap = fechaInicioCpcap;
    }

    public Date getFechaFinCpcap() {
        return fechaFinCpcap;
    }

    public void setFechaFinCpcap(Date fechaFinCpcap) {
        this.fechaFinCpcap = fechaFinCpcap;
    }

    public Integer getDuracionCpcap() {
        return duracionCpcap;
    }

    public void setDuracionCpcap(Integer duracionCpcap) {
        this.duracionCpcap = duracionCpcap;
    }

    public String getNroCertificacionCpcap() {
        return nroCertificacionCpcap;
    }

    public void setNroCertificacionCpcap(String nroCertificacionCpcap) {
        this.nroCertificacionCpcap = nroCertificacionCpcap;
    }

    public String getNroDisponibilidadCpcap() {
        return nroDisponibilidadCpcap;
    }

    public void setNroDisponibilidadCpcap(String nroDisponibilidadCpcap) {
        this.nroDisponibilidadCpcap = nroDisponibilidadCpcap;
    }

    public Date getFechaCertificacionCpcap() {
        return fechaCertificacionCpcap;
    }

    public void setFechaCertificacionCpcap(Date fechaCertificacionCpcap) {
        this.fechaCertificacionCpcap = fechaCertificacionCpcap;
    }

    public String getObservacionCpcap() {
        return observacionCpcap;
    }

    public void setObservacionCpcap(String observacionCpcap) {
        this.observacionCpcap = observacionCpcap;
    }

    public BigDecimal getValorInversionCpcap() {
        return valorInversionCpcap;
    }

    public void setValorInversionCpcap(BigDecimal valorInversionCpcap) {
        this.valorInversionCpcap = valorInversionCpcap;
    }

    public boolean getActivoCpcap() {
        return activoCpcap;
    }

    public void setActivoCpcap(boolean activoCpcap) {
        this.activoCpcap = activoCpcap;
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

    public List<CppEvaluacionEvento> getCppEvaluacionEventoList() {
        return cppEvaluacionEventoList;
    }

    public void setCppEvaluacionEventoList(List<CppEvaluacionEvento> cppEvaluacionEventoList) {
        this.cppEvaluacionEventoList = cppEvaluacionEventoList;
    }

    public List<CppPagoCapacitacion> getCppPagoCapacitacionList() {
        return cppPagoCapacitacionList;
    }

    public void setCppPagoCapacitacionList(List<CppPagoCapacitacion> cppPagoCapacitacionList) {
        this.cppPagoCapacitacionList = cppPagoCapacitacionList;
    }

    public GenTipoPeriodo getIdeGetpr() {
        return ideGetpr;
    }

    public void setIdeGetpr(GenTipoPeriodo ideGetpr) {
        this.ideGetpr = ideGetpr;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    public CppTipoCursoCapacita getIdeCptcc() {
        return ideCptcc;
    }

    public void setIdeCptcc(CppTipoCursoCapacita ideCptcc) {
        this.ideCptcc = ideCptcc;
    }

    public CppPresupuestoMensual getIdeCpprm() {
        return ideCpprm;
    }

    public void setIdeCpprm(CppPresupuestoMensual ideCpprm) {
        this.ideCpprm = ideCpprm;
    }

    public CppPlanCapacitacion getIdeCpplc() {
        return ideCpplc;
    }

    public void setIdeCpplc(CppPlanCapacitacion ideCpplc) {
        this.ideCpplc = ideCpplc;
    }

    public CppModalidadCapacita getIdeCpmoc() {
        return ideCpmoc;
    }

    public void setIdeCpmoc(CppModalidadCapacita ideCpmoc) {
        this.ideCpmoc = ideCpmoc;
    }

    public CppLugarCapacita getIdeCpluc() {
        return ideCpluc;
    }

    public void setIdeCpluc(CppLugarCapacita ideCpluc) {
        this.ideCpluc = ideCpluc;
    }

    public CppEstadoCapacitacion getIdeCpesc() {
        return ideCpesc;
    }

    public void setIdeCpesc(CppEstadoCapacitacion ideCpesc) {
        this.ideCpesc = ideCpesc;
    }

    public List<CppLogistica> getCppLogisticaList() {
        return cppLogisticaList;
    }

    public void setCppLogisticaList(List<CppLogistica> cppLogisticaList) {
        this.cppLogisticaList = cppLogisticaList;
    }

    public List<CppParticipantes> getCppParticipantesList() {
        return cppParticipantesList;
    }

    public void setCppParticipantesList(List<CppParticipantes> cppParticipantesList) {
        this.cppParticipantesList = cppParticipantesList;
    }

    public List<CppInstructores> getCppInstructoresList() {
        return cppInstructoresList;
    }

    public void setCppInstructoresList(List<CppInstructores> cppInstructoresList) {
        this.cppInstructoresList = cppInstructoresList;
    }

    public List<CppEvaluacionTaller> getCppEvaluacionTallerList() {
        return cppEvaluacionTallerList;
    }

    public void setCppEvaluacionTallerList(List<CppEvaluacionTaller> cppEvaluacionTallerList) {
        this.cppEvaluacionTallerList = cppEvaluacionTallerList;
    }

    public List<CppEvaluacionJefe> getCppEvaluacionJefeList() {
        return cppEvaluacionJefeList;
    }

    public void setCppEvaluacionJefeList(List<CppEvaluacionJefe> cppEvaluacionJefeList) {
        this.cppEvaluacionJefeList = cppEvaluacionJefeList;
    }

    public List<CppEvaluacionPersepcion> getCppEvaluacionPersepcionList() {
        return cppEvaluacionPersepcionList;
    }

    public void setCppEvaluacionPersepcionList(List<CppEvaluacionPersepcion> cppEvaluacionPersepcionList) {
        this.cppEvaluacionPersepcionList = cppEvaluacionPersepcionList;
    }

    public List<CppEjecucion> getCppEjecucionList() {
        return cppEjecucionList;
    }

    public void setCppEjecucionList(List<CppEjecucion> cppEjecucionList) {
        this.cppEjecucionList = cppEjecucionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpcap != null ? ideCpcap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppCapacitacion)) {
            return false;
        }
        CppCapacitacion other = (CppCapacitacion) object;
        if ((this.ideCpcap == null && other.ideCpcap != null) || (this.ideCpcap != null && !this.ideCpcap.equals(other.ideCpcap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppCapacitacion[ ideCpcap=" + ideCpcap + " ]";
    }
    
}
