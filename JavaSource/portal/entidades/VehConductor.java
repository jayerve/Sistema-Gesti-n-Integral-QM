/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "veh_conductor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehConductor.findAll", query = "SELECT v FROM VehConductor v"),
    @NamedQuery(name = "VehConductor.findByIdeVecon", query = "SELECT v FROM VehConductor v WHERE v.ideVecon = :ideVecon"),
    @NamedQuery(name = "VehConductor.findByDetalleVecon", query = "SELECT v FROM VehConductor v WHERE v.detalleVecon = :detalleVecon"),
    @NamedQuery(name = "VehConductor.findByActivoVecon", query = "SELECT v FROM VehConductor v WHERE v.activoVecon = :activoVecon"),
    @NamedQuery(name = "VehConductor.findByUsuarioIngre", query = "SELECT v FROM VehConductor v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehConductor.findByFechaIngre", query = "SELECT v FROM VehConductor v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehConductor.findByHoraIngre", query = "SELECT v FROM VehConductor v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehConductor.findByUsuarioActua", query = "SELECT v FROM VehConductor v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehConductor.findByFechaActua", query = "SELECT v FROM VehConductor v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehConductor.findByHoraActua", query = "SELECT v FROM VehConductor v WHERE v.horaActua = :horaActua"),
    @NamedQuery(name = "VehConductor.findByTipoLicenciaVecon", query = "SELECT v FROM VehConductor v WHERE v.tipoLicenciaVecon = :tipoLicenciaVecon"),
    @NamedQuery(name = "VehConductor.findByPuntosLicenciaVecon", query = "SELECT v FROM VehConductor v WHERE v.puntosLicenciaVecon = :puntosLicenciaVecon"),
    @NamedQuery(name = "VehConductor.findByFechaEmitidaVecon", query = "SELECT v FROM VehConductor v WHERE v.fechaEmitidaVecon = :fechaEmitidaVecon"),
    @NamedQuery(name = "VehConductor.findByFechaVigenciaVecon", query = "SELECT v FROM VehConductor v WHERE v.fechaVigenciaVecon = :fechaVigenciaVecon"),
    @NamedQuery(name = "VehConductor.findByEmailVecon", query = "SELECT v FROM VehConductor v WHERE v.emailVecon = :emailVecon")})
public class VehConductor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vecon")
    private Long ideVecon;
    @Column(name = "detalle_vecon")
    private String detalleVecon;
    @Column(name = "activo_vecon")
    private Boolean activoVecon;
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
    @Column(name = "tipo_licencia_vecon")
    private String tipoLicenciaVecon;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puntos_licencia_vecon")
    private BigDecimal puntosLicenciaVecon;
    @Column(name = "fecha_emitida_vecon")
    @Temporal(TemporalType.DATE)
    private Date fechaEmitidaVecon;
    @Column(name = "fecha_vigencia_vecon")
    @Temporal(TemporalType.DATE)
    private Date fechaVigenciaVecon;
    @Column(name = "email_vecon")
    private String emailVecon;
    @OneToMany(mappedBy = "ideVecon")
    private List<VehSolicitud> vehSolicitudList;
    @OneToMany(mappedBy = "ideVecon")
    private List<VehVehiculo> vehVehiculoList;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public VehConductor() {
    }

    public VehConductor(Long ideVecon) {
        this.ideVecon = ideVecon;
    }

    public Long getIdeVecon() {
        return ideVecon;
    }

    public void setIdeVecon(Long ideVecon) {
        this.ideVecon = ideVecon;
    }

    public String getDetalleVecon() {
        return detalleVecon;
    }

    public void setDetalleVecon(String detalleVecon) {
        this.detalleVecon = detalleVecon;
    }

    public Boolean getActivoVecon() {
        return activoVecon;
    }

    public void setActivoVecon(Boolean activoVecon) {
        this.activoVecon = activoVecon;
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

    public String getTipoLicenciaVecon() {
        return tipoLicenciaVecon;
    }

    public void setTipoLicenciaVecon(String tipoLicenciaVecon) {
        this.tipoLicenciaVecon = tipoLicenciaVecon;
    }

    public BigDecimal getPuntosLicenciaVecon() {
        return puntosLicenciaVecon;
    }

    public void setPuntosLicenciaVecon(BigDecimal puntosLicenciaVecon) {
        this.puntosLicenciaVecon = puntosLicenciaVecon;
    }

    public Date getFechaEmitidaVecon() {
        return fechaEmitidaVecon;
    }

    public void setFechaEmitidaVecon(Date fechaEmitidaVecon) {
        this.fechaEmitidaVecon = fechaEmitidaVecon;
    }

    public Date getFechaVigenciaVecon() {
        return fechaVigenciaVecon;
    }

    public void setFechaVigenciaVecon(Date fechaVigenciaVecon) {
        this.fechaVigenciaVecon = fechaVigenciaVecon;
    }

    public String getEmailVecon() {
        return emailVecon;
    }

    public void setEmailVecon(String emailVecon) {
        this.emailVecon = emailVecon;
    }

    @XmlTransient
    public List<VehSolicitud> getVehSolicitudList() {
        return vehSolicitudList;
    }

    public void setVehSolicitudList(List<VehSolicitud> vehSolicitudList) {
        this.vehSolicitudList = vehSolicitudList;
    }

    @XmlTransient
    public List<VehVehiculo> getVehVehiculoList() {
        return vehVehiculoList;
    }

    public void setVehVehiculoList(List<VehVehiculo> vehVehiculoList) {
        this.vehVehiculoList = vehVehiculoList;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVecon != null ? ideVecon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehConductor)) {
            return false;
        }
        VehConductor other = (VehConductor) object;
        if ((this.ideVecon == null && other.ideVecon != null) || (this.ideVecon != null && !this.ideVecon.equals(other.ideVecon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehConductor[ ideVecon=" + ideVecon + " ]";
    }
    
}
