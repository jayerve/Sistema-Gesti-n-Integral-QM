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
@Table(name = "spr_area", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprArea.findAll", query = "SELECT s FROM SprArea s"),
    @NamedQuery(name = "SprArea.findByIdeSpare", query = "SELECT s FROM SprArea s WHERE s.ideSpare = :ideSpare"),
    @NamedQuery(name = "SprArea.findByDetalleSpare", query = "SELECT s FROM SprArea s WHERE s.detalleSpare = :detalleSpare"),
    @NamedQuery(name = "SprArea.findByActivoSpest", query = "SELECT s FROM SprArea s WHERE s.activoSpest = :activoSpest"),
    @NamedQuery(name = "SprArea.findByUsuarioIngre", query = "SELECT s FROM SprArea s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprArea.findByFechaIngre", query = "SELECT s FROM SprArea s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprArea.findByHoraIngre", query = "SELECT s FROM SprArea s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprArea.findByUsuarioActua", query = "SELECT s FROM SprArea s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprArea.findByFechaActua", query = "SELECT s FROM SprArea s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprArea.findByHoraActua", query = "SELECT s FROM SprArea s WHERE s.horaActua = :horaActua")})
public class SprArea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spare", nullable = false)
    private Integer ideSpare;
    @Size(max = 100)
    @Column(name = "detalle_spare", length = 100)
    private String detalleSpare;
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
    @OneToMany(mappedBy = "ideSpare")
    private List<SprExperienciaLaboral> sprExperienciaLaboralList;
    @OneToMany(mappedBy = "ideSpare")
    private List<SprAreaSubarea> sprAreaSubareaList;

    public SprArea() {
    }

    public SprArea(Integer ideSpare) {
        this.ideSpare = ideSpare;
    }

    public Integer getIdeSpare() {
        return ideSpare;
    }

    public void setIdeSpare(Integer ideSpare) {
        this.ideSpare = ideSpare;
    }

    public String getDetalleSpare() {
        return detalleSpare;
    }

    public void setDetalleSpare(String detalleSpare) {
        this.detalleSpare = detalleSpare;
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

    public List<SprAreaSubarea> getSprAreaSubareaList() {
        return sprAreaSubareaList;
    }

    public void setSprAreaSubareaList(List<SprAreaSubarea> sprAreaSubareaList) {
        this.sprAreaSubareaList = sprAreaSubareaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpare != null ? ideSpare.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprArea)) {
            return false;
        }
        SprArea other = (SprArea) object;
        if ((this.ideSpare == null && other.ideSpare != null) || (this.ideSpare != null && !this.ideSpare.equals(other.ideSpare))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprArea[ ideSpare=" + ideSpare + " ]";
    }
    
}
