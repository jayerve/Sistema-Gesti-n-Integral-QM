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
@Table(name = "gen_dias", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenDias.findAll", query = "SELECT g FROM GenDias g"),
    @NamedQuery(name = "GenDias.findByIdeGedia", query = "SELECT g FROM GenDias g WHERE g.ideGedia = :ideGedia"),
    @NamedQuery(name = "GenDias.findByDetalleGedia", query = "SELECT g FROM GenDias g WHERE g.detalleGedia = :detalleGedia"),
    @NamedQuery(name = "GenDias.findByActivoGedia", query = "SELECT g FROM GenDias g WHERE g.activoGedia = :activoGedia"),
    @NamedQuery(name = "GenDias.findByUsuarioIngre", query = "SELECT g FROM GenDias g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenDias.findByFechaIngre", query = "SELECT g FROM GenDias g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenDias.findByUsuarioActua", query = "SELECT g FROM GenDias g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenDias.findByFechaActua", query = "SELECT g FROM GenDias g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenDias.findByHoraIngre", query = "SELECT g FROM GenDias g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenDias.findByHoraActua", query = "SELECT g FROM GenDias g WHERE g.horaActua = :horaActua")})
public class GenDias implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gedia", nullable = false)
    private Integer ideGedia;
    @Size(max = 50)
    @Column(name = "detalle_gedia", length = 50)
    private String detalleGedia;
    @Column(name = "activo_gedia")
    private Boolean activoGedia;
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
    @OneToMany(mappedBy = "ideGedia")
    private List<AsiDiaHorario> asiDiaHorarioList;

    public GenDias() {
    }

    public GenDias(Integer ideGedia) {
        this.ideGedia = ideGedia;
    }

    public Integer getIdeGedia() {
        return ideGedia;
    }

    public void setIdeGedia(Integer ideGedia) {
        this.ideGedia = ideGedia;
    }

    public String getDetalleGedia() {
        return detalleGedia;
    }

    public void setDetalleGedia(String detalleGedia) {
        this.detalleGedia = detalleGedia;
    }

    public Boolean getActivoGedia() {
        return activoGedia;
    }

    public void setActivoGedia(Boolean activoGedia) {
        this.activoGedia = activoGedia;
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

    public List<AsiDiaHorario> getAsiDiaHorarioList() {
        return asiDiaHorarioList;
    }

    public void setAsiDiaHorarioList(List<AsiDiaHorario> asiDiaHorarioList) {
        this.asiDiaHorarioList = asiDiaHorarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGedia != null ? ideGedia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenDias)) {
            return false;
        }
        GenDias other = (GenDias) object;
        if ((this.ideGedia == null && other.ideGedia != null) || (this.ideGedia != null && !this.ideGedia.equals(other.ideGedia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenDias[ ideGedia=" + ideGedia + " ]";
    }
    
}
