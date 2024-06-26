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
@Table(name = "cont_cierre_ano", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContCierreAno.findAll", query = "SELECT c FROM ContCierreAno c"),
    @NamedQuery(name = "ContCierreAno.findByIdeCocia", query = "SELECT c FROM ContCierreAno c WHERE c.ideCocia = :ideCocia"),
    @NamedQuery(name = "ContCierreAno.findByBloqueadoCocim", query = "SELECT c FROM ContCierreAno c WHERE c.bloqueadoCocim = :bloqueadoCocim"),
    @NamedQuery(name = "ContCierreAno.findByActivoCocim", query = "SELECT c FROM ContCierreAno c WHERE c.activoCocim = :activoCocim"),
    @NamedQuery(name = "ContCierreAno.findByUsuarioIngre", query = "SELECT c FROM ContCierreAno c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContCierreAno.findByFechaIngre", query = "SELECT c FROM ContCierreAno c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContCierreAno.findByHoraIngre", query = "SELECT c FROM ContCierreAno c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContCierreAno.findByUsuarioActua", query = "SELECT c FROM ContCierreAno c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContCierreAno.findByFechaActua", query = "SELECT c FROM ContCierreAno c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContCierreAno.findByHoraActua", query = "SELECT c FROM ContCierreAno c WHERE c.horaActua = :horaActua")})
public class ContCierreAno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cocia", nullable = false)
    private Long ideCocia;
    @Column(name = "bloqueado_cocim")
    private Boolean bloqueadoCocim;
    @Column(name = "activo_cocim")
    private Boolean activoCocim;
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
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @JoinColumn(name = "ide_cocim", referencedColumnName = "ide_cocim")
    @ManyToOne
    private ContCierreMovimiento ideCocim;

    public ContCierreAno() {
    }

    public ContCierreAno(Long ideCocia) {
        this.ideCocia = ideCocia;
    }

    public Long getIdeCocia() {
        return ideCocia;
    }

    public void setIdeCocia(Long ideCocia) {
        this.ideCocia = ideCocia;
    }

    public Boolean getBloqueadoCocim() {
        return bloqueadoCocim;
    }

    public void setBloqueadoCocim(Boolean bloqueadoCocim) {
        this.bloqueadoCocim = bloqueadoCocim;
    }

    public Boolean getActivoCocim() {
        return activoCocim;
    }

    public void setActivoCocim(Boolean activoCocim) {
        this.activoCocim = activoCocim;
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

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    public ContCierreMovimiento getIdeCocim() {
        return ideCocim;
    }

    public void setIdeCocim(ContCierreMovimiento ideCocim) {
        this.ideCocim = ideCocim;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCocia != null ? ideCocia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContCierreAno)) {
            return false;
        }
        ContCierreAno other = (ContCierreAno) object;
        if ((this.ideCocia == null && other.ideCocia != null) || (this.ideCocia != null && !this.ideCocia.equals(other.ideCocia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContCierreAno[ ideCocia=" + ideCocia + " ]";
    }
    
}
