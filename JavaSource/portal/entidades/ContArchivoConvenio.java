/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "cont_archivo_convenio", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContArchivoConvenio.findAll", query = "SELECT c FROM ContArchivoConvenio c"),
    @NamedQuery(name = "ContArchivoConvenio.findByIdeCoarc", query = "SELECT c FROM ContArchivoConvenio c WHERE c.ideCoarc = :ideCoarc"),
    @NamedQuery(name = "ContArchivoConvenio.findByIdeCocon", query = "SELECT c FROM ContArchivoConvenio c WHERE c.ideCocon = :ideCocon"),
    @NamedQuery(name = "ContArchivoConvenio.findByFechaCoarc", query = "SELECT c FROM ContArchivoConvenio c WHERE c.fechaCoarc = :fechaCoarc"),
    @NamedQuery(name = "ContArchivoConvenio.findByObservacionesCoarc", query = "SELECT c FROM ContArchivoConvenio c WHERE c.observacionesCoarc = :observacionesCoarc"),
    @NamedQuery(name = "ContArchivoConvenio.findByFotoCoarc", query = "SELECT c FROM ContArchivoConvenio c WHERE c.fotoCoarc = :fotoCoarc"),
    @NamedQuery(name = "ContArchivoConvenio.findByActivoCoarc", query = "SELECT c FROM ContArchivoConvenio c WHERE c.activoCoarc = :activoCoarc"),
    @NamedQuery(name = "ContArchivoConvenio.findByUsuarioIngre", query = "SELECT c FROM ContArchivoConvenio c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContArchivoConvenio.findByFechaIngre", query = "SELECT c FROM ContArchivoConvenio c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContArchivoConvenio.findByHoraIngre", query = "SELECT c FROM ContArchivoConvenio c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContArchivoConvenio.findByUsuarioActua", query = "SELECT c FROM ContArchivoConvenio c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContArchivoConvenio.findByFechaActua", query = "SELECT c FROM ContArchivoConvenio c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContArchivoConvenio.findByHoraActua", query = "SELECT c FROM ContArchivoConvenio c WHERE c.horaActua = :horaActua")})
public class ContArchivoConvenio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_coarc", nullable = false)
    private Long ideCoarc;
    @Column(name = "ide_cocon")
    private BigInteger ideCocon;
    @Column(name = "fecha_coarc")
    @Temporal(TemporalType.DATE)
    private Date fechaCoarc;
    @Size(max = 2147483647)
    @Column(name = "observaciones_coarc", length = 2147483647)
    private String observacionesCoarc;
    @Size(max = 250)
    @Column(name = "foto_coarc", length = 250)
    private String fotoCoarc;
    @Column(name = "activo_coarc")
    private Boolean activoCoarc;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;

    public ContArchivoConvenio() {
    }

    public ContArchivoConvenio(Long ideCoarc) {
        this.ideCoarc = ideCoarc;
    }

    public Long getIdeCoarc() {
        return ideCoarc;
    }

    public void setIdeCoarc(Long ideCoarc) {
        this.ideCoarc = ideCoarc;
    }

    public BigInteger getIdeCocon() {
        return ideCocon;
    }

    public void setIdeCocon(BigInteger ideCocon) {
        this.ideCocon = ideCocon;
    }

    public Date getFechaCoarc() {
        return fechaCoarc;
    }

    public void setFechaCoarc(Date fechaCoarc) {
        this.fechaCoarc = fechaCoarc;
    }

    public String getObservacionesCoarc() {
        return observacionesCoarc;
    }

    public void setObservacionesCoarc(String observacionesCoarc) {
        this.observacionesCoarc = observacionesCoarc;
    }

    public String getFotoCoarc() {
        return fotoCoarc;
    }

    public void setFotoCoarc(String fotoCoarc) {
        this.fotoCoarc = fotoCoarc;
    }

    public Boolean getActivoCoarc() {
        return activoCoarc;
    }

    public void setActivoCoarc(Boolean activoCoarc) {
        this.activoCoarc = activoCoarc;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCoarc != null ? ideCoarc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContArchivoConvenio)) {
            return false;
        }
        ContArchivoConvenio other = (ContArchivoConvenio) object;
        if ((this.ideCoarc == null && other.ideCoarc != null) || (this.ideCoarc != null && !this.ideCoarc.equals(other.ideCoarc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContArchivoConvenio[ ideCoarc=" + ideCoarc + " ]";
    }
    
}
