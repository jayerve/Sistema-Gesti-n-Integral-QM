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
@Table(name = "veh_tipo_combustible")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehTipoCombustible.findAll", query = "SELECT v FROM VehTipoCombustible v"),
    @NamedQuery(name = "VehTipoCombustible.findByIdeVecom", query = "SELECT v FROM VehTipoCombustible v WHERE v.ideVecom = :ideVecom"),
    @NamedQuery(name = "VehTipoCombustible.findByDetalleVecom", query = "SELECT v FROM VehTipoCombustible v WHERE v.detalleVecom = :detalleVecom"),
    @NamedQuery(name = "VehTipoCombustible.findByActivoVecom", query = "SELECT v FROM VehTipoCombustible v WHERE v.activoVecom = :activoVecom"),
    @NamedQuery(name = "VehTipoCombustible.findByUsuarioIngre", query = "SELECT v FROM VehTipoCombustible v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehTipoCombustible.findByFechaIngre", query = "SELECT v FROM VehTipoCombustible v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehTipoCombustible.findByHoraIngre", query = "SELECT v FROM VehTipoCombustible v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehTipoCombustible.findByUsuarioActua", query = "SELECT v FROM VehTipoCombustible v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehTipoCombustible.findByFechaActua", query = "SELECT v FROM VehTipoCombustible v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehTipoCombustible.findByHoraActua", query = "SELECT v FROM VehTipoCombustible v WHERE v.horaActua = :horaActua")})
public class VehTipoCombustible implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vecom")
    private Long ideVecom;
    @Column(name = "detalle_vecom")
    private String detalleVecom;
    @Column(name = "activo_vecom")
    private Boolean activoVecom;
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
    @OneToMany(mappedBy = "ideVecom")
    private List<VehVehiculo> vehVehiculoList;

    public VehTipoCombustible() {
    }

    public VehTipoCombustible(Long ideVecom) {
        this.ideVecom = ideVecom;
    }

    public Long getIdeVecom() {
        return ideVecom;
    }

    public void setIdeVecom(Long ideVecom) {
        this.ideVecom = ideVecom;
    }

    public String getDetalleVecom() {
        return detalleVecom;
    }

    public void setDetalleVecom(String detalleVecom) {
        this.detalleVecom = detalleVecom;
    }

    public Boolean getActivoVecom() {
        return activoVecom;
    }

    public void setActivoVecom(Boolean activoVecom) {
        this.activoVecom = activoVecom;
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
        hash += (ideVecom != null ? ideVecom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehTipoCombustible)) {
            return false;
        }
        VehTipoCombustible other = (VehTipoCombustible) object;
        if ((this.ideVecom == null && other.ideVecom != null) || (this.ideVecom != null && !this.ideVecom.equals(other.ideVecom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehTipoCombustible[ ideVecom=" + ideVecom + " ]";
    }
    
}
