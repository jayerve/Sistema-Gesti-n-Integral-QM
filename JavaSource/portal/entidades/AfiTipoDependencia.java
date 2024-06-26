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
@Table(name = "afi_tipo_dependencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiTipoDependencia.findAll", query = "SELECT a FROM AfiTipoDependencia a"),
    @NamedQuery(name = "AfiTipoDependencia.findByIdeAftid", query = "SELECT a FROM AfiTipoDependencia a WHERE a.ideAftid = :ideAftid"),
    @NamedQuery(name = "AfiTipoDependencia.findByDetalleAftid", query = "SELECT a FROM AfiTipoDependencia a WHERE a.detalleAftid = :detalleAftid"),
    @NamedQuery(name = "AfiTipoDependencia.findByActivoAftid", query = "SELECT a FROM AfiTipoDependencia a WHERE a.activoAftid = :activoAftid"),
    @NamedQuery(name = "AfiTipoDependencia.findByUsuarioIngre", query = "SELECT a FROM AfiTipoDependencia a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiTipoDependencia.findByFechaIngre", query = "SELECT a FROM AfiTipoDependencia a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiTipoDependencia.findByHoraIngre", query = "SELECT a FROM AfiTipoDependencia a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiTipoDependencia.findByUsuarioActua", query = "SELECT a FROM AfiTipoDependencia a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiTipoDependencia.findByFechaActua", query = "SELECT a FROM AfiTipoDependencia a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiTipoDependencia.findByHoraActua", query = "SELECT a FROM AfiTipoDependencia a WHERE a.horaActua = :horaActua")})
public class AfiTipoDependencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_aftid", nullable = false)
    private Integer ideAftid;
    @Size(max = 50)
    @Column(name = "detalle_aftid", length = 50)
    private String detalleAftid;
    @Column(name = "activo_aftid")
    private Boolean activoAftid;
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
    @OneToMany(mappedBy = "ideAftid")
    private List<AfiUbicacion> afiUbicacionList;

    public AfiTipoDependencia() {
    }

    public AfiTipoDependencia(Integer ideAftid) {
        this.ideAftid = ideAftid;
    }

    public Integer getIdeAftid() {
        return ideAftid;
    }

    public void setIdeAftid(Integer ideAftid) {
        this.ideAftid = ideAftid;
    }

    public String getDetalleAftid() {
        return detalleAftid;
    }

    public void setDetalleAftid(String detalleAftid) {
        this.detalleAftid = detalleAftid;
    }

    public Boolean getActivoAftid() {
        return activoAftid;
    }

    public void setActivoAftid(Boolean activoAftid) {
        this.activoAftid = activoAftid;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAftid != null ? ideAftid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiTipoDependencia)) {
            return false;
        }
        AfiTipoDependencia other = (AfiTipoDependencia) object;
        if ((this.ideAftid == null && other.ideAftid != null) || (this.ideAftid != null && !this.ideAftid.equals(other.ideAftid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiTipoDependencia[ ideAftid=" + ideAftid + " ]";
    }
    
}
