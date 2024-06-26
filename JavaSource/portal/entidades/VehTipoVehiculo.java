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
@Table(name = "veh_tipo_vehiculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehTipoVehiculo.findAll", query = "SELECT v FROM VehTipoVehiculo v"),
    @NamedQuery(name = "VehTipoVehiculo.findByIdeVetip", query = "SELECT v FROM VehTipoVehiculo v WHERE v.ideVetip = :ideVetip"),
    @NamedQuery(name = "VehTipoVehiculo.findByDetalleVetip", query = "SELECT v FROM VehTipoVehiculo v WHERE v.detalleVetip = :detalleVetip"),
    @NamedQuery(name = "VehTipoVehiculo.findByActivoVetip", query = "SELECT v FROM VehTipoVehiculo v WHERE v.activoVetip = :activoVetip"),
    @NamedQuery(name = "VehTipoVehiculo.findByUsuarioIngre", query = "SELECT v FROM VehTipoVehiculo v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehTipoVehiculo.findByFechaIngre", query = "SELECT v FROM VehTipoVehiculo v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehTipoVehiculo.findByHoraIngre", query = "SELECT v FROM VehTipoVehiculo v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehTipoVehiculo.findByUsuarioActua", query = "SELECT v FROM VehTipoVehiculo v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehTipoVehiculo.findByFechaActua", query = "SELECT v FROM VehTipoVehiculo v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehTipoVehiculo.findByHoraActua", query = "SELECT v FROM VehTipoVehiculo v WHERE v.horaActua = :horaActua")})
public class VehTipoVehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vetip")
    private Long ideVetip;
    @Column(name = "detalle_vetip")
    private String detalleVetip;
    @Column(name = "activo_vetip")
    private Boolean activoVetip;
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
    @OneToMany(mappedBy = "ideVetip")
    private List<VehParametroVehiculo> vehParametroVehiculoList;
    @OneToMany(mappedBy = "ideVetip")
    private List<VehVehiculo> vehVehiculoList;

    public VehTipoVehiculo() {
    }

    public VehTipoVehiculo(Long ideVetip) {
        this.ideVetip = ideVetip;
    }

    public Long getIdeVetip() {
        return ideVetip;
    }

    public void setIdeVetip(Long ideVetip) {
        this.ideVetip = ideVetip;
    }

    public String getDetalleVetip() {
        return detalleVetip;
    }

    public void setDetalleVetip(String detalleVetip) {
        this.detalleVetip = detalleVetip;
    }

    public Boolean getActivoVetip() {
        return activoVetip;
    }

    public void setActivoVetip(Boolean activoVetip) {
        this.activoVetip = activoVetip;
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
    public List<VehParametroVehiculo> getVehParametroVehiculoList() {
        return vehParametroVehiculoList;
    }

    public void setVehParametroVehiculoList(List<VehParametroVehiculo> vehParametroVehiculoList) {
        this.vehParametroVehiculoList = vehParametroVehiculoList;
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
        hash += (ideVetip != null ? ideVetip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehTipoVehiculo)) {
            return false;
        }
        VehTipoVehiculo other = (VehTipoVehiculo) object;
        if ((this.ideVetip == null && other.ideVetip != null) || (this.ideVetip != null && !this.ideVetip.equals(other.ideVetip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehTipoVehiculo[ ideVetip=" + ideVetip + " ]";
    }
    
}
