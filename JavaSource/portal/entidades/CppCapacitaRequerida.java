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
@Table(name = "cpp_capacita_requerida", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppCapacitaRequerida.findAll", query = "SELECT c FROM CppCapacitaRequerida c"),
    @NamedQuery(name = "CppCapacitaRequerida.findByIdeCpcar", query = "SELECT c FROM CppCapacitaRequerida c WHERE c.ideCpcar = :ideCpcar"),
    @NamedQuery(name = "CppCapacitaRequerida.findByObservacionCpcar", query = "SELECT c FROM CppCapacitaRequerida c WHERE c.observacionCpcar = :observacionCpcar"),
    @NamedQuery(name = "CppCapacitaRequerida.findByActivoCpcar", query = "SELECT c FROM CppCapacitaRequerida c WHERE c.activoCpcar = :activoCpcar"),
    @NamedQuery(name = "CppCapacitaRequerida.findByUsuarioIngre", query = "SELECT c FROM CppCapacitaRequerida c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppCapacitaRequerida.findByFechaIngre", query = "SELECT c FROM CppCapacitaRequerida c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppCapacitaRequerida.findByHoraIngre", query = "SELECT c FROM CppCapacitaRequerida c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppCapacitaRequerida.findByUsuarioActua", query = "SELECT c FROM CppCapacitaRequerida c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppCapacitaRequerida.findByFechaActua", query = "SELECT c FROM CppCapacitaRequerida c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppCapacitaRequerida.findByHoraActua", query = "SELECT c FROM CppCapacitaRequerida c WHERE c.horaActua = :horaActua")})
public class CppCapacitaRequerida implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpcar", nullable = false)
    private Integer ideCpcar;
    @Size(max = 4000)
    @Column(name = "observacion_cpcar", length = 4000)
    private String observacionCpcar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpcar", nullable = false)
    private boolean activoCpcar;
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
    @JoinColumn(name = "ide_cpper", referencedColumnName = "ide_cpper")
    @ManyToOne
    private CppPerfil ideCpper;
    @JoinColumn(name = "ide_cmdec", referencedColumnName = "ide_cmdec")
    @ManyToOne
    private CmpDetalleCompetencia ideCmdec;

    public CppCapacitaRequerida() {
    }

    public CppCapacitaRequerida(Integer ideCpcar) {
        this.ideCpcar = ideCpcar;
    }

    public CppCapacitaRequerida(Integer ideCpcar, boolean activoCpcar) {
        this.ideCpcar = ideCpcar;
        this.activoCpcar = activoCpcar;
    }

    public Integer getIdeCpcar() {
        return ideCpcar;
    }

    public void setIdeCpcar(Integer ideCpcar) {
        this.ideCpcar = ideCpcar;
    }

    public String getObservacionCpcar() {
        return observacionCpcar;
    }

    public void setObservacionCpcar(String observacionCpcar) {
        this.observacionCpcar = observacionCpcar;
    }

    public boolean getActivoCpcar() {
        return activoCpcar;
    }

    public void setActivoCpcar(boolean activoCpcar) {
        this.activoCpcar = activoCpcar;
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

    public CppPerfil getIdeCpper() {
        return ideCpper;
    }

    public void setIdeCpper(CppPerfil ideCpper) {
        this.ideCpper = ideCpper;
    }

    public CmpDetalleCompetencia getIdeCmdec() {
        return ideCmdec;
    }

    public void setIdeCmdec(CmpDetalleCompetencia ideCmdec) {
        this.ideCmdec = ideCmdec;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpcar != null ? ideCpcar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppCapacitaRequerida)) {
            return false;
        }
        CppCapacitaRequerida other = (CppCapacitaRequerida) object;
        if ((this.ideCpcar == null && other.ideCpcar != null) || (this.ideCpcar != null && !this.ideCpcar.equals(other.ideCpcar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppCapacitaRequerida[ ideCpcar=" + ideCpcar + " ]";
    }
    
}
