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
@Table(name = "sao_calificacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoCalificacion.findAll", query = "SELECT s FROM SaoCalificacion s"),
    @NamedQuery(name = "SaoCalificacion.findByIdeSacal", query = "SELECT s FROM SaoCalificacion s WHERE s.ideSacal = :ideSacal"),
    @NamedQuery(name = "SaoCalificacion.findByCalificacionSacal", query = "SELECT s FROM SaoCalificacion s WHERE s.calificacionSacal = :calificacionSacal"),
    @NamedQuery(name = "SaoCalificacion.findByActivoSacal", query = "SELECT s FROM SaoCalificacion s WHERE s.activoSacal = :activoSacal"),
    @NamedQuery(name = "SaoCalificacion.findByUsuarioIngre", query = "SELECT s FROM SaoCalificacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoCalificacion.findByFechaIngre", query = "SELECT s FROM SaoCalificacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoCalificacion.findByUsuarioActua", query = "SELECT s FROM SaoCalificacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoCalificacion.findByFechaActua", query = "SELECT s FROM SaoCalificacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoCalificacion.findByHoraIngre", query = "SELECT s FROM SaoCalificacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoCalificacion.findByHoraActua", query = "SELECT s FROM SaoCalificacion s WHERE s.horaActua = :horaActua")})
public class SaoCalificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sacal", nullable = false)
    private Integer ideSacal;
    @Column(name = "calificacion_sacal")
    private Integer calificacionSacal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sacal", nullable = false)
    private boolean activoSacal;
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
    @OneToMany(mappedBy = "ideSacal")
    private List<SaoDetalleEvaluacion> saoDetalleEvaluacionList;

    public SaoCalificacion() {
    }

    public SaoCalificacion(Integer ideSacal) {
        this.ideSacal = ideSacal;
    }

    public SaoCalificacion(Integer ideSacal, boolean activoSacal) {
        this.ideSacal = ideSacal;
        this.activoSacal = activoSacal;
    }

    public Integer getIdeSacal() {
        return ideSacal;
    }

    public void setIdeSacal(Integer ideSacal) {
        this.ideSacal = ideSacal;
    }

    public Integer getCalificacionSacal() {
        return calificacionSacal;
    }

    public void setCalificacionSacal(Integer calificacionSacal) {
        this.calificacionSacal = calificacionSacal;
    }

    public boolean getActivoSacal() {
        return activoSacal;
    }

    public void setActivoSacal(boolean activoSacal) {
        this.activoSacal = activoSacal;
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

    public List<SaoDetalleEvaluacion> getSaoDetalleEvaluacionList() {
        return saoDetalleEvaluacionList;
    }

    public void setSaoDetalleEvaluacionList(List<SaoDetalleEvaluacion> saoDetalleEvaluacionList) {
        this.saoDetalleEvaluacionList = saoDetalleEvaluacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSacal != null ? ideSacal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoCalificacion)) {
            return false;
        }
        SaoCalificacion other = (SaoCalificacion) object;
        if ((this.ideSacal == null && other.ideSacal != null) || (this.ideSacal != null && !this.ideSacal.equals(other.ideSacal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoCalificacion[ ideSacal=" + ideSacal + " ]";
    }
    
}
