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
@Table(name = "spr_perfil_competencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprPerfilCompetencia.findAll", query = "SELECT s FROM SprPerfilCompetencia s"),
    @NamedQuery(name = "SprPerfilCompetencia.findByIdeSppec", query = "SELECT s FROM SprPerfilCompetencia s WHERE s.ideSppec = :ideSppec"),
    @NamedQuery(name = "SprPerfilCompetencia.findByActivoSppec", query = "SELECT s FROM SprPerfilCompetencia s WHERE s.activoSppec = :activoSppec"),
    @NamedQuery(name = "SprPerfilCompetencia.findByUsuarioIngre", query = "SELECT s FROM SprPerfilCompetencia s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprPerfilCompetencia.findByFechaIngre", query = "SELECT s FROM SprPerfilCompetencia s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprPerfilCompetencia.findByHoraIngre", query = "SELECT s FROM SprPerfilCompetencia s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprPerfilCompetencia.findByUsuarioActua", query = "SELECT s FROM SprPerfilCompetencia s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprPerfilCompetencia.findByFechaActua", query = "SELECT s FROM SprPerfilCompetencia s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprPerfilCompetencia.findByHoraActua", query = "SELECT s FROM SprPerfilCompetencia s WHERE s.horaActua = :horaActua")})
public class SprPerfilCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sppec", nullable = false)
    private Integer ideSppec;
    @Column(name = "activo_sppec")
    private Boolean activoSppec;
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

    public SprPerfilCompetencia() {
    }

    public SprPerfilCompetencia(Integer ideSppec) {
        this.ideSppec = ideSppec;
    }

    public Integer getIdeSppec() {
        return ideSppec;
    }

    public void setIdeSppec(Integer ideSppec) {
        this.ideSppec = ideSppec;
    }

    public Boolean getActivoSppec() {
        return activoSppec;
    }

    public void setActivoSppec(Boolean activoSppec) {
        this.activoSppec = activoSppec;
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
        hash += (ideSppec != null ? ideSppec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprPerfilCompetencia)) {
            return false;
        }
        SprPerfilCompetencia other = (SprPerfilCompetencia) object;
        if ((this.ideSppec == null && other.ideSppec != null) || (this.ideSppec != null && !this.ideSppec.equals(other.ideSppec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprPerfilCompetencia[ ideSppec=" + ideSppec + " ]";
    }
    
}
