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
@Table(name = "rec_tipo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecTipo.findAll", query = "SELECT r FROM RecTipo r"),
    @NamedQuery(name = "RecTipo.findByIdeRetip", query = "SELECT r FROM RecTipo r WHERE r.ideRetip = :ideRetip"),
    @NamedQuery(name = "RecTipo.findByDetalleRetip", query = "SELECT r FROM RecTipo r WHERE r.detalleRetip = :detalleRetip"),
    @NamedQuery(name = "RecTipo.findByActivoRetip", query = "SELECT r FROM RecTipo r WHERE r.activoRetip = :activoRetip"),
    @NamedQuery(name = "RecTipo.findByUsuarioIngre", query = "SELECT r FROM RecTipo r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecTipo.findByFechaIngre", query = "SELECT r FROM RecTipo r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecTipo.findByHoraIngre", query = "SELECT r FROM RecTipo r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecTipo.findByUsuarioActua", query = "SELECT r FROM RecTipo r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecTipo.findByFechaActua", query = "SELECT r FROM RecTipo r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecTipo.findByHoraActua", query = "SELECT r FROM RecTipo r WHERE r.horaActua = :horaActua")})
public class RecTipo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_retip", nullable = false)
    private Long ideRetip;
    @Size(max = 50)
    @Column(name = "detalle_retip", length = 50)
    private String detalleRetip;
    @Column(name = "activo_retip")
    private Boolean activoRetip;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideRetip")
    private List<FacFactura> facFacturaList;

    public RecTipo() {
    }

    public RecTipo(Long ideRetip) {
        this.ideRetip = ideRetip;
    }

    public Long getIdeRetip() {
        return ideRetip;
    }

    public void setIdeRetip(Long ideRetip) {
        this.ideRetip = ideRetip;
    }

    public String getDetalleRetip() {
        return detalleRetip;
    }

    public void setDetalleRetip(String detalleRetip) {
        this.detalleRetip = detalleRetip;
    }

    public Boolean getActivoRetip() {
        return activoRetip;
    }

    public void setActivoRetip(Boolean activoRetip) {
        this.activoRetip = activoRetip;
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

    public List<FacFactura> getFacFacturaList() {
        return facFacturaList;
    }

    public void setFacFacturaList(List<FacFactura> facFacturaList) {
        this.facFacturaList = facFacturaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRetip != null ? ideRetip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecTipo)) {
            return false;
        }
        RecTipo other = (RecTipo) object;
        if ((this.ideRetip == null && other.ideRetip != null) || (this.ideRetip != null && !this.ideRetip.equals(other.ideRetip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecTipo[ ideRetip=" + ideRetip + " ]";
    }
    
}
