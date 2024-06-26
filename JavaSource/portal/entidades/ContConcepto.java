/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "cont_concepto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContConcepto.findAll", query = "SELECT c FROM ContConcepto c"),
    @NamedQuery(name = "ContConcepto.findByIdeCocon", query = "SELECT c FROM ContConcepto c WHERE c.ideCocon = :ideCocon"),
    @NamedQuery(name = "ContConcepto.findByDetalleCocon", query = "SELECT c FROM ContConcepto c WHERE c.detalleCocon = :detalleCocon"),
    @NamedQuery(name = "ContConcepto.findByActivoCocon", query = "SELECT c FROM ContConcepto c WHERE c.activoCocon = :activoCocon"),
    @NamedQuery(name = "ContConcepto.findByUsuarioIngre", query = "SELECT c FROM ContConcepto c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContConcepto.findByFechaIngre", query = "SELECT c FROM ContConcepto c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContConcepto.findByHoraIngre", query = "SELECT c FROM ContConcepto c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContConcepto.findByUsuarioActua", query = "SELECT c FROM ContConcepto c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContConcepto.findByFechaActua", query = "SELECT c FROM ContConcepto c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContConcepto.findByHoraActua", query = "SELECT c FROM ContConcepto c WHERE c.horaActua = :horaActua")})
public class ContConcepto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cocon", nullable = false)
    private Long ideCocon;
    @Size(max = 100)
    @Column(name = "detalle_cocon", length = 100)
    private String detalleCocon;
    @Column(name = "activo_cocon")
    private Boolean activoCocon;
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

    public ContConcepto() {
    }

    public ContConcepto(Long ideCocon) {
        this.ideCocon = ideCocon;
    }

    public Long getIdeCocon() {
        return ideCocon;
    }

    public void setIdeCocon(Long ideCocon) {
        this.ideCocon = ideCocon;
    }

    public String getDetalleCocon() {
        return detalleCocon;
    }

    public void setDetalleCocon(String detalleCocon) {
        this.detalleCocon = detalleCocon;
    }

    public Boolean getActivoCocon() {
        return activoCocon;
    }

    public void setActivoCocon(Boolean activoCocon) {
        this.activoCocon = activoCocon;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCocon != null ? ideCocon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContConcepto)) {
            return false;
        }
        ContConcepto other = (ContConcepto) object;
        if ((this.ideCocon == null && other.ideCocon != null) || (this.ideCocon != null && !this.ideCocon.equals(other.ideCocon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContConcepto[ ideCocon=" + ideCocon + " ]";
    }
    
}
