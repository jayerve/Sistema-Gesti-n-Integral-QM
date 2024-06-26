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
@Table(name = "gth_tipo_hobie", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoHobie.findAll", query = "SELECT g FROM GthTipoHobie g"),
    @NamedQuery(name = "GthTipoHobie.findByIdeGttih", query = "SELECT g FROM GthTipoHobie g WHERE g.ideGttih = :ideGttih"),
    @NamedQuery(name = "GthTipoHobie.findByDetalleGttih", query = "SELECT g FROM GthTipoHobie g WHERE g.detalleGttih = :detalleGttih"),
    @NamedQuery(name = "GthTipoHobie.findByActivoGttih", query = "SELECT g FROM GthTipoHobie g WHERE g.activoGttih = :activoGttih"),
    @NamedQuery(name = "GthTipoHobie.findByUsuarioIngre", query = "SELECT g FROM GthTipoHobie g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoHobie.findByFechaIngre", query = "SELECT g FROM GthTipoHobie g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoHobie.findByUsuarioActua", query = "SELECT g FROM GthTipoHobie g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoHobie.findByFechaActua", query = "SELECT g FROM GthTipoHobie g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoHobie.findByHoraIngre", query = "SELECT g FROM GthTipoHobie g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoHobie.findByHoraActua", query = "SELECT g FROM GthTipoHobie g WHERE g.horaActua = :horaActua")})
public class GthTipoHobie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttih", nullable = false)
    private Integer ideGttih;
    @Size(max = 50)
    @Column(name = "detalle_gttih", length = 50)
    private String detalleGttih;
    @Column(name = "activo_gttih")
    private Boolean activoGttih;
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
    @OneToMany(mappedBy = "ideGttih")
    private List<GthHobie> gthHobieList;

    public GthTipoHobie() {
    }

    public GthTipoHobie(Integer ideGttih) {
        this.ideGttih = ideGttih;
    }

    public Integer getIdeGttih() {
        return ideGttih;
    }

    public void setIdeGttih(Integer ideGttih) {
        this.ideGttih = ideGttih;
    }

    public String getDetalleGttih() {
        return detalleGttih;
    }

    public void setDetalleGttih(String detalleGttih) {
        this.detalleGttih = detalleGttih;
    }

    public Boolean getActivoGttih() {
        return activoGttih;
    }

    public void setActivoGttih(Boolean activoGttih) {
        this.activoGttih = activoGttih;
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

    public List<GthHobie> getGthHobieList() {
        return gthHobieList;
    }

    public void setGthHobieList(List<GthHobie> gthHobieList) {
        this.gthHobieList = gthHobieList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttih != null ? ideGttih.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoHobie)) {
            return false;
        }
        GthTipoHobie other = (GthTipoHobie) object;
        if ((this.ideGttih == null && other.ideGttih != null) || (this.ideGttih != null && !this.ideGttih.equals(other.ideGttih))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoHobie[ ideGttih=" + ideGttih + " ]";
    }
    
}
