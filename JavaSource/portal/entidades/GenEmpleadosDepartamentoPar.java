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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "gen_empleados_departamento_par", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findAll", query = "SELECT g FROM GenEmpleadosDepartamentoPar g"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByIdeGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.ideGeedp = :ideGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaGeedp = :fechaGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaFinctrGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaFinctrGeedp = :fechaFinctrGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByRmuGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.rmuGeedp = :rmuGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByAjusteSueldoGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.ajusteSueldoGeedp = :ajusteSueldoGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaEncargoGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaEncargoGeedp = :fechaEncargoGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaAjusteGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaAjusteGeedp = :fechaAjusteGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaLiquidacionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaLiquidacionGeedp = :fechaLiquidacionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByLiquidacionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.liquidacionGeedp = :liquidacionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaEncargoFinGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaEncargoFinGeedp = :fechaEncargoFinGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findBySueldoSubrogaGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.sueldoSubrogaGeedp = :sueldoSubrogaGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByEjecutoLiquidacionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.ejecutoLiquidacionGeedp = :ejecutoLiquidacionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByObservacionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.observacionGeedp = :observacionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByUsuarioIngre", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaIngre", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByUsuarioActua", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByFechaActua", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByHoraIngre", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByHoraActua", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByActivoGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.activoGeedp = :activoGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByLineaSupervicionGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.lineaSupervicionGeedp = :lineaSupervicionGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByAcumulaFondosGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.acumulaFondosGeedp = :acumulaFondosGeedp"),
    @NamedQuery(name = "GenEmpleadosDepartamentoPar.findByControlAsistenciaGeedp", query = "SELECT g FROM GenEmpleadosDepartamentoPar g WHERE g.controlAsistenciaGeedp = :controlAsistenciaGeedp")})
public class GenEmpleadosDepartamentoPar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geedp", nullable = false)
    private Integer ideGeedp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_geedp", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaGeedp;
    @Column(name = "fecha_finctr_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaFinctrGeedp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "rmu_geedp", nullable = false, precision = 12, scale = 3)
    private BigDecimal rmuGeedp;
    @Column(name = "ajuste_sueldo_geedp", precision = 12, scale = 2)
    private BigDecimal ajusteSueldoGeedp;
    @Column(name = "fecha_encargo_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaEncargoGeedp;
    @Column(name = "fecha_ajuste_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaAjusteGeedp;
    @Column(name = "fecha_liquidacion_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaLiquidacionGeedp;
    @Column(name = "liquidacion_geedp")
    private Integer liquidacionGeedp;
    @Column(name = "fecha_encargo_fin_geedp")
    @Temporal(TemporalType.DATE)
    private Date fechaEncargoFinGeedp;
    @Column(name = "sueldo_subroga_geedp", precision = 12, scale = 3)
    private BigDecimal sueldoSubrogaGeedp;
    @Column(name = "ejecuto_liquidacion_geedp")
    private Integer ejecutoLiquidacionGeedp;
    @Size(max = 4000)
    @Column(name = "observacion_geedp", length = 4000)
    private String observacionGeedp;
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
    @Column(name = "activo_geedp")
    private Boolean activoGeedp;
    @Column(name = "linea_supervicion_geedp")
    private Boolean lineaSupervicionGeedp;
    @Column(name = "acumula_fondos_geedp")
    private Boolean acumulaFondosGeedp;
    @Column(name = "control_asistencia_geedp")
    private Boolean controlAsistenciaGeedp;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SprPresupuestoPuesto> sprPresupuestoPuestoList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<SprPresupuestoPuesto> sprPresupuestoPuestoList1;
    @OneToMany(mappedBy = "ideGeedp")
    private List<AsiPermisosVacacionHext> asiPermisosVacacionHextList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<AsiPermisosVacacionHext> asiPermisosVacacionHextList1;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private List<AsiPermisosVacacionHext> asiPermisosVacacionHextList2;
    @OneToMany(mappedBy = "genIdeGeedp3")
    private List<AsiPermisosVacacionHext> asiPermisosVacacionHextList3;
    @OneToMany(mappedBy = "ideGeedp")
    private List<CppCapacitacion> cppCapacitacionList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SriProyeccionIngres> sriProyeccionIngresList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<EvlEvaluadores> evlEvaluadoresList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList1;
    @OneToMany(mappedBy = "ideGeedp")
    private List<TesComprobantePago> tesComprobantePagoList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<CppParticipantes> cppParticipantesList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<PreTramite> preTramiteList;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private List<PreTramite> preTramiteList1;
    @OneToMany(mappedBy = "ideGeedp")
    private List<PreTramite> preTramiteList2;
    @OneToMany(mappedBy = "genIdeGeedp3")
    private List<PreTramite> preTramiteList3;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SaoCustodio> saoCustodioList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<AsiValidaNomina> asiValidaNominaList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<PreComparecienteContrato> preComparecienteContratoList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList1;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeedp")
    private List<NrhAnticipo> nrhAnticipoList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<NrhAnticipo> nrhAnticipoList1;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private List<NrhAnticipo> nrhAnticipoList2;
    @OneToMany(mappedBy = "genIdeGeedp3")
    private List<NrhAnticipo> nrhAnticipoList3;
    @OneToMany(mappedBy = "ideGeedp")
    private List<AdqSolicitudCompra> adqSolicitudCompraList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<AfiCustodio> afiCustodioList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<ContViajeros> contViajerosList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SaoEvaluacionPosiciograma> saoEvaluacionPosiciogramaList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SprResponsableCalificacion> sprResponsableCalificacionList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<BodtConceptoEgreso> bodtConceptoEgresoList;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private List<BodtConceptoEgreso> bodtConceptoEgresoList1;
    @OneToMany(mappedBy = "ideGeedp")
    private List<BodtConceptoEgreso> bodtConceptoEgresoList2;
    @JoinColumn(name = "ide_gttsi", referencedColumnName = "ide_gttsi", nullable = false)
    @ManyToOne(optional = false)
    private GthTipoSindicato ideGttsi;
    @JoinColumn(name = "ide_gttem", referencedColumnName = "ide_gttem", nullable = false)
    @ManyToOne(optional = false)
    private GthTipoEmpleado ideGttem;
    @JoinColumn(name = "ide_gttco", referencedColumnName = "ide_gttco", nullable = false)
    @ManyToOne(optional = false)
    private GthTipoContrato ideGttco;
    @JoinColumn(name = "ide_gtgre", referencedColumnName = "ide_gtgre")
    @ManyToOne
    private GthGrupoEmpleado ideGtgre;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp", nullable = false)
    @ManyToOne(optional = false)
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_getiv", referencedColumnName = "ide_getiv")
    @ManyToOne
    private GenTipoVinculacion ideGetiv;
    @JoinColumns({
        @JoinColumn(name = "ide_gepgc", referencedColumnName = "ide_gepgc", nullable = false),
        @JoinColumn(name = "ide_gegro", referencedColumnName = "ide_gegro", nullable = false),
        @JoinColumn(name = "ide_gecaf", referencedColumnName = "ide_gecaf", nullable = false),
        @JoinColumn(name = "ide_sucu", referencedColumnName = "ide_sucu", nullable = false),
        @JoinColumn(name = "ide_gedep", referencedColumnName = "ide_gedep", nullable = false),
        @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare", nullable = false)})
    @ManyToOne(optional = false)
    private GenPartidaGrupoCargo genPartidaGrupoCargo;
    @JoinColumns({
        @JoinColumn(name = "gen_ide_gegro", referencedColumnName = "ide_gegro"),
        @JoinColumn(name = "gen_ide_gecaf", referencedColumnName = "ide_gecaf")})
    @ManyToOne
    private GenGrupoCargo genGrupoCargo;
    @JoinColumn(name = "ide_geded", referencedColumnName = "ide_geded")
    @ManyToOne
    private GenDetalleEmpleadoDepartame ideGeded;
    @JoinColumn(name = "ide_gecae", referencedColumnName = "ide_gecae")
    @ManyToOne
    private GenCategoriaEstatus ideGecae;
    @OneToMany(mappedBy = "ideGeedp")
    private List<NrhGarante> nrhGaranteList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SprSolicitudAprobacion> sprSolicitudAprobacionList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<SprSolicitudAprobacion> sprSolicitudAprobacionList1;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SaoCertificadoExterno> saoCertificadoExternoList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<ContServicioSuministro> contServicioSuministroList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<CppEvaluacionJefe> cppEvaluacionJefeList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<PreDocumentoHabilitante> preDocumentoHabilitanteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeedp")
    private List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList1;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList2;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SprSolicitudPuesto> sprSolicitudPuestoList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<SprSolicitudPuesto> sprSolicitudPuestoList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeedp")
    private List<NrhDetalleRol> nrhDetalleRolList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SaoFichaMedica> saoFichaMedicaList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<PreResponsableContratacion> preResponsableContratacionList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<SaoMatrizRiesgo> saoMatrizRiesgoList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<SaoMatrizRiesgo> saoMatrizRiesgoList1;
    @OneToMany(mappedBy = "ideGeedp")
    private List<EvlDesempenio> evlDesempenioList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<ContResponsableConvenio> contResponsableConvenioList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<PreAdministradorContrato> preAdministradorContratoList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<BisEntrevista> bisEntrevistaList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<BisEntrevista> bisEntrevistaList1;
    @OneToMany(mappedBy = "ideGeedp")
    private List<GthViaticos> gthViaticosList;
    @OneToMany(mappedBy = "genIdeGeedp")
    private List<GthViaticos> gthViaticosList1;
    @OneToMany(mappedBy = "genIdeGeedp3")
    private List<GthViaticos> gthViaticosList2;
    @OneToMany(mappedBy = "genIdeGeedp2")
    private List<GthViaticos> gthViaticosList3;
    @OneToMany(mappedBy = "ideGeedp")
    private List<AsiValidaAsistencia> asiValidaAsistenciaList;
    @OneToMany(mappedBy = "ideGeedp")
    private List<NrhRetencionJudicial> nrhRetencionJudicialList;

    public GenEmpleadosDepartamentoPar() {
    }

    public GenEmpleadosDepartamentoPar(Integer ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenEmpleadosDepartamentoPar(Integer ideGeedp, Date fechaGeedp, BigDecimal rmuGeedp) {
        this.ideGeedp = ideGeedp;
        this.fechaGeedp = fechaGeedp;
        this.rmuGeedp = rmuGeedp;
    }

    public Integer getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(Integer ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public Date getFechaGeedp() {
        return fechaGeedp;
    }

    public void setFechaGeedp(Date fechaGeedp) {
        this.fechaGeedp = fechaGeedp;
    }

    public Date getFechaFinctrGeedp() {
        return fechaFinctrGeedp;
    }

    public void setFechaFinctrGeedp(Date fechaFinctrGeedp) {
        this.fechaFinctrGeedp = fechaFinctrGeedp;
    }

    public BigDecimal getRmuGeedp() {
        return rmuGeedp;
    }

    public void setRmuGeedp(BigDecimal rmuGeedp) {
        this.rmuGeedp = rmuGeedp;
    }

    public BigDecimal getAjusteSueldoGeedp() {
        return ajusteSueldoGeedp;
    }

    public void setAjusteSueldoGeedp(BigDecimal ajusteSueldoGeedp) {
        this.ajusteSueldoGeedp = ajusteSueldoGeedp;
    }

    public Date getFechaEncargoGeedp() {
        return fechaEncargoGeedp;
    }

    public void setFechaEncargoGeedp(Date fechaEncargoGeedp) {
        this.fechaEncargoGeedp = fechaEncargoGeedp;
    }

    public Date getFechaAjusteGeedp() {
        return fechaAjusteGeedp;
    }

    public void setFechaAjusteGeedp(Date fechaAjusteGeedp) {
        this.fechaAjusteGeedp = fechaAjusteGeedp;
    }

    public Date getFechaLiquidacionGeedp() {
        return fechaLiquidacionGeedp;
    }

    public void setFechaLiquidacionGeedp(Date fechaLiquidacionGeedp) {
        this.fechaLiquidacionGeedp = fechaLiquidacionGeedp;
    }

    public Integer getLiquidacionGeedp() {
        return liquidacionGeedp;
    }

    public void setLiquidacionGeedp(Integer liquidacionGeedp) {
        this.liquidacionGeedp = liquidacionGeedp;
    }

    public Date getFechaEncargoFinGeedp() {
        return fechaEncargoFinGeedp;
    }

    public void setFechaEncargoFinGeedp(Date fechaEncargoFinGeedp) {
        this.fechaEncargoFinGeedp = fechaEncargoFinGeedp;
    }

    public BigDecimal getSueldoSubrogaGeedp() {
        return sueldoSubrogaGeedp;
    }

    public void setSueldoSubrogaGeedp(BigDecimal sueldoSubrogaGeedp) {
        this.sueldoSubrogaGeedp = sueldoSubrogaGeedp;
    }

    public Integer getEjecutoLiquidacionGeedp() {
        return ejecutoLiquidacionGeedp;
    }

    public void setEjecutoLiquidacionGeedp(Integer ejecutoLiquidacionGeedp) {
        this.ejecutoLiquidacionGeedp = ejecutoLiquidacionGeedp;
    }

    public String getObservacionGeedp() {
        return observacionGeedp;
    }

    public void setObservacionGeedp(String observacionGeedp) {
        this.observacionGeedp = observacionGeedp;
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

    public Boolean getActivoGeedp() {
        return activoGeedp;
    }

    public void setActivoGeedp(Boolean activoGeedp) {
        this.activoGeedp = activoGeedp;
    }

    public Boolean getLineaSupervicionGeedp() {
        return lineaSupervicionGeedp;
    }

    public void setLineaSupervicionGeedp(Boolean lineaSupervicionGeedp) {
        this.lineaSupervicionGeedp = lineaSupervicionGeedp;
    }

    public Boolean getAcumulaFondosGeedp() {
        return acumulaFondosGeedp;
    }

    public void setAcumulaFondosGeedp(Boolean acumulaFondosGeedp) {
        this.acumulaFondosGeedp = acumulaFondosGeedp;
    }

    public Boolean getControlAsistenciaGeedp() {
        return controlAsistenciaGeedp;
    }

    public void setControlAsistenciaGeedp(Boolean controlAsistenciaGeedp) {
        this.controlAsistenciaGeedp = controlAsistenciaGeedp;
    }

    public List<SprPresupuestoPuesto> getSprPresupuestoPuestoList() {
        return sprPresupuestoPuestoList;
    }

    public void setSprPresupuestoPuestoList(List<SprPresupuestoPuesto> sprPresupuestoPuestoList) {
        this.sprPresupuestoPuestoList = sprPresupuestoPuestoList;
    }

    public List<SprPresupuestoPuesto> getSprPresupuestoPuestoList1() {
        return sprPresupuestoPuestoList1;
    }

    public void setSprPresupuestoPuestoList1(List<SprPresupuestoPuesto> sprPresupuestoPuestoList1) {
        this.sprPresupuestoPuestoList1 = sprPresupuestoPuestoList1;
    }

    public List<AsiPermisosVacacionHext> getAsiPermisosVacacionHextList() {
        return asiPermisosVacacionHextList;
    }

    public void setAsiPermisosVacacionHextList(List<AsiPermisosVacacionHext> asiPermisosVacacionHextList) {
        this.asiPermisosVacacionHextList = asiPermisosVacacionHextList;
    }

    public List<AsiPermisosVacacionHext> getAsiPermisosVacacionHextList1() {
        return asiPermisosVacacionHextList1;
    }

    public void setAsiPermisosVacacionHextList1(List<AsiPermisosVacacionHext> asiPermisosVacacionHextList1) {
        this.asiPermisosVacacionHextList1 = asiPermisosVacacionHextList1;
    }

    public List<AsiPermisosVacacionHext> getAsiPermisosVacacionHextList2() {
        return asiPermisosVacacionHextList2;
    }

    public void setAsiPermisosVacacionHextList2(List<AsiPermisosVacacionHext> asiPermisosVacacionHextList2) {
        this.asiPermisosVacacionHextList2 = asiPermisosVacacionHextList2;
    }

    public List<AsiPermisosVacacionHext> getAsiPermisosVacacionHextList3() {
        return asiPermisosVacacionHextList3;
    }

    public void setAsiPermisosVacacionHextList3(List<AsiPermisosVacacionHext> asiPermisosVacacionHextList3) {
        this.asiPermisosVacacionHextList3 = asiPermisosVacacionHextList3;
    }

    public List<CppCapacitacion> getCppCapacitacionList() {
        return cppCapacitacionList;
    }

    public void setCppCapacitacionList(List<CppCapacitacion> cppCapacitacionList) {
        this.cppCapacitacionList = cppCapacitacionList;
    }

    public List<SriProyeccionIngres> getSriProyeccionIngresList() {
        return sriProyeccionIngresList;
    }

    public void setSriProyeccionIngresList(List<SriProyeccionIngres> sriProyeccionIngresList) {
        this.sriProyeccionIngresList = sriProyeccionIngresList;
    }

    public List<EvlEvaluadores> getEvlEvaluadoresList() {
        return evlEvaluadoresList;
    }

    public void setEvlEvaluadoresList(List<EvlEvaluadores> evlEvaluadoresList) {
        this.evlEvaluadoresList = evlEvaluadoresList;
    }

    public List<SaoDetalleMatrizRiesgo> getSaoDetalleMatrizRiesgoList() {
        return saoDetalleMatrizRiesgoList;
    }

    public void setSaoDetalleMatrizRiesgoList(List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList) {
        this.saoDetalleMatrizRiesgoList = saoDetalleMatrizRiesgoList;
    }

    public List<SaoDetalleMatrizRiesgo> getSaoDetalleMatrizRiesgoList1() {
        return saoDetalleMatrizRiesgoList1;
    }

    public void setSaoDetalleMatrizRiesgoList1(List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList1) {
        this.saoDetalleMatrizRiesgoList1 = saoDetalleMatrizRiesgoList1;
    }

    public List<TesComprobantePago> getTesComprobantePagoList() {
        return tesComprobantePagoList;
    }

    public void setTesComprobantePagoList(List<TesComprobantePago> tesComprobantePagoList) {
        this.tesComprobantePagoList = tesComprobantePagoList;
    }

    public List<CppParticipantes> getCppParticipantesList() {
        return cppParticipantesList;
    }

    public void setCppParticipantesList(List<CppParticipantes> cppParticipantesList) {
        this.cppParticipantesList = cppParticipantesList;
    }

    public List<PreTramite> getPreTramiteList() {
        return preTramiteList;
    }

    public void setPreTramiteList(List<PreTramite> preTramiteList) {
        this.preTramiteList = preTramiteList;
    }

    public List<PreTramite> getPreTramiteList1() {
        return preTramiteList1;
    }

    public void setPreTramiteList1(List<PreTramite> preTramiteList1) {
        this.preTramiteList1 = preTramiteList1;
    }

    public List<PreTramite> getPreTramiteList2() {
        return preTramiteList2;
    }

    public void setPreTramiteList2(List<PreTramite> preTramiteList2) {
        this.preTramiteList2 = preTramiteList2;
    }

    public List<PreTramite> getPreTramiteList3() {
        return preTramiteList3;
    }

    public void setPreTramiteList3(List<PreTramite> preTramiteList3) {
        this.preTramiteList3 = preTramiteList3;
    }

    public List<SaoCustodio> getSaoCustodioList() {
        return saoCustodioList;
    }

    public void setSaoCustodioList(List<SaoCustodio> saoCustodioList) {
        this.saoCustodioList = saoCustodioList;
    }

    public List<AsiValidaNomina> getAsiValidaNominaList() {
        return asiValidaNominaList;
    }

    public void setAsiValidaNominaList(List<AsiValidaNomina> asiValidaNominaList) {
        this.asiValidaNominaList = asiValidaNominaList;
    }

    public List<PreComparecienteContrato> getPreComparecienteContratoList() {
        return preComparecienteContratoList;
    }

    public void setPreComparecienteContratoList(List<PreComparecienteContrato> preComparecienteContratoList) {
        this.preComparecienteContratoList = preComparecienteContratoList;
    }

    public List<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaList() {
        return nrhDetalleFacturaGuarderiaList;
    }

    public void setNrhDetalleFacturaGuarderiaList(List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList) {
        this.nrhDetalleFacturaGuarderiaList = nrhDetalleFacturaGuarderiaList;
    }

    public List<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaList1() {
        return nrhDetalleFacturaGuarderiaList1;
    }

    public void setNrhDetalleFacturaGuarderiaList1(List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList1) {
        this.nrhDetalleFacturaGuarderiaList1 = nrhDetalleFacturaGuarderiaList1;
    }

    public List<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaList2() {
        return nrhDetalleFacturaGuarderiaList2;
    }

    public void setNrhDetalleFacturaGuarderiaList2(List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList2) {
        this.nrhDetalleFacturaGuarderiaList2 = nrhDetalleFacturaGuarderiaList2;
    }

    public List<NrhAnticipo> getNrhAnticipoList() {
        return nrhAnticipoList;
    }

    public void setNrhAnticipoList(List<NrhAnticipo> nrhAnticipoList) {
        this.nrhAnticipoList = nrhAnticipoList;
    }

    public List<NrhAnticipo> getNrhAnticipoList1() {
        return nrhAnticipoList1;
    }

    public void setNrhAnticipoList1(List<NrhAnticipo> nrhAnticipoList1) {
        this.nrhAnticipoList1 = nrhAnticipoList1;
    }

    public List<NrhAnticipo> getNrhAnticipoList2() {
        return nrhAnticipoList2;
    }

    public void setNrhAnticipoList2(List<NrhAnticipo> nrhAnticipoList2) {
        this.nrhAnticipoList2 = nrhAnticipoList2;
    }

    public List<NrhAnticipo> getNrhAnticipoList3() {
        return nrhAnticipoList3;
    }

    public void setNrhAnticipoList3(List<NrhAnticipo> nrhAnticipoList3) {
        this.nrhAnticipoList3 = nrhAnticipoList3;
    }

    public List<AdqSolicitudCompra> getAdqSolicitudCompraList() {
        return adqSolicitudCompraList;
    }

    public void setAdqSolicitudCompraList(List<AdqSolicitudCompra> adqSolicitudCompraList) {
        this.adqSolicitudCompraList = adqSolicitudCompraList;
    }

    public List<AfiCustodio> getAfiCustodioList() {
        return afiCustodioList;
    }

    public void setAfiCustodioList(List<AfiCustodio> afiCustodioList) {
        this.afiCustodioList = afiCustodioList;
    }

    public List<ContViajeros> getContViajerosList() {
        return contViajerosList;
    }

    public void setContViajerosList(List<ContViajeros> contViajerosList) {
        this.contViajerosList = contViajerosList;
    }

    public List<SaoEvaluacionPosiciograma> getSaoEvaluacionPosiciogramaList() {
        return saoEvaluacionPosiciogramaList;
    }

    public void setSaoEvaluacionPosiciogramaList(List<SaoEvaluacionPosiciograma> saoEvaluacionPosiciogramaList) {
        this.saoEvaluacionPosiciogramaList = saoEvaluacionPosiciogramaList;
    }

    public List<SprResponsableCalificacion> getSprResponsableCalificacionList() {
        return sprResponsableCalificacionList;
    }

    public void setSprResponsableCalificacionList(List<SprResponsableCalificacion> sprResponsableCalificacionList) {
        this.sprResponsableCalificacionList = sprResponsableCalificacionList;
    }

    public List<BodtConceptoEgreso> getBodtConceptoEgresoList() {
        return bodtConceptoEgresoList;
    }

    public void setBodtConceptoEgresoList(List<BodtConceptoEgreso> bodtConceptoEgresoList) {
        this.bodtConceptoEgresoList = bodtConceptoEgresoList;
    }

    public List<BodtConceptoEgreso> getBodtConceptoEgresoList1() {
        return bodtConceptoEgresoList1;
    }

    public void setBodtConceptoEgresoList1(List<BodtConceptoEgreso> bodtConceptoEgresoList1) {
        this.bodtConceptoEgresoList1 = bodtConceptoEgresoList1;
    }

    public List<BodtConceptoEgreso> getBodtConceptoEgresoList2() {
        return bodtConceptoEgresoList2;
    }

    public void setBodtConceptoEgresoList2(List<BodtConceptoEgreso> bodtConceptoEgresoList2) {
        this.bodtConceptoEgresoList2 = bodtConceptoEgresoList2;
    }

    public GthTipoSindicato getIdeGttsi() {
        return ideGttsi;
    }

    public void setIdeGttsi(GthTipoSindicato ideGttsi) {
        this.ideGttsi = ideGttsi;
    }

    public GthTipoEmpleado getIdeGttem() {
        return ideGttem;
    }

    public void setIdeGttem(GthTipoEmpleado ideGttem) {
        this.ideGttem = ideGttem;
    }

    public GthTipoContrato getIdeGttco() {
        return ideGttco;
    }

    public void setIdeGttco(GthTipoContrato ideGttco) {
        this.ideGttco = ideGttco;
    }

    public GthGrupoEmpleado getIdeGtgre() {
        return ideGtgre;
    }

    public void setIdeGtgre(GthGrupoEmpleado ideGtgre) {
        this.ideGtgre = ideGtgre;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenTipoVinculacion getIdeGetiv() {
        return ideGetiv;
    }

    public void setIdeGetiv(GenTipoVinculacion ideGetiv) {
        this.ideGetiv = ideGetiv;
    }

    public GenPartidaGrupoCargo getGenPartidaGrupoCargo() {
        return genPartidaGrupoCargo;
    }

    public void setGenPartidaGrupoCargo(GenPartidaGrupoCargo genPartidaGrupoCargo) {
        this.genPartidaGrupoCargo = genPartidaGrupoCargo;
    }

    public GenGrupoCargo getGenGrupoCargo() {
        return genGrupoCargo;
    }

    public void setGenGrupoCargo(GenGrupoCargo genGrupoCargo) {
        this.genGrupoCargo = genGrupoCargo;
    }

    public GenDetalleEmpleadoDepartame getIdeGeded() {
        return ideGeded;
    }

    public void setIdeGeded(GenDetalleEmpleadoDepartame ideGeded) {
        this.ideGeded = ideGeded;
    }

    public GenCategoriaEstatus getIdeGecae() {
        return ideGecae;
    }

    public void setIdeGecae(GenCategoriaEstatus ideGecae) {
        this.ideGecae = ideGecae;
    }

    public List<NrhGarante> getNrhGaranteList() {
        return nrhGaranteList;
    }

    public void setNrhGaranteList(List<NrhGarante> nrhGaranteList) {
        this.nrhGaranteList = nrhGaranteList;
    }

    public List<SprSolicitudAprobacion> getSprSolicitudAprobacionList() {
        return sprSolicitudAprobacionList;
    }

    public void setSprSolicitudAprobacionList(List<SprSolicitudAprobacion> sprSolicitudAprobacionList) {
        this.sprSolicitudAprobacionList = sprSolicitudAprobacionList;
    }

    public List<SprSolicitudAprobacion> getSprSolicitudAprobacionList1() {
        return sprSolicitudAprobacionList1;
    }

    public void setSprSolicitudAprobacionList1(List<SprSolicitudAprobacion> sprSolicitudAprobacionList1) {
        this.sprSolicitudAprobacionList1 = sprSolicitudAprobacionList1;
    }

    public List<SaoCertificadoExterno> getSaoCertificadoExternoList() {
        return saoCertificadoExternoList;
    }

    public void setSaoCertificadoExternoList(List<SaoCertificadoExterno> saoCertificadoExternoList) {
        this.saoCertificadoExternoList = saoCertificadoExternoList;
    }

    public List<ContServicioSuministro> getContServicioSuministroList() {
        return contServicioSuministroList;
    }

    public void setContServicioSuministroList(List<ContServicioSuministro> contServicioSuministroList) {
        this.contServicioSuministroList = contServicioSuministroList;
    }

    public List<CppEvaluacionJefe> getCppEvaluacionJefeList() {
        return cppEvaluacionJefeList;
    }

    public void setCppEvaluacionJefeList(List<CppEvaluacionJefe> cppEvaluacionJefeList) {
        this.cppEvaluacionJefeList = cppEvaluacionJefeList;
    }

    public List<PreDocumentoHabilitante> getPreDocumentoHabilitanteList() {
        return preDocumentoHabilitanteList;
    }

    public void setPreDocumentoHabilitanteList(List<PreDocumentoHabilitante> preDocumentoHabilitanteList) {
        this.preDocumentoHabilitanteList = preDocumentoHabilitanteList;
    }

    public List<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoList() {
        return nrhBeneficioEmpleadoList;
    }

    public void setNrhBeneficioEmpleadoList(List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList) {
        this.nrhBeneficioEmpleadoList = nrhBeneficioEmpleadoList;
    }

    public List<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoList1() {
        return nrhBeneficioEmpleadoList1;
    }

    public void setNrhBeneficioEmpleadoList1(List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList1) {
        this.nrhBeneficioEmpleadoList1 = nrhBeneficioEmpleadoList1;
    }

    public List<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoList2() {
        return nrhBeneficioEmpleadoList2;
    }

    public void setNrhBeneficioEmpleadoList2(List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList2) {
        this.nrhBeneficioEmpleadoList2 = nrhBeneficioEmpleadoList2;
    }

    public List<SprSolicitudPuesto> getSprSolicitudPuestoList() {
        return sprSolicitudPuestoList;
    }

    public void setSprSolicitudPuestoList(List<SprSolicitudPuesto> sprSolicitudPuestoList) {
        this.sprSolicitudPuestoList = sprSolicitudPuestoList;
    }

    public List<SprSolicitudPuesto> getSprSolicitudPuestoList1() {
        return sprSolicitudPuestoList1;
    }

    public void setSprSolicitudPuestoList1(List<SprSolicitudPuesto> sprSolicitudPuestoList1) {
        this.sprSolicitudPuestoList1 = sprSolicitudPuestoList1;
    }

    public List<NrhDetalleRol> getNrhDetalleRolList() {
        return nrhDetalleRolList;
    }

    public void setNrhDetalleRolList(List<NrhDetalleRol> nrhDetalleRolList) {
        this.nrhDetalleRolList = nrhDetalleRolList;
    }

    public List<SaoFichaMedica> getSaoFichaMedicaList() {
        return saoFichaMedicaList;
    }

    public void setSaoFichaMedicaList(List<SaoFichaMedica> saoFichaMedicaList) {
        this.saoFichaMedicaList = saoFichaMedicaList;
    }

    public List<PreResponsableContratacion> getPreResponsableContratacionList() {
        return preResponsableContratacionList;
    }

    public void setPreResponsableContratacionList(List<PreResponsableContratacion> preResponsableContratacionList) {
        this.preResponsableContratacionList = preResponsableContratacionList;
    }

    public List<SaoMatrizRiesgo> getSaoMatrizRiesgoList() {
        return saoMatrizRiesgoList;
    }

    public void setSaoMatrizRiesgoList(List<SaoMatrizRiesgo> saoMatrizRiesgoList) {
        this.saoMatrizRiesgoList = saoMatrizRiesgoList;
    }

    public List<SaoMatrizRiesgo> getSaoMatrizRiesgoList1() {
        return saoMatrizRiesgoList1;
    }

    public void setSaoMatrizRiesgoList1(List<SaoMatrizRiesgo> saoMatrizRiesgoList1) {
        this.saoMatrizRiesgoList1 = saoMatrizRiesgoList1;
    }

    public List<EvlDesempenio> getEvlDesempenioList() {
        return evlDesempenioList;
    }

    public void setEvlDesempenioList(List<EvlDesempenio> evlDesempenioList) {
        this.evlDesempenioList = evlDesempenioList;
    }

    public List<ContResponsableConvenio> getContResponsableConvenioList() {
        return contResponsableConvenioList;
    }

    public void setContResponsableConvenioList(List<ContResponsableConvenio> contResponsableConvenioList) {
        this.contResponsableConvenioList = contResponsableConvenioList;
    }

    public List<PreAdministradorContrato> getPreAdministradorContratoList() {
        return preAdministradorContratoList;
    }

    public void setPreAdministradorContratoList(List<PreAdministradorContrato> preAdministradorContratoList) {
        this.preAdministradorContratoList = preAdministradorContratoList;
    }

    public List<BisEntrevista> getBisEntrevistaList() {
        return bisEntrevistaList;
    }

    public void setBisEntrevistaList(List<BisEntrevista> bisEntrevistaList) {
        this.bisEntrevistaList = bisEntrevistaList;
    }

    public List<BisEntrevista> getBisEntrevistaList1() {
        return bisEntrevistaList1;
    }

    public void setBisEntrevistaList1(List<BisEntrevista> bisEntrevistaList1) {
        this.bisEntrevistaList1 = bisEntrevistaList1;
    }

    public List<GthViaticos> getGthViaticosList() {
        return gthViaticosList;
    }

    public void setGthViaticosList(List<GthViaticos> gthViaticosList) {
        this.gthViaticosList = gthViaticosList;
    }

    public List<GthViaticos> getGthViaticosList1() {
        return gthViaticosList1;
    }

    public void setGthViaticosList1(List<GthViaticos> gthViaticosList1) {
        this.gthViaticosList1 = gthViaticosList1;
    }

    public List<GthViaticos> getGthViaticosList2() {
        return gthViaticosList2;
    }

    public void setGthViaticosList2(List<GthViaticos> gthViaticosList2) {
        this.gthViaticosList2 = gthViaticosList2;
    }

    public List<GthViaticos> getGthViaticosList3() {
        return gthViaticosList3;
    }

    public void setGthViaticosList3(List<GthViaticos> gthViaticosList3) {
        this.gthViaticosList3 = gthViaticosList3;
    }

    public List<AsiValidaAsistencia> getAsiValidaAsistenciaList() {
        return asiValidaAsistenciaList;
    }

    public void setAsiValidaAsistenciaList(List<AsiValidaAsistencia> asiValidaAsistenciaList) {
        this.asiValidaAsistenciaList = asiValidaAsistenciaList;
    }

    public List<NrhRetencionJudicial> getNrhRetencionJudicialList() {
        return nrhRetencionJudicialList;
    }

    public void setNrhRetencionJudicialList(List<NrhRetencionJudicial> nrhRetencionJudicialList) {
        this.nrhRetencionJudicialList = nrhRetencionJudicialList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeedp != null ? ideGeedp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenEmpleadosDepartamentoPar)) {
            return false;
        }
        GenEmpleadosDepartamentoPar other = (GenEmpleadosDepartamentoPar) object;
        if ((this.ideGeedp == null && other.ideGeedp != null) || (this.ideGeedp != null && !this.ideGeedp.equals(other.ideGeedp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenEmpleadosDepartamentoPar[ ideGeedp=" + ideGeedp + " ]";
    }
    
}
