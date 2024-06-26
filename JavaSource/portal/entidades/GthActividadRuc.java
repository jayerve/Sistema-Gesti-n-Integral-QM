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
@Table(name = "gth_actividad_ruc", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthActividadRuc.findAll", query = "SELECT g FROM GthActividadRuc g"),
    @NamedQuery(name = "GthActividadRuc.findByIdeGtacr", query = "SELECT g FROM GthActividadRuc g WHERE g.ideGtacr = :ideGtacr"),
    @NamedQuery(name = "GthActividadRuc.findByDetalleGtacr", query = "SELECT g FROM GthActividadRuc g WHERE g.detalleGtacr = :detalleGtacr"),
    @NamedQuery(name = "GthActividadRuc.findByActivoGtacr", query = "SELECT g FROM GthActividadRuc g WHERE g.activoGtacr = :activoGtacr"),
    @NamedQuery(name = "GthActividadRuc.findByUsuarioIngre", query = "SELECT g FROM GthActividadRuc g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthActividadRuc.findByFechaIngre", query = "SELECT g FROM GthActividadRuc g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthActividadRuc.findByUsuarioActua", query = "SELECT g FROM GthActividadRuc g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthActividadRuc.findByFechaActua", query = "SELECT g FROM GthActividadRuc g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthActividadRuc.findByHoraIngre", query = "SELECT g FROM GthActividadRuc g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthActividadRuc.findByHoraActua", query = "SELECT g FROM GthActividadRuc g WHERE g.horaActua = :horaActua")})
public class GthActividadRuc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtacr", nullable = false)
    private Integer ideGtacr;
    @Size(max = 50)
    @Column(name = "detalle_gtacr", length = 50)
    private String detalleGtacr;
    @Column(name = "activo_gtacr")
    private Boolean activoGtacr;
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
    @OneToMany(mappedBy = "ideGtacr")
    private List<GthNegocioEmpleado> gthNegocioEmpleadoList;

    public GthActividadRuc() {
    }

    public GthActividadRuc(Integer ideGtacr) {
        this.ideGtacr = ideGtacr;
    }

    public Integer getIdeGtacr() {
        return ideGtacr;
    }

    public void setIdeGtacr(Integer ideGtacr) {
        this.ideGtacr = ideGtacr;
    }

    public String getDetalleGtacr() {
        return detalleGtacr;
    }

    public void setDetalleGtacr(String detalleGtacr) {
        this.detalleGtacr = detalleGtacr;
    }

    public Boolean getActivoGtacr() {
        return activoGtacr;
    }

    public void setActivoGtacr(Boolean activoGtacr) {
        this.activoGtacr = activoGtacr;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtacr != null ? ideGtacr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthActividadRuc)) {
            return false;
        }
        GthActividadRuc other = (GthActividadRuc) object;
        if ((this.ideGtacr == null && other.ideGtacr != null) || (this.ideGtacr != null && !this.ideGtacr.equals(other.ideGtacr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthActividadRuc[ ideGtacr=" + ideGtacr + " ]";
    }
    
}
