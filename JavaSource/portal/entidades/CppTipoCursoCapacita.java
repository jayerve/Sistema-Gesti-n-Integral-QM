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
@Table(name = "cpp_tipo_curso_capacita", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppTipoCursoCapacita.findAll", query = "SELECT c FROM CppTipoCursoCapacita c"),
    @NamedQuery(name = "CppTipoCursoCapacita.findByIdeCptcc", query = "SELECT c FROM CppTipoCursoCapacita c WHERE c.ideCptcc = :ideCptcc"),
    @NamedQuery(name = "CppTipoCursoCapacita.findByDetalleCptcc", query = "SELECT c FROM CppTipoCursoCapacita c WHERE c.detalleCptcc = :detalleCptcc"),
    @NamedQuery(name = "CppTipoCursoCapacita.findByActivoCptcc", query = "SELECT c FROM CppTipoCursoCapacita c WHERE c.activoCptcc = :activoCptcc"),
    @NamedQuery(name = "CppTipoCursoCapacita.findByUsuarioIngre", query = "SELECT c FROM CppTipoCursoCapacita c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppTipoCursoCapacita.findByFechaIngre", query = "SELECT c FROM CppTipoCursoCapacita c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppTipoCursoCapacita.findByHoraIngre", query = "SELECT c FROM CppTipoCursoCapacita c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppTipoCursoCapacita.findByUsuarioActua", query = "SELECT c FROM CppTipoCursoCapacita c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppTipoCursoCapacita.findByFechaActua", query = "SELECT c FROM CppTipoCursoCapacita c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppTipoCursoCapacita.findByHoraActua", query = "SELECT c FROM CppTipoCursoCapacita c WHERE c.horaActua = :horaActua")})
public class CppTipoCursoCapacita implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cptcc", nullable = false)
    private Integer ideCptcc;
    @Size(max = 50)
    @Column(name = "detalle_cptcc", length = 50)
    private String detalleCptcc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cptcc", nullable = false)
    private boolean activoCptcc;
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
    @OneToMany(mappedBy = "ideCptcc")
    private List<CppCapacitacion> cppCapacitacionList;
    @OneToMany(mappedBy = "ideCptcc")
    private List<CppPlanCapacitacion> cppPlanCapacitacionList;

    public CppTipoCursoCapacita() {
    }

    public CppTipoCursoCapacita(Integer ideCptcc) {
        this.ideCptcc = ideCptcc;
    }

    public CppTipoCursoCapacita(Integer ideCptcc, boolean activoCptcc) {
        this.ideCptcc = ideCptcc;
        this.activoCptcc = activoCptcc;
    }

    public Integer getIdeCptcc() {
        return ideCptcc;
    }

    public void setIdeCptcc(Integer ideCptcc) {
        this.ideCptcc = ideCptcc;
    }

    public String getDetalleCptcc() {
        return detalleCptcc;
    }

    public void setDetalleCptcc(String detalleCptcc) {
        this.detalleCptcc = detalleCptcc;
    }

    public boolean getActivoCptcc() {
        return activoCptcc;
    }

    public void setActivoCptcc(boolean activoCptcc) {
        this.activoCptcc = activoCptcc;
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

    public List<CppPlanCapacitacion> getCppPlanCapacitacionList() {
        return cppPlanCapacitacionList;
    }

    public void setCppPlanCapacitacionList(List<CppPlanCapacitacion> cppPlanCapacitacionList) {
        this.cppPlanCapacitacionList = cppPlanCapacitacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCptcc != null ? ideCptcc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppTipoCursoCapacita)) {
            return false;
        }
        CppTipoCursoCapacita other = (CppTipoCursoCapacita) object;
        if ((this.ideCptcc == null && other.ideCptcc != null) || (this.ideCptcc != null && !this.ideCptcc.equals(other.ideCptcc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppTipoCursoCapacita[ ideCptcc=" + ideCptcc + " ]";
    }
    
}
