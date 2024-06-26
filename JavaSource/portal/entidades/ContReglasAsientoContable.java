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
@Table(name = "cont_reglas_asiento_contable", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContReglasAsientoContable.findAll", query = "SELECT c FROM ContReglasAsientoContable c"),
    @NamedQuery(name = "ContReglasAsientoContable.findByIdeCorac", query = "SELECT c FROM ContReglasAsientoContable c WHERE c.ideCorac = :ideCorac"),
    @NamedQuery(name = "ContReglasAsientoContable.findByActivoCorac", query = "SELECT c FROM ContReglasAsientoContable c WHERE c.activoCorac = :activoCorac"),
    @NamedQuery(name = "ContReglasAsientoContable.findByUsuarioIngre", query = "SELECT c FROM ContReglasAsientoContable c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContReglasAsientoContable.findByFechaIngre", query = "SELECT c FROM ContReglasAsientoContable c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContReglasAsientoContable.findByHoraIngre", query = "SELECT c FROM ContReglasAsientoContable c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContReglasAsientoContable.findByUsuarioActua", query = "SELECT c FROM ContReglasAsientoContable c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContReglasAsientoContable.findByFechaActua", query = "SELECT c FROM ContReglasAsientoContable c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContReglasAsientoContable.findByHoraActua", query = "SELECT c FROM ContReglasAsientoContable c WHERE c.horaActua = :horaActua")})
public class ContReglasAsientoContable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_corac", nullable = false)
    private Long ideCorac;
    @Column(name = "activo_corac")
    private Boolean activoCorac;
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
    @JoinColumn(name = "ide_prmop", referencedColumnName = "ide_prmop")
    @ManyToOne
    private PreMovimientoPresupuestario idePrmop;
    @JoinColumn(name = "ide_conac", referencedColumnName = "ide_conac")
    @ManyToOne
    private ContNombreAsientoContable ideConac;

    public ContReglasAsientoContable() {
    }

    public ContReglasAsientoContable(Long ideCorac) {
        this.ideCorac = ideCorac;
    }

    public Long getIdeCorac() {
        return ideCorac;
    }

    public void setIdeCorac(Long ideCorac) {
        this.ideCorac = ideCorac;
    }

    public Boolean getActivoCorac() {
        return activoCorac;
    }

    public void setActivoCorac(Boolean activoCorac) {
        this.activoCorac = activoCorac;
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

    public PreMovimientoPresupuestario getIdePrmop() {
        return idePrmop;
    }

    public void setIdePrmop(PreMovimientoPresupuestario idePrmop) {
        this.idePrmop = idePrmop;
    }

    public ContNombreAsientoContable getIdeConac() {
        return ideConac;
    }

    public void setIdeConac(ContNombreAsientoContable ideConac) {
        this.ideConac = ideConac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCorac != null ? ideCorac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContReglasAsientoContable)) {
            return false;
        }
        ContReglasAsientoContable other = (ContReglasAsientoContable) object;
        if ((this.ideCorac == null && other.ideCorac != null) || (this.ideCorac != null && !this.ideCorac.equals(other.ideCorac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContReglasAsientoContable[ ideCorac=" + ideCorac + " ]";
    }
    
}
