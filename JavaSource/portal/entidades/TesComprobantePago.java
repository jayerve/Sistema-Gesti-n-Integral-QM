/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "tes_comprobante_pago", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesComprobantePago.findAll", query = "SELECT t FROM TesComprobantePago t"),
    @NamedQuery(name = "TesComprobantePago.findByIdeTecpo", query = "SELECT t FROM TesComprobantePago t WHERE t.ideTecpo = :ideTecpo"),
    @NamedQuery(name = "TesComprobantePago.findByFechaTecpo", query = "SELECT t FROM TesComprobantePago t WHERE t.fechaTecpo = :fechaTecpo"),
    @NamedQuery(name = "TesComprobantePago.findByComprobanteEgresoTecpo", query = "SELECT t FROM TesComprobantePago t WHERE t.comprobanteEgresoTecpo = :comprobanteEgresoTecpo"),
    @NamedQuery(name = "TesComprobantePago.findByOrdenPagoTecpo", query = "SELECT t FROM TesComprobantePago t WHERE t.ordenPagoTecpo = :ordenPagoTecpo"),
    @NamedQuery(name = "TesComprobantePago.findByDetalleTecpo", query = "SELECT t FROM TesComprobantePago t WHERE t.detalleTecpo = :detalleTecpo"),
    @NamedQuery(name = "TesComprobantePago.findByFechaPagoTecpo", query = "SELECT t FROM TesComprobantePago t WHERE t.fechaPagoTecpo = :fechaPagoTecpo"),
    @NamedQuery(name = "TesComprobantePago.findByActivoTecpo", query = "SELECT t FROM TesComprobantePago t WHERE t.activoTecpo = :activoTecpo"),
    @NamedQuery(name = "TesComprobantePago.findByUsuarioIngre", query = "SELECT t FROM TesComprobantePago t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesComprobantePago.findByFechaIngre", query = "SELECT t FROM TesComprobantePago t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesComprobantePago.findByHoraIngre", query = "SELECT t FROM TesComprobantePago t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesComprobantePago.findByUsuarioActua", query = "SELECT t FROM TesComprobantePago t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesComprobantePago.findByFechaActua", query = "SELECT t FROM TesComprobantePago t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesComprobantePago.findByHoraActua", query = "SELECT t FROM TesComprobantePago t WHERE t.horaActua = :horaActua"),
    @NamedQuery(name = "TesComprobantePago.findByIdeComov", query = "SELECT t FROM TesComprobantePago t WHERE t.ideComov = :ideComov")})
public class TesComprobantePago implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tecpo", nullable = false)
    private Long ideTecpo;
    @Column(name = "fecha_tecpo")
    @Temporal(TemporalType.DATE)
    private Date fechaTecpo;
    @Size(max = 20)
    @Column(name = "comprobante_egreso_tecpo", length = 20)
    private String comprobanteEgresoTecpo;
    @Size(max = 20)
    @Column(name = "orden_pago_tecpo", length = 20)
    private String ordenPagoTecpo;
    @Size(max = 2147483647)
    @Column(name = "detalle_tecpo", length = 2147483647)
    private String detalleTecpo;
    @Column(name = "fecha_pago_tecpo")
    @Temporal(TemporalType.DATE)
    private Date fechaPagoTecpo;
    @Column(name = "activo_tecpo")
    private Boolean activoTecpo;
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
    @Column(name = "ide_comov")
    private Integer ideComov;
    @JoinColumn(name = "ide_tetic", referencedColumnName = "ide_tetic")
    @ManyToOne
    private TesTipoConcepto ideTetic;
    @JoinColumn(name = "ide_tepro", referencedColumnName = "ide_tepro")
    @ManyToOne
    private TesProveedor ideTepro;
    @JoinColumn(name = "ide_prtra", referencedColumnName = "ide_prtra")
    @ManyToOne
    private PreTramite idePrtra;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @JoinColumn(name = "ide_adsoc", referencedColumnName = "ide_adsoc")
    @ManyToOne
    private AdqSolicitudCompra ideAdsoc;
    @OneToMany(mappedBy = "ideTecpo")
    private List<ContMovimiento> contMovimientoList;

    public TesComprobantePago() {
    }

    public TesComprobantePago(Long ideTecpo) {
        this.ideTecpo = ideTecpo;
    }

    public Long getIdeTecpo() {
        return ideTecpo;
    }

    public void setIdeTecpo(Long ideTecpo) {
        this.ideTecpo = ideTecpo;
    }

    public Date getFechaTecpo() {
        return fechaTecpo;
    }

    public void setFechaTecpo(Date fechaTecpo) {
        this.fechaTecpo = fechaTecpo;
    }

    public String getComprobanteEgresoTecpo() {
        return comprobanteEgresoTecpo;
    }

    public void setComprobanteEgresoTecpo(String comprobanteEgresoTecpo) {
        this.comprobanteEgresoTecpo = comprobanteEgresoTecpo;
    }

    public String getOrdenPagoTecpo() {
        return ordenPagoTecpo;
    }

    public void setOrdenPagoTecpo(String ordenPagoTecpo) {
        this.ordenPagoTecpo = ordenPagoTecpo;
    }

    public String getDetalleTecpo() {
        return detalleTecpo;
    }

    public void setDetalleTecpo(String detalleTecpo) {
        this.detalleTecpo = detalleTecpo;
    }

    public Date getFechaPagoTecpo() {
        return fechaPagoTecpo;
    }

    public void setFechaPagoTecpo(Date fechaPagoTecpo) {
        this.fechaPagoTecpo = fechaPagoTecpo;
    }

    public Boolean getActivoTecpo() {
        return activoTecpo;
    }

    public void setActivoTecpo(Boolean activoTecpo) {
        this.activoTecpo = activoTecpo;
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

    public Integer getIdeComov() {
        return ideComov;
    }

    public void setIdeComov(Integer ideComov) {
        this.ideComov = ideComov;
    }

    public TesTipoConcepto getIdeTetic() {
        return ideTetic;
    }

    public void setIdeTetic(TesTipoConcepto ideTetic) {
        this.ideTetic = ideTetic;
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

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public AdqSolicitudCompra getIdeAdsoc() {
        return ideAdsoc;
    }

    public void setIdeAdsoc(AdqSolicitudCompra ideAdsoc) {
        this.ideAdsoc = ideAdsoc;
    }

    public List<ContMovimiento> getContMovimientoList() {
        return contMovimientoList;
    }

    public void setContMovimientoList(List<ContMovimiento> contMovimientoList) {
        this.contMovimientoList = contMovimientoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTecpo != null ? ideTecpo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesComprobantePago)) {
            return false;
        }
        TesComprobantePago other = (TesComprobantePago) object;
        if ((this.ideTecpo == null && other.ideTecpo != null) || (this.ideTecpo != null && !this.ideTecpo.equals(other.ideTecpo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesComprobantePago[ ideTecpo=" + ideTecpo + " ]";
    }
    
}
