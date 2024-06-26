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
@Table(name = "gen_area", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenArea.findAll", query = "SELECT g FROM GenArea g"),
    @NamedQuery(name = "GenArea.findByIdeGeare", query = "SELECT g FROM GenArea g WHERE g.ideGeare = :ideGeare"),
    @NamedQuery(name = "GenArea.findByDetalleGeare", query = "SELECT g FROM GenArea g WHERE g.detalleGeare = :detalleGeare"),
    @NamedQuery(name = "GenArea.findByCodigoCoreGeare", query = "SELECT g FROM GenArea g WHERE g.codigoCoreGeare = :codigoCoreGeare"),
    @NamedQuery(name = "GenArea.findByActivoGeare", query = "SELECT g FROM GenArea g WHERE g.activoGeare = :activoGeare"),
    @NamedQuery(name = "GenArea.findByUsuarioIngre", query = "SELECT g FROM GenArea g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenArea.findByFechaIngre", query = "SELECT g FROM GenArea g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenArea.findByUsuarioActua", query = "SELECT g FROM GenArea g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenArea.findByFechaActua", query = "SELECT g FROM GenArea g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenArea.findByHoraIngre", query = "SELECT g FROM GenArea g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenArea.findByHoraActua", query = "SELECT g FROM GenArea g WHERE g.horaActua = :horaActua")})
public class GenArea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geare", nullable = false)
    private Integer ideGeare;
    @Size(max = 100)
    @Column(name = "detalle_geare", length = 100)
    private String detalleGeare;
    @Size(max = 50)
    @Column(name = "codigo_core_geare", length = 50)
    private String codigoCoreGeare;
    @Column(name = "activo_geare")
    private Boolean activoGeare;
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
    @OneToMany(mappedBy = "ideGeare")
    private List<BodtInventario> bodtInventarioList;
    @OneToMany(mappedBy = "ideGeare")
    private List<BodtConceptoEgreso> bodtConceptoEgresoList;
    @OneToMany(mappedBy = "ideGeare")
    private List<ContMovimiento> contMovimientoList;
    @OneToMany(mappedBy = "ideGeare")
    private List<AfiActivo> afiActivoList;

    public GenArea() {
    }

    public GenArea(Integer ideGeare) {
        this.ideGeare = ideGeare;
    }

    public Integer getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(Integer ideGeare) {
        this.ideGeare = ideGeare;
    }

    public String getDetalleGeare() {
        return detalleGeare;
    }

    public void setDetalleGeare(String detalleGeare) {
        this.detalleGeare = detalleGeare;
    }

    public String getCodigoCoreGeare() {
        return codigoCoreGeare;
    }

    public void setCodigoCoreGeare(String codigoCoreGeare) {
        this.codigoCoreGeare = codigoCoreGeare;
    }

    public Boolean getActivoGeare() {
        return activoGeare;
    }

    public void setActivoGeare(Boolean activoGeare) {
        this.activoGeare = activoGeare;
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

    public List<BodtInventario> getBodtInventarioList() {
        return bodtInventarioList;
    }

    public void setBodtInventarioList(List<BodtInventario> bodtInventarioList) {
        this.bodtInventarioList = bodtInventarioList;
    }

    public List<BodtConceptoEgreso> getBodtConceptoEgresoList() {
        return bodtConceptoEgresoList;
    }

    public void setBodtConceptoEgresoList(List<BodtConceptoEgreso> bodtConceptoEgresoList) {
        this.bodtConceptoEgresoList = bodtConceptoEgresoList;
    }

    public List<ContMovimiento> getContMovimientoList() {
        return contMovimientoList;
    }

    public void setContMovimientoList(List<ContMovimiento> contMovimientoList) {
        this.contMovimientoList = contMovimientoList;
    }

    public List<AfiActivo> getAfiActivoList() {
        return afiActivoList;
    }

    public void setAfiActivoList(List<AfiActivo> afiActivoList) {
        this.afiActivoList = afiActivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeare != null ? ideGeare.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenArea)) {
            return false;
        }
        GenArea other = (GenArea) object;
        if ((this.ideGeare == null && other.ideGeare != null) || (this.ideGeare != null && !this.ideGeare.equals(other.ideGeare))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenArea[ ideGeare=" + ideGeare + " ]";
    }
    
}
