/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "veh_solicitud")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VehSolicitud.findAll", query = "SELECT v FROM VehSolicitud v"),
    @NamedQuery(name = "VehSolicitud.findByIdeVesol", query = "SELECT v FROM VehSolicitud v WHERE v.ideVesol = :ideVesol"),
    @NamedQuery(name = "VehSolicitud.findByDetalleVesol", query = "SELECT v FROM VehSolicitud v WHERE v.detalleVesol = :detalleVesol"),
    @NamedQuery(name = "VehSolicitud.findByActivoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.activoVesol = :activoVesol"),
    @NamedQuery(name = "VehSolicitud.findByUsuarioIngre", query = "SELECT v FROM VehSolicitud v WHERE v.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "VehSolicitud.findByFechaIngre", query = "SELECT v FROM VehSolicitud v WHERE v.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "VehSolicitud.findByHoraIngre", query = "SELECT v FROM VehSolicitud v WHERE v.horaIngre = :horaIngre"),
    @NamedQuery(name = "VehSolicitud.findByUsuarioActua", query = "SELECT v FROM VehSolicitud v WHERE v.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "VehSolicitud.findByFechaActua", query = "SELECT v FROM VehSolicitud v WHERE v.fechaActua = :fechaActua"),
    @NamedQuery(name = "VehSolicitud.findByHoraActua", query = "SELECT v FROM VehSolicitud v WHERE v.horaActua = :horaActua"),
    @NamedQuery(name = "VehSolicitud.findByFechaSolicitudVesol", query = "SELECT v FROM VehSolicitud v WHERE v.fechaSolicitudVesol = :fechaSolicitudVesol"),
    @NamedQuery(name = "VehSolicitud.findByObservacionesVesol", query = "SELECT v FROM VehSolicitud v WHERE v.observacionesVesol = :observacionesVesol"),
    @NamedQuery(name = "VehSolicitud.findByMotivoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.motivoVesol = :motivoVesol"),
    @NamedQuery(name = "VehSolicitud.findByFechaSalidaVesol", query = "SELECT v FROM VehSolicitud v WHERE v.fechaSalidaVesol = :fechaSalidaVesol"),
    @NamedQuery(name = "VehSolicitud.findByHoraSalidaVesol", query = "SELECT v FROM VehSolicitud v WHERE v.horaSalidaVesol = :horaSalidaVesol"),
    @NamedQuery(name = "VehSolicitud.findByHoraRetornoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.horaRetornoVesol = :horaRetornoVesol"),
    @NamedQuery(name = "VehSolicitud.findByExtContactoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.extContactoVesol = :extContactoVesol"),
    @NamedQuery(name = "VehSolicitud.findByPrioridad", query = "SELECT v FROM VehSolicitud v WHERE v.prioridad = :prioridad"),
    @NamedQuery(name = "VehSolicitud.findByTiempoSolicitadoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.tiempoSolicitadoVesol = :tiempoSolicitadoVesol"),
    @NamedQuery(name = "VehSolicitud.findByGenIdeGtemp", query = "SELECT v FROM VehSolicitud v WHERE v.genIdeGtemp = :genIdeGtemp"),
    @NamedQuery(name = "VehSolicitud.findByDepartamentoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.departamentoVesol = :departamentoVesol"),
    @NamedQuery(name = "VehSolicitud.findByOcupantesVesol", query = "SELECT v FROM VehSolicitud v WHERE v.ocupantesVesol = :ocupantesVesol"),
    @NamedQuery(name = "VehSolicitud.findByFechaRegresoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.fechaRegresoVesol = :fechaRegresoVesol"),
    @NamedQuery(name = "VehSolicitud.findByDiasSolicitadoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.diasSolicitadoVesol = :diasSolicitadoVesol"),
    @NamedQuery(name = "VehSolicitud.findByTipoCalculoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.tipoCalculoVesol = :tipoCalculoVesol"),
    @NamedQuery(name = "VehSolicitud.findByLlevaCargaVesol", query = "SELECT v FROM VehSolicitud v WHERE v.llevaCargaVesol = :llevaCargaVesol"),
    @NamedQuery(name = "VehSolicitud.findByIdeVetip", query = "SELECT v FROM VehSolicitud v WHERE v.ideVetip = :ideVetip"),
    @NamedQuery(name = "VehSolicitud.findByIdeSucu", query = "SELECT v FROM VehSolicitud v WHERE v.ideSucu = :ideSucu"),
    @NamedQuery(name = "VehSolicitud.findByAprobadoVesol", query = "SELECT v FROM VehSolicitud v WHERE v.aprobadoVesol = :aprobadoVesol")})
public class VehSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_vesol")
    private Long ideVesol;
    @Column(name = "detalle_vesol")
    private String detalleVesol;
    @Column(name = "activo_vesol")
    private Boolean activoVesol;
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
    @Column(name = "fecha_solicitud_vesol")
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitudVesol;
    @Column(name = "observaciones_vesol")
    private String observacionesVesol;
    @Column(name = "motivo_vesol")
    private String motivoVesol;
    @Column(name = "fecha_salida_vesol")
    @Temporal(TemporalType.DATE)
    private Date fechaSalidaVesol;
    @Column(name = "hora_salida_vesol")
    @Temporal(TemporalType.TIME)
    private Date horaSalidaVesol;
    @Column(name = "hora_retorno_vesol")
    @Temporal(TemporalType.TIME)
    private Date horaRetornoVesol;
    @Column(name = "ext_contacto_vesol")
    private String extContactoVesol;
    @Column(name = "prioridad")
    private BigInteger prioridad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "tiempo_solicitado_vesol")
    private BigDecimal tiempoSolicitadoVesol;
    @Column(name = "gen_ide_gtemp")
    private BigInteger genIdeGtemp;
    @Column(name = "departamento_vesol")
    private String departamentoVesol;
    @Column(name = "ocupantes_vesol")
    private Integer ocupantesVesol;
    @Column(name = "fecha_regreso_vesol")
    @Temporal(TemporalType.DATE)
    private Date fechaRegresoVesol;
    @Column(name = "dias_solicitado_vesol")
    private Integer diasSolicitadoVesol;
    @Column(name = "tipo_calculo_vesol")
    private Integer tipoCalculoVesol;
    @Column(name = "lleva_carga_vesol")
    private Boolean llevaCargaVesol;
    @Column(name = "ide_vetip")
    private Long ideVetip;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "aprobado_vesol")
    private Boolean aprobadoVesol;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_vecon", referencedColumnName = "ide_vecon")
    @ManyToOne
    private VehConductor ideVecon;
    @JoinColumn(name = "ide_vetes", referencedColumnName = "ide_vetes")
    @ManyToOne
    private VehSolicitudEstado ideVetes;
    @JoinColumn(name = "ide_veveh", referencedColumnName = "ide_veveh")
    @ManyToOne
    private VehVehiculo ideVeveh;
    @OneToMany(mappedBy = "ideVesol")
    private List<VehSolicitudRuta> vehSolicitudRutaList;
    @OneToMany(mappedBy = "ideVesol")
    private List<VehSolicitudOcupante> vehSolicitudOcupanteList;
    @OneToMany(mappedBy = "ideVesol")
    private List<VehSolicitudArchivo> vehSolicitudArchivoList;

    public VehSolicitud() {
    	tiempoSolicitadoVesol=new BigDecimal("0");
    }

    public VehSolicitud(Long ideVesol) {
        this.ideVesol = ideVesol;
    }

    public Long getIdeVesol() {
        return ideVesol;
    }

    public void setIdeVesol(Long ideVesol) {
        this.ideVesol = ideVesol;
    }

    public String getDetalleVesol() {
        return detalleVesol;
    }

    public void setDetalleVesol(String detalleVesol) {
        this.detalleVesol = detalleVesol;
    }

    public Boolean getActivoVesol() {
        return activoVesol;
    }

    public void setActivoVesol(Boolean activoVesol) {
        this.activoVesol = activoVesol;
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

    public Date getFechaSolicitudVesol() {
        return fechaSolicitudVesol;
    }

    public void setFechaSolicitudVesol(Date fechaSolicitudVesol) {
        this.fechaSolicitudVesol = fechaSolicitudVesol;
    }

    public String getObservacionesVesol() {
        return observacionesVesol;
    }

    public void setObservacionesVesol(String observacionesVesol) {
        this.observacionesVesol = observacionesVesol;
    }

    public String getMotivoVesol() {
        return motivoVesol;
    }

    public void setMotivoVesol(String motivoVesol) {
        this.motivoVesol = motivoVesol;
        System.out.println("motivoVesol: "+motivoVesol);
    }

    public Date getFechaSalidaVesol() {
        return fechaSalidaVesol;
    }

    public void setFechaSalidaVesol(Date fechaSalidaVesol) {
        this.fechaSalidaVesol = fechaSalidaVesol;
    }

    public Date getHoraSalidaVesol() {
        return horaSalidaVesol;
    }

    public void setHoraSalidaVesol(Date horaSalidaVesol) {
        this.horaSalidaVesol = horaSalidaVesol;
    }

    public Date getHoraRetornoVesol() {
        return horaRetornoVesol;
    }

    public void setHoraRetornoVesol(Date horaRetornoVesol) {
        this.horaRetornoVesol = horaRetornoVesol;
    }

    public String getExtContactoVesol() {
        return extContactoVesol;
    }

    public void setExtContactoVesol(String extContactoVesol) {
        this.extContactoVesol = extContactoVesol;
    }

    public BigInteger getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(BigInteger prioridad) {
        this.prioridad = prioridad;
    }

    public BigDecimal getTiempoSolicitadoVesol() {
        return tiempoSolicitadoVesol;
    }

    public void setTiempoSolicitadoVesol(BigDecimal tiempoSolicitadoVesol) {
        this.tiempoSolicitadoVesol = tiempoSolicitadoVesol;
    }

    public BigInteger getGenIdeGtemp() {
        return genIdeGtemp;
    }

    public void setGenIdeGtemp(BigInteger genIdeGtemp) {
        this.genIdeGtemp = genIdeGtemp;
    }

    public String getDepartamentoVesol() {
        return departamentoVesol;
    }

    public void setDepartamentoVesol(String departamentoVesol) {
        this.departamentoVesol = departamentoVesol;
    }

    public Integer getOcupantesVesol() {
        return ocupantesVesol;
    }

    public void setOcupantesVesol(Integer ocupantesVesol) {
        this.ocupantesVesol = ocupantesVesol;
    }

    public Date getFechaRegresoVesol() {
        return fechaRegresoVesol;
    }

    public void setFechaRegresoVesol(Date fechaRegresoVesol) {
        this.fechaRegresoVesol = fechaRegresoVesol;
    }

    public Integer getDiasSolicitadoVesol() {
        return diasSolicitadoVesol;
    }

    public void setDiasSolicitadoVesol(Integer diasSolicitadoVesol) {
        this.diasSolicitadoVesol = diasSolicitadoVesol;
    }

    public Integer getTipoCalculoVesol() {
        return tipoCalculoVesol;
    }

    public void setTipoCalculoVesol(Integer tipoCalculoVesol) {
        this.tipoCalculoVesol = tipoCalculoVesol;
    }

    public Boolean getLlevaCargaVesol() {
        return llevaCargaVesol;
    }

    public void setLlevaCargaVesol(Boolean llevaCargaVesol) {
        this.llevaCargaVesol = llevaCargaVesol;
    }

    public Long getIdeVetip() {
        return ideVetip;
    }

    public void setIdeVetip(Long ideVetip) {
        this.ideVetip = ideVetip;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Boolean getAprobadoVesol() {
        return aprobadoVesol;
    }

    public void setAprobadoVesol(Boolean aprobadoVesol) {
        this.aprobadoVesol = aprobadoVesol;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp() {
        return genIdeGeedp;
    }

    public void setGenIdeGeedp(GenEmpleadosDepartamentoPar genIdeGeedp) {
        this.genIdeGeedp = genIdeGeedp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public VehConductor getIdeVecon() {
        return ideVecon;
    }

    public void setIdeVecon(VehConductor ideVecon) {
        this.ideVecon = ideVecon;
    }

    public VehSolicitudEstado getIdeVetes() {
        return ideVetes;
    }

    public void setIdeVetes(VehSolicitudEstado ideVetes) {
        this.ideVetes = ideVetes;
    }

    public VehVehiculo getIdeVeveh() {
        return ideVeveh;
    }

    public void setIdeVeveh(VehVehiculo ideVeveh) {
        this.ideVeveh = ideVeveh;
    }

    @XmlTransient
    public List<VehSolicitudRuta> getVehSolicitudRutaList() {
        return vehSolicitudRutaList;
    }

    public void setVehSolicitudRutaList(List<VehSolicitudRuta> vehSolicitudRutaList) {
        this.vehSolicitudRutaList = vehSolicitudRutaList;
    }

    @XmlTransient
    public List<VehSolicitudOcupante> getVehSolicitudOcupanteList() {
        return vehSolicitudOcupanteList;
    }

    public void setVehSolicitudOcupanteList(List<VehSolicitudOcupante> vehSolicitudOcupanteList) {
        this.vehSolicitudOcupanteList = vehSolicitudOcupanteList;
    }

    @XmlTransient
    public List<VehSolicitudArchivo> getVehSolicitudArchivoList() {
        return vehSolicitudArchivoList;
    }

    public void setVehSolicitudArchivoList(List<VehSolicitudArchivo> vehSolicitudArchivoList) {
        this.vehSolicitudArchivoList = vehSolicitudArchivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideVesol != null ? ideVesol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehSolicitud)) {
            return false;
        }
        VehSolicitud other = (VehSolicitud) object;
        if ((this.ideVesol == null && other.ideVesol != null) || (this.ideVesol != null && !this.ideVesol.equals(other.ideVesol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.VehSolicitud[ ideVesol=" + ideVesol + " ]";
    }
    
}
