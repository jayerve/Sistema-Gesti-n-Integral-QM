/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "spr_grupo_factor", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprGrupoFactor.findAll", query = "SELECT s FROM SprGrupoFactor s"),
    @NamedQuery(name = "SprGrupoFactor.findByIdeSpgrf", query = "SELECT s FROM SprGrupoFactor s WHERE s.ideSpgrf = :ideSpgrf"),
    @NamedQuery(name = "SprGrupoFactor.findByIdeGegro", query = "SELECT s FROM SprGrupoFactor s WHERE s.ideGegro = :ideGegro"),
    @NamedQuery(name = "SprGrupoFactor.findByPuntosSpgrf", query = "SELECT s FROM SprGrupoFactor s WHERE s.puntosSpgrf = :puntosSpgrf"),
    @NamedQuery(name = "SprGrupoFactor.findByActivoSpgrf", query = "SELECT s FROM SprGrupoFactor s WHERE s.activoSpgrf = :activoSpgrf"),
    @NamedQuery(name = "SprGrupoFactor.findByUsuarioIngre", query = "SELECT s FROM SprGrupoFactor s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprGrupoFactor.findByFechaIngre", query = "SELECT s FROM SprGrupoFactor s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprGrupoFactor.findByHoraIngre", query = "SELECT s FROM SprGrupoFactor s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprGrupoFactor.findByUsuarioActua", query = "SELECT s FROM SprGrupoFactor s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprGrupoFactor.findByFechaActua", query = "SELECT s FROM SprGrupoFactor s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprGrupoFactor.findByHoraActua", query = "SELECT s FROM SprGrupoFactor s WHERE s.horaActua = :horaActua")})
public class SprGrupoFactor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spgrf", nullable = false)
    private Integer ideSpgrf;
    @Column(name = "ide_gegro")
    private Integer ideGegro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puntos_spgrf", precision = 12, scale = 2)
    private BigDecimal puntosSpgrf;
    @Column(name = "activo_spgrf")
    private Boolean activoSpgrf;
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
    @JoinColumn(name = "ide_spfac", referencedColumnName = "ide_spfac")
    @ManyToOne
    private SprFactor ideSpfac;

    public SprGrupoFactor() {
    }

    public SprGrupoFactor(Integer ideSpgrf) {
        this.ideSpgrf = ideSpgrf;
    }

    public Integer getIdeSpgrf() {
        return ideSpgrf;
    }

    public void setIdeSpgrf(Integer ideSpgrf) {
        this.ideSpgrf = ideSpgrf;
    }

    public Integer getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(Integer ideGegro) {
        this.ideGegro = ideGegro;
    }

    public BigDecimal getPuntosSpgrf() {
        return puntosSpgrf;
    }

    public void setPuntosSpgrf(BigDecimal puntosSpgrf) {
        this.puntosSpgrf = puntosSpgrf;
    }

    public Boolean getActivoSpgrf() {
        return activoSpgrf;
    }

    public void setActivoSpgrf(Boolean activoSpgrf) {
        this.activoSpgrf = activoSpgrf;
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

    public SprFactor getIdeSpfac() {
        return ideSpfac;
    }

    public void setIdeSpfac(SprFactor ideSpfac) {
        this.ideSpfac = ideSpfac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpgrf != null ? ideSpgrf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprGrupoFactor)) {
            return false;
        }
        SprGrupoFactor other = (SprGrupoFactor) object;
        if ((this.ideSpgrf == null && other.ideSpgrf != null) || (this.ideSpgrf != null && !this.ideSpgrf.equals(other.ideSpgrf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprGrupoFactor[ ideSpgrf=" + ideSpgrf + " ]";
    }
    
}
