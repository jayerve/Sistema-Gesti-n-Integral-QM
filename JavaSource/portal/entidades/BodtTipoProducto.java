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
@Table(name = "bodt_tipo_producto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtTipoProducto.findAll", query = "SELECT b FROM BodtTipoProducto b"),
    @NamedQuery(name = "BodtTipoProducto.findByIdeBotip", query = "SELECT b FROM BodtTipoProducto b WHERE b.ideBotip = :ideBotip"),
    @NamedQuery(name = "BodtTipoProducto.findByDetalleBotip", query = "SELECT b FROM BodtTipoProducto b WHERE b.detalleBotip = :detalleBotip"),
    @NamedQuery(name = "BodtTipoProducto.findByActivoBotip", query = "SELECT b FROM BodtTipoProducto b WHERE b.activoBotip = :activoBotip"),
    @NamedQuery(name = "BodtTipoProducto.findByUsuarioIngre", query = "SELECT b FROM BodtTipoProducto b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtTipoProducto.findByFechaIngre", query = "SELECT b FROM BodtTipoProducto b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtTipoProducto.findByHoraIngre", query = "SELECT b FROM BodtTipoProducto b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtTipoProducto.findByUsuarioActua", query = "SELECT b FROM BodtTipoProducto b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtTipoProducto.findByFechaActua", query = "SELECT b FROM BodtTipoProducto b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtTipoProducto.findByHoraActua", query = "SELECT b FROM BodtTipoProducto b WHERE b.horaActua = :horaActua")})
public class BodtTipoProducto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_botip", nullable = false)
    private Long ideBotip;
    @Size(max = 50)
    @Column(name = "detalle_botip", length = 50)
    private String detalleBotip;
    @Column(name = "activo_botip")
    private Boolean activoBotip;
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
    @OneToMany(mappedBy = "ideBotip")
    private List<BodtMaterial> bodtMaterialList;

    public BodtTipoProducto() {
    }

    public BodtTipoProducto(Long ideBotip) {
        this.ideBotip = ideBotip;
    }

    public Long getIdeBotip() {
        return ideBotip;
    }

    public void setIdeBotip(Long ideBotip) {
        this.ideBotip = ideBotip;
    }

    public String getDetalleBotip() {
        return detalleBotip;
    }

    public void setDetalleBotip(String detalleBotip) {
        this.detalleBotip = detalleBotip;
    }

    public Boolean getActivoBotip() {
        return activoBotip;
    }

    public void setActivoBotip(Boolean activoBotip) {
        this.activoBotip = activoBotip;
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

    public List<BodtMaterial> getBodtMaterialList() {
        return bodtMaterialList;
    }

    public void setBodtMaterialList(List<BodtMaterial> bodtMaterialList) {
        this.bodtMaterialList = bodtMaterialList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBotip != null ? ideBotip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtTipoProducto)) {
            return false;
        }
        BodtTipoProducto other = (BodtTipoProducto) object;
        if ((this.ideBotip == null && other.ideBotip != null) || (this.ideBotip != null && !this.ideBotip.equals(other.ideBotip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtTipoProducto[ ideBotip=" + ideBotip + " ]";
    }
    
}
