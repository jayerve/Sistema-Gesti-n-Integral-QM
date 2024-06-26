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
@Table(name = "fac_datos_factura", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "FacDatosFactura.findAll", query = "SELECT f FROM FacDatosFactura f"),
    @NamedQuery(name = "FacDatosFactura.findByIdeFadaf", query = "SELECT f FROM FacDatosFactura f WHERE f.ideFadaf = :ideFadaf"),
    @NamedQuery(name = "FacDatosFactura.findByIdeSucu", query = "SELECT f FROM FacDatosFactura f WHERE f.ideSucu = :ideSucu"),
    @NamedQuery(name = "FacDatosFactura.findByIdeGedep", query = "SELECT f FROM FacDatosFactura f WHERE f.ideGedep = :ideGedep"),
    @NamedQuery(name = "FacDatosFactura.findBySerieFacturaFadaf", query = "SELECT f FROM FacDatosFactura f WHERE f.serieFacturaFadaf = :serieFacturaFadaf"),
    @NamedQuery(name = "FacDatosFactura.findByFechaImpresionFadaf", query = "SELECT f FROM FacDatosFactura f WHERE f.fechaImpresionFadaf = :fechaImpresionFadaf"),
    @NamedQuery(name = "FacDatosFactura.findByFechaVencimientoFadaf", query = "SELECT f FROM FacDatosFactura f WHERE f.fechaVencimientoFadaf = :fechaVencimientoFadaf"),
    @NamedQuery(name = "FacDatosFactura.findByCantidadFadaf", query = "SELECT f FROM FacDatosFactura f WHERE f.cantidadFadaf = :cantidadFadaf"),
    @NamedQuery(name = "FacDatosFactura.findByObservacionFadaf", query = "SELECT f FROM FacDatosFactura f WHERE f.observacionFadaf = :observacionFadaf"),
    @NamedQuery(name = "FacDatosFactura.findByNumMaxImpresionFadaf", query = "SELECT f FROM FacDatosFactura f WHERE f.numMaxImpresionFadaf = :numMaxImpresionFadaf"),
    @NamedQuery(name = "FacDatosFactura.findByActivoFadaf", query = "SELECT f FROM FacDatosFactura f WHERE f.activoFadaf = :activoFadaf"),
    @NamedQuery(name = "FacDatosFactura.findByUsuarioIngre", query = "SELECT f FROM FacDatosFactura f WHERE f.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "FacDatosFactura.findByFechaIngre", query = "SELECT f FROM FacDatosFactura f WHERE f.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "FacDatosFactura.findByHoraIngre", query = "SELECT f FROM FacDatosFactura f WHERE f.horaIngre = :horaIngre"),
    @NamedQuery(name = "FacDatosFactura.findByUsuarioActua", query = "SELECT f FROM FacDatosFactura f WHERE f.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "FacDatosFactura.findByFechaActua", query = "SELECT f FROM FacDatosFactura f WHERE f.fechaActua = :fechaActua"),
    @NamedQuery(name = "FacDatosFactura.findByHoraActua", query = "SELECT f FROM FacDatosFactura f WHERE f.horaActua = :horaActua"),
    @NamedQuery(name = "FacDatosFactura.findByIdeFapuv", query = "SELECT f FROM FacDatosFactura f WHERE f.ideFapuv = :ideFapuv")})
public class FacDatosFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_fadaf", nullable = false)
    private Long ideFadaf;
    @Column(name = "ide_sucu")
    private BigInteger ideSucu;
    @Column(name = "ide_gedep")
    private BigInteger ideGedep;
    @Size(max = 20)
    @Column(name = "serie_factura_fadaf", length = 20)
    private String serieFacturaFadaf;
    @Column(name = "fecha_impresion_fadaf")
    @Temporal(TemporalType.DATE)
    private Date fechaImpresionFadaf;
    @Column(name = "fecha_vencimiento_fadaf")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimientoFadaf;
    @Column(name = "cantidad_fadaf")
    private BigInteger cantidadFadaf;
    @Size(max = 2147483647)
    @Column(name = "observacion_fadaf", length = 2147483647)
    private String observacionFadaf;
    @Column(name = "num_max_impresion_fadaf")
    private BigInteger numMaxImpresionFadaf;
    @Column(name = "activo_fadaf")
    private Boolean activoFadaf;
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
    @Column(name = "ide_fapuv")
    private BigInteger ideFapuv;
    @OneToMany(mappedBy = "ideFadaf")
    private List<FacFactura> facFacturaList;
    @JoinColumn(name = "ide_bogrm", referencedColumnName = "ide_bogrm")
    @ManyToOne
    private BodtGrupoMaterial ideBogrm;

    public FacDatosFactura() {
    }

    public FacDatosFactura(Long ideFadaf) {
        this.ideFadaf = ideFadaf;
    }

    public Long getIdeFadaf() {
        return ideFadaf;
    }

    public void setIdeFadaf(Long ideFadaf) {
        this.ideFadaf = ideFadaf;
    }

    public BigInteger getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(BigInteger ideSucu) {
        this.ideSucu = ideSucu;
    }

    public BigInteger getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(BigInteger ideGedep) {
        this.ideGedep = ideGedep;
    }

    public String getSerieFacturaFadaf() {
        return serieFacturaFadaf;
    }

    public void setSerieFacturaFadaf(String serieFacturaFadaf) {
        this.serieFacturaFadaf = serieFacturaFadaf;
    }

    public Date getFechaImpresionFadaf() {
        return fechaImpresionFadaf;
    }

    public void setFechaImpresionFadaf(Date fechaImpresionFadaf) {
        this.fechaImpresionFadaf = fechaImpresionFadaf;
    }

    public Date getFechaVencimientoFadaf() {
        return fechaVencimientoFadaf;
    }

    public void setFechaVencimientoFadaf(Date fechaVencimientoFadaf) {
        this.fechaVencimientoFadaf = fechaVencimientoFadaf;
    }

    public BigInteger getCantidadFadaf() {
        return cantidadFadaf;
    }

    public void setCantidadFadaf(BigInteger cantidadFadaf) {
        this.cantidadFadaf = cantidadFadaf;
    }

    public String getObservacionFadaf() {
        return observacionFadaf;
    }

    public void setObservacionFadaf(String observacionFadaf) {
        this.observacionFadaf = observacionFadaf;
    }

    public BigInteger getNumMaxImpresionFadaf() {
        return numMaxImpresionFadaf;
    }

    public void setNumMaxImpresionFadaf(BigInteger numMaxImpresionFadaf) {
        this.numMaxImpresionFadaf = numMaxImpresionFadaf;
    }

    public Boolean getActivoFadaf() {
        return activoFadaf;
    }

    public void setActivoFadaf(Boolean activoFadaf) {
        this.activoFadaf = activoFadaf;
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

    public BigInteger getIdeFapuv() {
        return ideFapuv;
    }

    public void setIdeFapuv(BigInteger ideFapuv) {
        this.ideFapuv = ideFapuv;
    }

    public List<FacFactura> getFacFacturaList() {
        return facFacturaList;
    }

    public void setFacFacturaList(List<FacFactura> facFacturaList) {
        this.facFacturaList = facFacturaList;
    }

    public BodtGrupoMaterial getIdeBogrm() {
        return ideBogrm;
    }

    public void setIdeBogrm(BodtGrupoMaterial ideBogrm) {
        this.ideBogrm = ideBogrm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFadaf != null ? ideFadaf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDatosFactura)) {
            return false;
        }
        FacDatosFactura other = (FacDatosFactura) object;
        if ((this.ideFadaf == null && other.ideFadaf != null) || (this.ideFadaf != null && !this.ideFadaf.equals(other.ideFadaf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.FacDatosFactura[ ideFadaf=" + ideFadaf + " ]";
    }
    
}
