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
@Table(name = "spr_calificacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprCalificacion.findAll", query = "SELECT s FROM SprCalificacion s"),
    @NamedQuery(name = "SprCalificacion.findByIdeSpcal", query = "SELECT s FROM SprCalificacion s WHERE s.ideSpcal = :ideSpcal"),
    @NamedQuery(name = "SprCalificacion.findByFechaCalificacionSpcal", query = "SELECT s FROM SprCalificacion s WHERE s.fechaCalificacionSpcal = :fechaCalificacionSpcal"),
    @NamedQuery(name = "SprCalificacion.findByNotaSpcal", query = "SELECT s FROM SprCalificacion s WHERE s.notaSpcal = :notaSpcal"),
    @NamedQuery(name = "SprCalificacion.findByObservacionSpcal", query = "SELECT s FROM SprCalificacion s WHERE s.observacionSpcal = :observacionSpcal"),
    @NamedQuery(name = "SprCalificacion.findByActivoSpcal", query = "SELECT s FROM SprCalificacion s WHERE s.activoSpcal = :activoSpcal"),
    @NamedQuery(name = "SprCalificacion.findByUsuarioIngre", query = "SELECT s FROM SprCalificacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprCalificacion.findByFechaIngre", query = "SELECT s FROM SprCalificacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprCalificacion.findByHoraIngre", query = "SELECT s FROM SprCalificacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprCalificacion.findByUsuarioActua", query = "SELECT s FROM SprCalificacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprCalificacion.findByFechaActua", query = "SELECT s FROM SprCalificacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprCalificacion.findByHoraActua", query = "SELECT s FROM SprCalificacion s WHERE s.horaActua = :horaActua")})
public class SprCalificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spcal", nullable = false)
    private Integer ideSpcal;
    @Column(name = "fecha_calificacion_spcal")
    @Temporal(TemporalType.DATE)
    private Date fechaCalificacionSpcal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nota_spcal", precision = 12, scale = 2)
    private BigDecimal notaSpcal;
    @Size(max = 4000)
    @Column(name = "observacion_spcal", length = 4000)
    private String observacionSpcal;
    @Column(name = "activo_spcal")
    private Boolean activoSpcal;
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
    @JoinColumn(name = "ide_sprec", referencedColumnName = "ide_sprec")
    @ManyToOne
    private SprResponsableCalificacion ideSprec;
    @JoinColumn(name = "ide_sppos", referencedColumnName = "ide_sppos")
    @ManyToOne
    private SprPostulante ideSppos;
    @JoinColumn(name = "ide_spfac", referencedColumnName = "ide_spfac")
    @ManyToOne
    private SprFactor ideSpfac;

    public SprCalificacion() {
    }

    public SprCalificacion(Integer ideSpcal) {
        this.ideSpcal = ideSpcal;
    }

    public Integer getIdeSpcal() {
        return ideSpcal;
    }

    public void setIdeSpcal(Integer ideSpcal) {
        this.ideSpcal = ideSpcal;
    }

    public Date getFechaCalificacionSpcal() {
        return fechaCalificacionSpcal;
    }

    public void setFechaCalificacionSpcal(Date fechaCalificacionSpcal) {
        this.fechaCalificacionSpcal = fechaCalificacionSpcal;
    }

    public BigDecimal getNotaSpcal() {
        return notaSpcal;
    }

    public void setNotaSpcal(BigDecimal notaSpcal) {
        this.notaSpcal = notaSpcal;
    }

    public String getObservacionSpcal() {
        return observacionSpcal;
    }

    public void setObservacionSpcal(String observacionSpcal) {
        this.observacionSpcal = observacionSpcal;
    }

    public Boolean getActivoSpcal() {
        return activoSpcal;
    }

    public void setActivoSpcal(Boolean activoSpcal) {
        this.activoSpcal = activoSpcal;
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

    public SprResponsableCalificacion getIdeSprec() {
        return ideSprec;
    }

    public void setIdeSprec(SprResponsableCalificacion ideSprec) {
        this.ideSprec = ideSprec;
    }

    public SprPostulante getIdeSppos() {
        return ideSppos;
    }

    public void setIdeSppos(SprPostulante ideSppos) {
        this.ideSppos = ideSppos;
    }

    public SprFactor getIdeSpfac() {
        return ideSpfac;
    }

    public void setIdeSpfac(SprFactor ideSpfac) {
        this.ideSpfac = ideSpfac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpcal != null ? ideSpcal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprCalificacion)) {
            return false;
        }
        SprCalificacion other = (SprCalificacion) object;
        if ((this.ideSpcal == null && other.ideSpcal != null) || (this.ideSpcal != null && !this.ideSpcal.equals(other.ideSpcal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprCalificacion[ ideSpcal=" + ideSpcal + " ]";
    }
    
}
