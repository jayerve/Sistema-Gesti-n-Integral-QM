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
@Table(name = "tes_telefono", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesTelefono.findAll", query = "SELECT t FROM TesTelefono t"),
    @NamedQuery(name = "TesTelefono.findByIdeTetel", query = "SELECT t FROM TesTelefono t WHERE t.ideTetel = :ideTetel"),
    @NamedQuery(name = "TesTelefono.findByNumeroTelefonoTele", query = "SELECT t FROM TesTelefono t WHERE t.numeroTelefonoTele = :numeroTelefonoTele"),
    @NamedQuery(name = "TesTelefono.findByNotificacionTetel", query = "SELECT t FROM TesTelefono t WHERE t.notificacionTetel = :notificacionTetel"),
    @NamedQuery(name = "TesTelefono.findByActivoTedir", query = "SELECT t FROM TesTelefono t WHERE t.activoTedir = :activoTedir"),
    @NamedQuery(name = "TesTelefono.findByUsuarioIngre", query = "SELECT t FROM TesTelefono t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesTelefono.findByFechaIngre", query = "SELECT t FROM TesTelefono t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesTelefono.findByHoraIngre", query = "SELECT t FROM TesTelefono t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesTelefono.findByUsuarioActua", query = "SELECT t FROM TesTelefono t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesTelefono.findByFechaActua", query = "SELECT t FROM TesTelefono t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesTelefono.findByHoraActua", query = "SELECT t FROM TesTelefono t WHERE t.horaActua = :horaActua")})
public class TesTelefono implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tetel", nullable = false)
    private Long ideTetel;
    @Size(max = 35)
    @Column(name = "numero_telefono_tele", length = 35)
    private String numeroTelefonoTele;
    @Column(name = "notificacion_tetel")
    private Boolean notificacionTetel;
    @Column(name = "activo_tedir")
    private Boolean activoTedir;
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
    @JoinColumn(name = "ide_tepro", referencedColumnName = "ide_tepro")
    @ManyToOne
    private TesProveedor ideTepro;
    @JoinColumn(name = "ide_reteo", referencedColumnName = "ide_reteo")
    @ManyToOne
    private RecTelefonoOperadora ideReteo;

    public TesTelefono() {
    }

    public TesTelefono(Long ideTetel) {
        this.ideTetel = ideTetel;
    }

    public Long getIdeTetel() {
        return ideTetel;
    }

    public void setIdeTetel(Long ideTetel) {
        this.ideTetel = ideTetel;
    }

    public String getNumeroTelefonoTele() {
        return numeroTelefonoTele;
    }

    public void setNumeroTelefonoTele(String numeroTelefonoTele) {
        this.numeroTelefonoTele = numeroTelefonoTele;
    }

    public Boolean getNotificacionTetel() {
        return notificacionTetel;
    }

    public void setNotificacionTetel(Boolean notificacionTetel) {
        this.notificacionTetel = notificacionTetel;
    }

    public Boolean getActivoTedir() {
        return activoTedir;
    }

    public void setActivoTedir(Boolean activoTedir) {
        this.activoTedir = activoTedir;
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

    public TesProveedor getIdeTepro() {
        return ideTepro;
    }

    public void setIdeTepro(TesProveedor ideTepro) {
        this.ideTepro = ideTepro;
    }

    public RecTelefonoOperadora getIdeReteo() {
        return ideReteo;
    }

    public void setIdeReteo(RecTelefonoOperadora ideReteo) {
        this.ideReteo = ideReteo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTetel != null ? ideTetel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesTelefono)) {
            return false;
        }
        TesTelefono other = (TesTelefono) object;
        if ((this.ideTetel == null && other.ideTetel != null) || (this.ideTetel != null && !this.ideTetel.equals(other.ideTetel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesTelefono[ ideTetel=" + ideTetel + " ]";
    }
    
}
