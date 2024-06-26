/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "crc_usuario_calendario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrcUsuarioCalendario.findAll", query = "SELECT c FROM CrcUsuarioCalendario c"),
    @NamedQuery(name = "CrcUsuarioCalendario.findByIdeCrusc", query = "SELECT c FROM CrcUsuarioCalendario c WHERE c.ideCrusc = :ideCrusc"),
    @NamedQuery(name = "CrcUsuarioCalendario.findByActivoCrusc", query = "SELECT c FROM CrcUsuarioCalendario c WHERE c.activoCrusc = :activoCrusc"),
    @NamedQuery(name = "CrcUsuarioCalendario.findByUsuarioIngre", query = "SELECT c FROM CrcUsuarioCalendario c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrcUsuarioCalendario.findByFechaIngre", query = "SELECT c FROM CrcUsuarioCalendario c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrcUsuarioCalendario.findByHoraIngre", query = "SELECT c FROM CrcUsuarioCalendario c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrcUsuarioCalendario.findByUsuarioActua", query = "SELECT c FROM CrcUsuarioCalendario c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrcUsuarioCalendario.findByFechaActua", query = "SELECT c FROM CrcUsuarioCalendario c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrcUsuarioCalendario.findByHoraActua", query = "SELECT c FROM CrcUsuarioCalendario c WHERE c.horaActua = :horaActua")})
public class CrcUsuarioCalendario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_crusc", nullable = false)
    private Integer ideCrusc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_crusc", nullable = false)
    private boolean activoCrusc;
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
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;
    @JoinColumn(name = "ide_crgrc", referencedColumnName = "ide_crgrc")
    @ManyToOne
    private CrcGrupoCalendario ideCrgrc;

    public CrcUsuarioCalendario() {
    }

    public CrcUsuarioCalendario(Integer ideCrusc) {
        this.ideCrusc = ideCrusc;
    }

    public CrcUsuarioCalendario(Integer ideCrusc, boolean activoCrusc) {
        this.ideCrusc = ideCrusc;
        this.activoCrusc = activoCrusc;
    }

    public Integer getIdeCrusc() {
        return ideCrusc;
    }

    public void setIdeCrusc(Integer ideCrusc) {
        this.ideCrusc = ideCrusc;
    }

    public boolean getActivoCrusc() {
        return activoCrusc;
    }

    public void setActivoCrusc(boolean activoCrusc) {
        this.activoCrusc = activoCrusc;
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

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    public CrcGrupoCalendario getIdeCrgrc() {
        return ideCrgrc;
    }

    public void setIdeCrgrc(CrcGrupoCalendario ideCrgrc) {
        this.ideCrgrc = ideCrgrc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCrusc != null ? ideCrusc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrcUsuarioCalendario)) {
            return false;
        }
        CrcUsuarioCalendario other = (CrcUsuarioCalendario) object;
        if ((this.ideCrusc == null && other.ideCrusc != null) || (this.ideCrusc != null && !this.ideCrusc.equals(other.ideCrusc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrcUsuarioCalendario[ ideCrusc=" + ideCrusc + " ]";
    }
    
}
