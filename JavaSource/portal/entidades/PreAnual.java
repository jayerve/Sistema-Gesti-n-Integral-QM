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
@Table(name = "pre_anual", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreAnual.findAll", query = "SELECT p FROM PreAnual p"),
    @NamedQuery(name = "PreAnual.findByIdePranu", query = "SELECT p FROM PreAnual p WHERE p.idePranu = :idePranu"),
    @NamedQuery(name = "PreAnual.findByValorReformadoPranu", query = "SELECT p FROM PreAnual p WHERE p.valorReformadoPranu = :valorReformadoPranu"),
    @NamedQuery(name = "PreAnual.findByValorInicialPranu", query = "SELECT p FROM PreAnual p WHERE p.valorInicialPranu = :valorInicialPranu"),
    @NamedQuery(name = "PreAnual.findByValorCodificadoPranu", query = "SELECT p FROM PreAnual p WHERE p.valorCodificadoPranu = :valorCodificadoPranu"),
    @NamedQuery(name = "PreAnual.findByValorReformadoHPranu", query = "SELECT p FROM PreAnual p WHERE p.valorReformadoHPranu = :valorReformadoHPranu"),
    @NamedQuery(name = "PreAnual.findByValorReformadoDPranu", query = "SELECT p FROM PreAnual p WHERE p.valorReformadoDPranu = :valorReformadoDPranu"),
    @NamedQuery(name = "PreAnual.findByValorDevengadoPranu", query = "SELECT p FROM PreAnual p WHERE p.valorDevengadoPranu = :valorDevengadoPranu"),
    @NamedQuery(name = "PreAnual.findByValorPrecomprometidoPranu", query = "SELECT p FROM PreAnual p WHERE p.valorPrecomprometidoPranu = :valorPrecomprometidoPranu"),
    @NamedQuery(name = "PreAnual.findByValorEjeComprometidoPranu", query = "SELECT p FROM PreAnual p WHERE p.valorEjeComprometidoPranu = :valorEjeComprometidoPranu"),
    @NamedQuery(name = "PreAnual.findByValorRecaudadoPranu", query = "SELECT p FROM PreAnual p WHERE p.valorRecaudadoPranu = :valorRecaudadoPranu"),
    @NamedQuery(name = "PreAnual.findByValorRecaudadoEfectivoPranu", query = "SELECT p FROM PreAnual p WHERE p.valorRecaudadoEfectivoPranu = :valorRecaudadoEfectivoPranu"),
    @NamedQuery(name = "PreAnual.findByActivoPrean", query = "SELECT p FROM PreAnual p WHERE p.activoPrean = :activoPrean"),
    @NamedQuery(name = "PreAnual.findByUsuarioIngre", query = "SELECT p FROM PreAnual p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreAnual.findByFechaIngre", query = "SELECT p FROM PreAnual p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreAnual.findByHoraIngre", query = "SELECT p FROM PreAnual p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreAnual.findByUsuarioActua", query = "SELECT p FROM PreAnual p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreAnual.findByFechaActua", query = "SELECT p FROM PreAnual p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreAnual.findByHoraActua", query = "SELECT p FROM PreAnual p WHERE p.horaActua = :horaActua")})
public class PreAnual implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_pranu", nullable = false)
    private Long idePranu;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_reformado_pranu", precision = 10, scale = 3)
    private BigDecimal valorReformadoPranu;
    @Column(name = "valor_inicial_pranu", precision = 10, scale = 3)
    private BigDecimal valorInicialPranu;
    @Column(name = "valor_codificado_pranu", precision = 10, scale = 3)
    private BigDecimal valorCodificadoPranu;
    @Column(name = "valor_reformado_h_pranu", precision = 10, scale = 3)
    private BigDecimal valorReformadoHPranu;
    @Column(name = "valor_reformado_d_pranu", precision = 10, scale = 3)
    private BigDecimal valorReformadoDPranu;
    @Column(name = "valor_devengado_pranu", precision = 10, scale = 3)
    private BigDecimal valorDevengadoPranu;
    @Column(name = "valor_precomprometido_pranu", precision = 10, scale = 3)
    private BigDecimal valorPrecomprometidoPranu;
    @Column(name = "valor_eje_comprometido_pranu", precision = 10, scale = 3)
    private BigDecimal valorEjeComprometidoPranu;
    @Column(name = "valor_recaudado_pranu", precision = 10, scale = 3)
    private BigDecimal valorRecaudadoPranu;
    @Column(name = "valor_recaudado_efectivo_pranu", precision = 10, scale = 3)
    private BigDecimal valorRecaudadoEfectivoPranu;
    @Column(name = "activo_prean")
    private Boolean activoPrean;
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
    @OneToMany(mappedBy = "idePranu")
    private List<PreReformaMes> preReformaMesList;
    @JoinColumn(name = "ide_prpro", referencedColumnName = "ide_prpro")
    @ManyToOne
    private PrePrograma idePrpro;
    @JoinColumn(name = "ide_prfup", referencedColumnName = "ide_prfup")
    @ManyToOne
    private PreFuncionPrograma idePrfup;
    @JoinColumn(name = "ide_prcla", referencedColumnName = "ide_prcla")
    @ManyToOne
    private PreClasificador idePrcla;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;

    public PreAnual() {
    }

    public PreAnual(Long idePranu) {
        this.idePranu = idePranu;
    }

    public Long getIdePranu() {
        return idePranu;
    }

    public void setIdePranu(Long idePranu) {
        this.idePranu = idePranu;
    }

    public BigDecimal getValorReformadoPranu() {
        return valorReformadoPranu;
    }

    public void setValorReformadoPranu(BigDecimal valorReformadoPranu) {
        this.valorReformadoPranu = valorReformadoPranu;
    }

    public BigDecimal getValorInicialPranu() {
        return valorInicialPranu;
    }

    public void setValorInicialPranu(BigDecimal valorInicialPranu) {
        this.valorInicialPranu = valorInicialPranu;
    }

    public BigDecimal getValorCodificadoPranu() {
        return valorCodificadoPranu;
    }

    public void setValorCodificadoPranu(BigDecimal valorCodificadoPranu) {
        this.valorCodificadoPranu = valorCodificadoPranu;
    }

    public BigDecimal getValorReformadoHPranu() {
        return valorReformadoHPranu;
    }

    public void setValorReformadoHPranu(BigDecimal valorReformadoHPranu) {
        this.valorReformadoHPranu = valorReformadoHPranu;
    }

    public BigDecimal getValorReformadoDPranu() {
        return valorReformadoDPranu;
    }

    public void setValorReformadoDPranu(BigDecimal valorReformadoDPranu) {
        this.valorReformadoDPranu = valorReformadoDPranu;
    }

    public BigDecimal getValorDevengadoPranu() {
        return valorDevengadoPranu;
    }

    public void setValorDevengadoPranu(BigDecimal valorDevengadoPranu) {
        this.valorDevengadoPranu = valorDevengadoPranu;
    }

    public BigDecimal getValorPrecomprometidoPranu() {
        return valorPrecomprometidoPranu;
    }

    public void setValorPrecomprometidoPranu(BigDecimal valorPrecomprometidoPranu) {
        this.valorPrecomprometidoPranu = valorPrecomprometidoPranu;
    }

    public BigDecimal getValorEjeComprometidoPranu() {
        return valorEjeComprometidoPranu;
    }

    public void setValorEjeComprometidoPranu(BigDecimal valorEjeComprometidoPranu) {
        this.valorEjeComprometidoPranu = valorEjeComprometidoPranu;
    }

    public BigDecimal getValorRecaudadoPranu() {
        return valorRecaudadoPranu;
    }

    public void setValorRecaudadoPranu(BigDecimal valorRecaudadoPranu) {
        this.valorRecaudadoPranu = valorRecaudadoPranu;
    }

    public BigDecimal getValorRecaudadoEfectivoPranu() {
        return valorRecaudadoEfectivoPranu;
    }

    public void setValorRecaudadoEfectivoPranu(BigDecimal valorRecaudadoEfectivoPranu) {
        this.valorRecaudadoEfectivoPranu = valorRecaudadoEfectivoPranu;
    }

    public Boolean getActivoPrean() {
        return activoPrean;
    }

    public void setActivoPrean(Boolean activoPrean) {
        this.activoPrean = activoPrean;
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

    public List<PreReformaMes> getPreReformaMesList() {
        return preReformaMesList;
    }

    public void setPreReformaMesList(List<PreReformaMes> preReformaMesList) {
        this.preReformaMesList = preReformaMesList;
    }

    public PrePrograma getIdePrpro() {
        return idePrpro;
    }

    public void setIdePrpro(PrePrograma idePrpro) {
        this.idePrpro = idePrpro;
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

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePranu != null ? idePranu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreAnual)) {
            return false;
        }
        PreAnual other = (PreAnual) object;
        if ((this.idePranu == null && other.idePranu != null) || (this.idePranu != null && !this.idePranu.equals(other.idePranu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreAnual[ idePranu=" + idePranu + " ]";
    }
    
}
