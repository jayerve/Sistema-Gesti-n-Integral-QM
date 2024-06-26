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
@Table(name = "rec_tipo_asistencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecTipoAsistencia.findAll", query = "SELECT r FROM RecTipoAsistencia r"),
    @NamedQuery(name = "RecTipoAsistencia.findByIdeRetia", query = "SELECT r FROM RecTipoAsistencia r WHERE r.ideRetia = :ideRetia"),
    @NamedQuery(name = "RecTipoAsistencia.findByDetalleRetia", query = "SELECT r FROM RecTipoAsistencia r WHERE r.detalleRetia = :detalleRetia"),
    @NamedQuery(name = "RecTipoAsistencia.findByActivoRetia", query = "SELECT r FROM RecTipoAsistencia r WHERE r.activoRetia = :activoRetia"),
    @NamedQuery(name = "RecTipoAsistencia.findByUsuarioIngre", query = "SELECT r FROM RecTipoAsistencia r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecTipoAsistencia.findByFechaIngre", query = "SELECT r FROM RecTipoAsistencia r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecTipoAsistencia.findByHoraIngre", query = "SELECT r FROM RecTipoAsistencia r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecTipoAsistencia.findByUsuarioActua", query = "SELECT r FROM RecTipoAsistencia r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecTipoAsistencia.findByFechaActua", query = "SELECT r FROM RecTipoAsistencia r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecTipoAsistencia.findByHoraActua", query = "SELECT r FROM RecTipoAsistencia r WHERE r.horaActua = :horaActua")})
public class RecTipoAsistencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_retia", nullable = false)
    private Long ideRetia;
    @Size(max = 50)
    @Column(name = "detalle_retia", length = 50)
    private String detalleRetia;
    @Column(name = "activo_retia")
    private Boolean activoRetia;
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
    @OneToMany(mappedBy = "ideRetia")
    private List<RecClientes> recClientesList;

    public RecTipoAsistencia() {
    }

    public RecTipoAsistencia(Long ideRetia) {
        this.ideRetia = ideRetia;
    }

    public Long getIdeRetia() {
        return ideRetia;
    }

    public void setIdeRetia(Long ideRetia) {
        this.ideRetia = ideRetia;
    }

    public String getDetalleRetia() {
        return detalleRetia;
    }

    public void setDetalleRetia(String detalleRetia) {
        this.detalleRetia = detalleRetia;
    }

    public Boolean getActivoRetia() {
        return activoRetia;
    }

    public void setActivoRetia(Boolean activoRetia) {
        this.activoRetia = activoRetia;
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

    public List<RecClientes> getRecClientesList() {
        return recClientesList;
    }

    public void setRecClientesList(List<RecClientes> recClientesList) {
        this.recClientesList = recClientesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRetia != null ? ideRetia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecTipoAsistencia)) {
            return false;
        }
        RecTipoAsistencia other = (RecTipoAsistencia) object;
        if ((this.ideRetia == null && other.ideRetia != null) || (this.ideRetia != null && !this.ideRetia.equals(other.ideRetia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecTipoAsistencia[ ideRetia=" + ideRetia + " ]";
    }
    
}
