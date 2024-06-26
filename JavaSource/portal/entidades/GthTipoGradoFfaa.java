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
@Table(name = "gth_tipo_grado_ffaa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoGradoFfaa.findAll", query = "SELECT g FROM GthTipoGradoFfaa g"),
    @NamedQuery(name = "GthTipoGradoFfaa.findByIdeGttgf", query = "SELECT g FROM GthTipoGradoFfaa g WHERE g.ideGttgf = :ideGttgf"),
    @NamedQuery(name = "GthTipoGradoFfaa.findByDetalleGttgf", query = "SELECT g FROM GthTipoGradoFfaa g WHERE g.detalleGttgf = :detalleGttgf"),
    @NamedQuery(name = "GthTipoGradoFfaa.findByActivoGttgf", query = "SELECT g FROM GthTipoGradoFfaa g WHERE g.activoGttgf = :activoGttgf"),
    @NamedQuery(name = "GthTipoGradoFfaa.findByUsuarioIngre", query = "SELECT g FROM GthTipoGradoFfaa g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoGradoFfaa.findByFechaIngre", query = "SELECT g FROM GthTipoGradoFfaa g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoGradoFfaa.findByUsuarioActua", query = "SELECT g FROM GthTipoGradoFfaa g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoGradoFfaa.findByFechaActua", query = "SELECT g FROM GthTipoGradoFfaa g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoGradoFfaa.findByHoraIngre", query = "SELECT g FROM GthTipoGradoFfaa g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoGradoFfaa.findByHoraActua", query = "SELECT g FROM GthTipoGradoFfaa g WHERE g.horaActua = :horaActua")})
public class GthTipoGradoFfaa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttgf", nullable = false)
    private Integer ideGttgf;
    @Size(max = 50)
    @Column(name = "detalle_gttgf", length = 50)
    private String detalleGttgf;
    @Column(name = "activo_gttgf")
    private Boolean activoGttgf;
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
    @OneToMany(mappedBy = "ideGttgf")
    private List<GthRegistroMilitar> gthRegistroMilitarList;

    public GthTipoGradoFfaa() {
    }

    public GthTipoGradoFfaa(Integer ideGttgf) {
        this.ideGttgf = ideGttgf;
    }

    public Integer getIdeGttgf() {
        return ideGttgf;
    }

    public void setIdeGttgf(Integer ideGttgf) {
        this.ideGttgf = ideGttgf;
    }

    public String getDetalleGttgf() {
        return detalleGttgf;
    }

    public void setDetalleGttgf(String detalleGttgf) {
        this.detalleGttgf = detalleGttgf;
    }

    public Boolean getActivoGttgf() {
        return activoGttgf;
    }

    public void setActivoGttgf(Boolean activoGttgf) {
        this.activoGttgf = activoGttgf;
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

    public List<GthRegistroMilitar> getGthRegistroMilitarList() {
        return gthRegistroMilitarList;
    }

    public void setGthRegistroMilitarList(List<GthRegistroMilitar> gthRegistroMilitarList) {
        this.gthRegistroMilitarList = gthRegistroMilitarList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttgf != null ? ideGttgf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoGradoFfaa)) {
            return false;
        }
        GthTipoGradoFfaa other = (GthTipoGradoFfaa) object;
        if ((this.ideGttgf == null && other.ideGttgf != null) || (this.ideGttgf != null && !this.ideGttgf.equals(other.ideGttgf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoGradoFfaa[ ideGttgf=" + ideGttgf + " ]";
    }
    
}
