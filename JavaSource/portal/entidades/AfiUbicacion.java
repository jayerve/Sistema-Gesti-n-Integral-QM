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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "afi_ubicacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiUbicacion.findAll", query = "SELECT a FROM AfiUbicacion a"),
    @NamedQuery(name = "AfiUbicacion.findByIdeAfubi", query = "SELECT a FROM AfiUbicacion a WHERE a.ideAfubi = :ideAfubi"),
    @NamedQuery(name = "AfiUbicacion.findByDetalleAfubi", query = "SELECT a FROM AfiUbicacion a WHERE a.detalleAfubi = :detalleAfubi"),
    @NamedQuery(name = "AfiUbicacion.findByCodigoAfubi", query = "SELECT a FROM AfiUbicacion a WHERE a.codigoAfubi = :codigoAfubi"),
    @NamedQuery(name = "AfiUbicacion.findByActivoAfubi", query = "SELECT a FROM AfiUbicacion a WHERE a.activoAfubi = :activoAfubi"),
    @NamedQuery(name = "AfiUbicacion.findByUsuarioIngre", query = "SELECT a FROM AfiUbicacion a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiUbicacion.findByFechaIngre", query = "SELECT a FROM AfiUbicacion a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiUbicacion.findByHoraIngre", query = "SELECT a FROM AfiUbicacion a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiUbicacion.findByUsuarioActua", query = "SELECT a FROM AfiUbicacion a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiUbicacion.findByFechaActua", query = "SELECT a FROM AfiUbicacion a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiUbicacion.findByHoraActua", query = "SELECT a FROM AfiUbicacion a WHERE a.horaActua = :horaActua")})
public class AfiUbicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_afubi", nullable = false)
    private Long ideAfubi;
    @Size(max = 50)
    @Column(name = "detalle_afubi", length = 50)
    private String detalleAfubi;
    @Size(max = 50)
    @Column(name = "codigo_afubi", length = 50)
    private String codigoAfubi;
    @Column(name = "activo_afubi")
    private Boolean activoAfubi;
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
    @OneToMany(mappedBy = "afiIdeAfubi")
    private List<AfiUbicacion> afiUbicacionList;
    @JoinColumn(name = "afi_ide_afubi", referencedColumnName = "ide_afubi")
    @ManyToOne
    private AfiUbicacion afiIdeAfubi;
    @JoinColumn(name = "ide_aftid", referencedColumnName = "ide_aftid")
    @ManyToOne
    private AfiTipoDependencia ideAftid;

    public AfiUbicacion() {
    }

    public AfiUbicacion(Long ideAfubi) {
        this.ideAfubi = ideAfubi;
    }

    public Long getIdeAfubi() {
        return ideAfubi;
    }

    public void setIdeAfubi(Long ideAfubi) {
        this.ideAfubi = ideAfubi;
    }

    public String getDetalleAfubi() {
        return detalleAfubi;
    }

    public void setDetalleAfubi(String detalleAfubi) {
        this.detalleAfubi = detalleAfubi;
    }

    public String getCodigoAfubi() {
        return codigoAfubi;
    }

    public void setCodigoAfubi(String codigoAfubi) {
        this.codigoAfubi = codigoAfubi;
    }

    public Boolean getActivoAfubi() {
        return activoAfubi;
    }

    public void setActivoAfubi(Boolean activoAfubi) {
        this.activoAfubi = activoAfubi;
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

    public List<AfiUbicacion> getAfiUbicacionList() {
        return afiUbicacionList;
    }

    public void setAfiUbicacionList(List<AfiUbicacion> afiUbicacionList) {
        this.afiUbicacionList = afiUbicacionList;
    }

    public AfiUbicacion getAfiIdeAfubi() {
        return afiIdeAfubi;
    }

    public void setAfiIdeAfubi(AfiUbicacion afiIdeAfubi) {
        this.afiIdeAfubi = afiIdeAfubi;
    }

    public AfiTipoDependencia getIdeAftid() {
        return ideAftid;
    }

    public void setIdeAftid(AfiTipoDependencia ideAftid) {
        this.ideAftid = ideAftid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAfubi != null ? ideAfubi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiUbicacion)) {
            return false;
        }
        AfiUbicacion other = (AfiUbicacion) object;
        if ((this.ideAfubi == null && other.ideAfubi != null) || (this.ideAfubi != null && !this.ideAfubi.equals(other.ideAfubi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiUbicacion[ ideAfubi=" + ideAfubi + " ]";
    }
    
}
