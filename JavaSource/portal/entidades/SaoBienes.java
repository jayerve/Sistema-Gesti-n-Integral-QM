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
@Table(name = "sao_bienes", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoBienes.findAll", query = "SELECT s FROM SaoBienes s"),
    @NamedQuery(name = "SaoBienes.findByIdeSabie", query = "SELECT s FROM SaoBienes s WHERE s.ideSabie = :ideSabie"),
    @NamedQuery(name = "SaoBienes.findByCodigoBienSabie", query = "SELECT s FROM SaoBienes s WHERE s.codigoBienSabie = :codigoBienSabie"),
    @NamedQuery(name = "SaoBienes.findByNombreSabie", query = "SELECT s FROM SaoBienes s WHERE s.nombreSabie = :nombreSabie"),
    @NamedQuery(name = "SaoBienes.findByDescripcionSabie", query = "SELECT s FROM SaoBienes s WHERE s.descripcionSabie = :descripcionSabie"),
    @NamedQuery(name = "SaoBienes.findBySerieSabie", query = "SELECT s FROM SaoBienes s WHERE s.serieSabie = :serieSabie"),
    @NamedQuery(name = "SaoBienes.findByMarcaSabie", query = "SELECT s FROM SaoBienes s WHERE s.marcaSabie = :marcaSabie"),
    @NamedQuery(name = "SaoBienes.findByModeloSabie", query = "SELECT s FROM SaoBienes s WHERE s.modeloSabie = :modeloSabie"),
    @NamedQuery(name = "SaoBienes.findByCantidadSabie", query = "SELECT s FROM SaoBienes s WHERE s.cantidadSabie = :cantidadSabie"),
    @NamedQuery(name = "SaoBienes.findByFechaCompraSabie", query = "SELECT s FROM SaoBienes s WHERE s.fechaCompraSabie = :fechaCompraSabie"),
    @NamedQuery(name = "SaoBienes.findByFechaIngresoSabie", query = "SELECT s FROM SaoBienes s WHERE s.fechaIngresoSabie = :fechaIngresoSabie"),
    @NamedQuery(name = "SaoBienes.findByComprobanteEgresoSabie", query = "SELECT s FROM SaoBienes s WHERE s.comprobanteEgresoSabie = :comprobanteEgresoSabie"),
    @NamedQuery(name = "SaoBienes.findByFacturaSabie", query = "SELECT s FROM SaoBienes s WHERE s.facturaSabie = :facturaSabie"),
    @NamedQuery(name = "SaoBienes.findByValorCompraSabie", query = "SELECT s FROM SaoBienes s WHERE s.valorCompraSabie = :valorCompraSabie"),
    @NamedQuery(name = "SaoBienes.findByFotoSabie", query = "SELECT s FROM SaoBienes s WHERE s.fotoSabie = :fotoSabie"),
    @NamedQuery(name = "SaoBienes.findByActivoSabie", query = "SELECT s FROM SaoBienes s WHERE s.activoSabie = :activoSabie"),
    @NamedQuery(name = "SaoBienes.findByUsuarioIngre", query = "SELECT s FROM SaoBienes s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoBienes.findByFechaIngre", query = "SELECT s FROM SaoBienes s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoBienes.findByHoraIngre", query = "SELECT s FROM SaoBienes s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoBienes.findByUsuarioActua", query = "SELECT s FROM SaoBienes s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoBienes.findByFechaActua", query = "SELECT s FROM SaoBienes s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoBienes.findByHoraActua", query = "SELECT s FROM SaoBienes s WHERE s.horaActua = :horaActua")})
public class SaoBienes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sabie", nullable = false)
    private Integer ideSabie;
    @Size(max = 50)
    @Column(name = "codigo_bien_sabie", length = 50)
    private String codigoBienSabie;
    @Size(max = 100)
    @Column(name = "nombre_sabie", length = 100)
    private String nombreSabie;
    @Size(max = 1000)
    @Column(name = "descripcion_sabie", length = 1000)
    private String descripcionSabie;
    @Size(max = 50)
    @Column(name = "serie_sabie", length = 50)
    private String serieSabie;
    @Size(max = 50)
    @Column(name = "marca_sabie", length = 50)
    private String marcaSabie;
    @Size(max = 50)
    @Column(name = "modelo_sabie", length = 50)
    private String modeloSabie;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad_sabie", precision = 12, scale = 2)
    private BigDecimal cantidadSabie;
    @Column(name = "fecha_compra_sabie")
    @Temporal(TemporalType.DATE)
    private Date fechaCompraSabie;
    @Column(name = "fecha_ingreso_sabie")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoSabie;
    @Column(name = "comprobante_egreso_sabie")
    @Temporal(TemporalType.DATE)
    private Date comprobanteEgresoSabie;
    @Column(name = "factura_sabie", precision = 12, scale = 3)
    private BigDecimal facturaSabie;
    @Column(name = "valor_compra_sabie", precision = 12, scale = 4)
    private BigDecimal valorCompraSabie;
    @Size(max = 100)
    @Column(name = "foto_sabie", length = 100)
    private String fotoSabie;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sabie", nullable = false)
    private boolean activoSabie;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @OneToMany(mappedBy = "ideSabie")
    private List<SaoCustodio> saoCustodioList;
    @JoinColumn(name = "ide_saseg", referencedColumnName = "ide_saseg")
    @ManyToOne
    private SaoSeguro ideSaseg;
    @JoinColumn(name = "ide_sapro", referencedColumnName = "ide_sapro")
    @ManyToOne
    private SaoPropiedad ideSapro;
    @JoinColumn(name = "ide_sagrb", referencedColumnName = "ide_sagrb")
    @ManyToOne
    private SaoGrupoBien ideSagrb;
    @JoinColumn(name = "ide_saest", referencedColumnName = "ide_saest")
    @ManyToOne
    private SaoEstado ideSaest;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;

    public SaoBienes() {
    }

    public SaoBienes(Integer ideSabie) {
        this.ideSabie = ideSabie;
    }

    public SaoBienes(Integer ideSabie, boolean activoSabie) {
        this.ideSabie = ideSabie;
        this.activoSabie = activoSabie;
    }

    public Integer getIdeSabie() {
        return ideSabie;
    }

    public void setIdeSabie(Integer ideSabie) {
        this.ideSabie = ideSabie;
    }

    public String getCodigoBienSabie() {
        return codigoBienSabie;
    }

    public void setCodigoBienSabie(String codigoBienSabie) {
        this.codigoBienSabie = codigoBienSabie;
    }

    public String getNombreSabie() {
        return nombreSabie;
    }

    public void setNombreSabie(String nombreSabie) {
        this.nombreSabie = nombreSabie;
    }

    public String getDescripcionSabie() {
        return descripcionSabie;
    }

    public void setDescripcionSabie(String descripcionSabie) {
        this.descripcionSabie = descripcionSabie;
    }

    public String getSerieSabie() {
        return serieSabie;
    }

    public void setSerieSabie(String serieSabie) {
        this.serieSabie = serieSabie;
    }

    public String getMarcaSabie() {
        return marcaSabie;
    }

    public void setMarcaSabie(String marcaSabie) {
        this.marcaSabie = marcaSabie;
    }

    public String getModeloSabie() {
        return modeloSabie;
    }

    public void setModeloSabie(String modeloSabie) {
        this.modeloSabie = modeloSabie;
    }

    public BigDecimal getCantidadSabie() {
        return cantidadSabie;
    }

    public void setCantidadSabie(BigDecimal cantidadSabie) {
        this.cantidadSabie = cantidadSabie;
    }

    public Date getFechaCompraSabie() {
        return fechaCompraSabie;
    }

    public void setFechaCompraSabie(Date fechaCompraSabie) {
        this.fechaCompraSabie = fechaCompraSabie;
    }

    public Date getFechaIngresoSabie() {
        return fechaIngresoSabie;
    }

    public void setFechaIngresoSabie(Date fechaIngresoSabie) {
        this.fechaIngresoSabie = fechaIngresoSabie;
    }

    public Date getComprobanteEgresoSabie() {
        return comprobanteEgresoSabie;
    }

    public void setComprobanteEgresoSabie(Date comprobanteEgresoSabie) {
        this.comprobanteEgresoSabie = comprobanteEgresoSabie;
    }

    public BigDecimal getFacturaSabie() {
        return facturaSabie;
    }

    public void setFacturaSabie(BigDecimal facturaSabie) {
        this.facturaSabie = facturaSabie;
    }

    public BigDecimal getValorCompraSabie() {
        return valorCompraSabie;
    }

    public void setValorCompraSabie(BigDecimal valorCompraSabie) {
        this.valorCompraSabie = valorCompraSabie;
    }

    public String getFotoSabie() {
        return fotoSabie;
    }

    public void setFotoSabie(String fotoSabie) {
        this.fotoSabie = fotoSabie;
    }

    public boolean getActivoSabie() {
        return activoSabie;
    }

    public void setActivoSabie(boolean activoSabie) {
        this.activoSabie = activoSabie;
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

    public List<SaoCustodio> getSaoCustodioList() {
        return saoCustodioList;
    }

    public void setSaoCustodioList(List<SaoCustodio> saoCustodioList) {
        this.saoCustodioList = saoCustodioList;
    }

    public SaoSeguro getIdeSaseg() {
        return ideSaseg;
    }

    public void setIdeSaseg(SaoSeguro ideSaseg) {
        this.ideSaseg = ideSaseg;
    }

    public SaoPropiedad getIdeSapro() {
        return ideSapro;
    }

    public void setIdeSapro(SaoPropiedad ideSapro) {
        this.ideSapro = ideSapro;
    }

    public SaoGrupoBien getIdeSagrb() {
        return ideSagrb;
    }

    public void setIdeSagrb(SaoGrupoBien ideSagrb) {
        this.ideSagrb = ideSagrb;
    }

    public SaoEstado getIdeSaest() {
        return ideSaest;
    }

    public void setIdeSaest(SaoEstado ideSaest) {
        this.ideSaest = ideSaest;
    }

    public GenBeneficiario getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(GenBeneficiario ideGeben) {
        this.ideGeben = ideGeben;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSabie != null ? ideSabie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoBienes)) {
            return false;
        }
        SaoBienes other = (SaoBienes) object;
        if ((this.ideSabie == null && other.ideSabie != null) || (this.ideSabie != null && !this.ideSabie.equals(other.ideSabie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoBienes[ ideSabie=" + ideSabie + " ]";
    }
    
}
