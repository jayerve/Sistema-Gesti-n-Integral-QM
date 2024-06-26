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
@Table(name = "gth_tipo_especialidad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoEspecialidad.findAll", query = "SELECT g FROM GthTipoEspecialidad g"),
    @NamedQuery(name = "GthTipoEspecialidad.findByIdeGttes", query = "SELECT g FROM GthTipoEspecialidad g WHERE g.ideGttes = :ideGttes"),
    @NamedQuery(name = "GthTipoEspecialidad.findByDetalleGttes", query = "SELECT g FROM GthTipoEspecialidad g WHERE g.detalleGttes = :detalleGttes"),
    @NamedQuery(name = "GthTipoEspecialidad.findByActivoGttes", query = "SELECT g FROM GthTipoEspecialidad g WHERE g.activoGttes = :activoGttes"),
    @NamedQuery(name = "GthTipoEspecialidad.findByUsuarioIngre", query = "SELECT g FROM GthTipoEspecialidad g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoEspecialidad.findByFechaIngre", query = "SELECT g FROM GthTipoEspecialidad g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoEspecialidad.findByUsuarioActua", query = "SELECT g FROM GthTipoEspecialidad g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoEspecialidad.findByFechaActua", query = "SELECT g FROM GthTipoEspecialidad g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoEspecialidad.findByHoraIngre", query = "SELECT g FROM GthTipoEspecialidad g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoEspecialidad.findByHoraActua", query = "SELECT g FROM GthTipoEspecialidad g WHERE g.horaActua = :horaActua")})
public class GthTipoEspecialidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttes", nullable = false)
    private Integer ideGttes;
    @Size(max = 50)
    @Column(name = "detalle_gttes", length = 50)
    private String detalleGttes;
    @Column(name = "activo_gttes")
    private Boolean activoGttes;
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
    @OneToMany(mappedBy = "ideGttes")
    private List<SprEstudios> sprEstudiosList;
    @OneToMany(mappedBy = "ideGttes")
    private List<GthEducacionEmpleado> gthEducacionEmpleadoList;

    public GthTipoEspecialidad() {
    }

    public GthTipoEspecialidad(Integer ideGttes) {
        this.ideGttes = ideGttes;
    }

    public Integer getIdeGttes() {
        return ideGttes;
    }

    public void setIdeGttes(Integer ideGttes) {
        this.ideGttes = ideGttes;
    }

    public String getDetalleGttes() {
        return detalleGttes;
    }

    public void setDetalleGttes(String detalleGttes) {
        this.detalleGttes = detalleGttes;
    }

    public Boolean getActivoGttes() {
        return activoGttes;
    }

    public void setActivoGttes(Boolean activoGttes) {
        this.activoGttes = activoGttes;
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

    public List<SprEstudios> getSprEstudiosList() {
        return sprEstudiosList;
    }

    public void setSprEstudiosList(List<SprEstudios> sprEstudiosList) {
        this.sprEstudiosList = sprEstudiosList;
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
        hash += (ideGttes != null ? ideGttes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoEspecialidad)) {
            return false;
        }
        GthTipoEspecialidad other = (GthTipoEspecialidad) object;
        if ((this.ideGttes == null && other.ideGttes != null) || (this.ideGttes != null && !this.ideGttes.equals(other.ideGttes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoEspecialidad[ ideGttes=" + ideGttes + " ]";
    }
    
}
