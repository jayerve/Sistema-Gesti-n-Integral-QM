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
@Table(name = "pre_poa_mes", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PrePoaMes.findAll", query = "SELECT p FROM PrePoaMes p"),
    @NamedQuery(name = "PrePoaMes.findByIdePrpom", query = "SELECT p FROM PrePoaMes p WHERE p.idePrpom = :idePrpom"),
    @NamedQuery(name = "PrePoaMes.findByValorPresupuestoPrpom", query = "SELECT p FROM PrePoaMes p WHERE p.valorPresupuestoPrpom = :valorPresupuestoPrpom"),
    @NamedQuery(name = "PrePoaMes.findByActivoPrpom", query = "SELECT p FROM PrePoaMes p WHERE p.activoPrpom = :activoPrpom"),
    @NamedQuery(name = "PrePoaMes.findByUsuarioIngre", query = "SELECT p FROM PrePoaMes p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PrePoaMes.findByFechaIngre", query = "SELECT p FROM PrePoaMes p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PrePoaMes.findByHoraIngre", query = "SELECT p FROM PrePoaMes p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PrePoaMes.findByUsuarioActua", query = "SELECT p FROM PrePoaMes p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PrePoaMes.findByFechaActua", query = "SELECT p FROM PrePoaMes p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PrePoaMes.findByHoraActua", query = "SELECT p FROM PrePoaMes p WHERE p.horaActua = :horaActua")})
public class PrePoaMes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prpom", nullable = false)
    private Long idePrpom;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_presupuesto_prpom", precision = 10, scale = 2)
    private BigDecimal valorPresupuestoPrpom;
    @Column(name = "activo_prpom")
    private Boolean activoPrpom;
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
    @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes")
    @ManyToOne
    private GenMes ideGemes;

    public PrePoaMes() {
    }

    public PrePoaMes(Long idePrpom) {
        this.idePrpom = idePrpom;
    }

    public Long getIdePrpom() {
        return idePrpom;
    }

    public void setIdePrpom(Long idePrpom) {
        this.idePrpom = idePrpom;
    }

    public BigDecimal getValorPresupuestoPrpom() {
        return valorPresupuestoPrpom;
    }

    public void setValorPresupuestoPrpom(BigDecimal valorPresupuestoPrpom) {
        this.valorPresupuestoPrpom = valorPresupuestoPrpom;
    }

    public Boolean getActivoPrpom() {
        return activoPrpom;
    }

    public void setActivoPrpom(Boolean activoPrpom) {
        this.activoPrpom = activoPrpom;
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

    public GenMes getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(GenMes ideGemes) {
        this.ideGemes = ideGemes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrpom != null ? idePrpom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrePoaMes)) {
            return false;
        }
        PrePoaMes other = (PrePoaMes) object;
        if ((this.idePrpom == null && other.idePrpom != null) || (this.idePrpom != null && !this.idePrpom.equals(other.idePrpom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PrePoaMes[ idePrpom=" + idePrpom + " ]";
    }
    
}
