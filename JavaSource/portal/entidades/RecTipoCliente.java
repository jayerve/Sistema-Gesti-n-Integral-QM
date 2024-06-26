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
@Table(name = "rec_tipo_cliente", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecTipoCliente.findAll", query = "SELECT r FROM RecTipoCliente r"),
    @NamedQuery(name = "RecTipoCliente.findByIdeRetil", query = "SELECT r FROM RecTipoCliente r WHERE r.ideRetil = :ideRetil"),
    @NamedQuery(name = "RecTipoCliente.findByDetalleRetil", query = "SELECT r FROM RecTipoCliente r WHERE r.detalleRetil = :detalleRetil"),
    @NamedQuery(name = "RecTipoCliente.findByAbreviaturaRetil", query = "SELECT r FROM RecTipoCliente r WHERE r.abreviaturaRetil = :abreviaturaRetil"),
    @NamedQuery(name = "RecTipoCliente.findByActivoRetil", query = "SELECT r FROM RecTipoCliente r WHERE r.activoRetil = :activoRetil"),
    @NamedQuery(name = "RecTipoCliente.findByUsuarioIngre", query = "SELECT r FROM RecTipoCliente r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecTipoCliente.findByFechaIngre", query = "SELECT r FROM RecTipoCliente r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecTipoCliente.findByHoraIngre", query = "SELECT r FROM RecTipoCliente r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecTipoCliente.findByUsuarioActua", query = "SELECT r FROM RecTipoCliente r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecTipoCliente.findByFechaActua", query = "SELECT r FROM RecTipoCliente r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecTipoCliente.findByHoraActua", query = "SELECT r FROM RecTipoCliente r WHERE r.horaActua = :horaActua")})
public class RecTipoCliente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_retil", nullable = false)
    private Long ideRetil;
    @Size(max = 20)
    @Column(name = "detalle_retil", length = 20)
    private String detalleRetil;
    @Size(max = 20)
    @Column(name = "abreviatura_retil", length = 20)
    private String abreviaturaRetil;
    @Column(name = "activo_retil")
    private Boolean activoRetil;
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
    @OneToMany(mappedBy = "ideRetil")
    private List<RecClientes> recClientesList;

    public RecTipoCliente() {
    }

    public RecTipoCliente(Long ideRetil) {
        this.ideRetil = ideRetil;
    }

    public Long getIdeRetil() {
        return ideRetil;
    }

    public void setIdeRetil(Long ideRetil) {
        this.ideRetil = ideRetil;
    }

    public String getDetalleRetil() {
        return detalleRetil;
    }

    public void setDetalleRetil(String detalleRetil) {
        this.detalleRetil = detalleRetil;
    }

    public String getAbreviaturaRetil() {
        return abreviaturaRetil;
    }

    public void setAbreviaturaRetil(String abreviaturaRetil) {
        this.abreviaturaRetil = abreviaturaRetil;
    }

    public Boolean getActivoRetil() {
        return activoRetil;
    }

    public void setActivoRetil(Boolean activoRetil) {
        this.activoRetil = activoRetil;
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
        hash += (ideRetil != null ? ideRetil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecTipoCliente)) {
            return false;
        }
        RecTipoCliente other = (RecTipoCliente) object;
        if ((this.ideRetil == null && other.ideRetil != null) || (this.ideRetil != null && !this.ideRetil.equals(other.ideRetil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecTipoCliente[ ideRetil=" + ideRetil + " ]";
    }
    
}
