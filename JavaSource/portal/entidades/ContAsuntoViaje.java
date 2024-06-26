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
@Table(name = "cont_asunto_viaje", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContAsuntoViaje.findAll", query = "SELECT c FROM ContAsuntoViaje c"),
    @NamedQuery(name = "ContAsuntoViaje.findByIdeCoasv", query = "SELECT c FROM ContAsuntoViaje c WHERE c.ideCoasv = :ideCoasv"),
    @NamedQuery(name = "ContAsuntoViaje.findByDetalleCoasv", query = "SELECT c FROM ContAsuntoViaje c WHERE c.detalleCoasv = :detalleCoasv"),
    @NamedQuery(name = "ContAsuntoViaje.findByActivoCoasv", query = "SELECT c FROM ContAsuntoViaje c WHERE c.activoCoasv = :activoCoasv"),
    @NamedQuery(name = "ContAsuntoViaje.findByUsuarioIngre", query = "SELECT c FROM ContAsuntoViaje c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContAsuntoViaje.findByFechaIngre", query = "SELECT c FROM ContAsuntoViaje c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContAsuntoViaje.findByHoraIngre", query = "SELECT c FROM ContAsuntoViaje c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContAsuntoViaje.findByUsuarioActua", query = "SELECT c FROM ContAsuntoViaje c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContAsuntoViaje.findByFechaActua", query = "SELECT c FROM ContAsuntoViaje c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContAsuntoViaje.findByHoraActua", query = "SELECT c FROM ContAsuntoViaje c WHERE c.horaActua = :horaActua")})
public class ContAsuntoViaje implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_coasv", nullable = false)
    private Long ideCoasv;
    @Size(max = 50)
    @Column(name = "detalle_coasv", length = 50)
    private String detalleCoasv;
    @Column(name = "activo_coasv")
    private Boolean activoCoasv;
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
    @OneToMany(mappedBy = "ideCoasv")
    private List<ContTiketViaje> contTiketViajeList;

    public ContAsuntoViaje() {
    }

    public ContAsuntoViaje(Long ideCoasv) {
        this.ideCoasv = ideCoasv;
    }

    public Long getIdeCoasv() {
        return ideCoasv;
    }

    public void setIdeCoasv(Long ideCoasv) {
        this.ideCoasv = ideCoasv;
    }

    public String getDetalleCoasv() {
        return detalleCoasv;
    }

    public void setDetalleCoasv(String detalleCoasv) {
        this.detalleCoasv = detalleCoasv;
    }

    public Boolean getActivoCoasv() {
        return activoCoasv;
    }

    public void setActivoCoasv(Boolean activoCoasv) {
        this.activoCoasv = activoCoasv;
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
        hash += (ideCoasv != null ? ideCoasv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContAsuntoViaje)) {
            return false;
        }
        ContAsuntoViaje other = (ContAsuntoViaje) object;
        if ((this.ideCoasv == null && other.ideCoasv != null) || (this.ideCoasv != null && !this.ideCoasv.equals(other.ideCoasv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContAsuntoViaje[ ideCoasv=" + ideCoasv + " ]";
    }
    
}
