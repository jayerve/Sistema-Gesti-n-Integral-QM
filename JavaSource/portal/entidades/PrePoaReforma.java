/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "pre_poa_reforma", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PrePoaReforma.findAll", query = "SELECT p FROM PrePoaReforma p"),
    @NamedQuery(name = "PrePoaReforma.findByIdePrpor", query = "SELECT p FROM PrePoaReforma p WHERE p.idePrpor = :idePrpor"),
    @NamedQuery(name = "PrePoaReforma.findByValorReformadoPrpor", query = "SELECT p FROM PrePoaReforma p WHERE p.valorReformadoPrpor = :valorReformadoPrpor"),
    @NamedQuery(name = "PrePoaReforma.findByResolucionPrpor", query = "SELECT p FROM PrePoaReforma p WHERE p.resolucionPrpor = :resolucionPrpor"),
    @NamedQuery(name = "PrePoaReforma.findByActivoPrpor", query = "SELECT p FROM PrePoaReforma p WHERE p.activoPrpor = :activoPrpor"),
    @NamedQuery(name = "PrePoaReforma.findByUsuarioIngre", query = "SELECT p FROM PrePoaReforma p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PrePoaReforma.findByFechaIngre", query = "SELECT p FROM PrePoaReforma p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PrePoaReforma.findByHoraIngre", query = "SELECT p FROM PrePoaReforma p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PrePoaReforma.findByUsuarioActua", query = "SELECT p FROM PrePoaReforma p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PrePoaReforma.findByFechaActua", query = "SELECT p FROM PrePoaReforma p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PrePoaReforma.findByHoraActua", query = "SELECT p FROM PrePoaReforma p WHERE p.horaActua = :horaActua")})
public class PrePoaReforma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prpor", nullable = false)
    private Long idePrpor;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_reformado_prpor", precision = 10, scale = 2)
    private BigDecimal valorReformadoPrpor;
    @Size(max = 100)
    @Column(name = "resolucion_prpor", length = 100)
    private String resolucionPrpor;
    @Column(name = "activo_prpor")
    private Boolean activoPrpor;
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
    @JoinColumn(name = "ide_prpoa", referencedColumnName = "ide_prpoa")
    @ManyToOne
    private PrePoa idePrpoa;
    @JoinColumn(name = "pre_ide_prpoa", referencedColumnName = "ide_prpoa")
    @ManyToOne
    private PrePoa preIdePrpoa;
    @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes")
    @ManyToOne
    private GenMes ideGemes;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;

    public PrePoaReforma() {
    }

    public PrePoaReforma(Long idePrpor) {
        this.idePrpor = idePrpor;
    }

    public Long getIdePrpor() {
        return idePrpor;
    }

    public void setIdePrpor(Long idePrpor) {
        this.idePrpor = idePrpor;
    }

    public BigDecimal getValorReformadoPrpor() {
        return valorReformadoPrpor;
    }

    public void setValorReformadoPrpor(BigDecimal valorReformadoPrpor) {
        this.valorReformadoPrpor = valorReformadoPrpor;
    }

    public String getResolucionPrpor() {
        return resolucionPrpor;
    }

    public void setResolucionPrpor(String resolucionPrpor) {
        this.resolucionPrpor = resolucionPrpor;
    }

    public Boolean getActivoPrpor() {
        return activoPrpor;
    }

    public void setActivoPrpor(Boolean activoPrpor) {
        this.activoPrpor = activoPrpor;
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

    public PrePoa getIdePrpoa() {
        return idePrpoa;
    }

    public void setIdePrpoa(PrePoa idePrpoa) {
        this.idePrpoa = idePrpoa;
    }

    public PrePoa getPreIdePrpoa() {
        return preIdePrpoa;
    }

    public void setPreIdePrpoa(PrePoa preIdePrpoa) {
        this.preIdePrpoa = preIdePrpoa;
    }

    public GenMes getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(GenMes ideGemes) {
        this.ideGemes = ideGemes;
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
        hash += (idePrpor != null ? idePrpor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrePoaReforma)) {
            return false;
        }
        PrePoaReforma other = (PrePoaReforma) object;
        if ((this.idePrpor == null && other.idePrpor != null) || (this.idePrpor != null && !this.idePrpor.equals(other.idePrpor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PrePoaReforma[ idePrpor=" + idePrpor + " ]";
    }
    
}
