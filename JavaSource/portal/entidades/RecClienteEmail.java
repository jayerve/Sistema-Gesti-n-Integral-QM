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
@Table(name = "rec_cliente_email", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecClienteEmail.findAll", query = "SELECT r FROM RecClienteEmail r"),
    @NamedQuery(name = "RecClienteEmail.findByIdeRecle", query = "SELECT r FROM RecClienteEmail r WHERE r.ideRecle = :ideRecle"),
    @NamedQuery(name = "RecClienteEmail.findByEmailRecle", query = "SELECT r FROM RecClienteEmail r WHERE r.emailRecle = :emailRecle"),
    @NamedQuery(name = "RecClienteEmail.findByNotificacionRecle", query = "SELECT r FROM RecClienteEmail r WHERE r.notificacionRecle = :notificacionRecle"),
    @NamedQuery(name = "RecClienteEmail.findByActivoRecle", query = "SELECT r FROM RecClienteEmail r WHERE r.activoRecle = :activoRecle"),
    @NamedQuery(name = "RecClienteEmail.findByUsuarioIngre", query = "SELECT r FROM RecClienteEmail r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecClienteEmail.findByFechaIngre", query = "SELECT r FROM RecClienteEmail r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecClienteEmail.findByHoraIngre", query = "SELECT r FROM RecClienteEmail r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecClienteEmail.findByUsuarioActua", query = "SELECT r FROM RecClienteEmail r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecClienteEmail.findByFechaActua", query = "SELECT r FROM RecClienteEmail r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecClienteEmail.findByHoraActua", query = "SELECT r FROM RecClienteEmail r WHERE r.horaActua = :horaActua")})
public class RecClienteEmail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_recle", nullable = false)
    private Long ideRecle;
    @Size(max = 100)
    @Column(name = "email_recle", length = 100)
    private String emailRecle;
    @Column(name = "notificacion_recle")
    private Boolean notificacionRecle;
    @Column(name = "activo_recle")
    private Boolean activoRecle;
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

    public RecClienteEmail() {
    }

    public RecClienteEmail(Long ideRecle) {
        this.ideRecle = ideRecle;
    }

    public Long getIdeRecle() {
        return ideRecle;
    }

    public void setIdeRecle(Long ideRecle) {
        this.ideRecle = ideRecle;
    }

    public String getEmailRecle() {
        return emailRecle;
    }

    public void setEmailRecle(String emailRecle) {
        this.emailRecle = emailRecle;
    }

    public Boolean getNotificacionRecle() {
        return notificacionRecle;
    }

    public void setNotificacionRecle(Boolean notificacionRecle) {
        this.notificacionRecle = notificacionRecle;
    }

    public Boolean getActivoRecle() {
        return activoRecle;
    }

    public void setActivoRecle(Boolean activoRecle) {
        this.activoRecle = activoRecle;
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
        hash += (ideRecle != null ? ideRecle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecClienteEmail)) {
            return false;
        }
        RecClienteEmail other = (RecClienteEmail) object;
        if ((this.ideRecle == null && other.ideRecle != null) || (this.ideRecle != null && !this.ideRecle.equals(other.ideRecle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecClienteEmail[ ideRecle=" + ideRecle + " ]";
    }
    
}
