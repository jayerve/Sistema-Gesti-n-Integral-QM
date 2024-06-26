/*
 * To change this template, choose Tools | Templates
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
@Table(name = "adq_factura", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AdqFactura.findAll", query = "SELECT a FROM AdqFactura a"),
    @NamedQuery(name = "AdqFactura.findByIdeAdfac", query = "SELECT a FROM AdqFactura a WHERE a.ideAdfac = :ideAdfac"),
    @NamedQuery(name = "AdqFactura.findByIdeAdsoc", query = "SELECT a FROM AdqFactura a WHERE a.ideAdsoc = :ideAdsoc"),
    @NamedQuery(name = "AdqFactura.findByNumFacturaAdfac", query = "SELECT a FROM AdqFactura a WHERE a.numFacturaAdfac = :numFacturaAdfac"),
    @NamedQuery(name = "AdqFactura.findByFechaFacturaAdfac", query = "SELECT a FROM AdqFactura a WHERE a.fechaFacturaAdfac = :fechaFacturaAdfac"),
    @NamedQuery(name = "AdqFactura.findByDetalleAdfac", query = "SELECT a FROM AdqFactura a WHERE a.detalleAdfac = :detalleAdfac"),
    @NamedQuery(name = "AdqFactura.findByValorDescuentoAdfac", query = "SELECT a FROM AdqFactura a WHERE a.valorDescuentoAdfac = :valorDescuentoAdfac"),
    @NamedQuery(name = "AdqFactura.findBySubtotalAdfac", query = "SELECT a FROM AdqFactura a WHERE a.subtotalAdfac = :subtotalAdfac"),
    @NamedQuery(name = "AdqFactura.findByTotalAdfac", query = "SELECT a FROM AdqFactura a WHERE a.totalAdfac = :totalAdfac"),
    @NamedQuery(name = "AdqFactura.findByValorIvaAdfac", query = "SELECT a FROM AdqFactura a WHERE a.valorIvaAdfac = :valorIvaAdfac"),
    @NamedQuery(name = "AdqFactura.findByBaseIvaAdfac", query = "SELECT a FROM AdqFactura a WHERE a.baseIvaAdfac = :baseIvaAdfac"),
    @NamedQuery(name = "AdqFactura.findByActivoAdfac", query = "SELECT a FROM AdqFactura a WHERE a.activoAdfac = :activoAdfac"),
    @NamedQuery(name = "AdqFactura.findByUsuarioIngre", query = "SELECT a FROM AdqFactura a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AdqFactura.findByFechaIngre", query = "SELECT a FROM AdqFactura a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AdqFactura.findByHoraIngre", query = "SELECT a FROM AdqFactura a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AdqFactura.findByUsuarioActua", query = "SELECT a FROM AdqFactura a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AdqFactura.findByFechaActua", query = "SELECT a FROM AdqFactura a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AdqFactura.findByHoraActua", query = "SELECT a FROM AdqFactura a WHERE a.horaActua = :horaActua")})
public class AdqFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_adfac", nullable = false)
    private Long ideAdfac;
    @Column(name = "ide_adsoc")
    private BigInteger ideAdsoc;
    @Size(max = 20)
    @Column(name = "num_factura_adfac", length = 20)
    private String numFacturaAdfac;
    @Column(name = "fecha_factura_adfac")
    @Temporal(TemporalType.DATE)
    private Date fechaFacturaAdfac;
    @Size(max = 2147483647)
    @Column(name = "detalle_adfac", length = 2147483647)
    private String detalleAdfac;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_descuento_adfac", precision = 10, scale = 2)
    private BigDecimal valorDescuentoAdfac;
    @Column(name = "subtotal_adfac", precision = 10, scale = 2)
    private BigDecimal subtotalAdfac;
    @Column(name = "total_adfac", precision = 10, scale = 2)
    private BigDecimal totalAdfac;
    @Column(name = "valor_iva_adfac", precision = 10, scale = 2)
    private BigDecimal valorIvaAdfac;
    @Column(name = "base_iva_adfac", precision = 10, scale = 2)
    private BigDecimal baseIvaAdfac;
    @Column(name = "activo_adfac")
    private Boolean activoAdfac;
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
    /*@OneToMany(mappedBy = "ideAdfac")
    private List<TesProveedor> tesProveedorList;
*/
    @OneToMany(mappedBy = "ideAdfac")
    private List<AdqDetalleFactura> adqDetalleFacturaList;

    public AdqFactura() {
    }

    public AdqFactura(Long ideAdfac) {
        this.ideAdfac = ideAdfac;
    }

    public Long getIdeAdfac() {
        return ideAdfac;
    }

    public void setIdeAdfac(Long ideAdfac) {
        this.ideAdfac = ideAdfac;
    }

    public BigInteger getIdeAdsoc() {
        return ideAdsoc;
    }

    public void setIdeAdsoc(BigInteger ideAdsoc) {
        this.ideAdsoc = ideAdsoc;
    }

    public String getNumFacturaAdfac() {
        return numFacturaAdfac;
    }

    public void setNumFacturaAdfac(String numFacturaAdfac) {
        this.numFacturaAdfac = numFacturaAdfac;
    }

    public Date getFechaFacturaAdfac() {
        return fechaFacturaAdfac;
    }

    public void setFechaFacturaAdfac(Date fechaFacturaAdfac) {
        this.fechaFacturaAdfac = fechaFacturaAdfac;
    }

    public String getDetalleAdfac() {
        return detalleAdfac;
    }

    public void setDetalleAdfac(String detalleAdfac) {
        this.detalleAdfac = detalleAdfac;
    }

    public BigDecimal getValorDescuentoAdfac() {
        return valorDescuentoAdfac;
    }

    public void setValorDescuentoAdfac(BigDecimal valorDescuentoAdfac) {
        this.valorDescuentoAdfac = valorDescuentoAdfac;
    }

    public BigDecimal getSubtotalAdfac() {
        return subtotalAdfac;
    }

    public void setSubtotalAdfac(BigDecimal subtotalAdfac) {
        this.subtotalAdfac = subtotalAdfac;
    }

    public BigDecimal getTotalAdfac() {
        return totalAdfac;
    }

    public void setTotalAdfac(BigDecimal totalAdfac) {
        this.totalAdfac = totalAdfac;
    }

    public BigDecimal getValorIvaAdfac() {
        return valorIvaAdfac;
    }

    public void setValorIvaAdfac(BigDecimal valorIvaAdfac) {
        this.valorIvaAdfac = valorIvaAdfac;
    }

    public BigDecimal getBaseIvaAdfac() {
        return baseIvaAdfac;
    }

    public void setBaseIvaAdfac(BigDecimal baseIvaAdfac) {
        this.baseIvaAdfac = baseIvaAdfac;
    }

    public Boolean getActivoAdfac() {
        return activoAdfac;
    }

    public void setActivoAdfac(Boolean activoAdfac) {
        this.activoAdfac = activoAdfac;
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
/*
    public List<TesProveedor> getTesProveedorList() {
        return tesProveedorList;
    }

    public void setTesProveedorList(List<TesProveedor> tesProveedorList) {
        this.tesProveedorList = tesProveedorList;
    }
*/
    public List<AdqDetalleFactura> getAdqDetalleFacturaList() {
        return adqDetalleFacturaList;
    }

    public void setAdqDetalleFacturaList(List<AdqDetalleFactura> adqDetalleFacturaList) {
        this.adqDetalleFacturaList = adqDetalleFacturaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAdfac != null ? ideAdfac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdqFactura)) {
            return false;
        }
        AdqFactura other = (AdqFactura) object;
        if ((this.ideAdfac == null && other.ideAdfac != null) || (this.ideAdfac != null && !this.ideAdfac.equals(other.ideAdfac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AdqFactura[ ideAdfac=" + ideAdfac + " ]";
    }
    
}
