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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import paq_sistema.aplicacion.Utilitario;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "gth_empleado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthEmpleado.findAll", query = "SELECT g FROM GthEmpleado g"),
    @NamedQuery(name = "GthEmpleado.findByIdeGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.ideGtemp = :ideGtemp"),
    @NamedQuery(name = "GthEmpleado.findByDocumentoIdentidadGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.documentoIdentidadGtemp = :documentoIdentidadGtemp"),
    @NamedQuery(name = "GthEmpleado.findByFechaIngresoPaisGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.fechaIngresoPaisGtemp = :fechaIngresoPaisGtemp"),
    @NamedQuery(name = "GthEmpleado.findByCarnetExtranjeriaGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.carnetExtranjeriaGtemp = :carnetExtranjeriaGtemp"),
    @NamedQuery(name = "GthEmpleado.findByPrimerNombreGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.primerNombreGtemp = :primerNombreGtemp"),
    @NamedQuery(name = "GthEmpleado.findBySegundoNombreGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.segundoNombreGtemp = :segundoNombreGtemp"),
    @NamedQuery(name = "GthEmpleado.findByApellidoPaternoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.apellidoPaternoGtemp = :apellidoPaternoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByApellidoMaternoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.apellidoMaternoGtemp = :apellidoMaternoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByFechaNacimientoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.fechaNacimientoGtemp = :fechaNacimientoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByCargoPublicoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.cargoPublicoGtemp = :cargoPublicoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByPathFotoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.pathFotoGtemp = :pathFotoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByPathFirmaGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.pathFirmaGtemp = :pathFirmaGtemp"),
    @NamedQuery(name = "GthEmpleado.findByFechaIngresoGrupoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.fechaIngresoGrupoGtemp = :fechaIngresoGrupoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByFechaIngresoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.fechaIngresoGtemp = :fechaIngresoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByTarjetaMarcacionGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.tarjetaMarcacionGtemp = :tarjetaMarcacionGtemp"),
    @NamedQuery(name = "GthEmpleado.findByActivoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.activoGtemp = :activoGtemp"),
    @NamedQuery(name = "GthEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthEmpleado.findByFechaIngre", query = "SELECT g FROM GthEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthEmpleado.findByUsuarioActua", query = "SELECT g FROM GthEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthEmpleado.findByFechaActua", query = "SELECT g FROM GthEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthEmpleado.findByHoraIngre", query = "SELECT g FROM GthEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthEmpleado.findByHoraActua", query = "SELECT g FROM GthEmpleado g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthEmpleado.findBySeparacionBienesGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.separacionBienesGtemp = :separacionBienesGtemp"),
    @NamedQuery(name = "GthEmpleado.findByDiscapacitadoGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.discapacitadoGtemp = :discapacitadoGtemp"),
    
    @NamedQuery(name = "GthEmpleado.findByNumCargasGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.numCargasGtemp = :numCargasGtemp"),
    @NamedQuery(name = "GthEmpleado.findByEnfermedadCatastroficaGtemp", query = "SELECT g FROM GthEmpleado g WHERE g.enfermedadCatastroficaGtemp = :enfermedadCatastroficaGtemp")})




public class GthEmpleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtemp", nullable = false)
    private Integer ideGtemp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "documento_identidad_gtemp", nullable = false, length = 15)
    private String documentoIdentidadGtemp;
    @Column(name = "fecha_ingreso_pais_gtemp")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoPaisGtemp;
    @Size(max = 20)
    @Column(name = "carnet_extranjeria_gtemp", length = 20)
    private String carnetExtranjeriaGtemp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "primer_nombre_gtemp", nullable = false, length = 20)
    private String primerNombreGtemp;
    @Size(max = 20)
    @Column(name = "segundo_nombre_gtemp", length = 20)
    private String segundoNombreGtemp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "apellido_paterno_gtemp", nullable = false, length = 20)
    private String apellidoPaternoGtemp;
    @Size(max = 20)
    @Column(name = "apellido_materno_gtemp", length = 20)
    private String apellidoMaternoGtemp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_nacimiento_gtemp", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoGtemp;
    @Column(name = "cargo_publico_gtemp")
    private Integer cargoPublicoGtemp;
    @Size(max = 100)
    @Column(name = "path_foto_gtemp", length = 100)
    private String pathFotoGtemp;
    @Size(max = 100)
    @Column(name = "path_firma_gtemp", length = 100)
    private String pathFirmaGtemp;
    @Column(name = "fecha_ingreso_grupo_gtemp")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoGrupoGtemp;
    @Column(name = "fecha_ingreso_gtemp")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoGtemp;
    @Size(max = 50)
    @Column(name = "tarjeta_marcacion_gtemp", length = 50)
    private String tarjetaMarcacionGtemp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_gtemp", nullable = false)
    private boolean activoGtemp;
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
    @Column(name = "separacion_bienes_gtemp")
    private Boolean separacionBienesGtemp;
    @Column(name = "discapacitado_gtemp")
    private Boolean discapacitadoGtemp;
    @OneToMany(mappedBy = "ideGtemp")
    private List<AsiPermisosVacacionHext> asiPermisosVacacionHextList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<AsiNovedadDetalle> asiNovedadDetalleList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SbsArchivoOnce> sbsArchivoOnceList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SriProyeccionIngres> sriProyeccionIngresList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<CppParticipantes> cppParticipantesList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthDireccion> gthDireccionList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SaoCustodio> saoCustodioList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SbsArchivoCuarentaUno> sbsArchivoCuarentaUnoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthSeguroVida> gthSeguroVidaList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<PreComparecienteContrato> preComparecienteContratoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gthEmpleado")
    private List<GthRegistroMilitar> gthRegistroMilitarList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthTelefono> gthTelefonoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private List<NrhAnticipo> nrhAnticipoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private List<GthConyuge> gthConyugeList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SisUsuario> sisUsuarioList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthTerrenoEmpleado> gthTerrenoEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthIdiomaEmpleado> gthIdiomaEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<ContViajeros> contViajerosList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<FacFactura> facFacturaList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthCuentaAnticipo> gthCuentaAnticipoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthSituacionEconomicaEmplea> gthSituacionEconomicaEmpleaList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthMembresiaEmpleado> gthMembresiaEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SaoEvaluacionPosiciograma> saoEvaluacionPosiciogramaList;
    @JoinColumn(name = "ide_gttis", referencedColumnName = "ide_gttis", nullable = false)
    @ManyToOne(optional = false)
    private GthTipoSangre ideGttis;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi", nullable = false)
    @ManyToOne(optional = false)
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtnac", referencedColumnName = "ide_gtnac", nullable = false)
    @ManyToOne(optional = false)
    private GthNacionalidad ideGtnac;
    @JoinColumn(name = "ide_gtgen", referencedColumnName = "ide_gtgen", nullable = false)
    @ManyToOne(optional = false)
    private GthGenero ideGtgen;
    @JoinColumn(name = "ide_gtesc", referencedColumnName = "ide_gtesc", nullable = false)
    @ManyToOne(optional = false)
    private GthEstadoCivil ideGtesc;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip", nullable = false)
    @ManyToOne(optional = false)
    private GenDivisionPolitica ideGedip;
    //@OneToMany(mappedBy = "ideGtemp")
    //private List<SbsArchivoVeinteTres> sbsArchivoVeinteTresList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthTarjetaCreditoEmpleado> gthTarjetaCreditoEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<AsiVacacion> asiVacacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private List<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthCasaEmpleado> gthCasaEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SaoCertificadoExterno> saoCertificadoExternoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private List<SriDeduciblesEmpleado> sriDeduciblesEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private List<GthDiscapacidadEmpleado> gthDiscapacidadEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SaoFichaMedica> saoFichaMedicaList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<PreResponsableContratacion> preResponsableContratacionList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthCapacitacionEmpleado> gthCapacitacionEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<SbsArchivoEdiez> sbsArchivoEdiezList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthCorreo> gthCorreoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthArchivoEmpleado> gthArchivoEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthAmigosEmpresaEmplea> gthAmigosEmpresaEmpleaList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<EvlDesempenio> evlDesempenioList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<ContResponsableConvenio> contResponsableConvenioList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthSituacionFinancieraEmple> gthSituacionFinancieraEmpleList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthEducacionEmpleado> gthEducacionEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthNegocioEmpleado> gthNegocioEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<PreAdministradorContrato> preAdministradorContratoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthExperienciaLaboralEmplea> gthExperienciaLaboralEmpleaList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<BisEntrevista> bisEntrevistaList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthViaticos> gthViaticosList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<CrcAsistenteEvento> crcAsistenteEventoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthVehiculoEmpleado> gthVehiculoEmpleadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gthEmpleado")
    private List<GthHobie> gthHobieList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthFamiliar> gthFamiliarList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtemp")
    private List<GthCargasFamiliares> gthCargasFamiliaresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gthEmpleado")
    private List<GthPersonaEmergencia> gthPersonaEmergenciaList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthInversionEmpleado> gthInversionEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<GthDocumentacionEmpleado> gthDocumentacionEmpleadoList;
    @OneToMany(mappedBy = "ideGtemp")
    private List<NrhRetencionJudicial> nrhRetencionJudicialList;

    @Column(name = "num_cargas_gtemp")
    private Integer numCargasGtemp;
    
   
    @Column(name = "enfermedad_catastrofica_gtemp", nullable = false)
    private boolean enfermedadCatastroficaGtemp; 
    
    public GthEmpleado() {
    }

    public GthEmpleado(Integer ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GthEmpleado(Integer ideGtemp, String documentoIdentidadGtemp, String primerNombreGtemp, String apellidoPaternoGtemp, Date fechaNacimientoGtemp, boolean activoGtemp) {
        this.ideGtemp = ideGtemp;
        this.documentoIdentidadGtemp = documentoIdentidadGtemp;
        this.primerNombreGtemp = primerNombreGtemp;
        this.apellidoPaternoGtemp = apellidoPaternoGtemp;
        this.fechaNacimientoGtemp = fechaNacimientoGtemp;
        this.activoGtemp = activoGtemp;
    }

    public Integer getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(Integer ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public String getDocumentoIdentidadGtemp() {
        return documentoIdentidadGtemp;
    }

    public void setDocumentoIdentidadGtemp(String documentoIdentidadGtemp) {
        this.documentoIdentidadGtemp = documentoIdentidadGtemp;
    }

    public Date getFechaIngresoPaisGtemp() {
        return fechaIngresoPaisGtemp;
    }

    public void setFechaIngresoPaisGtemp(Date fechaIngresoPaisGtemp) {
        this.fechaIngresoPaisGtemp = fechaIngresoPaisGtemp;
    }

    public String getCarnetExtranjeriaGtemp() {
        return carnetExtranjeriaGtemp;
    }

    public void setCarnetExtranjeriaGtemp(String carnetExtranjeriaGtemp) {
        this.carnetExtranjeriaGtemp = carnetExtranjeriaGtemp;
    }

    public String getPrimerNombreGtemp() {
        return primerNombreGtemp;
    }

    public void setPrimerNombreGtemp(String primerNombreGtemp) {
        this.primerNombreGtemp = primerNombreGtemp;
    }

    public String getSegundoNombreGtemp() {
        return segundoNombreGtemp;
    }

    public void setSegundoNombreGtemp(String segundoNombreGtemp) {
        this.segundoNombreGtemp = segundoNombreGtemp;
    }

    public String getApellidoPaternoGtemp() {
        return apellidoPaternoGtemp;
    }

    public void setApellidoPaternoGtemp(String apellidoPaternoGtemp) {
        this.apellidoPaternoGtemp = apellidoPaternoGtemp;
    }

    public String getApellidoMaternoGtemp() {
        return apellidoMaternoGtemp;
    }

    public void setApellidoMaternoGtemp(String apellidoMaternoGtemp) {
        this.apellidoMaternoGtemp = apellidoMaternoGtemp;
    }

    public Date getFechaNacimientoGtemp() {
        return fechaNacimientoGtemp;
    }

    public void setFechaNacimientoGtemp(Date fechaNacimientoGtemp) {
        this.fechaNacimientoGtemp = fechaNacimientoGtemp;
    }

    public Integer getCargoPublicoGtemp() {
        return cargoPublicoGtemp;
    }

    public void setCargoPublicoGtemp(Integer cargoPublicoGtemp) {
        this.cargoPublicoGtemp = cargoPublicoGtemp;
    }

    public String getPathFotoGtemp() {
        return new Utilitario().getPropiedad("rutaDownload")+pathFotoGtemp;
    }

    public void setPathFotoGtemp(String pathFotoGtemp) {
        this.pathFotoGtemp = pathFotoGtemp;
    }

    public String getPathFirmaGtemp() {
        return pathFirmaGtemp;
    }

    public void setPathFirmaGtemp(String pathFirmaGtemp) {
        this.pathFirmaGtemp = pathFirmaGtemp;
    }

    public Date getFechaIngresoGrupoGtemp() {
        return fechaIngresoGrupoGtemp;
    }

    public void setFechaIngresoGrupoGtemp(Date fechaIngresoGrupoGtemp) {
        this.fechaIngresoGrupoGtemp = fechaIngresoGrupoGtemp;
    }

    public Date getFechaIngresoGtemp() {
        return fechaIngresoGtemp;
    }

    public void setFechaIngresoGtemp(Date fechaIngresoGtemp) {
        this.fechaIngresoGtemp = fechaIngresoGtemp;
    }

    public String getTarjetaMarcacionGtemp() {
        return tarjetaMarcacionGtemp;
    }

    public void setTarjetaMarcacionGtemp(String tarjetaMarcacionGtemp) {
        this.tarjetaMarcacionGtemp = tarjetaMarcacionGtemp;
    }

    public boolean getActivoGtemp() {
        return activoGtemp;
    }

    public void setActivoGtemp(boolean activoGtemp) {
        this.activoGtemp = activoGtemp;
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

    public Boolean getSeparacionBienesGtemp() {
        return separacionBienesGtemp;
    }

    public void setSeparacionBienesGtemp(Boolean separacionBienesGtemp) {
        this.separacionBienesGtemp = separacionBienesGtemp;
    }

    public Boolean getDiscapacitadoGtemp() {
        return discapacitadoGtemp;
    }

    public void setDiscapacitadoGtemp(Boolean discapacitadoGtemp) {
        this.discapacitadoGtemp = discapacitadoGtemp;
    }

    public List<AsiPermisosVacacionHext> getAsiPermisosVacacionHextList() {
        return asiPermisosVacacionHextList;
    }

    public void setAsiPermisosVacacionHextList(List<AsiPermisosVacacionHext> asiPermisosVacacionHextList) {
        this.asiPermisosVacacionHextList = asiPermisosVacacionHextList;
    }

    public List<AsiNovedadDetalle> getAsiNovedadDetalleList() {
        return asiNovedadDetalleList;
    }

    public void setAsiNovedadDetalleList(List<AsiNovedadDetalle> asiNovedadDetalleList) {
        this.asiNovedadDetalleList = asiNovedadDetalleList;
    }

    public List<SbsArchivoOnce> getSbsArchivoOnceList() {
        return sbsArchivoOnceList;
    }

    public void setSbsArchivoOnceList(List<SbsArchivoOnce> sbsArchivoOnceList) {
        this.sbsArchivoOnceList = sbsArchivoOnceList;
    }

    public List<SbsArchivoVeinteUno> getSbsArchivoVeinteUnoList() {
        return sbsArchivoVeinteUnoList;
    }

    public void setSbsArchivoVeinteUnoList(List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList) {
        this.sbsArchivoVeinteUnoList = sbsArchivoVeinteUnoList;
    }

    public List<SriProyeccionIngres> getSriProyeccionIngresList() {
        return sriProyeccionIngresList;
    }

    public void setSriProyeccionIngresList(List<SriProyeccionIngres> sriProyeccionIngresList) {
        this.sriProyeccionIngresList = sriProyeccionIngresList;
    }

    public List<GenDetalleEmpleadoDepartame> getGenDetalleEmpleadoDepartameList() {
        return genDetalleEmpleadoDepartameList;
    }

    public void setGenDetalleEmpleadoDepartameList(List<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameList) {
        this.genDetalleEmpleadoDepartameList = genDetalleEmpleadoDepartameList;
    }

    public List<CppParticipantes> getCppParticipantesList() {
        return cppParticipantesList;
    }

    public void setCppParticipantesList(List<CppParticipantes> cppParticipantesList) {
        this.cppParticipantesList = cppParticipantesList;
    }

    public List<GthDireccion> getGthDireccionList() {
        return gthDireccionList;
    }

    public void setGthDireccionList(List<GthDireccion> gthDireccionList) {
        this.gthDireccionList = gthDireccionList;
    }

    public List<GthEndeudamientoEmpleado> getGthEndeudamientoEmpleadoList() {
        return gthEndeudamientoEmpleadoList;
    }

    public void setGthEndeudamientoEmpleadoList(List<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoList) {
        this.gthEndeudamientoEmpleadoList = gthEndeudamientoEmpleadoList;
    }

    public List<SaoCustodio> getSaoCustodioList() {
        return saoCustodioList;
    }

    public void setSaoCustodioList(List<SaoCustodio> saoCustodioList) {
        this.saoCustodioList = saoCustodioList;
    }

    public List<SbsArchivoCuarentaUno> getSbsArchivoCuarentaUnoList() {
        return sbsArchivoCuarentaUnoList;
    }

    public void setSbsArchivoCuarentaUnoList(List<SbsArchivoCuarentaUno> sbsArchivoCuarentaUnoList) {
        this.sbsArchivoCuarentaUnoList = sbsArchivoCuarentaUnoList;
    }

    public List<GthSeguroVida> getGthSeguroVidaList() {
        return gthSeguroVidaList;
    }

    public void setGthSeguroVidaList(List<GthSeguroVida> gthSeguroVidaList) {
        this.gthSeguroVidaList = gthSeguroVidaList;
    }

    public List<PreComparecienteContrato> getPreComparecienteContratoList() {
        return preComparecienteContratoList;
    }

    public void setPreComparecienteContratoList(List<PreComparecienteContrato> preComparecienteContratoList) {
        this.preComparecienteContratoList = preComparecienteContratoList;
    }

    public List<GthRegistroMilitar> getGthRegistroMilitarList() {
        return gthRegistroMilitarList;
    }

    public void setGthRegistroMilitarList(List<GthRegistroMilitar> gthRegistroMilitarList) {
        this.gthRegistroMilitarList = gthRegistroMilitarList;
    }

    public List<GthTelefono> getGthTelefonoList() {
        return gthTelefonoList;
    }

    public void setGthTelefonoList(List<GthTelefono> gthTelefonoList) {
        this.gthTelefonoList = gthTelefonoList;
    }

    public List<NrhAnticipo> getNrhAnticipoList() {
        return nrhAnticipoList;
    }

    public void setNrhAnticipoList(List<NrhAnticipo> nrhAnticipoList) {
        this.nrhAnticipoList = nrhAnticipoList;
    }

    public List<GthConyuge> getGthConyugeList() {
        return gthConyugeList;
    }

    public void setGthConyugeList(List<GthConyuge> gthConyugeList) {
        this.gthConyugeList = gthConyugeList;
    }

    public List<SisUsuario> getSisUsuarioList() {
        return sisUsuarioList;
    }

    public void setSisUsuarioList(List<SisUsuario> sisUsuarioList) {
        this.sisUsuarioList = sisUsuarioList;
    }

    public List<GthTerrenoEmpleado> getGthTerrenoEmpleadoList() {
        return gthTerrenoEmpleadoList;
    }

    public void setGthTerrenoEmpleadoList(List<GthTerrenoEmpleado> gthTerrenoEmpleadoList) {
        this.gthTerrenoEmpleadoList = gthTerrenoEmpleadoList;
    }

    public List<GthIdiomaEmpleado> getGthIdiomaEmpleadoList() {
        return gthIdiomaEmpleadoList;
    }

    public void setGthIdiomaEmpleadoList(List<GthIdiomaEmpleado> gthIdiomaEmpleadoList) {
        this.gthIdiomaEmpleadoList = gthIdiomaEmpleadoList;
    }

    public List<ContViajeros> getContViajerosList() {
        return contViajerosList;
    }

    public void setContViajerosList(List<ContViajeros> contViajerosList) {
        this.contViajerosList = contViajerosList;
    }

    public List<FacFactura> getFacFacturaList() {
        return facFacturaList;
    }

    public void setFacFacturaList(List<FacFactura> facFacturaList) {
        this.facFacturaList = facFacturaList;
    }

    public List<GthCuentaAnticipo> getGthCuentaAnticipoList() {
        return gthCuentaAnticipoList;
    }

    public void setGthCuentaAnticipoList(List<GthCuentaAnticipo> gthCuentaAnticipoList) {
        this.gthCuentaAnticipoList = gthCuentaAnticipoList;
    }

    public List<GthSituacionEconomicaEmplea> getGthSituacionEconomicaEmpleaList() {
        return gthSituacionEconomicaEmpleaList;
    }

    public void setGthSituacionEconomicaEmpleaList(List<GthSituacionEconomicaEmplea> gthSituacionEconomicaEmpleaList) {
        this.gthSituacionEconomicaEmpleaList = gthSituacionEconomicaEmpleaList;
    }

    public List<GthMembresiaEmpleado> getGthMembresiaEmpleadoList() {
        return gthMembresiaEmpleadoList;
    }

    public void setGthMembresiaEmpleadoList(List<GthMembresiaEmpleado> gthMembresiaEmpleadoList) {
        this.gthMembresiaEmpleadoList = gthMembresiaEmpleadoList;
    }

    public List<SaoEvaluacionPosiciograma> getSaoEvaluacionPosiciogramaList() {
        return saoEvaluacionPosiciogramaList;
    }

    public void setSaoEvaluacionPosiciogramaList(List<SaoEvaluacionPosiciograma> saoEvaluacionPosiciogramaList) {
        this.saoEvaluacionPosiciogramaList = saoEvaluacionPosiciogramaList;
    }

    public GthTipoSangre getIdeGttis() {
        return ideGttis;
    }

    public void setIdeGttis(GthTipoSangre ideGttis) {
        this.ideGttis = ideGttis;
    }

    public GthTipoDocumentoIdentidad getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(GthTipoDocumentoIdentidad ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public GthNacionalidad getIdeGtnac() {
        return ideGtnac;
    }

    public void setIdeGtnac(GthNacionalidad ideGtnac) {
        this.ideGtnac = ideGtnac;
    }

    public GthGenero getIdeGtgen() {
        return ideGtgen;
    }

    public void setIdeGtgen(GthGenero ideGtgen) {
        this.ideGtgen = ideGtgen;
    }

    public GthEstadoCivil getIdeGtesc() {
        return ideGtesc;
    }

    public void setIdeGtesc(GthEstadoCivil ideGtesc) {
        this.ideGtesc = ideGtesc;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

  /*  public List<SbsArchivoVeinteTres> getSbsArchivoVeinteTresList() {
        return sbsArchivoVeinteTresList;
    }

    public void setSbsArchivoVeinteTresList(List<SbsArchivoVeinteTres> sbsArchivoVeinteTresList) {
        this.sbsArchivoVeinteTresList = sbsArchivoVeinteTresList;
    }*/

    public List<GthTarjetaCreditoEmpleado> getGthTarjetaCreditoEmpleadoList() {
        return gthTarjetaCreditoEmpleadoList;
    }

    public void setGthTarjetaCreditoEmpleadoList(List<GthTarjetaCreditoEmpleado> gthTarjetaCreditoEmpleadoList) {
        this.gthTarjetaCreditoEmpleadoList = gthTarjetaCreditoEmpleadoList;
    }

    public List<AsiVacacion> getAsiVacacionList() {
        return asiVacacionList;
    }

    public void setAsiVacacionList(List<AsiVacacion> asiVacacionList) {
        this.asiVacacionList = asiVacacionList;
    }

    public List<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParList() {
        return genEmpleadosDepartamentoParList;
    }

    public void setGenEmpleadosDepartamentoParList(List<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParList) {
        this.genEmpleadosDepartamentoParList = genEmpleadosDepartamentoParList;
    }

    public List<GthCasaEmpleado> getGthCasaEmpleadoList() {
        return gthCasaEmpleadoList;
    }

    public void setGthCasaEmpleadoList(List<GthCasaEmpleado> gthCasaEmpleadoList) {
        this.gthCasaEmpleadoList = gthCasaEmpleadoList;
    }

    public List<SaoCertificadoExterno> getSaoCertificadoExternoList() {
        return saoCertificadoExternoList;
    }

    public void setSaoCertificadoExternoList(List<SaoCertificadoExterno> saoCertificadoExternoList) {
        this.saoCertificadoExternoList = saoCertificadoExternoList;
    }

    public List<SriDeduciblesEmpleado> getSriDeduciblesEmpleadoList() {
        return sriDeduciblesEmpleadoList;
    }

    public void setSriDeduciblesEmpleadoList(List<SriDeduciblesEmpleado> sriDeduciblesEmpleadoList) {
        this.sriDeduciblesEmpleadoList = sriDeduciblesEmpleadoList;
    }

    public List<GthCuentaBancariaEmpleado> getGthCuentaBancariaEmpleadoList() {
        return gthCuentaBancariaEmpleadoList;
    }

    public void setGthCuentaBancariaEmpleadoList(List<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoList) {
        this.gthCuentaBancariaEmpleadoList = gthCuentaBancariaEmpleadoList;
    }

    public List<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoList() {
        return nrhBeneficioEmpleadoList;
    }

    public void setNrhBeneficioEmpleadoList(List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList) {
        this.nrhBeneficioEmpleadoList = nrhBeneficioEmpleadoList;
    }

    public List<GthDiscapacidadEmpleado> getGthDiscapacidadEmpleadoList() {
        return gthDiscapacidadEmpleadoList;
    }

    public void setGthDiscapacidadEmpleadoList(List<GthDiscapacidadEmpleado> gthDiscapacidadEmpleadoList) {
        this.gthDiscapacidadEmpleadoList = gthDiscapacidadEmpleadoList;
    }

    public List<GthExperienciaDocenteEmplea> getGthExperienciaDocenteEmpleaList() {
        return gthExperienciaDocenteEmpleaList;
    }

    public void setGthExperienciaDocenteEmpleaList(List<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaList) {
        this.gthExperienciaDocenteEmpleaList = gthExperienciaDocenteEmpleaList;
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

    public List<GthCapacitacionEmpleado> getGthCapacitacionEmpleadoList() {
        return gthCapacitacionEmpleadoList;
    }

    public void setGthCapacitacionEmpleadoList(List<GthCapacitacionEmpleado> gthCapacitacionEmpleadoList) {
        this.gthCapacitacionEmpleadoList = gthCapacitacionEmpleadoList;
    }

    public List<SbsArchivoEdiez> getSbsArchivoEdiezList() {
        return sbsArchivoEdiezList;
    }

    public void setSbsArchivoEdiezList(List<SbsArchivoEdiez> sbsArchivoEdiezList) {
        this.sbsArchivoEdiezList = sbsArchivoEdiezList;
    }

    public List<GthCorreo> getGthCorreoList() {
        return gthCorreoList;
    }

    public void setGthCorreoList(List<GthCorreo> gthCorreoList) {
        this.gthCorreoList = gthCorreoList;
    }

    public List<GthArchivoEmpleado> getGthArchivoEmpleadoList() {
        return gthArchivoEmpleadoList;
    }

    public void setGthArchivoEmpleadoList(List<GthArchivoEmpleado> gthArchivoEmpleadoList) {
        this.gthArchivoEmpleadoList = gthArchivoEmpleadoList;
    }

    public List<GthAmigosEmpresaEmplea> getGthAmigosEmpresaEmpleaList() {
        return gthAmigosEmpresaEmpleaList;
    }

    public void setGthAmigosEmpresaEmpleaList(List<GthAmigosEmpresaEmplea> gthAmigosEmpresaEmpleaList) {
        this.gthAmigosEmpresaEmpleaList = gthAmigosEmpresaEmpleaList;
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

    public List<GthSituacionFinancieraEmple> getGthSituacionFinancieraEmpleList() {
        return gthSituacionFinancieraEmpleList;
    }

    public void setGthSituacionFinancieraEmpleList(List<GthSituacionFinancieraEmple> gthSituacionFinancieraEmpleList) {
        this.gthSituacionFinancieraEmpleList = gthSituacionFinancieraEmpleList;
    }

    public List<GthEducacionEmpleado> getGthEducacionEmpleadoList() {
        return gthEducacionEmpleadoList;
    }

    public void setGthEducacionEmpleadoList(List<GthEducacionEmpleado> gthEducacionEmpleadoList) {
        this.gthEducacionEmpleadoList = gthEducacionEmpleadoList;
    }

    public List<GthNegocioEmpleado> getGthNegocioEmpleadoList() {
        return gthNegocioEmpleadoList;
    }

    public void setGthNegocioEmpleadoList(List<GthNegocioEmpleado> gthNegocioEmpleadoList) {
        this.gthNegocioEmpleadoList = gthNegocioEmpleadoList;
    }

    public List<PreAdministradorContrato> getPreAdministradorContratoList() {
        return preAdministradorContratoList;
    }

    public void setPreAdministradorContratoList(List<PreAdministradorContrato> preAdministradorContratoList) {
        this.preAdministradorContratoList = preAdministradorContratoList;
    }

    public List<GthExperienciaLaboralEmplea> getGthExperienciaLaboralEmpleaList() {
        return gthExperienciaLaboralEmpleaList;
    }

    public void setGthExperienciaLaboralEmpleaList(List<GthExperienciaLaboralEmplea> gthExperienciaLaboralEmpleaList) {
        this.gthExperienciaLaboralEmpleaList = gthExperienciaLaboralEmpleaList;
    }

    public List<BisEntrevista> getBisEntrevistaList() {
        return bisEntrevistaList;
    }

    public void setBisEntrevistaList(List<BisEntrevista> bisEntrevistaList) {
        this.bisEntrevistaList = bisEntrevistaList;
    }

    public List<GthViaticos> getGthViaticosList() {
        return gthViaticosList;
    }

    public void setGthViaticosList(List<GthViaticos> gthViaticosList) {
        this.gthViaticosList = gthViaticosList;
    }

    public List<CrcAsistenteEvento> getCrcAsistenteEventoList() {
        return crcAsistenteEventoList;
    }

    public void setCrcAsistenteEventoList(List<CrcAsistenteEvento> crcAsistenteEventoList) {
        this.crcAsistenteEventoList = crcAsistenteEventoList;
    }

    public List<GthVehiculoEmpleado> getGthVehiculoEmpleadoList() {
        return gthVehiculoEmpleadoList;
    }

    public void setGthVehiculoEmpleadoList(List<GthVehiculoEmpleado> gthVehiculoEmpleadoList) {
        this.gthVehiculoEmpleadoList = gthVehiculoEmpleadoList;
    }

    public List<GthHobie> getGthHobieList() {
        return gthHobieList;
    }

    public void setGthHobieList(List<GthHobie> gthHobieList) {
        this.gthHobieList = gthHobieList;
    }

    public List<GthFamiliar> getGthFamiliarList() {
        return gthFamiliarList;
    }

    public void setGthFamiliarList(List<GthFamiliar> gthFamiliarList) {
        this.gthFamiliarList = gthFamiliarList;
    }

    public List<GthCargasFamiliares> getGthCargasFamiliaresList() {
        return gthCargasFamiliaresList;
    }

    public void setGthCargasFamiliaresList(List<GthCargasFamiliares> gthCargasFamiliaresList) {
        this.gthCargasFamiliaresList = gthCargasFamiliaresList;
    }

    public List<GthPersonaEmergencia> getGthPersonaEmergenciaList() {
        return gthPersonaEmergenciaList;
    }

    public void setGthPersonaEmergenciaList(List<GthPersonaEmergencia> gthPersonaEmergenciaList) {
        this.gthPersonaEmergenciaList = gthPersonaEmergenciaList;
    }

    public List<GthInversionEmpleado> getGthInversionEmpleadoList() {
        return gthInversionEmpleadoList;
    }

    public void setGthInversionEmpleadoList(List<GthInversionEmpleado> gthInversionEmpleadoList) {
        this.gthInversionEmpleadoList = gthInversionEmpleadoList;
    }

    public List<GthDocumentacionEmpleado> getGthDocumentacionEmpleadoList() {
        return gthDocumentacionEmpleadoList;
    }

    public void setGthDocumentacionEmpleadoList(List<GthDocumentacionEmpleado> gthDocumentacionEmpleadoList) {
        this.gthDocumentacionEmpleadoList = gthDocumentacionEmpleadoList;
    }

    public List<NrhRetencionJudicial> getNrhRetencionJudicialList() {
        return nrhRetencionJudicialList;
    }

    public void setNrhRetencionJudicialList(List<NrhRetencionJudicial> nrhRetencionJudicialList) {
        this.nrhRetencionJudicialList = nrhRetencionJudicialList;
    }

    
    public Integer getNumCargasGtemp() {
		return numCargasGtemp;
	}

	public void setNumCargasGtemp(Integer numCargasGtemp) {
		this.numCargasGtemp = numCargasGtemp;
	}




    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtemp != null ? ideGtemp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthEmpleado)) {
            return false;
        }
        GthEmpleado other = (GthEmpleado) object;
        if ((this.ideGtemp == null && other.ideGtemp != null) || (this.ideGtemp != null && !this.ideGtemp.equals(other.ideGtemp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthEmpleado[ ideGtemp=" + ideGtemp + " ]";
    }
    
	public boolean isEnfermedadCatastroficaGtemp() {
		return enfermedadCatastroficaGtemp;
	}

	public void setEnfermedadCatastroficaGtemp(boolean enfermedadCatastroficaGtemp) {
		this.enfermedadCatastroficaGtemp = enfermedadCatastroficaGtemp;
	}

    
    
}
