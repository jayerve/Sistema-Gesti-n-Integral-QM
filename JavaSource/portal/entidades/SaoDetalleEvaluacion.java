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
@Table(name = "sao_detalle_evaluacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoDetalleEvaluacion.findAll", query = "SELECT s FROM SaoDetalleEvaluacion s"),
    @NamedQuery(name = "SaoDetalleEvaluacion.findByIdeSadee", query = "SELECT s FROM SaoDetalleEvaluacion s WHERE s.ideSadee = :ideSadee"),
    @NamedQuery(name = "SaoDetalleEvaluacion.findByObservacionSadee", query = "SELECT s FROM SaoDetalleEvaluacion s WHERE s.observacionSadee = :observacionSadee"),
    @NamedQuery(name = "SaoDetalleEvaluacion.findByActivoSadee", query = "SELECT s FROM SaoDetalleEvaluacion s WHERE s.activoSadee = :activoSadee"),
    @NamedQuery(name = "SaoDetalleEvaluacion.findByUsuarioIngre", query = "SELECT s FROM SaoDetalleEvaluacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoDetalleEvaluacion.findByFechaIngre", query = "SELECT s FROM SaoDetalleEvaluacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoDetalleEvaluacion.findByHoraIngre", query = "SELECT s FROM SaoDetalleEvaluacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoDetalleEvaluacion.findByUsuarioActua", query = "SELECT s FROM SaoDetalleEvaluacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoDetalleEvaluacion.findByFechaActua", query = "SELECT s FROM SaoDetalleEvaluacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoDetalleEvaluacion.findByHoraActua", query = "SELECT s FROM SaoDetalleEvaluacion s WHERE s.horaActua = :horaActua")})
public class SaoDetalleEvaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sadee", nullable = false)
    private Integer ideSadee;
    @Size(max = 1000)
    @Column(name = "observacion_sadee", length = 1000)
    private String observacionSadee;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sadee", nullable = false)
    private boolean activoSadee;
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
    @JoinColumn(name = "ide_sapos", referencedColumnName = "ide_sapos")
    @ManyToOne
    private SaoPosiciograma ideSapos;
    @JoinColumn(name = "ide_saevp", referencedColumnName = "ide_saevp")
    @ManyToOne
    private SaoEvaluacionPosiciograma ideSaevp;
    @JoinColumn(name = "ide_sacal", referencedColumnName = "ide_sacal")
    @ManyToOne
    private SaoCalificacion ideSacal;

    public SaoDetalleEvaluacion() {
    }

    public SaoDetalleEvaluacion(Integer ideSadee) {
        this.ideSadee = ideSadee;
    }

    public SaoDetalleEvaluacion(Integer ideSadee, boolean activoSadee) {
        this.ideSadee = ideSadee;
        this.activoSadee = activoSadee;
    }

    public Integer getIdeSadee() {
        return ideSadee;
    }

    public void setIdeSadee(Integer ideSadee) {
        this.ideSadee = ideSadee;
    }

    public String getObservacionSadee() {
        return observacionSadee;
    }

    public void setObservacionSadee(String observacionSadee) {
        this.observacionSadee = observacionSadee;
    }

    public boolean getActivoSadee() {
        return activoSadee;
    }

    public void setActivoSadee(boolean activoSadee) {
        this.activoSadee = activoSadee;
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

    public SaoPosiciograma getIdeSapos() {
        return ideSapos;
    }

    public void setIdeSapos(SaoPosiciograma ideSapos) {
        this.ideSapos = ideSapos;
    }

    public SaoEvaluacionPosiciograma getIdeSaevp() {
        return ideSaevp;
    }

    public void setIdeSaevp(SaoEvaluacionPosiciograma ideSaevp) {
        this.ideSaevp = ideSaevp;
    }

    public SaoCalificacion getIdeSacal() {
        return ideSacal;
    }

    public void setIdeSacal(SaoCalificacion ideSacal) {
        this.ideSacal = ideSacal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSadee != null ? ideSadee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoDetalleEvaluacion)) {
            return false;
        }
        SaoDetalleEvaluacion other = (SaoDetalleEvaluacion) object;
        if ((this.ideSadee == null && other.ideSadee != null) || (this.ideSadee != null && !this.ideSadee.equals(other.ideSadee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoDetalleEvaluacion[ ideSadee=" + ideSadee + " ]";
    }
    
}
