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
@Table(name = "gth_actividad_laboral", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthActividadLaboral.findAll", query = "SELECT g FROM GthActividadLaboral g"),
    @NamedQuery(name = "GthActividadLaboral.findByIdeGtacl", query = "SELECT g FROM GthActividadLaboral g WHERE g.ideGtacl = :ideGtacl"),
    @NamedQuery(name = "GthActividadLaboral.findByDetalleGtacl", query = "SELECT g FROM GthActividadLaboral g WHERE g.detalleGtacl = :detalleGtacl"),
    @NamedQuery(name = "GthActividadLaboral.findByActivoGtacl", query = "SELECT g FROM GthActividadLaboral g WHERE g.activoGtacl = :activoGtacl"),
    @NamedQuery(name = "GthActividadLaboral.findByUsuarioIngre", query = "SELECT g FROM GthActividadLaboral g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthActividadLaboral.findByFechaIngre", query = "SELECT g FROM GthActividadLaboral g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthActividadLaboral.findByUsuarioActua", query = "SELECT g FROM GthActividadLaboral g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthActividadLaboral.findByFechaActua", query = "SELECT g FROM GthActividadLaboral g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthActividadLaboral.findByHoraIngre", query = "SELECT g FROM GthActividadLaboral g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthActividadLaboral.findByHoraActua", query = "SELECT g FROM GthActividadLaboral g WHERE g.horaActua = :horaActua")})
public class GthActividadLaboral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtacl", nullable = false)
    private Integer ideGtacl;
    @Size(max = 50)
    @Column(name = "detalle_gtacl", length = 50)
    private String detalleGtacl;
    @Column(name = "activo_gtacl")
    private Boolean activoGtacl;
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
    @OneToMany(mappedBy = "ideGtacl")
    private List<GthFamiliar> gthFamiliarList;

    public GthActividadLaboral() {
    }

    public GthActividadLaboral(Integer ideGtacl) {
        this.ideGtacl = ideGtacl;
    }

    public Integer getIdeGtacl() {
        return ideGtacl;
    }

    public void setIdeGtacl(Integer ideGtacl) {
        this.ideGtacl = ideGtacl;
    }

    public String getDetalleGtacl() {
        return detalleGtacl;
    }

    public void setDetalleGtacl(String detalleGtacl) {
        this.detalleGtacl = detalleGtacl;
    }

    public Boolean getActivoGtacl() {
        return activoGtacl;
    }

    public void setActivoGtacl(Boolean activoGtacl) {
        this.activoGtacl = activoGtacl;
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

    public List<GthFamiliar> getGthFamiliarList() {
        return gthFamiliarList;
    }

    public void setGthFamiliarList(List<GthFamiliar> gthFamiliarList) {
        this.gthFamiliarList = gthFamiliarList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtacl != null ? ideGtacl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthActividadLaboral)) {
            return false;
        }
        GthActividadLaboral other = (GthActividadLaboral) object;
        if ((this.ideGtacl == null && other.ideGtacl != null) || (this.ideGtacl != null && !this.ideGtacl.equals(other.ideGtacl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthActividadLaboral[ ideGtacl=" + ideGtacl + " ]";
    }
    
}
