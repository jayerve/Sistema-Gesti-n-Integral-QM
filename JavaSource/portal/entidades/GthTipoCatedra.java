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
@Table(name = "gth_tipo_catedra", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoCatedra.findAll", query = "SELECT g FROM GthTipoCatedra g"),
    @NamedQuery(name = "GthTipoCatedra.findByIdeGttca", query = "SELECT g FROM GthTipoCatedra g WHERE g.ideGttca = :ideGttca"),
    @NamedQuery(name = "GthTipoCatedra.findByDetalleGttca", query = "SELECT g FROM GthTipoCatedra g WHERE g.detalleGttca = :detalleGttca"),
    @NamedQuery(name = "GthTipoCatedra.findByActivoGttca", query = "SELECT g FROM GthTipoCatedra g WHERE g.activoGttca = :activoGttca"),
    @NamedQuery(name = "GthTipoCatedra.findByUsuarioIngre", query = "SELECT g FROM GthTipoCatedra g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoCatedra.findByFechaIngre", query = "SELECT g FROM GthTipoCatedra g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoCatedra.findByUsuarioActua", query = "SELECT g FROM GthTipoCatedra g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoCatedra.findByFechaActua", query = "SELECT g FROM GthTipoCatedra g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoCatedra.findByHoraIngre", query = "SELECT g FROM GthTipoCatedra g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoCatedra.findByHoraActua", query = "SELECT g FROM GthTipoCatedra g WHERE g.horaActua = :horaActua")})
public class GthTipoCatedra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttca", nullable = false)
    private Integer ideGttca;
    @Size(max = 50)
    @Column(name = "detalle_gttca", length = 50)
    private String detalleGttca;
    @Column(name = "activo_gttca")
    private Boolean activoGttca;
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
    @OneToMany(mappedBy = "ideGttca")
    private List<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaList;

    public GthTipoCatedra() {
    }

    public GthTipoCatedra(Integer ideGttca) {
        this.ideGttca = ideGttca;
    }

    public Integer getIdeGttca() {
        return ideGttca;
    }

    public void setIdeGttca(Integer ideGttca) {
        this.ideGttca = ideGttca;
    }

    public String getDetalleGttca() {
        return detalleGttca;
    }

    public void setDetalleGttca(String detalleGttca) {
        this.detalleGttca = detalleGttca;
    }

    public Boolean getActivoGttca() {
        return activoGttca;
    }

    public void setActivoGttca(Boolean activoGttca) {
        this.activoGttca = activoGttca;
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

    public List<GthExperienciaDocenteEmplea> getGthExperienciaDocenteEmpleaList() {
        return gthExperienciaDocenteEmpleaList;
    }

    public void setGthExperienciaDocenteEmpleaList(List<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaList) {
        this.gthExperienciaDocenteEmpleaList = gthExperienciaDocenteEmpleaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttca != null ? ideGttca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoCatedra)) {
            return false;
        }
        GthTipoCatedra other = (GthTipoCatedra) object;
        if ((this.ideGttca == null && other.ideGttca != null) || (this.ideGttca != null && !this.ideGttca.equals(other.ideGttca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoCatedra[ ideGttca=" + ideGttca + " ]";
    }
    
}
