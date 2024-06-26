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
@Table(name = "spr_grupo_minimo_seleccion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findAll", query = "SELECT s FROM SprGrupoMinimoSeleccion s"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByIdeSpgms", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.ideSpgms = :ideSpgms"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByIdeGegro", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.ideGegro = :ideGegro"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByPuntosSpgms", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.puntosSpgms = :puntosSpgms"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByActivoSpgms", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.activoSpgms = :activoSpgms"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByUsuarioIngre", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByFechaIngre", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByUsuarioActua", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByFechaActua", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByHoraActua", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.horaActua = :horaActua"),
    @NamedQuery(name = "SprGrupoMinimoSeleccion.findByHoraIngre", query = "SELECT s FROM SprGrupoMinimoSeleccion s WHERE s.horaIngre = :horaIngre")})
public class SprGrupoMinimoSeleccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spgms", nullable = false)
    private Integer ideSpgms;
    @Column(name = "ide_gegro")
    private Integer ideGegro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puntos_spgms", precision = 12, scale = 2)
    private BigDecimal puntosSpgms;
    @Column(name = "activo_spgms")
    private Boolean activoSpgms;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;

    public SprGrupoMinimoSeleccion() {
    }

    public SprGrupoMinimoSeleccion(Integer ideSpgms) {
        this.ideSpgms = ideSpgms;
    }

    public Integer getIdeSpgms() {
        return ideSpgms;
    }

    public void setIdeSpgms(Integer ideSpgms) {
        this.ideSpgms = ideSpgms;
    }

    public Integer getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(Integer ideGegro) {
        this.ideGegro = ideGegro;
    }

    public BigDecimal getPuntosSpgms() {
        return puntosSpgms;
    }

    public void setPuntosSpgms(BigDecimal puntosSpgms) {
        this.puntosSpgms = puntosSpgms;
    }

    public Boolean getActivoSpgms() {
        return activoSpgms;
    }

    public void setActivoSpgms(Boolean activoSpgms) {
        this.activoSpgms = activoSpgms;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpgms != null ? ideSpgms.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprGrupoMinimoSeleccion)) {
            return false;
        }
        SprGrupoMinimoSeleccion other = (SprGrupoMinimoSeleccion) object;
        if ((this.ideSpgms == null && other.ideSpgms != null) || (this.ideSpgms != null && !this.ideSpgms.equals(other.ideSpgms))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprGrupoMinimoSeleccion[ ideSpgms=" + ideSpgms + " ]";
    }
    
}
