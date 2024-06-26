/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "pre_movimiento_presupuestario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreMovimientoPresupuestario.findAll", query = "SELECT p FROM PreMovimientoPresupuestario p"),
    @NamedQuery(name = "PreMovimientoPresupuestario.findByIdePrmop", query = "SELECT p FROM PreMovimientoPresupuestario p WHERE p.idePrmop = :idePrmop"),
    @NamedQuery(name = "PreMovimientoPresupuestario.findByDetallePrmop", query = "SELECT p FROM PreMovimientoPresupuestario p WHERE p.detallePrmop = :detallePrmop"),
    @NamedQuery(name = "PreMovimientoPresupuestario.findByActivoPrmop", query = "SELECT p FROM PreMovimientoPresupuestario p WHERE p.activoPrmop = :activoPrmop"),
    @NamedQuery(name = "PreMovimientoPresupuestario.findByUsuarioIngre", query = "SELECT p FROM PreMovimientoPresupuestario p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreMovimientoPresupuestario.findByFechaIngre", query = "SELECT p FROM PreMovimientoPresupuestario p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreMovimientoPresupuestario.findByHoraIngre", query = "SELECT p FROM PreMovimientoPresupuestario p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreMovimientoPresupuestario.findByUsuarioActua", query = "SELECT p FROM PreMovimientoPresupuestario p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreMovimientoPresupuestario.findByFechaActua", query = "SELECT p FROM PreMovimientoPresupuestario p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreMovimientoPresupuestario.findByHoraActua", query = "SELECT p FROM PreMovimientoPresupuestario p WHERE p.horaActua = :horaActua")})
public class PreMovimientoPresupuestario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prmop", nullable = false)
    private Long idePrmop;
    @Size(max = 50)
    @Column(name = "detalle_prmop", length = 50)
    private String detallePrmop;
    @Column(name = "activo_prmop")
    private Boolean activoPrmop;
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
    @OneToMany(mappedBy = "idePrmop")
    private List<ContReglasAsientoContable> contReglasAsientoContableList;
    @OneToMany(mappedBy = "idePrmop")
    private List<PreAsociacionPresupuestaria> preAsociacionPresupuestariaList;

    public PreMovimientoPresupuestario() {
    }

    public PreMovimientoPresupuestario(Long idePrmop) {
        this.idePrmop = idePrmop;
    }

    public Long getIdePrmop() {
        return idePrmop;
    }

    public void setIdePrmop(Long idePrmop) {
        this.idePrmop = idePrmop;
    }

    public String getDetallePrmop() {
        return detallePrmop;
    }

    public void setDetallePrmop(String detallePrmop) {
        this.detallePrmop = detallePrmop;
    }

    public Boolean getActivoPrmop() {
        return activoPrmop;
    }

    public void setActivoPrmop(Boolean activoPrmop) {
        this.activoPrmop = activoPrmop;
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

    public List<ContReglasAsientoContable> getContReglasAsientoContableList() {
        return contReglasAsientoContableList;
    }

    public void setContReglasAsientoContableList(List<ContReglasAsientoContable> contReglasAsientoContableList) {
        this.contReglasAsientoContableList = contReglasAsientoContableList;
    }

    public List<PreAsociacionPresupuestaria> getPreAsociacionPresupuestariaList() {
        return preAsociacionPresupuestariaList;
    }

    public void setPreAsociacionPresupuestariaList(List<PreAsociacionPresupuestaria> preAsociacionPresupuestariaList) {
        this.preAsociacionPresupuestariaList = preAsociacionPresupuestariaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrmop != null ? idePrmop.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreMovimientoPresupuestario)) {
            return false;
        }
        PreMovimientoPresupuestario other = (PreMovimientoPresupuestario) object;
        if ((this.idePrmop == null && other.idePrmop != null) || (this.idePrmop != null && !this.idePrmop.equals(other.idePrmop))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreMovimientoPresupuestario[ idePrmop=" + idePrmop + " ]";
    }
    
}
