/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "nrh_rubro_detalle_pago", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhRubroDetallePago.findAll", query = "SELECT n FROM NrhRubroDetallePago n"),
    @NamedQuery(name = "NrhRubroDetallePago.findByIdeNrrdp", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.ideNrrdp = :ideNrrdp"),
    @NamedQuery(name = "NrhRubroDetallePago.findByValorRubroNrrdp", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.valorRubroNrrdp = :valorRubroNrrdp"),
    @NamedQuery(name = "NrhRubroDetallePago.findByPorcentajeNrrdp", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.porcentajeNrrdp = :porcentajeNrrdp"),
    @NamedQuery(name = "NrhRubroDetallePago.findByValorDescuentoNrrdp", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.valorDescuentoNrrdp = :valorDescuentoNrrdp"),
    @NamedQuery(name = "NrhRubroDetallePago.findByFechaPagoRubroNrrdp", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.fechaPagoRubroNrrdp = :fechaPagoRubroNrrdp"),
    @NamedQuery(name = "NrhRubroDetallePago.findByActivoNrrdp", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.activoNrrdp = :activoNrrdp"),
    @NamedQuery(name = "NrhRubroDetallePago.findByUsuarioIngre", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRubroDetallePago.findByFechaIngre", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRubroDetallePago.findByUsuarioActua", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRubroDetallePago.findByFechaActua", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRubroDetallePago.findByHoraIngre", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRubroDetallePago.findByHoraActua", query = "SELECT n FROM NrhRubroDetallePago n WHERE n.horaActua = :horaActua")})
public class NrhRubroDetallePago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrrdp", nullable = false)
    private Integer ideNrrdp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_rubro_nrrdp", nullable = false, precision = 12, scale = 3)
    private BigDecimal valorRubroNrrdp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje_nrrdp", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeNrrdp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_descuento_nrrdp", nullable = false, precision = 12, scale = 3)
    private BigDecimal valorDescuentoNrrdp;
    @Column(name = "fecha_pago_rubro_nrrdp")
    @Temporal(TemporalType.DATE)
    private Date fechaPagoRubroNrrdp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_nrrdp", nullable = false)
    private boolean activoNrrdp;
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
    @JoinColumn(name = "ide_nrrub", referencedColumnName = "ide_nrrub", nullable = false)
    @ManyToOne(optional = false)
    private NrhRubro ideNrrub;
    @JoinColumn(name = "ide_nrcap", referencedColumnName = "ide_nrcap", nullable = false)
    @ManyToOne(optional = false)
    private NrhCapacidadPago ideNrcap;

    public NrhRubroDetallePago() {
    }

    public NrhRubroDetallePago(Integer ideNrrdp) {
        this.ideNrrdp = ideNrrdp;
    }

    public NrhRubroDetallePago(Integer ideNrrdp, BigDecimal valorRubroNrrdp, BigDecimal porcentajeNrrdp, BigDecimal valorDescuentoNrrdp, boolean activoNrrdp) {
        this.ideNrrdp = ideNrrdp;
        this.valorRubroNrrdp = valorRubroNrrdp;
        this.porcentajeNrrdp = porcentajeNrrdp;
        this.valorDescuentoNrrdp = valorDescuentoNrrdp;
        this.activoNrrdp = activoNrrdp;
    }

    public Integer getIdeNrrdp() {
        return ideNrrdp;
    }

    public void setIdeNrrdp(Integer ideNrrdp) {
        this.ideNrrdp = ideNrrdp;
    }

    public BigDecimal getValorRubroNrrdp() {
        return valorRubroNrrdp;
    }

    public void setValorRubroNrrdp(BigDecimal valorRubroNrrdp) {
        this.valorRubroNrrdp = valorRubroNrrdp;
    }

    public BigDecimal getPorcentajeNrrdp() {
        return porcentajeNrrdp;
    }

    public void setPorcentajeNrrdp(BigDecimal porcentajeNrrdp) {
        this.porcentajeNrrdp = porcentajeNrrdp;
    }

    public BigDecimal getValorDescuentoNrrdp() {
        return valorDescuentoNrrdp;
    }

    public void setValorDescuentoNrrdp(BigDecimal valorDescuentoNrrdp) {
        this.valorDescuentoNrrdp = valorDescuentoNrrdp;
    }

    public Date getFechaPagoRubroNrrdp() {
        return fechaPagoRubroNrrdp;
    }

    public void setFechaPagoRubroNrrdp(Date fechaPagoRubroNrrdp) {
        this.fechaPagoRubroNrrdp = fechaPagoRubroNrrdp;
    }

    public boolean getActivoNrrdp() {
        return activoNrrdp;
    }

    public void setActivoNrrdp(boolean activoNrrdp) {
        this.activoNrrdp = activoNrrdp;
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

    public NrhRubro getIdeNrrub() {
        return ideNrrub;
    }

    public void setIdeNrrub(NrhRubro ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public NrhCapacidadPago getIdeNrcap() {
        return ideNrcap;
    }

    public void setIdeNrcap(NrhCapacidadPago ideNrcap) {
        this.ideNrcap = ideNrcap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrrdp != null ? ideNrrdp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRubroDetallePago)) {
            return false;
        }
        NrhRubroDetallePago other = (NrhRubroDetallePago) object;
        if ((this.ideNrrdp == null && other.ideNrrdp != null) || (this.ideNrrdp != null && !this.ideNrrdp.equals(other.ideNrrdp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRubroDetallePago[ ideNrrdp=" + ideNrrdp + " ]";
    }
    
}
