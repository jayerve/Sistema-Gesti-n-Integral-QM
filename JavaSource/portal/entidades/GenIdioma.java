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
@Table(name = "gen_idioma", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenIdioma.findAll", query = "SELECT g FROM GenIdioma g"),
    @NamedQuery(name = "GenIdioma.findByIdeGeidi", query = "SELECT g FROM GenIdioma g WHERE g.ideGeidi = :ideGeidi"),
    @NamedQuery(name = "GenIdioma.findByDetalleGeidi", query = "SELECT g FROM GenIdioma g WHERE g.detalleGeidi = :detalleGeidi"),
    @NamedQuery(name = "GenIdioma.findByActivoGeidi", query = "SELECT g FROM GenIdioma g WHERE g.activoGeidi = :activoGeidi"),
    @NamedQuery(name = "GenIdioma.findByUsuarioIngre", query = "SELECT g FROM GenIdioma g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenIdioma.findByFechaIngre", query = "SELECT g FROM GenIdioma g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenIdioma.findByUsuarioActua", query = "SELECT g FROM GenIdioma g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenIdioma.findByFechaActua", query = "SELECT g FROM GenIdioma g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenIdioma.findByHoraIngre", query = "SELECT g FROM GenIdioma g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenIdioma.findByHoraActua", query = "SELECT g FROM GenIdioma g WHERE g.horaActua = :horaActua")})
public class GenIdioma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geidi", nullable = false)
    private Integer ideGeidi;
    @Size(max = 50)
    @Column(name = "detalle_geidi", length = 50)
    private String detalleGeidi;
    @Column(name = "activo_geidi")
    private Boolean activoGeidi;
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
    @OneToMany(mappedBy = "ideGeidi")
    private List<GthIdiomaEmpleado> gthIdiomaEmpleadoList;
    @OneToMany(mappedBy = "ideGeidi")
    private List<SprIdioma> sprIdiomaList;

    public GenIdioma() {
    }

    public GenIdioma(Integer ideGeidi) {
        this.ideGeidi = ideGeidi;
    }

    public Integer getIdeGeidi() {
        return ideGeidi;
    }

    public void setIdeGeidi(Integer ideGeidi) {
        this.ideGeidi = ideGeidi;
    }

    public String getDetalleGeidi() {
        return detalleGeidi;
    }

    public void setDetalleGeidi(String detalleGeidi) {
        this.detalleGeidi = detalleGeidi;
    }

    public Boolean getActivoGeidi() {
        return activoGeidi;
    }

    public void setActivoGeidi(Boolean activoGeidi) {
        this.activoGeidi = activoGeidi;
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

    public List<GthIdiomaEmpleado> getGthIdiomaEmpleadoList() {
        return gthIdiomaEmpleadoList;
    }

    public void setGthIdiomaEmpleadoList(List<GthIdiomaEmpleado> gthIdiomaEmpleadoList) {
        this.gthIdiomaEmpleadoList = gthIdiomaEmpleadoList;
    }

    public List<SprIdioma> getSprIdiomaList() {
        return sprIdiomaList;
    }

    public void setSprIdiomaList(List<SprIdioma> sprIdiomaList) {
        this.sprIdiomaList = sprIdiomaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeidi != null ? ideGeidi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenIdioma)) {
            return false;
        }
        GenIdioma other = (GenIdioma) object;
        if ((this.ideGeidi == null && other.ideGeidi != null) || (this.ideGeidi != null && !this.ideGeidi.equals(other.ideGeidi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenIdioma[ ideGeidi=" + ideGeidi + " ]";
    }
    
}
