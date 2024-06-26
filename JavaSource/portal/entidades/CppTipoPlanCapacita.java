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
@Table(name = "cpp_tipo_plan_capacita", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppTipoPlanCapacita.findAll", query = "SELECT c FROM CppTipoPlanCapacita c"),
    @NamedQuery(name = "CppTipoPlanCapacita.findByIdeCptpc", query = "SELECT c FROM CppTipoPlanCapacita c WHERE c.ideCptpc = :ideCptpc"),
    @NamedQuery(name = "CppTipoPlanCapacita.findByDetalleCptpc", query = "SELECT c FROM CppTipoPlanCapacita c WHERE c.detalleCptpc = :detalleCptpc"),
    @NamedQuery(name = "CppTipoPlanCapacita.findByActivoCptpc", query = "SELECT c FROM CppTipoPlanCapacita c WHERE c.activoCptpc = :activoCptpc"),
    @NamedQuery(name = "CppTipoPlanCapacita.findByUsuarioIngre", query = "SELECT c FROM CppTipoPlanCapacita c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppTipoPlanCapacita.findByFechaIngre", query = "SELECT c FROM CppTipoPlanCapacita c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppTipoPlanCapacita.findByHoraIngre", query = "SELECT c FROM CppTipoPlanCapacita c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppTipoPlanCapacita.findByUsuarioActua", query = "SELECT c FROM CppTipoPlanCapacita c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppTipoPlanCapacita.findByFechaActua", query = "SELECT c FROM CppTipoPlanCapacita c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppTipoPlanCapacita.findByHoraActua", query = "SELECT c FROM CppTipoPlanCapacita c WHERE c.horaActua = :horaActua")})
public class CppTipoPlanCapacita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cptpc", nullable = false)
    private Integer ideCptpc;
    @Size(max = 50)
    @Column(name = "detalle_cptpc", length = 50)
    private String detalleCptpc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cptpc", nullable = false)
    private boolean activoCptpc;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @OneToMany(mappedBy = "ideCptpc")
    private List<CppPlanCapacitacion> cppPlanCapacitacionList;

    public CppTipoPlanCapacita() {
    }

    public CppTipoPlanCapacita(Integer ideCptpc) {
        this.ideCptpc = ideCptpc;
    }

    public CppTipoPlanCapacita(Integer ideCptpc, boolean activoCptpc) {
        this.ideCptpc = ideCptpc;
        this.activoCptpc = activoCptpc;
    }

    public Integer getIdeCptpc() {
        return ideCptpc;
    }

    public void setIdeCptpc(Integer ideCptpc) {
        this.ideCptpc = ideCptpc;
    }

    public String getDetalleCptpc() {
        return detalleCptpc;
    }

    public void setDetalleCptpc(String detalleCptpc) {
        this.detalleCptpc = detalleCptpc;
    }

    public boolean getActivoCptpc() {
        return activoCptpc;
    }

    public void setActivoCptpc(boolean activoCptpc) {
        this.activoCptpc = activoCptpc;
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

    public List<CppPlanCapacitacion> getCppPlanCapacitacionList() {
        return cppPlanCapacitacionList;
    }

    public void setCppPlanCapacitacionList(List<CppPlanCapacitacion> cppPlanCapacitacionList) {
        this.cppPlanCapacitacionList = cppPlanCapacitacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCptpc != null ? ideCptpc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppTipoPlanCapacita)) {
            return false;
        }
        CppTipoPlanCapacita other = (CppTipoPlanCapacita) object;
        if ((this.ideCptpc == null && other.ideCptpc != null) || (this.ideCptpc != null && !this.ideCptpc.equals(other.ideCptpc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppTipoPlanCapacita[ ideCptpc=" + ideCptpc + " ]";
    }
    
}
