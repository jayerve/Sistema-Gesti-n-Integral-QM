/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author abecerra
 */
@Entity
@Table(name = "veh_parametro_vehiculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehParametroVehiculo.findAll", query = "SELECT v FROM VehParametroVehiculo v"),
    @NamedQuery(name = "VehParametroVehiculo.findByIdeVepav", query = "SELECT v FROM VehParametroVehiculo v WHERE v.ideVepav = :ideVepav"),
    @NamedQuery(name = "VehParametroVehiculo.findByDetalleVepav", query = "SELECT v FROM VehParametroVehiculo v WHERE v.detalleVepav = :detalleVepav"),
    @NamedQuery(name = "VehParametroVehiculo.findByActivoVepav", query = "SELECT v FROM VehParametroVehiculo v WHERE v.activoVepav = :activoVepav"),
    @NamedQuery(name = "VehParametroVehiculo.findByUsuarioIngre", query = "SELECT v FROM VehParametroVehiculo v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehParametroVehiculo.findByFechaIngre", query = "SELECT v FROM VehParametroVehiculo v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehParametroVehiculo.findByHoraIngre", query = "SELECT v FROM VehParametroVehiculo v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehParametroVehiculo.findByUsuarioActua", query = "SELECT v FROM VehParametroVehiculo v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehParametroVehiculo.findByFechaActua", query = "SELECT v FROM VehParametroVehiculo v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehParametroVehiculo.findByHoraActua", query = "SELECT v FROM VehParametroVehiculo v WHERE v.horaActua = :horaActua"),
    @NamedQuery(name = "VehParametroVehiculo.findByValorFrecuenciaVepav", query = "SELECT v FROM VehParametroVehiculo v WHERE v.valorFrecuenciaVepav = :valorFrecuenciaVepav"),
    @NamedQuery(name = "VehParametroVehiculo.findByValorAlertaVepav", query = "SELECT v FROM VehParametroVehiculo v WHERE v.valorAlertaVepav = :valorAlertaVepav")})
public class VehParametroVehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vepav")
    private Long ideVepav;
    @Column(name = "detalle_vepav")
    private String detalleVepav;
    @Column(name = "activo_vepav")
    private Boolean activoVepav;
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
    @Column(name = "valor_frecuencia_vepav")
    private Integer valorFrecuenciaVepav;
    @Column(name = "valor_alerta_vepav")
    private Integer valorAlertaVepav;
    @JoinColumn(name = "ide_vetip", referencedColumnName = "ide_vetip")
    @ManyToOne
    private VehTipoVehiculo ideVetip;

    public VehParametroVehiculo() {
    }

    public VehParametroVehiculo(Long ideVepav) {
        this.ideVepav = ideVepav;
    }

    public Long getIdeVepav() {
        return ideVepav;
    }

    public void setIdeVepav(Long ideVepav) {
        this.ideVepav = ideVepav;
    }

    public String getDetalleVepav() {
        return detalleVepav;
    }

    public void setDetalleVepav(String detalleVepav) {
        this.detalleVepav = detalleVepav;
    }

    public Boolean getActivoVepav() {
        return activoVepav;
    }

    public void setActivoVepav(Boolean activoVepav) {
        this.activoVepav = activoVepav;
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

    public Integer getValorFrecuenciaVepav() {
        return valorFrecuenciaVepav;
    }

    public void setValorFrecuenciaVepav(Integer valorFrecuenciaVepav) {
        this.valorFrecuenciaVepav = valorFrecuenciaVepav;
    }

    public Integer getValorAlertaVepav() {
        return valorAlertaVepav;
    }

    public void setValorAlertaVepav(Integer valorAlertaVepav) {
        this.valorAlertaVepav = valorAlertaVepav;
    }

    public VehTipoVehiculo getIdeVetip() {
        return ideVetip;
    }

    public void setIdeVetip(VehTipoVehiculo ideVetip) {
        this.ideVetip = ideVetip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVepav != null ? ideVepav.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehParametroVehiculo)) {
            return false;
        }
        VehParametroVehiculo other = (VehParametroVehiculo) object;
        if ((this.ideVepav == null && other.ideVepav != null) || (this.ideVepav != null && !this.ideVepav.equals(other.ideVepav))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehParametroVehiculo[ ideVepav=" + ideVepav + " ]";
    }
    
}
