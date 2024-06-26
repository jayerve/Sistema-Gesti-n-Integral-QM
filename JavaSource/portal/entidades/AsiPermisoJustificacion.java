/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "asi_permiso_justificacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiPermisoJustificacion.findAll", query = "SELECT a FROM AsiPermisoJustificacion a"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByIdeAspej", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.ideAspej = :ideAspej"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByDetalleAspej", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.detalleAspej = :detalleAspej"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByFechaAspej", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.fechaAspej = :fechaAspej"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByArchivoAspej", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.archivoAspej = :archivoAspej"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByActivoAspej", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.activoAspej = :activoAspej"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByUsuarioIngre", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByFechaIngre", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByUsuarioActua", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByFechaActua", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByHoraIngre", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiPermisoJustificacion.findByHoraActua", query = "SELECT a FROM AsiPermisoJustificacion a WHERE a.horaActua = :horaActua")})
public class AsiPermisoJustificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_aspej", nullable = false)
    private Integer ideAspej;
    @Size(max = 4000)
    @Column(name = "detalle_aspej", length = 4000)
    private String detalleAspej;
    @Column(name = "fecha_aspej")
    @Temporal(TemporalType.DATE)
    private Date fechaAspej;
    @Size(max = 100)
    @Column(name = "archivo_aspej", length = 100)
    private String archivoAspej;
    @Column(name = "activo_aspej")
    private Boolean activoAspej;
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
    @JoinColumn(name = "ide_aspvh", referencedColumnName = "ide_aspvh")
    @ManyToOne
    private AsiPermisosVacacionHext ideAspvh;

    public AsiPermisoJustificacion() {
    }

    public AsiPermisoJustificacion(Integer ideAspej) {
        this.ideAspej = ideAspej;
    }

    public Integer getIdeAspej() {
        return ideAspej;
    }

    public void setIdeAspej(Integer ideAspej) {
        this.ideAspej = ideAspej;
    }

    public String getDetalleAspej() {
        return detalleAspej;
    }

    public void setDetalleAspej(String detalleAspej) {
        this.detalleAspej = detalleAspej;
    }

    public Date getFechaAspej() {
        return fechaAspej;
    }

    public void setFechaAspej(Date fechaAspej) {
        this.fechaAspej = fechaAspej;
    }

    public String getArchivoAspej() {
        return archivoAspej;
    }

    public void setArchivoAspej(String archivoAspej) {
        this.archivoAspej = archivoAspej;
    }

    public Boolean getActivoAspej() {
        return activoAspej;
    }

    public void setActivoAspej(Boolean activoAspej) {
        this.activoAspej = activoAspej;
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

    public AsiPermisosVacacionHext getIdeAspvh() {
        return ideAspvh;
    }

    public void setIdeAspvh(AsiPermisosVacacionHext ideAspvh) {
        this.ideAspvh = ideAspvh;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAspej != null ? ideAspej.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiPermisoJustificacion)) {
            return false;
        }
        AsiPermisoJustificacion other = (AsiPermisoJustificacion) object;
        if ((this.ideAspej == null && other.ideAspej != null) || (this.ideAspej != null && !this.ideAspej.equals(other.ideAspej))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiPermisoJustificacion[ ideAspej=" + ideAspej + " ]";
    }
    
}
