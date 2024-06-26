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
@Table(name = "sao_ficha_medica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoFichaMedica.findAll", query = "SELECT s FROM SaoFichaMedica s"),
    @NamedQuery(name = "SaoFichaMedica.findByIdeSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.ideSafim = :ideSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByIdeSucu", query = "SELECT s FROM SaoFichaMedica s WHERE s.ideSucu = :ideSucu"),
    @NamedQuery(name = "SaoFichaMedica.findByAnioEdadSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.anioEdadSafim = :anioEdadSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByMesEdadSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.mesEdadSafim = :mesEdadSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByPesoSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.pesoSafim = :pesoSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByTallaSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.tallaSafim = :tallaSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByImcSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.imcSafim = :imcSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByTemperaturaSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.temperaturaSafim = :temperaturaSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByPulsoSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.pulsoSafim = :pulsoSafim"),
    @NamedQuery(name = "SaoFichaMedica.findBySuperficieCorporalSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.superficieCorporalSafim = :superficieCorporalSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByFreRespiratoriaSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.freRespiratoriaSafim = :freRespiratoriaSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByFreCardiacaSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.freCardiacaSafim = :freCardiacaSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByFechaConsultaSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.fechaConsultaSafim = :fechaConsultaSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByFechaDesdeCertSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.fechaDesdeCertSafim = :fechaDesdeCertSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByFechaHastaCertSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.fechaHastaCertSafim = :fechaHastaCertSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByNroDiaCertSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.nroDiaCertSafim = :nroDiaCertSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByObservacionesSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.observacionesSafim = :observacionesSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByActivoSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.activoSafim = :activoSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByUsuarioIngre", query = "SELECT s FROM SaoFichaMedica s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoFichaMedica.findByFechaIngre", query = "SELECT s FROM SaoFichaMedica s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoFichaMedica.findByUsuarioActua", query = "SELECT s FROM SaoFichaMedica s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoFichaMedica.findByFechaActua", query = "SELECT s FROM SaoFichaMedica s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoFichaMedica.findByHoraActua", query = "SELECT s FROM SaoFichaMedica s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SaoFichaMedica.findByPresuntivoSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.presuntivoSafim = :presuntivoSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByDefinitivoSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.definitivoSafim = :definitivoSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByPrimeraSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.primeraSafim = :primeraSafim"),
    @NamedQuery(name = "SaoFichaMedica.findBySubsecuenteSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.subsecuenteSafim = :subsecuenteSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByInterconsultaSafim", query = "SELECT s FROM SaoFichaMedica s WHERE s.interconsultaSafim = :interconsultaSafim"),
    @NamedQuery(name = "SaoFichaMedica.findByHoraIngre", query = "SELECT s FROM SaoFichaMedica s WHERE s.horaIngre = :horaIngre")})
public class SaoFichaMedica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_safim", nullable = false)
    private Integer ideSafim;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "anio_edad_safim")
    private Integer anioEdadSafim;
    @Column(name = "mes_edad_safim")
    private Integer mesEdadSafim;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso_safim", precision = 12, scale = 2)
    private BigDecimal pesoSafim;
    @Column(name = "talla_safim", precision = 12, scale = 2)
    private BigDecimal tallaSafim;
    @Column(name = "imc_safim", precision = 12, scale = 2)
    private BigDecimal imcSafim;
    @Column(name = "temperatura_safim")
    private Integer temperaturaSafim;
    @Column(name = "pulso_safim")
    private Integer pulsoSafim;
    @Column(name = "superficie_corporal_safim", precision = 12, scale = 3)
    private BigDecimal superficieCorporalSafim;
    @Column(name = "fre_respiratoria_safim")
    private Integer freRespiratoriaSafim;
    @Column(name = "fre_cardiaca_safim")
    private Integer freCardiacaSafim;
    @Column(name = "fecha_consulta_safim")
    @Temporal(TemporalType.DATE)
    private Date fechaConsultaSafim;
    @Column(name = "fecha_desde_cert_safim")
    @Temporal(TemporalType.DATE)
    private Date fechaDesdeCertSafim;
    @Column(name = "fecha_hasta_cert_safim")
    @Temporal(TemporalType.DATE)
    private Date fechaHastaCertSafim;
    @Column(name = "nro_dia_cert_safim")
    private Integer nroDiaCertSafim;
    @Size(max = 4000)
    @Column(name = "observaciones_safim", length = 4000)
    private String observacionesSafim;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_safim", nullable = false)
    private boolean activoSafim;
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
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @Column(name = "presuntivo_safim")
    private Boolean presuntivoSafim;
    @Column(name = "definitivo_safim")
    private Boolean definitivoSafim;
    @Column(name = "primera_safim")
    private Boolean primeraSafim;
    @Column(name = "subsecuente_safim")
    private Boolean subsecuenteSafim;
    @Column(name = "interconsulta_safim")
    private Boolean interconsultaSafim;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @OneToMany(mappedBy = "ideSafim")
    private List<SaoFichaExamenes> saoFichaExamenesList;
    @OneToMany(mappedBy = "ideSafim")
    private List<SaoFichaMotivoConsulta> saoFichaMotivoConsultaList;
    @OneToMany(mappedBy = "ideSafim")
    private List<SaoRecetaMedica> saoRecetaMedicaList;
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;
    @JoinColumn(name = "ide_satic", referencedColumnName = "ide_satic")
    @ManyToOne
    private SaoTipoConsulta ideSatic;
    @JoinColumn(name = "ide_saesp", referencedColumnName = "ide_saesp")
    @ManyToOne
    private SaoEspecialidad ideSaesp;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @OneToMany(mappedBy = "ideSafim")
    private List<SaoFichaAnamnesis> saoFichaAnamnesisList;
    @OneToMany(mappedBy = "ideSafim")
    private List<SaoFichaDiagnostico> saoFichaDiagnosticoList;

    public SaoFichaMedica() {
    }

    public SaoFichaMedica(Integer ideSafim) {
        this.ideSafim = ideSafim;
    }

    public SaoFichaMedica(Integer ideSafim, boolean activoSafim) {
        this.ideSafim = ideSafim;
        this.activoSafim = activoSafim;
    }

    public Integer getIdeSafim() {
        return ideSafim;
    }

    public void setIdeSafim(Integer ideSafim) {
        this.ideSafim = ideSafim;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Integer getAnioEdadSafim() {
        return anioEdadSafim;
    }

    public void setAnioEdadSafim(Integer anioEdadSafim) {
        this.anioEdadSafim = anioEdadSafim;
    }

    public Integer getMesEdadSafim() {
        return mesEdadSafim;
    }

    public void setMesEdadSafim(Integer mesEdadSafim) {
        this.mesEdadSafim = mesEdadSafim;
    }

    public BigDecimal getPesoSafim() {
        return pesoSafim;
    }

    public void setPesoSafim(BigDecimal pesoSafim) {
        this.pesoSafim = pesoSafim;
    }

    public BigDecimal getTallaSafim() {
        return tallaSafim;
    }

    public void setTallaSafim(BigDecimal tallaSafim) {
        this.tallaSafim = tallaSafim;
    }

    public BigDecimal getImcSafim() {
        return imcSafim;
    }

    public void setImcSafim(BigDecimal imcSafim) {
        this.imcSafim = imcSafim;
    }

    public Integer getTemperaturaSafim() {
        return temperaturaSafim;
    }

    public void setTemperaturaSafim(Integer temperaturaSafim) {
        this.temperaturaSafim = temperaturaSafim;
    }

    public Integer getPulsoSafim() {
        return pulsoSafim;
    }

    public void setPulsoSafim(Integer pulsoSafim) {
        this.pulsoSafim = pulsoSafim;
    }

    public BigDecimal getSuperficieCorporalSafim() {
        return superficieCorporalSafim;
    }

    public void setSuperficieCorporalSafim(BigDecimal superficieCorporalSafim) {
        this.superficieCorporalSafim = superficieCorporalSafim;
    }

    public Integer getFreRespiratoriaSafim() {
        return freRespiratoriaSafim;
    }

    public void setFreRespiratoriaSafim(Integer freRespiratoriaSafim) {
        this.freRespiratoriaSafim = freRespiratoriaSafim;
    }

    public Integer getFreCardiacaSafim() {
        return freCardiacaSafim;
    }

    public void setFreCardiacaSafim(Integer freCardiacaSafim) {
        this.freCardiacaSafim = freCardiacaSafim;
    }

    public Date getFechaConsultaSafim() {
        return fechaConsultaSafim;
    }

    public void setFechaConsultaSafim(Date fechaConsultaSafim) {
        this.fechaConsultaSafim = fechaConsultaSafim;
    }

    public Date getFechaDesdeCertSafim() {
        return fechaDesdeCertSafim;
    }

    public void setFechaDesdeCertSafim(Date fechaDesdeCertSafim) {
        this.fechaDesdeCertSafim = fechaDesdeCertSafim;
    }

    public Date getFechaHastaCertSafim() {
        return fechaHastaCertSafim;
    }

    public void setFechaHastaCertSafim(Date fechaHastaCertSafim) {
        this.fechaHastaCertSafim = fechaHastaCertSafim;
    }

    public Integer getNroDiaCertSafim() {
        return nroDiaCertSafim;
    }

    public void setNroDiaCertSafim(Integer nroDiaCertSafim) {
        this.nroDiaCertSafim = nroDiaCertSafim;
    }

    public String getObservacionesSafim() {
        return observacionesSafim;
    }

    public void setObservacionesSafim(String observacionesSafim) {
        this.observacionesSafim = observacionesSafim;
    }

    public boolean getActivoSafim() {
        return activoSafim;
    }

    public void setActivoSafim(boolean activoSafim) {
        this.activoSafim = activoSafim;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public Boolean getPresuntivoSafim() {
        return presuntivoSafim;
    }

    public void setPresuntivoSafim(Boolean presuntivoSafim) {
        this.presuntivoSafim = presuntivoSafim;
    }

    public Boolean getDefinitivoSafim() {
        return definitivoSafim;
    }

    public void setDefinitivoSafim(Boolean definitivoSafim) {
        this.definitivoSafim = definitivoSafim;
    }

    public Boolean getPrimeraSafim() {
        return primeraSafim;
    }

    public void setPrimeraSafim(Boolean primeraSafim) {
        this.primeraSafim = primeraSafim;
    }

    public Boolean getSubsecuenteSafim() {
        return subsecuenteSafim;
    }

    public void setSubsecuenteSafim(Boolean subsecuenteSafim) {
        this.subsecuenteSafim = subsecuenteSafim;
    }

    public Boolean getInterconsultaSafim() {
        return interconsultaSafim;
    }

    public void setInterconsultaSafim(Boolean interconsultaSafim) {
        this.interconsultaSafim = interconsultaSafim;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public List<SaoFichaExamenes> getSaoFichaExamenesList() {
        return saoFichaExamenesList;
    }

    public void setSaoFichaExamenesList(List<SaoFichaExamenes> saoFichaExamenesList) {
        this.saoFichaExamenesList = saoFichaExamenesList;
    }

    public List<SaoFichaMotivoConsulta> getSaoFichaMotivoConsultaList() {
        return saoFichaMotivoConsultaList;
    }

    public void setSaoFichaMotivoConsultaList(List<SaoFichaMotivoConsulta> saoFichaMotivoConsultaList) {
        this.saoFichaMotivoConsultaList = saoFichaMotivoConsultaList;
    }

    public List<SaoRecetaMedica> getSaoRecetaMedicaList() {
        return saoRecetaMedicaList;
    }

    public void setSaoRecetaMedicaList(List<SaoRecetaMedica> saoRecetaMedicaList) {
        this.saoRecetaMedicaList = saoRecetaMedicaList;
    }

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    public SaoTipoConsulta getIdeSatic() {
        return ideSatic;
    }

    public void setIdeSatic(SaoTipoConsulta ideSatic) {
        this.ideSatic = ideSatic;
    }

    public SaoEspecialidad getIdeSaesp() {
        return ideSaesp;
    }

    public void setIdeSaesp(SaoEspecialidad ideSaesp) {
        this.ideSaesp = ideSaesp;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public List<SaoFichaAnamnesis> getSaoFichaAnamnesisList() {
        return saoFichaAnamnesisList;
    }

    public void setSaoFichaAnamnesisList(List<SaoFichaAnamnesis> saoFichaAnamnesisList) {
        this.saoFichaAnamnesisList = saoFichaAnamnesisList;
    }

    public List<SaoFichaDiagnostico> getSaoFichaDiagnosticoList() {
        return saoFichaDiagnosticoList;
    }

    public void setSaoFichaDiagnosticoList(List<SaoFichaDiagnostico> saoFichaDiagnosticoList) {
        this.saoFichaDiagnosticoList = saoFichaDiagnosticoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSafim != null ? ideSafim.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoFichaMedica)) {
            return false;
        }
        SaoFichaMedica other = (SaoFichaMedica) object;
        if ((this.ideSafim == null && other.ideSafim != null) || (this.ideSafim != null && !this.ideSafim.equals(other.ideSafim))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoFichaMedica[ ideSafim=" + ideSafim + " ]";
    }
    
}
