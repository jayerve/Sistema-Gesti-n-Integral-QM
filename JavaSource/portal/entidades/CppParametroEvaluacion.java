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
@Table(name = "cpp_parametro_evaluacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppParametroEvaluacion.findAll", query = "SELECT c FROM CppParametroEvaluacion c"),
    @NamedQuery(name = "CppParametroEvaluacion.findByIdeCppae", query = "SELECT c FROM CppParametroEvaluacion c WHERE c.ideCppae = :ideCppae"),
    @NamedQuery(name = "CppParametroEvaluacion.findByDetalleCppae", query = "SELECT c FROM CppParametroEvaluacion c WHERE c.detalleCppae = :detalleCppae"),
    @NamedQuery(name = "CppParametroEvaluacion.findByActivoCppae", query = "SELECT c FROM CppParametroEvaluacion c WHERE c.activoCppae = :activoCppae"),
    @NamedQuery(name = "CppParametroEvaluacion.findByUsuarioIngre", query = "SELECT c FROM CppParametroEvaluacion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppParametroEvaluacion.findByFechaIngre", query = "SELECT c FROM CppParametroEvaluacion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppParametroEvaluacion.findByHoraIngre", query = "SELECT c FROM CppParametroEvaluacion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppParametroEvaluacion.findByUsuarioActua", query = "SELECT c FROM CppParametroEvaluacion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppParametroEvaluacion.findByFechaActua", query = "SELECT c FROM CppParametroEvaluacion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppParametroEvaluacion.findByHoraActua", query = "SELECT c FROM CppParametroEvaluacion c WHERE c.horaActua = :horaActua")})
public class CppParametroEvaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cppae", nullable = false)
    private Integer ideCppae;
    @Size(max = 100)
    @Column(name = "detalle_cppae", length = 100)
    private String detalleCppae;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cppae", nullable = false)
    private boolean activoCppae;
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
    @OneToMany(mappedBy = "ideCppae")
    private List<CppDetalleEvalTaller> cppDetalleEvalTallerList;

    public CppParametroEvaluacion() {
    }

    public CppParametroEvaluacion(Integer ideCppae) {
        this.ideCppae = ideCppae;
    }

    public CppParametroEvaluacion(Integer ideCppae, boolean activoCppae) {
        this.ideCppae = ideCppae;
        this.activoCppae = activoCppae;
    }

    public Integer getIdeCppae() {
        return ideCppae;
    }

    public void setIdeCppae(Integer ideCppae) {
        this.ideCppae = ideCppae;
    }

    public String getDetalleCppae() {
        return detalleCppae;
    }

    public void setDetalleCppae(String detalleCppae) {
        this.detalleCppae = detalleCppae;
    }

    public boolean getActivoCppae() {
        return activoCppae;
    }

    public void setActivoCppae(boolean activoCppae) {
        this.activoCppae = activoCppae;
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

    public List<CppDetalleEvalTaller> getCppDetalleEvalTallerList() {
        return cppDetalleEvalTallerList;
    }

    public void setCppDetalleEvalTallerList(List<CppDetalleEvalTaller> cppDetalleEvalTallerList) {
        this.cppDetalleEvalTallerList = cppDetalleEvalTallerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCppae != null ? ideCppae.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppParametroEvaluacion)) {
            return false;
        }
        CppParametroEvaluacion other = (CppParametroEvaluacion) object;
        if ((this.ideCppae == null && other.ideCppae != null) || (this.ideCppae != null && !this.ideCppae.equals(other.ideCppae))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppParametroEvaluacion[ ideCppae=" + ideCppae + " ]";
    }
    
}
