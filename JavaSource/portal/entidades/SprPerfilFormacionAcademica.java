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
@Table(name = "spr_perfil_formacion_academica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprPerfilFormacionAcademica.findAll", query = "SELECT s FROM SprPerfilFormacionAcademica s"),
    @NamedQuery(name = "SprPerfilFormacionAcademica.findByIdeSppfa", query = "SELECT s FROM SprPerfilFormacionAcademica s WHERE s.ideSppfa = :ideSppfa"),
    @NamedQuery(name = "SprPerfilFormacionAcademica.findByActivoSppfa", query = "SELECT s FROM SprPerfilFormacionAcademica s WHERE s.activoSppfa = :activoSppfa"),
    @NamedQuery(name = "SprPerfilFormacionAcademica.findByUsuarioIngre", query = "SELECT s FROM SprPerfilFormacionAcademica s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprPerfilFormacionAcademica.findByFechaIngre", query = "SELECT s FROM SprPerfilFormacionAcademica s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprPerfilFormacionAcademica.findByHoraIngre", query = "SELECT s FROM SprPerfilFormacionAcademica s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprPerfilFormacionAcademica.findByUsuarioActua", query = "SELECT s FROM SprPerfilFormacionAcademica s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprPerfilFormacionAcademica.findByFechaActua", query = "SELECT s FROM SprPerfilFormacionAcademica s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprPerfilFormacionAcademica.findByHoraActua", query = "SELECT s FROM SprPerfilFormacionAcademica s WHERE s.horaActua = :horaActua")})
public class SprPerfilFormacionAcademica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sppfa", nullable = false)
    private Integer ideSppfa;
    @Column(name = "activo_sppfa")
    private Boolean activoSppfa;
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

    public SprPerfilFormacionAcademica() {
    }

    public SprPerfilFormacionAcademica(Integer ideSppfa) {
        this.ideSppfa = ideSppfa;
    }

    public Integer getIdeSppfa() {
        return ideSppfa;
    }

    public void setIdeSppfa(Integer ideSppfa) {
        this.ideSppfa = ideSppfa;
    }

    public Boolean getActivoSppfa() {
        return activoSppfa;
    }

    public void setActivoSppfa(Boolean activoSppfa) {
        this.activoSppfa = activoSppfa;
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
        hash += (ideSppfa != null ? ideSppfa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprPerfilFormacionAcademica)) {
            return false;
        }
        SprPerfilFormacionAcademica other = (SprPerfilFormacionAcademica) object;
        if ((this.ideSppfa == null && other.ideSppfa != null) || (this.ideSppfa != null && !this.ideSppfa.equals(other.ideSppfa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprPerfilFormacionAcademica[ ideSppfa=" + ideSppfa + " ]";
    }
    
}
