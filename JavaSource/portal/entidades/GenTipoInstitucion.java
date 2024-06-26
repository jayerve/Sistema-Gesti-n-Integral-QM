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
@Table(name = "gen_tipo_institucion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenTipoInstitucion.findAll", query = "SELECT g FROM GenTipoInstitucion g"),
    @NamedQuery(name = "GenTipoInstitucion.findByIdeGetii", query = "SELECT g FROM GenTipoInstitucion g WHERE g.ideGetii = :ideGetii"),
    @NamedQuery(name = "GenTipoInstitucion.findByViaticoGetii", query = "SELECT g FROM GenTipoInstitucion g WHERE g.viaticoGetii = :viaticoGetii"),
    @NamedQuery(name = "GenTipoInstitucion.findByDetalleGetii", query = "SELECT g FROM GenTipoInstitucion g WHERE g.detalleGetii = :detalleGetii"),
    @NamedQuery(name = "GenTipoInstitucion.findByActivoGetii", query = "SELECT g FROM GenTipoInstitucion g WHERE g.activoGetii = :activoGetii"),
    @NamedQuery(name = "GenTipoInstitucion.findByUsuarioIngre", query = "SELECT g FROM GenTipoInstitucion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoInstitucion.findByFechaIngre", query = "SELECT g FROM GenTipoInstitucion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoInstitucion.findByUsuarioActua", query = "SELECT g FROM GenTipoInstitucion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoInstitucion.findByFechaActua", query = "SELECT g FROM GenTipoInstitucion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoInstitucion.findByHoraIngre", query = "SELECT g FROM GenTipoInstitucion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoInstitucion.findByHoraActua", query = "SELECT g FROM GenTipoInstitucion g WHERE g.horaActua = :horaActua")})
public class GenTipoInstitucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_getii", nullable = false)
    private Integer ideGetii;
    @Column(name = "viatico_getii")
    private Integer viaticoGetii;
    @Size(max = 50)
    @Column(name = "detalle_getii", length = 50)
    private String detalleGetii;
    @Column(name = "activo_getii")
    private Boolean activoGetii;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGetii")
    private List<GenInstitucion> genInstitucionList;

    public GenTipoInstitucion() {
    }

    public GenTipoInstitucion(Integer ideGetii) {
        this.ideGetii = ideGetii;
    }

    public Integer getIdeGetii() {
        return ideGetii;
    }

    public void setIdeGetii(Integer ideGetii) {
        this.ideGetii = ideGetii;
    }

    public Integer getViaticoGetii() {
        return viaticoGetii;
    }

    public void setViaticoGetii(Integer viaticoGetii) {
        this.viaticoGetii = viaticoGetii;
    }

    public String getDetalleGetii() {
        return detalleGetii;
    }

    public void setDetalleGetii(String detalleGetii) {
        this.detalleGetii = detalleGetii;
    }

    public Boolean getActivoGetii() {
        return activoGetii;
    }

    public void setActivoGetii(Boolean activoGetii) {
        this.activoGetii = activoGetii;
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

    public List<GenInstitucion> getGenInstitucionList() {
        return genInstitucionList;
    }

    public void setGenInstitucionList(List<GenInstitucion> genInstitucionList) {
        this.genInstitucionList = genInstitucionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetii != null ? ideGetii.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoInstitucion)) {
            return false;
        }
        GenTipoInstitucion other = (GenTipoInstitucion) object;
        if ((this.ideGetii == null && other.ideGetii != null) || (this.ideGetii != null && !this.ideGetii.equals(other.ideGetii))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoInstitucion[ ideGetii=" + ideGetii + " ]";
    }
    
}
