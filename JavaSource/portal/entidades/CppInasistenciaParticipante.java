/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "cpp_inasistencia_participante", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppInasistenciaParticipante.findAll", query = "SELECT c FROM CppInasistenciaParticipante c"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByIdeCpinp", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.ideCpinp = :ideCpinp"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByFechaCpinp", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.fechaCpinp = :fechaCpinp"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByNroHorasCpinp", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.nroHorasCpinp = :nroHorasCpinp"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByRazonCpinp", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.razonCpinp = :razonCpinp"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByObservacionCpinp", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.observacionCpinp = :observacionCpinp"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByArchivoCpinp", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.archivoCpinp = :archivoCpinp"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByJustificadoCpinp", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.justificadoCpinp = :justificadoCpinp"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByActivoCpinp", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.activoCpinp = :activoCpinp"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByUsuarioIngre", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByFechaIngre", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByHoraIngre", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByUsuarioActua", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByFechaActua", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppInasistenciaParticipante.findByHoraActua", query = "SELECT c FROM CppInasistenciaParticipante c WHERE c.horaActua = :horaActua")})
public class CppInasistenciaParticipante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpinp", nullable = false)
    private Integer ideCpinp;
    @Column(name = "fecha_cpinp")
    @Temporal(TemporalType.DATE)
    private Date fechaCpinp;
    @Column(name = "nro_horas_cpinp")
    private Integer nroHorasCpinp;
    @Size(max = 1000)
    @Column(name = "razon_cpinp", length = 1000)
    private String razonCpinp;
    @Size(max = 4000)
    @Column(name = "observacion_cpinp", length = 4000)
    private String observacionCpinp;
    @Size(max = 100)
    @Column(name = "archivo_cpinp", length = 100)
    private String archivoCpinp;
    @Column(name = "justificado_cpinp")
    private Integer justificadoCpinp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpinp", nullable = false)
    private boolean activoCpinp;
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

    public CppInasistenciaParticipante() {
    }

    public CppInasistenciaParticipante(Integer ideCpinp) {
        this.ideCpinp = ideCpinp;
    }

    public CppInasistenciaParticipante(Integer ideCpinp, boolean activoCpinp) {
        this.ideCpinp = ideCpinp;
        this.activoCpinp = activoCpinp;
    }

    public Integer getIdeCpinp() {
        return ideCpinp;
    }

    public void setIdeCpinp(Integer ideCpinp) {
        this.ideCpinp = ideCpinp;
    }

    public Date getFechaCpinp() {
        return fechaCpinp;
    }

    public void setFechaCpinp(Date fechaCpinp) {
        this.fechaCpinp = fechaCpinp;
    }

    public Integer getNroHorasCpinp() {
        return nroHorasCpinp;
    }

    public void setNroHorasCpinp(Integer nroHorasCpinp) {
        this.nroHorasCpinp = nroHorasCpinp;
    }

    public String getRazonCpinp() {
        return razonCpinp;
    }

    public void setRazonCpinp(String razonCpinp) {
        this.razonCpinp = razonCpinp;
    }

    public String getObservacionCpinp() {
        return observacionCpinp;
    }

    public void setObservacionCpinp(String observacionCpinp) {
        this.observacionCpinp = observacionCpinp;
    }

    public String getArchivoCpinp() {
        return archivoCpinp;
    }

    public void setArchivoCpinp(String archivoCpinp) {
        this.archivoCpinp = archivoCpinp;
    }

    public Integer getJustificadoCpinp() {
        return justificadoCpinp;
    }

    public void setJustificadoCpinp(Integer justificadoCpinp) {
        this.justificadoCpinp = justificadoCpinp;
    }

    public boolean getActivoCpinp() {
        return activoCpinp;
    }

    public void setActivoCpinp(boolean activoCpinp) {
        this.activoCpinp = activoCpinp;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpinp != null ? ideCpinp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppInasistenciaParticipante)) {
            return false;
        }
        CppInasistenciaParticipante other = (CppInasistenciaParticipante) object;
        if ((this.ideCpinp == null && other.ideCpinp != null) || (this.ideCpinp != null && !this.ideCpinp.equals(other.ideCpinp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppInasistenciaParticipante[ ideCpinp=" + ideCpinp + " ]";
    }
    
}
