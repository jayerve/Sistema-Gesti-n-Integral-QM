/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "cpp_resultado_evaluacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppResultadoEvaluacion.findAll", query = "SELECT c FROM CppResultadoEvaluacion c"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByIdeCpree", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.ideCpree = :ideCpree"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByDetalleCpree", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.detalleCpree = :detalleCpree"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByPorcentajeCpree", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.porcentajeCpree = :porcentajeCpree"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByActivoCpree", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.activoCpree = :activoCpree"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByUsuarioIngre", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByFechaIngre", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByHoraIngre", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByUsuarioActua", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByFechaActua", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppResultadoEvaluacion.findByHoraActua", query = "SELECT c FROM CppResultadoEvaluacion c WHERE c.horaActua = :horaActua")})
public class CppResultadoEvaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpree", nullable = false)
    private Integer ideCpree;
    @Size(max = 1000)
    @Column(name = "detalle_cpree", length = 1000)
    private String detalleCpree;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_cpree", precision = 12, scale = 2)
    private BigDecimal porcentajeCpree;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpree", nullable = false)
    private boolean activoCpree;
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
    @OneToMany(mappedBy = "ideCpree")
    private List<CppEvaluaResulJefe> cppEvaluaResulJefeList;

    public CppResultadoEvaluacion() {
    }

    public CppResultadoEvaluacion(Integer ideCpree) {
        this.ideCpree = ideCpree;
    }

    public CppResultadoEvaluacion(Integer ideCpree, boolean activoCpree) {
        this.ideCpree = ideCpree;
        this.activoCpree = activoCpree;
    }

    public Integer getIdeCpree() {
        return ideCpree;
    }

    public void setIdeCpree(Integer ideCpree) {
        this.ideCpree = ideCpree;
    }

    public String getDetalleCpree() {
        return detalleCpree;
    }

    public void setDetalleCpree(String detalleCpree) {
        this.detalleCpree = detalleCpree;
    }

    public BigDecimal getPorcentajeCpree() {
        return porcentajeCpree;
    }

    public void setPorcentajeCpree(BigDecimal porcentajeCpree) {
        this.porcentajeCpree = porcentajeCpree;
    }

    public boolean getActivoCpree() {
        return activoCpree;
    }

    public void setActivoCpree(boolean activoCpree) {
        this.activoCpree = activoCpree;
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

    public List<CppEvaluaResulJefe> getCppEvaluaResulJefeList() {
        return cppEvaluaResulJefeList;
    }

    public void setCppEvaluaResulJefeList(List<CppEvaluaResulJefe> cppEvaluaResulJefeList) {
        this.cppEvaluaResulJefeList = cppEvaluaResulJefeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpree != null ? ideCpree.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppResultadoEvaluacion)) {
            return false;
        }
        CppResultadoEvaluacion other = (CppResultadoEvaluacion) object;
        if ((this.ideCpree == null && other.ideCpree != null) || (this.ideCpree != null && !this.ideCpree.equals(other.ideCpree))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppResultadoEvaluacion[ ideCpree=" + ideCpree + " ]";
    }
    
}
