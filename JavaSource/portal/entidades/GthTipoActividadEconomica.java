/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "gth_tipo_actividad_economica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoActividadEconomica.findAll", query = "SELECT g FROM GthTipoActividadEconomica g"),
    @NamedQuery(name = "GthTipoActividadEconomica.findByIdeGttae", query = "SELECT g FROM GthTipoActividadEconomica g WHERE g.ideGttae = :ideGttae"),
    @NamedQuery(name = "GthTipoActividadEconomica.findByDetalleGttae", query = "SELECT g FROM GthTipoActividadEconomica g WHERE g.detalleGttae = :detalleGttae"),
    @NamedQuery(name = "GthTipoActividadEconomica.findByActivoGttae", query = "SELECT g FROM GthTipoActividadEconomica g WHERE g.activoGttae = :activoGttae"),
    @NamedQuery(name = "GthTipoActividadEconomica.findByUsuarioIngre", query = "SELECT g FROM GthTipoActividadEconomica g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoActividadEconomica.findByFechaIngre", query = "SELECT g FROM GthTipoActividadEconomica g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoActividadEconomica.findByUsuarioActua", query = "SELECT g FROM GthTipoActividadEconomica g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoActividadEconomica.findByFechaActua", query = "SELECT g FROM GthTipoActividadEconomica g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoActividadEconomica.findByHoraIngre", query = "SELECT g FROM GthTipoActividadEconomica g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoActividadEconomica.findByHoraActua", query = "SELECT g FROM GthTipoActividadEconomica g WHERE g.horaActua = :horaActua")})
public class GthTipoActividadEconomica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttae", nullable = false)
    private Integer ideGttae;
    @Size(max = 50)
    @Column(name = "detalle_gttae", length = 50)
    private String detalleGttae;
    @Column(name = "activo_gttae")
    private Boolean activoGttae;
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
    @OneToMany(mappedBy = "ideGttae")
    private List<GthNegocioEmpleado> gthNegocioEmpleadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttae")
    private List<GenBeneficiario> genBeneficiarioList;

    public GthTipoActividadEconomica() {
    }

    public GthTipoActividadEconomica(Integer ideGttae) {
        this.ideGttae = ideGttae;
    }

    public Integer getIdeGttae() {
        return ideGttae;
    }

    public void setIdeGttae(Integer ideGttae) {
        this.ideGttae = ideGttae;
    }

    public String getDetalleGttae() {
        return detalleGttae;
    }

    public void setDetalleGttae(String detalleGttae) {
        this.detalleGttae = detalleGttae;
    }

    public Boolean getActivoGttae() {
        return activoGttae;
    }

    public void setActivoGttae(Boolean activoGttae) {
        this.activoGttae = activoGttae;
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

    public List<GthNegocioEmpleado> getGthNegocioEmpleadoList() {
        return gthNegocioEmpleadoList;
    }

    public void setGthNegocioEmpleadoList(List<GthNegocioEmpleado> gthNegocioEmpleadoList) {
        this.gthNegocioEmpleadoList = gthNegocioEmpleadoList;
    }

    public List<GenBeneficiario> getGenBeneficiarioList() {
        return genBeneficiarioList;
    }

    public void setGenBeneficiarioList(List<GenBeneficiario> genBeneficiarioList) {
        this.genBeneficiarioList = genBeneficiarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttae != null ? ideGttae.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoActividadEconomica)) {
            return false;
        }
        GthTipoActividadEconomica other = (GthTipoActividadEconomica) object;
        if ((this.ideGttae == null && other.ideGttae != null) || (this.ideGttae != null && !this.ideGttae.equals(other.ideGttae))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoActividadEconomica[ ideGttae=" + ideGttae + " ]";
    }
    
}
