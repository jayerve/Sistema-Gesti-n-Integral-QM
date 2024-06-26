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
@Table(name = "gth_tipo_archivo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoArchivo.findAll", query = "SELECT g FROM GthTipoArchivo g"),
    @NamedQuery(name = "GthTipoArchivo.findByIdeGttar", query = "SELECT g FROM GthTipoArchivo g WHERE g.ideGttar = :ideGttar"),
    @NamedQuery(name = "GthTipoArchivo.findByDetalleGttar", query = "SELECT g FROM GthTipoArchivo g WHERE g.detalleGttar = :detalleGttar"),
    @NamedQuery(name = "GthTipoArchivo.findByActivoGttar", query = "SELECT g FROM GthTipoArchivo g WHERE g.activoGttar = :activoGttar"),
    @NamedQuery(name = "GthTipoArchivo.findByUsuarioIngre", query = "SELECT g FROM GthTipoArchivo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoArchivo.findByFechaIngre", query = "SELECT g FROM GthTipoArchivo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoArchivo.findByUsuarioActua", query = "SELECT g FROM GthTipoArchivo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoArchivo.findByFechaActua", query = "SELECT g FROM GthTipoArchivo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoArchivo.findByHoraIngre", query = "SELECT g FROM GthTipoArchivo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoArchivo.findByHoraActua", query = "SELECT g FROM GthTipoArchivo g WHERE g.horaActua = :horaActua")})
public class GthTipoArchivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttar", nullable = false)
    private Integer ideGttar;
    @Size(max = 50)
    @Column(name = "detalle_gttar", length = 50)
    private String detalleGttar;
    @Column(name = "activo_gttar")
    private Boolean activoGttar;
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
    @OneToMany(mappedBy = "ideGttar")
    private List<GthArchivoEmpleado> gthArchivoEmpleadoList;

    public GthTipoArchivo() {
    }

    public GthTipoArchivo(Integer ideGttar) {
        this.ideGttar = ideGttar;
    }

    public Integer getIdeGttar() {
        return ideGttar;
    }

    public void setIdeGttar(Integer ideGttar) {
        this.ideGttar = ideGttar;
    }

    public String getDetalleGttar() {
        return detalleGttar;
    }

    public void setDetalleGttar(String detalleGttar) {
        this.detalleGttar = detalleGttar;
    }

    public Boolean getActivoGttar() {
        return activoGttar;
    }

    public void setActivoGttar(Boolean activoGttar) {
        this.activoGttar = activoGttar;
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

    public List<GthArchivoEmpleado> getGthArchivoEmpleadoList() {
        return gthArchivoEmpleadoList;
    }

    public void setGthArchivoEmpleadoList(List<GthArchivoEmpleado> gthArchivoEmpleadoList) {
        this.gthArchivoEmpleadoList = gthArchivoEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttar != null ? ideGttar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoArchivo)) {
            return false;
        }
        GthTipoArchivo other = (GthTipoArchivo) object;
        if ((this.ideGttar == null && other.ideGttar != null) || (this.ideGttar != null && !this.ideGttar.equals(other.ideGttar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoArchivo[ ideGttar=" + ideGttar + " ]";
    }
    
}
