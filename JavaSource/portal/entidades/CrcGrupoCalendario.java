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
@Table(name = "crc_grupo_calendario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrcGrupoCalendario.findAll", query = "SELECT c FROM CrcGrupoCalendario c"),
    @NamedQuery(name = "CrcGrupoCalendario.findByIdeCrgrc", query = "SELECT c FROM CrcGrupoCalendario c WHERE c.ideCrgrc = :ideCrgrc"),
    @NamedQuery(name = "CrcGrupoCalendario.findByDetalleCrgrc", query = "SELECT c FROM CrcGrupoCalendario c WHERE c.detalleCrgrc = :detalleCrgrc"),
    @NamedQuery(name = "CrcGrupoCalendario.findByActivoCrgrc", query = "SELECT c FROM CrcGrupoCalendario c WHERE c.activoCrgrc = :activoCrgrc"),
    @NamedQuery(name = "CrcGrupoCalendario.findByUsuarioIngre", query = "SELECT c FROM CrcGrupoCalendario c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrcGrupoCalendario.findByFechaIngre", query = "SELECT c FROM CrcGrupoCalendario c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrcGrupoCalendario.findByHoraIngre", query = "SELECT c FROM CrcGrupoCalendario c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrcGrupoCalendario.findByUsuarioActua", query = "SELECT c FROM CrcGrupoCalendario c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrcGrupoCalendario.findByFechaActua", query = "SELECT c FROM CrcGrupoCalendario c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrcGrupoCalendario.findByHoraActua", query = "SELECT c FROM CrcGrupoCalendario c WHERE c.horaActua = :horaActua")})
public class CrcGrupoCalendario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_crgrc", nullable = false)
    private Integer ideCrgrc;
    @Size(max = 100)
    @Column(name = "detalle_crgrc", length = 100)
    private String detalleCrgrc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_crgrc", nullable = false)
    private boolean activoCrgrc;
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
    @OneToMany(mappedBy = "ideCrgrc")
    private List<CrcEvento> crcEventoList;
    @OneToMany(mappedBy = "ideCrgrc")
    private List<CrcUsuarioCalendario> crcUsuarioCalendarioList;

    public CrcGrupoCalendario() {
    }

    public CrcGrupoCalendario(Integer ideCrgrc) {
        this.ideCrgrc = ideCrgrc;
    }

    public CrcGrupoCalendario(Integer ideCrgrc, boolean activoCrgrc) {
        this.ideCrgrc = ideCrgrc;
        this.activoCrgrc = activoCrgrc;
    }

    public Integer getIdeCrgrc() {
        return ideCrgrc;
    }

    public void setIdeCrgrc(Integer ideCrgrc) {
        this.ideCrgrc = ideCrgrc;
    }

    public String getDetalleCrgrc() {
        return detalleCrgrc;
    }

    public void setDetalleCrgrc(String detalleCrgrc) {
        this.detalleCrgrc = detalleCrgrc;
    }

    public boolean getActivoCrgrc() {
        return activoCrgrc;
    }

    public void setActivoCrgrc(boolean activoCrgrc) {
        this.activoCrgrc = activoCrgrc;
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

    public List<CrcEvento> getCrcEventoList() {
        return crcEventoList;
    }

    public void setCrcEventoList(List<CrcEvento> crcEventoList) {
        this.crcEventoList = crcEventoList;
    }

    public List<CrcUsuarioCalendario> getCrcUsuarioCalendarioList() {
        return crcUsuarioCalendarioList;
    }

    public void setCrcUsuarioCalendarioList(List<CrcUsuarioCalendario> crcUsuarioCalendarioList) {
        this.crcUsuarioCalendarioList = crcUsuarioCalendarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCrgrc != null ? ideCrgrc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrcGrupoCalendario)) {
            return false;
        }
        CrcGrupoCalendario other = (CrcGrupoCalendario) object;
        if ((this.ideCrgrc == null && other.ideCrgrc != null) || (this.ideCrgrc != null && !this.ideCrgrc.equals(other.ideCrgrc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrcGrupoCalendario[ ideCrgrc=" + ideCrgrc + " ]";
    }

}
