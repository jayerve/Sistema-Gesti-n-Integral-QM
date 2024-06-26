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
@Table(name = "tes_tipo_poliza", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesTipoPoliza.findAll", query = "SELECT t FROM TesTipoPoliza t"),
    @NamedQuery(name = "TesTipoPoliza.findByIdeTetip", query = "SELECT t FROM TesTipoPoliza t WHERE t.ideTetip = :ideTetip"),
    @NamedQuery(name = "TesTipoPoliza.findByDetalleTetip", query = "SELECT t FROM TesTipoPoliza t WHERE t.detalleTetip = :detalleTetip"),
    @NamedQuery(name = "TesTipoPoliza.findByActivoTetip", query = "SELECT t FROM TesTipoPoliza t WHERE t.activoTetip = :activoTetip"),
    @NamedQuery(name = "TesTipoPoliza.findByUsuarioIngre", query = "SELECT t FROM TesTipoPoliza t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesTipoPoliza.findByFechaIngre", query = "SELECT t FROM TesTipoPoliza t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesTipoPoliza.findByHoraIngre", query = "SELECT t FROM TesTipoPoliza t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesTipoPoliza.findByUsuarioActua", query = "SELECT t FROM TesTipoPoliza t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesTipoPoliza.findByFechaActua", query = "SELECT t FROM TesTipoPoliza t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesTipoPoliza.findByHoraActua", query = "SELECT t FROM TesTipoPoliza t WHERE t.horaActua = :horaActua")})
public class TesTipoPoliza implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tetip", nullable = false)
    private Long ideTetip;
    @Size(max = 50)
    @Column(name = "detalle_tetip", length = 50)
    private String detalleTetip;
    @Column(name = "activo_tetip")
    private Boolean activoTetip;
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
    @OneToMany(mappedBy = "ideTetip")
    private List<TesPoliza> tesPolizaList;

    public TesTipoPoliza() {
    }

    public TesTipoPoliza(Long ideTetip) {
        this.ideTetip = ideTetip;
    }

    public Long getIdeTetip() {
        return ideTetip;
    }

    public void setIdeTetip(Long ideTetip) {
        this.ideTetip = ideTetip;
    }

    public String getDetalleTetip() {
        return detalleTetip;
    }

    public void setDetalleTetip(String detalleTetip) {
        this.detalleTetip = detalleTetip;
    }

    public Boolean getActivoTetip() {
        return activoTetip;
    }

    public void setActivoTetip(Boolean activoTetip) {
        this.activoTetip = activoTetip;
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

    public List<TesPoliza> getTesPolizaList() {
        return tesPolizaList;
    }

    public void setTesPolizaList(List<TesPoliza> tesPolizaList) {
        this.tesPolizaList = tesPolizaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTetip != null ? ideTetip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesTipoPoliza)) {
            return false;
        }
        TesTipoPoliza other = (TesTipoPoliza) object;
        if ((this.ideTetip == null && other.ideTetip != null) || (this.ideTetip != null && !this.ideTetip.equals(other.ideTetip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesTipoPoliza[ ideTetip=" + ideTetip + " ]";
    }
    
}
