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
@Table(name = "cont_cierre_movimiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContCierreMovimiento.findAll", query = "SELECT c FROM ContCierreMovimiento c"),
    @NamedQuery(name = "ContCierreMovimiento.findByIdeCocim", query = "SELECT c FROM ContCierreMovimiento c WHERE c.ideCocim = :ideCocim"),
    @NamedQuery(name = "ContCierreMovimiento.findByDetalleCocim", query = "SELECT c FROM ContCierreMovimiento c WHERE c.detalleCocim = :detalleCocim"),
    @NamedQuery(name = "ContCierreMovimiento.findByActivoCocim", query = "SELECT c FROM ContCierreMovimiento c WHERE c.activoCocim = :activoCocim"),
    @NamedQuery(name = "ContCierreMovimiento.findByUsuarioIngre", query = "SELECT c FROM ContCierreMovimiento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContCierreMovimiento.findByFechaIngre", query = "SELECT c FROM ContCierreMovimiento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContCierreMovimiento.findByHoraIngre", query = "SELECT c FROM ContCierreMovimiento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContCierreMovimiento.findByUsuarioActua", query = "SELECT c FROM ContCierreMovimiento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContCierreMovimiento.findByFechaActua", query = "SELECT c FROM ContCierreMovimiento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContCierreMovimiento.findByHoraActua", query = "SELECT c FROM ContCierreMovimiento c WHERE c.horaActua = :horaActua")})
public class ContCierreMovimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cocim", nullable = false)
    private Long ideCocim;
    @Size(max = 100)
    @Column(name = "detalle_cocim", length = 100)
    private String detalleCocim;
    @Column(name = "activo_cocim")
    private Boolean activoCocim;
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
    @OneToMany(mappedBy = "ideCocim")
    private List<ContCierreAno> contCierreAnoList;

    public ContCierreMovimiento() {
    }

    public ContCierreMovimiento(Long ideCocim) {
        this.ideCocim = ideCocim;
    }

    public Long getIdeCocim() {
        return ideCocim;
    }

    public void setIdeCocim(Long ideCocim) {
        this.ideCocim = ideCocim;
    }

    public String getDetalleCocim() {
        return detalleCocim;
    }

    public void setDetalleCocim(String detalleCocim) {
        this.detalleCocim = detalleCocim;
    }

    public Boolean getActivoCocim() {
        return activoCocim;
    }

    public void setActivoCocim(Boolean activoCocim) {
        this.activoCocim = activoCocim;
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

    public List<ContCierreAno> getContCierreAnoList() {
        return contCierreAnoList;
    }

    public void setContCierreAnoList(List<ContCierreAno> contCierreAnoList) {
        this.contCierreAnoList = contCierreAnoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCocim != null ? ideCocim.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContCierreMovimiento)) {
            return false;
        }
        ContCierreMovimiento other = (ContCierreMovimiento) object;
        if ((this.ideCocim == null && other.ideCocim != null) || (this.ideCocim != null && !this.ideCocim.equals(other.ideCocim))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContCierreMovimiento[ ideCocim=" + ideCocim + " ]";
    }
    
}
