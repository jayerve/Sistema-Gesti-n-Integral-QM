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
@Table(name = "cont_tipo_asiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContTipoAsiento.findAll", query = "SELECT c FROM ContTipoAsiento c"),
    @NamedQuery(name = "ContTipoAsiento.findByIdeCotia", query = "SELECT c FROM ContTipoAsiento c WHERE c.ideCotia = :ideCotia"),
    @NamedQuery(name = "ContTipoAsiento.findByDetalleCotia", query = "SELECT c FROM ContTipoAsiento c WHERE c.detalleCotia = :detalleCotia"),
    @NamedQuery(name = "ContTipoAsiento.findByActivoCotia", query = "SELECT c FROM ContTipoAsiento c WHERE c.activoCotia = :activoCotia"),
    @NamedQuery(name = "ContTipoAsiento.findByUsuarioIngre", query = "SELECT c FROM ContTipoAsiento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContTipoAsiento.findByFechaIngre", query = "SELECT c FROM ContTipoAsiento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContTipoAsiento.findByHoraIngre", query = "SELECT c FROM ContTipoAsiento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContTipoAsiento.findByUsuarioActua", query = "SELECT c FROM ContTipoAsiento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContTipoAsiento.findByFechaActua", query = "SELECT c FROM ContTipoAsiento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContTipoAsiento.findByHoraActua", query = "SELECT c FROM ContTipoAsiento c WHERE c.horaActua = :horaActua")})
public class ContTipoAsiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cotia", nullable = false)
    private Long ideCotia;
    @Size(max = 50)
    @Column(name = "detalle_cotia", length = 50)
    private String detalleCotia;
    @Column(name = "activo_cotia")
    private Boolean activoCotia;
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
    @OneToMany(mappedBy = "ideCotia")
    private List<ContMovimiento> contMovimientoList;

    public ContTipoAsiento() {
    }

    public ContTipoAsiento(Long ideCotia) {
        this.ideCotia = ideCotia;
    }

    public Long getIdeCotia() {
        return ideCotia;
    }

    public void setIdeCotia(Long ideCotia) {
        this.ideCotia = ideCotia;
    }

    public String getDetalleCotia() {
        return detalleCotia;
    }

    public void setDetalleCotia(String detalleCotia) {
        this.detalleCotia = detalleCotia;
    }

    public Boolean getActivoCotia() {
        return activoCotia;
    }

    public void setActivoCotia(Boolean activoCotia) {
        this.activoCotia = activoCotia;
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
        hash += (ideCotia != null ? ideCotia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContTipoAsiento)) {
            return false;
        }
        ContTipoAsiento other = (ContTipoAsiento) object;
        if ((this.ideCotia == null && other.ideCotia != null) || (this.ideCotia != null && !this.ideCotia.equals(other.ideCotia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContTipoAsiento[ ideCotia=" + ideCotia + " ]";
    }
    
}
