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
@Table(name = "afi_activo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AfiActivo.findAll", query = "SELECT a FROM AfiActivo a"),
    @NamedQuery(name = "AfiActivo.findByIdeAfact", query = "SELECT a FROM AfiActivo a WHERE a.ideAfact = :ideAfact"),
    @NamedQuery(name = "AfiActivo.findByFechaBajaAfact", query = "SELECT a FROM AfiActivo a WHERE a.fechaBajaAfact = :fechaBajaAfact"),
    @NamedQuery(name = "AfiActivo.findByCantidadAfact", query = "SELECT a FROM AfiActivo a WHERE a.cantidadAfact = :cantidadAfact"),
    @NamedQuery(name = "AfiActivo.findByRazonBajaAfact", query = "SELECT a FROM AfiActivo a WHERE a.razonBajaAfact = :razonBajaAfact"),
    @NamedQuery(name = "AfiActivo.findByMarcaAfact", query = "SELECT a FROM AfiActivo a WHERE a.marcaAfact = :marcaAfact"),
    @NamedQuery(name = "AfiActivo.findBySerieAfact", query = "SELECT a FROM AfiActivo a WHERE a.serieAfact = :serieAfact"),
    @NamedQuery(name = "AfiActivo.findByModeloAfact", query = "SELECT a FROM AfiActivo a WHERE a.modeloAfact = :modeloAfact"),
    @NamedQuery(name = "AfiActivo.findByValorUnitarioAfact", query = "SELECT a FROM AfiActivo a WHERE a.valorUnitarioAfact = :valorUnitarioAfact"),
    @NamedQuery(name = "AfiActivo.findByEgresoBodegaAfact", query = "SELECT a FROM AfiActivo a WHERE a.egresoBodegaAfact = :egresoBodegaAfact"),
    @NamedQuery(name = "AfiActivo.findByVidaUtilAfact", query = "SELECT a FROM AfiActivo a WHERE a.vidaUtilAfact = :vidaUtilAfact"),
    @NamedQuery(name = "AfiActivo.findByDetalleAfact", query = "SELECT a FROM AfiActivo a WHERE a.detalleAfact = :detalleAfact"),
    @NamedQuery(name = "AfiActivo.findByFechaAltaAfact", query = "SELECT a FROM AfiActivo a WHERE a.fechaAltaAfact = :fechaAltaAfact"),
    @NamedQuery(name = "AfiActivo.findByValorNetoAfact", query = "SELECT a FROM AfiActivo a WHERE a.valorNetoAfact = :valorNetoAfact"),
    @NamedQuery(name = "AfiActivo.findByFechaReavaluoAfact", query = "SELECT a FROM AfiActivo a WHERE a.fechaReavaluoAfact = :fechaReavaluoAfact"),
    @NamedQuery(name = "AfiActivo.findByValorCompraAfact", query = "SELECT a FROM AfiActivo a WHERE a.valorCompraAfact = :valorCompraAfact"),
    @NamedQuery(name = "AfiActivo.findByFechaCalculoAfact", query = "SELECT a FROM AfiActivo a WHERE a.fechaCalculoAfact = :fechaCalculoAfact"),
    @NamedQuery(name = "AfiActivo.findByValorRevaluoAfact", query = "SELECT a FROM AfiActivo a WHERE a.valorRevaluoAfact = :valorRevaluoAfact"),
    @NamedQuery(name = "AfiActivo.findByFechaGarantiaAfact", query = "SELECT a FROM AfiActivo a WHERE a.fechaGarantiaAfact = :fechaGarantiaAfact"),
    @NamedQuery(name = "AfiActivo.findByActivoAfact", query = "SELECT a FROM AfiActivo a WHERE a.activoAfact = :activoAfact"),
    @NamedQuery(name = "AfiActivo.findByUsuarioIngre", query = "SELECT a FROM AfiActivo a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiActivo.findByFechaIngre", query = "SELECT a FROM AfiActivo a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiActivo.findByHoraIngre", query = "SELECT a FROM AfiActivo a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiActivo.findByUsuarioActua", query = "SELECT a FROM AfiActivo a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiActivo.findByFechaActua", query = "SELECT a FROM AfiActivo a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiActivo.findByHoraActua", query = "SELECT a FROM AfiActivo a WHERE a.horaActua = :horaActua"),
    @NamedQuery(name = "AfiActivo.findByIdeCocac", query = "SELECT a FROM AfiActivo a WHERE a.ideCocac = :ideCocac"),
    @NamedQuery(name = "AfiActivo.findBySecuencialAfact", query = "SELECT a FROM AfiActivo a WHERE a.secuencialAfact = :secuencialAfact"),
    @NamedQuery(name = "AfiActivo.findByFotoBienAfact", query = "SELECT a FROM AfiActivo a WHERE a.fotoBienAfact = :fotoBienAfact"),
    @NamedQuery(name = "AfiActivo.findByValorResidualAfact", query = "SELECT a FROM AfiActivo a WHERE a.valorResidualAfact = :valorResidualAfact"),
    @NamedQuery(name = "AfiActivo.findByCodAnteriorAfact", query = "SELECT a FROM AfiActivo a WHERE a.codAnteriorAfact = :codAnteriorAfact"),
    @NamedQuery(name = "AfiActivo.findByValorDepreMesAfact", query = "SELECT a FROM AfiActivo a WHERE a.valorDepreMesAfact = :valorDepreMesAfact"),
    @NamedQuery(name = "AfiActivo.findByValDepreciacionPeriodoAfact", query = "SELECT a FROM AfiActivo a WHERE a.valDepreciacionPeriodoAfact = :valDepreciacionPeriodoAfact"),
    @NamedQuery(name = "AfiActivo.findByValorDepreciacionAfact", query = "SELECT a FROM AfiActivo a WHERE a.valorDepreciacionAfact = :valorDepreciacionAfact"),
    @NamedQuery(name = "AfiActivo.findByDepreciacionAcumuladaAfact", query = "SELECT a FROM AfiActivo a WHERE a.depreciacionAcumuladaAfact = :depreciacionAcumuladaAfact"),
    @NamedQuery(name = "AfiActivo.findByColorAfact", query = "SELECT a FROM AfiActivo a WHERE a.colorAfact = :colorAfact"),
    @NamedQuery(name = "AfiActivo.findByNroFacturaAfact", query = "SELECT a FROM AfiActivo a WHERE a.nroFacturaAfact = :nroFacturaAfact"),
    @NamedQuery(name = "AfiActivo.findByObservacionesAfact", query = "SELECT a FROM AfiActivo a WHERE a.observacionesAfact = :observacionesAfact"),
    @NamedQuery(name = "AfiActivo.findByIdePrcla", query = "SELECT a FROM AfiActivo a WHERE a.idePrcla = :idePrcla"),
    @NamedQuery(name = "AfiActivo.findByValorIva", query = "SELECT a FROM AfiActivo a WHERE a.valorIva = :valorIva"),
    @NamedQuery(name = "AfiActivo.findByTipoComprobante", query = "SELECT a FROM AfiActivo a WHERE a.tipoComprobante = :tipoComprobante"),
    @NamedQuery(name = "AfiActivo.findByChasisAfact", query = "SELECT a FROM AfiActivo a WHERE a.chasisAfact = :chasisAfact"),
    @NamedQuery(name = "AfiActivo.findByMotorAfact", query = "SELECT a FROM AfiActivo a WHERE a.motorAfact = :motorAfact"),
    @NamedQuery(name = "AfiActivo.findByAfiUltimaActa", query = "SELECT a FROM AfiActivo a WHERE a.afiUltimaActa = :afiUltimaActa"),
    @NamedQuery(name = "AfiActivo.findByIdeBoubi", query = "SELECT a FROM AfiActivo a WHERE a.ideBoubi = :ideBoubi"),
    @NamedQuery(name = "AfiActivo.findByAfiPrestado", query = "SELECT a FROM AfiActivo a WHERE a.afiPrestado = :afiPrestado")})
public class AfiActivo implements Serializable {

    @OneToMany(mappedBy = "ideAfact")
    private List<VehVehiculo> vehVehiculoList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_afact")
    private Long ideAfact;
    @Column(name = "fecha_baja_afact")
    @Temporal(TemporalType.DATE)
    private Date fechaBajaAfact;
    @Column(name = "cantidad_afact")
    private BigInteger cantidadAfact;
    @Column(name = "razon_baja_afact")
    private String razonBajaAfact;
    @Column(name = "marca_afact")
    private String marcaAfact;
    @Column(name = "serie_afact")
    private String serieAfact;
    @Column(name = "modelo_afact")
    private String modeloAfact;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_unitario_afact")
    private BigDecimal valorUnitarioAfact;
    @Column(name = "egreso_bodega_afact")
    private String egresoBodegaAfact;
    @Column(name = "vida_util_afact")
    private BigInteger vidaUtilAfact;
    @Column(name = "detalle_afact")
    private String detalleAfact;
    @Column(name = "fecha_alta_afact")
    @Temporal(TemporalType.DATE)
    private Date fechaAltaAfact;
    @Column(name = "valor_neto_afact")
    private BigDecimal valorNetoAfact;
    @Column(name = "fecha_reavaluo_afact")
    @Temporal(TemporalType.DATE)
    private Date fechaReavaluoAfact;
    @Column(name = "valor_compra_afact")
    private BigDecimal valorCompraAfact;
    @Column(name = "fecha_calculo_afact")
    @Temporal(TemporalType.DATE)
    private Date fechaCalculoAfact;
    @Column(name = "valor_revaluo_afact")
    private BigDecimal valorRevaluoAfact;
    @Column(name = "fecha_garantia_afact")
    @Temporal(TemporalType.DATE)
    private Date fechaGarantiaAfact;
    @Column(name = "activo_afact")
    private Boolean activoAfact;
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
    @Column(name = "ide_cocac")
    private BigInteger ideCocac;
    @Column(name = "secuencial_afact")
    private Integer secuencialAfact;
    @Column(name = "foto_bien_afact")
    private String fotoBienAfact;
    @Column(name = "valor_residual_afact")
    private BigDecimal valorResidualAfact;
    @Column(name = "cod_anterior_afact")
    private String codAnteriorAfact;
    @Column(name = "valor_depre_mes_afact")
    private BigDecimal valorDepreMesAfact;
    @Column(name = "val_depreciacion_periodo_afact")
    private BigDecimal valDepreciacionPeriodoAfact;
    @Column(name = "valor_depreciacion_afact")
    private BigDecimal valorDepreciacionAfact;
    @Column(name = "depreciacion_acumulada_afact")
    private BigDecimal depreciacionAcumuladaAfact;
    @Column(name = "color_afact")
    private String colorAfact;
    @Column(name = "nro_factura_afact")
    private String nroFacturaAfact;
    @Column(name = "observaciones_afact")
    private String observacionesAfact;
    @Column(name = "ide_prcla")
    private BigInteger idePrcla;
    @Column(name = "valor_iva")
    private BigInteger valorIva;
    @Column(name = "tipo_comprobante")
    private String tipoComprobante;
    @Column(name = "chasis_afact")
    private String chasisAfact;
    @Column(name = "motor_afact")
    private String motorAfact;
    @Column(name = "afi_ultima_acta")
    private BigInteger afiUltimaActa;
    @Column(name = "ide_boubi")
    private BigInteger ideBoubi;
    @Column(name = "afi_prestado")
    private Boolean afiPrestado;
    //@OneToMany(mappedBy = "ideAfact")
    //private List<AfiCustodio> afiCustodioList;
    //@OneToMany(mappedBy = "ideAfact")
    //private List<AfiArchivo> afiArchivoList;
    //@OneToMany(mappedBy = "ideAfact")
    //private List<AfiDocDetalleActivo> afiDocDetalleActivoList;
    @JoinColumn(name = "ide_tepro", referencedColumnName = "ide_tepro")
    @ManyToOne
    private TesProveedor ideTepro;
    @JoinColumn(name = "ide_adsoc", referencedColumnName = "ide_prcla")
    @ManyToOne
    private AdqSolicitudCompra ideAdsoc1;
    @JoinColumn(name = "ide_afacd", referencedColumnName = "ide_afacd")
    @ManyToOne
    private AfiActividad ideAfacd;
    @JoinColumn(name = "ide_afest", referencedColumnName = "ide_afest")
    @ManyToOne
    private AfiEstado ideAfest;
    @JoinColumn(name = "ide_afnoa", referencedColumnName = "ide_afnoa")
    @ManyToOne
    private AfiNombreActivo ideAfnoa;
    @JoinColumn(name = "ide_afseg", referencedColumnName = "ide_afseg")
    @ManyToOne
    private AfiSeguro ideAfseg;
    @JoinColumn(name = "ide_aftia", referencedColumnName = "ide_aftia")
    @ManyToOne
    private AfiTipoActivo ideAftia;
    @JoinColumn(name = "ide_aftip", referencedColumnName = "ide_aftip")
    @ManyToOne
    private AfiTipoPropiedad ideAftip;
    @JoinColumn(name = "ide_afubi", referencedColumnName = "ide_afubi")
    @ManyToOne
    private AfiUbicacion ideAfubi;
    @JoinColumn(name = "ide_bobod", referencedColumnName = "ide_bobod")
    @ManyToOne
    private BodtBodega ideBobod;
    //@JoinColumn(name = "ide_bocam", referencedColumnName = "ide_bocam")
    //@ManyToOne
    //private BodtCatalogoMaterial ideBocam;
    @JoinColumn(name = "ide_boegr", referencedColumnName = "ide_boegr")
    @ManyToOne
    private BodtEgreso ideBoegr;
    @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare")
    @ManyToOne
    private GenArea ideGeare;

    public AfiActivo() {
    }

    public AfiActivo(Long ideAfact) {
        this.ideAfact = ideAfact;
    }

    public Long getIdeAfact() {
        return ideAfact;
    }

    public void setIdeAfact(Long ideAfact) {
        this.ideAfact = ideAfact;
    }

    public Date getFechaBajaAfact() {
        return fechaBajaAfact;
    }

    public void setFechaBajaAfact(Date fechaBajaAfact) {
        this.fechaBajaAfact = fechaBajaAfact;
    }

    public BigInteger getCantidadAfact() {
        return cantidadAfact;
    }

    public void setCantidadAfact(BigInteger cantidadAfact) {
        this.cantidadAfact = cantidadAfact;
    }

    public String getRazonBajaAfact() {
        return razonBajaAfact;
    }

    public void setRazonBajaAfact(String razonBajaAfact) {
        this.razonBajaAfact = razonBajaAfact;
    }

    public String getMarcaAfact() {
        return marcaAfact;
    }

    public void setMarcaAfact(String marcaAfact) {
        this.marcaAfact = marcaAfact;
    }

    public String getSerieAfact() {
        return serieAfact;
    }

    public void setSerieAfact(String serieAfact) {
        this.serieAfact = serieAfact;
    }

    public String getModeloAfact() {
        return modeloAfact;
    }

    public void setModeloAfact(String modeloAfact) {
        this.modeloAfact = modeloAfact;
    }

    public BigDecimal getValorUnitarioAfact() {
        return valorUnitarioAfact;
    }

    public void setValorUnitarioAfact(BigDecimal valorUnitarioAfact) {
        this.valorUnitarioAfact = valorUnitarioAfact;
    }

    public String getEgresoBodegaAfact() {
        return egresoBodegaAfact;
    }

    public void setEgresoBodegaAfact(String egresoBodegaAfact) {
        this.egresoBodegaAfact = egresoBodegaAfact;
    }

    public BigInteger getVidaUtilAfact() {
        return vidaUtilAfact;
    }

    public void setVidaUtilAfact(BigInteger vidaUtilAfact) {
        this.vidaUtilAfact = vidaUtilAfact;
    }

    public String getDetalleAfact() {
        return detalleAfact;
    }

    public void setDetalleAfact(String detalleAfact) {
        this.detalleAfact = detalleAfact;
    }

    public Date getFechaAltaAfact() {
        return fechaAltaAfact;
    }

    public void setFechaAltaAfact(Date fechaAltaAfact) {
        this.fechaAltaAfact = fechaAltaAfact;
    }

    public BigDecimal getValorNetoAfact() {
        return valorNetoAfact;
    }

    public void setValorNetoAfact(BigDecimal valorNetoAfact) {
        this.valorNetoAfact = valorNetoAfact;
    }

    public Date getFechaReavaluoAfact() {
        return fechaReavaluoAfact;
    }

    public void setFechaReavaluoAfact(Date fechaReavaluoAfact) {
        this.fechaReavaluoAfact = fechaReavaluoAfact;
    }

    public BigDecimal getValorCompraAfact() {
        return valorCompraAfact;
    }

    public void setValorCompraAfact(BigDecimal valorCompraAfact) {
        this.valorCompraAfact = valorCompraAfact;
    }

    public Date getFechaCalculoAfact() {
        return fechaCalculoAfact;
    }

    public void setFechaCalculoAfact(Date fechaCalculoAfact) {
        this.fechaCalculoAfact = fechaCalculoAfact;
    }

    public BigDecimal getValorRevaluoAfact() {
        return valorRevaluoAfact;
    }

    public void setValorRevaluoAfact(BigDecimal valorRevaluoAfact) {
        this.valorRevaluoAfact = valorRevaluoAfact;
    }

    public Date getFechaGarantiaAfact() {
        return fechaGarantiaAfact;
    }

    public void setFechaGarantiaAfact(Date fechaGarantiaAfact) {
        this.fechaGarantiaAfact = fechaGarantiaAfact;
    }

    public Boolean getActivoAfact() {
        return activoAfact;
    }

    public void setActivoAfact(Boolean activoAfact) {
        this.activoAfact = activoAfact;
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

    public BigInteger getIdeCocac() {
        return ideCocac;
    }

    public void setIdeCocac(BigInteger ideCocac) {
        this.ideCocac = ideCocac;
    }

    public Integer getSecuencialAfact() {
        return secuencialAfact;
    }

    public void setSecuencialAfact(Integer secuencialAfact) {
        this.secuencialAfact = secuencialAfact;
    }

    public String getFotoBienAfact() {
        return fotoBienAfact;
    }

    public void setFotoBienAfact(String fotoBienAfact) {
        this.fotoBienAfact = fotoBienAfact;
    }

    public BigDecimal getValorResidualAfact() {
        return valorResidualAfact;
    }

    public void setValorResidualAfact(BigDecimal valorResidualAfact) {
        this.valorResidualAfact = valorResidualAfact;
    }

    public String getCodAnteriorAfact() {
        return codAnteriorAfact;
    }

    public void setCodAnteriorAfact(String codAnteriorAfact) {
        this.codAnteriorAfact = codAnteriorAfact;
    }

    public BigDecimal getValorDepreMesAfact() {
        return valorDepreMesAfact;
    }

    public void setValorDepreMesAfact(BigDecimal valorDepreMesAfact) {
        this.valorDepreMesAfact = valorDepreMesAfact;
    }

    public BigDecimal getValDepreciacionPeriodoAfact() {
        return valDepreciacionPeriodoAfact;
    }

    public void setValDepreciacionPeriodoAfact(BigDecimal valDepreciacionPeriodoAfact) {
        this.valDepreciacionPeriodoAfact = valDepreciacionPeriodoAfact;
    }

    public BigDecimal getValorDepreciacionAfact() {
        return valorDepreciacionAfact;
    }

    public void setValorDepreciacionAfact(BigDecimal valorDepreciacionAfact) {
        this.valorDepreciacionAfact = valorDepreciacionAfact;
    }

    public BigDecimal getDepreciacionAcumuladaAfact() {
        return depreciacionAcumuladaAfact;
    }

    public void setDepreciacionAcumuladaAfact(BigDecimal depreciacionAcumuladaAfact) {
        this.depreciacionAcumuladaAfact = depreciacionAcumuladaAfact;
    }

    public String getColorAfact() {
        return colorAfact;
    }

    public void setColorAfact(String colorAfact) {
        this.colorAfact = colorAfact;
    }

    public String getNroFacturaAfact() {
        return nroFacturaAfact;
    }

    public void setNroFacturaAfact(String nroFacturaAfact) {
        this.nroFacturaAfact = nroFacturaAfact;
    }

    public String getObservacionesAfact() {
        return observacionesAfact;
    }

    public void setObservacionesAfact(String observacionesAfact) {
        this.observacionesAfact = observacionesAfact;
    }

    public BigInteger getIdePrcla() {
        return idePrcla;
    }

    public void setIdePrcla(BigInteger idePrcla) {
        this.idePrcla = idePrcla;
    }

    public BigInteger getValorIva() {
        return valorIva;
    }

    public void setValorIva(BigInteger valorIva) {
        this.valorIva = valorIva;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public String getChasisAfact() {
        return chasisAfact;
    }

    public void setChasisAfact(String chasisAfact) {
        this.chasisAfact = chasisAfact;
    }

    public String getMotorAfact() {
        return motorAfact;
    }

    public void setMotorAfact(String motorAfact) {
        this.motorAfact = motorAfact;
    }

    public BigInteger getAfiUltimaActa() {
        return afiUltimaActa;
    }

    public void setAfiUltimaActa(BigInteger afiUltimaActa) {
        this.afiUltimaActa = afiUltimaActa;
    }

    public BigInteger getIdeBoubi() {
        return ideBoubi;
    }

    public void setIdeBoubi(BigInteger ideBoubi) {
        this.ideBoubi = ideBoubi;
    }

    public Boolean getAfiPrestado() {
        return afiPrestado;
    }

    public void setAfiPrestado(Boolean afiPrestado) {
        this.afiPrestado = afiPrestado;
    }

    /*@XmlTransient
    public List<AfiCustodio> getAfiCustodioList() {
        return afiCustodioList;
    }

    public void setAfiCustodioList(List<AfiCustodio> afiCustodioList) {
        this.afiCustodioList = afiCustodioList;
    }

    @XmlTransient
    public List<AfiArchivo> getAfiArchivoList() {
        return afiArchivoList;
    }

    public void setAfiArchivoList(List<AfiArchivo> afiArchivoList) {
        this.afiArchivoList = afiArchivoList;
    }

    @XmlTransient
    public List<AfiDocDetalleActivo> getAfiDocDetalleActivoList() {
        return afiDocDetalleActivoList;
    }

    public void setAfiDocDetalleActivoList(List<AfiDocDetalleActivo> afiDocDetalleActivoList) {
        this.afiDocDetalleActivoList = afiDocDetalleActivoList;
    }*/

    public TesProveedor getIdeTepro() {
        return ideTepro;
    }

    public void setIdeTepro(TesProveedor ideTepro) {
        this.ideTepro = ideTepro;
    }


    public AdqSolicitudCompra getIdeAdsoc1() {
        return ideAdsoc1;
    }

    public void setIdeAdsoc1(AdqSolicitudCompra ideAdsoc1) {
        this.ideAdsoc1 = ideAdsoc1;
    }

    public AfiActividad getIdeAfacd() {
        return ideAfacd;
    }

    public void setIdeAfacd(AfiActividad ideAfacd) {
        this.ideAfacd = ideAfacd;
    }

    public AfiEstado getIdeAfest() {
        return ideAfest;
    }

    public void setIdeAfest(AfiEstado ideAfest) {
        this.ideAfest = ideAfest;
    }

    public AfiNombreActivo getIdeAfnoa() {
        return ideAfnoa;
    }

    public void setIdeAfnoa(AfiNombreActivo ideAfnoa) {
        this.ideAfnoa = ideAfnoa;
    }

    public AfiSeguro getIdeAfseg() {
        return ideAfseg;
    }

    public void setIdeAfseg(AfiSeguro ideAfseg) {
        this.ideAfseg = ideAfseg;
    }

    public AfiTipoActivo getIdeAftia() {
        return ideAftia;
    }

    public void setIdeAftia(AfiTipoActivo ideAftia) {
        this.ideAftia = ideAftia;
    }

    public AfiTipoPropiedad getIdeAftip() {
        return ideAftip;
    }

    public void setIdeAftip(AfiTipoPropiedad ideAftip) {
        this.ideAftip = ideAftip;
    }

    public AfiUbicacion getIdeAfubi() {
        return ideAfubi;
    }

    public void setIdeAfubi(AfiUbicacion ideAfubi) {
        this.ideAfubi = ideAfubi;
    }

    public BodtBodega getIdeBobod() {
        return ideBobod;
    }

    public void setIdeBobod(BodtBodega ideBobod) {
        this.ideBobod = ideBobod;
    }

    /*public BodtCatalogoMaterial getIdeBocam() {
        return ideBocam;
    }

    public void setIdeBocam(BodtCatalogoMaterial ideBocam) {
        this.ideBocam = ideBocam;
    }*/

    public BodtEgreso getIdeBoegr() {
        return ideBoegr;
    }

    public void setIdeBoegr(BodtEgreso ideBoegr) {
        this.ideBoegr = ideBoegr;
    }

    public GenArea getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(GenArea ideGeare) {
        this.ideGeare = ideGeare;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAfact != null ? ideAfact.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiActivo)) {
            return false;
        }
        AfiActivo other = (AfiActivo) object;
        if ((this.ideAfact == null && other.ideAfact != null) || (this.ideAfact != null && !this.ideAfact.equals(other.ideAfact))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiActivo[ ideAfact=" + ideAfact + " ]";
    }
    
    @XmlTransient
    public List<VehVehiculo> getVehVehiculoList() {
        return vehVehiculoList;
    }

    public void setVehVehiculoList(List<VehVehiculo> vehVehiculoList) {
        this.vehVehiculoList = vehVehiculoList;
    }
    
}
