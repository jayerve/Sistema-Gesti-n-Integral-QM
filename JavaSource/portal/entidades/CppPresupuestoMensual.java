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
@Table(name = "cpp_presupuesto_mensual", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppPresupuestoMensual.findAll", query = "SELECT c FROM CppPresupuestoMensual c"),
    @NamedQuery(name = "CppPresupuestoMensual.findByIdeCpprm", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.ideCpprm = :ideCpprm"),
    @NamedQuery(name = "CppPresupuestoMensual.findByIdeGemes", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.ideGemes = :ideGemes"),
    @NamedQuery(name = "CppPresupuestoMensual.findByValorAsignadoCppm", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.valorAsignadoCppm = :valorAsignadoCppm"),
    @NamedQuery(name = "CppPresupuestoMensual.findByValorReformadoCppm", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.valorReformadoCppm = :valorReformadoCppm"),
    @NamedQuery(name = "CppPresupuestoMensual.findByValorEjecutadoCppm", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.valorEjecutadoCppm = :valorEjecutadoCppm"),
    @NamedQuery(name = "CppPresupuestoMensual.findByFechaAsignadoCppm", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.fechaAsignadoCppm = :fechaAsignadoCppm"),
    @NamedQuery(name = "CppPresupuestoMensual.findByFechaReformadoCppm", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.fechaReformadoCppm = :fechaReformadoCppm"),
    @NamedQuery(name = "CppPresupuestoMensual.findByFechaEjecutadoCppm", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.fechaEjecutadoCppm = :fechaEjecutadoCppm"),
    @NamedQuery(name = "CppPresupuestoMensual.findByObservacionCpprm", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.observacionCpprm = :observacionCpprm"),
    @NamedQuery(name = "CppPresupuestoMensual.findByActivoCpprm", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.activoCpprm = :activoCpprm"),
    @NamedQuery(name = "CppPresupuestoMensual.findByUsuarioIngre", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppPresupuestoMensual.findByFechaIngre", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppPresupuestoMensual.findByHoraIngre", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppPresupuestoMensual.findByUsuarioActua", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppPresupuestoMensual.findByFechaActua", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppPresupuestoMensual.findByHoraActua", query = "SELECT c FROM CppPresupuestoMensual c WHERE c.horaActua = :horaActua")})
public class CppPresupuestoMensual implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpprm", nullable = false)
    private Integer ideCpprm;
    @Column(name = "ide_gemes")
    private Integer ideGemes;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_asignado_cppm", precision = 12, scale = 2)
    private BigDecimal valorAsignadoCppm;
    @Column(name = "valor_reformado_cppm", precision = 12, scale = 2)
    private BigDecimal valorReformadoCppm;
    @Column(name = "valor_ejecutado_cppm", precision = 12, scale = 2)
    private BigDecimal valorEjecutadoCppm;
    @Column(name = "fecha_asignado_cppm")
    @Temporal(TemporalType.DATE)
    private Date fechaAsignadoCppm;
    @Column(name = "fecha_reformado_cppm")
    @Temporal(TemporalType.DATE)
    private Date fechaReformadoCppm;
    @Column(name = "fecha_ejecutado_cppm")
    @Temporal(TemporalType.DATE)
    private Date fechaEjecutadoCppm;
    @Size(max = 4000)
    @Column(name = "observacion_cpprm", length = 4000)
    private String observacionCpprm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpprm", nullable = false)
    private boolean activoCpprm;
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
    @OneToMany(mappedBy = "ideCpprm")
    private List<CppCapacitacion> cppCapacitacionList;
    @JoinColumn(name = "ide_cppre", referencedColumnName = "ide_cppre")
    @ManyToOne
    private CppPresupuesto ideCppre;

    public CppPresupuestoMensual() {
    }

    public CppPresupuestoMensual(Integer ideCpprm) {
        this.ideCpprm = ideCpprm;
    }

    public CppPresupuestoMensual(Integer ideCpprm, boolean activoCpprm) {
        this.ideCpprm = ideCpprm;
        this.activoCpprm = activoCpprm;
    }

    public Integer getIdeCpprm() {
        return ideCpprm;
    }

    public void setIdeCpprm(Integer ideCpprm) {
        this.ideCpprm = ideCpprm;
    }

    public Integer getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(Integer ideGemes) {
        this.ideGemes = ideGemes;
    }

    public BigDecimal getValorAsignadoCppm() {
        return valorAsignadoCppm;
    }

    public void setValorAsignadoCppm(BigDecimal valorAsignadoCppm) {
        this.valorAsignadoCppm = valorAsignadoCppm;
    }

    public BigDecimal getValorReformadoCppm() {
        return valorReformadoCppm;
    }

    public void setValorReformadoCppm(BigDecimal valorReformadoCppm) {
        this.valorReformadoCppm = valorReformadoCppm;
    }

    public BigDecimal getValorEjecutadoCppm() {
        return valorEjecutadoCppm;
    }

    public void setValorEjecutadoCppm(BigDecimal valorEjecutadoCppm) {
        this.valorEjecutadoCppm = valorEjecutadoCppm;
    }

    public Date getFechaAsignadoCppm() {
        return fechaAsignadoCppm;
    }

    public void setFechaAsignadoCppm(Date fechaAsignadoCppm) {
        this.fechaAsignadoCppm = fechaAsignadoCppm;
    }

    public Date getFechaReformadoCppm() {
        return fechaReformadoCppm;
    }

    public void setFechaReformadoCppm(Date fechaReformadoCppm) {
        this.fechaReformadoCppm = fechaReformadoCppm;
    }

    public Date getFechaEjecutadoCppm() {
        return fechaEjecutadoCppm;
    }

    public void setFechaEjecutadoCppm(Date fechaEjecutadoCppm) {
        this.fechaEjecutadoCppm = fechaEjecutadoCppm;
    }

    public String getObservacionCpprm() {
        return observacionCpprm;
    }

    public void setObservacionCpprm(String observacionCpprm) {
        this.observacionCpprm = observacionCpprm;
    }

    public boolean getActivoCpprm() {
        return activoCpprm;
    }

    public void setActivoCpprm(boolean activoCpprm) {
        this.activoCpprm = activoCpprm;
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

    public CppPresupuesto getIdeCppre() {
        return ideCppre;
    }

    public void setIdeCppre(CppPresupuesto ideCppre) {
        this.ideCppre = ideCppre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpprm != null ? ideCpprm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppPresupuestoMensual)) {
            return false;
        }
        CppPresupuestoMensual other = (CppPresupuestoMensual) object;
        if ((this.ideCpprm == null && other.ideCpprm != null) || (this.ideCpprm != null && !this.ideCpprm.equals(other.ideCpprm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppPresupuestoMensual[ ideCpprm=" + ideCpprm + " ]";
    }
    
}
