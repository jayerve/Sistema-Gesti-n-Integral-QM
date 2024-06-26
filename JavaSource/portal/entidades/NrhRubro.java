/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "nrh_rubro", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhRubro.findAll", query = "SELECT n FROM NrhRubro n"),
    @NamedQuery(name = "NrhRubro.findByIdeNrrub", query = "SELECT n FROM NrhRubro n WHERE n.ideNrrub = :ideNrrub"),
    @NamedQuery(name = "NrhRubro.findByDetalleNrrub", query = "SELECT n FROM NrhRubro n WHERE n.detalleNrrub = :detalleNrrub"),
    @NamedQuery(name = "NrhRubro.findByActivoNrrub", query = "SELECT n FROM NrhRubro n WHERE n.activoNrrub = :activoNrrub"),
    @NamedQuery(name = "NrhRubro.findByUsuarioIngre", query = "SELECT n FROM NrhRubro n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRubro.findByFechaIngre", query = "SELECT n FROM NrhRubro n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRubro.findByUsuarioActua", query = "SELECT n FROM NrhRubro n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRubro.findByFechaActua", query = "SELECT n FROM NrhRubro n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRubro.findByHoraIngre", query = "SELECT n FROM NrhRubro n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRubro.findByHoraActua", query = "SELECT n FROM NrhRubro n WHERE n.horaActua = :horaActua"),
    @NamedQuery(name = "NrhRubro.findByAnticipoNrrub", query = "SELECT n FROM NrhRubro n WHERE n.anticipoNrrub = :anticipoNrrub"),
    @NamedQuery(name = "NrhRubro.findByDecimoNrrub", query = "SELECT n FROM NrhRubro n WHERE n.decimoNrrub = :decimoNrrub")})
public class NrhRubro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrrub", nullable = false)
    private Integer ideNrrub;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "detalle_nrrub", nullable = false, length = 200)
    private String detalleNrrub;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_nrrub", nullable = false)
    private boolean activoNrrub;
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
    @Column(name = "anticipo_nrrub")
    private Boolean anticipoNrrub;
    @Column(name = "decimo_nrrub")
    private Boolean decimoNrrub;
    @OneToMany(mappedBy = "ideNrrub")
    private List<NrhRetencionRubroDescuento> nrhRetencionRubroDescuentoList;
    @OneToMany(mappedBy = "ideNrrub")
    private List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList;
    @JoinColumn(name = "ide_nrtir", referencedColumnName = "ide_nrtir", nullable = false)
    @ManyToOne(optional = false)
    private NrhTipoRubro ideNrtir;
    @JoinColumn(name = "ide_nrfoc", referencedColumnName = "ide_nrfoc", nullable = false)
    @ManyToOne(optional = false)
    private NrhFormaCalculo ideNrfoc;
    @OneToMany(mappedBy = "ideNrrub")
    private List<NrhRubroAsiento> nrhRubroAsientoList;
    @OneToMany(mappedBy = "ideNrrub")
    private List<NrhAmortizacion> nrhAmortizacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrrub")
    private List<NrhRubroDetallePago> nrhRubroDetallePagoList;
    @OneToMany(mappedBy = "ideNrrub")
    private List<AsiMotivo> asiMotivoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrrub")
    private List<NrhDetalleRubro> nrhDetalleRubroList;

    public NrhRubro() {
    }

    public NrhRubro(Integer ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public NrhRubro(Integer ideNrrub, String detalleNrrub, boolean activoNrrub) {
        this.ideNrrub = ideNrrub;
        this.detalleNrrub = detalleNrrub;
        this.activoNrrub = activoNrrub;
    }

    public Integer getIdeNrrub() {
        return ideNrrub;
    }

    public void setIdeNrrub(Integer ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public String getDetalleNrrub() {
        return detalleNrrub;
    }

    public void setDetalleNrrub(String detalleNrrub) {
        this.detalleNrrub = detalleNrrub;
    }

    public boolean getActivoNrrub() {
        return activoNrrub;
    }

    public void setActivoNrrub(boolean activoNrrub) {
        this.activoNrrub = activoNrrub;
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

    public Boolean getAnticipoNrrub() {
        return anticipoNrrub;
    }

    public void setAnticipoNrrub(Boolean anticipoNrrub) {
        this.anticipoNrrub = anticipoNrrub;
    }

    public Boolean getDecimoNrrub() {
        return decimoNrrub;
    }

    public void setDecimoNrrub(Boolean decimoNrrub) {
        this.decimoNrrub = decimoNrrub;
    }

    public List<NrhRetencionRubroDescuento> getNrhRetencionRubroDescuentoList() {
        return nrhRetencionRubroDescuentoList;
    }

    public void setNrhRetencionRubroDescuentoList(List<NrhRetencionRubroDescuento> nrhRetencionRubroDescuentoList) {
        this.nrhRetencionRubroDescuentoList = nrhRetencionRubroDescuentoList;
    }

    public List<NrhDetalleFacturaGuarderia> getNrhDetalleFacturaGuarderiaList() {
        return nrhDetalleFacturaGuarderiaList;
    }

    public void setNrhDetalleFacturaGuarderiaList(List<NrhDetalleFacturaGuarderia> nrhDetalleFacturaGuarderiaList) {
        this.nrhDetalleFacturaGuarderiaList = nrhDetalleFacturaGuarderiaList;
    }

    public NrhTipoRubro getIdeNrtir() {
        return ideNrtir;
    }

    public void setIdeNrtir(NrhTipoRubro ideNrtir) {
        this.ideNrtir = ideNrtir;
    }

    public NrhFormaCalculo getIdeNrfoc() {
        return ideNrfoc;
    }

    public void setIdeNrfoc(NrhFormaCalculo ideNrfoc) {
        this.ideNrfoc = ideNrfoc;
    }

    public List<NrhRubroAsiento> getNrhRubroAsientoList() {
        return nrhRubroAsientoList;
    }

    public void setNrhRubroAsientoList(List<NrhRubroAsiento> nrhRubroAsientoList) {
        this.nrhRubroAsientoList = nrhRubroAsientoList;
    }

    public List<NrhAmortizacion> getNrhAmortizacionList() {
        return nrhAmortizacionList;
    }

    public void setNrhAmortizacionList(List<NrhAmortizacion> nrhAmortizacionList) {
        this.nrhAmortizacionList = nrhAmortizacionList;
    }

    public List<NrhRubroDetallePago> getNrhRubroDetallePagoList() {
        return nrhRubroDetallePagoList;
    }

    public void setNrhRubroDetallePagoList(List<NrhRubroDetallePago> nrhRubroDetallePagoList) {
        this.nrhRubroDetallePagoList = nrhRubroDetallePagoList;
    }

    public List<AsiMotivo> getAsiMotivoList() {
        return asiMotivoList;
    }

    public void setAsiMotivoList(List<AsiMotivo> asiMotivoList) {
        this.asiMotivoList = asiMotivoList;
    }

    public List<NrhDetalleRubro> getNrhDetalleRubroList() {
        return nrhDetalleRubroList;
    }

    public void setNrhDetalleRubroList(List<NrhDetalleRubro> nrhDetalleRubroList) {
        this.nrhDetalleRubroList = nrhDetalleRubroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrrub != null ? ideNrrub.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRubro)) {
            return false;
        }
        NrhRubro other = (NrhRubro) object;
        if ((this.ideNrrub == null && other.ideNrrub != null) || (this.ideNrrub != null && !this.ideNrrub.equals(other.ideNrrub))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRubro[ ideNrrub=" + ideNrrub + " ]";
    }
    
}
