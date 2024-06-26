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
@Table(name = "spr_area_subarea", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprAreaSubarea.findAll", query = "SELECT s FROM SprAreaSubarea s"),
    @NamedQuery(name = "SprAreaSubarea.findByIdeSpars", query = "SELECT s FROM SprAreaSubarea s WHERE s.ideSpars = :ideSpars"),
    @NamedQuery(name = "SprAreaSubarea.findByActivoSpars", query = "SELECT s FROM SprAreaSubarea s WHERE s.activoSpars = :activoSpars"),
    @NamedQuery(name = "SprAreaSubarea.findByUsuarioIngre", query = "SELECT s FROM SprAreaSubarea s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprAreaSubarea.findByFechaIngre", query = "SELECT s FROM SprAreaSubarea s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprAreaSubarea.findByHoraIngre", query = "SELECT s FROM SprAreaSubarea s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprAreaSubarea.findByUsuarioActua", query = "SELECT s FROM SprAreaSubarea s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprAreaSubarea.findByFechaActua", query = "SELECT s FROM SprAreaSubarea s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprAreaSubarea.findByHoraActua", query = "SELECT s FROM SprAreaSubarea s WHERE s.horaActua = :horaActua")})
public class SprAreaSubarea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spars", nullable = false)
    private Integer ideSpars;
    @Column(name = "activo_spars")
    private Boolean activoSpars;
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
    @JoinColumn(name = "ide_spsub", referencedColumnName = "ide_spsub")
    @ManyToOne
    private SprSubarea ideSpsub;
    @JoinColumn(name = "ide_spare", referencedColumnName = "ide_spare")
    @ManyToOne
    private SprArea ideSpare;

    public SprAreaSubarea() {
    }

    public SprAreaSubarea(Integer ideSpars) {
        this.ideSpars = ideSpars;
    }

    public Integer getIdeSpars() {
        return ideSpars;
    }

    public void setIdeSpars(Integer ideSpars) {
        this.ideSpars = ideSpars;
    }

    public Boolean getActivoSpars() {
        return activoSpars;
    }

    public void setActivoSpars(Boolean activoSpars) {
        this.activoSpars = activoSpars;
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

    public SprSubarea getIdeSpsub() {
        return ideSpsub;
    }

    public void setIdeSpsub(SprSubarea ideSpsub) {
        this.ideSpsub = ideSpsub;
    }

    public SprArea getIdeSpare() {
        return ideSpare;
    }

    public void setIdeSpare(SprArea ideSpare) {
        this.ideSpare = ideSpare;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpars != null ? ideSpars.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprAreaSubarea)) {
            return false;
        }
        SprAreaSubarea other = (SprAreaSubarea) object;
        if ((this.ideSpars == null && other.ideSpars != null) || (this.ideSpars != null && !this.ideSpars.equals(other.ideSpars))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprAreaSubarea[ ideSpars=" + ideSpars + " ]";
    }
    
}
