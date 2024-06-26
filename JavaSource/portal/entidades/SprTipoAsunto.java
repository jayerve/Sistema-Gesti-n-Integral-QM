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
@Table(name = "spr_tipo_asunto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprTipoAsunto.findAll", query = "SELECT s FROM SprTipoAsunto s"),
    @NamedQuery(name = "SprTipoAsunto.findByIdeSptia", query = "SELECT s FROM SprTipoAsunto s WHERE s.ideSptia = :ideSptia"),
    @NamedQuery(name = "SprTipoAsunto.findByDetalleSptia", query = "SELECT s FROM SprTipoAsunto s WHERE s.detalleSptia = :detalleSptia"),
    @NamedQuery(name = "SprTipoAsunto.findByActivoSptia", query = "SELECT s FROM SprTipoAsunto s WHERE s.activoSptia = :activoSptia"),
    @NamedQuery(name = "SprTipoAsunto.findByUsuarioIngre", query = "SELECT s FROM SprTipoAsunto s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprTipoAsunto.findByFechaIngre", query = "SELECT s FROM SprTipoAsunto s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprTipoAsunto.findByHoraIngre", query = "SELECT s FROM SprTipoAsunto s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprTipoAsunto.findByUsuarioActua", query = "SELECT s FROM SprTipoAsunto s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprTipoAsunto.findByFechaActua", query = "SELECT s FROM SprTipoAsunto s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprTipoAsunto.findByHoraActua", query = "SELECT s FROM SprTipoAsunto s WHERE s.horaActua = :horaActua")})
public class SprTipoAsunto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sptia", nullable = false)
    private Integer ideSptia;
    @Size(max = 50)
    @Column(name = "detalle_sptia", length = 50)
    private String detalleSptia;
    @Column(name = "activo_sptia")
    private Boolean activoSptia;
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
    @OneToMany(mappedBy = "ideSptia")
    private List<SprAsunto> sprAsuntoList;

    public SprTipoAsunto() {
    }

    public SprTipoAsunto(Integer ideSptia) {
        this.ideSptia = ideSptia;
    }

    public Integer getIdeSptia() {
        return ideSptia;
    }

    public void setIdeSptia(Integer ideSptia) {
        this.ideSptia = ideSptia;
    }

    public String getDetalleSptia() {
        return detalleSptia;
    }

    public void setDetalleSptia(String detalleSptia) {
        this.detalleSptia = detalleSptia;
    }

    public Boolean getActivoSptia() {
        return activoSptia;
    }

    public void setActivoSptia(Boolean activoSptia) {
        this.activoSptia = activoSptia;
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

    public List<SprAsunto> getSprAsuntoList() {
        return sprAsuntoList;
    }

    public void setSprAsuntoList(List<SprAsunto> sprAsuntoList) {
        this.sprAsuntoList = sprAsuntoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSptia != null ? ideSptia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprTipoAsunto)) {
            return false;
        }
        SprTipoAsunto other = (SprTipoAsunto) object;
        if ((this.ideSptia == null && other.ideSptia != null) || (this.ideSptia != null && !this.ideSptia.equals(other.ideSptia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprTipoAsunto[ ideSptia=" + ideSptia + " ]";
    }
    
}
