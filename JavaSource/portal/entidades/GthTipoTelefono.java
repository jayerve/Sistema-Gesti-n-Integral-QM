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
@Table(name = "gth_tipo_telefono", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoTelefono.findAll", query = "SELECT g FROM GthTipoTelefono g"),
    @NamedQuery(name = "GthTipoTelefono.findByIdeGttit", query = "SELECT g FROM GthTipoTelefono g WHERE g.ideGttit = :ideGttit"),
    @NamedQuery(name = "GthTipoTelefono.findByDetalleGttit", query = "SELECT g FROM GthTipoTelefono g WHERE g.detalleGttit = :detalleGttit"),
    @NamedQuery(name = "GthTipoTelefono.findByActivoGttit", query = "SELECT g FROM GthTipoTelefono g WHERE g.activoGttit = :activoGttit"),
    @NamedQuery(name = "GthTipoTelefono.findByUsuarioIngre", query = "SELECT g FROM GthTipoTelefono g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoTelefono.findByFechaIngre", query = "SELECT g FROM GthTipoTelefono g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoTelefono.findByUsuarioActua", query = "SELECT g FROM GthTipoTelefono g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoTelefono.findByFechaActua", query = "SELECT g FROM GthTipoTelefono g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoTelefono.findByHoraIngre", query = "SELECT g FROM GthTipoTelefono g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoTelefono.findByHoraActua", query = "SELECT g FROM GthTipoTelefono g WHERE g.horaActua = :horaActua")})
public class GthTipoTelefono implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttit", nullable = false)
    private Integer ideGttit;
    @Size(max = 50)
    @Column(name = "detalle_gttit", length = 50)
    private String detalleGttit;
    @Column(name = "activo_gttit")
    private Boolean activoGttit;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideGttit")
    private List<GthTelefono> gthTelefonoList;

    public GthTipoTelefono() {
    }

    public GthTipoTelefono(Integer ideGttit) {
        this.ideGttit = ideGttit;
    }

    public Integer getIdeGttit() {
        return ideGttit;
    }

    public void setIdeGttit(Integer ideGttit) {
        this.ideGttit = ideGttit;
    }

    public String getDetalleGttit() {
        return detalleGttit;
    }

    public void setDetalleGttit(String detalleGttit) {
        this.detalleGttit = detalleGttit;
    }

    public Boolean getActivoGttit() {
        return activoGttit;
    }

    public void setActivoGttit(Boolean activoGttit) {
        this.activoGttit = activoGttit;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public List<GthTelefono> getGthTelefonoList() {
        return gthTelefonoList;
    }

    public void setGthTelefonoList(List<GthTelefono> gthTelefonoList) {
        this.gthTelefonoList = gthTelefonoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttit != null ? ideGttit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoTelefono)) {
            return false;
        }
        GthTipoTelefono other = (GthTipoTelefono) object;
        if ((this.ideGttit == null && other.ideGttit != null) || (this.ideGttit != null && !this.ideGttit.equals(other.ideGttit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoTelefono[ ideGttit=" + ideGttit + " ]";
    }
    
}
