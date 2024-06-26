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
@Table(name = "cont_tipo_movimiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContTipoMovimiento.findAll", query = "SELECT c FROM ContTipoMovimiento c"),
    @NamedQuery(name = "ContTipoMovimiento.findByIdeCotim", query = "SELECT c FROM ContTipoMovimiento c WHERE c.ideCotim = :ideCotim"),
    @NamedQuery(name = "ContTipoMovimiento.findByDetalleCotim", query = "SELECT c FROM ContTipoMovimiento c WHERE c.detalleCotim = :detalleCotim"),
    @NamedQuery(name = "ContTipoMovimiento.findByActivoCotim", query = "SELECT c FROM ContTipoMovimiento c WHERE c.activoCotim = :activoCotim"),
    @NamedQuery(name = "ContTipoMovimiento.findByUsuarioIngre", query = "SELECT c FROM ContTipoMovimiento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContTipoMovimiento.findByFechaIngre", query = "SELECT c FROM ContTipoMovimiento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContTipoMovimiento.findByHoraIngre", query = "SELECT c FROM ContTipoMovimiento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContTipoMovimiento.findByUsuarioActua", query = "SELECT c FROM ContTipoMovimiento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContTipoMovimiento.findByFechaActua", query = "SELECT c FROM ContTipoMovimiento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContTipoMovimiento.findByHoraActua", query = "SELECT c FROM ContTipoMovimiento c WHERE c.horaActua = :horaActua")})
public class ContTipoMovimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cotim", nullable = false)
    private Long ideCotim;
    @Size(max = 50)
    @Column(name = "detalle_cotim", length = 50)
    private String detalleCotim;
    @Column(name = "activo_cotim")
    private Boolean activoCotim;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaActua;
    @OneToMany(mappedBy = "ideCotim")
    private List<ContMovimiento> contMovimientoList;

    public ContTipoMovimiento() {
    }

    public ContTipoMovimiento(Long ideCotim) {
        this.ideCotim = ideCotim;
    }

    public Long getIdeCotim() {
        return ideCotim;
    }

    public void setIdeCotim(Long ideCotim) {
        this.ideCotim = ideCotim;
    }

    public String getDetalleCotim() {
        return detalleCotim;
    }

    public void setDetalleCotim(String detalleCotim) {
        this.detalleCotim = detalleCotim;
    }

    public Boolean getActivoCotim() {
        return activoCotim;
    }

    public void setActivoCotim(Boolean activoCotim) {
        this.activoCotim = activoCotim;
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

    public List<ContMovimiento> getContMovimientoList() {
        return contMovimientoList;
    }

    public void setContMovimientoList(List<ContMovimiento> contMovimientoList) {
        this.contMovimientoList = contMovimientoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCotim != null ? ideCotim.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContTipoMovimiento)) {
            return false;
        }
        ContTipoMovimiento other = (ContTipoMovimiento) object;
        if ((this.ideCotim == null && other.ideCotim != null) || (this.ideCotim != null && !this.ideCotim.equals(other.ideCotim))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContTipoMovimiento[ ideCotim=" + ideCotim + " ]";
    }
    
}
