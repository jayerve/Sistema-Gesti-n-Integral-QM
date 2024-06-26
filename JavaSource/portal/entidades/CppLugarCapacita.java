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
@Table(name = "cpp_lugar_capacita", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppLugarCapacita.findAll", query = "SELECT c FROM CppLugarCapacita c"),
    @NamedQuery(name = "CppLugarCapacita.findByIdeCpluc", query = "SELECT c FROM CppLugarCapacita c WHERE c.ideCpluc = :ideCpluc"),
    @NamedQuery(name = "CppLugarCapacita.findByDetalleCpluc", query = "SELECT c FROM CppLugarCapacita c WHERE c.detalleCpluc = :detalleCpluc"),
    @NamedQuery(name = "CppLugarCapacita.findByActivoCpluc", query = "SELECT c FROM CppLugarCapacita c WHERE c.activoCpluc = :activoCpluc"),
    @NamedQuery(name = "CppLugarCapacita.findByUsuarioIngre", query = "SELECT c FROM CppLugarCapacita c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppLugarCapacita.findByFechaIngre", query = "SELECT c FROM CppLugarCapacita c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppLugarCapacita.findByHoraIngre", query = "SELECT c FROM CppLugarCapacita c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppLugarCapacita.findByUsuarioActua", query = "SELECT c FROM CppLugarCapacita c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppLugarCapacita.findByFechaActua", query = "SELECT c FROM CppLugarCapacita c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppLugarCapacita.findByHoraActua", query = "SELECT c FROM CppLugarCapacita c WHERE c.horaActua = :horaActua")})
public class CppLugarCapacita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpluc", nullable = false)
    private Integer ideCpluc;
    @Size(max = 50)
    @Column(name = "detalle_cpluc", length = 50)
    private String detalleCpluc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpluc", nullable = false)
    private boolean activoCpluc;
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
    @OneToMany(mappedBy = "ideCpluc")
    private List<CppCapacitacion> cppCapacitacionList;

    public CppLugarCapacita() {
    }

    public CppLugarCapacita(Integer ideCpluc) {
        this.ideCpluc = ideCpluc;
    }

    public CppLugarCapacita(Integer ideCpluc, boolean activoCpluc) {
        this.ideCpluc = ideCpluc;
        this.activoCpluc = activoCpluc;
    }

    public Integer getIdeCpluc() {
        return ideCpluc;
    }

    public void setIdeCpluc(Integer ideCpluc) {
        this.ideCpluc = ideCpluc;
    }

    public String getDetalleCpluc() {
        return detalleCpluc;
    }

    public void setDetalleCpluc(String detalleCpluc) {
        this.detalleCpluc = detalleCpluc;
    }

    public boolean getActivoCpluc() {
        return activoCpluc;
    }

    public void setActivoCpluc(boolean activoCpluc) {
        this.activoCpluc = activoCpluc;
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
        hash += (ideCpluc != null ? ideCpluc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppLugarCapacita)) {
            return false;
        }
        CppLugarCapacita other = (CppLugarCapacita) object;
        if ((this.ideCpluc == null && other.ideCpluc != null) || (this.ideCpluc != null && !this.ideCpluc.equals(other.ideCpluc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppLugarCapacita[ ideCpluc=" + ideCpluc + " ]";
    }
    
}
