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
@Table(name = "spr_perfil_experiencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprPerfilExperiencia.findAll", query = "SELECT s FROM SprPerfilExperiencia s"),
    @NamedQuery(name = "SprPerfilExperiencia.findByIdeSppee", query = "SELECT s FROM SprPerfilExperiencia s WHERE s.ideSppee = :ideSppee"),
    @NamedQuery(name = "SprPerfilExperiencia.findByActivoSppee", query = "SELECT s FROM SprPerfilExperiencia s WHERE s.activoSppee = :activoSppee"),
    @NamedQuery(name = "SprPerfilExperiencia.findByUsuarioIngre", query = "SELECT s FROM SprPerfilExperiencia s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprPerfilExperiencia.findByFechaIngre", query = "SELECT s FROM SprPerfilExperiencia s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprPerfilExperiencia.findByHoraIngre", query = "SELECT s FROM SprPerfilExperiencia s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprPerfilExperiencia.findByUsuarioActua", query = "SELECT s FROM SprPerfilExperiencia s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprPerfilExperiencia.findByFechaActua", query = "SELECT s FROM SprPerfilExperiencia s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprPerfilExperiencia.findByHoraActua", query = "SELECT s FROM SprPerfilExperiencia s WHERE s.horaActua = :horaActua")})
public class SprPerfilExperiencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sppee", nullable = false)
    private Integer ideSppee;
    @Column(name = "activo_sppee")
    private Boolean activoSppee;
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
    @JoinColumn(name = "ide_spdsp", referencedColumnName = "ide_spdsp")
    @ManyToOne
    private SprDetalleSolicitudPuesto ideSpdsp;
    @JoinColumn(name = "ide_cmdec", referencedColumnName = "ide_cmdec")
    @ManyToOne
    private CmpDetalleCompetencia ideCmdec;

    public SprPerfilExperiencia() {
    }

    public SprPerfilExperiencia(Integer ideSppee) {
        this.ideSppee = ideSppee;
    }

    public Integer getIdeSppee() {
        return ideSppee;
    }

    public void setIdeSppee(Integer ideSppee) {
        this.ideSppee = ideSppee;
    }

    public Boolean getActivoSppee() {
        return activoSppee;
    }

    public void setActivoSppee(Boolean activoSppee) {
        this.activoSppee = activoSppee;
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

    public SprDetalleSolicitudPuesto getIdeSpdsp() {
        return ideSpdsp;
    }

    public void setIdeSpdsp(SprDetalleSolicitudPuesto ideSpdsp) {
        this.ideSpdsp = ideSpdsp;
    }

    public CmpDetalleCompetencia getIdeCmdec() {
        return ideCmdec;
    }

    public void setIdeCmdec(CmpDetalleCompetencia ideCmdec) {
        this.ideCmdec = ideCmdec;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSppee != null ? ideSppee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprPerfilExperiencia)) {
            return false;
        }
        SprPerfilExperiencia other = (SprPerfilExperiencia) object;
        if ((this.ideSppee == null && other.ideSppee != null) || (this.ideSppee != null && !this.ideSppee.equals(other.ideSppee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprPerfilExperiencia[ ideSppee=" + ideSppee + " ]";
    }
    
}
