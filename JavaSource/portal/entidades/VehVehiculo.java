/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "veh_vehiculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehVehiculo.findAll", query = "SELECT v FROM VehVehiculo v"),
    @NamedQuery(name = "VehVehiculo.findByIdeVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.ideVeveh = :ideVeveh"),
    @NamedQuery(name = "VehVehiculo.findByMotorVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.motorVeveh = :motorVeveh"),
    @NamedQuery(name = "VehVehiculo.findByPlacaVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.placaVeveh = :placaVeveh"),
    @NamedQuery(name = "VehVehiculo.findByChasisVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.chasisVeveh = :chasisVeveh"),
    @NamedQuery(name = "VehVehiculo.findByAnioFabricacionVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.anioFabricacionVeveh = :anioFabricacionVeveh"),
    @NamedQuery(name = "VehVehiculo.findByFechaMatriculacionVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.fechaMatriculacionVeveh = :fechaMatriculacionVeveh"),
    @NamedQuery(name = "VehVehiculo.findByFechaCaducidadVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.fechaCaducidadVeveh = :fechaCaducidadVeveh"),
    @NamedQuery(name = "VehVehiculo.findByCilindrajeVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.cilindrajeVeveh = :cilindrajeVeveh"),
    @NamedQuery(name = "VehVehiculo.findByKilometrajeVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.kilometrajeVeveh = :kilometrajeVeveh"),
    @NamedQuery(name = "VehVehiculo.findByFechaUltimoPagoVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.fechaUltimoPagoVeveh = :fechaUltimoPagoVeveh"),
    @NamedQuery(name = "VehVehiculo.findByFechaAdquisicionVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.fechaAdquisicionVeveh = :fechaAdquisicionVeveh"),
    @NamedQuery(name = "VehVehiculo.findByActivoVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.activoVeveh = :activoVeveh"),
    @NamedQuery(name = "VehVehiculo.findByUsuarioIngre", query = "SELECT v FROM VehVehiculo v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehVehiculo.findByFechaIngre", query = "SELECT v FROM VehVehiculo v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehVehiculo.findByHoraIngre", query = "SELECT v FROM VehVehiculo v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehVehiculo.findByUsuarioActua", query = "SELECT v FROM VehVehiculo v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehVehiculo.findByFechaActua", query = "SELECT v FROM VehVehiculo v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehVehiculo.findByHoraActua", query = "SELECT v FROM VehVehiculo v WHERE v.horaActua = :horaActua"),
    @NamedQuery(name = "VehVehiculo.findByCapacidadPasajerosVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.capacidadPasajerosVeveh = :capacidadPasajerosVeveh"),
    @NamedQuery(name = "VehVehiculo.findByLecturaAceiteVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.lecturaAceiteVeveh = :lecturaAceiteVeveh"),
    @NamedQuery(name = "VehVehiculo.findByLecturaAbcVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.lecturaAbcVeveh = :lecturaAbcVeveh"),
    @NamedQuery(name = "VehVehiculo.findByLecturaLlantasVeveh", query = "SELECT v FROM VehVehiculo v WHERE v.lecturaLlantasVeveh = :lecturaLlantasVeveh")})
public class VehVehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_veveh")
    private Long ideVeveh;
    @Column(name = "motor_veveh")
    private String motorVeveh;
    @Column(name = "placa_veveh")
    private String placaVeveh;
    @Column(name = "chasis_veveh")
    private String chasisVeveh;
    @Column(name = "anio_fabricacion_veveh")
    private String anioFabricacionVeveh;
    @Column(name = "fecha_matriculacion_veveh")
    @Temporal(TemporalType.DATE)
    private Date fechaMatriculacionVeveh;
    @Column(name = "fecha_caducidad_veveh")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidadVeveh;
    @Column(name = "cilindraje_veveh")
    private Integer cilindrajeVeveh;
    @Column(name = "kilometraje_veveh")
    private Integer kilometrajeVeveh;
    @Column(name = "fecha_ultimo_pago_veveh")
    @Temporal(TemporalType.DATE)
    private Date fechaUltimoPagoVeveh;
    @Column(name = "fecha_adquisicion_veveh")
    @Temporal(TemporalType.DATE)
    private Date fechaAdquisicionVeveh;
    @Column(name = "activo_veveh")
    private Boolean activoVeveh;
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
    @Column(name = "capacidad_pasajeros_veveh")
    private BigInteger capacidadPasajerosVeveh;
    @Column(name = "lectura_aceite_veveh")
    private BigInteger lecturaAceiteVeveh;
    @Column(name = "lectura_abc_veveh")
    private BigInteger lecturaAbcVeveh;
    @Column(name = "lectura_llantas_veveh")
    private BigInteger lecturaLlantasVeveh;
    @OneToMany(mappedBy = "ideVeveh")
    private List<VehSolicitud> vehSolicitudList;
    @JoinColumn(name = "ide_afact", referencedColumnName = "ide_afact")
    @ManyToOne
    private AfiActivo ideAfact;
    @JoinColumn(name = "ide_vecon", referencedColumnName = "ide_vecon")
    @ManyToOne
    private VehConductor ideVecon;
    @JoinColumn(name = "ide_vecol", referencedColumnName = "ide_vecol")
    @ManyToOne
    private VehTipoColor ideVecol;
    @JoinColumn(name = "ide_vecom", referencedColumnName = "ide_vecom")
    @ManyToOne
    private VehTipoCombustible ideVecom;
    @JoinColumn(name = "ide_vemar", referencedColumnName = "ide_vemar")
    @ManyToOne
    private VehTipoMarca ideVemar;
    @JoinColumn(name = "ide_vemod", referencedColumnName = "ide_vemod")
    @ManyToOne
    private VehTipoModelo ideVemod;
    @JoinColumn(name = "ide_vetip", referencedColumnName = "ide_vetip")
    @ManyToOne
    private VehTipoVehiculo ideVetip;

    public VehVehiculo() {
    }

    public VehVehiculo(Long ideVeveh) {
        this.ideVeveh = ideVeveh;
    }

    public Long getIdeVeveh() {
        return ideVeveh;
    }

    public void setIdeVeveh(Long ideVeveh) {
        this.ideVeveh = ideVeveh;
    }

    public String getMotorVeveh() {
        return motorVeveh;
    }

    public void setMotorVeveh(String motorVeveh) {
        this.motorVeveh = motorVeveh;
    }

    public String getPlacaVeveh() {
        return placaVeveh;
    }

    public void setPlacaVeveh(String placaVeveh) {
        this.placaVeveh = placaVeveh;
    }

    public String getChasisVeveh() {
        return chasisVeveh;
    }

    public void setChasisVeveh(String chasisVeveh) {
        this.chasisVeveh = chasisVeveh;
    }

    public String getAnioFabricacionVeveh() {
        return anioFabricacionVeveh;
    }

    public void setAnioFabricacionVeveh(String anioFabricacionVeveh) {
        this.anioFabricacionVeveh = anioFabricacionVeveh;
    }

    public Date getFechaMatriculacionVeveh() {
        return fechaMatriculacionVeveh;
    }

    public void setFechaMatriculacionVeveh(Date fechaMatriculacionVeveh) {
        this.fechaMatriculacionVeveh = fechaMatriculacionVeveh;
    }

    public Date getFechaCaducidadVeveh() {
        return fechaCaducidadVeveh;
    }

    public void setFechaCaducidadVeveh(Date fechaCaducidadVeveh) {
        this.fechaCaducidadVeveh = fechaCaducidadVeveh;
    }

    public Integer getCilindrajeVeveh() {
        return cilindrajeVeveh;
    }

    public void setCilindrajeVeveh(Integer cilindrajeVeveh) {
        this.cilindrajeVeveh = cilindrajeVeveh;
    }

    public Integer getKilometrajeVeveh() {
        return kilometrajeVeveh;
    }

    public void setKilometrajeVeveh(Integer kilometrajeVeveh) {
        this.kilometrajeVeveh = kilometrajeVeveh;
    }

    public Date getFechaUltimoPagoVeveh() {
        return fechaUltimoPagoVeveh;
    }

    public void setFechaUltimoPagoVeveh(Date fechaUltimoPagoVeveh) {
        this.fechaUltimoPagoVeveh = fechaUltimoPagoVeveh;
    }

    public Date getFechaAdquisicionVeveh() {
        return fechaAdquisicionVeveh;
    }

    public void setFechaAdquisicionVeveh(Date fechaAdquisicionVeveh) {
        this.fechaAdquisicionVeveh = fechaAdquisicionVeveh;
    }

    public Boolean getActivoVeveh() {
        return activoVeveh;
    }

    public void setActivoVeveh(Boolean activoVeveh) {
        this.activoVeveh = activoVeveh;
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

    public BigInteger getCapacidadPasajerosVeveh() {
        return capacidadPasajerosVeveh;
    }

    public void setCapacidadPasajerosVeveh(BigInteger capacidadPasajerosVeveh) {
        this.capacidadPasajerosVeveh = capacidadPasajerosVeveh;
    }

    public BigInteger getLecturaAceiteVeveh() {
        return lecturaAceiteVeveh;
    }

    public void setLecturaAceiteVeveh(BigInteger lecturaAceiteVeveh) {
        this.lecturaAceiteVeveh = lecturaAceiteVeveh;
    }

    public BigInteger getLecturaAbcVeveh() {
        return lecturaAbcVeveh;
    }

    public void setLecturaAbcVeveh(BigInteger lecturaAbcVeveh) {
        this.lecturaAbcVeveh = lecturaAbcVeveh;
    }

    public BigInteger getLecturaLlantasVeveh() {
        return lecturaLlantasVeveh;
    }

    public void setLecturaLlantasVeveh(BigInteger lecturaLlantasVeveh) {
        this.lecturaLlantasVeveh = lecturaLlantasVeveh;
    }

    @XmlTransient
    public List<VehSolicitud> getVehSolicitudList() {
        return vehSolicitudList;
    }

    public void setVehSolicitudList(List<VehSolicitud> vehSolicitudList) {
        this.vehSolicitudList = vehSolicitudList;
    }

    public AfiActivo getIdeAfact() {
        return ideAfact;
    }

    public void setIdeAfact(AfiActivo ideAfact) {
        this.ideAfact = ideAfact;
    }

    public VehConductor getIdeVecon() {
        return ideVecon;
    }

    public void setIdeVecon(VehConductor ideVecon) {
        this.ideVecon = ideVecon;
    }

    public VehTipoColor getIdeVecol() {
        return ideVecol;
    }

    public void setIdeVecol(VehTipoColor ideVecol) {
        this.ideVecol = ideVecol;
    }

    public VehTipoCombustible getIdeVecom() {
        return ideVecom;
    }

    public void setIdeVecom(VehTipoCombustible ideVecom) {
        this.ideVecom = ideVecom;
    }

    public VehTipoMarca getIdeVemar() {
        return ideVemar;
    }

    public void setIdeVemar(VehTipoMarca ideVemar) {
        this.ideVemar = ideVemar;
    }

    public VehTipoModelo getIdeVemod() {
        return ideVemod;
    }

    public void setIdeVemod(VehTipoModelo ideVemod) {
        this.ideVemod = ideVemod;
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
        hash += (ideVeveh != null ? ideVeveh.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehVehiculo)) {
            return false;
        }
        VehVehiculo other = (VehVehiculo) object;
        if ((this.ideVeveh == null && other.ideVeveh != null) || (this.ideVeveh != null && !this.ideVeveh.equals(other.ideVeveh))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehVehiculo[ ideVeveh=" + ideVeveh + " ]";
    }
    
}
