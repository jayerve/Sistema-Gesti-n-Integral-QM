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
@Table(name = "spr_industria", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprIndustria.findAll", query = "SELECT s FROM SprIndustria s"),
    @NamedQuery(name = "SprIndustria.findByIdeSpind", query = "SELECT s FROM SprIndustria s WHERE s.ideSpind = :ideSpind"),
    @NamedQuery(name = "SprIndustria.findByDetalleSpind", query = "SELECT s FROM SprIndustria s WHERE s.detalleSpind = :detalleSpind"),
    @NamedQuery(name = "SprIndustria.findByActivoSpest", query = "SELECT s FROM SprIndustria s WHERE s.activoSpest = :activoSpest"),
    @NamedQuery(name = "SprIndustria.findByUsuarioIngre", query = "SELECT s FROM SprIndustria s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprIndustria.findByFechaIngre", query = "SELECT s FROM SprIndustria s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprIndustria.findByHoraIngre", query = "SELECT s FROM SprIndustria s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprIndustria.findByUsuarioActua", query = "SELECT s FROM SprIndustria s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprIndustria.findByFechaActua", query = "SELECT s FROM SprIndustria s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprIndustria.findByHoraActua", query = "SELECT s FROM SprIndustria s WHERE s.horaActua = :horaActua")})
public class SprIndustria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spind", nullable = false)
    private Integer ideSpind;
    @Size(max = 100)
    @Column(name = "detalle_spind", length = 100)
    private String detalleSpind;
    @Column(name = "activo_spest")
    private Boolean activoSpest;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @OneToMany(mappedBy = "ideSpind")
    private List<SprExperienciaLaboral> sprExperienciaLaboralList;

    public SprIndustria() {
    }

    public SprIndustria(Integer ideSpind) {
        this.ideSpind = ideSpind;
    }

    public Integer getIdeSpind() {
        return ideSpind;
    }

    public void setIdeSpind(Integer ideSpind) {
        this.ideSpind = ideSpind;
    }

    public String getDetalleSpind() {
        return detalleSpind;
    }

    public void setDetalleSpind(String detalleSpind) {
        this.detalleSpind = detalleSpind;
    }

    public Boolean getActivoSpest() {
        return activoSpest;
    }

    public void setActivoSpest(Boolean activoSpest) {
        this.activoSpest = activoSpest;
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

    public List<SprExperienciaLaboral> getSprExperienciaLaboralList() {
        return sprExperienciaLaboralList;
    }

    public void setSprExperienciaLaboralList(List<SprExperienciaLaboral> sprExperienciaLaboralList) {
        this.sprExperienciaLaboralList = sprExperienciaLaboralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpind != null ? ideSpind.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprIndustria)) {
            return false;
        }
        SprIndustria other = (SprIndustria) object;
        if ((this.ideSpind == null && other.ideSpind != null) || (this.ideSpind != null && !this.ideSpind.equals(other.ideSpind))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprIndustria[ ideSpind=" + ideSpind + " ]";
    }
    
}
