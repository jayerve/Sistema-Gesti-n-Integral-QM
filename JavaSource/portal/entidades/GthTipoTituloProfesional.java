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
@Table(name = "gth_tipo_titulo_profesional", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoTituloProfesional.findAll", query = "SELECT g FROM GthTipoTituloProfesional g"),
    @NamedQuery(name = "GthTipoTituloProfesional.findByIdeGtttp", query = "SELECT g FROM GthTipoTituloProfesional g WHERE g.ideGtttp = :ideGtttp"),
    @NamedQuery(name = "GthTipoTituloProfesional.findByDetalleGtttp", query = "SELECT g FROM GthTipoTituloProfesional g WHERE g.detalleGtttp = :detalleGtttp"),
    @NamedQuery(name = "GthTipoTituloProfesional.findByActivoGtttp", query = "SELECT g FROM GthTipoTituloProfesional g WHERE g.activoGtttp = :activoGtttp"),
    @NamedQuery(name = "GthTipoTituloProfesional.findByUsuarioIngre", query = "SELECT g FROM GthTipoTituloProfesional g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoTituloProfesional.findByFechaIngre", query = "SELECT g FROM GthTipoTituloProfesional g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoTituloProfesional.findByUsuarioActua", query = "SELECT g FROM GthTipoTituloProfesional g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoTituloProfesional.findByFechaActua", query = "SELECT g FROM GthTipoTituloProfesional g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoTituloProfesional.findByHoraIngre", query = "SELECT g FROM GthTipoTituloProfesional g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoTituloProfesional.findByHoraActua", query = "SELECT g FROM GthTipoTituloProfesional g WHERE g.horaActua = :horaActua")})
public class GthTipoTituloProfesional implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtttp", nullable = false)
    private Integer ideGtttp;
    @Size(max = 50)
    @Column(name = "detalle_gtttp", length = 50)
    private String detalleGtttp;
    @Column(name = "activo_gtttp")
    private Boolean activoGtttp;
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
    @OneToMany(mappedBy = "ideGtttp")
    private List<GthEducacionEmpleado> gthEducacionEmpleadoList;

    public GthTipoTituloProfesional() {
    }

    public GthTipoTituloProfesional(Integer ideGtttp) {
        this.ideGtttp = ideGtttp;
    }

    public Integer getIdeGtttp() {
        return ideGtttp;
    }

    public void setIdeGtttp(Integer ideGtttp) {
        this.ideGtttp = ideGtttp;
    }

    public String getDetalleGtttp() {
        return detalleGtttp;
    }

    public void setDetalleGtttp(String detalleGtttp) {
        this.detalleGtttp = detalleGtttp;
    }

    public Boolean getActivoGtttp() {
        return activoGtttp;
    }

    public void setActivoGtttp(Boolean activoGtttp) {
        this.activoGtttp = activoGtttp;
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

    public List<GthEducacionEmpleado> getGthEducacionEmpleadoList() {
        return gthEducacionEmpleadoList;
    }

    public void setGthEducacionEmpleadoList(List<GthEducacionEmpleado> gthEducacionEmpleadoList) {
        this.gthEducacionEmpleadoList = gthEducacionEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtttp != null ? ideGtttp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoTituloProfesional)) {
            return false;
        }
        GthTipoTituloProfesional other = (GthTipoTituloProfesional) object;
        if ((this.ideGtttp == null && other.ideGtttp != null) || (this.ideGtttp != null && !this.ideGtttp.equals(other.ideGtttp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoTituloProfesional[ ideGtttp=" + ideGtttp + " ]";
    }
    
}
