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
@Table(name = "crc_tipo_calendario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrcTipoCalendario.findAll", query = "SELECT c FROM CrcTipoCalendario c"),
    @NamedQuery(name = "CrcTipoCalendario.findByIdeCrtic", query = "SELECT c FROM CrcTipoCalendario c WHERE c.ideCrtic = :ideCrtic"),
    @NamedQuery(name = "CrcTipoCalendario.findByDetalleCrtic", query = "SELECT c FROM CrcTipoCalendario c WHERE c.detalleCrtic = :detalleCrtic"),
    @NamedQuery(name = "CrcTipoCalendario.findByActivoCrtic", query = "SELECT c FROM CrcTipoCalendario c WHERE c.activoCrtic = :activoCrtic"),
    @NamedQuery(name = "CrcTipoCalendario.findByUsuarioIngre", query = "SELECT c FROM CrcTipoCalendario c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrcTipoCalendario.findByFechaIngre", query = "SELECT c FROM CrcTipoCalendario c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrcTipoCalendario.findByHoraIngre", query = "SELECT c FROM CrcTipoCalendario c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrcTipoCalendario.findByUsuarioActua", query = "SELECT c FROM CrcTipoCalendario c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrcTipoCalendario.findByFechaActua", query = "SELECT c FROM CrcTipoCalendario c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrcTipoCalendario.findByHoraActua", query = "SELECT c FROM CrcTipoCalendario c WHERE c.horaActua = :horaActua")})
public class CrcTipoCalendario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_crtic", nullable = false)
    private Integer ideCrtic;
    @Size(max = 50)
    @Column(name = "detalle_crtic", length = 50)
    private String detalleCrtic;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_crtic", nullable = false)
    private boolean activoCrtic;
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
    @OneToMany(mappedBy = "ideCrtic")
    private List<CrcDetalleEvento> crcDetalleEventoList;

    public CrcTipoCalendario() {
    }

    public CrcTipoCalendario(Integer ideCrtic) {
        this.ideCrtic = ideCrtic;
    }

    public CrcTipoCalendario(Integer ideCrtic, boolean activoCrtic) {
        this.ideCrtic = ideCrtic;
        this.activoCrtic = activoCrtic;
    }

    public Integer getIdeCrtic() {
        return ideCrtic;
    }

    public void setIdeCrtic(Integer ideCrtic) {
        this.ideCrtic = ideCrtic;
    }

    public String getDetalleCrtic() {
        return detalleCrtic;
    }

    public void setDetalleCrtic(String detalleCrtic) {
        this.detalleCrtic = detalleCrtic;
    }

    public boolean getActivoCrtic() {
        return activoCrtic;
    }

    public void setActivoCrtic(boolean activoCrtic) {
        this.activoCrtic = activoCrtic;
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

    public List<CrcDetalleEvento> getCrcDetalleEventoList() {
        return crcDetalleEventoList;
    }

    public void setCrcDetalleEventoList(List<CrcDetalleEvento> crcDetalleEventoList) {
        this.crcDetalleEventoList = crcDetalleEventoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCrtic != null ? ideCrtic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrcTipoCalendario)) {
            return false;
        }
        CrcTipoCalendario other = (CrcTipoCalendario) object;
        if ((this.ideCrtic == null && other.ideCrtic != null) || (this.ideCrtic != null && !this.ideCrtic.equals(other.ideCrtic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrcTipoCalendario[ ideCrtic=" + ideCrtic + " ]";
    }
    
}
