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
@Table(name = "rec_cliente_direccion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecClienteDireccion.findAll", query = "SELECT r FROM RecClienteDireccion r"),
    @NamedQuery(name = "RecClienteDireccion.findByIdeRecld", query = "SELECT r FROM RecClienteDireccion r WHERE r.ideRecld = :ideRecld"),
    @NamedQuery(name = "RecClienteDireccion.findByDireccionRecld", query = "SELECT r FROM RecClienteDireccion r WHERE r.direccionRecld = :direccionRecld"),
    @NamedQuery(name = "RecClienteDireccion.findByNotificacionRecld", query = "SELECT r FROM RecClienteDireccion r WHERE r.notificacionRecld = :notificacionRecld"),
    @NamedQuery(name = "RecClienteDireccion.findByActivoRecld", query = "SELECT r FROM RecClienteDireccion r WHERE r.activoRecld = :activoRecld"),
    @NamedQuery(name = "RecClienteDireccion.findByUsuarioIngre", query = "SELECT r FROM RecClienteDireccion r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecClienteDireccion.findByFechaIngre", query = "SELECT r FROM RecClienteDireccion r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecClienteDireccion.findByHoraIngre", query = "SELECT r FROM RecClienteDireccion r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecClienteDireccion.findByUsuarioActua", query = "SELECT r FROM RecClienteDireccion r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecClienteDireccion.findByFechaActua", query = "SELECT r FROM RecClienteDireccion r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecClienteDireccion.findByHoraActua", query = "SELECT r FROM RecClienteDireccion r WHERE r.horaActua = :horaActua")})
public class RecClienteDireccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_recld", nullable = false)
    private Long ideRecld;
    @Size(max = 500)
    @Column(name = "direccion_recld", length = 500)
    private String direccionRecld;
    @Column(name = "notificacion_recld")
    private Boolean notificacionRecld;
    @Column(name = "activo_recld")
    private Boolean activoRecld;
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
    @JoinColumn(name = "ide_recli", referencedColumnName = "ide_recli")
    @ManyToOne
    private RecClientes ideRecli;

    public RecClienteDireccion() {
    }

    public RecClienteDireccion(Long ideRecld) {
        this.ideRecld = ideRecld;
    }

    public Long getIdeRecld() {
        return ideRecld;
    }

    public void setIdeRecld(Long ideRecld) {
        this.ideRecld = ideRecld;
    }

    public String getDireccionRecld() {
        return direccionRecld;
    }

    public void setDireccionRecld(String direccionRecld) {
        this.direccionRecld = direccionRecld;
    }

    public Boolean getNotificacionRecld() {
        return notificacionRecld;
    }

    public void setNotificacionRecld(Boolean notificacionRecld) {
        this.notificacionRecld = notificacionRecld;
    }

    public Boolean getActivoRecld() {
        return activoRecld;
    }

    public void setActivoRecld(Boolean activoRecld) {
        this.activoRecld = activoRecld;
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

    public RecClientes getIdeRecli() {
        return ideRecli;
    }

    public void setIdeRecli(RecClientes ideRecli) {
        this.ideRecli = ideRecli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRecld != null ? ideRecld.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecClienteDireccion)) {
            return false;
        }
        RecClienteDireccion other = (RecClienteDireccion) object;
        if ((this.ideRecld == null && other.ideRecld != null) || (this.ideRecld != null && !this.ideRecld.equals(other.ideRecld))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecClienteDireccion[ ideRecld=" + ideRecld + " ]";
    }
    
}
