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
@Table(name = "cpp_presupuesto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppPresupuesto.findAll", query = "SELECT c FROM CppPresupuesto c"),
    @NamedQuery(name = "CppPresupuesto.findByIdeCppre", query = "SELECT c FROM CppPresupuesto c WHERE c.ideCppre = :ideCppre"),
    @NamedQuery(name = "CppPresupuesto.findByIdeGeare", query = "SELECT c FROM CppPresupuesto c WHERE c.ideGeare = :ideGeare"),
    @NamedQuery(name = "CppPresupuesto.findByPresupuestoInicialCppre", query = "SELECT c FROM CppPresupuesto c WHERE c.presupuestoInicialCppre = :presupuestoInicialCppre"),
    @NamedQuery(name = "CppPresupuesto.findByFechaAsignacionCppre", query = "SELECT c FROM CppPresupuesto c WHERE c.fechaAsignacionCppre = :fechaAsignacionCppre"),
    @NamedQuery(name = "CppPresupuesto.findByObservacionCppre", query = "SELECT c FROM CppPresupuesto c WHERE c.observacionCppre = :observacionCppre"),
    @NamedQuery(name = "CppPresupuesto.findByActivoCppre", query = "SELECT c FROM CppPresupuesto c WHERE c.activoCppre = :activoCppre"),
    @NamedQuery(name = "CppPresupuesto.findByUsuarioIngre", query = "SELECT c FROM CppPresupuesto c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppPresupuesto.findByFechaIngre", query = "SELECT c FROM CppPresupuesto c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppPresupuesto.findByHoraIngre", query = "SELECT c FROM CppPresupuesto c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppPresupuesto.findByUsuarioActua", query = "SELECT c FROM CppPresupuesto c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppPresupuesto.findByFechaActua", query = "SELECT c FROM CppPresupuesto c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppPresupuesto.findByHoraActua", query = "SELECT c FROM CppPresupuesto c WHERE c.horaActua = :horaActua")})
public class CppPresupuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cppre", nullable = false)
    private Integer ideCppre;
    @Column(name = "ide_geare")
    private Integer ideGeare;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "presupuesto_inicial_cppre", precision = 12, scale = 2)
    private BigDecimal presupuestoInicialCppre;
    @Column(name = "fecha_asignacion_cppre")
    @Temporal(TemporalType.DATE)
    private Date fechaAsignacionCppre;
    @Size(max = 4000)
    @Column(name = "observacion_cppre", length = 4000)
    private String observacionCppre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cppre", nullable = false)
    private boolean activoCppre;
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
    @OneToMany(mappedBy = "ideCppre")
    private List<CppPerfil> cppPerfilList;
    @OneToMany(mappedBy = "ideCppre")
    private List<CppPresupuestoMensual> cppPresupuestoMensualList;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;

    public CppPresupuesto() {
    }

    public CppPresupuesto(Integer ideCppre) {
        this.ideCppre = ideCppre;
    }

    public CppPresupuesto(Integer ideCppre, boolean activoCppre) {
        this.ideCppre = ideCppre;
        this.activoCppre = activoCppre;
    }

    public Integer getIdeCppre() {
        return ideCppre;
    }

    public void setIdeCppre(Integer ideCppre) {
        this.ideCppre = ideCppre;
    }

    public Integer getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(Integer ideGeare) {
        this.ideGeare = ideGeare;
    }

    public BigDecimal getPresupuestoInicialCppre() {
        return presupuestoInicialCppre;
    }

    public void setPresupuestoInicialCppre(BigDecimal presupuestoInicialCppre) {
        this.presupuestoInicialCppre = presupuestoInicialCppre;
    }

    public Date getFechaAsignacionCppre() {
        return fechaAsignacionCppre;
    }

    public void setFechaAsignacionCppre(Date fechaAsignacionCppre) {
        this.fechaAsignacionCppre = fechaAsignacionCppre;
    }

    public String getObservacionCppre() {
        return observacionCppre;
    }

    public void setObservacionCppre(String observacionCppre) {
        this.observacionCppre = observacionCppre;
    }

    public boolean getActivoCppre() {
        return activoCppre;
    }

    public void setActivoCppre(boolean activoCppre) {
        this.activoCppre = activoCppre;
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

    public List<CppPerfil> getCppPerfilList() {
        return cppPerfilList;
    }

    public void setCppPerfilList(List<CppPerfil> cppPerfilList) {
        this.cppPerfilList = cppPerfilList;
    }

    public List<CppPresupuestoMensual> getCppPresupuestoMensualList() {
        return cppPresupuestoMensualList;
    }

    public void setCppPresupuestoMensualList(List<CppPresupuestoMensual> cppPresupuestoMensualList) {
        this.cppPresupuestoMensualList = cppPresupuestoMensualList;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCppre != null ? ideCppre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppPresupuesto)) {
            return false;
        }
        CppPresupuesto other = (CppPresupuesto) object;
        if ((this.ideCppre == null && other.ideCppre != null) || (this.ideCppre != null && !this.ideCppre.equals(other.ideCppre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppPresupuesto[ ideCppre=" + ideCppre + " ]";
    }
    
}
