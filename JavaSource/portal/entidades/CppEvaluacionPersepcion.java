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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "cpp_evaluacion_persepcion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppEvaluacionPersepcion.findAll", query = "SELECT c FROM CppEvaluacionPersepcion c"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByIdeCpevp", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.ideCpevp = :ideCpevp"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByFechaEvaluacionCpevp", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.fechaEvaluacionCpevp = :fechaEvaluacionCpevp"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByResponsableCpevp", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.responsableCpevp = :responsableCpevp"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByObservacionCpevp", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.observacionCpevp = :observacionCpevp"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByActivoCpevp", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.activoCpevp = :activoCpevp"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByUsuarioIngre", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByFechaIngre", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByHoraIngre", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByUsuarioActua", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByFechaActua", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppEvaluacionPersepcion.findByHoraActua", query = "SELECT c FROM CppEvaluacionPersepcion c WHERE c.horaActua = :horaActua")})
public class CppEvaluacionPersepcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpevp", nullable = false)
    private Integer ideCpevp;
    @Column(name = "fecha_evaluacion_cpevp")
    @Temporal(TemporalType.DATE)
    private Date fechaEvaluacionCpevp;
    @Size(max = 100)
    @Column(name = "responsable_cpevp", length = 100)
    private String responsableCpevp;
    @Size(max = 4000)
    @Column(name = "observacion_cpevp", length = 4000)
    private String observacionCpevp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpevp", nullable = false)
    private boolean activoCpevp;
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
    @OneToMany(mappedBy = "ideCpevp")
    private List<CppDetalleEvalPersepcion> cppDetalleEvalPersepcionList;
    @JoinColumn(name = "ide_cpcap", referencedColumnName = "ide_cpcap")
    @ManyToOne
    private CppCapacitacion ideCpcap;

    public CppEvaluacionPersepcion() {
    }

    public CppEvaluacionPersepcion(Integer ideCpevp) {
        this.ideCpevp = ideCpevp;
    }

    public CppEvaluacionPersepcion(Integer ideCpevp, boolean activoCpevp) {
        this.ideCpevp = ideCpevp;
        this.activoCpevp = activoCpevp;
    }

    public Integer getIdeCpevp() {
        return ideCpevp;
    }

    public void setIdeCpevp(Integer ideCpevp) {
        this.ideCpevp = ideCpevp;
    }

    public Date getFechaEvaluacionCpevp() {
        return fechaEvaluacionCpevp;
    }

    public void setFechaEvaluacionCpevp(Date fechaEvaluacionCpevp) {
        this.fechaEvaluacionCpevp = fechaEvaluacionCpevp;
    }

    public String getResponsableCpevp() {
        return responsableCpevp;
    }

    public void setResponsableCpevp(String responsableCpevp) {
        this.responsableCpevp = responsableCpevp;
    }

    public String getObservacionCpevp() {
        return observacionCpevp;
    }

    public void setObservacionCpevp(String observacionCpevp) {
        this.observacionCpevp = observacionCpevp;
    }

    public boolean getActivoCpevp() {
        return activoCpevp;
    }

    public void setActivoCpevp(boolean activoCpevp) {
        this.activoCpevp = activoCpevp;
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

    public List<CppDetalleEvalPersepcion> getCppDetalleEvalPersepcionList() {
        return cppDetalleEvalPersepcionList;
    }

    public void setCppDetalleEvalPersepcionList(List<CppDetalleEvalPersepcion> cppDetalleEvalPersepcionList) {
        this.cppDetalleEvalPersepcionList = cppDetalleEvalPersepcionList;
    }

    public CppCapacitacion getIdeCpcap() {
        return ideCpcap;
    }

    public void setIdeCpcap(CppCapacitacion ideCpcap) {
        this.ideCpcap = ideCpcap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpevp != null ? ideCpevp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppEvaluacionPersepcion)) {
            return false;
        }
        CppEvaluacionPersepcion other = (CppEvaluacionPersepcion) object;
        if ((this.ideCpevp == null && other.ideCpevp != null) || (this.ideCpevp != null && !this.ideCpevp.equals(other.ideCpevp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppEvaluacionPersepcion[ ideCpevp=" + ideCpevp + " ]";
    }
    
}
