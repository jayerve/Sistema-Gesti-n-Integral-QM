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
@Table(name = "afi_nombre_activo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiNombreActivo.findAll", query = "SELECT a FROM AfiNombreActivo a"),
    @NamedQuery(name = "AfiNombreActivo.findByIdeAfnoa", query = "SELECT a FROM AfiNombreActivo a WHERE a.ideAfnoa = :ideAfnoa"),
    @NamedQuery(name = "AfiNombreActivo.findByDetalleAfnoa", query = "SELECT a FROM AfiNombreActivo a WHERE a.detalleAfnoa = :detalleAfnoa"),
    @NamedQuery(name = "AfiNombreActivo.findByActivoAfnoa", query = "SELECT a FROM AfiNombreActivo a WHERE a.activoAfnoa = :activoAfnoa"),
    @NamedQuery(name = "AfiNombreActivo.findByUsuarioIngre", query = "SELECT a FROM AfiNombreActivo a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiNombreActivo.findByFechaIngre", query = "SELECT a FROM AfiNombreActivo a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiNombreActivo.findByHoraIngre", query = "SELECT a FROM AfiNombreActivo a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiNombreActivo.findByUsuarioActua", query = "SELECT a FROM AfiNombreActivo a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiNombreActivo.findByFechaActua", query = "SELECT a FROM AfiNombreActivo a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiNombreActivo.findByHoraActua", query = "SELECT a FROM AfiNombreActivo a WHERE a.horaActua = :horaActua")})
public class AfiNombreActivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_afnoa", nullable = false)
    private Long ideAfnoa;
    @Size(max = 50)
    @Column(name = "detalle_afnoa", length = 50)
    private String detalleAfnoa;
    @Column(name = "activo_afnoa")
    private Boolean activoAfnoa;
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
    @JoinColumn(name = "ide_afgra", referencedColumnName = "ide_afgra")
    @ManyToOne
    private AfiGrupoActivo ideAfgra;

    public AfiNombreActivo() {
    }

    public AfiNombreActivo(Long ideAfnoa) {
        this.ideAfnoa = ideAfnoa;
    }

    public Long getIdeAfnoa() {
        return ideAfnoa;
    }

    public void setIdeAfnoa(Long ideAfnoa) {
        this.ideAfnoa = ideAfnoa;
    }

    public String getDetalleAfnoa() {
        return detalleAfnoa;
    }

    public void setDetalleAfnoa(String detalleAfnoa) {
        this.detalleAfnoa = detalleAfnoa;
    }

    public Boolean getActivoAfnoa() {
        return activoAfnoa;
    }

    public void setActivoAfnoa(Boolean activoAfnoa) {
        this.activoAfnoa = activoAfnoa;
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

    public AfiGrupoActivo getIdeAfgra() {
        return ideAfgra;
    }

    public void setIdeAfgra(AfiGrupoActivo ideAfgra) {
        this.ideAfgra = ideAfgra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAfnoa != null ? ideAfnoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiNombreActivo)) {
            return false;
        }
        AfiNombreActivo other = (AfiNombreActivo) object;
        if ((this.ideAfnoa == null && other.ideAfnoa != null) || (this.ideAfnoa != null && !this.ideAfnoa.equals(other.ideAfnoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiNombreActivo[ ideAfnoa=" + ideAfnoa + " ]";
    }
    
}
