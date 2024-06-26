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
@Table(name = "gen_modulo_estado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenModuloEstado.findAll", query = "SELECT g FROM GenModuloEstado g"),
    @NamedQuery(name = "GenModuloEstado.findByIdeGemoe", query = "SELECT g FROM GenModuloEstado g WHERE g.ideGemoe = :ideGemoe"),
    @NamedQuery(name = "GenModuloEstado.findByActivoGemoe", query = "SELECT g FROM GenModuloEstado g WHERE g.activoGemoe = :activoGemoe"),
    @NamedQuery(name = "GenModuloEstado.findByUsuarioIngre", query = "SELECT g FROM GenModuloEstado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenModuloEstado.findByFechaIngre", query = "SELECT g FROM GenModuloEstado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenModuloEstado.findByHoraIngre", query = "SELECT g FROM GenModuloEstado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenModuloEstado.findByUsuarioActua", query = "SELECT g FROM GenModuloEstado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenModuloEstado.findByFechaActua", query = "SELECT g FROM GenModuloEstado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenModuloEstado.findByHoraActua", query = "SELECT g FROM GenModuloEstado g WHERE g.horaActua = :horaActua")})
public class GenModuloEstado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gemoe", nullable = false)
    private Long ideGemoe;
    @Column(name = "activo_gemoe")
    private Boolean activoGemoe;
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
    @JoinColumn(name = "ide_gemod", referencedColumnName = "ide_gemod")
    @ManyToOne
    private GenModulo ideGemod;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;

    public GenModuloEstado() {
    }

    public GenModuloEstado(Long ideGemoe) {
        this.ideGemoe = ideGemoe;
    }

    public Long getIdeGemoe() {
        return ideGemoe;
    }

    public void setIdeGemoe(Long ideGemoe) {
        this.ideGemoe = ideGemoe;
    }

    public Boolean getActivoGemoe() {
        return activoGemoe;
    }

    public void setActivoGemoe(Boolean activoGemoe) {
        this.activoGemoe = activoGemoe;
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

    public GenModulo getIdeGemod() {
        return ideGemod;
    }

    public void setIdeGemod(GenModulo ideGemod) {
        this.ideGemod = ideGemod;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemoe != null ? ideGemoe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenModuloEstado)) {
            return false;
        }
        GenModuloEstado other = (GenModuloEstado) object;
        if ((this.ideGemoe == null && other.ideGemoe != null) || (this.ideGemoe != null && !this.ideGemoe.equals(other.ideGemoe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenModuloEstado[ ideGemoe=" + ideGemoe + " ]";
    }
    
}
