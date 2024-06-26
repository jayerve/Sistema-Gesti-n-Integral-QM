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
@Table(name = "cont_clase_viaje", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContClaseViaje.findAll", query = "SELECT c FROM ContClaseViaje c"),
    @NamedQuery(name = "ContClaseViaje.findByIdeCoclv", query = "SELECT c FROM ContClaseViaje c WHERE c.ideCoclv = :ideCoclv"),
    @NamedQuery(name = "ContClaseViaje.findByDetalleCoclv", query = "SELECT c FROM ContClaseViaje c WHERE c.detalleCoclv = :detalleCoclv"),
    @NamedQuery(name = "ContClaseViaje.findByActivoCoclv", query = "SELECT c FROM ContClaseViaje c WHERE c.activoCoclv = :activoCoclv"),
    @NamedQuery(name = "ContClaseViaje.findByUsuarioIngre", query = "SELECT c FROM ContClaseViaje c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContClaseViaje.findByFechaIngre", query = "SELECT c FROM ContClaseViaje c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContClaseViaje.findByHoraIngre", query = "SELECT c FROM ContClaseViaje c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContClaseViaje.findByUsuarioActua", query = "SELECT c FROM ContClaseViaje c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContClaseViaje.findByFechaActua", query = "SELECT c FROM ContClaseViaje c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContClaseViaje.findByHoraActua", query = "SELECT c FROM ContClaseViaje c WHERE c.horaActua = :horaActua")})
public class ContClaseViaje implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_coclv", nullable = false)
    private Long ideCoclv;
    @Size(max = 50)
    @Column(name = "detalle_coclv", length = 50)
    private String detalleCoclv;
    @Column(name = "activo_coclv")
    private Boolean activoCoclv;
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
    @OneToMany(mappedBy = "ideCoclv")
    private List<ContViajeros> contViajerosList;

    public ContClaseViaje() {
    }

    public ContClaseViaje(Long ideCoclv) {
        this.ideCoclv = ideCoclv;
    }

    public Long getIdeCoclv() {
        return ideCoclv;
    }

    public void setIdeCoclv(Long ideCoclv) {
        this.ideCoclv = ideCoclv;
    }

    public String getDetalleCoclv() {
        return detalleCoclv;
    }

    public void setDetalleCoclv(String detalleCoclv) {
        this.detalleCoclv = detalleCoclv;
    }

    public Boolean getActivoCoclv() {
        return activoCoclv;
    }

    public void setActivoCoclv(Boolean activoCoclv) {
        this.activoCoclv = activoCoclv;
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

    public List<ContViajeros> getContViajerosList() {
        return contViajerosList;
    }

    public void setContViajerosList(List<ContViajeros> contViajerosList) {
        this.contViajerosList = contViajerosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCoclv != null ? ideCoclv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContClaseViaje)) {
            return false;
        }
        ContClaseViaje other = (ContClaseViaje) object;
        if ((this.ideCoclv == null && other.ideCoclv != null) || (this.ideCoclv != null && !this.ideCoclv.equals(other.ideCoclv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContClaseViaje[ ideCoclv=" + ideCoclv + " ]";
    }
    
}
