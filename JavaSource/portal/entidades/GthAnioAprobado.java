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
@Table(name = "gth_anio_aprobado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthAnioAprobado.findAll", query = "SELECT g FROM GthAnioAprobado g"),
    @NamedQuery(name = "GthAnioAprobado.findByIdeGtana", query = "SELECT g FROM GthAnioAprobado g WHERE g.ideGtana = :ideGtana"),
    @NamedQuery(name = "GthAnioAprobado.findByDetalleGtana", query = "SELECT g FROM GthAnioAprobado g WHERE g.detalleGtana = :detalleGtana"),
    @NamedQuery(name = "GthAnioAprobado.findByActivoGtana", query = "SELECT g FROM GthAnioAprobado g WHERE g.activoGtana = :activoGtana"),
    @NamedQuery(name = "GthAnioAprobado.findByUsuarioIngre", query = "SELECT g FROM GthAnioAprobado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthAnioAprobado.findByFechaIngre", query = "SELECT g FROM GthAnioAprobado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthAnioAprobado.findByUsuarioActua", query = "SELECT g FROM GthAnioAprobado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthAnioAprobado.findByFechaActua", query = "SELECT g FROM GthAnioAprobado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthAnioAprobado.findByHoraActua", query = "SELECT g FROM GthAnioAprobado g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthAnioAprobado.findByHoraIngre", query = "SELECT g FROM GthAnioAprobado g WHERE g.horaIngre = :horaIngre")})
public class GthAnioAprobado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtana", nullable = false)
    private Integer ideGtana;
    @Size(max = 50)
    @Column(name = "detalle_gtana", length = 50)
    private String detalleGtana;
    @Column(name = "activo_gtana")
    private Boolean activoGtana;
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
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @OneToMany(mappedBy = "ideGtana")
    private List<GthEducacionEmpleado> gthEducacionEmpleadoList;

    public GthAnioAprobado() {
    }

    public GthAnioAprobado(Integer ideGtana) {
        this.ideGtana = ideGtana;
    }

    public Integer getIdeGtana() {
        return ideGtana;
    }

    public void setIdeGtana(Integer ideGtana) {
        this.ideGtana = ideGtana;
    }

    public String getDetalleGtana() {
        return detalleGtana;
    }

    public void setDetalleGtana(String detalleGtana) {
        this.detalleGtana = detalleGtana;
    }

    public Boolean getActivoGtana() {
        return activoGtana;
    }

    public void setActivoGtana(Boolean activoGtana) {
        this.activoGtana = activoGtana;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public List<GthEducacionEmpleado> getGthEducacionEmpleadoList() {
        return gthEducacionEmpleadoList;
    }

    public void setGthEducacionEmpleadoList(List<GthEducacionEmpleado> gthEducacionEmpleadoList) {
        this.gthEducacionEmpleadoList = gthEducacionEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtana != null ? ideGtana.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthAnioAprobado)) {
            return false;
        }
        GthAnioAprobado other = (GthAnioAprobado) object;
        if ((this.ideGtana == null && other.ideGtana != null) || (this.ideGtana != null && !this.ideGtana.equals(other.ideGtana))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthAnioAprobado[ ideGtana=" + ideGtana + " ]";
    }
    
}
