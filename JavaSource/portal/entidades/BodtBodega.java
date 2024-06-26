/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "bodt_bodega", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtBodega.findAll", query = "SELECT b FROM BodtBodega b"),
    @NamedQuery(name = "BodtBodega.findByIdeBobod", query = "SELECT b FROM BodtBodega b WHERE b.ideBobod = :ideBobod"),
    @NamedQuery(name = "BodtBodega.findByFechaIngresoBobod", query = "SELECT b FROM BodtBodega b WHERE b.fechaIngresoBobod = :fechaIngresoBobod"),
    @NamedQuery(name = "BodtBodega.findByFechaCompraBobod", query = "SELECT b FROM BodtBodega b WHERE b.fechaCompraBobod = :fechaCompraBobod"),
    @NamedQuery(name = "BodtBodega.findByRecibidoBobod", query = "SELECT b FROM BodtBodega b WHERE b.recibidoBobod = :recibidoBobod"),
    @NamedQuery(name = "BodtBodega.findByCantidadIngresoBobod", query = "SELECT b FROM BodtBodega b WHERE b.cantidadIngresoBobod = :cantidadIngresoBobod"),
    @NamedQuery(name = "BodtBodega.findByNumeroIngresoBobod", query = "SELECT b FROM BodtBodega b WHERE b.numeroIngresoBobod = :numeroIngresoBobod"),
    @NamedQuery(name = "BodtBodega.findByNumFacturaBobod", query = "SELECT b FROM BodtBodega b WHERE b.numFacturaBobod = :numFacturaBobod"),
    @NamedQuery(name = "BodtBodega.findByNumDocBobod", query = "SELECT b FROM BodtBodega b WHERE b.numDocBobod = :numDocBobod"),
    @NamedQuery(name = "BodtBodega.findByDescripcionBobod", query = "SELECT b FROM BodtBodega b WHERE b.descripcionBobod = :descripcionBobod"),
    @NamedQuery(name = "BodtBodega.findByTipoIngresoBobod", query = "SELECT b FROM BodtBodega b WHERE b.tipoIngresoBobod = :tipoIngresoBobod"),
    @NamedQuery(name = "BodtBodega.findByMarcaBobod", query = "SELECT b FROM BodtBodega b WHERE b.marcaBobod = :marcaBobod"),
    @NamedQuery(name = "BodtBodega.findByModeloBobod", query = "SELECT b FROM BodtBodega b WHERE b.modeloBobod = :modeloBobod"),
    @NamedQuery(name = "BodtBodega.findBySerieBobod", query = "SELECT b FROM BodtBodega b WHERE b.serieBobod = :serieBobod"),
    @NamedQuery(name = "BodtBodega.findByValorUnitarioBobod", query = "SELECT b FROM BodtBodega b WHERE b.valorUnitarioBobod = :valorUnitarioBobod"),
    @NamedQuery(name = "BodtBodega.findByValorTotalBobod", query = "SELECT b FROM BodtBodega b WHERE b.valorTotalBobod = :valorTotalBobod"),
    @NamedQuery(name = "BodtBodega.findByColorBobod", query = "SELECT b FROM BodtBodega b WHERE b.colorBobod = :colorBobod"),
    @NamedQuery(name = "BodtBodega.findByExistenciaAnteriorBobod", query = "SELECT b FROM BodtBodega b WHERE b.existenciaAnteriorBobod = :existenciaAnteriorBobod"),
    @NamedQuery(name = "BodtBodega.findBySaldoBobod", query = "SELECT b FROM BodtBodega b WHERE b.saldoBobod = :saldoBobod"),
    @NamedQuery(name = "BodtBodega.findByActivoBobod", query = "SELECT b FROM BodtBodega b WHERE b.activoBobod = :activoBobod"),
    @NamedQuery(name = "BodtBodega.findByUsuarioIngre", query = "SELECT b FROM BodtBodega b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtBodega.findByFechaIngre", query = "SELECT b FROM BodtBodega b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtBodega.findByHoraIngre", query = "SELECT b FROM BodtBodega b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtBodega.findByUsuarioActua", query = "SELECT b FROM BodtBodega b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtBodega.findByFechaActua", query = "SELECT b FROM BodtBodega b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtBodega.findByHoraActua", query = "SELECT b FROM BodtBodega b WHERE b.horaActua = :horaActua"),
    @NamedQuery(name = "BodtBodega.findByIdeAdsoc", query = "SELECT b FROM BodtBodega b WHERE b.ideAdsoc = :ideAdsoc")})
public class BodtBodega implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_bobod", nullable = false)
    private Long ideBobod;
    @Column(name = "fecha_ingreso_bobod")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoBobod;
    @Column(name = "fecha_compra_bobod")
    @Temporal(TemporalType.DATE)
    private Date fechaCompraBobod;
    @Column(name = "recibido_bobod")
    private Boolean recibidoBobod;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad_ingreso_bobod", precision = 10, scale = 2)
    private BigDecimal cantidadIngresoBobod;
    @Column(name = "numero_ingreso_bobod")
    private BigInteger numeroIngresoBobod;
    @Size(max = 20)
    @Column(name = "num_factura_bobod", length = 20)
    private String numFacturaBobod;
    @Size(max = 10)
    @Column(name = "num_doc_bobod", length = 10)
    private String numDocBobod;
    @Size(max = 2147483647)
    @Column(name = "descripcion_bobod", length = 2147483647)
    private String descripcionBobod;
    @Column(name = "tipo_ingreso_bobod")
    private BigInteger tipoIngresoBobod;
    @Size(max = 50)
    @Column(name = "marca_bobod", length = 50)
    private String marcaBobod;
    @Size(max = 50)
    @Column(name = "modelo_bobod", length = 50)
    private String modeloBobod;
    @Size(max = 50)
    @Column(name = "serie_bobod", length = 50)
    private String serieBobod;
    @Column(name = "valor_unitario_bobod", precision = 10, scale = 4)
    private BigDecimal valorUnitarioBobod;
    @Column(name = "valor_total_bobod", precision = 10, scale = 4)
    private BigDecimal valorTotalBobod;
    @Size(max = 20)
    @Column(name = "color_bobod", length = 20)
    private String colorBobod;
    @Column(name = "existencia_anterior_bobod", precision = 10, scale = 2)
    private BigDecimal existenciaAnteriorBobod;
    @Column(name = "saldo_bobod", precision = 10, scale = 2)
    private BigDecimal saldoBobod;
    @Column(name = "activo_bobod")
    private Boolean activoBobod;
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
    @Column(name = "ide_adsoc")
    private BigInteger ideAdsoc;
    @JoinColumn(name = "ide_tepro", referencedColumnName = "ide_tepro")
    @ManyToOne
    private TesProveedor ideTepro;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @JoinColumn(name = "ide_bomat", referencedColumnName = "ide_bomat")
    @ManyToOne
    private BodtMaterial ideBomat;

    public BodtBodega() {
    }

    public BodtBodega(Long ideBobod) {
        this.ideBobod = ideBobod;
    }

    public Long getIdeBobod() {
        return ideBobod;
    }

    public void setIdeBobod(Long ideBobod) {
        this.ideBobod = ideBobod;
    }

    public Date getFechaIngresoBobod() {
        return fechaIngresoBobod;
    }

    public void setFechaIngresoBobod(Date fechaIngresoBobod) {
        this.fechaIngresoBobod = fechaIngresoBobod;
    }

    public Date getFechaCompraBobod() {
        return fechaCompraBobod;
    }

    public void setFechaCompraBobod(Date fechaCompraBobod) {
        this.fechaCompraBobod = fechaCompraBobod;
    }

    public Boolean getRecibidoBobod() {
        return recibidoBobod;
    }

    public void setRecibidoBobod(Boolean recibidoBobod) {
        this.recibidoBobod = recibidoBobod;
    }

    public BigDecimal getCantidadIngresoBobod() {
        return cantidadIngresoBobod;
    }

    public void setCantidadIngresoBobod(BigDecimal cantidadIngresoBobod) {
        this.cantidadIngresoBobod = cantidadIngresoBobod;
    }

    public BigInteger getNumeroIngresoBobod() {
        return numeroIngresoBobod;
    }

    public void setNumeroIngresoBobod(BigInteger numeroIngresoBobod) {
        this.numeroIngresoBobod = numeroIngresoBobod;
    }

    public String getNumFacturaBobod() {
        return numFacturaBobod;
    }

    public void setNumFacturaBobod(String numFacturaBobod) {
        this.numFacturaBobod = numFacturaBobod;
    }

    public String getNumDocBobod() {
        return numDocBobod;
    }

    public void setNumDocBobod(String numDocBobod) {
        this.numDocBobod = numDocBobod;
    }

    public String getDescripcionBobod() {
        return descripcionBobod;
    }

    public void setDescripcionBobod(String descripcionBobod) {
        this.descripcionBobod = descripcionBobod;
    }

    public BigInteger getTipoIngresoBobod() {
        return tipoIngresoBobod;
    }

    public void setTipoIngresoBobod(BigInteger tipoIngresoBobod) {
        this.tipoIngresoBobod = tipoIngresoBobod;
    }

    public String getMarcaBobod() {
        return marcaBobod;
    }

    public void setMarcaBobod(String marcaBobod) {
        this.marcaBobod = marcaBobod;
    }

    public String getModeloBobod() {
        return modeloBobod;
    }

    public void setModeloBobod(String modeloBobod) {
        this.modeloBobod = modeloBobod;
    }

    public String getSerieBobod() {
        return serieBobod;
    }

    public void setSerieBobod(String serieBobod) {
        this.serieBobod = serieBobod;
    }

    public BigDecimal getValorUnitarioBobod() {
        return valorUnitarioBobod;
    }

    public void setValorUnitarioBobod(BigDecimal valorUnitarioBobod) {
        this.valorUnitarioBobod = valorUnitarioBobod;
    }

    public BigDecimal getValorTotalBobod() {
        return valorTotalBobod;
    }

    public void setValorTotalBobod(BigDecimal valorTotalBobod) {
        this.valorTotalBobod = valorTotalBobod;
    }

    public String getColorBobod() {
        return colorBobod;
    }

    public void setColorBobod(String colorBobod) {
        this.colorBobod = colorBobod;
    }

    public BigDecimal getExistenciaAnteriorBobod() {
        return existenciaAnteriorBobod;
    }

    public void setExistenciaAnteriorBobod(BigDecimal existenciaAnteriorBobod) {
        this.existenciaAnteriorBobod = existenciaAnteriorBobod;
    }

    public BigDecimal getSaldoBobod() {
        return saldoBobod;
    }

    public void setSaldoBobod(BigDecimal saldoBobod) {
        this.saldoBobod = saldoBobod;
    }

    public Boolean getActivoBobod() {
        return activoBobod;
    }

    public void setActivoBobod(Boolean activoBobod) {
        this.activoBobod = activoBobod;
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

    public BigInteger getIdeAdsoc() {
        return ideAdsoc;
    }

    public void setIdeAdsoc(BigInteger ideAdsoc) {
        this.ideAdsoc = ideAdsoc;
    }

    public TesProveedor getIdeTepro() {
        return ideTepro;
    }

    public void setIdeTepro(TesProveedor ideTepro) {
        this.ideTepro = ideTepro;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public BodtMaterial getIdeBomat() {
        return ideBomat;
    }

    public void setIdeBomat(BodtMaterial ideBomat) {
        this.ideBomat = ideBomat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBobod != null ? ideBobod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtBodega)) {
            return false;
        }
        BodtBodega other = (BodtBodega) object;
        if ((this.ideBobod == null && other.ideBobod != null) || (this.ideBobod != null && !this.ideBobod.equals(other.ideBobod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtBodega[ ideBobod=" + ideBobod + " ]";
    }
    
}
