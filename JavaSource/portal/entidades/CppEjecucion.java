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
@Table(name = "cpp_ejecucion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppEjecucion.findAll", query = "SELECT c FROM CppEjecucion c"),
    @NamedQuery(name = "CppEjecucion.findByIdeCpeje", query = "SELECT c FROM CppEjecucion c WHERE c.ideCpeje = :ideCpeje"),
    @NamedQuery(name = "CppEjecucion.findByFechaCapacitaCpeje", query = "SELECT c FROM CppEjecucion c WHERE c.fechaCapacitaCpeje = :fechaCapacitaCpeje"),
    @NamedQuery(name = "CppEjecucion.findByNroHoraCpeje", query = "SELECT c FROM CppEjecucion c WHERE c.nroHoraCpeje = :nroHoraCpeje"),
    @NamedQuery(name = "CppEjecucion.findByObservacionCpeje", query = "SELECT c FROM CppEjecucion c WHERE c.observacionCpeje = :observacionCpeje"),
    @NamedQuery(name = "CppEjecucion.findByActivoCpeje", query = "SELECT c FROM CppEjecucion c WHERE c.activoCpeje = :activoCpeje"),
    @NamedQuery(name = "CppEjecucion.findByUsuarioIngre", query = "SELECT c FROM CppEjecucion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppEjecucion.findByFechaIngre", query = "SELECT c FROM CppEjecucion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppEjecucion.findByHoraIngre", query = "SELECT c FROM CppEjecucion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppEjecucion.findByUsuarioActua", query = "SELECT c FROM CppEjecucion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppEjecucion.findByFechaActua", query = "SELECT c FROM CppEjecucion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppEjecucion.findByHoraActua", query = "SELECT c FROM CppEjecucion c WHERE c.horaActua = :horaActua")})
public class CppEjecucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpeje", nullable = false)
    private Integer ideCpeje;
    @Column(name = "fecha_capacita_cpeje")
    @Temporal(TemporalType.DATE)
    private Date fechaCapacitaCpeje;
    @Column(name = "nro_hora_cpeje")
    private Integer nroHoraCpeje;
    @Size(max = 4000)
    @Column(name = "observacion_cpeje", length = 4000)
    private String observacionCpeje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpeje", nullable = false)
    private boolean activoCpeje;
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
    @JoinColumn(name = "ide_cpcap", referencedColumnName = "ide_cpcap")
    @ManyToOne
    private CppCapacitacion ideCpcap;

    public CppEjecucion() {
    }

    public CppEjecucion(Integer ideCpeje) {
        this.ideCpeje = ideCpeje;
    }

    public CppEjecucion(Integer ideCpeje, boolean activoCpeje) {
        this.ideCpeje = ideCpeje;
        this.activoCpeje = activoCpeje;
    }

    public Integer getIdeCpeje() {
        return ideCpeje;
    }

    public void setIdeCpeje(Integer ideCpeje) {
        this.ideCpeje = ideCpeje;
    }

    public Date getFechaCapacitaCpeje() {
        return fechaCapacitaCpeje;
    }

    public void setFechaCapacitaCpeje(Date fechaCapacitaCpeje) {
        this.fechaCapacitaCpeje = fechaCapacitaCpeje;
    }

    public Integer getNroHoraCpeje() {
        return nroHoraCpeje;
    }

    public void setNroHoraCpeje(Integer nroHoraCpeje) {
        this.nroHoraCpeje = nroHoraCpeje;
    }

    public String getObservacionCpeje() {
        return observacionCpeje;
    }

    public void setObservacionCpeje(String observacionCpeje) {
        this.observacionCpeje = observacionCpeje;
    }

    public boolean getActivoCpeje() {
        return activoCpeje;
    }

    public void setActivoCpeje(boolean activoCpeje) {
        this.activoCpeje = activoCpeje;
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

    public CppCapacitacion getIdeCpcap() {
        return ideCpcap;
    }

    public void setIdeCpcap(CppCapacitacion ideCpcap) {
        this.ideCpcap = ideCpcap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpeje != null ? ideCpeje.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppEjecucion)) {
            return false;
        }
        CppEjecucion other = (CppEjecucion) object;
        if ((this.ideCpeje == null && other.ideCpeje != null) || (this.ideCpeje != null && !this.ideCpeje.equals(other.ideCpeje))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppEjecucion[ ideCpeje=" + ideCpeje + " ]";
    }
    
}
