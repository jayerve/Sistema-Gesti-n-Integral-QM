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
@Table(name = "gth_marca_vehiculo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthMarcaVehiculo.findAll", query = "SELECT g FROM GthMarcaVehiculo g"),
    @NamedQuery(name = "GthMarcaVehiculo.findByIdeGtmav", query = "SELECT g FROM GthMarcaVehiculo g WHERE g.ideGtmav = :ideGtmav"),
    @NamedQuery(name = "GthMarcaVehiculo.findByDetalleGtmav", query = "SELECT g FROM GthMarcaVehiculo g WHERE g.detalleGtmav = :detalleGtmav"),
    @NamedQuery(name = "GthMarcaVehiculo.findByActivoGtmav", query = "SELECT g FROM GthMarcaVehiculo g WHERE g.activoGtmav = :activoGtmav"),
    @NamedQuery(name = "GthMarcaVehiculo.findByUsuarioIngre", query = "SELECT g FROM GthMarcaVehiculo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthMarcaVehiculo.findByFechaIngre", query = "SELECT g FROM GthMarcaVehiculo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthMarcaVehiculo.findByUsuarioActua", query = "SELECT g FROM GthMarcaVehiculo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthMarcaVehiculo.findByFechaActua", query = "SELECT g FROM GthMarcaVehiculo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthMarcaVehiculo.findByHoraIngre", query = "SELECT g FROM GthMarcaVehiculo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthMarcaVehiculo.findByHoraActua", query = "SELECT g FROM GthMarcaVehiculo g WHERE g.horaActua = :horaActua")})
public class GthMarcaVehiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtmav", nullable = false)
    private Integer ideGtmav;
    @Size(max = 50)
    @Column(name = "detalle_gtmav", length = 50)
    private String detalleGtmav;
    @Column(name = "activo_gtmav")
    private Boolean activoGtmav;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gthMarcaVehiculo")
    private List<GthModeloVehiculo> gthModeloVehiculoList;

    public GthMarcaVehiculo() {
    }

    public GthMarcaVehiculo(Integer ideGtmav) {
        this.ideGtmav = ideGtmav;
    }

    public Integer getIdeGtmav() {
        return ideGtmav;
    }

    public void setIdeGtmav(Integer ideGtmav) {
        this.ideGtmav = ideGtmav;
    }

    public String getDetalleGtmav() {
        return detalleGtmav;
    }

    public void setDetalleGtmav(String detalleGtmav) {
        this.detalleGtmav = detalleGtmav;
    }

    public Boolean getActivoGtmav() {
        return activoGtmav;
    }

    public void setActivoGtmav(Boolean activoGtmav) {
        this.activoGtmav = activoGtmav;
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

    public List<GthModeloVehiculo> getGthModeloVehiculoList() {
        return gthModeloVehiculoList;
    }

    public void setGthModeloVehiculoList(List<GthModeloVehiculo> gthModeloVehiculoList) {
        this.gthModeloVehiculoList = gthModeloVehiculoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtmav != null ? ideGtmav.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthMarcaVehiculo)) {
            return false;
        }
        GthMarcaVehiculo other = (GthMarcaVehiculo) object;
        if ((this.ideGtmav == null && other.ideGtmav != null) || (this.ideGtmav != null && !this.ideGtmav.equals(other.ideGtmav))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthMarcaVehiculo[ ideGtmav=" + ideGtmav + " ]";
    }
    
}
