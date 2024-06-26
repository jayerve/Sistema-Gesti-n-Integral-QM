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
@Table(name = "adq_detalle_factura", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AdqDetalleFactura.findAll", query = "SELECT a FROM AdqDetalleFactura a"),
    @NamedQuery(name = "AdqDetalleFactura.findByIdeAddef", query = "SELECT a FROM AdqDetalleFactura a WHERE a.ideAddef = :ideAddef"),
    @NamedQuery(name = "AdqDetalleFactura.findByValorUnitarioAddef", query = "SELECT a FROM AdqDetalleFactura a WHERE a.valorUnitarioAddef = :valorUnitarioAddef"),
    @NamedQuery(name = "AdqDetalleFactura.findByCantidadAddef", query = "SELECT a FROM AdqDetalleFactura a WHERE a.cantidadAddef = :cantidadAddef"),
    @NamedQuery(name = "AdqDetalleFactura.findByValorTotalAddef", query = "SELECT a FROM AdqDetalleFactura a WHERE a.valorTotalAddef = :valorTotalAddef"),
    @NamedQuery(name = "AdqDetalleFactura.findByAplicaIvaAddef", query = "SELECT a FROM AdqDetalleFactura a WHERE a.aplicaIvaAddef = :aplicaIvaAddef"),
    @NamedQuery(name = "AdqDetalleFactura.findByActivoAddef", query = "SELECT a FROM AdqDetalleFactura a WHERE a.activoAddef = :activoAddef"),
    @NamedQuery(name = "AdqDetalleFactura.findByUsuarioIngre", query = "SELECT a FROM AdqDetalleFactura a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AdqDetalleFactura.findByFechaIngre", query = "SELECT a FROM AdqDetalleFactura a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AdqDetalleFactura.findByHoraIngre", query = "SELECT a FROM AdqDetalleFactura a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AdqDetalleFactura.findByUsuarioActua", query = "SELECT a FROM AdqDetalleFactura a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AdqDetalleFactura.findByFechaActua", query = "SELECT a FROM AdqDetalleFactura a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AdqDetalleFactura.findByHoraActua", query = "SELECT a FROM AdqDetalleFactura a WHERE a.horaActua = :horaActua")})
public class AdqDetalleFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_addef", nullable = false)
    private Long ideAddef;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_unitario_addef", precision = 10, scale = 2)
    private BigDecimal valorUnitarioAddef;
    @Column(name = "cantidad_addef", precision = 10, scale = 2)
    private BigDecimal cantidadAddef;
    @Column(name = "valor_total_addef", precision = 10, scale = 2)
    private BigDecimal valorTotalAddef;
    @Column(name = "aplica_iva_addef")
    private Boolean aplicaIvaAddef;
    @Column(name = "activo_addef")
    private Boolean activoAddef;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @JoinColumn(name = "ide_bomat", referencedColumnName = "ide_bomat")
    @ManyToOne
    private BodtMaterial ideBomat;
    @JoinColumn(name = "ide_adfac", referencedColumnName = "ide_adfac")
    @ManyToOne
    private AdqFactura ideAdfac;

    public AdqDetalleFactura() {
    }

    public AdqDetalleFactura(Long ideAddef) {
        this.ideAddef = ideAddef;
    }

    public Long getIdeAddef() {
        return ideAddef;
    }

    public void setIdeAddef(Long ideAddef) {
        this.ideAddef = ideAddef;
    }

    public BigDecimal getValorUnitarioAddef() {
        return valorUnitarioAddef;
    }

    public void setValorUnitarioAddef(BigDecimal valorUnitarioAddef) {
        this.valorUnitarioAddef = valorUnitarioAddef;
    }

    public BigDecimal getCantidadAddef() {
        return cantidadAddef;
    }

    public void setCantidadAddef(BigDecimal cantidadAddef) {
        this.cantidadAddef = cantidadAddef;
    }

    public BigDecimal getValorTotalAddef() {
        return valorTotalAddef;
    }

    public void setValorTotalAddef(BigDecimal valorTotalAddef) {
        this.valorTotalAddef = valorTotalAddef;
    }

    public Boolean getAplicaIvaAddef() {
        return aplicaIvaAddef;
    }

    public void setAplicaIvaAddef(Boolean aplicaIvaAddef) {
        this.aplicaIvaAddef = aplicaIvaAddef;
    }

    public Boolean getActivoAddef() {
        return activoAddef;
    }

    public void setActivoAddef(Boolean activoAddef) {
        this.activoAddef = activoAddef;
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

    public BodtMaterial getIdeBomat() {
        return ideBomat;
    }

    public void setIdeBomat(BodtMaterial ideBomat) {
        this.ideBomat = ideBomat;
    }

    public AdqFactura getIdeAdfac() {
        return ideAdfac;
    }

    public void setIdeAdfac(AdqFactura ideAdfac) {
        this.ideAdfac = ideAdfac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAddef != null ? ideAddef.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdqDetalleFactura)) {
            return false;
        }
        AdqDetalleFactura other = (AdqDetalleFactura) object;
        if ((this.ideAddef == null && other.ideAddef != null) || (this.ideAddef != null && !this.ideAddef.equals(other.ideAddef))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AdqDetalleFactura[ ideAddef=" + ideAddef + " ]";
    }
    
}
