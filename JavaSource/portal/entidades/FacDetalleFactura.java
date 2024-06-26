/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "fac_detalle_factura", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "FacDetalleFactura.findAll", query = "SELECT f FROM FacDetalleFactura f"),
    @NamedQuery(name = "FacDetalleFactura.findByIdeFadef", query = "SELECT f FROM FacDetalleFactura f WHERE f.ideFadef = :ideFadef"),
    @NamedQuery(name = "FacDetalleFactura.findByCantidadFadef", query = "SELECT f FROM FacDetalleFactura f WHERE f.cantidadFadef = :cantidadFadef"),
    @NamedQuery(name = "FacDetalleFactura.findByValorFadef", query = "SELECT f FROM FacDetalleFactura f WHERE f.valorFadef = :valorFadef"),
    @NamedQuery(name = "FacDetalleFactura.findByFechaFadef", query = "SELECT f FROM FacDetalleFactura f WHERE f.fechaFadef = :fechaFadef"),
    @NamedQuery(name = "FacDetalleFactura.findByTotalFadef", query = "SELECT f FROM FacDetalleFactura f WHERE f.totalFadef = :totalFadef"),
    @NamedQuery(name = "FacDetalleFactura.findByObservacionFadef", query = "SELECT f FROM FacDetalleFactura f WHERE f.observacionFadef = :observacionFadef"),
    @NamedQuery(name = "FacDetalleFactura.findByActivoFadef", query = "SELECT f FROM FacDetalleFactura f WHERE f.activoFadef = :activoFadef"),
    @NamedQuery(name = "FacDetalleFactura.findByUsuarioIngre", query = "SELECT f FROM FacDetalleFactura f WHERE f.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "FacDetalleFactura.findByFechaIngre", query = "SELECT f FROM FacDetalleFactura f WHERE f.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "FacDetalleFactura.findByHoraIngre", query = "SELECT f FROM FacDetalleFactura f WHERE f.horaIngre = :horaIngre"),
    @NamedQuery(name = "FacDetalleFactura.findByUsuarioActua", query = "SELECT f FROM FacDetalleFactura f WHERE f.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "FacDetalleFactura.findByFechaActua", query = "SELECT f FROM FacDetalleFactura f WHERE f.fechaActua = :fechaActua"),
    @NamedQuery(name = "FacDetalleFactura.findByHoraActua", query = "SELECT f FROM FacDetalleFactura f WHERE f.horaActua = :horaActua")})
public class FacDetalleFactura implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_fadef", nullable = false)
    private Integer ideFadef;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad_fadef", precision = 10, scale = 1)
    private BigDecimal cantidadFadef;
    @Column(name = "valor_fadef", precision = 10, scale = 3)
    private BigDecimal valorFadef;
    @Column(name = "fecha_fadef")
    @Temporal(TemporalType.DATE)
    private Date fechaFadef;
    @Column(name = "total_fadef", precision = 10, scale = 3)
    private BigDecimal totalFadef;
    @Size(max = 2147483647)
    @Column(name = "observacion_fadef", length = 2147483647)
    private String observacionFadef;
    @Column(name = "activo_fadef")
    private Boolean activoFadef;
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
    @JoinColumn(name = "ide_fafac", referencedColumnName = "ide_fafac")
    @ManyToOne
    private FacFactura ideFafac;
    @JoinColumn(name = "ide_bomat", referencedColumnName = "ide_bomat")
    @ManyToOne
    private BodtMaterial ideBomat;

    public FacDetalleFactura() {
    }

    public FacDetalleFactura(Integer ideFadef) {
        this.ideFadef = ideFadef;
    }

    public Integer getIdeFadef() {
        return ideFadef;
    }

    public void setIdeFadef(Integer ideFadef) {
        this.ideFadef = ideFadef;
    }

    public BigDecimal getCantidadFadef() {
        return cantidadFadef;
    }

    public void setCantidadFadef(BigDecimal cantidadFadef) {
        this.cantidadFadef = cantidadFadef;
    }

    public BigDecimal getValorFadef() {
        return valorFadef;
    }

    public void setValorFadef(BigDecimal valorFadef) {
        this.valorFadef = valorFadef;
    }

    public Date getFechaFadef() {
        return fechaFadef;
    }

    public void setFechaFadef(Date fechaFadef) {
        this.fechaFadef = fechaFadef;
    }

    public BigDecimal getTotalFadef() {
        return totalFadef;
    }

    public void setTotalFadef(BigDecimal totalFadef) {
        this.totalFadef = totalFadef;
    }

    public String getObservacionFadef() {
        return observacionFadef;
    }

    public void setObservacionFadef(String observacionFadef) {
        this.observacionFadef = observacionFadef;
    }

    public Boolean getActivoFadef() {
        return activoFadef;
    }

    public void setActivoFadef(Boolean activoFadef) {
        this.activoFadef = activoFadef;
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

    public FacFactura getIdeFafac() {
        return ideFafac;
    }

    public void setIdeFafac(FacFactura ideFafac) {
        this.ideFafac = ideFafac;
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
        hash += (ideFadef != null ? ideFadef.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacDetalleFactura)) {
            return false;
        }
        FacDetalleFactura other = (FacDetalleFactura) object;
        if ((this.ideFadef == null && other.ideFadef != null) || (this.ideFadef != null && !this.ideFadef.equals(other.ideFadef))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.FacDetalleFactura[ ideFadef=" + ideFadef + " ]";
    }
    
}
