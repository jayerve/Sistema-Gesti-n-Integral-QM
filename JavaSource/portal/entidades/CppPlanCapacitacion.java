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
@Table(name = "cpp_plan_capacitacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppPlanCapacitacion.findAll", query = "SELECT c FROM CppPlanCapacitacion c"),
    @NamedQuery(name = "CppPlanCapacitacion.findByIdeCpplc", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.ideCpplc = :ideCpplc"),
    @NamedQuery(name = "CppPlanCapacitacion.findByIdeGemes", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.ideGemes = :ideGemes"),
    @NamedQuery(name = "CppPlanCapacitacion.findByNombreCapacitaCpplc", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.nombreCapacitaCpplc = :nombreCapacitaCpplc"),
    @NamedQuery(name = "CppPlanCapacitacion.findByObservacionCpplc", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.observacionCpplc = :observacionCpplc"),
    @NamedQuery(name = "CppPlanCapacitacion.findByActivoCpplc", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.activoCpplc = :activoCpplc"),
    @NamedQuery(name = "CppPlanCapacitacion.findByUsuarioIngre", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppPlanCapacitacion.findByFechaIngre", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppPlanCapacitacion.findByHoraIngre", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppPlanCapacitacion.findByUsuarioActua", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppPlanCapacitacion.findByFechaActua", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppPlanCapacitacion.findByHoraActua", query = "SELECT c FROM CppPlanCapacitacion c WHERE c.horaActua = :horaActua")})
public class CppPlanCapacitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpplc", nullable = false)
    private Integer ideCpplc;
    @Column(name = "ide_gemes")
    private Integer ideGemes;
    @Size(max = 1000)
    @Column(name = "nombre_capacita_cpplc", length = 1000)
    private String nombreCapacitaCpplc;
    @Size(max = 4000)
    @Column(name = "observacion_cpplc", length = 4000)
    private String observacionCpplc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpplc", nullable = false)
    private boolean activoCpplc;
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
    @OneToMany(mappedBy = "ideCpplc")
    private List<CppCapacitacion> cppCapacitacionList;
    @JoinColumn(name = "ide_cptpc", referencedColumnName = "ide_cptpc")
    @ManyToOne
    private CppTipoPlanCapacita ideCptpc;
    @JoinColumn(name = "ide_cptcc", referencedColumnName = "ide_cptcc")
    @ManyToOne
    private CppTipoCursoCapacita ideCptcc;
    @JoinColumn(name = "ide_cptic", referencedColumnName = "ide_cptic")
    @ManyToOne
    private CppTipoCapacitacion ideCptic;
    @JoinColumn(name = "ide_cpper", referencedColumnName = "ide_cpper")
    @ManyToOne
    private CppPerfil ideCpper;

    public CppPlanCapacitacion() {
    }

    public CppPlanCapacitacion(Integer ideCpplc) {
        this.ideCpplc = ideCpplc;
    }

    public CppPlanCapacitacion(Integer ideCpplc, boolean activoCpplc) {
        this.ideCpplc = ideCpplc;
        this.activoCpplc = activoCpplc;
    }

    public Integer getIdeCpplc() {
        return ideCpplc;
    }

    public void setIdeCpplc(Integer ideCpplc) {
        this.ideCpplc = ideCpplc;
    }

    public Integer getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(Integer ideGemes) {
        this.ideGemes = ideGemes;
    }

    public String getNombreCapacitaCpplc() {
        return nombreCapacitaCpplc;
    }

    public void setNombreCapacitaCpplc(String nombreCapacitaCpplc) {
        this.nombreCapacitaCpplc = nombreCapacitaCpplc;
    }

    public String getObservacionCpplc() {
        return observacionCpplc;
    }

    public void setObservacionCpplc(String observacionCpplc) {
        this.observacionCpplc = observacionCpplc;
    }

    public boolean getActivoCpplc() {
        return activoCpplc;
    }

    public void setActivoCpplc(boolean activoCpplc) {
        this.activoCpplc = activoCpplc;
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

    public List<CppCapacitacion> getCppCapacitacionList() {
        return cppCapacitacionList;
    }

    public void setCppCapacitacionList(List<CppCapacitacion> cppCapacitacionList) {
        this.cppCapacitacionList = cppCapacitacionList;
    }

    public CppTipoPlanCapacita getIdeCptpc() {
        return ideCptpc;
    }

    public void setIdeCptpc(CppTipoPlanCapacita ideCptpc) {
        this.ideCptpc = ideCptpc;
    }

    public CppTipoCursoCapacita getIdeCptcc() {
        return ideCptcc;
    }

    public void setIdeCptcc(CppTipoCursoCapacita ideCptcc) {
        this.ideCptcc = ideCptcc;
    }

    public CppTipoCapacitacion getIdeCptic() {
        return ideCptic;
    }

    public void setIdeCptic(CppTipoCapacitacion ideCptic) {
        this.ideCptic = ideCptic;
    }

    public CppPerfil getIdeCpper() {
        return ideCpper;
    }

    public void setIdeCpper(CppPerfil ideCpper) {
        this.ideCpper = ideCpper;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpplc != null ? ideCpplc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppPlanCapacitacion)) {
            return false;
        }
        CppPlanCapacitacion other = (CppPlanCapacitacion) object;
        if ((this.ideCpplc == null && other.ideCpplc != null) || (this.ideCpplc != null && !this.ideCpplc.equals(other.ideCpplc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppPlanCapacitacion[ ideCpplc=" + ideCpplc + " ]";
    }
    
}
