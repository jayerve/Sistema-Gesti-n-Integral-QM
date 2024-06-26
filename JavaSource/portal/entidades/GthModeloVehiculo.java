/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "gth_modelo_vehiculo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthModeloVehiculo.findAll", query = "SELECT g FROM GthModeloVehiculo g"),
    @NamedQuery(name = "GthModeloVehiculo.findByIdeGtmov", query = "SELECT g FROM GthModeloVehiculo g WHERE g.gthModeloVehiculoPK.ideGtmov = :ideGtmov"),
    @NamedQuery(name = "GthModeloVehiculo.findByIdeGtmav", query = "SELECT g FROM GthModeloVehiculo g WHERE g.gthModeloVehiculoPK.ideGtmav = :ideGtmav"),
    @NamedQuery(name = "GthModeloVehiculo.findByDetalleGtmov", query = "SELECT g FROM GthModeloVehiculo g WHERE g.detalleGtmov = :detalleGtmov"),
    @NamedQuery(name = "GthModeloVehiculo.findByActivoGtmov", query = "SELECT g FROM GthModeloVehiculo g WHERE g.activoGtmov = :activoGtmov"),
    @NamedQuery(name = "GthModeloVehiculo.findByUsuarioIngre", query = "SELECT g FROM GthModeloVehiculo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthModeloVehiculo.findByFechaIngre", query = "SELECT g FROM GthModeloVehiculo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthModeloVehiculo.findByUsuarioActua", query = "SELECT g FROM GthModeloVehiculo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthModeloVehiculo.findByFechaActua", query = "SELECT g FROM GthModeloVehiculo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthModeloVehiculo.findByHoraIngre", query = "SELECT g FROM GthModeloVehiculo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthModeloVehiculo.findByHoraActua", query = "SELECT g FROM GthModeloVehiculo g WHERE g.horaActua = :horaActua")})
public class GthModeloVehiculo implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GthModeloVehiculoPK gthModeloVehiculoPK;
    @Size(max = 50)
    @Column(name = "detalle_gtmov", length = 50)
    private String detalleGtmov;
    @Column(name = "activo_gtmov")
    private Boolean activoGtmov;
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
    @JoinColumn(name = "ide_gtmav", referencedColumnName = "ide_gtmav", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GthMarcaVehiculo gthMarcaVehiculo;
    @OneToMany(mappedBy = "gthModeloVehiculo")
    private List<GthVehiculoEmpleado> gthVehiculoEmpleadoList;

    public GthModeloVehiculo() {
    }

    public GthModeloVehiculo(GthModeloVehiculoPK gthModeloVehiculoPK) {
        this.gthModeloVehiculoPK = gthModeloVehiculoPK;
    }

    public GthModeloVehiculo(int ideGtmov, int ideGtmav) {
        this.gthModeloVehiculoPK = new GthModeloVehiculoPK(ideGtmov, ideGtmav);
    }

    public GthModeloVehiculoPK getGthModeloVehiculoPK() {
        return gthModeloVehiculoPK;
    }

    public void setGthModeloVehiculoPK(GthModeloVehiculoPK gthModeloVehiculoPK) {
        this.gthModeloVehiculoPK = gthModeloVehiculoPK;
    }

    public String getDetalleGtmov() {
        return detalleGtmov;
    }

    public void setDetalleGtmov(String detalleGtmov) {
        this.detalleGtmov = detalleGtmov;
    }

    public Boolean getActivoGtmov() {
        return activoGtmov;
    }

    public void setActivoGtmov(Boolean activoGtmov) {
        this.activoGtmov = activoGtmov;
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

    public GthMarcaVehiculo getGthMarcaVehiculo() {
        return gthMarcaVehiculo;
    }

    public void setGthMarcaVehiculo(GthMarcaVehiculo gthMarcaVehiculo) {
        this.gthMarcaVehiculo = gthMarcaVehiculo;
    }

    public List<GthVehiculoEmpleado> getGthVehiculoEmpleadoList() {
        return gthVehiculoEmpleadoList;
    }

    public void setGthVehiculoEmpleadoList(List<GthVehiculoEmpleado> gthVehiculoEmpleadoList) {
        this.gthVehiculoEmpleadoList = gthVehiculoEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gthModeloVehiculoPK != null ? gthModeloVehiculoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthModeloVehiculo)) {
            return false;
        }
        GthModeloVehiculo other = (GthModeloVehiculo) object;
        if ((this.gthModeloVehiculoPK == null && other.gthModeloVehiculoPK != null) || (this.gthModeloVehiculoPK != null && !this.gthModeloVehiculoPK.equals(other.gthModeloVehiculoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthModeloVehiculo[ gthModeloVehiculoPK=" + gthModeloVehiculoPK + " ]";
    }
    
}
