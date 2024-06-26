/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "pre_poa_financiamiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PrePoaFinanciamiento.findAll", query = "SELECT p FROM PrePoaFinanciamiento p"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByIdePrpof", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.idePrpof = :idePrpof"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByIdePrpoa", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.idePrpoa = :idePrpoa"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByValorFinanciamientoPrpof", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.valorFinanciamientoPrpof = :valorFinanciamientoPrpof"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByActivoPrpof", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.activoPrpof = :activoPrpof"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByUsuarioIngre", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByFechaIngre", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByHoraIngre", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByUsuarioActua", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByFechaActua", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PrePoaFinanciamiento.findByHoraActua", query = "SELECT p FROM PrePoaFinanciamiento p WHERE p.horaActua = :horaActua")})
public class PrePoaFinanciamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prpof", nullable = false)
    private Long idePrpof;
    @Column(name = "ide_prpoa")
    private BigInteger idePrpoa;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_financiamiento_prpof", precision = 10, scale = 2)
    private BigDecimal valorFinanciamientoPrpof;
    @Column(name = "activo_prpof")
    private Boolean activoPrpof;
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
    @JoinColumn(name = "ide_prfuf", referencedColumnName = "ide_prfuf")
    @ManyToOne
    private PreFuenteFinanciamiento idePrfuf;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;

    public PrePoaFinanciamiento() {
    }

    public PrePoaFinanciamiento(Long idePrpof) {
        this.idePrpof = idePrpof;
    }

    public Long getIdePrpof() {
        return idePrpof;
    }

    public void setIdePrpof(Long idePrpof) {
        this.idePrpof = idePrpof;
    }

    public BigInteger getIdePrpoa() {
        return idePrpoa;
    }

    public void setIdePrpoa(BigInteger idePrpoa) {
        this.idePrpoa = idePrpoa;
    }

    public BigDecimal getValorFinanciamientoPrpof() {
        return valorFinanciamientoPrpof;
    }

    public void setValorFinanciamientoPrpof(BigDecimal valorFinanciamientoPrpof) {
        this.valorFinanciamientoPrpof = valorFinanciamientoPrpof;
    }

    public Boolean getActivoPrpof() {
        return activoPrpof;
    }

    public void setActivoPrpof(Boolean activoPrpof) {
        this.activoPrpof = activoPrpof;
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

    public PreFuenteFinanciamiento getIdePrfuf() {
        return idePrfuf;
    }

    public void setIdePrfuf(PreFuenteFinanciamiento idePrfuf) {
        this.idePrfuf = idePrfuf;
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
        hash += (idePrpof != null ? idePrpof.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrePoaFinanciamiento)) {
            return false;
        }
        PrePoaFinanciamiento other = (PrePoaFinanciamiento) object;
        if ((this.idePrpof == null && other.idePrpof != null) || (this.idePrpof != null && !this.idePrpof.equals(other.idePrpof))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PrePoaFinanciamiento[ idePrpof=" + idePrpof + " ]";
    }
    
}
