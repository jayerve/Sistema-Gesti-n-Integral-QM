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
@Table(name = "gth_cargo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthCargo.findAll", query = "SELECT g FROM GthCargo g"),
    @NamedQuery(name = "GthCargo.findByIdeGtcar", query = "SELECT g FROM GthCargo g WHERE g.ideGtcar = :ideGtcar"),
    @NamedQuery(name = "GthCargo.findByDetalleGtcar", query = "SELECT g FROM GthCargo g WHERE g.detalleGtcar = :detalleGtcar"),
    @NamedQuery(name = "GthCargo.findByActivoGtcar", query = "SELECT g FROM GthCargo g WHERE g.activoGtcar = :activoGtcar"),
    @NamedQuery(name = "GthCargo.findByUsuarioIngre", query = "SELECT g FROM GthCargo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthCargo.findByFechaIngre", query = "SELECT g FROM GthCargo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthCargo.findByUsuarioActua", query = "SELECT g FROM GthCargo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthCargo.findByFechaActua", query = "SELECT g FROM GthCargo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthCargo.findByHoraIngre", query = "SELECT g FROM GthCargo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthCargo.findByHoraActua", query = "SELECT g FROM GthCargo g WHERE g.horaActua = :horaActua")})
public class GthCargo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtcar", nullable = false)
    private Integer ideGtcar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "detalle_gtcar", nullable = false, length = 50)
    private String detalleGtcar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_gtcar", nullable = false)
    private boolean activoGtcar;
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
    @OneToMany(mappedBy = "ideGtcar")
    private List<GthConyuge> gthConyugeList;

    public GthCargo() {
    }

    public GthCargo(Integer ideGtcar) {
        this.ideGtcar = ideGtcar;
    }

    public GthCargo(Integer ideGtcar, String detalleGtcar, boolean activoGtcar) {
        this.ideGtcar = ideGtcar;
        this.detalleGtcar = detalleGtcar;
        this.activoGtcar = activoGtcar;
    }

    public Integer getIdeGtcar() {
        return ideGtcar;
    }

    public void setIdeGtcar(Integer ideGtcar) {
        this.ideGtcar = ideGtcar;
    }

    public String getDetalleGtcar() {
        return detalleGtcar;
    }

    public void setDetalleGtcar(String detalleGtcar) {
        this.detalleGtcar = detalleGtcar;
    }

    public boolean getActivoGtcar() {
        return activoGtcar;
    }

    public void setActivoGtcar(boolean activoGtcar) {
        this.activoGtcar = activoGtcar;
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

    public List<GthConyuge> getGthConyugeList() {
        return gthConyugeList;
    }

    public void setGthConyugeList(List<GthConyuge> gthConyugeList) {
        this.gthConyugeList = gthConyugeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtcar != null ? ideGtcar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthCargo)) {
            return false;
        }
        GthCargo other = (GthCargo) object;
        if ((this.ideGtcar == null && other.ideGtcar != null) || (this.ideGtcar != null && !this.ideGtcar.equals(other.ideGtcar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthCargo[ ideGtcar=" + ideGtcar + " ]";
    }
    
}
