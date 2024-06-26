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
@Table(name = "rec_telefono_operadora", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecTelefonoOperadora.findAll", query = "SELECT r FROM RecTelefonoOperadora r"),
    @NamedQuery(name = "RecTelefonoOperadora.findByIdeReteo", query = "SELECT r FROM RecTelefonoOperadora r WHERE r.ideReteo = :ideReteo"),
    @NamedQuery(name = "RecTelefonoOperadora.findByDetalleReteo", query = "SELECT r FROM RecTelefonoOperadora r WHERE r.detalleReteo = :detalleReteo"),
    @NamedQuery(name = "RecTelefonoOperadora.findByActivoReteo", query = "SELECT r FROM RecTelefonoOperadora r WHERE r.activoReteo = :activoReteo"),
    @NamedQuery(name = "RecTelefonoOperadora.findByUsuarioIngre", query = "SELECT r FROM RecTelefonoOperadora r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecTelefonoOperadora.findByFechaIngre", query = "SELECT r FROM RecTelefonoOperadora r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecTelefonoOperadora.findByHoraIngre", query = "SELECT r FROM RecTelefonoOperadora r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecTelefonoOperadora.findByUsuarioActua", query = "SELECT r FROM RecTelefonoOperadora r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecTelefonoOperadora.findByFechaActua", query = "SELECT r FROM RecTelefonoOperadora r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecTelefonoOperadora.findByHoraActua", query = "SELECT r FROM RecTelefonoOperadora r WHERE r.horaActua = :horaActua")})
public class RecTelefonoOperadora implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_reteo", nullable = false)
    private Long ideReteo;
    @Size(max = 50)
    @Column(name = "detalle_reteo", length = 50)
    private String detalleReteo;
    @Column(name = "activo_reteo")
    private Boolean activoReteo;
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
    @OneToMany(mappedBy = "ideReteo")
    private List<RecClienteTelefono> recClienteTelefonoList;
    @OneToMany(mappedBy = "ideReteo")
    private List<TesTelefono> tesTelefonoList;

    public RecTelefonoOperadora() {
    }

    public RecTelefonoOperadora(Long ideReteo) {
        this.ideReteo = ideReteo;
    }

    public Long getIdeReteo() {
        return ideReteo;
    }

    public void setIdeReteo(Long ideReteo) {
        this.ideReteo = ideReteo;
    }

    public String getDetalleReteo() {
        return detalleReteo;
    }

    public void setDetalleReteo(String detalleReteo) {
        this.detalleReteo = detalleReteo;
    }

    public Boolean getActivoReteo() {
        return activoReteo;
    }

    public void setActivoReteo(Boolean activoReteo) {
        this.activoReteo = activoReteo;
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

    public List<RecClienteTelefono> getRecClienteTelefonoList() {
        return recClienteTelefonoList;
    }

    public void setRecClienteTelefonoList(List<RecClienteTelefono> recClienteTelefonoList) {
        this.recClienteTelefonoList = recClienteTelefonoList;
    }

    public List<TesTelefono> getTesTelefonoList() {
        return tesTelefonoList;
    }

    public void setTesTelefonoList(List<TesTelefono> tesTelefonoList) {
        this.tesTelefonoList = tesTelefonoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideReteo != null ? ideReteo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecTelefonoOperadora)) {
            return false;
        }
        RecTelefonoOperadora other = (RecTelefonoOperadora) object;
        if ((this.ideReteo == null && other.ideReteo != null) || (this.ideReteo != null && !this.ideReteo.equals(other.ideReteo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecTelefonoOperadora[ ideReteo=" + ideReteo + " ]";
    }
    
}
