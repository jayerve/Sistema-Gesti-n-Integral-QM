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
@Table(name = "gth_tipo_transporte_viatico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoTransporteViatico.findAll", query = "SELECT g FROM GthTipoTransporteViatico g"),
    @NamedQuery(name = "GthTipoTransporteViatico.findByIdeGtttv", query = "SELECT g FROM GthTipoTransporteViatico g WHERE g.ideGtttv = :ideGtttv"),
    @NamedQuery(name = "GthTipoTransporteViatico.findByDetalleGtttv", query = "SELECT g FROM GthTipoTransporteViatico g WHERE g.detalleGtttv = :detalleGtttv"),
    @NamedQuery(name = "GthTipoTransporteViatico.findByActivoGtttv", query = "SELECT g FROM GthTipoTransporteViatico g WHERE g.activoGtttv = :activoGtttv"),
    @NamedQuery(name = "GthTipoTransporteViatico.findByUsuarioIngre", query = "SELECT g FROM GthTipoTransporteViatico g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoTransporteViatico.findByFechaIngre", query = "SELECT g FROM GthTipoTransporteViatico g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoTransporteViatico.findByUsuarioActua", query = "SELECT g FROM GthTipoTransporteViatico g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoTransporteViatico.findByFechaActua", query = "SELECT g FROM GthTipoTransporteViatico g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoTransporteViatico.findByHoraIngre", query = "SELECT g FROM GthTipoTransporteViatico g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoTransporteViatico.findByHoraActua", query = "SELECT g FROM GthTipoTransporteViatico g WHERE g.horaActua = :horaActua")})
public class GthTipoTransporteViatico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtttv", nullable = false)
    private Integer ideGtttv;
    @Size(max = 1000)
    @Column(name = "detalle_gtttv", length = 1000)
    private String detalleGtttv;
    @Column(name = "activo_gtttv")
    private Boolean activoGtttv;
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
    @OneToMany(mappedBy = "ideGtttv")
    private List<GthTransporteViatico> gthTransporteViaticoList;

    public GthTipoTransporteViatico() {
    }

    public GthTipoTransporteViatico(Integer ideGtttv) {
        this.ideGtttv = ideGtttv;
    }

    public Integer getIdeGtttv() {
        return ideGtttv;
    }

    public void setIdeGtttv(Integer ideGtttv) {
        this.ideGtttv = ideGtttv;
    }

    public String getDetalleGtttv() {
        return detalleGtttv;
    }

    public void setDetalleGtttv(String detalleGtttv) {
        this.detalleGtttv = detalleGtttv;
    }

    public Boolean getActivoGtttv() {
        return activoGtttv;
    }

    public void setActivoGtttv(Boolean activoGtttv) {
        this.activoGtttv = activoGtttv;
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

    public List<GthTransporteViatico> getGthTransporteViaticoList() {
        return gthTransporteViaticoList;
    }

    public void setGthTransporteViaticoList(List<GthTransporteViatico> gthTransporteViaticoList) {
        this.gthTransporteViaticoList = gthTransporteViaticoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtttv != null ? ideGtttv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoTransporteViatico)) {
            return false;
        }
        GthTipoTransporteViatico other = (GthTipoTransporteViatico) object;
        if ((this.ideGtttv == null && other.ideGtttv != null) || (this.ideGtttv != null && !this.ideGtttv.equals(other.ideGtttv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoTransporteViatico[ ideGtttv=" + ideGtttv + " ]";
    }
    
}
