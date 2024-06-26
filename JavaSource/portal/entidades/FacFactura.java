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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "fac_factura", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "FacFactura.findAll", query = "SELECT f FROM FacFactura f"),
    @NamedQuery(name = "FacFactura.findByIdeFafac", query = "SELECT f FROM FacFactura f WHERE f.ideFafac = :ideFafac"),
    @NamedQuery(name = "FacFactura.findByIdeComov", query = "SELECT f FROM FacFactura f WHERE f.ideComov = :ideComov"),
    @NamedQuery(name = "FacFactura.findByIdeSucu", query = "SELECT f FROM FacFactura f WHERE f.ideSucu = :ideSucu"),
    @NamedQuery(name = "FacFactura.findByFechaVencimientoFafac", query = "SELECT f FROM FacFactura f WHERE f.fechaVencimientoFafac = :fechaVencimientoFafac"),
    @NamedQuery(name = "FacFactura.findByFechaEmisionFafac", query = "SELECT f FROM FacFactura f WHERE f.fechaEmisionFafac = :fechaEmisionFafac"),
    @NamedQuery(name = "FacFactura.findByFechaTansaccionFafac", query = "SELECT f FROM FacFactura f WHERE f.fechaTansaccionFafac = :fechaTansaccionFafac"),
    @NamedQuery(name = "FacFactura.findBySecuencialFafac", query = "SELECT f FROM FacFactura f WHERE f.secuencialFafac = :secuencialFafac"),
    @NamedQuery(name = "FacFactura.findByDireccionFafac", query = "SELECT f FROM FacFactura f WHERE f.direccionFafac = :direccionFafac"),
    @NamedQuery(name = "FacFactura.findByObservacionFafac", query = "SELECT f FROM FacFactura f WHERE f.observacionFafac = :observacionFafac"),
    @NamedQuery(name = "FacFactura.findByBaseNoIvaFafac", query = "SELECT f FROM FacFactura f WHERE f.baseNoIvaFafac = :baseNoIvaFafac"),
    @NamedQuery(name = "FacFactura.findByBaseCeroFafac", query = "SELECT f FROM FacFactura f WHERE f.baseCeroFafac = :baseCeroFafac"),
    @NamedQuery(name = "FacFactura.findByBaseAprobadaFafac", query = "SELECT f FROM FacFactura f WHERE f.baseAprobadaFafac = :baseAprobadaFafac"),
    @NamedQuery(name = "FacFactura.findByValorIvaFafac", query = "SELECT f FROM FacFactura f WHERE f.valorIvaFafac = :valorIvaFafac"),
    @NamedQuery(name = "FacFactura.findByTotalFafac", query = "SELECT f FROM FacFactura f WHERE f.totalFafac = :totalFafac"),
    @NamedQuery(name = "FacFactura.findByConciliadoFafac", query = "SELECT f FROM FacFactura f WHERE f.conciliadoFafac = :conciliadoFafac"),
    @NamedQuery(name = "FacFactura.findByRazonAnuladoFafac", query = "SELECT f FROM FacFactura f WHERE f.razonAnuladoFafac = :razonAnuladoFafac"),
    @NamedQuery(name = "FacFactura.findByFechaAnuladoFafac", query = "SELECT f FROM FacFactura f WHERE f.fechaAnuladoFafac = :fechaAnuladoFafac"),
    @NamedQuery(name = "FacFactura.findByActivoFafac", query = "SELECT f FROM FacFactura f WHERE f.activoFafac = :activoFafac"),
    @NamedQuery(name = "FacFactura.findByUsuarioIngre", query = "SELECT f FROM FacFactura f WHERE f.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "FacFactura.findByFechaIngre", query = "SELECT f FROM FacFactura f WHERE f.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "FacFactura.findByHoraIngre", query = "SELECT f FROM FacFactura f WHERE f.horaIngre = :horaIngre"),
    @NamedQuery(name = "FacFactura.findByUsuarioActua", query = "SELECT f FROM FacFactura f WHERE f.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "FacFactura.findByFechaActua", query = "SELECT f FROM FacFactura f WHERE f.fechaActua = :fechaActua"),
    @NamedQuery(name = "FacFactura.findByHoraActua", query = "SELECT f FROM FacFactura f WHERE f.horaActua = :horaActua"),
    @NamedQuery(name = "FacFactura.findByCodigoFaclineaFafac", query = "SELECT f FROM FacFactura f WHERE f.codigoFaclineaFafac = :codigoFaclineaFafac"),
    @NamedQuery(name = "FacFactura.findByResponsableFaclineaFafac", query = "SELECT f FROM FacFactura f WHERE f.responsableFaclineaFafac = :responsableFaclineaFafac"),
    @NamedQuery(name = "FacFactura.findByNumComprobanteFafac", query = "SELECT f FROM FacFactura f WHERE f.numComprobanteFafac = :numComprobanteFafac"),
    @NamedQuery(name = "FacFactura.findByFacturaFisicaFafac", query = "SELECT f FROM FacFactura f WHERE f.facturaFisicaFafac = :facturaFisicaFafac")})
public class FacFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_fafac", nullable = false)
    private Integer ideFafac;
    @Column(name = "ide_comov")
    private BigInteger ideComov;
    @Column(name = "ide_sucu")
    private BigInteger ideSucu;
    @Column(name = "fecha_vencimiento_fafac")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimientoFafac;
    @Column(name = "fecha_emision_fafac")
    @Temporal(TemporalType.DATE)
    private Date fechaEmisionFafac;
    @Column(name = "fecha_tansaccion_fafac")
    @Temporal(TemporalType.DATE)
    private Date fechaTansaccionFafac;
    @Size(max = 20)
    @Column(name = "secuencial_fafac", length = 20)
    private String secuencialFafac;
    @Size(max = 250)
    @Column(name = "direccion_fafac", length = 250)
    private String direccionFafac;
    @Size(max = 2147483647)
    @Column(name = "observacion_fafac", length = 2147483647)
    private String observacionFafac;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "base_no_iva_fafac", precision = 10, scale = 3)
    private BigDecimal baseNoIvaFafac;
    @Column(name = "base_cero_fafac", precision = 10, scale = 3)
    private BigDecimal baseCeroFafac;
    @Column(name = "base_aprobada_fafac", precision = 10, scale = 3)
    private BigDecimal baseAprobadaFafac;
    @Column(name = "valor_iva_fafac", precision = 10, scale = 3)
    private BigDecimal valorIvaFafac;
    @Column(name = "total_fafac", precision = 10, scale = 3)
    private BigDecimal totalFafac;
    @Column(name = "conciliado_fafac")
    private Boolean conciliadoFafac;
    @Size(max = 2147483647)
    @Column(name = "razon_anulado_fafac", length = 2147483647)
    private String razonAnuladoFafac;
    @Column(name = "fecha_anulado_fafac")
    @Temporal(TemporalType.DATE)
    private Date fechaAnuladoFafac;
    @Column(name = "activo_fafac")
    private Boolean activoFafac;
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
    @Column(name = "codigo_faclinea_fafac")
    private Integer codigoFaclineaFafac;
    @Size(max = 50)
    @Column(name = "responsable_faclinea_fafac", length = 50)
    private String responsableFaclineaFafac;
    @Size(max = 50)
    @Column(name = "num_comprobante_fafac", length = 50)
    private String numComprobanteFafac;
    @Size(max = 20)
    @Column(name = "factura_fisica_fafac", length = 20)
    private String facturaFisicaFafac;
    @JoinColumn(name = "ide_tetid", referencedColumnName = "ide_tetid")
    @ManyToOne
    private TesTipoDocumento ideTetid;
    @JoinColumn(name = "ide_tedar", referencedColumnName = "ide_tedar")
    @ManyToOne
    private TesDatosRetencion ideTedar;
    @JoinColumn(name = "ide_retip", referencedColumnName = "ide_retip")
    @ManyToOne
    private RecTipo ideRetip;
    @JoinColumn(name = "ide_recli", referencedColumnName = "ide_recli")
    @ManyToOne
    private RecClientes ideRecli;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_falug", referencedColumnName = "ide_falug")
    @ManyToOne
    private FacLugar ideFalug;
    @JoinColumn(name = "ide_fadaf", referencedColumnName = "ide_fadaf")
    @ManyToOne
    private FacDatosFactura ideFadaf;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @OneToMany(mappedBy = "ideFafac")
    private List<FacDetalleFactura> facDetalleFacturaList;

    public FacFactura() {
    }

    public FacFactura(Integer ideFafac) {
        this.ideFafac = ideFafac;
    }

    public Integer getIdeFafac() {
        return ideFafac;
    }

    public void setIdeFafac(Integer ideFafac) {
        this.ideFafac = ideFafac;
    }

    public BigInteger getIdeComov() {
        return ideComov;
    }

    public void setIdeComov(BigInteger ideComov) {
        this.ideComov = ideComov;
    }

    public BigInteger getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(BigInteger ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Date getFechaVencimientoFafac() {
        return fechaVencimientoFafac;
    }

    public void setFechaVencimientoFafac(Date fechaVencimientoFafac) {
        this.fechaVencimientoFafac = fechaVencimientoFafac;
    }

    public Date getFechaEmisionFafac() {
        return fechaEmisionFafac;
    }

    public void setFechaEmisionFafac(Date fechaEmisionFafac) {
        this.fechaEmisionFafac = fechaEmisionFafac;
    }

    public Date getFechaTansaccionFafac() {
        return fechaTansaccionFafac;
    }

    public void setFechaTansaccionFafac(Date fechaTansaccionFafac) {
        this.fechaTansaccionFafac = fechaTansaccionFafac;
    }

    public String getSecuencialFafac() {
        return secuencialFafac;
    }

    public void setSecuencialFafac(String secuencialFafac) {
        this.secuencialFafac = secuencialFafac;
    }

    public String getDireccionFafac() {
        return direccionFafac;
    }

    public void setDireccionFafac(String direccionFafac) {
        this.direccionFafac = direccionFafac;
    }

    public String getObservacionFafac() {
        return observacionFafac;
    }

    public void setObservacionFafac(String observacionFafac) {
        this.observacionFafac = observacionFafac;
    }

    public BigDecimal getBaseNoIvaFafac() {
        return baseNoIvaFafac;
    }

    public void setBaseNoIvaFafac(BigDecimal baseNoIvaFafac) {
        this.baseNoIvaFafac = baseNoIvaFafac;
    }

    public BigDecimal getBaseCeroFafac() {
        return baseCeroFafac;
    }

    public void setBaseCeroFafac(BigDecimal baseCeroFafac) {
        this.baseCeroFafac = baseCeroFafac;
    }

    public BigDecimal getBaseAprobadaFafac() {
        return baseAprobadaFafac;
    }

    public void setBaseAprobadaFafac(BigDecimal baseAprobadaFafac) {
        this.baseAprobadaFafac = baseAprobadaFafac;
    }

    public BigDecimal getValorIvaFafac() {
        return valorIvaFafac;
    }

    public void setValorIvaFafac(BigDecimal valorIvaFafac) {
        this.valorIvaFafac = valorIvaFafac;
    }

    public BigDecimal getTotalFafac() {
        return totalFafac;
    }

    public void setTotalFafac(BigDecimal totalFafac) {
        this.totalFafac = totalFafac;
    }

    public Boolean getConciliadoFafac() {
        return conciliadoFafac;
    }

    public void setConciliadoFafac(Boolean conciliadoFafac) {
        this.conciliadoFafac = conciliadoFafac;
    }

    public String getRazonAnuladoFafac() {
        return razonAnuladoFafac;
    }

    public void setRazonAnuladoFafac(String razonAnuladoFafac) {
        this.razonAnuladoFafac = razonAnuladoFafac;
    }

    public Date getFechaAnuladoFafac() {
        return fechaAnuladoFafac;
    }

    public void setFechaAnuladoFafac(Date fechaAnuladoFafac) {
        this.fechaAnuladoFafac = fechaAnuladoFafac;
    }

    public Boolean getActivoFafac() {
        return activoFafac;
    }

    public void setActivoFafac(Boolean activoFafac) {
        this.activoFafac = activoFafac;
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

    public Integer getCodigoFaclineaFafac() {
        return codigoFaclineaFafac;
    }

    public void setCodigoFaclineaFafac(Integer codigoFaclineaFafac) {
        this.codigoFaclineaFafac = codigoFaclineaFafac;
    }

    public String getResponsableFaclineaFafac() {
        return responsableFaclineaFafac;
    }

    public void setResponsableFaclineaFafac(String responsableFaclineaFafac) {
        this.responsableFaclineaFafac = responsableFaclineaFafac;
    }

    public String getNumComprobanteFafac() {
        return numComprobanteFafac;
    }

    public void setNumComprobanteFafac(String numComprobanteFafac) {
        this.numComprobanteFafac = numComprobanteFafac;
    }

    public String getFacturaFisicaFafac() {
        return facturaFisicaFafac;
    }

    public void setFacturaFisicaFafac(String facturaFisicaFafac) {
        this.facturaFisicaFafac = facturaFisicaFafac;
    }

    public TesTipoDocumento getIdeTetid() {
        return ideTetid;
    }

    public void setIdeTetid(TesTipoDocumento ideTetid) {
        this.ideTetid = ideTetid;
    }

    public TesDatosRetencion getIdeTedar() {
        return ideTedar;
    }

    public void setIdeTedar(TesDatosRetencion ideTedar) {
        this.ideTedar = ideTedar;
    }

    public RecTipo getIdeRetip() {
        return ideRetip;
    }

    public void setIdeRetip(RecTipo ideRetip) {
        this.ideRetip = ideRetip;
    }

    public RecClientes getIdeRecli() {
        return ideRecli;
    }

    public void setIdeRecli(RecClientes ideRecli) {
        this.ideRecli = ideRecli;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public FacLugar getIdeFalug() {
        return ideFalug;
    }

    public void setIdeFalug(FacLugar ideFalug) {
        this.ideFalug = ideFalug;
    }

    public FacDatosFactura getIdeFadaf() {
        return ideFadaf;
    }

    public void setIdeFadaf(FacDatosFactura ideFadaf) {
        this.ideFadaf = ideFadaf;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public List<FacDetalleFactura> getFacDetalleFacturaList() {
        return facDetalleFacturaList;
    }

    public void setFacDetalleFacturaList(List<FacDetalleFactura> facDetalleFacturaList) {
        this.facDetalleFacturaList = facDetalleFacturaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFafac != null ? ideFafac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacFactura)) {
            return false;
        }
        FacFactura other = (FacFactura) object;
        if ((this.ideFafac == null && other.ideFafac != null) || (this.ideFafac != null && !this.ideFafac.equals(other.ideFafac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.FacFactura[ ideFafac=" + ideFafac + " ]";
    }
    
}
