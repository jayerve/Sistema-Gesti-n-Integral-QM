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
@Table(name = "spr_idioma", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprIdioma.findAll", query = "SELECT s FROM SprIdioma s"),
    @NamedQuery(name = "SprIdioma.findByIdeSpidi", query = "SELECT s FROM SprIdioma s WHERE s.ideSpidi = :ideSpidi"),
    @NamedQuery(name = "SprIdioma.findByPorcentajeLeeSpidi", query = "SELECT s FROM SprIdioma s WHERE s.porcentajeLeeSpidi = :porcentajeLeeSpidi"),
    @NamedQuery(name = "SprIdioma.findByPorcentajeEscribeSpidi", query = "SELECT s FROM SprIdioma s WHERE s.porcentajeEscribeSpidi = :porcentajeEscribeSpidi"),
    @NamedQuery(name = "SprIdioma.findByPorcentajeHablaSpidi", query = "SELECT s FROM SprIdioma s WHERE s.porcentajeHablaSpidi = :porcentajeHablaSpidi"),
    @NamedQuery(name = "SprIdioma.findByActivoSpidi", query = "SELECT s FROM SprIdioma s WHERE s.activoSpidi = :activoSpidi"),
    @NamedQuery(name = "SprIdioma.findByUsuarioIngre", query = "SELECT s FROM SprIdioma s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprIdioma.findByFechaIngre", query = "SELECT s FROM SprIdioma s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprIdioma.findByHoraIngre", query = "SELECT s FROM SprIdioma s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprIdioma.findByUsuarioActua", query = "SELECT s FROM SprIdioma s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprIdioma.findByFechaActua", query = "SELECT s FROM SprIdioma s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprIdioma.findByHoraActua", query = "SELECT s FROM SprIdioma s WHERE s.horaActua = :horaActua")})
public class SprIdioma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spidi", nullable = false)
    private Integer ideSpidi;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_lee_spidi", precision = 12, scale = 2)
    private BigDecimal porcentajeLeeSpidi;
    @Column(name = "porcentaje_escribe_spidi", precision = 12, scale = 2)
    private BigDecimal porcentajeEscribeSpidi;
    @Column(name = "porcentaje_habla_spidi", precision = 12, scale = 2)
    private BigDecimal porcentajeHablaSpidi;
    @Column(name = "activo_spidi")
    private Boolean activoSpidi;
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
    @JoinColumn(name = "ide_spbap", referencedColumnName = "ide_spbap")
    @ManyToOne
    private SprBasePostulante ideSpbap;
    @JoinColumn(name = "ide_geidi", referencedColumnName = "ide_geidi")
    @ManyToOne
    private GenIdioma ideGeidi;

    public SprIdioma() {
    }

    public SprIdioma(Integer ideSpidi) {
        this.ideSpidi = ideSpidi;
    }

    public Integer getIdeSpidi() {
        return ideSpidi;
    }

    public void setIdeSpidi(Integer ideSpidi) {
        this.ideSpidi = ideSpidi;
    }

    public BigDecimal getPorcentajeLeeSpidi() {
        return porcentajeLeeSpidi;
    }

    public void setPorcentajeLeeSpidi(BigDecimal porcentajeLeeSpidi) {
        this.porcentajeLeeSpidi = porcentajeLeeSpidi;
    }

    public BigDecimal getPorcentajeEscribeSpidi() {
        return porcentajeEscribeSpidi;
    }

    public void setPorcentajeEscribeSpidi(BigDecimal porcentajeEscribeSpidi) {
        this.porcentajeEscribeSpidi = porcentajeEscribeSpidi;
    }

    public BigDecimal getPorcentajeHablaSpidi() {
        return porcentajeHablaSpidi;
    }

    public void setPorcentajeHablaSpidi(BigDecimal porcentajeHablaSpidi) {
        this.porcentajeHablaSpidi = porcentajeHablaSpidi;
    }

    public Boolean getActivoSpidi() {
        return activoSpidi;
    }

    public void setActivoSpidi(Boolean activoSpidi) {
        this.activoSpidi = activoSpidi;
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

    public SprBasePostulante getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(SprBasePostulante ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    public GenIdioma getIdeGeidi() {
        return ideGeidi;
    }

    public void setIdeGeidi(GenIdioma ideGeidi) {
        this.ideGeidi = ideGeidi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpidi != null ? ideSpidi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprIdioma)) {
            return false;
        }
        SprIdioma other = (SprIdioma) object;
        if ((this.ideSpidi == null && other.ideSpidi != null) || (this.ideSpidi != null && !this.ideSpidi.equals(other.ideSpidi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprIdioma[ ideSpidi=" + ideSpidi + " ]";
    }
    
}
