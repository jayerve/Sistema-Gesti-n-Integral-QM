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
@Table(name = "veh_tipo_marca")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehTipoMarca.findAll", query = "SELECT v FROM VehTipoMarca v"),
    @NamedQuery(name = "VehTipoMarca.findByIdeVemar", query = "SELECT v FROM VehTipoMarca v WHERE v.ideVemar = :ideVemar"),
    @NamedQuery(name = "VehTipoMarca.findByDetalleVemar", query = "SELECT v FROM VehTipoMarca v WHERE v.detalleVemar = :detalleVemar"),
    @NamedQuery(name = "VehTipoMarca.findByActivoVemar", query = "SELECT v FROM VehTipoMarca v WHERE v.activoVemar = :activoVemar"),
    @NamedQuery(name = "VehTipoMarca.findByUsuarioIngre", query = "SELECT v FROM VehTipoMarca v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehTipoMarca.findByFechaIngre", query = "SELECT v FROM VehTipoMarca v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehTipoMarca.findByHoraIngre", query = "SELECT v FROM VehTipoMarca v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehTipoMarca.findByUsuarioActua", query = "SELECT v FROM VehTipoMarca v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehTipoMarca.findByFechaActua", query = "SELECT v FROM VehTipoMarca v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehTipoMarca.findByHoraActua", query = "SELECT v FROM VehTipoMarca v WHERE v.horaActua = :horaActua")})
public class VehTipoMarca implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vemar")
    private Long ideVemar;
    @Column(name = "detalle_vemar")
    private String detalleVemar;
    @Column(name = "activo_vemar")
    private Boolean activoVemar;
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
    @OneToMany(mappedBy = "ideVemar")
    private List<VehVehiculo> vehVehiculoList;

    public VehTipoMarca() {
    }

    public VehTipoMarca(Long ideVemar) {
        this.ideVemar = ideVemar;
    }

    public Long getIdeVemar() {
        return ideVemar;
    }

    public void setIdeVemar(Long ideVemar) {
        this.ideVemar = ideVemar;
    }

    public String getDetalleVemar() {
        return detalleVemar;
    }

    public void setDetalleVemar(String detalleVemar) {
        this.detalleVemar = detalleVemar;
    }

    public Boolean getActivoVemar() {
        return activoVemar;
    }

    public void setActivoVemar(Boolean activoVemar) {
        this.activoVemar = activoVemar;
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
        hash += (ideVemar != null ? ideVemar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehTipoMarca)) {
            return false;
        }
        VehTipoMarca other = (VehTipoMarca) object;
        if ((this.ideVemar == null && other.ideVemar != null) || (this.ideVemar != null && !this.ideVemar.equals(other.ideVemar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehTipoMarca[ ideVemar=" + ideVemar + " ]";
    }
    
}
