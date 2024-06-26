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
@Table(name = "gth_tipo_endeudamiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoEndeudamiento.findAll", query = "SELECT g FROM GthTipoEndeudamiento g"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByIdeGtten", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.ideGtten = :ideGtten"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByDetalleGtten", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.detalleGtten = :detalleGtten"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByActivoGtten", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.activoGtten = :activoGtten"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByUsuarioIngre", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByFechaIngre", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByUsuarioActua", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByFechaActua", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByHoraIngre", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoEndeudamiento.findByHoraActua", query = "SELECT g FROM GthTipoEndeudamiento g WHERE g.horaActua = :horaActua")})
public class GthTipoEndeudamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtten", nullable = false)
    private Integer ideGtten;
    @Size(max = 50)
    @Column(name = "detalle_gtten", length = 50)
    private String detalleGtten;
    @Column(name = "activo_gtten")
    private Boolean activoGtten;
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
    @OneToMany(mappedBy = "ideGtten")
    private List<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoList;

    public GthTipoEndeudamiento() {
    }

    public GthTipoEndeudamiento(Integer ideGtten) {
        this.ideGtten = ideGtten;
    }

    public Integer getIdeGtten() {
        return ideGtten;
    }

    public void setIdeGtten(Integer ideGtten) {
        this.ideGtten = ideGtten;
    }

    public String getDetalleGtten() {
        return detalleGtten;
    }

    public void setDetalleGtten(String detalleGtten) {
        this.detalleGtten = detalleGtten;
    }

    public Boolean getActivoGtten() {
        return activoGtten;
    }

    public void setActivoGtten(Boolean activoGtten) {
        this.activoGtten = activoGtten;
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

    public List<GthEndeudamientoEmpleado> getGthEndeudamientoEmpleadoList() {
        return gthEndeudamientoEmpleadoList;
    }

    public void setGthEndeudamientoEmpleadoList(List<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoList) {
        this.gthEndeudamientoEmpleadoList = gthEndeudamientoEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtten != null ? ideGtten.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoEndeudamiento)) {
            return false;
        }
        GthTipoEndeudamiento other = (GthTipoEndeudamiento) object;
        if ((this.ideGtten == null && other.ideGtten != null) || (this.ideGtten != null && !this.ideGtten.equals(other.ideGtten))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoEndeudamiento[ ideGtten=" + ideGtten + " ]";
    }
    
}
