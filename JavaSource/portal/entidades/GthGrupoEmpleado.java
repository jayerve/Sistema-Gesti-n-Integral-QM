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
@Table(name = "gth_grupo_empleado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthGrupoEmpleado.findAll", query = "SELECT g FROM GthGrupoEmpleado g"),
    @NamedQuery(name = "GthGrupoEmpleado.findByIdeGtgre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.ideGtgre = :ideGtgre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByDetalleGtgre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.detalleGtgre = :detalleGtgre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByMinutoAlmuerzoGtgre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.minutoAlmuerzoGtgre = :minutoAlmuerzoGtgre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByActivoGtgre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.activoGtgre = :activoGtgre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByFechaIngre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByUsuarioActua", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthGrupoEmpleado.findByFechaActua", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthGrupoEmpleado.findByHoraIngre", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthGrupoEmpleado.findByHoraActua", query = "SELECT g FROM GthGrupoEmpleado g WHERE g.horaActua = :horaActua")})
public class GthGrupoEmpleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtgre", nullable = false)
    private Integer ideGtgre;
    @Size(max = 50)
    @Column(name = "detalle_gtgre", length = 50)
    private String detalleGtgre;
    @Column(name = "minuto_almuerzo_gtgre")
    private Integer minutoAlmuerzoGtgre;
    @Column(name = "activo_gtgre")
    private Boolean activoGtgre;
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
    @OneToMany(mappedBy = "ideGtgre")
    private List<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParList;
    @OneToMany(mappedBy = "ideGtgre")
    private List<AsiTurnos> asiTurnosList;

    public GthGrupoEmpleado() {
    }

    public GthGrupoEmpleado(Integer ideGtgre) {
        this.ideGtgre = ideGtgre;
    }

    public Integer getIdeGtgre() {
        return ideGtgre;
    }

    public void setIdeGtgre(Integer ideGtgre) {
        this.ideGtgre = ideGtgre;
    }

    public String getDetalleGtgre() {
        return detalleGtgre;
    }

    public void setDetalleGtgre(String detalleGtgre) {
        this.detalleGtgre = detalleGtgre;
    }

    public Integer getMinutoAlmuerzoGtgre() {
        return minutoAlmuerzoGtgre;
    }

    public void setMinutoAlmuerzoGtgre(Integer minutoAlmuerzoGtgre) {
        this.minutoAlmuerzoGtgre = minutoAlmuerzoGtgre;
    }

    public Boolean getActivoGtgre() {
        return activoGtgre;
    }

    public void setActivoGtgre(Boolean activoGtgre) {
        this.activoGtgre = activoGtgre;
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

    public List<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParList() {
        return genEmpleadosDepartamentoParList;
    }

    public void setGenEmpleadosDepartamentoParList(List<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParList) {
        this.genEmpleadosDepartamentoParList = genEmpleadosDepartamentoParList;
    }

    public List<AsiTurnos> getAsiTurnosList() {
        return asiTurnosList;
    }

    public void setAsiTurnosList(List<AsiTurnos> asiTurnosList) {
        this.asiTurnosList = asiTurnosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtgre != null ? ideGtgre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthGrupoEmpleado)) {
            return false;
        }
        GthGrupoEmpleado other = (GthGrupoEmpleado) object;
        if ((this.ideGtgre == null && other.ideGtgre != null) || (this.ideGtgre != null && !this.ideGtgre.equals(other.ideGtgre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthGrupoEmpleado[ ideGtgre=" + ideGtgre + " ]";
    }
    
}
