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
@Table(name = "bodt_inventario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtInventario.findAll", query = "SELECT b FROM BodtInventario b"),
    @NamedQuery(name = "BodtInventario.findByIdeBoinv", query = "SELECT b FROM BodtInventario b WHERE b.ideBoinv = :ideBoinv"),
    @NamedQuery(name = "BodtInventario.findByIngresoMaterialBoinv", query = "SELECT b FROM BodtInventario b WHERE b.ingresoMaterialBoinv = :ingresoMaterialBoinv"),
    @NamedQuery(name = "BodtInventario.findByEgresoMaterialBoinv", query = "SELECT b FROM BodtInventario b WHERE b.egresoMaterialBoinv = :egresoMaterialBoinv"),
    @NamedQuery(name = "BodtInventario.findByExistenciaInicialBoinv", query = "SELECT b FROM BodtInventario b WHERE b.existenciaInicialBoinv = :existenciaInicialBoinv"),
    @NamedQuery(name = "BodtInventario.findByCostoAnteriorBoinv", query = "SELECT b FROM BodtInventario b WHERE b.costoAnteriorBoinv = :costoAnteriorBoinv"),
    @NamedQuery(name = "BodtInventario.findByCostoActualBoinv", query = "SELECT b FROM BodtInventario b WHERE b.costoActualBoinv = :costoActualBoinv"),
    @NamedQuery(name = "BodtInventario.findByFechaIngrArticuloBoinv", query = "SELECT b FROM BodtInventario b WHERE b.fechaIngrArticuloBoinv = :fechaIngrArticuloBoinv"),
    @NamedQuery(name = "BodtInventario.findByCostoInicialBoinv", query = "SELECT b FROM BodtInventario b WHERE b.costoInicialBoinv = :costoInicialBoinv"),
    @NamedQuery(name = "BodtInventario.findByActivoBoinv", query = "SELECT b FROM BodtInventario b WHERE b.activoBoinv = :activoBoinv"),
    @NamedQuery(name = "BodtInventario.findByUsuarioIngre", query = "SELECT b FROM BodtInventario b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtInventario.findByFechaIngre", query = "SELECT b FROM BodtInventario b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtInventario.findByHoraIngre", query = "SELECT b FROM BodtInventario b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtInventario.findByUsuarioActua", query = "SELECT b FROM BodtInventario b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtInventario.findByFechaActua", query = "SELECT b FROM BodtInventario b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtInventario.findByHoraActua", query = "SELECT b FROM BodtInventario b WHERE b.horaActua = :horaActua")})
public class BodtInventario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_boinv", nullable = false)
    private Long ideBoinv;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ingreso_material_boinv", precision = 10, scale = 2)
    private BigDecimal ingresoMaterialBoinv;
    @Column(name = "egreso_material_boinv", precision = 10, scale = 2)
    private BigDecimal egresoMaterialBoinv;
    @Column(name = "existencia_inicial_boinv", precision = 10, scale = 4)
    private BigDecimal existenciaInicialBoinv;
    @Column(name = "costo_anterior_boinv", precision = 10, scale = 4)
    private BigDecimal costoAnteriorBoinv;
    @Column(name = "costo_actual_boinv", precision = 10, scale = 4)
    private BigDecimal costoActualBoinv;
    @Column(name = "fecha_ingr_articulo_boinv")
    @Temporal(TemporalType.DATE)
    private Date fechaIngrArticuloBoinv;
    @Column(name = "costo_inicial_boinv", precision = 10, scale = 4)
    private BigDecimal costoInicialBoinv;
    @Column(name = "activo_boinv")
    private Boolean activoBoinv;
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
    @JoinColumn(name = "ide_geare", referencedColumnName = "ide_geare")
    @ManyToOne
    private GenArea ideGeare;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @JoinColumn(name = "gen_ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio genIdeGeani;
    @OneToMany(mappedBy = "ideBoinv")
    private List<BodtEgreso> bodtEgresoList;

    public BodtInventario() {
    }

    public BodtInventario(Long ideBoinv) {
        this.ideBoinv = ideBoinv;
    }

    public Long getIdeBoinv() {
        return ideBoinv;
    }

    public void setIdeBoinv(Long ideBoinv) {
        this.ideBoinv = ideBoinv;
    }

    public BigDecimal getIngresoMaterialBoinv() {
        return ingresoMaterialBoinv;
    }

    public void setIngresoMaterialBoinv(BigDecimal ingresoMaterialBoinv) {
        this.ingresoMaterialBoinv = ingresoMaterialBoinv;
    }

    public BigDecimal getEgresoMaterialBoinv() {
        return egresoMaterialBoinv;
    }

    public void setEgresoMaterialBoinv(BigDecimal egresoMaterialBoinv) {
        this.egresoMaterialBoinv = egresoMaterialBoinv;
    }

    public BigDecimal getExistenciaInicialBoinv() {
        return existenciaInicialBoinv;
    }

    public void setExistenciaInicialBoinv(BigDecimal existenciaInicialBoinv) {
        this.existenciaInicialBoinv = existenciaInicialBoinv;
    }

    public BigDecimal getCostoAnteriorBoinv() {
        return costoAnteriorBoinv;
    }

    public void setCostoAnteriorBoinv(BigDecimal costoAnteriorBoinv) {
        this.costoAnteriorBoinv = costoAnteriorBoinv;
    }

    public BigDecimal getCostoActualBoinv() {
        return costoActualBoinv;
    }

    public void setCostoActualBoinv(BigDecimal costoActualBoinv) {
        this.costoActualBoinv = costoActualBoinv;
    }

    public Date getFechaIngrArticuloBoinv() {
        return fechaIngrArticuloBoinv;
    }

    public void setFechaIngrArticuloBoinv(Date fechaIngrArticuloBoinv) {
        this.fechaIngrArticuloBoinv = fechaIngrArticuloBoinv;
    }

    public BigDecimal getCostoInicialBoinv() {
        return costoInicialBoinv;
    }

    public void setCostoInicialBoinv(BigDecimal costoInicialBoinv) {
        this.costoInicialBoinv = costoInicialBoinv;
    }

    public Boolean getActivoBoinv() {
        return activoBoinv;
    }

    public void setActivoBoinv(Boolean activoBoinv) {
        this.activoBoinv = activoBoinv;
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

    public GenArea getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(GenArea ideGeare) {
        this.ideGeare = ideGeare;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    public GenAnio getGenIdeGeani() {
        return genIdeGeani;
    }

    public void setGenIdeGeani(GenAnio genIdeGeani) {
        this.genIdeGeani = genIdeGeani;
    }

    public List<BodtEgreso> getBodtEgresoList() {
        return bodtEgresoList;
    }

    public void setBodtEgresoList(List<BodtEgreso> bodtEgresoList) {
        this.bodtEgresoList = bodtEgresoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBoinv != null ? ideBoinv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtInventario)) {
            return false;
        }
        BodtInventario other = (BodtInventario) object;
        if ((this.ideBoinv == null && other.ideBoinv != null) || (this.ideBoinv != null && !this.ideBoinv.equals(other.ideBoinv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtInventario[ ideBoinv=" + ideBoinv + " ]";
    }
    
}
