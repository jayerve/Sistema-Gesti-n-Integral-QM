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
@Table(name = "spr_presupuesto_archivo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprPresupuestoArchivo.findAll", query = "SELECT s FROM SprPresupuestoArchivo s"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByIdeSppra", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.ideSppra = :ideSppra"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByArchivoSppra", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.archivoSppra = :archivoSppra"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByObservacionSppra", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.observacionSppra = :observacionSppra"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByActivoSppra", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.activoSppra = :activoSppra"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByUsuarioIngre", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByFechaIngre", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByHoraIngre", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByUsuarioActua", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByFechaActua", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprPresupuestoArchivo.findByHoraActua", query = "SELECT s FROM SprPresupuestoArchivo s WHERE s.horaActua = :horaActua")})
public class SprPresupuestoArchivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sppra", nullable = false)
    private Integer ideSppra;
    @Size(max = 100)
    @Column(name = "archivo_sppra", length = 100)
    private String archivoSppra;
    @Size(max = 4000)
    @Column(name = "observacion_sppra", length = 4000)
    private String observacionSppra;
    @Column(name = "activo_sppra")
    private Boolean activoSppra;
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
    @JoinColumn(name = "ide_spprp", referencedColumnName = "ide_spprp")
    @ManyToOne
    private SprPresupuestoPuesto ideSpprp;

    public SprPresupuestoArchivo() {
    }

    public SprPresupuestoArchivo(Integer ideSppra) {
        this.ideSppra = ideSppra;
    }

    public Integer getIdeSppra() {
        return ideSppra;
    }

    public void setIdeSppra(Integer ideSppra) {
        this.ideSppra = ideSppra;
    }

    public String getArchivoSppra() {
        return archivoSppra;
    }

    public void setArchivoSppra(String archivoSppra) {
        this.archivoSppra = archivoSppra;
    }

    public String getObservacionSppra() {
        return observacionSppra;
    }

    public void setObservacionSppra(String observacionSppra) {
        this.observacionSppra = observacionSppra;
    }

    public Boolean getActivoSppra() {
        return activoSppra;
    }

    public void setActivoSppra(Boolean activoSppra) {
        this.activoSppra = activoSppra;
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

    public SprPresupuestoPuesto getIdeSpprp() {
        return ideSpprp;
    }

    public void setIdeSpprp(SprPresupuestoPuesto ideSpprp) {
        this.ideSpprp = ideSpprp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSppra != null ? ideSppra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprPresupuestoArchivo)) {
            return false;
        }
        SprPresupuestoArchivo other = (SprPresupuestoArchivo) object;
        if ((this.ideSppra == null && other.ideSppra != null) || (this.ideSppra != null && !this.ideSppra.equals(other.ideSppra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprPresupuestoArchivo[ ideSppra=" + ideSppra + " ]";
    }
    
}
