/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "pre_pac_periodo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PrePacPeriodo.findAll", query = "SELECT p FROM PrePacPeriodo p"),
    @NamedQuery(name = "PrePacPeriodo.findByIdePrpcp", query = "SELECT p FROM PrePacPeriodo p WHERE p.idePrpcp = :idePrpcp"),
    @NamedQuery(name = "PrePacPeriodo.findByActivoPrpcp", query = "SELECT p FROM PrePacPeriodo p WHERE p.activoPrpcp = :activoPrpcp"),
    @NamedQuery(name = "PrePacPeriodo.findByUsuarioIngre", query = "SELECT p FROM PrePacPeriodo p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PrePacPeriodo.findByFechaIngre", query = "SELECT p FROM PrePacPeriodo p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PrePacPeriodo.findByHoraIngre", query = "SELECT p FROM PrePacPeriodo p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PrePacPeriodo.findByUsuarioActua", query = "SELECT p FROM PrePacPeriodo p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PrePacPeriodo.findByFechaActua", query = "SELECT p FROM PrePacPeriodo p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PrePacPeriodo.findByHoraActua", query = "SELECT p FROM PrePacPeriodo p WHERE p.horaActua = :horaActua")})
public class PrePacPeriodo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prpcp", nullable = false)
    private Long idePrpcp;
    @Column(name = "activo_prpcp")
    private Boolean activoPrpcp;
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
    @JoinColumn(name = "ide_copec", referencedColumnName = "ide_copec")
    @ManyToOne
    private ContPeriodoCuatrimestre ideCopec;

    public PrePacPeriodo() {
    }

    public PrePacPeriodo(Long idePrpcp) {
        this.idePrpcp = idePrpcp;
    }

    public Long getIdePrpcp() {
        return idePrpcp;
    }

    public void setIdePrpcp(Long idePrpcp) {
        this.idePrpcp = idePrpcp;
    }

    public Boolean getActivoPrpcp() {
        return activoPrpcp;
    }

    public void setActivoPrpcp(Boolean activoPrpcp) {
        this.activoPrpcp = activoPrpcp;
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

    public ContPeriodoCuatrimestre getIdeCopec() {
        return ideCopec;
    }

    public void setIdeCopec(ContPeriodoCuatrimestre ideCopec) {
        this.ideCopec = ideCopec;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrpcp != null ? idePrpcp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrePacPeriodo)) {
            return false;
        }
        PrePacPeriodo other = (PrePacPeriodo) object;
        if ((this.idePrpcp == null && other.idePrpcp != null) || (this.idePrpcp != null && !this.idePrpcp.equals(other.idePrpcp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PrePacPeriodo[ idePrpcp=" + idePrpcp + " ]";
    }
    
}
