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
@Table(name = "tes_poliza", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesPoliza.findAll", query = "SELECT t FROM TesPoliza t"),
    @NamedQuery(name = "TesPoliza.findByIdeTepol", query = "SELECT t FROM TesPoliza t WHERE t.ideTepol = :ideTepol"),
    @NamedQuery(name = "TesPoliza.findByNumeroPolizaTepol", query = "SELECT t FROM TesPoliza t WHERE t.numeroPolizaTepol = :numeroPolizaTepol"),
    @NamedQuery(name = "TesPoliza.findByFechaEmisionTepol", query = "SELECT t FROM TesPoliza t WHERE t.fechaEmisionTepol = :fechaEmisionTepol"),
    @NamedQuery(name = "TesPoliza.findByValorTepol", query = "SELECT t FROM TesPoliza t WHERE t.valorTepol = :valorTepol"),
    @NamedQuery(name = "TesPoliza.findByVigenciaDesdeTepol", query = "SELECT t FROM TesPoliza t WHERE t.vigenciaDesdeTepol = :vigenciaDesdeTepol"),
    @NamedQuery(name = "TesPoliza.findByVigenciaHastaTepol", query = "SELECT t FROM TesPoliza t WHERE t.vigenciaHastaTepol = :vigenciaHastaTepol"),
    @NamedQuery(name = "TesPoliza.findByActivoTeplo", query = "SELECT t FROM TesPoliza t WHERE t.activoTeplo = :activoTeplo"),
    @NamedQuery(name = "TesPoliza.findByUsuarioIngre", query = "SELECT t FROM TesPoliza t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesPoliza.findByFechaIngre", query = "SELECT t FROM TesPoliza t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesPoliza.findByHoraIngre", query = "SELECT t FROM TesPoliza t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesPoliza.findByUsuarioActua", query = "SELECT t FROM TesPoliza t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesPoliza.findByFechaActua", query = "SELECT t FROM TesPoliza t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesPoliza.findByHoraActua", query = "SELECT t FROM TesPoliza t WHERE t.horaActua = :horaActua"),
    @NamedQuery(name = "TesPoliza.findBySecuencialTepol", query = "SELECT t FROM TesPoliza t WHERE t.secuencialTepol = :secuencialTepol")})
public class TesPoliza implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tepol", nullable = false)
    private Long ideTepol;
    @Size(max = 50)
    @Column(name = "numero_poliza_tepol", length = 50)
    private String numeroPolizaTepol;
    @Column(name = "fecha_emision_tepol")
    @Temporal(TemporalType.DATE)
    private Date fechaEmisionTepol;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_tepol", precision = 10, scale = 2)
    private BigDecimal valorTepol;
    @Column(name = "vigencia_desde_tepol")
    @Temporal(TemporalType.DATE)
    private Date vigenciaDesdeTepol;
    @Column(name = "vigencia_hasta_tepol")
    @Temporal(TemporalType.DATE)
    private Date vigenciaHastaTepol;
    @Column(name = "activo_teplo")
    private Boolean activoTeplo;
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
    @Column(name = "secuencial_tepol")
    private Integer secuencialTepol;
    @JoinColumn(name = "ide_tetip", referencedColumnName = "ide_tetip")
    @ManyToOne
    private TesTipoPoliza ideTetip;
    @JoinColumn(name = "ide_prcon", referencedColumnName = "ide_prcon")
    @ManyToOne
    private PreContrato idePrcon;
    @JoinColumn(name = "ide_gemos", referencedColumnName = "ide_gemos")
    @ManyToOne
    private GenModuloSecuencial ideGemos;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @OneToMany(mappedBy = "ideTepol")
    private List<TesArchivo> tesArchivoList;
    @OneToMany(mappedBy = "ideTepol")
    private List<TesRenovacionPoliza> tesRenovacionPolizaList;

    public TesPoliza() {
    }

    public TesPoliza(Long ideTepol) {
        this.ideTepol = ideTepol;
    }

    public Long getIdeTepol() {
        return ideTepol;
    }

    public void setIdeTepol(Long ideTepol) {
        this.ideTepol = ideTepol;
    }

    public String getNumeroPolizaTepol() {
        return numeroPolizaTepol;
    }

    public void setNumeroPolizaTepol(String numeroPolizaTepol) {
        this.numeroPolizaTepol = numeroPolizaTepol;
    }

    public Date getFechaEmisionTepol() {
        return fechaEmisionTepol;
    }

    public void setFechaEmisionTepol(Date fechaEmisionTepol) {
        this.fechaEmisionTepol = fechaEmisionTepol;
    }

    public BigDecimal getValorTepol() {
        return valorTepol;
    }

    public void setValorTepol(BigDecimal valorTepol) {
        this.valorTepol = valorTepol;
    }

    public Date getVigenciaDesdeTepol() {
        return vigenciaDesdeTepol;
    }

    public void setVigenciaDesdeTepol(Date vigenciaDesdeTepol) {
        this.vigenciaDesdeTepol = vigenciaDesdeTepol;
    }

    public Date getVigenciaHastaTepol() {
        return vigenciaHastaTepol;
    }

    public void setVigenciaHastaTepol(Date vigenciaHastaTepol) {
        this.vigenciaHastaTepol = vigenciaHastaTepol;
    }

    public Boolean getActivoTeplo() {
        return activoTeplo;
    }

    public void setActivoTeplo(Boolean activoTeplo) {
        this.activoTeplo = activoTeplo;
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

    public Integer getSecuencialTepol() {
        return secuencialTepol;
    }

    public void setSecuencialTepol(Integer secuencialTepol) {
        this.secuencialTepol = secuencialTepol;
    }

    public TesTipoPoliza getIdeTetip() {
        return ideTetip;
    }

    public void setIdeTetip(TesTipoPoliza ideTetip) {
        this.ideTetip = ideTetip;
    }

    public PreContrato getIdePrcon() {
        return idePrcon;
    }

    public void setIdePrcon(PreContrato idePrcon) {
        this.idePrcon = idePrcon;
    }

    public GenModuloSecuencial getIdeGemos() {
        return ideGemos;
    }

    public void setIdeGemos(GenModuloSecuencial ideGemos) {
        this.ideGemos = ideGemos;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public List<TesArchivo> getTesArchivoList() {
        return tesArchivoList;
    }

    public void setTesArchivoList(List<TesArchivo> tesArchivoList) {
        this.tesArchivoList = tesArchivoList;
    }

    public List<TesRenovacionPoliza> getTesRenovacionPolizaList() {
        return tesRenovacionPolizaList;
    }

    public void setTesRenovacionPolizaList(List<TesRenovacionPoliza> tesRenovacionPolizaList) {
        this.tesRenovacionPolizaList = tesRenovacionPolizaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTepol != null ? ideTepol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesPoliza)) {
            return false;
        }
        TesPoliza other = (TesPoliza) object;
        if ((this.ideTepol == null && other.ideTepol != null) || (this.ideTepol != null && !this.ideTepol.equals(other.ideTepol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesPoliza[ ideTepol=" + ideTepol + " ]";
    }
    
}
