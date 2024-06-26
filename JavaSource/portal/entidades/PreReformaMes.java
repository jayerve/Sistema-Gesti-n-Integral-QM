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
@Table(name = "pre_reforma_mes", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreReformaMes.findAll", query = "SELECT p FROM PreReformaMes p"),
    @NamedQuery(name = "PreReformaMes.findByIdePrrem", query = "SELECT p FROM PreReformaMes p WHERE p.idePrrem = :idePrrem"),
    @NamedQuery(name = "PreReformaMes.findByValReformaHPrrem", query = "SELECT p FROM PreReformaMes p WHERE p.valReformaHPrrem = :valReformaHPrrem"),
    @NamedQuery(name = "PreReformaMes.findByValReformaDPrrem", query = "SELECT p FROM PreReformaMes p WHERE p.valReformaDPrrem = :valReformaDPrrem"),
    @NamedQuery(name = "PreReformaMes.findByFechaReformaPrrem", query = "SELECT p FROM PreReformaMes p WHERE p.fechaReformaPrrem = :fechaReformaPrrem"),
    @NamedQuery(name = "PreReformaMes.findByActivoPrrem", query = "SELECT p FROM PreReformaMes p WHERE p.activoPrrem = :activoPrrem"),
    @NamedQuery(name = "PreReformaMes.findByUsuarioIngre", query = "SELECT p FROM PreReformaMes p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreReformaMes.findByFechaIngre", query = "SELECT p FROM PreReformaMes p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreReformaMes.findByHoraIngre", query = "SELECT p FROM PreReformaMes p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreReformaMes.findByUsuarioActua", query = "SELECT p FROM PreReformaMes p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreReformaMes.findByFechaActua", query = "SELECT p FROM PreReformaMes p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreReformaMes.findByHoraActua", query = "SELECT p FROM PreReformaMes p WHERE p.horaActua = :horaActua")})
public class PreReformaMes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prrem", nullable = false)
    private Long idePrrem;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "val_reforma_h_prrem", precision = 10, scale = 3)
    private BigDecimal valReformaHPrrem;
    @Column(name = "val_reforma_d_prrem", precision = 10, scale = 3)
    private BigDecimal valReformaDPrrem;
    @Column(name = "fecha_reforma_prrem")
    @Temporal(TemporalType.DATE)
    private Date fechaReformaPrrem;
    @Column(name = "activo_prrem")
    private Boolean activoPrrem;
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
    @JoinColumn(name = "ide_pranu", referencedColumnName = "ide_pranu")
    @ManyToOne
    private PreAnual idePranu;
    @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes")
    @ManyToOne
    private GenMes ideGemes;

    public PreReformaMes() {
    }

    public PreReformaMes(Long idePrrem) {
        this.idePrrem = idePrrem;
    }

    public Long getIdePrrem() {
        return idePrrem;
    }

    public void setIdePrrem(Long idePrrem) {
        this.idePrrem = idePrrem;
    }

    public BigDecimal getValReformaHPrrem() {
        return valReformaHPrrem;
    }

    public void setValReformaHPrrem(BigDecimal valReformaHPrrem) {
        this.valReformaHPrrem = valReformaHPrrem;
    }

    public BigDecimal getValReformaDPrrem() {
        return valReformaDPrrem;
    }

    public void setValReformaDPrrem(BigDecimal valReformaDPrrem) {
        this.valReformaDPrrem = valReformaDPrrem;
    }

    public Date getFechaReformaPrrem() {
        return fechaReformaPrrem;
    }

    public void setFechaReformaPrrem(Date fechaReformaPrrem) {
        this.fechaReformaPrrem = fechaReformaPrrem;
    }

    public Boolean getActivoPrrem() {
        return activoPrrem;
    }

    public void setActivoPrrem(Boolean activoPrrem) {
        this.activoPrrem = activoPrrem;
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

    public PreAnual getIdePranu() {
        return idePranu;
    }

    public void setIdePranu(PreAnual idePranu) {
        this.idePranu = idePranu;
    }

    public GenMes getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(GenMes ideGemes) {
        this.ideGemes = ideGemes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrrem != null ? idePrrem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreReformaMes)) {
            return false;
        }
        PreReformaMes other = (PreReformaMes) object;
        if ((this.idePrrem == null && other.idePrrem != null) || (this.idePrrem != null && !this.idePrrem.equals(other.idePrrem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreReformaMes[ idePrrem=" + idePrrem + " ]";
    }
    
}
