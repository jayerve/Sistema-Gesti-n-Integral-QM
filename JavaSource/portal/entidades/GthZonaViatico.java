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
@Table(name = "gth_zona_viatico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthZonaViatico.findAll", query = "SELECT g FROM GthZonaViatico g"),
    @NamedQuery(name = "GthZonaViatico.findByIdeGtzov", query = "SELECT g FROM GthZonaViatico g WHERE g.ideGtzov = :ideGtzov"),
    @NamedQuery(name = "GthZonaViatico.findByDetalleGtzov", query = "SELECT g FROM GthZonaViatico g WHERE g.detalleGtzov = :detalleGtzov"),
    @NamedQuery(name = "GthZonaViatico.findByActivoGtzov", query = "SELECT g FROM GthZonaViatico g WHERE g.activoGtzov = :activoGtzov"),
    @NamedQuery(name = "GthZonaViatico.findByUsuarioIngre", query = "SELECT g FROM GthZonaViatico g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthZonaViatico.findByFechaIngre", query = "SELECT g FROM GthZonaViatico g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthZonaViatico.findByUsuarioActua", query = "SELECT g FROM GthZonaViatico g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthZonaViatico.findByFechaActua", query = "SELECT g FROM GthZonaViatico g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthZonaViatico.findByHoraIngre", query = "SELECT g FROM GthZonaViatico g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthZonaViatico.findByHoraActua", query = "SELECT g FROM GthZonaViatico g WHERE g.horaActua = :horaActua")})
public class GthZonaViatico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtzov", nullable = false)
    private Integer ideGtzov;
    @Size(max = 50)
    @Column(name = "detalle_gtzov", length = 50)
    private String detalleGtzov;
    @Column(name = "activo_gtzov")
    private Boolean activoGtzov;
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
    @OneToMany(mappedBy = "ideGtzov")
    private List<GthNivelZonaViatico> gthNivelZonaViaticoList;

    public GthZonaViatico() {
    }

    public GthZonaViatico(Integer ideGtzov) {
        this.ideGtzov = ideGtzov;
    }

    public Integer getIdeGtzov() {
        return ideGtzov;
    }

    public void setIdeGtzov(Integer ideGtzov) {
        this.ideGtzov = ideGtzov;
    }

    public String getDetalleGtzov() {
        return detalleGtzov;
    }

    public void setDetalleGtzov(String detalleGtzov) {
        this.detalleGtzov = detalleGtzov;
    }

    public Boolean getActivoGtzov() {
        return activoGtzov;
    }

    public void setActivoGtzov(Boolean activoGtzov) {
        this.activoGtzov = activoGtzov;
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

    public List<GthNivelZonaViatico> getGthNivelZonaViaticoList() {
        return gthNivelZonaViaticoList;
    }

    public void setGthNivelZonaViaticoList(List<GthNivelZonaViatico> gthNivelZonaViaticoList) {
        this.gthNivelZonaViaticoList = gthNivelZonaViaticoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtzov != null ? ideGtzov.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthZonaViatico)) {
            return false;
        }
        GthZonaViatico other = (GthZonaViatico) object;
        if ((this.ideGtzov == null && other.ideGtzov != null) || (this.ideGtzov != null && !this.ideGtzov.equals(other.ideGtzov))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthZonaViatico[ ideGtzov=" + ideGtzov + " ]";
    }
    
}
