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
@Table(name = "asi_novedad_justificacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiNovedadJustificacion.findAll", query = "SELECT a FROM AsiNovedadJustificacion a"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByIdeAsnoj", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.ideAsnoj = :ideAsnoj"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByDetalleAspej", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.detalleAspej = :detalleAspej"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByFechaAsnoj", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.fechaAsnoj = :fechaAsnoj"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByArchivoAsnoj", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.archivoAsnoj = :archivoAsnoj"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByActivoAsnoj", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.activoAsnoj = :activoAsnoj"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByUsuarioIngre", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByFechaIngre", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByUsuarioActua", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByFechaActua", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByHoraIngre", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiNovedadJustificacion.findByHoraActua", query = "SELECT a FROM AsiNovedadJustificacion a WHERE a.horaActua = :horaActua")})
public class AsiNovedadJustificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asnoj", nullable = false)
    private Integer ideAsnoj;
    @Size(max = 4000)
    @Column(name = "detalle_aspej", length = 4000)
    private String detalleAspej;
    @Column(name = "fecha_asnoj")
    @Temporal(TemporalType.DATE)
    private Date fechaAsnoj;
    @Size(max = 100)
    @Column(name = "archivo_asnoj", length = 100)
    private String archivoAsnoj;
    @Column(name = "activo_asnoj")
    private Boolean activoAsnoj;
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
    @JoinColumn(name = "ide_asnod", referencedColumnName = "ide_asnod")
    @ManyToOne
    private AsiNovedadDetalle ideAsnod;

    public AsiNovedadJustificacion() {
    }

    public AsiNovedadJustificacion(Integer ideAsnoj) {
        this.ideAsnoj = ideAsnoj;
    }

    public Integer getIdeAsnoj() {
        return ideAsnoj;
    }

    public void setIdeAsnoj(Integer ideAsnoj) {
        this.ideAsnoj = ideAsnoj;
    }

    public String getDetalleAspej() {
        return detalleAspej;
    }

    public void setDetalleAspej(String detalleAspej) {
        this.detalleAspej = detalleAspej;
    }

    public Date getFechaAsnoj() {
        return fechaAsnoj;
    }

    public void setFechaAsnoj(Date fechaAsnoj) {
        this.fechaAsnoj = fechaAsnoj;
    }

    public String getArchivoAsnoj() {
        return archivoAsnoj;
    }

    public void setArchivoAsnoj(String archivoAsnoj) {
        this.archivoAsnoj = archivoAsnoj;
    }

    public Boolean getActivoAsnoj() {
        return activoAsnoj;
    }

    public void setActivoAsnoj(Boolean activoAsnoj) {
        this.activoAsnoj = activoAsnoj;
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

    public AsiNovedadDetalle getIdeAsnod() {
        return ideAsnod;
    }

    public void setIdeAsnod(AsiNovedadDetalle ideAsnod) {
        this.ideAsnod = ideAsnod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsnoj != null ? ideAsnoj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiNovedadJustificacion)) {
            return false;
        }
        AsiNovedadJustificacion other = (AsiNovedadJustificacion) object;
        if ((this.ideAsnoj == null && other.ideAsnoj != null) || (this.ideAsnoj != null && !this.ideAsnoj.equals(other.ideAsnoj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiNovedadJustificacion[ ideAsnoj=" + ideAsnoj + " ]";
    }
    
}
