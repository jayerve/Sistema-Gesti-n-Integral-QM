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
@Table(name = "rec_tipo_contribuyente", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecTipoContribuyente.findAll", query = "SELECT r FROM RecTipoContribuyente r"),
    @NamedQuery(name = "RecTipoContribuyente.findByIdeRetic", query = "SELECT r FROM RecTipoContribuyente r WHERE r.ideRetic = :ideRetic"),
    @NamedQuery(name = "RecTipoContribuyente.findByDetalleRetic", query = "SELECT r FROM RecTipoContribuyente r WHERE r.detalleRetic = :detalleRetic"),
    @NamedQuery(name = "RecTipoContribuyente.findByActivoRetic", query = "SELECT r FROM RecTipoContribuyente r WHERE r.activoRetic = :activoRetic"),
    @NamedQuery(name = "RecTipoContribuyente.findByUsuarioIngre", query = "SELECT r FROM RecTipoContribuyente r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecTipoContribuyente.findByFechaIngre", query = "SELECT r FROM RecTipoContribuyente r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecTipoContribuyente.findByHoraIngre", query = "SELECT r FROM RecTipoContribuyente r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecTipoContribuyente.findByUsuarioActua", query = "SELECT r FROM RecTipoContribuyente r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecTipoContribuyente.findByFechaActua", query = "SELECT r FROM RecTipoContribuyente r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecTipoContribuyente.findByHoraActua", query = "SELECT r FROM RecTipoContribuyente r WHERE r.horaActua = :horaActua")})
public class RecTipoContribuyente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_retic", nullable = false)
    private Long ideRetic;
    @Size(max = 50)
    @Column(name = "detalle_retic", length = 50)
    private String detalleRetic;
    @Column(name = "activo_retic")
    private Boolean activoRetic;
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
    @OneToMany(mappedBy = "ideRetic")
    private List<TesProveedor> tesProveedorList;
    @OneToMany(mappedBy = "ideRetic")
    private List<RecClientes> recClientesList;

    public RecTipoContribuyente() {
    }

    public RecTipoContribuyente(Long ideRetic) {
        this.ideRetic = ideRetic;
    }

    public Long getIdeRetic() {
        return ideRetic;
    }

    public void setIdeRetic(Long ideRetic) {
        this.ideRetic = ideRetic;
    }

    public String getDetalleRetic() {
        return detalleRetic;
    }

    public void setDetalleRetic(String detalleRetic) {
        this.detalleRetic = detalleRetic;
    }

    public Boolean getActivoRetic() {
        return activoRetic;
    }

    public void setActivoRetic(Boolean activoRetic) {
        this.activoRetic = activoRetic;
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

    public List<TesProveedor> getTesProveedorList() {
        return tesProveedorList;
    }

    public void setTesProveedorList(List<TesProveedor> tesProveedorList) {
        this.tesProveedorList = tesProveedorList;
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
        hash += (ideRetic != null ? ideRetic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecTipoContribuyente)) {
            return false;
        }
        RecTipoContribuyente other = (RecTipoContribuyente) object;
        if ((this.ideRetic == null && other.ideRetic != null) || (this.ideRetic != null && !this.ideRetic.equals(other.ideRetic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecTipoContribuyente[ ideRetic=" + ideRetic + " ]";
    }
    
}
