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
@Table(name = "spr_subarea", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprSubarea.findAll", query = "SELECT s FROM SprSubarea s"),
    @NamedQuery(name = "SprSubarea.findByIdeSpsub", query = "SELECT s FROM SprSubarea s WHERE s.ideSpsub = :ideSpsub"),
    @NamedQuery(name = "SprSubarea.findByDetalleSpsub", query = "SELECT s FROM SprSubarea s WHERE s.detalleSpsub = :detalleSpsub"),
    @NamedQuery(name = "SprSubarea.findByActivoSpsub", query = "SELECT s FROM SprSubarea s WHERE s.activoSpsub = :activoSpsub"),
    @NamedQuery(name = "SprSubarea.findByUsuarioIngre", query = "SELECT s FROM SprSubarea s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprSubarea.findByFechaIngre", query = "SELECT s FROM SprSubarea s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprSubarea.findByHoraIngre", query = "SELECT s FROM SprSubarea s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprSubarea.findByUsuarioActua", query = "SELECT s FROM SprSubarea s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprSubarea.findByFechaActua", query = "SELECT s FROM SprSubarea s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprSubarea.findByHoraActua", query = "SELECT s FROM SprSubarea s WHERE s.horaActua = :horaActua")})
public class SprSubarea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spsub", nullable = false)
    private Integer ideSpsub;
    @Size(max = 100)
    @Column(name = "detalle_spsub", length = 100)
    private String detalleSpsub;
    @Column(name = "activo_spsub")
    private Boolean activoSpsub;
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
    @OneToMany(mappedBy = "ideSpsub")
    private List<SprExperienciaLaboral> sprExperienciaLaboralList;
    @OneToMany(mappedBy = "ideSpsub")
    private List<SprAreaSubarea> sprAreaSubareaList;

    public SprSubarea() {
    }

    public SprSubarea(Integer ideSpsub) {
        this.ideSpsub = ideSpsub;
    }

    public Integer getIdeSpsub() {
        return ideSpsub;
    }

    public void setIdeSpsub(Integer ideSpsub) {
        this.ideSpsub = ideSpsub;
    }

    public String getDetalleSpsub() {
        return detalleSpsub;
    }

    public void setDetalleSpsub(String detalleSpsub) {
        this.detalleSpsub = detalleSpsub;
    }

    public Boolean getActivoSpsub() {
        return activoSpsub;
    }

    public void setActivoSpsub(Boolean activoSpsub) {
        this.activoSpsub = activoSpsub;
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

    public List<SprAreaSubarea> getSprAreaSubareaList() {
        return sprAreaSubareaList;
    }

    public void setSprAreaSubareaList(List<SprAreaSubarea> sprAreaSubareaList) {
        this.sprAreaSubareaList = sprAreaSubareaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpsub != null ? ideSpsub.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprSubarea)) {
            return false;
        }
        SprSubarea other = (SprSubarea) object;
        if ((this.ideSpsub == null && other.ideSpsub != null) || (this.ideSpsub != null && !this.ideSpsub.equals(other.ideSpsub))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprSubarea[ ideSpsub=" + ideSpsub + " ]";
    }
    
}
