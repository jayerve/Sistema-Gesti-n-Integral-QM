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
@Table(name = "gth_negocio_empleado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthNegocioEmpleado.findAll", query = "SELECT g FROM GthNegocioEmpleado g"),
    @NamedQuery(name = "GthNegocioEmpleado.findByIdeGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.ideGtnee = :ideGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByPropioGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.propioGtnee = :propioGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByNombreComercialGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.nombreComercialGtnee = :nombreComercialGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByRucGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.rucGtnee = :rucGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByFechaVigenciaRucGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.fechaVigenciaRucGtnee = :fechaVigenciaRucGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByTotalVentaGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.totalVentaGtnee = :totalVentaGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByTotalGastoGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.totalGastoGtnee = :totalGastoGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByTotalUtilidadGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.totalUtilidadGtnee = :totalUtilidadGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findBySocioGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.socioGtnee = :socioGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByActivoGtnee", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.activoGtnee = :activoGtnee"),
    @NamedQuery(name = "GthNegocioEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthNegocioEmpleado.findByFechaIngre", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthNegocioEmpleado.findByUsuarioActua", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthNegocioEmpleado.findByFechaActua", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthNegocioEmpleado.findByHoraIngre", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthNegocioEmpleado.findByHoraActua", query = "SELECT g FROM GthNegocioEmpleado g WHERE g.horaActua = :horaActua")})
public class GthNegocioEmpleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtnee", nullable = false)
    private Integer ideGtnee;
    @Column(name = "propio_gtnee")
    private Integer propioGtnee;
    @Size(max = 100)
    @Column(name = "nombre_comercial_gtnee", length = 100)
    private String nombreComercialGtnee;
    @Size(max = 13)
    @Column(name = "ruc_gtnee", length = 13)
    private String rucGtnee;
    @Column(name = "fecha_vigencia_ruc_gtnee")
    @Temporal(TemporalType.DATE)
    private Date fechaVigenciaRucGtnee;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_venta_gtnee", precision = 12, scale = 3)
    private BigDecimal totalVentaGtnee;
    @Column(name = "total_gasto_gtnee", precision = 12, scale = 3)
    private BigDecimal totalGastoGtnee;
    @Column(name = "total_utilidad_gtnee", precision = 12, scale = 3)
    private BigDecimal totalUtilidadGtnee;
    @Column(name = "socio_gtnee")
    private Integer socioGtnee;
    @Column(name = "activo_gtnee")
    private Boolean activoGtnee;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideGtnee")
    private List<GthDireccion> gthDireccionList;
    @OneToMany(mappedBy = "ideGtnee")
    private List<GthTelefono> gthTelefonoList;
    @OneToMany(mappedBy = "ideGtnee")
    private List<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaList;
    @OneToMany(mappedBy = "ideGtnee")
    private List<GthArchivoEmpleado> gthArchivoEmpleadoList;
    @JoinColumn(name = "ide_gttae", referencedColumnName = "ide_gttae")
    @ManyToOne
    private GthTipoActividadEconomica ideGttae;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_gtacr", referencedColumnName = "ide_gtacr")
    @ManyToOne
    private GthActividadRuc ideGtacr;

    public GthNegocioEmpleado() {
    }

    public GthNegocioEmpleado(Integer ideGtnee) {
        this.ideGtnee = ideGtnee;
    }

    public Integer getIdeGtnee() {
        return ideGtnee;
    }

    public void setIdeGtnee(Integer ideGtnee) {
        this.ideGtnee = ideGtnee;
    }

    public Integer getPropioGtnee() {
        return propioGtnee;
    }

    public void setPropioGtnee(Integer propioGtnee) {
        this.propioGtnee = propioGtnee;
    }

    public String getNombreComercialGtnee() {
        return nombreComercialGtnee;
    }

    public void setNombreComercialGtnee(String nombreComercialGtnee) {
        this.nombreComercialGtnee = nombreComercialGtnee;
    }

    public String getRucGtnee() {
        return rucGtnee;
    }

    public void setRucGtnee(String rucGtnee) {
        this.rucGtnee = rucGtnee;
    }

    public Date getFechaVigenciaRucGtnee() {
        return fechaVigenciaRucGtnee;
    }

    public void setFechaVigenciaRucGtnee(Date fechaVigenciaRucGtnee) {
        this.fechaVigenciaRucGtnee = fechaVigenciaRucGtnee;
    }

    public BigDecimal getTotalVentaGtnee() {
        return totalVentaGtnee;
    }

    public void setTotalVentaGtnee(BigDecimal totalVentaGtnee) {
        this.totalVentaGtnee = totalVentaGtnee;
    }

    public BigDecimal getTotalGastoGtnee() {
        return totalGastoGtnee;
    }

    public void setTotalGastoGtnee(BigDecimal totalGastoGtnee) {
        this.totalGastoGtnee = totalGastoGtnee;
    }

    public BigDecimal getTotalUtilidadGtnee() {
        return totalUtilidadGtnee;
    }

    public void setTotalUtilidadGtnee(BigDecimal totalUtilidadGtnee) {
        this.totalUtilidadGtnee = totalUtilidadGtnee;
    }

    public Integer getSocioGtnee() {
        return socioGtnee;
    }

    public void setSocioGtnee(Integer socioGtnee) {
        this.socioGtnee = socioGtnee;
    }

    public Boolean getActivoGtnee() {
        return activoGtnee;
    }

    public void setActivoGtnee(Boolean activoGtnee) {
        this.activoGtnee = activoGtnee;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public List<GthDireccion> getGthDireccionList() {
        return gthDireccionList;
    }

    public void setGthDireccionList(List<GthDireccion> gthDireccionList) {
        this.gthDireccionList = gthDireccionList;
    }

    public List<GthTelefono> getGthTelefonoList() {
        return gthTelefonoList;
    }

    public void setGthTelefonoList(List<GthTelefono> gthTelefonoList) {
        this.gthTelefonoList = gthTelefonoList;
    }

    public List<GthParticipaNegocioEmplea> getGthParticipaNegocioEmpleaList() {
        return gthParticipaNegocioEmpleaList;
    }

    public void setGthParticipaNegocioEmpleaList(List<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaList) {
        this.gthParticipaNegocioEmpleaList = gthParticipaNegocioEmpleaList;
    }

    public List<GthArchivoEmpleado> getGthArchivoEmpleadoList() {
        return gthArchivoEmpleadoList;
    }

    public void setGthArchivoEmpleadoList(List<GthArchivoEmpleado> gthArchivoEmpleadoList) {
        this.gthArchivoEmpleadoList = gthArchivoEmpleadoList;
    }

    public GthTipoActividadEconomica getIdeGttae() {
        return ideGttae;
    }

    public void setIdeGttae(GthTipoActividadEconomica ideGttae) {
        this.ideGttae = ideGttae;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GthActividadRuc getIdeGtacr() {
        return ideGtacr;
    }

    public void setIdeGtacr(GthActividadRuc ideGtacr) {
        this.ideGtacr = ideGtacr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtnee != null ? ideGtnee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthNegocioEmpleado)) {
            return false;
        }
        GthNegocioEmpleado other = (GthNegocioEmpleado) object;
        if ((this.ideGtnee == null && other.ideGtnee != null) || (this.ideGtnee != null && !this.ideGtnee.equals(other.ideGtnee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthNegocioEmpleado[ ideGtnee=" + ideGtnee + " ]";
    }
    
}
