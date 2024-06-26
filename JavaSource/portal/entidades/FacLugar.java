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
@Table(name = "fac_lugar", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "FacLugar.findAll", query = "SELECT f FROM FacLugar f"),
    @NamedQuery(name = "FacLugar.findByIdeFalug", query = "SELECT f FROM FacLugar f WHERE f.ideFalug = :ideFalug"),
    @NamedQuery(name = "FacLugar.findByDetalleLugarFalug", query = "SELECT f FROM FacLugar f WHERE f.detalleLugarFalug = :detalleLugarFalug"),
    @NamedQuery(name = "FacLugar.findByActivoFalug", query = "SELECT f FROM FacLugar f WHERE f.activoFalug = :activoFalug"),
    @NamedQuery(name = "FacLugar.findByUsuarioIngre", query = "SELECT f FROM FacLugar f WHERE f.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "FacLugar.findByFechaIngre", query = "SELECT f FROM FacLugar f WHERE f.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "FacLugar.findByHoraIngre", query = "SELECT f FROM FacLugar f WHERE f.horaIngre = :horaIngre"),
    @NamedQuery(name = "FacLugar.findByUsuarioActua", query = "SELECT f FROM FacLugar f WHERE f.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "FacLugar.findByFechaActua", query = "SELECT f FROM FacLugar f WHERE f.fechaActua = :fechaActua"),
    @NamedQuery(name = "FacLugar.findByHoraActua", query = "SELECT f FROM FacLugar f WHERE f.horaActua = :horaActua")})
public class FacLugar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_falug", nullable = false)
    private Long ideFalug;
    @Size(max = 250)
    @Column(name = "detalle_lugar_falug", length = 250)
    private String detalleLugarFalug;
    @Column(name = "activo_falug")
    private Boolean activoFalug;
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
    @OneToMany(mappedBy = "ideFalug")
    private List<FacFactura> facFacturaList;
    @OneToMany(mappedBy = "ideFalug")
    private List<FacUsuarioLugar> facUsuarioLugarList;
    @OneToMany(mappedBy = "ideFalug")
    private List<FacVentaLugar> facVentaLugarList;

    public FacLugar() {
    }

    public FacLugar(Long ideFalug) {
        this.ideFalug = ideFalug;
    }

    public Long getIdeFalug() {
        return ideFalug;
    }

    public void setIdeFalug(Long ideFalug) {
        this.ideFalug = ideFalug;
    }

    public String getDetalleLugarFalug() {
        return detalleLugarFalug;
    }

    public void setDetalleLugarFalug(String detalleLugarFalug) {
        this.detalleLugarFalug = detalleLugarFalug;
    }

    public Boolean getActivoFalug() {
        return activoFalug;
    }

    public void setActivoFalug(Boolean activoFalug) {
        this.activoFalug = activoFalug;
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

    public List<FacUsuarioLugar> getFacUsuarioLugarList() {
        return facUsuarioLugarList;
    }

    public void setFacUsuarioLugarList(List<FacUsuarioLugar> facUsuarioLugarList) {
        this.facUsuarioLugarList = facUsuarioLugarList;
    }

    public List<FacVentaLugar> getFacVentaLugarList() {
        return facVentaLugarList;
    }

    public void setFacVentaLugarList(List<FacVentaLugar> facVentaLugarList) {
        this.facVentaLugarList = facVentaLugarList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFalug != null ? ideFalug.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacLugar)) {
            return false;
        }
        FacLugar other = (FacLugar) object;
        if ((this.ideFalug == null && other.ideFalug != null) || (this.ideFalug != null && !this.ideFalug.equals(other.ideFalug))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.FacLugar[ ideFalug=" + ideFalug + " ]";
    }
    
}
