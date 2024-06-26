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
@Table(name = "cpp_participantes", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppParticipantes.findAll", query = "SELECT c FROM CppParticipantes c"),
    @NamedQuery(name = "CppParticipantes.findByIdeCppar", query = "SELECT c FROM CppParticipantes c WHERE c.ideCppar = :ideCppar"),
    @NamedQuery(name = "CppParticipantes.findByNotaAprobadoCppar", query = "SELECT c FROM CppParticipantes c WHERE c.notaAprobadoCppar = :notaAprobadoCppar"),
    @NamedQuery(name = "CppParticipantes.findByAprobadoCppar", query = "SELECT c FROM CppParticipantes c WHERE c.aprobadoCppar = :aprobadoCppar"),
    @NamedQuery(name = "CppParticipantes.findByFechaAprobacionCppar", query = "SELECT c FROM CppParticipantes c WHERE c.fechaAprobacionCppar = :fechaAprobacionCppar"),
    @NamedQuery(name = "CppParticipantes.findByObservacionCppar", query = "SELECT c FROM CppParticipantes c WHERE c.observacionCppar = :observacionCppar"),
    @NamedQuery(name = "CppParticipantes.findByActivoCppar", query = "SELECT c FROM CppParticipantes c WHERE c.activoCppar = :activoCppar"),
    @NamedQuery(name = "CppParticipantes.findByUsuarioIngre", query = "SELECT c FROM CppParticipantes c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppParticipantes.findByFechaIngre", query = "SELECT c FROM CppParticipantes c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppParticipantes.findByHoraIngre", query = "SELECT c FROM CppParticipantes c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppParticipantes.findByUsuarioActua", query = "SELECT c FROM CppParticipantes c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppParticipantes.findByFechaActua", query = "SELECT c FROM CppParticipantes c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppParticipantes.findByHoraActua", query = "SELECT c FROM CppParticipantes c WHERE c.horaActua = :horaActua")})
public class CppParticipantes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cppar", nullable = false)
    private Integer ideCppar;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nota_aprobado_cppar", precision = 12, scale = 2)
    private BigDecimal notaAprobadoCppar;
    @Column(name = "aprobado_cppar")
    private Integer aprobadoCppar;
    @Column(name = "fecha_aprobacion_cppar")
    @Temporal(TemporalType.DATE)
    private Date fechaAprobacionCppar;
    @Size(max = 1000)
    @Column(name = "observacion_cppar", length = 1000)
    private String observacionCppar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cppar", nullable = false)
    private boolean activoCppar;
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
    @OneToMany(mappedBy = "ideCppar")
    private List<CppEvaluacionEvento> cppEvaluacionEventoList;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_cpcap", referencedColumnName = "ide_cpcap")
    @ManyToOne
    private CppCapacitacion ideCpcap;
    @OneToMany(mappedBy = "ideCppar")
    private List<CppEvaluacionTaller> cppEvaluacionTallerList;
    @OneToMany(mappedBy = "ideCppar")
    private List<CppEvaluacionJefe> cppEvaluacionJefeList;
    @OneToMany(mappedBy = "ideCppar")
    private List<CppInasistenciaParticipante> cppInasistenciaParticipanteList;
    @OneToMany(mappedBy = "ideCppar")
    private List<CppArchivoParticipante> cppArchivoParticipanteList;

    public CppParticipantes() {
    }

    public CppParticipantes(Integer ideCppar) {
        this.ideCppar = ideCppar;
    }

    public CppParticipantes(Integer ideCppar, boolean activoCppar) {
        this.ideCppar = ideCppar;
        this.activoCppar = activoCppar;
    }

    public Integer getIdeCppar() {
        return ideCppar;
    }

    public void setIdeCppar(Integer ideCppar) {
        this.ideCppar = ideCppar;
    }

    public BigDecimal getNotaAprobadoCppar() {
        return notaAprobadoCppar;
    }

    public void setNotaAprobadoCppar(BigDecimal notaAprobadoCppar) {
        this.notaAprobadoCppar = notaAprobadoCppar;
    }

    public Integer getAprobadoCppar() {
        return aprobadoCppar;
    }

    public void setAprobadoCppar(Integer aprobadoCppar) {
        this.aprobadoCppar = aprobadoCppar;
    }

    public Date getFechaAprobacionCppar() {
        return fechaAprobacionCppar;
    }

    public void setFechaAprobacionCppar(Date fechaAprobacionCppar) {
        this.fechaAprobacionCppar = fechaAprobacionCppar;
    }

    public String getObservacionCppar() {
        return observacionCppar;
    }

    public void setObservacionCppar(String observacionCppar) {
        this.observacionCppar = observacionCppar;
    }

    public boolean getActivoCppar() {
        return activoCppar;
    }

    public void setActivoCppar(boolean activoCppar) {
        this.activoCppar = activoCppar;
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

    public List<CppEvaluacionEvento> getCppEvaluacionEventoList() {
        return cppEvaluacionEventoList;
    }

    public void setCppEvaluacionEventoList(List<CppEvaluacionEvento> cppEvaluacionEventoList) {
        this.cppEvaluacionEventoList = cppEvaluacionEventoList;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public CppCapacitacion getIdeCpcap() {
        return ideCpcap;
    }

    public void setIdeCpcap(CppCapacitacion ideCpcap) {
        this.ideCpcap = ideCpcap;
    }

    public List<CppEvaluacionTaller> getCppEvaluacionTallerList() {
        return cppEvaluacionTallerList;
    }

    public void setCppEvaluacionTallerList(List<CppEvaluacionTaller> cppEvaluacionTallerList) {
        this.cppEvaluacionTallerList = cppEvaluacionTallerList;
    }

    public List<CppEvaluacionJefe> getCppEvaluacionJefeList() {
        return cppEvaluacionJefeList;
    }

    public void setCppEvaluacionJefeList(List<CppEvaluacionJefe> cppEvaluacionJefeList) {
        this.cppEvaluacionJefeList = cppEvaluacionJefeList;
    }

    public List<CppInasistenciaParticipante> getCppInasistenciaParticipanteList() {
        return cppInasistenciaParticipanteList;
    }

    public void setCppInasistenciaParticipanteList(List<CppInasistenciaParticipante> cppInasistenciaParticipanteList) {
        this.cppInasistenciaParticipanteList = cppInasistenciaParticipanteList;
    }

    public List<CppArchivoParticipante> getCppArchivoParticipanteList() {
        return cppArchivoParticipanteList;
    }

    public void setCppArchivoParticipanteList(List<CppArchivoParticipante> cppArchivoParticipanteList) {
        this.cppArchivoParticipanteList = cppArchivoParticipanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCppar != null ? ideCppar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppParticipantes)) {
            return false;
        }
        CppParticipantes other = (CppParticipantes) object;
        if ((this.ideCppar == null && other.ideCppar != null) || (this.ideCppar != null && !this.ideCppar.equals(other.ideCppar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppParticipantes[ ideCppar=" + ideCppar + " ]";
    }
    
}
