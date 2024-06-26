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
@Table(name = "afi_tipo_activo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiTipoActivo.findAll", query = "SELECT a FROM AfiTipoActivo a"),
    @NamedQuery(name = "AfiTipoActivo.findByIdeAftia", query = "SELECT a FROM AfiTipoActivo a WHERE a.ideAftia = :ideAftia"),
    @NamedQuery(name = "AfiTipoActivo.findByDetalleAftia", query = "SELECT a FROM AfiTipoActivo a WHERE a.detalleAftia = :detalleAftia"),
    @NamedQuery(name = "AfiTipoActivo.findByActivoAftia", query = "SELECT a FROM AfiTipoActivo a WHERE a.activoAftia = :activoAftia"),
    @NamedQuery(name = "AfiTipoActivo.findByUsuarioIngre", query = "SELECT a FROM AfiTipoActivo a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiTipoActivo.findByFechaIngre", query = "SELECT a FROM AfiTipoActivo a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiTipoActivo.findByHoraIngre", query = "SELECT a FROM AfiTipoActivo a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiTipoActivo.findByUsuarioActua", query = "SELECT a FROM AfiTipoActivo a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiTipoActivo.findByFechaActua", query = "SELECT a FROM AfiTipoActivo a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiTipoActivo.findByHoraActua", query = "SELECT a FROM AfiTipoActivo a WHERE a.horaActua = :horaActua")})
public class AfiTipoActivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_aftia", nullable = false)
    private Integer ideAftia;
    @Size(max = 50)
    @Column(name = "detalle_aftia", length = 50)
    private String detalleAftia;
    @Column(name = "activo_aftia")
    private Boolean activoAftia;
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
    @OneToMany(mappedBy = "ideAftia")
    private List<AfiActivo> afiActivoList;

    public AfiTipoActivo() {
    }

    public AfiTipoActivo(Integer ideAftia) {
        this.ideAftia = ideAftia;
    }

    public Integer getIdeAftia() {
        return ideAftia;
    }

    public void setIdeAftia(Integer ideAftia) {
        this.ideAftia = ideAftia;
    }

    public String getDetalleAftia() {
        return detalleAftia;
    }

    public void setDetalleAftia(String detalleAftia) {
        this.detalleAftia = detalleAftia;
    }

    public Boolean getActivoAftia() {
        return activoAftia;
    }

    public void setActivoAftia(Boolean activoAftia) {
        this.activoAftia = activoAftia;
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
        hash += (ideAftia != null ? ideAftia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiTipoActivo)) {
            return false;
        }
        AfiTipoActivo other = (AfiTipoActivo) object;
        if ((this.ideAftia == null && other.ideAftia != null) || (this.ideAftia != null && !this.ideAftia.equals(other.ideAftia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiTipoActivo[ ideAftia=" + ideAftia + " ]";
    }
    
}
