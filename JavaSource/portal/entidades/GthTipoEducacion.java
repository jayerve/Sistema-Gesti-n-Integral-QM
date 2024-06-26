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
@Table(name = "gth_tipo_educacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoEducacion.findAll", query = "SELECT g FROM GthTipoEducacion g"),
    @NamedQuery(name = "GthTipoEducacion.findByIdeGtted", query = "SELECT g FROM GthTipoEducacion g WHERE g.ideGtted = :ideGtted"),
    @NamedQuery(name = "GthTipoEducacion.findByDetalleGtted", query = "SELECT g FROM GthTipoEducacion g WHERE g.detalleGtted = :detalleGtted"),
    @NamedQuery(name = "GthTipoEducacion.findByActivoGtted", query = "SELECT g FROM GthTipoEducacion g WHERE g.activoGtted = :activoGtted"),
    @NamedQuery(name = "GthTipoEducacion.findByUsuarioIngre", query = "SELECT g FROM GthTipoEducacion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoEducacion.findByFechaIngre", query = "SELECT g FROM GthTipoEducacion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoEducacion.findByUsuarioActua", query = "SELECT g FROM GthTipoEducacion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoEducacion.findByFechaActua", query = "SELECT g FROM GthTipoEducacion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoEducacion.findByHoraIngre", query = "SELECT g FROM GthTipoEducacion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoEducacion.findByHoraActua", query = "SELECT g FROM GthTipoEducacion g WHERE g.horaActua = :horaActua")})
public class GthTipoEducacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtted", nullable = false)
    private Integer ideGtted;
    @Size(max = 50)
    @Column(name = "detalle_gtted", length = 50)
    private String detalleGtted;
    @Column(name = "activo_gtted")
    private Boolean activoGtted;
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
    @OneToMany(mappedBy = "ideGtted")
    private List<SprEstudios> sprEstudiosList;
    @OneToMany(mappedBy = "ideGtted")
    private List<GthEducacionEmpleado> gthEducacionEmpleadoList;

    public GthTipoEducacion() {
    }

    public GthTipoEducacion(Integer ideGtted) {
        this.ideGtted = ideGtted;
    }

    public Integer getIdeGtted() {
        return ideGtted;
    }

    public void setIdeGtted(Integer ideGtted) {
        this.ideGtted = ideGtted;
    }

    public String getDetalleGtted() {
        return detalleGtted;
    }

    public void setDetalleGtted(String detalleGtted) {
        this.detalleGtted = detalleGtted;
    }

    public Boolean getActivoGtted() {
        return activoGtted;
    }

    public void setActivoGtted(Boolean activoGtted) {
        this.activoGtted = activoGtted;
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
        hash += (ideGtted != null ? ideGtted.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoEducacion)) {
            return false;
        }
        GthTipoEducacion other = (GthTipoEducacion) object;
        if ((this.ideGtted == null && other.ideGtted != null) || (this.ideGtted != null && !this.ideGtted.equals(other.ideGtted))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoEducacion[ ideGtted=" + ideGtted + " ]";
    }
    
}
