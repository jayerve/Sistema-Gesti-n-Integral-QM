/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "bodt_material", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtMaterial.findAll", query = "SELECT b FROM BodtMaterial b"),
    @NamedQuery(name = "BodtMaterial.findByIdeBomat", query = "SELECT b FROM BodtMaterial b WHERE b.ideBomat = :ideBomat"),
    @NamedQuery(name = "BodtMaterial.findByCodigoBomat", query = "SELECT b FROM BodtMaterial b WHERE b.codigoBomat = :codigoBomat"),
    @NamedQuery(name = "BodtMaterial.findByDetalleBomat", query = "SELECT b FROM BodtMaterial b WHERE b.detalleBomat = :detalleBomat"),
    @NamedQuery(name = "BodtMaterial.findByIvaBomat", query = "SELECT b FROM BodtMaterial b WHERE b.ivaBomat = :ivaBomat"),
    @NamedQuery(name = "BodtMaterial.findByObservacionBomat", query = "SELECT b FROM BodtMaterial b WHERE b.observacionBomat = :observacionBomat"),
    @NamedQuery(name = "BodtMaterial.findByIceBomat", query = "SELECT b FROM BodtMaterial b WHERE b.iceBomat = :iceBomat"),
    @NamedQuery(name = "BodtMaterial.findByAplicaValorBomat", query = "SELECT b FROM BodtMaterial b WHERE b.aplicaValorBomat = :aplicaValorBomat"),
    @NamedQuery(name = "BodtMaterial.findByFotoBomat", query = "SELECT b FROM BodtMaterial b WHERE b.fotoBomat = :fotoBomat"),
    @NamedQuery(name = "BodtMaterial.findByActivoBomat", query = "SELECT b FROM BodtMaterial b WHERE b.activoBomat = :activoBomat"),
    @NamedQuery(name = "BodtMaterial.findByUsuarioIngre", query = "SELECT b FROM BodtMaterial b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtMaterial.findByFechaIngre", query = "SELECT b FROM BodtMaterial b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtMaterial.findByHoraIngre", query = "SELECT b FROM BodtMaterial b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtMaterial.findByUsuarioActua", query = "SELECT b FROM BodtMaterial b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtMaterial.findByFechaActua", query = "SELECT b FROM BodtMaterial b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtMaterial.findByHoraActua", query = "SELECT b FROM BodtMaterial b WHERE b.horaActua = :horaActua")})
public class BodtMaterial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_bomat", nullable = false)
    private Long ideBomat;
    @Size(max = 50)
    @Column(name = "codigo_bomat", length = 50)
    private String codigoBomat;
    @Size(max = 250)
    @Column(name = "detalle_bomat", length = 250)
    private String detalleBomat;
    @Column(name = "iva_bomat")
    private BigInteger ivaBomat;
    @Size(max = 2147483647)
    @Column(name = "observacion_bomat", length = 2147483647)
    private String observacionBomat;
    @Column(name = "ice_bomat")
    private Boolean iceBomat;
    @Column(name = "aplica_valor_bomat")
    private Boolean aplicaValorBomat;
    @Size(max = 250)
    @Column(name = "foto_bomat", length = 250)
    private String fotoBomat;
    @Column(name = "activo_bomat")
    private Boolean activoBomat;
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
    @OneToMany(mappedBy = "ideBomat")
    private List<BodtBodega> bodtBodegaList;
    @JoinColumn(name = "ide_bounm", referencedColumnName = "ide_bounm")
    @ManyToOne
    private BodtUnidadMedida ideBounm;
    @JoinColumn(name = "ide_botip", referencedColumnName = "ide_botip")
    @ManyToOne
    private BodtTipoProducto ideBotip;
    @JoinColumn(name = "ide_bogrm", referencedColumnName = "ide_bogrm")
    @ManyToOne
    private BodtGrupoMaterial ideBogrm;
    @OneToMany(mappedBy = "ideBomat")
    private List<FacDetalleFactura> facDetalleFacturaList;
    @OneToMany(mappedBy = "ideBomat")
    private List<AdqDetalleFactura> adqDetalleFacturaList;
    //@OneToMany(mappedBy = "ideBomat")
    //private List<BodtEgreso> bodtEgresoList;
    @OneToMany(mappedBy = "ideBomat")
    private List<TesMaterialTarifa> tesMaterialTarifaList;

    public BodtMaterial() {
    }

    public BodtMaterial(Long ideBomat) {
        this.ideBomat = ideBomat;
    }

    public Long getIdeBomat() {
        return ideBomat;
    }

    public void setIdeBomat(Long ideBomat) {
        this.ideBomat = ideBomat;
    }

    public String getCodigoBomat() {
        return codigoBomat;
    }

    public void setCodigoBomat(String codigoBomat) {
        this.codigoBomat = codigoBomat;
    }

    public String getDetalleBomat() {
        return detalleBomat;
    }

    public void setDetalleBomat(String detalleBomat) {
        this.detalleBomat = detalleBomat;
    }

    public BigInteger getIvaBomat() {
        return ivaBomat;
    }

    public void setIvaBomat(BigInteger ivaBomat) {
        this.ivaBomat = ivaBomat;
    }

    public String getObservacionBomat() {
        return observacionBomat;
    }

    public void setObservacionBomat(String observacionBomat) {
        this.observacionBomat = observacionBomat;
    }

    public Boolean getIceBomat() {
        return iceBomat;
    }

    public void setIceBomat(Boolean iceBomat) {
        this.iceBomat = iceBomat;
    }

    public Boolean getAplicaValorBomat() {
        return aplicaValorBomat;
    }

    public void setAplicaValorBomat(Boolean aplicaValorBomat) {
        this.aplicaValorBomat = aplicaValorBomat;
    }

    public String getFotoBomat() {
        return fotoBomat;
    }

    public void setFotoBomat(String fotoBomat) {
        this.fotoBomat = fotoBomat;
    }

    public Boolean getActivoBomat() {
        return activoBomat;
    }

    public void setActivoBomat(Boolean activoBomat) {
        this.activoBomat = activoBomat;
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

    public List<BodtBodega> getBodtBodegaList() {
        return bodtBodegaList;
    }

    public void setBodtBodegaList(List<BodtBodega> bodtBodegaList) {
        this.bodtBodegaList = bodtBodegaList;
    }

    public BodtUnidadMedida getIdeBounm() {
        return ideBounm;
    }

    public void setIdeBounm(BodtUnidadMedida ideBounm) {
        this.ideBounm = ideBounm;
    }

    public BodtTipoProducto getIdeBotip() {
        return ideBotip;
    }

    public void setIdeBotip(BodtTipoProducto ideBotip) {
        this.ideBotip = ideBotip;
    }

    public BodtGrupoMaterial getIdeBogrm() {
        return ideBogrm;
    }

    public void setIdeBogrm(BodtGrupoMaterial ideBogrm) {
        this.ideBogrm = ideBogrm;
    }

    public List<FacDetalleFactura> getFacDetalleFacturaList() {
        return facDetalleFacturaList;
    }

    public void setFacDetalleFacturaList(List<FacDetalleFactura> facDetalleFacturaList) {
        this.facDetalleFacturaList = facDetalleFacturaList;
    }

    public List<AdqDetalleFactura> getAdqDetalleFacturaList() {
        return adqDetalleFacturaList;
    }

    public void setAdqDetalleFacturaList(List<AdqDetalleFactura> adqDetalleFacturaList) {
        this.adqDetalleFacturaList = adqDetalleFacturaList;
    }

    /*public List<BodtEgreso> getBodtEgresoList() {
        return bodtEgresoList;
    }

    public void setBodtEgresoList(List<BodtEgreso> bodtEgresoList) {
        this.bodtEgresoList = bodtEgresoList;
    }*/

    public List<TesMaterialTarifa> getTesMaterialTarifaList() {
        return tesMaterialTarifaList;
    }

    public void setTesMaterialTarifaList(List<TesMaterialTarifa> tesMaterialTarifaList) {
        this.tesMaterialTarifaList = tesMaterialTarifaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBomat != null ? ideBomat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtMaterial)) {
            return false;
        }
        BodtMaterial other = (BodtMaterial) object;
        if ((this.ideBomat == null && other.ideBomat != null) || (this.ideBomat != null && !this.ideBomat.equals(other.ideBomat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtMaterial[ ideBomat=" + ideBomat + " ]";
    }
    
}
