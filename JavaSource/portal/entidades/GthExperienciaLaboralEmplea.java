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
@Table(name = "gth_experiencia_laboral_emplea", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findAll", query = "SELECT g FROM GthExperienciaLaboralEmplea g"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByIdeGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.ideGtele = :ideGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByDetalleCargoGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.detalleCargoGtele = :detalleCargoGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByAreaDesempenioGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.areaDesempenioGtele = :areaDesempenioGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByNroSubordinadosGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.nroSubordinadosGtele = :nroSubordinadosGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByMotivoSalidaGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.motivoSalidaGtele = :motivoSalidaGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByJefeInmediatoGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.jefeInmediatoGtele = :jefeInmediatoGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByCargoJefeGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.cargoJefeGtele = :cargoJefeGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByFuncionesDesempenioGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.funcionesDesempenioGtele = :funcionesDesempenioGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByTelefonoGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.telefonoGtele = :telefonoGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByActivoGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.activoGtele = :activoGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByUsuarioIngre", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByFechaIngre", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByHoraIngre", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByUsuarioActua", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByFechaActua", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByHoraActua", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByFechaIngresoGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.fechaIngresoGtele = :fechaIngresoGtele"),
    @NamedQuery(name = "GthExperienciaLaboralEmplea.findByFechaSalidaGtele", query = "SELECT g FROM GthExperienciaLaboralEmplea g WHERE g.fechaSalidaGtele = :fechaSalidaGtele")})
public class GthExperienciaLaboralEmplea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtele", nullable = false)
    private Integer ideGtele;
    @Size(max = 100)
    @Column(name = "detalle_cargo_gtele", length = 100)
    private String detalleCargoGtele;
    @Size(max = 100)
    @Column(name = "area_desempenio_gtele", length = 100)
    private String areaDesempenioGtele;
    @Column(name = "nro_subordinados_gtele")
    private Integer nroSubordinadosGtele;
    @Size(max = 2000)
    @Column(name = "motivo_salida_gtele", length = 2000)
    private String motivoSalidaGtele;
    @Size(max = 100)
    @Column(name = "jefe_inmediato_gtele", length = 100)
    private String jefeInmediatoGtele;
    @Size(max = 100)
    @Column(name = "cargo_jefe_gtele", length = 100)
    private String cargoJefeGtele;
    @Size(max = 4000)
    @Column(name = "funciones_desempenio_gtele", length = 4000)
    private String funcionesDesempenioGtele;
    @Size(max = 20)
    @Column(name = "telefono_gtele", length = 20)
    private String telefonoGtele;
    @Column(name = "activo_gtele")
    private Boolean activoGtele;
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
    @Column(name = "fecha_ingreso_gtele")
    @Temporal(TemporalType.TIME)
    private Date fechaIngresoGtele;
    @Column(name = "fecha_salida_gtele")
    @Temporal(TemporalType.TIME)
    private Date fechaSalidaGtele;
    @OneToMany(mappedBy = "ideGtele")
    private List<GthDireccion> gthDireccionList;
    @OneToMany(mappedBy = "ideGtele")
    private List<GthTelefono> gthTelefonoList;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;

    public GthExperienciaLaboralEmplea() {
    }

    public GthExperienciaLaboralEmplea(Integer ideGtele) {
        this.ideGtele = ideGtele;
    }

    public Integer getIdeGtele() {
        return ideGtele;
    }

    public void setIdeGtele(Integer ideGtele) {
        this.ideGtele = ideGtele;
    }

    public String getDetalleCargoGtele() {
        return detalleCargoGtele;
    }

    public void setDetalleCargoGtele(String detalleCargoGtele) {
        this.detalleCargoGtele = detalleCargoGtele;
    }

    public String getAreaDesempenioGtele() {
        return areaDesempenioGtele;
    }

    public void setAreaDesempenioGtele(String areaDesempenioGtele) {
        this.areaDesempenioGtele = areaDesempenioGtele;
    }

    public Integer getNroSubordinadosGtele() {
        return nroSubordinadosGtele;
    }

    public void setNroSubordinadosGtele(Integer nroSubordinadosGtele) {
        this.nroSubordinadosGtele = nroSubordinadosGtele;
    }

    public String getMotivoSalidaGtele() {
        return motivoSalidaGtele;
    }

    public void setMotivoSalidaGtele(String motivoSalidaGtele) {
        this.motivoSalidaGtele = motivoSalidaGtele;
    }

    public String getJefeInmediatoGtele() {
        return jefeInmediatoGtele;
    }

    public void setJefeInmediatoGtele(String jefeInmediatoGtele) {
        this.jefeInmediatoGtele = jefeInmediatoGtele;
    }

    public String getCargoJefeGtele() {
        return cargoJefeGtele;
    }

    public void setCargoJefeGtele(String cargoJefeGtele) {
        this.cargoJefeGtele = cargoJefeGtele;
    }

    public String getFuncionesDesempenioGtele() {
        return funcionesDesempenioGtele;
    }

    public void setFuncionesDesempenioGtele(String funcionesDesempenioGtele) {
        this.funcionesDesempenioGtele = funcionesDesempenioGtele;
    }

    public String getTelefonoGtele() {
        return telefonoGtele;
    }

    public void setTelefonoGtele(String telefonoGtele) {
        this.telefonoGtele = telefonoGtele;
    }

    public Boolean getActivoGtele() {
        return activoGtele;
    }

    public void setActivoGtele(Boolean activoGtele) {
        this.activoGtele = activoGtele;
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

    public Date getFechaIngresoGtele() {
        return fechaIngresoGtele;
    }

    public void setFechaIngresoGtele(Date fechaIngresoGtele) {
        this.fechaIngresoGtele = fechaIngresoGtele;
    }

    public Date getFechaSalidaGtele() {
        return fechaSalidaGtele;
    }

    public void setFechaSalidaGtele(Date fechaSalidaGtele) {
        this.fechaSalidaGtele = fechaSalidaGtele;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtele != null ? ideGtele.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthExperienciaLaboralEmplea)) {
            return false;
        }
        GthExperienciaLaboralEmplea other = (GthExperienciaLaboralEmplea) object;
        if ((this.ideGtele == null && other.ideGtele != null) || (this.ideGtele != null && !this.ideGtele.equals(other.ideGtele))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthExperienciaLaboralEmplea[ ideGtele=" + ideGtele + " ]";
    }
    
}
