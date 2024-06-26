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
@Table(name = "gen_estados", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenEstados.findAll", query = "SELECT g FROM GenEstados g"),
    @NamedQuery(name = "GenEstados.findByIdeGeest", query = "SELECT g FROM GenEstados g WHERE g.ideGeest = :ideGeest"),
    @NamedQuery(name = "GenEstados.findByDetalleGeest", query = "SELECT g FROM GenEstados g WHERE g.detalleGeest = :detalleGeest"),
    @NamedQuery(name = "GenEstados.findByActivoGeest", query = "SELECT g FROM GenEstados g WHERE g.activoGeest = :activoGeest"),
    @NamedQuery(name = "GenEstados.findByUsuarioIngre", query = "SELECT g FROM GenEstados g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenEstados.findByFechaIngre", query = "SELECT g FROM GenEstados g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenEstados.findByUsuarioActua", query = "SELECT g FROM GenEstados g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenEstados.findByFechaActua", query = "SELECT g FROM GenEstados g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenEstados.findByHoraIngre", query = "SELECT g FROM GenEstados g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenEstados.findByHoraActua", query = "SELECT g FROM GenEstados g WHERE g.horaActua = :horaActua")})
public class GenEstados implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geest", nullable = false)
    private Integer ideGeest;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "detalle_geest", nullable = false, length = 50)
    private String detalleGeest;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_geest", nullable = false)
    private boolean activoGeest;
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
    @OneToMany(mappedBy = "ideGeest")
    private List<AsiPermisosVacacionHext> asiPermisosVacacionHextList;

    public GenEstados() {
    }

    public GenEstados(Integer ideGeest) {
        this.ideGeest = ideGeest;
    }

    public GenEstados(Integer ideGeest, String detalleGeest, boolean activoGeest) {
        this.ideGeest = ideGeest;
        this.detalleGeest = detalleGeest;
        this.activoGeest = activoGeest;
    }

    public Integer getIdeGeest() {
        return ideGeest;
    }

    public void setIdeGeest(Integer ideGeest) {
        this.ideGeest = ideGeest;
    }

    public String getDetalleGeest() {
        return detalleGeest;
    }

    public void setDetalleGeest(String detalleGeest) {
        this.detalleGeest = detalleGeest;
    }

    public boolean getActivoGeest() {
        return activoGeest;
    }

    public void setActivoGeest(boolean activoGeest) {
        this.activoGeest = activoGeest;
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

    public List<AsiPermisosVacacionHext> getAsiPermisosVacacionHextList() {
        return asiPermisosVacacionHextList;
    }

    public void setAsiPermisosVacacionHextList(List<AsiPermisosVacacionHext> asiPermisosVacacionHextList) {
        this.asiPermisosVacacionHextList = asiPermisosVacacionHextList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeest != null ? ideGeest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenEstados)) {
            return false;
        }
        GenEstados other = (GenEstados) object;
        if ((this.ideGeest == null && other.ideGeest != null) || (this.ideGeest != null && !this.ideGeest.equals(other.ideGeest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenEstados[ ideGeest=" + ideGeest + " ]";
    }
    
}
