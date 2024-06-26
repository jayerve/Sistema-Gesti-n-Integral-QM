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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "gth_membresia_empleado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthMembresiaEmpleado.findAll", query = "SELECT g FROM GthMembresiaEmpleado g"),
    @NamedQuery(name = "GthMembresiaEmpleado.findByIdeGtmee", query = "SELECT g FROM GthMembresiaEmpleado g WHERE g.ideGtmee = :ideGtmee"),
    @NamedQuery(name = "GthMembresiaEmpleado.findByFechaMembresiaGtmee", query = "SELECT g FROM GthMembresiaEmpleado g WHERE g.fechaMembresiaGtmee = :fechaMembresiaGtmee"),
    @NamedQuery(name = "GthMembresiaEmpleado.findByActivoGtmee", query = "SELECT g FROM GthMembresiaEmpleado g WHERE g.activoGtmee = :activoGtmee"),
    @NamedQuery(name = "GthMembresiaEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthMembresiaEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthMembresiaEmpleado.findByFechaIngre", query = "SELECT g FROM GthMembresiaEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthMembresiaEmpleado.findByUsuarioActua", query = "SELECT g FROM GthMembresiaEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthMembresiaEmpleado.findByFechaActua", query = "SELECT g FROM GthMembresiaEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthMembresiaEmpleado.findByHoraIngre", query = "SELECT g FROM GthMembresiaEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthMembresiaEmpleado.findByHoraActua", query = "SELECT g FROM GthMembresiaEmpleado g WHERE g.horaActua = :horaActua")})
public class GthMembresiaEmpleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtmee", nullable = false)
    private Integer ideGtmee;
    @Column(name = "fecha_membresia_gtmee")
    @Temporal(TemporalType.DATE)
    private Date fechaMembresiaGtmee;
    @Column(name = "activo_gtmee")
    private Boolean activoGtmee;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;

    public GthMembresiaEmpleado() {
    }

    public GthMembresiaEmpleado(Integer ideGtmee) {
        this.ideGtmee = ideGtmee;
    }

    public Integer getIdeGtmee() {
        return ideGtmee;
    }

    public void setIdeGtmee(Integer ideGtmee) {
        this.ideGtmee = ideGtmee;
    }

    public Date getFechaMembresiaGtmee() {
        return fechaMembresiaGtmee;
    }

    public void setFechaMembresiaGtmee(Date fechaMembresiaGtmee) {
        this.fechaMembresiaGtmee = fechaMembresiaGtmee;
    }

    public Boolean getActivoGtmee() {
        return activoGtmee;
    }

    public void setActivoGtmee(Boolean activoGtmee) {
        this.activoGtmee = activoGtmee;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtmee != null ? ideGtmee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthMembresiaEmpleado)) {
            return false;
        }
        GthMembresiaEmpleado other = (GthMembresiaEmpleado) object;
        if ((this.ideGtmee == null && other.ideGtmee != null) || (this.ideGtmee != null && !this.ideGtmee.equals(other.ideGtmee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthMembresiaEmpleado[ ideGtmee=" + ideGtmee + " ]";
    }
    
}
