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
@Table(name = "cpp_detalle_eval_persepcion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppDetalleEvalPersepcion.findAll", query = "SELECT c FROM CppDetalleEvalPersepcion c"),
    @NamedQuery(name = "CppDetalleEvalPersepcion.findByIdeCpdep", query = "SELECT c FROM CppDetalleEvalPersepcion c WHERE c.ideCpdep = :ideCpdep"),
    @NamedQuery(name = "CppDetalleEvalPersepcion.findByActivoCpdep", query = "SELECT c FROM CppDetalleEvalPersepcion c WHERE c.activoCpdep = :activoCpdep"),
    @NamedQuery(name = "CppDetalleEvalPersepcion.findByUsuarioIngre", query = "SELECT c FROM CppDetalleEvalPersepcion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppDetalleEvalPersepcion.findByFechaIngre", query = "SELECT c FROM CppDetalleEvalPersepcion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppDetalleEvalPersepcion.findByHoraIngre", query = "SELECT c FROM CppDetalleEvalPersepcion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppDetalleEvalPersepcion.findByUsuarioActua", query = "SELECT c FROM CppDetalleEvalPersepcion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppDetalleEvalPersepcion.findByFechaActua", query = "SELECT c FROM CppDetalleEvalPersepcion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppDetalleEvalPersepcion.findByHoraActua", query = "SELECT c FROM CppDetalleEvalPersepcion c WHERE c.horaActua = :horaActua")})
public class CppDetalleEvalPersepcion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpdep", nullable = false)
    private Integer ideCpdep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpdep", nullable = false)
    private boolean activoCpdep;
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
    @JoinColumn(name = "ide_cppae", referencedColumnName = "ide_cppae")
    @ManyToOne
    private CppPersepcionCapacitacion ideCppae;
    @JoinColumn(name = "ide_cpevp", referencedColumnName = "ide_cpevp")
    @ManyToOne
    private CppEvaluacionPersepcion ideCpevp;
    @JoinColumn(name = "ide_cpeqe", referencedColumnName = "ide_cpeqe")
    @ManyToOne
    private CppEquivalenciaEvento ideCpeqe;

    public CppDetalleEvalPersepcion() {
    }

    public CppDetalleEvalPersepcion(Integer ideCpdep) {
        this.ideCpdep = ideCpdep;
    }

    public CppDetalleEvalPersepcion(Integer ideCpdep, boolean activoCpdep) {
        this.ideCpdep = ideCpdep;
        this.activoCpdep = activoCpdep;
    }

    public Integer getIdeCpdep() {
        return ideCpdep;
    }

    public void setIdeCpdep(Integer ideCpdep) {
        this.ideCpdep = ideCpdep;
    }

    public boolean getActivoCpdep() {
        return activoCpdep;
    }

    public void setActivoCpdep(boolean activoCpdep) {
        this.activoCpdep = activoCpdep;
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

    public CppPersepcionCapacitacion getIdeCppae() {
        return ideCppae;
    }

    public void setIdeCppae(CppPersepcionCapacitacion ideCppae) {
        this.ideCppae = ideCppae;
    }

    public CppEvaluacionPersepcion getIdeCpevp() {
        return ideCpevp;
    }

    public void setIdeCpevp(CppEvaluacionPersepcion ideCpevp) {
        this.ideCpevp = ideCpevp;
    }

    public CppEquivalenciaEvento getIdeCpeqe() {
        return ideCpeqe;
    }

    public void setIdeCpeqe(CppEquivalenciaEvento ideCpeqe) {
        this.ideCpeqe = ideCpeqe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpdep != null ? ideCpdep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppDetalleEvalPersepcion)) {
            return false;
        }
        CppDetalleEvalPersepcion other = (CppDetalleEvalPersepcion) object;
        if ((this.ideCpdep == null && other.ideCpdep != null) || (this.ideCpdep != null && !this.ideCpdep.equals(other.ideCpdep))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppDetalleEvalPersepcion[ ideCpdep=" + ideCpdep + " ]";
    }
    
}
