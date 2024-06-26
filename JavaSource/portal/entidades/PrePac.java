/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "pre_pac", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PrePac.findAll", query = "SELECT p FROM PrePac p"),
    @NamedQuery(name = "PrePac.findByIdePrpac", query = "SELECT p FROM PrePac p WHERE p.idePrpac = :idePrpac"),
    @NamedQuery(name = "PrePac.findByNroOrdenPrpac", query = "SELECT p FROM PrePac p WHERE p.nroOrdenPrpac = :nroOrdenPrpac"),
    @NamedQuery(name = "PrePac.findByCpcPrpac", query = "SELECT p FROM PrePac p WHERE p.cpcPrpac = :cpcPrpac"),
    @NamedQuery(name = "PrePac.findByDescripcionPrpac", query = "SELECT p FROM PrePac p WHERE p.descripcionPrpac = :descripcionPrpac"),
    @NamedQuery(name = "PrePac.findByValorUnitarioPrpac", query = "SELECT p FROM PrePac p WHERE p.valorUnitarioPrpac = :valorUnitarioPrpac"),
    @NamedQuery(name = "PrePac.findByValorTotalPrpac", query = "SELECT p FROM PrePac p WHERE p.valorTotalPrpac = :valorTotalPrpac"),
    @NamedQuery(name = "PrePac.findByActivoPrpac", query = "SELECT p FROM PrePac p WHERE p.activoPrpac = :activoPrpac"),
    @NamedQuery(name = "PrePac.findByUsuarioIngre", query = "SELECT p FROM PrePac p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PrePac.findByFechaIngre", query = "SELECT p FROM PrePac p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PrePac.findByHoraIngre", query = "SELECT p FROM PrePac p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PrePac.findByUsuarioActua", query = "SELECT p FROM PrePac p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PrePac.findByFechaActua", query = "SELECT p FROM PrePac p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PrePac.findByHoraActua", query = "SELECT p FROM PrePac p WHERE p.horaActua = :horaActua")})
public class PrePac implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prpac", nullable = false)
    private Long idePrpac;
    @Column(name = "nro_orden_prpac")
    private BigInteger nroOrdenPrpac;
    @Size(max = 50)
    @Column(name = "cpc_prpac", length = 50)
    private String cpcPrpac;
    @Size(max = 2147483647)
    @Column(name = "descripcion_prpac", length = 2147483647)
    private String descripcionPrpac;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_unitario_prpac", precision = 10, scale = 2)
    private BigDecimal valorUnitarioPrpac;
    @Column(name = "valor_total_prpac", precision = 10, scale = 2)
    private BigDecimal valorTotalPrpac;
    @Column(name = "activo_prpac")
    private Boolean activoPrpac;
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
    @OneToMany(mappedBy = "idePrpac")
    private List<PreContratacionPublica> preContratacionPublicaList;
    @OneToMany(mappedBy = "idePrpac")
    private List<PreArchivo> preArchivoList;
    @JoinColumn(name = "ide_prcla", referencedColumnName = "ide_prcla")
    @ManyToOne
    private PreClasificador idePrcla;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @JoinColumn(name = "ide_cotio", referencedColumnName = "ide_cotio")
    @ManyToOne
    private ContTipoCompra ideCotio;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @JoinColumn(name = "ide_bounm", referencedColumnName = "ide_bounm")
    @ManyToOne
    private BodtUnidadMedida ideBounm;
    @OneToMany(mappedBy = "idePrpac")
    private List<PrePartidaPac> prePartidaPacList;
    @OneToMany(mappedBy = "idePrpac")
    private List<PrePacPeriodo> prePacPeriodoList;

    public PrePac() {
    }

    public PrePac(Long idePrpac) {
        this.idePrpac = idePrpac;
    }

    public Long getIdePrpac() {
        return idePrpac;
    }

    public void setIdePrpac(Long idePrpac) {
        this.idePrpac = idePrpac;
    }

    public BigInteger getNroOrdenPrpac() {
        return nroOrdenPrpac;
    }

    public void setNroOrdenPrpac(BigInteger nroOrdenPrpac) {
        this.nroOrdenPrpac = nroOrdenPrpac;
    }

    public String getCpcPrpac() {
        return cpcPrpac;
    }

    public void setCpcPrpac(String cpcPrpac) {
        this.cpcPrpac = cpcPrpac;
    }

    public String getDescripcionPrpac() {
        return descripcionPrpac;
    }

    public void setDescripcionPrpac(String descripcionPrpac) {
        this.descripcionPrpac = descripcionPrpac;
    }

    public BigDecimal getValorUnitarioPrpac() {
        return valorUnitarioPrpac;
    }

    public void setValorUnitarioPrpac(BigDecimal valorUnitarioPrpac) {
        this.valorUnitarioPrpac = valorUnitarioPrpac;
    }

    public BigDecimal getValorTotalPrpac() {
        return valorTotalPrpac;
    }

    public void setValorTotalPrpac(BigDecimal valorTotalPrpac) {
        this.valorTotalPrpac = valorTotalPrpac;
    }

    public Boolean getActivoPrpac() {
        return activoPrpac;
    }

    public void setActivoPrpac(Boolean activoPrpac) {
        this.activoPrpac = activoPrpac;
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

    public List<PreContratacionPublica> getPreContratacionPublicaList() {
        return preContratacionPublicaList;
    }

    public void setPreContratacionPublicaList(List<PreContratacionPublica> preContratacionPublicaList) {
        this.preContratacionPublicaList = preContratacionPublicaList;
    }

    public List<PreArchivo> getPreArchivoList() {
        return preArchivoList;
    }

    public void setPreArchivoList(List<PreArchivo> preArchivoList) {
        this.preArchivoList = preArchivoList;
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

    public ContTipoCompra getIdeCotio() {
        return ideCotio;
    }

    public void setIdeCotio(ContTipoCompra ideCotio) {
        this.ideCotio = ideCotio;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public BodtUnidadMedida getIdeBounm() {
        return ideBounm;
    }

    public void setIdeBounm(BodtUnidadMedida ideBounm) {
        this.ideBounm = ideBounm;
    }

    public List<PrePartidaPac> getPrePartidaPacList() {
        return prePartidaPacList;
    }

    public void setPrePartidaPacList(List<PrePartidaPac> prePartidaPacList) {
        this.prePartidaPacList = prePartidaPacList;
    }

    public List<PrePacPeriodo> getPrePacPeriodoList() {
        return prePacPeriodoList;
    }

    public void setPrePacPeriodoList(List<PrePacPeriodo> prePacPeriodoList) {
        this.prePacPeriodoList = prePacPeriodoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrpac != null ? idePrpac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrePac)) {
            return false;
        }
        PrePac other = (PrePac) object;
        if ((this.idePrpac == null && other.idePrpac != null) || (this.idePrpac != null && !this.idePrpac.equals(other.idePrpac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PrePac[ idePrpac=" + idePrpac + " ]";
    }
    
}
