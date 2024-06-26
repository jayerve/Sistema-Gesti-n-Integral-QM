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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "spr_asunto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprAsunto.findAll", query = "SELECT s FROM SprAsunto s"),
    @NamedQuery(name = "SprAsunto.findByIdeSpasu", query = "SELECT s FROM SprAsunto s WHERE s.ideSpasu = :ideSpasu"),
    @NamedQuery(name = "SprAsunto.findByDetalleSpasu", query = "SELECT s FROM SprAsunto s WHERE s.detalleSpasu = :detalleSpasu"),
    @NamedQuery(name = "SprAsunto.findByActivoSpasu", query = "SELECT s FROM SprAsunto s WHERE s.activoSpasu = :activoSpasu"),
    @NamedQuery(name = "SprAsunto.findByUsuarioIngre", query = "SELECT s FROM SprAsunto s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprAsunto.findByFechaIngre", query = "SELECT s FROM SprAsunto s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprAsunto.findByHoraIngre", query = "SELECT s FROM SprAsunto s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprAsunto.findByUsuarioActua", query = "SELECT s FROM SprAsunto s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprAsunto.findByFechaActua", query = "SELECT s FROM SprAsunto s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprAsunto.findByHoraActua", query = "SELECT s FROM SprAsunto s WHERE s.horaActua = :horaActua")})
public class SprAsunto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spasu", nullable = false)
    private Integer ideSpasu;
    @Size(max = 50)
    @Column(name = "detalle_spasu", length = 50)
    private String detalleSpasu;
    @Column(name = "activo_spasu")
    private Boolean activoSpasu;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @OneToMany(mappedBy = "ideSpasu")
    private List<SprPresupuestoPuesto> sprPresupuestoPuestoList;
    @OneToMany(mappedBy = "ideSpasu")
    private List<SprSolicitudAprobacion> sprSolicitudAprobacionList;
    @OneToMany(mappedBy = "ideSpasu")
    private List<SprSolicitudPuesto> sprSolicitudPuestoList;
    @JoinColumn(name = "ide_sptia", referencedColumnName = "ide_sptia")
    @ManyToOne
    private SprTipoAsunto ideSptia;

    public SprAsunto() {
    }

    public SprAsunto(Integer ideSpasu) {
        this.ideSpasu = ideSpasu;
    }

    public Integer getIdeSpasu() {
        return ideSpasu;
    }

    public void setIdeSpasu(Integer ideSpasu) {
        this.ideSpasu = ideSpasu;
    }

    public String getDetalleSpasu() {
        return detalleSpasu;
    }

    public void setDetalleSpasu(String detalleSpasu) {
        this.detalleSpasu = detalleSpasu;
    }

    public Boolean getActivoSpasu() {
        return activoSpasu;
    }

    public void setActivoSpasu(Boolean activoSpasu) {
        this.activoSpasu = activoSpasu;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public List<SprPresupuestoPuesto> getSprPresupuestoPuestoList() {
        return sprPresupuestoPuestoList;
    }

    public void setSprPresupuestoPuestoList(List<SprPresupuestoPuesto> sprPresupuestoPuestoList) {
        this.sprPresupuestoPuestoList = sprPresupuestoPuestoList;
    }

    public List<SprSolicitudAprobacion> getSprSolicitudAprobacionList() {
        return sprSolicitudAprobacionList;
    }

    public void setSprSolicitudAprobacionList(List<SprSolicitudAprobacion> sprSolicitudAprobacionList) {
        this.sprSolicitudAprobacionList = sprSolicitudAprobacionList;
    }

    public List<SprSolicitudPuesto> getSprSolicitudPuestoList() {
        return sprSolicitudPuestoList;
    }

    public void setSprSolicitudPuestoList(List<SprSolicitudPuesto> sprSolicitudPuestoList) {
        this.sprSolicitudPuestoList = sprSolicitudPuestoList;
    }

    public SprTipoAsunto getIdeSptia() {
        return ideSptia;
    }

    public void setIdeSptia(SprTipoAsunto ideSptia) {
        this.ideSptia = ideSptia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpasu != null ? ideSpasu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprAsunto)) {
            return false;
        }
        SprAsunto other = (SprAsunto) object;
        if ((this.ideSpasu == null && other.ideSpasu != null) || (this.ideSpasu != null && !this.ideSpasu.equals(other.ideSpasu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprAsunto[ ideSpasu=" + ideSpasu + " ]";
    }
    
}
