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
@Table(name = "rec_cliente_telefono", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecClienteTelefono.findAll", query = "SELECT r FROM RecClienteTelefono r"),
    @NamedQuery(name = "RecClienteTelefono.findByIdeReclt", query = "SELECT r FROM RecClienteTelefono r WHERE r.ideReclt = :ideReclt"),
    @NamedQuery(name = "RecClienteTelefono.findByTelefonoReclt", query = "SELECT r FROM RecClienteTelefono r WHERE r.telefonoReclt = :telefonoReclt"),
    @NamedQuery(name = "RecClienteTelefono.findByNotificacionReclt", query = "SELECT r FROM RecClienteTelefono r WHERE r.notificacionReclt = :notificacionReclt"),
    @NamedQuery(name = "RecClienteTelefono.findByActivoReclt", query = "SELECT r FROM RecClienteTelefono r WHERE r.activoReclt = :activoReclt"),
    @NamedQuery(name = "RecClienteTelefono.findByUsuarioIngre", query = "SELECT r FROM RecClienteTelefono r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecClienteTelefono.findByFechaIngre", query = "SELECT r FROM RecClienteTelefono r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecClienteTelefono.findByHoraIngre", query = "SELECT r FROM RecClienteTelefono r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecClienteTelefono.findByUsuarioActua", query = "SELECT r FROM RecClienteTelefono r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecClienteTelefono.findByFechaActua", query = "SELECT r FROM RecClienteTelefono r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecClienteTelefono.findByHoraActua", query = "SELECT r FROM RecClienteTelefono r WHERE r.horaActua = :horaActua")})
public class RecClienteTelefono implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_reclt", nullable = false)
    private Long ideReclt;
    @Size(max = 10)
    @Column(name = "telefono_reclt", length = 10)
    private String telefonoReclt;
    @Column(name = "notificacion_reclt")
    private Boolean notificacionReclt;
    @Column(name = "activo_reclt")
    private Boolean activoReclt;
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
    @JoinColumn(name = "ide_reteo", referencedColumnName = "ide_reteo")
    @ManyToOne
    private RecTelefonoOperadora ideReteo;
    @JoinColumn(name = "ide_recli", referencedColumnName = "ide_recli")
    @ManyToOne
    private RecClientes ideRecli;

    public RecClienteTelefono() {
    }

    public RecClienteTelefono(Long ideReclt) {
        this.ideReclt = ideReclt;
    }

    public Long getIdeReclt() {
        return ideReclt;
    }

    public void setIdeReclt(Long ideReclt) {
        this.ideReclt = ideReclt;
    }

    public String getTelefonoReclt() {
        return telefonoReclt;
    }

    public void setTelefonoReclt(String telefonoReclt) {
        this.telefonoReclt = telefonoReclt;
    }

    public Boolean getNotificacionReclt() {
        return notificacionReclt;
    }

    public void setNotificacionReclt(Boolean notificacionReclt) {
        this.notificacionReclt = notificacionReclt;
    }

    public Boolean getActivoReclt() {
        return activoReclt;
    }

    public void setActivoReclt(Boolean activoReclt) {
        this.activoReclt = activoReclt;
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

    public RecTelefonoOperadora getIdeReteo() {
        return ideReteo;
    }

    public void setIdeReteo(RecTelefonoOperadora ideReteo) {
        this.ideReteo = ideReteo;
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
        hash += (ideReclt != null ? ideReclt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecClienteTelefono)) {
            return false;
        }
        RecClienteTelefono other = (RecClienteTelefono) object;
        if ((this.ideReclt == null && other.ideReclt != null) || (this.ideReclt != null && !this.ideReclt.equals(other.ideReclt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecClienteTelefono[ ideReclt=" + ideReclt + " ]";
    }
    
}
