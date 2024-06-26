/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "bodt_aplica_inventario_material", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findAll", query = "SELECT b FROM BodtAplicaInventarioMaterial b"),
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findByIdeBoaim", query = "SELECT b FROM BodtAplicaInventarioMaterial b WHERE b.ideBoaim = :ideBoaim"),
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findByAplicaAreaBoaim", query = "SELECT b FROM BodtAplicaInventarioMaterial b WHERE b.aplicaAreaBoaim = :aplicaAreaBoaim"),
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findByActivoBoaim", query = "SELECT b FROM BodtAplicaInventarioMaterial b WHERE b.activoBoaim = :activoBoaim"),
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findByUsuarioIngre", query = "SELECT b FROM BodtAplicaInventarioMaterial b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findByFechaIngre", query = "SELECT b FROM BodtAplicaInventarioMaterial b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findByHoraIngre", query = "SELECT b FROM BodtAplicaInventarioMaterial b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findByUsuarioActua", query = "SELECT b FROM BodtAplicaInventarioMaterial b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findByFechaActua", query = "SELECT b FROM BodtAplicaInventarioMaterial b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtAplicaInventarioMaterial.findByHoraActua", query = "SELECT b FROM BodtAplicaInventarioMaterial b WHERE b.horaActua = :horaActua")})
public class BodtAplicaInventarioMaterial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_boaim", nullable = false)
    private Long ideBoaim;
    @Column(name = "aplica_area_boaim")
    private Boolean aplicaAreaBoaim;
    @Column(name = "activo_boaim")
    private Boolean activoBoaim;
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
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;

    public BodtAplicaInventarioMaterial() {
    }

    public BodtAplicaInventarioMaterial(Long ideBoaim) {
        this.ideBoaim = ideBoaim;
    }

    public Long getIdeBoaim() {
        return ideBoaim;
    }

    public void setIdeBoaim(Long ideBoaim) {
        this.ideBoaim = ideBoaim;
    }

    public Boolean getAplicaAreaBoaim() {
        return aplicaAreaBoaim;
    }

    public void setAplicaAreaBoaim(Boolean aplicaAreaBoaim) {
        this.aplicaAreaBoaim = aplicaAreaBoaim;
    }

    public Boolean getActivoBoaim() {
        return activoBoaim;
    }

    public void setActivoBoaim(Boolean activoBoaim) {
        this.activoBoaim = activoBoaim;
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

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBoaim != null ? ideBoaim.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtAplicaInventarioMaterial)) {
            return false;
        }
        BodtAplicaInventarioMaterial other = (BodtAplicaInventarioMaterial) object;
        if ((this.ideBoaim == null && other.ideBoaim != null) || (this.ideBoaim != null && !this.ideBoaim.equals(other.ideBoaim))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtAplicaInventarioMaterial[ ideBoaim=" + ideBoaim + " ]";
    }
    
}
