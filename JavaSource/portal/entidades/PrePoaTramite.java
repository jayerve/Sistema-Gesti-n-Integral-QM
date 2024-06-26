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
@Table(name = "pre_poa_tramite", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PrePoaTramite.findAll", query = "SELECT p FROM PrePoaTramite p"),
    @NamedQuery(name = "PrePoaTramite.findByIdePrpot", query = "SELECT p FROM PrePoaTramite p WHERE p.idePrpot = :idePrpot"),
    @NamedQuery(name = "PrePoaTramite.findByIdePrtra", query = "SELECT p FROM PrePoaTramite p WHERE p.idePrtra = :idePrtra"),
    @NamedQuery(name = "PrePoaTramite.findByComprometidoPrpot", query = "SELECT p FROM PrePoaTramite p WHERE p.comprometidoPrpot = :comprometidoPrpot"),
    @NamedQuery(name = "PrePoaTramite.findByObservacionesPrpot", query = "SELECT p FROM PrePoaTramite p WHERE p.observacionesPrpot = :observacionesPrpot"),
    @NamedQuery(name = "PrePoaTramite.findByActivoPrpot", query = "SELECT p FROM PrePoaTramite p WHERE p.activoPrpot = :activoPrpot"),
    @NamedQuery(name = "PrePoaTramite.findByUsuarioIngre", query = "SELECT p FROM PrePoaTramite p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PrePoaTramite.findByFechaIngre", query = "SELECT p FROM PrePoaTramite p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PrePoaTramite.findByHoraIngre", query = "SELECT p FROM PrePoaTramite p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PrePoaTramite.findByUsuarioActua", query = "SELECT p FROM PrePoaTramite p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PrePoaTramite.findByFechaActua", query = "SELECT p FROM PrePoaTramite p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PrePoaTramite.findByHoraActua", query = "SELECT p FROM PrePoaTramite p WHERE p.horaActua = :horaActua")})
public class PrePoaTramite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prpot", nullable = false)
    private Long idePrpot;
    @Column(name = "ide_prtra")
    private BigInteger idePrtra;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "comprometido_prpot", precision = 10, scale = 2)
    private BigDecimal comprometidoPrpot;
    @Size(max = 2147483647)
    @Column(name = "observaciones_prpot", length = 2147483647)
    private String observacionesPrpot;
    @Column(name = "activo_prpot")
    private Boolean activoPrpot;
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

    public PrePoaTramite() {
    }

    public PrePoaTramite(Long idePrpot) {
        this.idePrpot = idePrpot;
    }

    public Long getIdePrpot() {
        return idePrpot;
    }

    public void setIdePrpot(Long idePrpot) {
        this.idePrpot = idePrpot;
    }

    public BigInteger getIdePrtra() {
        return idePrtra;
    }

    public void setIdePrtra(BigInteger idePrtra) {
        this.idePrtra = idePrtra;
    }

    public BigDecimal getComprometidoPrpot() {
        return comprometidoPrpot;
    }

    public void setComprometidoPrpot(BigDecimal comprometidoPrpot) {
        this.comprometidoPrpot = comprometidoPrpot;
    }

    public String getObservacionesPrpot() {
        return observacionesPrpot;
    }

    public void setObservacionesPrpot(String observacionesPrpot) {
        this.observacionesPrpot = observacionesPrpot;
    }

    public Boolean getActivoPrpot() {
        return activoPrpot;
    }

    public void setActivoPrpot(Boolean activoPrpot) {
        this.activoPrpot = activoPrpot;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrpot != null ? idePrpot.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrePoaTramite)) {
            return false;
        }
        PrePoaTramite other = (PrePoaTramite) object;
        if ((this.idePrpot == null && other.idePrpot != null) || (this.idePrpot != null && !this.idePrpot.equals(other.idePrpot))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PrePoaTramite[ idePrpot=" + idePrpot + " ]";
    }
    
}
