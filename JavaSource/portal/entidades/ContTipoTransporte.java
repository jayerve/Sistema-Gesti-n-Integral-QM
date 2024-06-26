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
@Table(name = "cont_tipo_transporte", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContTipoTransporte.findAll", query = "SELECT c FROM ContTipoTransporte c"),
    @NamedQuery(name = "ContTipoTransporte.findByIdeCotit", query = "SELECT c FROM ContTipoTransporte c WHERE c.ideCotit = :ideCotit"),
    @NamedQuery(name = "ContTipoTransporte.findByDetalleCotit", query = "SELECT c FROM ContTipoTransporte c WHERE c.detalleCotit = :detalleCotit"),
    @NamedQuery(name = "ContTipoTransporte.findByActivoCotit", query = "SELECT c FROM ContTipoTransporte c WHERE c.activoCotit = :activoCotit"),
    @NamedQuery(name = "ContTipoTransporte.findByUsuarioIngre", query = "SELECT c FROM ContTipoTransporte c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContTipoTransporte.findByFechaIngre", query = "SELECT c FROM ContTipoTransporte c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContTipoTransporte.findByHoraIngre", query = "SELECT c FROM ContTipoTransporte c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContTipoTransporte.findByUsuarioActua", query = "SELECT c FROM ContTipoTransporte c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContTipoTransporte.findByFechaActua", query = "SELECT c FROM ContTipoTransporte c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContTipoTransporte.findByHoraActua", query = "SELECT c FROM ContTipoTransporte c WHERE c.horaActua = :horaActua")})
public class ContTipoTransporte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cotit", nullable = false)
    private Long ideCotit;
    @Size(max = 50)
    @Column(name = "detalle_cotit", length = 50)
    private String detalleCotit;
    @Column(name = "activo_cotit")
    private Boolean activoCotit;
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
    @OneToMany(mappedBy = "ideCotit")
    private List<ContTiketViaje> contTiketViajeList;

    public ContTipoTransporte() {
    }

    public ContTipoTransporte(Long ideCotit) {
        this.ideCotit = ideCotit;
    }

    public Long getIdeCotit() {
        return ideCotit;
    }

    public void setIdeCotit(Long ideCotit) {
        this.ideCotit = ideCotit;
    }

    public String getDetalleCotit() {
        return detalleCotit;
    }

    public void setDetalleCotit(String detalleCotit) {
        this.detalleCotit = detalleCotit;
    }

    public Boolean getActivoCotit() {
        return activoCotit;
    }

    public void setActivoCotit(Boolean activoCotit) {
        this.activoCotit = activoCotit;
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

    public List<ContTiketViaje> getContTiketViajeList() {
        return contTiketViajeList;
    }

    public void setContTiketViajeList(List<ContTiketViaje> contTiketViajeList) {
        this.contTiketViajeList = contTiketViajeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCotit != null ? ideCotit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContTipoTransporte)) {
            return false;
        }
        ContTipoTransporte other = (ContTipoTransporte) object;
        if ((this.ideCotit == null && other.ideCotit != null) || (this.ideCotit != null && !this.ideCotit.equals(other.ideCotit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContTipoTransporte[ ideCotit=" + ideCotit + " ]";
    }
    
}
