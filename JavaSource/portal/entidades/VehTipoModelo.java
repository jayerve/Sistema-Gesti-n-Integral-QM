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
@Table(name = "veh_tipo_modelo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehTipoModelo.findAll", query = "SELECT v FROM VehTipoModelo v"),
    @NamedQuery(name = "VehTipoModelo.findByIdeVemod", query = "SELECT v FROM VehTipoModelo v WHERE v.ideVemod = :ideVemod"),
    @NamedQuery(name = "VehTipoModelo.findByDetalleVemod", query = "SELECT v FROM VehTipoModelo v WHERE v.detalleVemod = :detalleVemod"),
    @NamedQuery(name = "VehTipoModelo.findByActivoVemod", query = "SELECT v FROM VehTipoModelo v WHERE v.activoVemod = :activoVemod"),
    @NamedQuery(name = "VehTipoModelo.findByUsuarioIngre", query = "SELECT v FROM VehTipoModelo v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehTipoModelo.findByFechaIngre", query = "SELECT v FROM VehTipoModelo v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehTipoModelo.findByHoraIngre", query = "SELECT v FROM VehTipoModelo v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehTipoModelo.findByUsuarioActua", query = "SELECT v FROM VehTipoModelo v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehTipoModelo.findByFechaActua", query = "SELECT v FROM VehTipoModelo v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehTipoModelo.findByHoraActua", query = "SELECT v FROM VehTipoModelo v WHERE v.horaActua = :horaActua")})
public class VehTipoModelo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vemod")
    private Long ideVemod;
    @Column(name = "detalle_vemod")
    private String detalleVemod;
    @Column(name = "activo_vemod")
    private Boolean activoVemod;
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
    @OneToMany(mappedBy = "ideVemod")
    private List<VehVehiculo> vehVehiculoList;

    public VehTipoModelo() {
    }

    public VehTipoModelo(Long ideVemod) {
        this.ideVemod = ideVemod;
    }

    public Long getIdeVemod() {
        return ideVemod;
    }

    public void setIdeVemod(Long ideVemod) {
        this.ideVemod = ideVemod;
    }

    public String getDetalleVemod() {
        return detalleVemod;
    }

    public void setDetalleVemod(String detalleVemod) {
        this.detalleVemod = detalleVemod;
    }

    public Boolean getActivoVemod() {
        return activoVemod;
    }

    public void setActivoVemod(Boolean activoVemod) {
        this.activoVemod = activoVemod;
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
        hash += (ideVemod != null ? ideVemod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehTipoModelo)) {
            return false;
        }
        VehTipoModelo other = (VehTipoModelo) object;
        if ((this.ideVemod == null && other.ideVemod != null) || (this.ideVemod != null && !this.ideVemod.equals(other.ideVemod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehTipoModelo[ ideVemod=" + ideVemod + " ]";
    }
    
}
