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
@Table(name = "cpp_modalidad_capacita", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppModalidadCapacita.findAll", query = "SELECT c FROM CppModalidadCapacita c"),
    @NamedQuery(name = "CppModalidadCapacita.findByIdeCpmoc", query = "SELECT c FROM CppModalidadCapacita c WHERE c.ideCpmoc = :ideCpmoc"),
    @NamedQuery(name = "CppModalidadCapacita.findByDetalleCpmoc", query = "SELECT c FROM CppModalidadCapacita c WHERE c.detalleCpmoc = :detalleCpmoc"),
    @NamedQuery(name = "CppModalidadCapacita.findByActivoCpmoc", query = "SELECT c FROM CppModalidadCapacita c WHERE c.activoCpmoc = :activoCpmoc"),
    @NamedQuery(name = "CppModalidadCapacita.findByUsuarioIngre", query = "SELECT c FROM CppModalidadCapacita c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppModalidadCapacita.findByFechaIngre", query = "SELECT c FROM CppModalidadCapacita c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppModalidadCapacita.findByHoraIngre", query = "SELECT c FROM CppModalidadCapacita c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppModalidadCapacita.findByUsuarioActua", query = "SELECT c FROM CppModalidadCapacita c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppModalidadCapacita.findByFechaActua", query = "SELECT c FROM CppModalidadCapacita c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppModalidadCapacita.findByHoraActua", query = "SELECT c FROM CppModalidadCapacita c WHERE c.horaActua = :horaActua")})
public class CppModalidadCapacita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpmoc", nullable = false)
    private Integer ideCpmoc;
    @Size(max = 50)
    @Column(name = "detalle_cpmoc", length = 50)
    private String detalleCpmoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpmoc", nullable = false)
    private boolean activoCpmoc;
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
    @OneToMany(mappedBy = "ideCpmoc")
    private List<CppCapacitacion> cppCapacitacionList;

    public CppModalidadCapacita() {
    }

    public CppModalidadCapacita(Integer ideCpmoc) {
        this.ideCpmoc = ideCpmoc;
    }

    public CppModalidadCapacita(Integer ideCpmoc, boolean activoCpmoc) {
        this.ideCpmoc = ideCpmoc;
        this.activoCpmoc = activoCpmoc;
    }

    public Integer getIdeCpmoc() {
        return ideCpmoc;
    }

    public void setIdeCpmoc(Integer ideCpmoc) {
        this.ideCpmoc = ideCpmoc;
    }

    public String getDetalleCpmoc() {
        return detalleCpmoc;
    }

    public void setDetalleCpmoc(String detalleCpmoc) {
        this.detalleCpmoc = detalleCpmoc;
    }

    public boolean getActivoCpmoc() {
        return activoCpmoc;
    }

    public void setActivoCpmoc(boolean activoCpmoc) {
        this.activoCpmoc = activoCpmoc;
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
        hash += (ideCpmoc != null ? ideCpmoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppModalidadCapacita)) {
            return false;
        }
        CppModalidadCapacita other = (CppModalidadCapacita) object;
        if ((this.ideCpmoc == null && other.ideCpmoc != null) || (this.ideCpmoc != null && !this.ideCpmoc.equals(other.ideCpmoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppModalidadCapacita[ ideCpmoc=" + ideCpmoc + " ]";
    }
    
}
