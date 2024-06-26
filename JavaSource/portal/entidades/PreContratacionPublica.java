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
@Table(name = "pre_contratacion_publica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreContratacionPublica.findAll", query = "SELECT p FROM PreContratacionPublica p"),
    @NamedQuery(name = "PreContratacionPublica.findByIdePrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.idePrcop = :idePrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByCodigoProcesoPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.codigoProcesoPrcop = :codigoProcesoPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByObjetoContratacionPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.objetoContratacionPrcop = :objetoContratacionPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByPresupuestoReferenciaPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.presupuestoReferenciaPrcop = :presupuestoReferenciaPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByMontoAdjudicadoPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.montoAdjudicadoPrcop = :montoAdjudicadoPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByAclaracionPagoPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.aclaracionPagoPrcop = :aclaracionPagoPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByTipoAdjudicacionPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.tipoAdjudicacionPrcop = :tipoAdjudicacionPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByFechaInicioProcesoPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.fechaInicioProcesoPrcop = :fechaInicioProcesoPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByFechaAdjudicacionPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.fechaAdjudicacionPrcop = :fechaAdjudicacionPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByObservacionPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.observacionPrcop = :observacionPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByActivoPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.activoPrcop = :activoPrcop"),
    @NamedQuery(name = "PreContratacionPublica.findByUsuarioIngre", query = "SELECT p FROM PreContratacionPublica p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreContratacionPublica.findByFechaIngre", query = "SELECT p FROM PreContratacionPublica p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreContratacionPublica.findByHoraIngre", query = "SELECT p FROM PreContratacionPublica p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreContratacionPublica.findByUsuarioActua", query = "SELECT p FROM PreContratacionPublica p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreContratacionPublica.findByFechaActua", query = "SELECT p FROM PreContratacionPublica p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreContratacionPublica.findByHoraActua", query = "SELECT p FROM PreContratacionPublica p WHERE p.horaActua = :horaActua"),
    @NamedQuery(name = "PreContratacionPublica.findByIdeCotio", query = "SELECT p FROM PreContratacionPublica p WHERE p.ideCotio = :ideCotio"),
    @NamedQuery(name = "PreContratacionPublica.findBySecuencialPrcop", query = "SELECT p FROM PreContratacionPublica p WHERE p.secuencialPrcop = :secuencialPrcop")})
public class PreContratacionPublica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prcop", nullable = false)
    private Long idePrcop;
    @Size(max = 50)
    @Column(name = "codigo_proceso_prcop", length = 50)
    private String codigoProcesoPrcop;
    @Size(max = 2147483647)
    @Column(name = "objeto_contratacion_prcop", length = 2147483647)
    private String objetoContratacionPrcop;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "presupuesto_referencia_prcop", precision = 10, scale = 2)
    private BigDecimal presupuestoReferenciaPrcop;
    @Column(name = "monto_adjudicado_prcop", precision = 10, scale = 2)
    private BigDecimal montoAdjudicadoPrcop;
    @Size(max = 2147483647)
    @Column(name = "aclaracion_pago_prcop", length = 2147483647)
    private String aclaracionPagoPrcop;
    @Column(name = "tipo_adjudicacion_prcop")
    private BigInteger tipoAdjudicacionPrcop;
    @Column(name = "fecha_inicio_proceso_prcop")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioProcesoPrcop;
    @Column(name = "fecha_adjudicacion_prcop")
    @Temporal(TemporalType.DATE)
    private Date fechaAdjudicacionPrcop;
    @Size(max = 2147483647)
    @Column(name = "observacion_prcop", length = 2147483647)
    private String observacionPrcop;
    @Column(name = "activo_prcop")
    private Boolean activoPrcop;
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
    @Column(name = "ide_cotio")
    private BigInteger ideCotio;
    @Column(name = "secuencial_prcop")
    private Integer secuencialPrcop;
    @OneToMany(mappedBy = "idePrcop")
    private List<PreContrato> preContratoList;
    @OneToMany(mappedBy = "idePrcop")
    private List<PreContratacionPartida> preContratacionPartidaList;
    @JoinColumn(name = "ide_prtpc", referencedColumnName = "ide_prtpc")
    @ManyToOne
    private PreTipoContratacion idePrtpc;
    @JoinColumn(name = "ide_prpac", referencedColumnName = "ide_prpac")
    @ManyToOne
    private PrePac idePrpac;
    @JoinColumn(name = "ide_prfop", referencedColumnName = "ide_prfop")
    @ManyToOne
    private PreFormaPago idePrfop;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @OneToMany(mappedBy = "idePrcop")
    private List<PreArchivo> preArchivoList;
    @OneToMany(mappedBy = "idePrcop")
    private List<PreResponsableContratacion> preResponsableContratacionList;

    public PreContratacionPublica() {
    }

    public PreContratacionPublica(Long idePrcop) {
        this.idePrcop = idePrcop;
    }

    public Long getIdePrcop() {
        return idePrcop;
    }

    public void setIdePrcop(Long idePrcop) {
        this.idePrcop = idePrcop;
    }

    public String getCodigoProcesoPrcop() {
        return codigoProcesoPrcop;
    }

    public void setCodigoProcesoPrcop(String codigoProcesoPrcop) {
        this.codigoProcesoPrcop = codigoProcesoPrcop;
    }

    public String getObjetoContratacionPrcop() {
        return objetoContratacionPrcop;
    }

    public void setObjetoContratacionPrcop(String objetoContratacionPrcop) {
        this.objetoContratacionPrcop = objetoContratacionPrcop;
    }

    public BigDecimal getPresupuestoReferenciaPrcop() {
        return presupuestoReferenciaPrcop;
    }

    public void setPresupuestoReferenciaPrcop(BigDecimal presupuestoReferenciaPrcop) {
        this.presupuestoReferenciaPrcop = presupuestoReferenciaPrcop;
    }

    public BigDecimal getMontoAdjudicadoPrcop() {
        return montoAdjudicadoPrcop;
    }

    public void setMontoAdjudicadoPrcop(BigDecimal montoAdjudicadoPrcop) {
        this.montoAdjudicadoPrcop = montoAdjudicadoPrcop;
    }

    public String getAclaracionPagoPrcop() {
        return aclaracionPagoPrcop;
    }

    public void setAclaracionPagoPrcop(String aclaracionPagoPrcop) {
        this.aclaracionPagoPrcop = aclaracionPagoPrcop;
    }

    public BigInteger getTipoAdjudicacionPrcop() {
        return tipoAdjudicacionPrcop;
    }

    public void setTipoAdjudicacionPrcop(BigInteger tipoAdjudicacionPrcop) {
        this.tipoAdjudicacionPrcop = tipoAdjudicacionPrcop;
    }

    public Date getFechaInicioProcesoPrcop() {
        return fechaInicioProcesoPrcop;
    }

    public void setFechaInicioProcesoPrcop(Date fechaInicioProcesoPrcop) {
        this.fechaInicioProcesoPrcop = fechaInicioProcesoPrcop;
    }

    public Date getFechaAdjudicacionPrcop() {
        return fechaAdjudicacionPrcop;
    }

    public void setFechaAdjudicacionPrcop(Date fechaAdjudicacionPrcop) {
        this.fechaAdjudicacionPrcop = fechaAdjudicacionPrcop;
    }

    public String getObservacionPrcop() {
        return observacionPrcop;
    }

    public void setObservacionPrcop(String observacionPrcop) {
        this.observacionPrcop = observacionPrcop;
    }

    public Boolean getActivoPrcop() {
        return activoPrcop;
    }

    public void setActivoPrcop(Boolean activoPrcop) {
        this.activoPrcop = activoPrcop;
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

    public BigInteger getIdeCotio() {
        return ideCotio;
    }

    public void setIdeCotio(BigInteger ideCotio) {
        this.ideCotio = ideCotio;
    }

    public Integer getSecuencialPrcop() {
        return secuencialPrcop;
    }

    public void setSecuencialPrcop(Integer secuencialPrcop) {
        this.secuencialPrcop = secuencialPrcop;
    }

    public List<PreContrato> getPreContratoList() {
        return preContratoList;
    }

    public void setPreContratoList(List<PreContrato> preContratoList) {
        this.preContratoList = preContratoList;
    }

    public List<PreContratacionPartida> getPreContratacionPartidaList() {
        return preContratacionPartidaList;
    }

    public void setPreContratacionPartidaList(List<PreContratacionPartida> preContratacionPartidaList) {
        this.preContratacionPartidaList = preContratacionPartidaList;
    }

    public PreTipoContratacion getIdePrtpc() {
        return idePrtpc;
    }

    public void setIdePrtpc(PreTipoContratacion idePrtpc) {
        this.idePrtpc = idePrtpc;
    }

    public PrePac getIdePrpac() {
        return idePrpac;
    }

    public void setIdePrpac(PrePac idePrpac) {
        this.idePrpac = idePrpac;
    }

    public PreFormaPago getIdePrfop() {
        return idePrfop;
    }

    public void setIdePrfop(PreFormaPago idePrfop) {
        this.idePrfop = idePrfop;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public List<PreArchivo> getPreArchivoList() {
        return preArchivoList;
    }

    public void setPreArchivoList(List<PreArchivo> preArchivoList) {
        this.preArchivoList = preArchivoList;
    }

    public List<PreResponsableContratacion> getPreResponsableContratacionList() {
        return preResponsableContratacionList;
    }

    public void setPreResponsableContratacionList(List<PreResponsableContratacion> preResponsableContratacionList) {
        this.preResponsableContratacionList = preResponsableContratacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrcop != null ? idePrcop.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreContratacionPublica)) {
            return false;
        }
        PreContratacionPublica other = (PreContratacionPublica) object;
        if ((this.idePrcop == null && other.idePrcop != null) || (this.idePrcop != null && !this.idePrcop.equals(other.idePrcop))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreContratacionPublica[ idePrcop=" + idePrcop + " ]";
    }
    
}
