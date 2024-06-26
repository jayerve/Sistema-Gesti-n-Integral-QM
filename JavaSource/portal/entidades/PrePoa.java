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
@Table(name = "pre_poa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PrePoa.findAll", query = "SELECT p FROM PrePoa p"),
    @NamedQuery(name = "PrePoa.findByIdePrpoa", query = "SELECT p FROM PrePoa p WHERE p.idePrpoa = :idePrpoa"),
    @NamedQuery(name = "PrePoa.findByObjetoProgramaPrpoa", query = "SELECT p FROM PrePoa p WHERE p.objetoProgramaPrpoa = :objetoProgramaPrpoa"),
    @NamedQuery(name = "PrePoa.findByObjetivoProyectoPrpoa", query = "SELECT p FROM PrePoa p WHERE p.objetivoProyectoPrpoa = :objetivoProyectoPrpoa"),
    @NamedQuery(name = "PrePoa.findByMetaProyectoPrpoa", query = "SELECT p FROM PrePoa p WHERE p.metaProyectoPrpoa = :metaProyectoPrpoa"),
    @NamedQuery(name = "PrePoa.findByFechaInicioPrpoa", query = "SELECT p FROM PrePoa p WHERE p.fechaInicioPrpoa = :fechaInicioPrpoa"),
    @NamedQuery(name = "PrePoa.findByFechaFinPrpoa", query = "SELECT p FROM PrePoa p WHERE p.fechaFinPrpoa = :fechaFinPrpoa"),
    @NamedQuery(name = "PrePoa.findByNumResolucionPrpoa", query = "SELECT p FROM PrePoa p WHERE p.numResolucionPrpoa = :numResolucionPrpoa"),
    @NamedQuery(name = "PrePoa.findByPresupuestoInicialPrpoa", query = "SELECT p FROM PrePoa p WHERE p.presupuestoInicialPrpoa = :presupuestoInicialPrpoa"),
    @NamedQuery(name = "PrePoa.findByPresupuestoCodificadoPrpoa", query = "SELECT p FROM PrePoa p WHERE p.presupuestoCodificadoPrpoa = :presupuestoCodificadoPrpoa"),
    @NamedQuery(name = "PrePoa.findByActivoPrpoa", query = "SELECT p FROM PrePoa p WHERE p.activoPrpoa = :activoPrpoa"),
    @NamedQuery(name = "PrePoa.findByUsuarioIngre", query = "SELECT p FROM PrePoa p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PrePoa.findByFechaIngre", query = "SELECT p FROM PrePoa p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PrePoa.findByHoraIngre", query = "SELECT p FROM PrePoa p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PrePoa.findByUsuarioActua", query = "SELECT p FROM PrePoa p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PrePoa.findByFechaActua", query = "SELECT p FROM PrePoa p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PrePoa.findByHoraActua", query = "SELECT p FROM PrePoa p WHERE p.horaActua = :horaActua")})
public class PrePoa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prpoa", nullable = false)
    private Long idePrpoa;
    @Size(max = 2147483647)
    @Column(name = "objeto_programa_prpoa", length = 2147483647)
    private String objetoProgramaPrpoa;
    @Size(max = 2147483647)
    @Column(name = "objetivo_proyecto_prpoa", length = 2147483647)
    private String objetivoProyectoPrpoa;
    @Size(max = 2147483647)
    @Column(name = "meta_proyecto_prpoa", length = 2147483647)
    private String metaProyectoPrpoa;
    @Column(name = "fecha_inicio_prpoa")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioPrpoa;
    @Column(name = "fecha_fin_prpoa")
    @Temporal(TemporalType.DATE)
    private Date fechaFinPrpoa;
    @Size(max = 50)
    @Column(name = "num_resolucion_prpoa", length = 50)
    private String numResolucionPrpoa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "presupuesto_inicial_prpoa", precision = 10, scale = 2)
    private BigDecimal presupuestoInicialPrpoa;
    @Column(name = "presupuesto_codificado_prpoa", precision = 10, scale = 2)
    private BigDecimal presupuestoCodificadoPrpoa;
    @Column(name = "activo_prpoa")
    private Boolean activoPrpoa;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "idePrpoa")
    private List<PrePoaMes> prePoaMesList;
    @OneToMany(mappedBy = "idePrpoa")
    private List<PreArchivo> preArchivoList;
    @OneToMany(mappedBy = "idePrpoa")
    private List<PrePoaReforma> prePoaReformaList;
    @OneToMany(mappedBy = "preIdePrpoa")
    private List<PrePoaReforma> prePoaReformaList1;
    @OneToMany(mappedBy = "idePrpoa")
    private List<PrePoaTramite> prePoaTramiteList;
    @JoinColumn(name = "ide_prsua", referencedColumnName = "ide_prsua")
    @ManyToOne
    private PreSubActividad idePrsua;
    @JoinColumn(name = "ide_prfup", referencedColumnName = "ide_prfup")
    @ManyToOne
    private PreFuncionPrograma idePrfup;
    @JoinColumn(name = "ide_prcla", referencedColumnName = "ide_prcla")
    @ManyToOne
    private PreClasificador idePrcla;
    @JoinColumn(name = "ide_geuna", referencedColumnName = "ide_geuna")
    @ManyToOne
    private GenUnidadAdministrativa ideGeuna;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;

    public PrePoa() {
    }

    public PrePoa(Long idePrpoa) {
        this.idePrpoa = idePrpoa;
    }

    public Long getIdePrpoa() {
        return idePrpoa;
    }

    public void setIdePrpoa(Long idePrpoa) {
        this.idePrpoa = idePrpoa;
    }

    public String getObjetoProgramaPrpoa() {
        return objetoProgramaPrpoa;
    }

    public void setObjetoProgramaPrpoa(String objetoProgramaPrpoa) {
        this.objetoProgramaPrpoa = objetoProgramaPrpoa;
    }

    public String getObjetivoProyectoPrpoa() {
        return objetivoProyectoPrpoa;
    }

    public void setObjetivoProyectoPrpoa(String objetivoProyectoPrpoa) {
        this.objetivoProyectoPrpoa = objetivoProyectoPrpoa;
    }

    public String getMetaProyectoPrpoa() {
        return metaProyectoPrpoa;
    }

    public void setMetaProyectoPrpoa(String metaProyectoPrpoa) {
        this.metaProyectoPrpoa = metaProyectoPrpoa;
    }

    public Date getFechaInicioPrpoa() {
        return fechaInicioPrpoa;
    }

    public void setFechaInicioPrpoa(Date fechaInicioPrpoa) {
        this.fechaInicioPrpoa = fechaInicioPrpoa;
    }

    public Date getFechaFinPrpoa() {
        return fechaFinPrpoa;
    }

    public void setFechaFinPrpoa(Date fechaFinPrpoa) {
        this.fechaFinPrpoa = fechaFinPrpoa;
    }

    public String getNumResolucionPrpoa() {
        return numResolucionPrpoa;
    }

    public void setNumResolucionPrpoa(String numResolucionPrpoa) {
        this.numResolucionPrpoa = numResolucionPrpoa;
    }

    public BigDecimal getPresupuestoInicialPrpoa() {
        return presupuestoInicialPrpoa;
    }

    public void setPresupuestoInicialPrpoa(BigDecimal presupuestoInicialPrpoa) {
        this.presupuestoInicialPrpoa = presupuestoInicialPrpoa;
    }

    public BigDecimal getPresupuestoCodificadoPrpoa() {
        return presupuestoCodificadoPrpoa;
    }

    public void setPresupuestoCodificadoPrpoa(BigDecimal presupuestoCodificadoPrpoa) {
        this.presupuestoCodificadoPrpoa = presupuestoCodificadoPrpoa;
    }

    public Boolean getActivoPrpoa() {
        return activoPrpoa;
    }

    public void setActivoPrpoa(Boolean activoPrpoa) {
        this.activoPrpoa = activoPrpoa;
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

    public List<PrePoaMes> getPrePoaMesList() {
        return prePoaMesList;
    }

    public void setPrePoaMesList(List<PrePoaMes> prePoaMesList) {
        this.prePoaMesList = prePoaMesList;
    }

    public List<PreArchivo> getPreArchivoList() {
        return preArchivoList;
    }

    public void setPreArchivoList(List<PreArchivo> preArchivoList) {
        this.preArchivoList = preArchivoList;
    }

    public List<PrePoaReforma> getPrePoaReformaList() {
        return prePoaReformaList;
    }

    public void setPrePoaReformaList(List<PrePoaReforma> prePoaReformaList) {
        this.prePoaReformaList = prePoaReformaList;
    }

    public List<PrePoaReforma> getPrePoaReformaList1() {
        return prePoaReformaList1;
    }

    public void setPrePoaReformaList1(List<PrePoaReforma> prePoaReformaList1) {
        this.prePoaReformaList1 = prePoaReformaList1;
    }

    public List<PrePoaTramite> getPrePoaTramiteList() {
        return prePoaTramiteList;
    }

    public void setPrePoaTramiteList(List<PrePoaTramite> prePoaTramiteList) {
        this.prePoaTramiteList = prePoaTramiteList;
    }

    public PreSubActividad getIdePrsua() {
        return idePrsua;
    }

    public void setIdePrsua(PreSubActividad idePrsua) {
        this.idePrsua = idePrsua;
    }

    public PreFuncionPrograma getIdePrfup() {
        return idePrfup;
    }

    public void setIdePrfup(PreFuncionPrograma idePrfup) {
        this.idePrfup = idePrfup;
    }

    public PreClasificador getIdePrcla() {
        return idePrcla;
    }

    public void setIdePrcla(PreClasificador idePrcla) {
        this.idePrcla = idePrcla;
    }

    public GenUnidadAdministrativa getIdeGeuna() {
        return ideGeuna;
    }

    public void setIdeGeuna(GenUnidadAdministrativa ideGeuna) {
        this.ideGeuna = ideGeuna;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrpoa != null ? idePrpoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrePoa)) {
            return false;
        }
        PrePoa other = (PrePoa) object;
        if ((this.idePrpoa == null && other.idePrpoa != null) || (this.idePrpoa != null && !this.idePrpoa.equals(other.idePrpoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PrePoa[ idePrpoa=" + idePrpoa + " ]";
    }
    
}
