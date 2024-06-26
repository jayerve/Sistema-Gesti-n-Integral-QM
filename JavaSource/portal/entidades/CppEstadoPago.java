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
@Table(name = "cpp_estado_pago", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppEstadoPago.findAll", query = "SELECT c FROM CppEstadoPago c"),
    @NamedQuery(name = "CppEstadoPago.findByIdeCpesp", query = "SELECT c FROM CppEstadoPago c WHERE c.ideCpesp = :ideCpesp"),
    @NamedQuery(name = "CppEstadoPago.findByDetalleCpesp", query = "SELECT c FROM CppEstadoPago c WHERE c.detalleCpesp = :detalleCpesp"),
    @NamedQuery(name = "CppEstadoPago.findByActivoCpesp", query = "SELECT c FROM CppEstadoPago c WHERE c.activoCpesp = :activoCpesp"),
    @NamedQuery(name = "CppEstadoPago.findByUsuarioIngre", query = "SELECT c FROM CppEstadoPago c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppEstadoPago.findByFechaIngre", query = "SELECT c FROM CppEstadoPago c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppEstadoPago.findByHoraIngre", query = "SELECT c FROM CppEstadoPago c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppEstadoPago.findByUsuarioActua", query = "SELECT c FROM CppEstadoPago c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppEstadoPago.findByFechaActua", query = "SELECT c FROM CppEstadoPago c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppEstadoPago.findByHoraActua", query = "SELECT c FROM CppEstadoPago c WHERE c.horaActua = :horaActua")})
public class CppEstadoPago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpesp", nullable = false)
    private Integer ideCpesp;
    @Size(max = 50)
    @Column(name = "detalle_cpesp", length = 50)
    private String detalleCpesp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpesp", nullable = false)
    private boolean activoCpesp;
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
    @OneToMany(mappedBy = "ideCpesp")
    private List<CppPagoCapacitacion> cppPagoCapacitacionList;

    public CppEstadoPago() {
    }

    public CppEstadoPago(Integer ideCpesp) {
        this.ideCpesp = ideCpesp;
    }

    public CppEstadoPago(Integer ideCpesp, boolean activoCpesp) {
        this.ideCpesp = ideCpesp;
        this.activoCpesp = activoCpesp;
    }

    public Integer getIdeCpesp() {
        return ideCpesp;
    }

    public void setIdeCpesp(Integer ideCpesp) {
        this.ideCpesp = ideCpesp;
    }

    public String getDetalleCpesp() {
        return detalleCpesp;
    }

    public void setDetalleCpesp(String detalleCpesp) {
        this.detalleCpesp = detalleCpesp;
    }

    public boolean getActivoCpesp() {
        return activoCpesp;
    }

    public void setActivoCpesp(boolean activoCpesp) {
        this.activoCpesp = activoCpesp;
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

    public List<CppPagoCapacitacion> getCppPagoCapacitacionList() {
        return cppPagoCapacitacionList;
    }

    public void setCppPagoCapacitacionList(List<CppPagoCapacitacion> cppPagoCapacitacionList) {
        this.cppPagoCapacitacionList = cppPagoCapacitacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpesp != null ? ideCpesp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppEstadoPago)) {
            return false;
        }
        CppEstadoPago other = (CppEstadoPago) object;
        if ((this.ideCpesp == null && other.ideCpesp != null) || (this.ideCpesp != null && !this.ideCpesp.equals(other.ideCpesp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppEstadoPago[ ideCpesp=" + ideCpesp + " ]";
    }
    
}
