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
@Table(name = "cpp_estado_capacitacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppEstadoCapacitacion.findAll", query = "SELECT c FROM CppEstadoCapacitacion c"),
    @NamedQuery(name = "CppEstadoCapacitacion.findByIdeCpesc", query = "SELECT c FROM CppEstadoCapacitacion c WHERE c.ideCpesc = :ideCpesc"),
    @NamedQuery(name = "CppEstadoCapacitacion.findByDetalleCpesc", query = "SELECT c FROM CppEstadoCapacitacion c WHERE c.detalleCpesc = :detalleCpesc"),
    @NamedQuery(name = "CppEstadoCapacitacion.findByActivoCpesc", query = "SELECT c FROM CppEstadoCapacitacion c WHERE c.activoCpesc = :activoCpesc"),
    @NamedQuery(name = "CppEstadoCapacitacion.findByUsuarioIngre", query = "SELECT c FROM CppEstadoCapacitacion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppEstadoCapacitacion.findByFechaIngre", query = "SELECT c FROM CppEstadoCapacitacion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppEstadoCapacitacion.findByHoraIngre", query = "SELECT c FROM CppEstadoCapacitacion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppEstadoCapacitacion.findByUsuarioActua", query = "SELECT c FROM CppEstadoCapacitacion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppEstadoCapacitacion.findByFechaActua", query = "SELECT c FROM CppEstadoCapacitacion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppEstadoCapacitacion.findByHoraActua", query = "SELECT c FROM CppEstadoCapacitacion c WHERE c.horaActua = :horaActua")})
public class CppEstadoCapacitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpesc", nullable = false)
    private Integer ideCpesc;
    @Size(max = 50)
    @Column(name = "detalle_cpesc", length = 50)
    private String detalleCpesc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpesc", nullable = false)
    private boolean activoCpesc;
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
    @OneToMany(mappedBy = "ideCpesc")
    private List<CppCapacitacion> cppCapacitacionList;

    public CppEstadoCapacitacion() {
    }

    public CppEstadoCapacitacion(Integer ideCpesc) {
        this.ideCpesc = ideCpesc;
    }

    public CppEstadoCapacitacion(Integer ideCpesc, boolean activoCpesc) {
        this.ideCpesc = ideCpesc;
        this.activoCpesc = activoCpesc;
    }

    public Integer getIdeCpesc() {
        return ideCpesc;
    }

    public void setIdeCpesc(Integer ideCpesc) {
        this.ideCpesc = ideCpesc;
    }

    public String getDetalleCpesc() {
        return detalleCpesc;
    }

    public void setDetalleCpesc(String detalleCpesc) {
        this.detalleCpesc = detalleCpesc;
    }

    public boolean getActivoCpesc() {
        return activoCpesc;
    }

    public void setActivoCpesc(boolean activoCpesc) {
        this.activoCpesc = activoCpesc;
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

    public List<CppCapacitacion> getCppCapacitacionList() {
        return cppCapacitacionList;
    }

    public void setCppCapacitacionList(List<CppCapacitacion> cppCapacitacionList) {
        this.cppCapacitacionList = cppCapacitacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpesc != null ? ideCpesc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppEstadoCapacitacion)) {
            return false;
        }
        CppEstadoCapacitacion other = (CppEstadoCapacitacion) object;
        if ((this.ideCpesc == null && other.ideCpesc != null) || (this.ideCpesc != null && !this.ideCpesc.equals(other.ideCpesc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppEstadoCapacitacion[ ideCpesc=" + ideCpesc + " ]";
    }
    
}
