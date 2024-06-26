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
@Table(name = "tes_tipo_documento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesTipoDocumento.findAll", query = "SELECT t FROM TesTipoDocumento t"),
    @NamedQuery(name = "TesTipoDocumento.findByIdeTetid", query = "SELECT t FROM TesTipoDocumento t WHERE t.ideTetid = :ideTetid"),
    @NamedQuery(name = "TesTipoDocumento.findByDetalleTetid", query = "SELECT t FROM TesTipoDocumento t WHERE t.detalleTetid = :detalleTetid"),
    @NamedQuery(name = "TesTipoDocumento.findByActivoTetid", query = "SELECT t FROM TesTipoDocumento t WHERE t.activoTetid = :activoTetid"),
    @NamedQuery(name = "TesTipoDocumento.findByUsuarioIngre", query = "SELECT t FROM TesTipoDocumento t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesTipoDocumento.findByFechaIngre", query = "SELECT t FROM TesTipoDocumento t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesTipoDocumento.findByHoraIngre", query = "SELECT t FROM TesTipoDocumento t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesTipoDocumento.findByUsuarioActua", query = "SELECT t FROM TesTipoDocumento t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesTipoDocumento.findByFechaActua", query = "SELECT t FROM TesTipoDocumento t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesTipoDocumento.findByHoraActua", query = "SELECT t FROM TesTipoDocumento t WHERE t.horaActua = :horaActua")})
public class TesTipoDocumento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tetid", nullable = false)
    private Long ideTetid;
    @Size(max = 50)
    @Column(name = "detalle_tetid", length = 50)
    private String detalleTetid;
    @Column(name = "activo_tetid")
    private Boolean activoTetid;
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
    @OneToMany(mappedBy = "ideTetid")
    private List<FacFactura> facFacturaList;

    public TesTipoDocumento() {
    }

    public TesTipoDocumento(Long ideTetid) {
        this.ideTetid = ideTetid;
    }

    public Long getIdeTetid() {
        return ideTetid;
    }

    public void setIdeTetid(Long ideTetid) {
        this.ideTetid = ideTetid;
    }

    public String getDetalleTetid() {
        return detalleTetid;
    }

    public void setDetalleTetid(String detalleTetid) {
        this.detalleTetid = detalleTetid;
    }

    public Boolean getActivoTetid() {
        return activoTetid;
    }

    public void setActivoTetid(Boolean activoTetid) {
        this.activoTetid = activoTetid;
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
        hash += (ideTetid != null ? ideTetid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesTipoDocumento)) {
            return false;
        }
        TesTipoDocumento other = (TesTipoDocumento) object;
        if ((this.ideTetid == null && other.ideTetid != null) || (this.ideTetid != null && !this.ideTetid.equals(other.ideTetid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesTipoDocumento[ ideTetid=" + ideTetid + " ]";
    }
    
}
