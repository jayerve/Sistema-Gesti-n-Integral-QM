/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "gth_estado_civil", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthEstadoCivil.findAll", query = "SELECT g FROM GthEstadoCivil g"),
    @NamedQuery(name = "GthEstadoCivil.findByIdeGtesc", query = "SELECT g FROM GthEstadoCivil g WHERE g.ideGtesc = :ideGtesc"),
    @NamedQuery(name = "GthEstadoCivil.findByDetalleGtesc", query = "SELECT g FROM GthEstadoCivil g WHERE g.detalleGtesc = :detalleGtesc"),
    @NamedQuery(name = "GthEstadoCivil.findByActivoGtesc", query = "SELECT g FROM GthEstadoCivil g WHERE g.activoGtesc = :activoGtesc"),
    @NamedQuery(name = "GthEstadoCivil.findByUsuarioIngre", query = "SELECT g FROM GthEstadoCivil g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthEstadoCivil.findByFechaIngre", query = "SELECT g FROM GthEstadoCivil g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthEstadoCivil.findByUsuarioActua", query = "SELECT g FROM GthEstadoCivil g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthEstadoCivil.findByFechaActua", query = "SELECT g FROM GthEstadoCivil g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthEstadoCivil.findByHoraIngre", query = "SELECT g FROM GthEstadoCivil g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthEstadoCivil.findByHoraActua", query = "SELECT g FROM GthEstadoCivil g WHERE g.horaActua = :horaActua")})
public class GthEstadoCivil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtesc", nullable = false)
    private Integer ideGtesc;
    @Size(max = 50)
    @Column(name = "detalle_gtesc", length = 50)
    private String detalleGtesc;
    @Column(name = "activo_gtesc")
    private Boolean activoGtesc;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGtesc")
    private List<GthEmpleado> gthEmpleadoList;
    @OneToMany(mappedBy = "ideGtesc")
    private List<RecClientes> recClientesList;

    public GthEstadoCivil() {
    }

    public GthEstadoCivil(Integer ideGtesc) {
        this.ideGtesc = ideGtesc;
    }

    public Integer getIdeGtesc() {
        return ideGtesc;
    }

    public void setIdeGtesc(Integer ideGtesc) {
        this.ideGtesc = ideGtesc;
    }

    public String getDetalleGtesc() {
        return detalleGtesc;
    }

    public void setDetalleGtesc(String detalleGtesc) {
        this.detalleGtesc = detalleGtesc;
    }

    public Boolean getActivoGtesc() {
        return activoGtesc;
    }

    public void setActivoGtesc(Boolean activoGtesc) {
        this.activoGtesc = activoGtesc;
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

    public List<GthEmpleado> getGthEmpleadoList() {
        return gthEmpleadoList;
    }

    public void setGthEmpleadoList(List<GthEmpleado> gthEmpleadoList) {
        this.gthEmpleadoList = gthEmpleadoList;
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
        hash += (ideGtesc != null ? ideGtesc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthEstadoCivil)) {
            return false;
        }
        GthEstadoCivil other = (GthEstadoCivil) object;
        if ((this.ideGtesc == null && other.ideGtesc != null) || (this.ideGtesc != null && !this.ideGtesc.equals(other.ideGtesc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthEstadoCivil[ ideGtesc=" + ideGtesc + " ]";
    }
    
}
