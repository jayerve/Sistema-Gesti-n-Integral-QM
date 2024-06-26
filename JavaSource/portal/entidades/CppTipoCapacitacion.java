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
@Table(name = "cpp_tipo_capacitacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppTipoCapacitacion.findAll", query = "SELECT c FROM CppTipoCapacitacion c"),
    @NamedQuery(name = "CppTipoCapacitacion.findByIdeCptic", query = "SELECT c FROM CppTipoCapacitacion c WHERE c.ideCptic = :ideCptic"),
    @NamedQuery(name = "CppTipoCapacitacion.findByDetalleCpptic", query = "SELECT c FROM CppTipoCapacitacion c WHERE c.detalleCpptic = :detalleCpptic"),
    @NamedQuery(name = "CppTipoCapacitacion.findByActivoCpptic", query = "SELECT c FROM CppTipoCapacitacion c WHERE c.activoCpptic = :activoCpptic"),
    @NamedQuery(name = "CppTipoCapacitacion.findByUsuarioIngre", query = "SELECT c FROM CppTipoCapacitacion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppTipoCapacitacion.findByFechaIngre", query = "SELECT c FROM CppTipoCapacitacion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppTipoCapacitacion.findByHoraIngre", query = "SELECT c FROM CppTipoCapacitacion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppTipoCapacitacion.findByUsuarioActua", query = "SELECT c FROM CppTipoCapacitacion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppTipoCapacitacion.findByFechaActua", query = "SELECT c FROM CppTipoCapacitacion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppTipoCapacitacion.findByHoraActua", query = "SELECT c FROM CppTipoCapacitacion c WHERE c.horaActua = :horaActua")})
public class CppTipoCapacitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cptic", nullable = false)
    private Integer ideCptic;
    @Size(max = 50)
    @Column(name = "detalle_cpptic", length = 50)
    private String detalleCpptic;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpptic", nullable = false)
    private boolean activoCpptic;
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
    @OneToMany(mappedBy = "ideCptic")
    private List<CppPlanCapacitacion> cppPlanCapacitacionList;

    public CppTipoCapacitacion() {
    }

    public CppTipoCapacitacion(Integer ideCptic) {
        this.ideCptic = ideCptic;
    }

    public CppTipoCapacitacion(Integer ideCptic, boolean activoCpptic) {
        this.ideCptic = ideCptic;
        this.activoCpptic = activoCpptic;
    }

    public Integer getIdeCptic() {
        return ideCptic;
    }

    public void setIdeCptic(Integer ideCptic) {
        this.ideCptic = ideCptic;
    }

    public String getDetalleCpptic() {
        return detalleCpptic;
    }

    public void setDetalleCpptic(String detalleCpptic) {
        this.detalleCpptic = detalleCpptic;
    }

    public boolean getActivoCpptic() {
        return activoCpptic;
    }

    public void setActivoCpptic(boolean activoCpptic) {
        this.activoCpptic = activoCpptic;
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
        hash += (ideCptic != null ? ideCptic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppTipoCapacitacion)) {
            return false;
        }
        CppTipoCapacitacion other = (CppTipoCapacitacion) object;
        if ((this.ideCptic == null && other.ideCptic != null) || (this.ideCptic != null && !this.ideCptic.equals(other.ideCptic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppTipoCapacitacion[ ideCptic=" + ideCptic + " ]";
    }
    
}
