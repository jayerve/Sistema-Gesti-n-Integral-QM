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
@Table(name = "gen_tipo_periodo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenTipoPeriodo.findAll", query = "SELECT g FROM GenTipoPeriodo g"),
    @NamedQuery(name = "GenTipoPeriodo.findByIdeGetpr", query = "SELECT g FROM GenTipoPeriodo g WHERE g.ideGetpr = :ideGetpr"),
    @NamedQuery(name = "GenTipoPeriodo.findByDetalleGetpr", query = "SELECT g FROM GenTipoPeriodo g WHERE g.detalleGetpr = :detalleGetpr"),
    @NamedQuery(name = "GenTipoPeriodo.findByActivoGetpr", query = "SELECT g FROM GenTipoPeriodo g WHERE g.activoGetpr = :activoGetpr"),
    @NamedQuery(name = "GenTipoPeriodo.findByUsuarioIngre", query = "SELECT g FROM GenTipoPeriodo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoPeriodo.findByFechaIngre", query = "SELECT g FROM GenTipoPeriodo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoPeriodo.findByUsuarioActua", query = "SELECT g FROM GenTipoPeriodo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoPeriodo.findByFechaActua", query = "SELECT g FROM GenTipoPeriodo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoPeriodo.findByHoraIngre", query = "SELECT g FROM GenTipoPeriodo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoPeriodo.findByHoraActua", query = "SELECT g FROM GenTipoPeriodo g WHERE g.horaActua = :horaActua")})
public class GenTipoPeriodo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_getpr", nullable = false)
    private Integer ideGetpr;
    @Size(max = 50)
    @Column(name = "detalle_getpr", length = 50)
    private String detalleGetpr;
    @Column(name = "activo_getpr")
    private Boolean activoGetpr;
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
    @OneToMany(mappedBy = "ideGetpr")
    private List<CppCapacitacion> cppCapacitacionList;
    @OneToMany(mappedBy = "ideGetpr")
    private List<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoList;
    @OneToMany(mappedBy = "ideGetpr")
    private List<GthIdiomaEmpleado> gthIdiomaEmpleadoList;
    @OneToMany(mappedBy = "ideGetpr")
    private List<GthCapacitacionEmpleado> gthCapacitacionEmpleadoList;
    @OneToMany(mappedBy = "ideGetpr")
    private List<AsiMotivo> asiMotivoList;
    @OneToMany(mappedBy = "ideGetpr")
    private List<GthInversionEmpleado> gthInversionEmpleadoList;
    @OneToMany(mappedBy = "ideGetpr")
    private List<SprCapacitacion> sprCapacitacionList;

    public GenTipoPeriodo() {
    }

    public GenTipoPeriodo(Integer ideGetpr) {
        this.ideGetpr = ideGetpr;
    }

    public Integer getIdeGetpr() {
        return ideGetpr;
    }

    public void setIdeGetpr(Integer ideGetpr) {
        this.ideGetpr = ideGetpr;
    }

    public String getDetalleGetpr() {
        return detalleGetpr;
    }

    public void setDetalleGetpr(String detalleGetpr) {
        this.detalleGetpr = detalleGetpr;
    }

    public Boolean getActivoGetpr() {
        return activoGetpr;
    }

    public void setActivoGetpr(Boolean activoGetpr) {
        this.activoGetpr = activoGetpr;
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

    public List<CppCapacitacion> getCppCapacitacionList() {
        return cppCapacitacionList;
    }

    public void setCppCapacitacionList(List<CppCapacitacion> cppCapacitacionList) {
        this.cppCapacitacionList = cppCapacitacionList;
    }

    public List<GthEndeudamientoEmpleado> getGthEndeudamientoEmpleadoList() {
        return gthEndeudamientoEmpleadoList;
    }

    public void setGthEndeudamientoEmpleadoList(List<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoList) {
        this.gthEndeudamientoEmpleadoList = gthEndeudamientoEmpleadoList;
    }

    public List<GthIdiomaEmpleado> getGthIdiomaEmpleadoList() {
        return gthIdiomaEmpleadoList;
    }

    public void setGthIdiomaEmpleadoList(List<GthIdiomaEmpleado> gthIdiomaEmpleadoList) {
        this.gthIdiomaEmpleadoList = gthIdiomaEmpleadoList;
    }

    public List<GthCapacitacionEmpleado> getGthCapacitacionEmpleadoList() {
        return gthCapacitacionEmpleadoList;
    }

    public void setGthCapacitacionEmpleadoList(List<GthCapacitacionEmpleado> gthCapacitacionEmpleadoList) {
        this.gthCapacitacionEmpleadoList = gthCapacitacionEmpleadoList;
    }

    public List<AsiMotivo> getAsiMotivoList() {
        return asiMotivoList;
    }

    public void setAsiMotivoList(List<AsiMotivo> asiMotivoList) {
        this.asiMotivoList = asiMotivoList;
    }

    public List<GthInversionEmpleado> getGthInversionEmpleadoList() {
        return gthInversionEmpleadoList;
    }

    public void setGthInversionEmpleadoList(List<GthInversionEmpleado> gthInversionEmpleadoList) {
        this.gthInversionEmpleadoList = gthInversionEmpleadoList;
    }

    public List<SprCapacitacion> getSprCapacitacionList() {
        return sprCapacitacionList;
    }

    public void setSprCapacitacionList(List<SprCapacitacion> sprCapacitacionList) {
        this.sprCapacitacionList = sprCapacitacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetpr != null ? ideGetpr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoPeriodo)) {
            return false;
        }
        GenTipoPeriodo other = (GenTipoPeriodo) object;
        if ((this.ideGetpr == null && other.ideGetpr != null) || (this.ideGetpr != null && !this.ideGetpr.equals(other.ideGetpr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoPeriodo[ ideGetpr=" + ideGetpr + " ]";
    }
    
}
