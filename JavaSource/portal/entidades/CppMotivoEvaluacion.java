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
@Table(name = "cpp_motivo_evaluacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppMotivoEvaluacion.findAll", query = "SELECT c FROM CppMotivoEvaluacion c"),
    @NamedQuery(name = "CppMotivoEvaluacion.findByIdeCpmoe", query = "SELECT c FROM CppMotivoEvaluacion c WHERE c.ideCpmoe = :ideCpmoe"),
    @NamedQuery(name = "CppMotivoEvaluacion.findByDetalleCpmoe", query = "SELECT c FROM CppMotivoEvaluacion c WHERE c.detalleCpmoe = :detalleCpmoe"),
    @NamedQuery(name = "CppMotivoEvaluacion.findByActivoCpmoe", query = "SELECT c FROM CppMotivoEvaluacion c WHERE c.activoCpmoe = :activoCpmoe"),
    @NamedQuery(name = "CppMotivoEvaluacion.findByUsuarioIngre", query = "SELECT c FROM CppMotivoEvaluacion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppMotivoEvaluacion.findByFechaIngre", query = "SELECT c FROM CppMotivoEvaluacion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppMotivoEvaluacion.findByHoraIngre", query = "SELECT c FROM CppMotivoEvaluacion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppMotivoEvaluacion.findByUsuarioActua", query = "SELECT c FROM CppMotivoEvaluacion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppMotivoEvaluacion.findByFechaActua", query = "SELECT c FROM CppMotivoEvaluacion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppMotivoEvaluacion.findByHoraActua", query = "SELECT c FROM CppMotivoEvaluacion c WHERE c.horaActua = :horaActua")})
public class CppMotivoEvaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpmoe", nullable = false)
    private Integer ideCpmoe;
    @Size(max = 100)
    @Column(name = "detalle_cpmoe", length = 100)
    private String detalleCpmoe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpmoe", nullable = false)
    private boolean activoCpmoe;
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
    @OneToMany(mappedBy = "ideCpmoe")
    private List<CppDetalleEvalEvento> cppDetalleEvalEventoList;

    public CppMotivoEvaluacion() {
    }

    public CppMotivoEvaluacion(Integer ideCpmoe) {
        this.ideCpmoe = ideCpmoe;
    }

    public CppMotivoEvaluacion(Integer ideCpmoe, boolean activoCpmoe) {
        this.ideCpmoe = ideCpmoe;
        this.activoCpmoe = activoCpmoe;
    }

    public Integer getIdeCpmoe() {
        return ideCpmoe;
    }

    public void setIdeCpmoe(Integer ideCpmoe) {
        this.ideCpmoe = ideCpmoe;
    }

    public String getDetalleCpmoe() {
        return detalleCpmoe;
    }

    public void setDetalleCpmoe(String detalleCpmoe) {
        this.detalleCpmoe = detalleCpmoe;
    }

    public boolean getActivoCpmoe() {
        return activoCpmoe;
    }

    public void setActivoCpmoe(boolean activoCpmoe) {
        this.activoCpmoe = activoCpmoe;
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

    public List<CppDetalleEvalEvento> getCppDetalleEvalEventoList() {
        return cppDetalleEvalEventoList;
    }

    public void setCppDetalleEvalEventoList(List<CppDetalleEvalEvento> cppDetalleEvalEventoList) {
        this.cppDetalleEvalEventoList = cppDetalleEvalEventoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpmoe != null ? ideCpmoe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppMotivoEvaluacion)) {
            return false;
        }
        CppMotivoEvaluacion other = (CppMotivoEvaluacion) object;
        if ((this.ideCpmoe == null && other.ideCpmoe != null) || (this.ideCpmoe != null && !this.ideCpmoe.equals(other.ideCpmoe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppMotivoEvaluacion[ ideCpmoe=" + ideCpmoe + " ]";
    }
    
}
