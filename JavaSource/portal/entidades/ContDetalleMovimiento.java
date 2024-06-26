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
@Table(name = "cont_detalle_movimiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContDetalleMovimiento.findAll", query = "SELECT c FROM ContDetalleMovimiento c"),
    @NamedQuery(name = "ContDetalleMovimiento.findByIdeCodem", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.ideCodem = :ideCodem"),
    @NamedQuery(name = "ContDetalleMovimiento.findByDebeCodem", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.debeCodem = :debeCodem"),
    @NamedQuery(name = "ContDetalleMovimiento.findByDetalleCodem", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.detalleCodem = :detalleCodem"),
    @NamedQuery(name = "ContDetalleMovimiento.findByHaberCodem", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.haberCodem = :haberCodem"),
    @NamedQuery(name = "ContDetalleMovimiento.findByActivoCodem", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.activoCodem = :activoCodem"),
    @NamedQuery(name = "ContDetalleMovimiento.findByUsuarioIngre", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContDetalleMovimiento.findByFechaIngre", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContDetalleMovimiento.findByHoraIngre", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContDetalleMovimiento.findByUsuarioActua", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContDetalleMovimiento.findByFechaActua", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContDetalleMovimiento.findByHoraActua", query = "SELECT c FROM ContDetalleMovimiento c WHERE c.horaActua = :horaActua")})
public class ContDetalleMovimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_codem", nullable = false)
    private Long ideCodem;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debe_codem", precision = 10, scale = 2)
    private BigDecimal debeCodem;
    @Size(max = 2147483647)
    @Column(name = "detalle_codem", length = 2147483647)
    private String detalleCodem;
    @Column(name = "haber_codem", precision = 10, scale = 2)
    private BigDecimal haberCodem;
    @Column(name = "activo_codem")
    private Boolean activoCodem;
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
    @JoinColumn(name = "ide_covig", referencedColumnName = "ide_covig")
    @ManyToOne
    private ContVigente ideCovig;
    @JoinColumn(name = "con_ide_covig", referencedColumnName = "ide_covig")
    @ManyToOne
    private ContVigente conIdeCovig;
    @JoinColumn(name = "con_ide_covig2", referencedColumnName = "ide_covig")
    @ManyToOne
    private ContVigente conIdeCovig2;
    @JoinColumn(name = "ide_comov", referencedColumnName = "ide_comov")
    @ManyToOne
    private ContMovimiento ideComov;

    public ContDetalleMovimiento() {
    }

    public ContDetalleMovimiento(Long ideCodem) {
        this.ideCodem = ideCodem;
    }

    public Long getIdeCodem() {
        return ideCodem;
    }

    public void setIdeCodem(Long ideCodem) {
        this.ideCodem = ideCodem;
    }

    public BigDecimal getDebeCodem() {
        return debeCodem;
    }

    public void setDebeCodem(BigDecimal debeCodem) {
        this.debeCodem = debeCodem;
    }

    public String getDetalleCodem() {
        return detalleCodem;
    }

    public void setDetalleCodem(String detalleCodem) {
        this.detalleCodem = detalleCodem;
    }

    public BigDecimal getHaberCodem() {
        return haberCodem;
    }

    public void setHaberCodem(BigDecimal haberCodem) {
        this.haberCodem = haberCodem;
    }

    public Boolean getActivoCodem() {
        return activoCodem;
    }

    public void setActivoCodem(Boolean activoCodem) {
        this.activoCodem = activoCodem;
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

    public ContVigente getIdeCovig() {
        return ideCovig;
    }

    public void setIdeCovig(ContVigente ideCovig) {
        this.ideCovig = ideCovig;
    }

    public ContVigente getConIdeCovig() {
        return conIdeCovig;
    }

    public void setConIdeCovig(ContVigente conIdeCovig) {
        this.conIdeCovig = conIdeCovig;
    }

    public ContVigente getConIdeCovig2() {
        return conIdeCovig2;
    }

    public void setConIdeCovig2(ContVigente conIdeCovig2) {
        this.conIdeCovig2 = conIdeCovig2;
    }

    public ContMovimiento getIdeComov() {
        return ideComov;
    }

    public void setIdeComov(ContMovimiento ideComov) {
        this.ideComov = ideComov;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCodem != null ? ideCodem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContDetalleMovimiento)) {
            return false;
        }
        ContDetalleMovimiento other = (ContDetalleMovimiento) object;
        if ((this.ideCodem == null && other.ideCodem != null) || (this.ideCodem != null && !this.ideCodem.equals(other.ideCodem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContDetalleMovimiento[ ideCodem=" + ideCodem + " ]";
    }
    
}
