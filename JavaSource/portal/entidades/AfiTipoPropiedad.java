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
@Table(name = "afi_tipo_propiedad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiTipoPropiedad.findAll", query = "SELECT a FROM AfiTipoPropiedad a"),
    @NamedQuery(name = "AfiTipoPropiedad.findByIdeAftip", query = "SELECT a FROM AfiTipoPropiedad a WHERE a.ideAftip = :ideAftip"),
    @NamedQuery(name = "AfiTipoPropiedad.findByDetalleAftip", query = "SELECT a FROM AfiTipoPropiedad a WHERE a.detalleAftip = :detalleAftip"),
    @NamedQuery(name = "AfiTipoPropiedad.findByActivoAftip", query = "SELECT a FROM AfiTipoPropiedad a WHERE a.activoAftip = :activoAftip"),
    @NamedQuery(name = "AfiTipoPropiedad.findByUsuarioIngre", query = "SELECT a FROM AfiTipoPropiedad a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiTipoPropiedad.findByFechaIngre", query = "SELECT a FROM AfiTipoPropiedad a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiTipoPropiedad.findByHoraIngre", query = "SELECT a FROM AfiTipoPropiedad a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiTipoPropiedad.findByUsuarioActua", query = "SELECT a FROM AfiTipoPropiedad a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiTipoPropiedad.findByFechaActua", query = "SELECT a FROM AfiTipoPropiedad a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiTipoPropiedad.findByHoraActua", query = "SELECT a FROM AfiTipoPropiedad a WHERE a.horaActua = :horaActua")})
public class AfiTipoPropiedad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_aftip", nullable = false)
    private Integer ideAftip;
    @Size(max = 50)
    @Column(name = "detalle_aftip", length = 50)
    private String detalleAftip;
    @Column(name = "activo_aftip")
    private Boolean activoAftip;
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
    @OneToMany(mappedBy = "ideAftip")
    private List<AfiActivo> afiActivoList;

    public AfiTipoPropiedad() {
    }

    public AfiTipoPropiedad(Integer ideAftip) {
        this.ideAftip = ideAftip;
    }

    public Integer getIdeAftip() {
        return ideAftip;
    }

    public void setIdeAftip(Integer ideAftip) {
        this.ideAftip = ideAftip;
    }

    public String getDetalleAftip() {
        return detalleAftip;
    }

    public void setDetalleAftip(String detalleAftip) {
        this.detalleAftip = detalleAftip;
    }

    public Boolean getActivoAftip() {
        return activoAftip;
    }

    public void setActivoAftip(Boolean activoAftip) {
        this.activoAftip = activoAftip;
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

    public List<AfiActivo> getAfiActivoList() {
        return afiActivoList;
    }

    public void setAfiActivoList(List<AfiActivo> afiActivoList) {
        this.afiActivoList = afiActivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAftip != null ? ideAftip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiTipoPropiedad)) {
            return false;
        }
        AfiTipoPropiedad other = (AfiTipoPropiedad) object;
        if ((this.ideAftip == null && other.ideAftip != null) || (this.ideAftip != null && !this.ideAftip.equals(other.ideAftip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiTipoPropiedad[ ideAftip=" + ideAftip + " ]";
    }
    
}
