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
@Table(name = "spr_capacitacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprCapacitacion.findAll", query = "SELECT s FROM SprCapacitacion s"),
    @NamedQuery(name = "SprCapacitacion.findByIdeSpcap", query = "SELECT s FROM SprCapacitacion s WHERE s.ideSpcap = :ideSpcap"),
    @NamedQuery(name = "SprCapacitacion.findByDetalleSpcap", query = "SELECT s FROM SprCapacitacion s WHERE s.detalleSpcap = :detalleSpcap"),
    @NamedQuery(name = "SprCapacitacion.findByInstructorSpcap", query = "SELECT s FROM SprCapacitacion s WHERE s.instructorSpcap = :instructorSpcap"),
    @NamedQuery(name = "SprCapacitacion.findByFechaSpcap", query = "SELECT s FROM SprCapacitacion s WHERE s.fechaSpcap = :fechaSpcap"),
    @NamedQuery(name = "SprCapacitacion.findByDuracionSpcap", query = "SELECT s FROM SprCapacitacion s WHERE s.duracionSpcap = :duracionSpcap"),
    @NamedQuery(name = "SprCapacitacion.findByActivoSpcap", query = "SELECT s FROM SprCapacitacion s WHERE s.activoSpcap = :activoSpcap"),
    @NamedQuery(name = "SprCapacitacion.findByUsuarioIngre", query = "SELECT s FROM SprCapacitacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprCapacitacion.findByFechaIngre", query = "SELECT s FROM SprCapacitacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprCapacitacion.findByHoraIngre", query = "SELECT s FROM SprCapacitacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprCapacitacion.findByUsuarioActua", query = "SELECT s FROM SprCapacitacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprCapacitacion.findByFechaActua", query = "SELECT s FROM SprCapacitacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprCapacitacion.findByHoraActua", query = "SELECT s FROM SprCapacitacion s WHERE s.horaActua = :horaActua")})
public class SprCapacitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spcap", nullable = false)
    private Integer ideSpcap;
    @Size(max = 4000)
    @Column(name = "detalle_spcap", length = 4000)
    private String detalleSpcap;
    @Size(max = 100)
    @Column(name = "instructor_spcap", length = 100)
    private String instructorSpcap;
    @Column(name = "fecha_spcap")
    @Temporal(TemporalType.DATE)
    private Date fechaSpcap;
    @Column(name = "duracion_spcap")
    private Integer duracionSpcap;
    @Column(name = "activo_spcap")
    private Boolean activoSpcap;
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
    @JoinColumn(name = "ide_spbap", referencedColumnName = "ide_spbap")
    @ManyToOne
    private SprBasePostulante ideSpbap;
    @JoinColumn(name = "ide_getpr", referencedColumnName = "ide_getpr")
    @ManyToOne
    private GenTipoPeriodo ideGetpr;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;

    public SprCapacitacion() {
    }

    public SprCapacitacion(Integer ideSpcap) {
        this.ideSpcap = ideSpcap;
    }

    public Integer getIdeSpcap() {
        return ideSpcap;
    }

    public void setIdeSpcap(Integer ideSpcap) {
        this.ideSpcap = ideSpcap;
    }

    public String getDetalleSpcap() {
        return detalleSpcap;
    }

    public void setDetalleSpcap(String detalleSpcap) {
        this.detalleSpcap = detalleSpcap;
    }

    public String getInstructorSpcap() {
        return instructorSpcap;
    }

    public void setInstructorSpcap(String instructorSpcap) {
        this.instructorSpcap = instructorSpcap;
    }

    public Date getFechaSpcap() {
        return fechaSpcap;
    }

    public void setFechaSpcap(Date fechaSpcap) {
        this.fechaSpcap = fechaSpcap;
    }

    public Integer getDuracionSpcap() {
        return duracionSpcap;
    }

    public void setDuracionSpcap(Integer duracionSpcap) {
        this.duracionSpcap = duracionSpcap;
    }

    public Boolean getActivoSpcap() {
        return activoSpcap;
    }

    public void setActivoSpcap(Boolean activoSpcap) {
        this.activoSpcap = activoSpcap;
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

    public SprBasePostulante getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(SprBasePostulante ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    public GenTipoPeriodo getIdeGetpr() {
        return ideGetpr;
    }

    public void setIdeGetpr(GenTipoPeriodo ideGetpr) {
        this.ideGetpr = ideGetpr;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpcap != null ? ideSpcap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprCapacitacion)) {
            return false;
        }
        SprCapacitacion other = (SprCapacitacion) object;
        if ((this.ideSpcap == null && other.ideSpcap != null) || (this.ideSpcap != null && !this.ideSpcap.equals(other.ideSpcap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprCapacitacion[ ideSpcap=" + ideSpcap + " ]";
    }
    
}
