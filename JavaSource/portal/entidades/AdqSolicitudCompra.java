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
@Table(name = "adq_solicitud_compra", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AdqSolicitudCompra.findAll", query = "SELECT a FROM AdqSolicitudCompra a"),
    @NamedQuery(name = "AdqSolicitudCompra.findByIdeAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.ideAdsoc = :ideAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByDetalleAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.detalleAdsoc = :detalleAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByNroSolicitudAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.nroSolicitudAdsoc = :nroSolicitudAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByFechaSolicitudAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.fechaSolicitudAdsoc = :fechaSolicitudAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByValorAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.valorAdsoc = :valorAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByAprobadoAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.aprobadoAdsoc = :aprobadoAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByFechaAdjudicacionAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.fechaAdjudicacionAdsoc = :fechaAdjudicacionAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByProformaProveedorAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.proformaProveedorAdsoc = :proformaProveedorAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByFechaProformaProveedorAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.fechaProformaProveedorAdsoc = :fechaProformaProveedorAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByFechaProforma2Adsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.fechaProforma2Adsoc = :fechaProforma2Adsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByValorProforma2Adsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.valorProforma2Adsoc = :valorProforma2Adsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByFacturaProforma2Adsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.facturaProforma2Adsoc = :facturaProforma2Adsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByOferente2Adsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.oferente2Adsoc = :oferente2Adsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByFechaProforma1Adsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.fechaProforma1Adsoc = :fechaProforma1Adsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByOferente1Adsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.oferente1Adsoc = :oferente1Adsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByFacturaProforma1Adsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.facturaProforma1Adsoc = :facturaProforma1Adsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByValorProforma1Adsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.valorProforma1Adsoc = :valorProforma1Adsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByActivoAdsoc", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.activoAdsoc = :activoAdsoc"),
    @NamedQuery(name = "AdqSolicitudCompra.findByUsuarioIngre", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AdqSolicitudCompra.findByFechaIngre", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AdqSolicitudCompra.findByHoraIngre", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AdqSolicitudCompra.findByUsuarioActua", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AdqSolicitudCompra.findByFechaActua", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AdqSolicitudCompra.findByHoraActua", query = "SELECT a FROM AdqSolicitudCompra a WHERE a.horaActua = :horaActua")})
public class AdqSolicitudCompra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_adsoc", nullable = false)
    private Long ideAdsoc;
    @Size(max = 2147483647)
    @Column(name = "detalle_adsoc", length = 2147483647)
    private String detalleAdsoc;
    @Size(max = 20)
    @Column(name = "nro_solicitud_adsoc", length = 20)
    private String nroSolicitudAdsoc;
    @Column(name = "fecha_solicitud_adsoc")
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitudAdsoc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_adsoc", precision = 10, scale = 2)
    private BigDecimal valorAdsoc;
    @Column(name = "aprobado_adsoc")
    private Boolean aprobadoAdsoc;
    @Column(name = "fecha_adjudicacion_adsoc")
    @Temporal(TemporalType.DATE)
    private Date fechaAdjudicacionAdsoc;
    @Size(max = 20)
    @Column(name = "proforma_proveedor_adsoc", length = 20)
    private String proformaProveedorAdsoc;
    @Column(name = "fecha_proforma_proveedor_adsoc")
    @Temporal(TemporalType.DATE)
    private Date fechaProformaProveedorAdsoc;
    @Column(name = "fecha_proforma2_adsoc")
    @Temporal(TemporalType.DATE)
    private Date fechaProforma2Adsoc;
    @Column(name = "valor_proforma2_adsoc", precision = 10, scale = 2)
    private BigDecimal valorProforma2Adsoc;
    @Size(max = 20)
    @Column(name = "factura_proforma2_adsoc", length = 20)
    private String facturaProforma2Adsoc;
    @Size(max = 20)
    @Column(name = "oferente2_adsoc", length = 20)
    private String oferente2Adsoc;
    @Column(name = "fecha_proforma1_adsoc")
    @Temporal(TemporalType.DATE)
    private Date fechaProforma1Adsoc;
    @Size(max = 20)
    @Column(name = "oferente1_adsoc", length = 20)
    private String oferente1Adsoc;
    @Size(max = 20)
    @Column(name = "factura_proforma1_adsoc", length = 20)
    private String facturaProforma1Adsoc;
    @Column(name = "valor_proforma1_adsoc", precision = 10, scale = 2)
    private BigDecimal valorProforma1Adsoc;
    @Column(name = "activo_adsoc")
    private Boolean activoAdsoc;
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
    @OneToMany(mappedBy = "ideAdsoc")
    private List<TesComprobantePago> tesComprobantePagoList;
    @JoinColumn(name = "ide_tepro", referencedColumnName = "ide_tepro")
    @ManyToOne
    private TesProveedor ideTepro;
    @JoinColumn(name = "ide_prtra", referencedColumnName = "ide_prtra")
    @ManyToOne
    private PreTramite idePrtra;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_copag", referencedColumnName = "ide_copag")
    @ManyToOne
    private ContParametrosGeneral ideCopag;

    public AdqSolicitudCompra() {
    }

    public AdqSolicitudCompra(Long ideAdsoc) {
        this.ideAdsoc = ideAdsoc;
    }

    public Long getIdeAdsoc() {
        return ideAdsoc;
    }

    public void setIdeAdsoc(Long ideAdsoc) {
        this.ideAdsoc = ideAdsoc;
    }

    public String getDetalleAdsoc() {
        return detalleAdsoc;
    }

    public void setDetalleAdsoc(String detalleAdsoc) {
        this.detalleAdsoc = detalleAdsoc;
    }

    public String getNroSolicitudAdsoc() {
        return nroSolicitudAdsoc;
    }

    public void setNroSolicitudAdsoc(String nroSolicitudAdsoc) {
        this.nroSolicitudAdsoc = nroSolicitudAdsoc;
    }

    public Date getFechaSolicitudAdsoc() {
        return fechaSolicitudAdsoc;
    }

    public void setFechaSolicitudAdsoc(Date fechaSolicitudAdsoc) {
        this.fechaSolicitudAdsoc = fechaSolicitudAdsoc;
    }

    public BigDecimal getValorAdsoc() {
        return valorAdsoc;
    }

    public void setValorAdsoc(BigDecimal valorAdsoc) {
        this.valorAdsoc = valorAdsoc;
    }

    public Boolean getAprobadoAdsoc() {
        return aprobadoAdsoc;
    }

    public void setAprobadoAdsoc(Boolean aprobadoAdsoc) {
        this.aprobadoAdsoc = aprobadoAdsoc;
    }

    public Date getFechaAdjudicacionAdsoc() {
        return fechaAdjudicacionAdsoc;
    }

    public void setFechaAdjudicacionAdsoc(Date fechaAdjudicacionAdsoc) {
        this.fechaAdjudicacionAdsoc = fechaAdjudicacionAdsoc;
    }

    public String getProformaProveedorAdsoc() {
        return proformaProveedorAdsoc;
    }

    public void setProformaProveedorAdsoc(String proformaProveedorAdsoc) {
        this.proformaProveedorAdsoc = proformaProveedorAdsoc;
    }

    public Date getFechaProformaProveedorAdsoc() {
        return fechaProformaProveedorAdsoc;
    }

    public void setFechaProformaProveedorAdsoc(Date fechaProformaProveedorAdsoc) {
        this.fechaProformaProveedorAdsoc = fechaProformaProveedorAdsoc;
    }

    public Date getFechaProforma2Adsoc() {
        return fechaProforma2Adsoc;
    }

    public void setFechaProforma2Adsoc(Date fechaProforma2Adsoc) {
        this.fechaProforma2Adsoc = fechaProforma2Adsoc;
    }

    public BigDecimal getValorProforma2Adsoc() {
        return valorProforma2Adsoc;
    }

    public void setValorProforma2Adsoc(BigDecimal valorProforma2Adsoc) {
        this.valorProforma2Adsoc = valorProforma2Adsoc;
    }

    public String getFacturaProforma2Adsoc() {
        return facturaProforma2Adsoc;
    }

    public void setFacturaProforma2Adsoc(String facturaProforma2Adsoc) {
        this.facturaProforma2Adsoc = facturaProforma2Adsoc;
    }

    public String getOferente2Adsoc() {
        return oferente2Adsoc;
    }

    public void setOferente2Adsoc(String oferente2Adsoc) {
        this.oferente2Adsoc = oferente2Adsoc;
    }

    public Date getFechaProforma1Adsoc() {
        return fechaProforma1Adsoc;
    }

    public void setFechaProforma1Adsoc(Date fechaProforma1Adsoc) {
        this.fechaProforma1Adsoc = fechaProforma1Adsoc;
    }

    public String getOferente1Adsoc() {
        return oferente1Adsoc;
    }

    public void setOferente1Adsoc(String oferente1Adsoc) {
        this.oferente1Adsoc = oferente1Adsoc;
    }

    public String getFacturaProforma1Adsoc() {
        return facturaProforma1Adsoc;
    }

    public void setFacturaProforma1Adsoc(String facturaProforma1Adsoc) {
        this.facturaProforma1Adsoc = facturaProforma1Adsoc;
    }

    public BigDecimal getValorProforma1Adsoc() {
        return valorProforma1Adsoc;
    }

    public void setValorProforma1Adsoc(BigDecimal valorProforma1Adsoc) {
        this.valorProforma1Adsoc = valorProforma1Adsoc;
    }

    public Boolean getActivoAdsoc() {
        return activoAdsoc;
    }

    public void setActivoAdsoc(Boolean activoAdsoc) {
        this.activoAdsoc = activoAdsoc;
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

    public List<TesComprobantePago> getTesComprobantePagoList() {
        return tesComprobantePagoList;
    }

    public void setTesComprobantePagoList(List<TesComprobantePago> tesComprobantePagoList) {
        this.tesComprobantePagoList = tesComprobantePagoList;
    }

    public TesProveedor getIdeTepro() {
        return ideTepro;
    }

    public void setIdeTepro(TesProveedor ideTepro) {
        this.ideTepro = ideTepro;
    }

    public PreTramite getIdePrtra() {
        return idePrtra;
    }

    public void setIdePrtra(PreTramite idePrtra) {
        this.idePrtra = idePrtra;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public ContParametrosGeneral getIdeCopag() {
        return ideCopag;
    }

    public void setIdeCopag(ContParametrosGeneral ideCopag) {
        this.ideCopag = ideCopag;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAdsoc != null ? ideAdsoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdqSolicitudCompra)) {
            return false;
        }
        AdqSolicitudCompra other = (AdqSolicitudCompra) object;
        if ((this.ideAdsoc == null && other.ideAdsoc != null) || (this.ideAdsoc != null && !this.ideAdsoc.equals(other.ideAdsoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AdqSolicitudCompra[ ideAdsoc=" + ideAdsoc + " ]";
    }
    
}
