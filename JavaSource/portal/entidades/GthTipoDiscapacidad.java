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
@Table(name = "gth_tipo_discapacidad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoDiscapacidad.findAll", query = "SELECT g FROM GthTipoDiscapacidad g"),
    @NamedQuery(name = "GthTipoDiscapacidad.findByIdeGttds", query = "SELECT g FROM GthTipoDiscapacidad g WHERE g.ideGttds = :ideGttds"),
    @NamedQuery(name = "GthTipoDiscapacidad.findByDetalleGttds", query = "SELECT g FROM GthTipoDiscapacidad g WHERE g.detalleGttds = :detalleGttds"),
    @NamedQuery(name = "GthTipoDiscapacidad.findByActivoGttds", query = "SELECT g FROM GthTipoDiscapacidad g WHERE g.activoGttds = :activoGttds"),
    @NamedQuery(name = "GthTipoDiscapacidad.findByUsuarioIngre", query = "SELECT g FROM GthTipoDiscapacidad g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoDiscapacidad.findByFechaIngre", query = "SELECT g FROM GthTipoDiscapacidad g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoDiscapacidad.findByUsuarioActua", query = "SELECT g FROM GthTipoDiscapacidad g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoDiscapacidad.findByFechaActua", query = "SELECT g FROM GthTipoDiscapacidad g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoDiscapacidad.findByHoraIngre", query = "SELECT g FROM GthTipoDiscapacidad g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoDiscapacidad.findByHoraActua", query = "SELECT g FROM GthTipoDiscapacidad g WHERE g.horaActua = :horaActua")})
public class GthTipoDiscapacidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttds", nullable = false)
    private Integer ideGttds;
    @Size(max = 50)
    @Column(name = "detalle_gttds", length = 50)
    private String detalleGttds;
    @Column(name = "activo_gttds")
    private Boolean activoGttds;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttds")
    private List<GthDiscapacidadEmpleado> gthDiscapacidadEmpleadoList;

    public GthTipoDiscapacidad() {
    }

    public GthTipoDiscapacidad(Integer ideGttds) {
        this.ideGttds = ideGttds;
    }

    public Integer getIdeGttds() {
        return ideGttds;
    }

    public void setIdeGttds(Integer ideGttds) {
        this.ideGttds = ideGttds;
    }

    public String getDetalleGttds() {
        return detalleGttds;
    }

    public void setDetalleGttds(String detalleGttds) {
        this.detalleGttds = detalleGttds;
    }

    public Boolean getActivoGttds() {
        return activoGttds;
    }

    public void setActivoGttds(Boolean activoGttds) {
        this.activoGttds = activoGttds;
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

    public List<GthDiscapacidadEmpleado> getGthDiscapacidadEmpleadoList() {
        return gthDiscapacidadEmpleadoList;
    }

    public void setGthDiscapacidadEmpleadoList(List<GthDiscapacidadEmpleado> gthDiscapacidadEmpleadoList) {
        this.gthDiscapacidadEmpleadoList = gthDiscapacidadEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttds != null ? ideGttds.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoDiscapacidad)) {
            return false;
        }
        GthTipoDiscapacidad other = (GthTipoDiscapacidad) object;
        if ((this.ideGttds == null && other.ideGttds != null) || (this.ideGttds != null && !this.ideGttds.equals(other.ideGttds))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoDiscapacidad[ ideGttds=" + ideGttds + " ]";
    }
    
}
