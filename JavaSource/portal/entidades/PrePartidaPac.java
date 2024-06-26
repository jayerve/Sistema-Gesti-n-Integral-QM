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
@Table(name = "pre_partida_pac", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PrePartidaPac.findAll", query = "SELECT p FROM PrePartidaPac p"),
    @NamedQuery(name = "PrePartidaPac.findByIdePrpap", query = "SELECT p FROM PrePartidaPac p WHERE p.idePrpap = :idePrpap"),
    @NamedQuery(name = "PrePartidaPac.findByValorPrpap", query = "SELECT p FROM PrePartidaPac p WHERE p.valorPrpap = :valorPrpap"),
    @NamedQuery(name = "PrePartidaPac.findByActivoPrpap", query = "SELECT p FROM PrePartidaPac p WHERE p.activoPrpap = :activoPrpap"),
    @NamedQuery(name = "PrePartidaPac.findByUsuarioIngre", query = "SELECT p FROM PrePartidaPac p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PrePartidaPac.findByFechaIngre", query = "SELECT p FROM PrePartidaPac p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PrePartidaPac.findByHoraIngre", query = "SELECT p FROM PrePartidaPac p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PrePartidaPac.findByUsuarioActua", query = "SELECT p FROM PrePartidaPac p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PrePartidaPac.findByFechaActua", query = "SELECT p FROM PrePartidaPac p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PrePartidaPac.findByHoraActua", query = "SELECT p FROM PrePartidaPac p WHERE p.horaActua = :horaActua")})
public class PrePartidaPac implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prpap", nullable = false)
    private Long idePrpap;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_prpap", precision = 10, scale = 2)
    private BigDecimal valorPrpap;
    @Column(name = "activo_prpap")
    private Boolean activoPrpap;
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
    @JoinColumn(name = "ide_prpac", referencedColumnName = "ide_prpac")
    @ManyToOne
    private PrePac idePrpac;
    @JoinColumn(name = "ide_prcla", referencedColumnName = "ide_prcla")
    @ManyToOne
    private PreClasificador idePrcla;

    public PrePartidaPac() {
    }

    public PrePartidaPac(Long idePrpap) {
        this.idePrpap = idePrpap;
    }

    public Long getIdePrpap() {
        return idePrpap;
    }

    public void setIdePrpap(Long idePrpap) {
        this.idePrpap = idePrpap;
    }

    public BigDecimal getValorPrpap() {
        return valorPrpap;
    }

    public void setValorPrpap(BigDecimal valorPrpap) {
        this.valorPrpap = valorPrpap;
    }

    public Boolean getActivoPrpap() {
        return activoPrpap;
    }

    public void setActivoPrpap(Boolean activoPrpap) {
        this.activoPrpap = activoPrpap;
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

    public PrePac getIdePrpac() {
        return idePrpac;
    }

    public void setIdePrpac(PrePac idePrpac) {
        this.idePrpac = idePrpac;
    }

    public PreClasificador getIdePrcla() {
        return idePrcla;
    }

    public void setIdePrcla(PreClasificador idePrcla) {
        this.idePrcla = idePrcla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrpap != null ? idePrpap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrePartidaPac)) {
            return false;
        }
        PrePartidaPac other = (PrePartidaPac) object;
        if ((this.idePrpap == null && other.idePrpap != null) || (this.idePrpap != null && !this.idePrpap.equals(other.idePrpap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PrePartidaPac[ idePrpap=" + idePrpap + " ]";
    }
    
}
