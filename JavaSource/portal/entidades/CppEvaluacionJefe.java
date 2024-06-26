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
@Table(name = "cpp_evaluacion_jefe", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppEvaluacionJefe.findAll", query = "SELECT c FROM CppEvaluacionJefe c"),
    @NamedQuery(name = "CppEvaluacionJefe.findByIdeCpevj", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.ideCpevj = :ideCpevj"),
    @NamedQuery(name = "CppEvaluacionJefe.findByFechaEvaluacionCpevj", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.fechaEvaluacionCpevj = :fechaEvaluacionCpevj"),
    @NamedQuery(name = "CppEvaluacionJefe.findByObservacionCpvj", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.observacionCpvj = :observacionCpvj"),
    @NamedQuery(name = "CppEvaluacionJefe.findByActivoCpevj", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.activoCpevj = :activoCpevj"),
    @NamedQuery(name = "CppEvaluacionJefe.findByUsuarioIngre", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppEvaluacionJefe.findByFechaIngre", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppEvaluacionJefe.findByHoraIngre", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppEvaluacionJefe.findByUsuarioActua", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppEvaluacionJefe.findByFechaActua", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppEvaluacionJefe.findByHoraActua", query = "SELECT c FROM CppEvaluacionJefe c WHERE c.horaActua = :horaActua")})
public class CppEvaluacionJefe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpevj", nullable = false)
    private Integer ideCpevj;
    @Column(name = "fecha_evaluacion_cpevj")
    @Temporal(TemporalType.DATE)
    private Date fechaEvaluacionCpevj;
    @Size(max = 4000)
    @Column(name = "observacion_cpvj", length = 4000)
    private String observacionCpvj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpevj", nullable = false)
    private boolean activoCpevj;
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
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_cppar", referencedColumnName = "ide_cppar")
    @ManyToOne
    private CppParticipantes ideCppar;
    @JoinColumn(name = "ide_cpcap", referencedColumnName = "ide_cpcap")
    @ManyToOne
    private CppCapacitacion ideCpcap;
    @OneToMany(mappedBy = "ideCpevj")
    private List<CppDetalleNoefectivo> cppDetalleNoefectivoList;
    @OneToMany(mappedBy = "ideCpevj")
    private List<CppEvaluaResulJefe> cppEvaluaResulJefeList;

    public CppEvaluacionJefe() {
    }

    public CppEvaluacionJefe(Integer ideCpevj) {
        this.ideCpevj = ideCpevj;
    }

    public CppEvaluacionJefe(Integer ideCpevj, boolean activoCpevj) {
        this.ideCpevj = ideCpevj;
        this.activoCpevj = activoCpevj;
    }

    public Integer getIdeCpevj() {
        return ideCpevj;
    }

    public void setIdeCpevj(Integer ideCpevj) {
        this.ideCpevj = ideCpevj;
    }

    public Date getFechaEvaluacionCpevj() {
        return fechaEvaluacionCpevj;
    }

    public void setFechaEvaluacionCpevj(Date fechaEvaluacionCpevj) {
        this.fechaEvaluacionCpevj = fechaEvaluacionCpevj;
    }

    public String getObservacionCpvj() {
        return observacionCpvj;
    }

    public void setObservacionCpvj(String observacionCpvj) {
        this.observacionCpvj = observacionCpvj;
    }

    public boolean getActivoCpevj() {
        return activoCpevj;
    }

    public void setActivoCpevj(boolean activoCpevj) {
        this.activoCpevj = activoCpevj;
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

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
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

    public List<CppDetalleNoefectivo> getCppDetalleNoefectivoList() {
        return cppDetalleNoefectivoList;
    }

    public void setCppDetalleNoefectivoList(List<CppDetalleNoefectivo> cppDetalleNoefectivoList) {
        this.cppDetalleNoefectivoList = cppDetalleNoefectivoList;
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
        hash += (ideCpevj != null ? ideCpevj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppEvaluacionJefe)) {
            return false;
        }
        CppEvaluacionJefe other = (CppEvaluacionJefe) object;
        if ((this.ideCpevj == null && other.ideCpevj != null) || (this.ideCpevj != null && !this.ideCpevj.equals(other.ideCpevj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppEvaluacionJefe[ ideCpevj=" + ideCpevj + " ]";
    }
    
}
