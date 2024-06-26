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
@Table(name = "cpp_evaluacion_taller", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppEvaluacionTaller.findAll", query = "SELECT c FROM CppEvaluacionTaller c"),
    @NamedQuery(name = "CppEvaluacionTaller.findByIdeCpevt", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.ideCpevt = :ideCpevt"),
    @NamedQuery(name = "CppEvaluacionTaller.findByFechaEvaluacionCpevt", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.fechaEvaluacionCpevt = :fechaEvaluacionCpevt"),
    @NamedQuery(name = "CppEvaluacionTaller.findByObservacionCpvt", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.observacionCpvt = :observacionCpvt"),
    @NamedQuery(name = "CppEvaluacionTaller.findByActivoCpevt", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.activoCpevt = :activoCpevt"),
    @NamedQuery(name = "CppEvaluacionTaller.findByUsuarioIngre", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppEvaluacionTaller.findByFechaIngre", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppEvaluacionTaller.findByHoraIngre", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppEvaluacionTaller.findByUsuarioActua", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppEvaluacionTaller.findByFechaActua", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppEvaluacionTaller.findByHoraActua", query = "SELECT c FROM CppEvaluacionTaller c WHERE c.horaActua = :horaActua")})
public class CppEvaluacionTaller implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpevt", nullable = false)
    private Integer ideCpevt;
    @Column(name = "fecha_evaluacion_cpevt")
    @Temporal(TemporalType.DATE)
    private Date fechaEvaluacionCpevt;
    @Size(max = 4000)
    @Column(name = "observacion_cpvt", length = 4000)
    private String observacionCpvt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpevt", nullable = false)
    private boolean activoCpevt;
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
    @JoinColumn(name = "ide_cppar", referencedColumnName = "ide_cppar")
    @ManyToOne
    private CppParticipantes ideCppar;
    @JoinColumn(name = "ide_cpcap", referencedColumnName = "ide_cpcap")
    @ManyToOne
    private CppCapacitacion ideCpcap;
    @OneToMany(mappedBy = "ideCpevt")
    private List<CppDetalleEvalTaller> cppDetalleEvalTallerList;

    public CppEvaluacionTaller() {
    }

    public CppEvaluacionTaller(Integer ideCpevt) {
        this.ideCpevt = ideCpevt;
    }

    public CppEvaluacionTaller(Integer ideCpevt, boolean activoCpevt) {
        this.ideCpevt = ideCpevt;
        this.activoCpevt = activoCpevt;
    }

    public Integer getIdeCpevt() {
        return ideCpevt;
    }

    public void setIdeCpevt(Integer ideCpevt) {
        this.ideCpevt = ideCpevt;
    }

    public Date getFechaEvaluacionCpevt() {
        return fechaEvaluacionCpevt;
    }

    public void setFechaEvaluacionCpevt(Date fechaEvaluacionCpevt) {
        this.fechaEvaluacionCpevt = fechaEvaluacionCpevt;
    }

    public String getObservacionCpvt() {
        return observacionCpvt;
    }

    public void setObservacionCpvt(String observacionCpvt) {
        this.observacionCpvt = observacionCpvt;
    }

    public boolean getActivoCpevt() {
        return activoCpevt;
    }

    public void setActivoCpevt(boolean activoCpevt) {
        this.activoCpevt = activoCpevt;
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

    public CppParticipantes getIdeCppar() {
        return ideCppar;
    }

    public void setIdeCppar(CppParticipantes ideCppar) {
        this.ideCppar = ideCppar;
    }

    public CppCapacitacion getIdeCpcap() {
        return ideCpcap;
    }

    public void setIdeCpcap(CppCapacitacion ideCpcap) {
        this.ideCpcap = ideCpcap;
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
        hash += (ideCpevt != null ? ideCpevt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppEvaluacionTaller)) {
            return false;
        }
        CppEvaluacionTaller other = (CppEvaluacionTaller) object;
        if ((this.ideCpevt == null && other.ideCpevt != null) || (this.ideCpevt != null && !this.ideCpevt.equals(other.ideCpevt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppEvaluacionTaller[ ideCpevt=" + ideCpevt + " ]";
    }
    
}
