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
@Table(name = "cpp_evaluacion_evento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppEvaluacionEvento.findAll", query = "SELECT c FROM CppEvaluacionEvento c"),
    @NamedQuery(name = "CppEvaluacionEvento.findByIdeCpeve", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.ideCpeve = :ideCpeve"),
    @NamedQuery(name = "CppEvaluacionEvento.findByFechaEvaluacionCpeve", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.fechaEvaluacionCpeve = :fechaEvaluacionCpeve"),
    @NamedQuery(name = "CppEvaluacionEvento.findByObservacionCpve", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.observacionCpve = :observacionCpve"),
    @NamedQuery(name = "CppEvaluacionEvento.findByActivoCpeve", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.activoCpeve = :activoCpeve"),
    @NamedQuery(name = "CppEvaluacionEvento.findByUsuarioIngre", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppEvaluacionEvento.findByFechaIngre", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppEvaluacionEvento.findByHoraIngre", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppEvaluacionEvento.findByUsuarioActua", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppEvaluacionEvento.findByFechaActua", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppEvaluacionEvento.findByHoraActua", query = "SELECT c FROM CppEvaluacionEvento c WHERE c.horaActua = :horaActua")})
public class CppEvaluacionEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpeve", nullable = false)
    private Integer ideCpeve;
    @Column(name = "fecha_evaluacion_cpeve")
    @Temporal(TemporalType.DATE)
    private Date fechaEvaluacionCpeve;
    @Size(max = 4000)
    @Column(name = "observacion_cpve", length = 4000)
    private String observacionCpve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpeve", nullable = false)
    private boolean activoCpeve;
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
    @OneToMany(mappedBy = "ideCpeve")
    private List<CppDetalleEvalEvento> cppDetalleEvalEventoList;

    public CppEvaluacionEvento() {
    }

    public CppEvaluacionEvento(Integer ideCpeve) {
        this.ideCpeve = ideCpeve;
    }

    public CppEvaluacionEvento(Integer ideCpeve, boolean activoCpeve) {
        this.ideCpeve = ideCpeve;
        this.activoCpeve = activoCpeve;
    }

    public Integer getIdeCpeve() {
        return ideCpeve;
    }

    public void setIdeCpeve(Integer ideCpeve) {
        this.ideCpeve = ideCpeve;
    }

    public Date getFechaEvaluacionCpeve() {
        return fechaEvaluacionCpeve;
    }

    public void setFechaEvaluacionCpeve(Date fechaEvaluacionCpeve) {
        this.fechaEvaluacionCpeve = fechaEvaluacionCpeve;
    }

    public String getObservacionCpve() {
        return observacionCpve;
    }

    public void setObservacionCpve(String observacionCpve) {
        this.observacionCpve = observacionCpve;
    }

    public boolean getActivoCpeve() {
        return activoCpeve;
    }

    public void setActivoCpeve(boolean activoCpeve) {
        this.activoCpeve = activoCpeve;
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

    public List<CppDetalleEvalEvento> getCppDetalleEvalEventoList() {
        return cppDetalleEvalEventoList;
    }

    public void setCppDetalleEvalEventoList(List<CppDetalleEvalEvento> cppDetalleEvalEventoList) {
        this.cppDetalleEvalEventoList = cppDetalleEvalEventoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpeve != null ? ideCpeve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppEvaluacionEvento)) {
            return false;
        }
        CppEvaluacionEvento other = (CppEvaluacionEvento) object;
        if ((this.ideCpeve == null && other.ideCpeve != null) || (this.ideCpeve != null && !this.ideCpeve.equals(other.ideCpeve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppEvaluacionEvento[ ideCpeve=" + ideCpeve + " ]";
    }
    
}
