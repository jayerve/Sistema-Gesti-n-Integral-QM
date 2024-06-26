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
@Table(name = "evl_grupo_factor", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlGrupoFactor.findAll", query = "SELECT e FROM EvlGrupoFactor e"),
    @NamedQuery(name = "EvlGrupoFactor.findByIdeEvgrf", query = "SELECT e FROM EvlGrupoFactor e WHERE e.ideEvgrf = :ideEvgrf"),
    @NamedQuery(name = "EvlGrupoFactor.findByIdeGegro", query = "SELECT e FROM EvlGrupoFactor e WHERE e.ideGegro = :ideGegro"),
    @NamedQuery(name = "EvlGrupoFactor.findByPesoPorEvgrf", query = "SELECT e FROM EvlGrupoFactor e WHERE e.pesoPorEvgrf = :pesoPorEvgrf"),
    @NamedQuery(name = "EvlGrupoFactor.findByActivoEvgrf", query = "SELECT e FROM EvlGrupoFactor e WHERE e.activoEvgrf = :activoEvgrf"),
    @NamedQuery(name = "EvlGrupoFactor.findByUsuarioIngre", query = "SELECT e FROM EvlGrupoFactor e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlGrupoFactor.findByFechaIngre", query = "SELECT e FROM EvlGrupoFactor e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlGrupoFactor.findByHoraIngre", query = "SELECT e FROM EvlGrupoFactor e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlGrupoFactor.findByUsuarioActua", query = "SELECT e FROM EvlGrupoFactor e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlGrupoFactor.findByFechaActua", query = "SELECT e FROM EvlGrupoFactor e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlGrupoFactor.findByHoraActua", query = "SELECT e FROM EvlGrupoFactor e WHERE e.horaActua = :horaActua")})
public class EvlGrupoFactor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evgrf", nullable = false)
    private Integer ideEvgrf;
    @Column(name = "ide_gegro")
    private Integer ideGegro;
    @Column(name = "peso_por_evgrf")
    private Integer pesoPorEvgrf;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evgrf", nullable = false)
    private boolean activoEvgrf;
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
    @JoinColumn(name = "ide_evfae", referencedColumnName = "ide_evfae")
    @ManyToOne
    private EvlFactorEvaluacion ideEvfae;

    public EvlGrupoFactor() {
    }

    public EvlGrupoFactor(Integer ideEvgrf) {
        this.ideEvgrf = ideEvgrf;
    }

    public EvlGrupoFactor(Integer ideEvgrf, boolean activoEvgrf) {
        this.ideEvgrf = ideEvgrf;
        this.activoEvgrf = activoEvgrf;
    }

    public Integer getIdeEvgrf() {
        return ideEvgrf;
    }

    public void setIdeEvgrf(Integer ideEvgrf) {
        this.ideEvgrf = ideEvgrf;
    }

    public Integer getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(Integer ideGegro) {
        this.ideGegro = ideGegro;
    }

    public Integer getPesoPorEvgrf() {
        return pesoPorEvgrf;
    }

    public void setPesoPorEvgrf(Integer pesoPorEvgrf) {
        this.pesoPorEvgrf = pesoPorEvgrf;
    }

    public boolean getActivoEvgrf() {
        return activoEvgrf;
    }

    public void setActivoEvgrf(boolean activoEvgrf) {
        this.activoEvgrf = activoEvgrf;
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

    public EvlFactorEvaluacion getIdeEvfae() {
        return ideEvfae;
    }

    public void setIdeEvfae(EvlFactorEvaluacion ideEvfae) {
        this.ideEvfae = ideEvfae;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEvgrf != null ? ideEvgrf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlGrupoFactor)) {
            return false;
        }
        EvlGrupoFactor other = (EvlGrupoFactor) object;
        if ((this.ideEvgrf == null && other.ideEvgrf != null) || (this.ideEvgrf != null && !this.ideEvgrf.equals(other.ideEvgrf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlGrupoFactor[ ideEvgrf=" + ideEvgrf + " ]";
    }
    
}
