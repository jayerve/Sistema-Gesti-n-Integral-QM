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
@Table(name = "asi_estado_vacacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiEstadoVacacion.findAll", query = "SELECT a FROM AsiEstadoVacacion a"),
    @NamedQuery(name = "AsiEstadoVacacion.findByIdeAsesv", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.ideAsesv = :ideAsesv"),
    @NamedQuery(name = "AsiEstadoVacacion.findByDetalleAsesv", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.detalleAsesv = :detalleAsesv"),
    @NamedQuery(name = "AsiEstadoVacacion.findByActivoAsesv", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.activoAsesv = :activoAsesv"),
    @NamedQuery(name = "AsiEstadoVacacion.findByUsuarioIngre", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiEstadoVacacion.findByFechaIngre", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiEstadoVacacion.findByUsuarioActua", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiEstadoVacacion.findByFechaActua", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiEstadoVacacion.findByHoraActua", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.horaActua = :horaActua"),
    @NamedQuery(name = "AsiEstadoVacacion.findByHoraIngre", query = "SELECT a FROM AsiEstadoVacacion a WHERE a.horaIngre = :horaIngre")})
public class AsiEstadoVacacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asesv", nullable = false)
    private Integer ideAsesv;
    @Size(max = 50)
    @Column(name = "detalle_asesv", length = 50)
    private String detalleAsesv;
    @Column(name = "activo_asesv")
    private Boolean activoAsesv;
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
    @OneToMany(mappedBy = "ideAsesv")
    private List<AsiDetalleVacacion> asiDetalleVacacionList;

    public AsiEstadoVacacion() {
    }

    public AsiEstadoVacacion(Integer ideAsesv) {
        this.ideAsesv = ideAsesv;
    }

    public Integer getIdeAsesv() {
        return ideAsesv;
    }

    public void setIdeAsesv(Integer ideAsesv) {
        this.ideAsesv = ideAsesv;
    }

    public String getDetalleAsesv() {
        return detalleAsesv;
    }

    public void setDetalleAsesv(String detalleAsesv) {
        this.detalleAsesv = detalleAsesv;
    }

    public Boolean getActivoAsesv() {
        return activoAsesv;
    }

    public void setActivoAsesv(Boolean activoAsesv) {
        this.activoAsesv = activoAsesv;
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

    public List<AsiDetalleVacacion> getAsiDetalleVacacionList() {
        return asiDetalleVacacionList;
    }

    public void setAsiDetalleVacacionList(List<AsiDetalleVacacion> asiDetalleVacacionList) {
        this.asiDetalleVacacionList = asiDetalleVacacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsesv != null ? ideAsesv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiEstadoVacacion)) {
            return false;
        }
        AsiEstadoVacacion other = (AsiEstadoVacacion) object;
        if ((this.ideAsesv == null && other.ideAsesv != null) || (this.ideAsesv != null && !this.ideAsesv.equals(other.ideAsesv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiEstadoVacacion[ ideAsesv=" + ideAsesv + " ]";
    }
    
}
