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
@Table(name = "gen_region", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenRegion.findAll", query = "SELECT g FROM GenRegion g"),
    @NamedQuery(name = "GenRegion.findByIdeGereg", query = "SELECT g FROM GenRegion g WHERE g.ideGereg = :ideGereg"),
    @NamedQuery(name = "GenRegion.findByDetalleGereg", query = "SELECT g FROM GenRegion g WHERE g.detalleGereg = :detalleGereg"),
    @NamedQuery(name = "GenRegion.findByActivoGereg", query = "SELECT g FROM GenRegion g WHERE g.activoGereg = :activoGereg"),
    @NamedQuery(name = "GenRegion.findByUsuarioIngre", query = "SELECT g FROM GenRegion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenRegion.findByFechaIngre", query = "SELECT g FROM GenRegion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenRegion.findByUsuarioActua", query = "SELECT g FROM GenRegion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenRegion.findByFechaActua", query = "SELECT g FROM GenRegion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenRegion.findByHoraIngre", query = "SELECT g FROM GenRegion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenRegion.findByHoraActua", query = "SELECT g FROM GenRegion g WHERE g.horaActua = :horaActua")})
public class GenRegion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_gereg")
    private Integer ideGereg;
    @Size(max = 50)
    @Column(name = "detalle_gereg", length = 50)
    private String detalleGereg;
    @Column(name = "activo_gereg")
    private Boolean activoGereg;
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
    @OneToMany(mappedBy = "ideGereg")
    private List<GenDivisionPolitica> genDivisionPoliticaList;
    @OneToMany(mappedBy = "ideGereg")
    private List<NrhDetalleRubro> nrhDetalleRubroList;

    public GenRegion() {
    }

    public GenRegion(Integer ideGereg) {
        this.ideGereg = ideGereg;
    }

    public Integer getIdeGereg() {
        return ideGereg;
    }

    public void setIdeGereg(Integer ideGereg) {
        this.ideGereg = ideGereg;
    }

    public String getDetalleGereg() {
        return detalleGereg;
    }

    public void setDetalleGereg(String detalleGereg) {
        this.detalleGereg = detalleGereg;
    }

    public Boolean getActivoGereg() {
        return activoGereg;
    }

    public void setActivoGereg(Boolean activoGereg) {
        this.activoGereg = activoGereg;
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

    public List<GenDivisionPolitica> getGenDivisionPoliticaList() {
        return genDivisionPoliticaList;
    }

    public void setGenDivisionPoliticaList(List<GenDivisionPolitica> genDivisionPoliticaList) {
        this.genDivisionPoliticaList = genDivisionPoliticaList;
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
        hash += (ideGereg != null ? ideGereg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenRegion)) {
            return false;
        }
        GenRegion other = (GenRegion) object;
        if ((this.ideGereg == null && other.ideGereg != null) || (this.ideGereg != null && !this.ideGereg.equals(other.ideGereg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenRegion[ ideGereg=" + ideGereg + " ]";
    }
    
}
