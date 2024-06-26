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
@Table(name = "gen_tipo_persona_modulo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenTipoPersonaModulo.findAll", query = "SELECT g FROM GenTipoPersonaModulo g"),
    @NamedQuery(name = "GenTipoPersonaModulo.findByIdeGetpm", query = "SELECT g FROM GenTipoPersonaModulo g WHERE g.ideGetpm = :ideGetpm"),
    @NamedQuery(name = "GenTipoPersonaModulo.findByActivoGetpm", query = "SELECT g FROM GenTipoPersonaModulo g WHERE g.activoGetpm = :activoGetpm"),
    @NamedQuery(name = "GenTipoPersonaModulo.findByUsuarioIngre", query = "SELECT g FROM GenTipoPersonaModulo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenTipoPersonaModulo.findByFechaIngre", query = "SELECT g FROM GenTipoPersonaModulo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenTipoPersonaModulo.findByHoraIngre", query = "SELECT g FROM GenTipoPersonaModulo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenTipoPersonaModulo.findByUsuarioActua", query = "SELECT g FROM GenTipoPersonaModulo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenTipoPersonaModulo.findByFechaActua", query = "SELECT g FROM GenTipoPersonaModulo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenTipoPersonaModulo.findByHoraActua", query = "SELECT g FROM GenTipoPersonaModulo g WHERE g.horaActua = :horaActua")})
public class GenTipoPersonaModulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_getpm", nullable = false)
    private Long ideGetpm;
    @Column(name = "activo_getpm")
    private Boolean activoGetpm;
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
    @JoinColumn(name = "ide_getip", referencedColumnName = "ide_getip")
    @ManyToOne
    private GenTipoPersona ideGetip;
    @JoinColumn(name = "ide_gemod", referencedColumnName = "ide_gemod")
    @ManyToOne
    private GenModulo ideGemod;

    public GenTipoPersonaModulo() {
    }

    public GenTipoPersonaModulo(Long ideGetpm) {
        this.ideGetpm = ideGetpm;
    }

    public Long getIdeGetpm() {
        return ideGetpm;
    }

    public void setIdeGetpm(Long ideGetpm) {
        this.ideGetpm = ideGetpm;
    }

    public Boolean getActivoGetpm() {
        return activoGetpm;
    }

    public void setActivoGetpm(Boolean activoGetpm) {
        this.activoGetpm = activoGetpm;
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

    public GenTipoPersona getIdeGetip() {
        return ideGetip;
    }

    public void setIdeGetip(GenTipoPersona ideGetip) {
        this.ideGetip = ideGetip;
    }

    public GenModulo getIdeGemod() {
        return ideGemod;
    }

    public void setIdeGemod(GenModulo ideGemod) {
        this.ideGemod = ideGemod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGetpm != null ? ideGetpm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenTipoPersonaModulo)) {
            return false;
        }
        GenTipoPersonaModulo other = (GenTipoPersonaModulo) object;
        if ((this.ideGetpm == null && other.ideGetpm != null) || (this.ideGetpm != null && !this.ideGetpm.equals(other.ideGetpm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenTipoPersonaModulo[ ideGetpm=" + ideGetpm + " ]";
    }
    
}
