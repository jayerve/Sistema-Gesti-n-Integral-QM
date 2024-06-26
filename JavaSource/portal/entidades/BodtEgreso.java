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
@Table(name = "bodt_egreso", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtEgreso.findAll", query = "SELECT b FROM BodtEgreso b"),
    @NamedQuery(name = "BodtEgreso.findByIdeBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.ideBoegr = :ideBoegr"),
    @NamedQuery(name = "BodtEgreso.findByFechaEgresoBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.fechaEgresoBoegr = :fechaEgresoBoegr"),
    @NamedQuery(name = "BodtEgreso.findByCantidadEgresoBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.cantidadEgresoBoegr = :cantidadEgresoBoegr"),
    @NamedQuery(name = "BodtEgreso.findByDocumentoEgresoBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.documentoEgresoBoegr = :documentoEgresoBoegr"),
    @NamedQuery(name = "BodtEgreso.findByCostoEgresoBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.costoEgresoBoegr = :costoEgresoBoegr"),
    @NamedQuery(name = "BodtEgreso.findByTotalEgresoBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.totalEgresoBoegr = :totalEgresoBoegr"),
    @NamedQuery(name = "BodtEgreso.findByExistenciasBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.existenciasBoegr = :existenciasBoegr"),
    @NamedQuery(name = "BodtEgreso.findByCostoAnteriorBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.costoAnteriorBoegr = :costoAnteriorBoegr"),
    @NamedQuery(name = "BodtEgreso.findByFechaIngresoArticuloBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.fechaIngresoArticuloBoegr = :fechaIngresoArticuloBoegr"),
    @NamedQuery(name = "BodtEgreso.findByActivoBoegr", query = "SELECT b FROM BodtEgreso b WHERE b.activoBoegr = :activoBoegr"),
    @NamedQuery(name = "BodtEgreso.findByUsuarioIngre", query = "SELECT b FROM BodtEgreso b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtEgreso.findByFechaIngre", query = "SELECT b FROM BodtEgreso b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtEgreso.findByHoraIngre", query = "SELECT b FROM BodtEgreso b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtEgreso.findByUsuarioActua", query = "SELECT b FROM BodtEgreso b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtEgreso.findByFechaActua", query = "SELECT b FROM BodtEgreso b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtEgreso.findByHoraActua", query = "SELECT b FROM BodtEgreso b WHERE b.horaActua = :horaActua")})
public class BodtEgreso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_boegr", nullable = false)
    private Long ideBoegr;
    @Column(name = "fecha_egreso_boegr")
    @Temporal(TemporalType.DATE)
    private Date fechaEgresoBoegr;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad_egreso_boegr", precision = 10, scale = 4)
    private BigDecimal cantidadEgresoBoegr;
    @Size(max = 20)
    @Column(name = "documento_egreso_boegr", length = 20)
    private String documentoEgresoBoegr;
    @Column(name = "costo_egreso_boegr", precision = 10, scale = 4)
    private BigDecimal costoEgresoBoegr;
    @Column(name = "total_egreso_boegr", precision = 10, scale = 4)
    private BigDecimal totalEgresoBoegr;
    @Column(name = "existencias_boegr", precision = 10, scale = 4)
    private BigDecimal existenciasBoegr;
    @Column(name = "costo_anterior_boegr", precision = 10, scale = 4)
    private BigDecimal costoAnteriorBoegr;
    @Column(name = "fecha_ingreso_articulo_boegr")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoArticuloBoegr;
    @Column(name = "activo_boegr")
    private Boolean activoBoegr;
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
    //@JoinColumn(name = "ide_bomat", referencedColumnName = "ide_bomat")
    //@ManyToOne
    //private BodtMaterial ideBomat;
    @JoinColumn(name = "ide_boinv", referencedColumnName = "ide_boinv")
    @ManyToOne
    private BodtInventario ideBoinv;
    @JoinColumn(name = "ide_bocoe", referencedColumnName = "ide_bocoe")
    @ManyToOne
    private BodtConceptoEgreso ideBocoe;

    public BodtEgreso() {
    }

    public BodtEgreso(Long ideBoegr) {
        this.ideBoegr = ideBoegr;
    }

    public Long getIdeBoegr() {
        return ideBoegr;
    }

    public void setIdeBoegr(Long ideBoegr) {
        this.ideBoegr = ideBoegr;
    }

    public Date getFechaEgresoBoegr() {
        return fechaEgresoBoegr;
    }

    public void setFechaEgresoBoegr(Date fechaEgresoBoegr) {
        this.fechaEgresoBoegr = fechaEgresoBoegr;
    }

    public BigDecimal getCantidadEgresoBoegr() {
        return cantidadEgresoBoegr;
    }

    public void setCantidadEgresoBoegr(BigDecimal cantidadEgresoBoegr) {
        this.cantidadEgresoBoegr = cantidadEgresoBoegr;
    }

    public String getDocumentoEgresoBoegr() {
        return documentoEgresoBoegr;
    }

    public void setDocumentoEgresoBoegr(String documentoEgresoBoegr) {
        this.documentoEgresoBoegr = documentoEgresoBoegr;
    }

    public BigDecimal getCostoEgresoBoegr() {
        return costoEgresoBoegr;
    }

    public void setCostoEgresoBoegr(BigDecimal costoEgresoBoegr) {
        this.costoEgresoBoegr = costoEgresoBoegr;
    }

    public BigDecimal getTotalEgresoBoegr() {
        return totalEgresoBoegr;
    }

    public void setTotalEgresoBoegr(BigDecimal totalEgresoBoegr) {
        this.totalEgresoBoegr = totalEgresoBoegr;
    }

    public BigDecimal getExistenciasBoegr() {
        return existenciasBoegr;
    }

    public void setExistenciasBoegr(BigDecimal existenciasBoegr) {
        this.existenciasBoegr = existenciasBoegr;
    }

    public BigDecimal getCostoAnteriorBoegr() {
        return costoAnteriorBoegr;
    }

    public void setCostoAnteriorBoegr(BigDecimal costoAnteriorBoegr) {
        this.costoAnteriorBoegr = costoAnteriorBoegr;
    }

    public Date getFechaIngresoArticuloBoegr() {
        return fechaIngresoArticuloBoegr;
    }

    public void setFechaIngresoArticuloBoegr(Date fechaIngresoArticuloBoegr) {
        this.fechaIngresoArticuloBoegr = fechaIngresoArticuloBoegr;
    }

    public Boolean getActivoBoegr() {
        return activoBoegr;
    }

    public void setActivoBoegr(Boolean activoBoegr) {
        this.activoBoegr = activoBoegr;
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

    /*public BodtMaterial getIdeBomat() {
        return ideBomat;
    }

    public void setIdeBomat(BodtMaterial ideBomat) {
        this.ideBomat = ideBomat;
    }*/

    public BodtInventario getIdeBoinv() {
        return ideBoinv;
    }

    public void setIdeBoinv(BodtInventario ideBoinv) {
        this.ideBoinv = ideBoinv;
    }

    public BodtConceptoEgreso getIdeBocoe() {
        return ideBocoe;
    }

    public void setIdeBocoe(BodtConceptoEgreso ideBocoe) {
        this.ideBocoe = ideBocoe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBoegr != null ? ideBoegr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtEgreso)) {
            return false;
        }
        BodtEgreso other = (BodtEgreso) object;
        if ((this.ideBoegr == null && other.ideBoegr != null) || (this.ideBoegr != null && !this.ideBoegr.equals(other.ideBoegr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtEgreso[ ideBoegr=" + ideBoegr + " ]";
    }
    
}
