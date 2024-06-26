/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "nrh_capacidad_pago", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhCapacidadPago.findAll", query = "SELECT n FROM NrhCapacidadPago n"),
    @NamedQuery(name = "NrhCapacidadPago.findByIdeNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.ideNrcap = :ideNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByFechaCalculoNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.fechaCalculoNrcap = :fechaCalculoNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByTotalIngresoNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.totalIngresoNrcap = :totalIngresoNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByTotalEgresoNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.totalEgresoNrcap = :totalEgresoNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByNroMesNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.nroMesNrcap = :nroMesNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByMontoRecibirNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.montoRecibirNrcap = :montoRecibirNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByCuotaMensualNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.cuotaMensualNrcap = :cuotaMensualNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByPorcentajeEndeudaNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.porcentajeEndeudaNrcap = :porcentajeEndeudaNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByCuotaLimiteNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.cuotaLimiteNrcap = :cuotaLimiteNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByActivoNrcap", query = "SELECT n FROM NrhCapacidadPago n WHERE n.activoNrcap = :activoNrcap"),
    @NamedQuery(name = "NrhCapacidadPago.findByUsuarioIngre", query = "SELECT n FROM NrhCapacidadPago n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhCapacidadPago.findByFechaIngre", query = "SELECT n FROM NrhCapacidadPago n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhCapacidadPago.findByUsuarioActua", query = "SELECT n FROM NrhCapacidadPago n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhCapacidadPago.findByFechaActua", query = "SELECT n FROM NrhCapacidadPago n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhCapacidadPago.findByHoraIngre", query = "SELECT n FROM NrhCapacidadPago n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhCapacidadPago.findByHoraActua", query = "SELECT n FROM NrhCapacidadPago n WHERE n.horaActua = :horaActua")})
public class NrhCapacidadPago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrcap", nullable = false)
    private Integer ideNrcap;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_calculo_nrcap", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCalculoNrcap;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_ingreso_nrcap", nullable = false, precision = 12, scale = 3)
    private BigDecimal totalIngresoNrcap;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_egreso_nrcap", nullable = false, precision = 12, scale = 3)
    private BigDecimal totalEgresoNrcap;
    @Column(name = "nro_mes_nrcap")
    private Integer nroMesNrcap;
    @Column(name = "monto_recibir_nrcap", precision = 12, scale = 3)
    private BigDecimal montoRecibirNrcap;
    @Column(name = "cuota_mensual_nrcap", precision = 12, scale = 3)
    private BigDecimal cuotaMensualNrcap;
    @Column(name = "porcentaje_endeuda_nrcap", precision = 5, scale = 2)
    private BigDecimal porcentajeEndeudaNrcap;
    @Column(name = "cuota_limite_nrcap", precision = 12, scale = 3)
    private BigDecimal cuotaLimiteNrcap;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_nrcap", nullable = false)
    private boolean activoNrcap;
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
    @JoinColumn(name = "ide_nrant", referencedColumnName = "ide_nrant", nullable = false)
    @ManyToOne(optional = false)
    private NrhAnticipo ideNrant;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrcap")
    private List<NrhRubroDetallePago> nrhRubroDetallePagoList;

    public NrhCapacidadPago() {
    }

    public NrhCapacidadPago(Integer ideNrcap) {
        this.ideNrcap = ideNrcap;
    }

    public NrhCapacidadPago(Integer ideNrcap, Date fechaCalculoNrcap, BigDecimal totalIngresoNrcap, BigDecimal totalEgresoNrcap, boolean activoNrcap) {
        this.ideNrcap = ideNrcap;
        this.fechaCalculoNrcap = fechaCalculoNrcap;
        this.totalIngresoNrcap = totalIngresoNrcap;
        this.totalEgresoNrcap = totalEgresoNrcap;
        this.activoNrcap = activoNrcap;
    }

    public Integer getIdeNrcap() {
        return ideNrcap;
    }

    public void setIdeNrcap(Integer ideNrcap) {
        this.ideNrcap = ideNrcap;
    }

    public Date getFechaCalculoNrcap() {
        return fechaCalculoNrcap;
    }

    public void setFechaCalculoNrcap(Date fechaCalculoNrcap) {
        this.fechaCalculoNrcap = fechaCalculoNrcap;
    }

    public BigDecimal getTotalIngresoNrcap() {
        return totalIngresoNrcap;
    }

    public void setTotalIngresoNrcap(BigDecimal totalIngresoNrcap) {
        this.totalIngresoNrcap = totalIngresoNrcap;
    }

    public BigDecimal getTotalEgresoNrcap() {
        return totalEgresoNrcap;
    }

    public void setTotalEgresoNrcap(BigDecimal totalEgresoNrcap) {
        this.totalEgresoNrcap = totalEgresoNrcap;
    }

    public Integer getNroMesNrcap() {
        return nroMesNrcap;
    }

    public void setNroMesNrcap(Integer nroMesNrcap) {
        this.nroMesNrcap = nroMesNrcap;
    }

    public BigDecimal getMontoRecibirNrcap() {
        return montoRecibirNrcap;
    }

    public void setMontoRecibirNrcap(BigDecimal montoRecibirNrcap) {
        this.montoRecibirNrcap = montoRecibirNrcap;
    }

    public BigDecimal getCuotaMensualNrcap() {
        return cuotaMensualNrcap;
    }

    public void setCuotaMensualNrcap(BigDecimal cuotaMensualNrcap) {
        this.cuotaMensualNrcap = cuotaMensualNrcap;
    }

    public BigDecimal getPorcentajeEndeudaNrcap() {
        return porcentajeEndeudaNrcap;
    }

    public void setPorcentajeEndeudaNrcap(BigDecimal porcentajeEndeudaNrcap) {
        this.porcentajeEndeudaNrcap = porcentajeEndeudaNrcap;
    }

    public BigDecimal getCuotaLimiteNrcap() {
        return cuotaLimiteNrcap;
    }

    public void setCuotaLimiteNrcap(BigDecimal cuotaLimiteNrcap) {
        this.cuotaLimiteNrcap = cuotaLimiteNrcap;
    }

    public boolean getActivoNrcap() {
        return activoNrcap;
    }

    public void setActivoNrcap(boolean activoNrcap) {
        this.activoNrcap = activoNrcap;
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

    public NrhAnticipo getIdeNrant() {
        return ideNrant;
    }

    public void setIdeNrant(NrhAnticipo ideNrant) {
        this.ideNrant = ideNrant;
    }

    public List<NrhRubroDetallePago> getNrhRubroDetallePagoList() {
        return nrhRubroDetallePagoList;
    }

    public void setNrhRubroDetallePagoList(List<NrhRubroDetallePago> nrhRubroDetallePagoList) {
        this.nrhRubroDetallePagoList = nrhRubroDetallePagoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrcap != null ? ideNrcap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhCapacidadPago)) {
            return false;
        }
        NrhCapacidadPago other = (NrhCapacidadPago) object;
        if ((this.ideNrcap == null && other.ideNrcap != null) || (this.ideNrcap != null && !this.ideNrcap.equals(other.ideNrcap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhCapacidadPago[ ideNrcap=" + ideNrcap + " ]";
    }
    
}
