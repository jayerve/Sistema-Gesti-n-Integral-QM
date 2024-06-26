/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author abecerra
 */
@Entity
@Table(name = "veh_tipo_color")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehTipoColor.findAll", query = "SELECT v FROM VehTipoColor v"),
    @NamedQuery(name = "VehTipoColor.findByIdeVecol", query = "SELECT v FROM VehTipoColor v WHERE v.ideVecol = :ideVecol"),
    @NamedQuery(name = "VehTipoColor.findByDetalleVecol", query = "SELECT v FROM VehTipoColor v WHERE v.detalleVecol = :detalleVecol"),
    @NamedQuery(name = "VehTipoColor.findByActivoVecol", query = "SELECT v FROM VehTipoColor v WHERE v.activoVecol = :activoVecol"),
    @NamedQuery(name = "VehTipoColor.findByUsuarioIngre", query = "SELECT v FROM VehTipoColor v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehTipoColor.findByFechaIngre", query = "SELECT v FROM VehTipoColor v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehTipoColor.findByHoraIngre", query = "SELECT v FROM VehTipoColor v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehTipoColor.findByUsuarioActua", query = "SELECT v FROM VehTipoColor v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehTipoColor.findByFechaActua", query = "SELECT v FROM VehTipoColor v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehTipoColor.findByHoraActua", query = "SELECT v FROM VehTipoColor v WHERE v.horaActua = :horaActua")})
public class VehTipoColor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vecol")
    private Long ideVecol;
    @Column(name = "detalle_vecol")
    private String detalleVecol;
    @Column(name = "activo_vecol")
    private Boolean activoVecol;
    @Column(name = "usuario_ingre")
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "usuario_actua")
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideVecol")
    private List<VehVehiculo> vehVehiculoList;

    public VehTipoColor() {
    }

    public VehTipoColor(Long ideVecol) {
        this.ideVecol = ideVecol;
    }

    public Long getIdeVecol() {
        return ideVecol;
    }

    public void setIdeVecol(Long ideVecol) {
        this.ideVecol = ideVecol;
    }

    public String getDetalleVecol() {
        return detalleVecol;
    }

    public void setDetalleVecol(String detalleVecol) {
        this.detalleVecol = detalleVecol;
    }

    public Boolean getActivoVecol() {
        return activoVecol;
    }

    public void setActivoVecol(Boolean activoVecol) {
        this.activoVecol = activoVecol;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
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

    @XmlTransient
    public List<VehVehiculo> getVehVehiculoList() {
        return vehVehiculoList;
    }

    public void setVehVehiculoList(List<VehVehiculo> vehVehiculoList) {
        this.vehVehiculoList = vehVehiculoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVecol != null ? ideVecol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehTipoColor)) {
            return false;
        }
        VehTipoColor other = (VehTipoColor) object;
        if ((this.ideVecol == null && other.ideVecol != null) || (this.ideVecol != null && !this.ideVecol.equals(other.ideVecol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehTipoColor[ ideVecol=" + ideVecol + " ]";
    }
    
}
