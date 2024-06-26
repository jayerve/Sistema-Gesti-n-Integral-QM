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
@Table(name = "spr_estudios", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprEstudios.findAll", query = "SELECT s FROM SprEstudios s"),
    @NamedQuery(name = "SprEstudios.findByIdeSpest", query = "SELECT s FROM SprEstudios s WHERE s.ideSpest = :ideSpest"),
    @NamedQuery(name = "SprEstudios.findByTituloEspest", query = "SELECT s FROM SprEstudios s WHERE s.tituloEspest = :tituloEspest"),
    @NamedQuery(name = "SprEstudios.findByFechaInicioSpest", query = "SELECT s FROM SprEstudios s WHERE s.fechaInicioSpest = :fechaInicioSpest"),
    @NamedQuery(name = "SprEstudios.findByFechaFinSpest", query = "SELECT s FROM SprEstudios s WHERE s.fechaFinSpest = :fechaFinSpest"),
    @NamedQuery(name = "SprEstudios.findByPresenteSpest", query = "SELECT s FROM SprEstudios s WHERE s.presenteSpest = :presenteSpest"),
    @NamedQuery(name = "SprEstudios.findByPromedioSpest", query = "SELECT s FROM SprEstudios s WHERE s.promedioSpest = :promedioSpest"),
    @NamedQuery(name = "SprEstudios.findByMateriaAprobadoSpest", query = "SELECT s FROM SprEstudios s WHERE s.materiaAprobadoSpest = :materiaAprobadoSpest"),
    @NamedQuery(name = "SprEstudios.findByCantidadMateriaSpest", query = "SELECT s FROM SprEstudios s WHERE s.cantidadMateriaSpest = :cantidadMateriaSpest"),
    @NamedQuery(name = "SprEstudios.findByActivoSpest", query = "SELECT s FROM SprEstudios s WHERE s.activoSpest = :activoSpest"),
    @NamedQuery(name = "SprEstudios.findByUsuarioIngre", query = "SELECT s FROM SprEstudios s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprEstudios.findByFechaIngre", query = "SELECT s FROM SprEstudios s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprEstudios.findByHoraIngre", query = "SELECT s FROM SprEstudios s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprEstudios.findByUsuarioActua", query = "SELECT s FROM SprEstudios s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprEstudios.findByFechaActua", query = "SELECT s FROM SprEstudios s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprEstudios.findByHoraActua", query = "SELECT s FROM SprEstudios s WHERE s.horaActua = :horaActua")})
public class SprEstudios implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spest", nullable = false)
    private Integer ideSpest;
    @Size(max = 100)
    @Column(name = "titulo_espest", length = 100)
    private String tituloEspest;
    @Column(name = "fecha_inicio_spest")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioSpest;
    @Column(name = "fecha_fin_spest")
    @Temporal(TemporalType.DATE)
    private Date fechaFinSpest;
    @Column(name = "presente_spest")
    private Integer presenteSpest;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "promedio_spest", precision = 12, scale = 2)
    private BigDecimal promedioSpest;
    @Column(name = "materia_aprobado_spest")
    private Integer materiaAprobadoSpest;
    @Column(name = "cantidad_materia_spest")
    private Integer cantidadMateriaSpest;
    @Column(name = "activo_spest")
    private Boolean activoSpest;
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
    @JoinColumn(name = "ide_spese", referencedColumnName = "ide_spese")
    @ManyToOne
    private SprEstadoEstudio ideSpese;
    @JoinColumn(name = "ide_spbap", referencedColumnName = "ide_spbap")
    @ManyToOne
    private SprBasePostulante ideSpbap;
    @JoinColumn(name = "ide_gttes", referencedColumnName = "ide_gttes")
    @ManyToOne
    private GthTipoEspecialidad ideGttes;
    @JoinColumn(name = "ide_gtted", referencedColumnName = "ide_gtted")
    @ManyToOne
    private GthTipoEducacion ideGtted;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;

    public SprEstudios() {
    }

    public SprEstudios(Integer ideSpest) {
        this.ideSpest = ideSpest;
    }

    public Integer getIdeSpest() {
        return ideSpest;
    }

    public void setIdeSpest(Integer ideSpest) {
        this.ideSpest = ideSpest;
    }

    public String getTituloEspest() {
        return tituloEspest;
    }

    public void setTituloEspest(String tituloEspest) {
        this.tituloEspest = tituloEspest;
    }

    public Date getFechaInicioSpest() {
        return fechaInicioSpest;
    }

    public void setFechaInicioSpest(Date fechaInicioSpest) {
        this.fechaInicioSpest = fechaInicioSpest;
    }

    public Date getFechaFinSpest() {
        return fechaFinSpest;
    }

    public void setFechaFinSpest(Date fechaFinSpest) {
        this.fechaFinSpest = fechaFinSpest;
    }

    public Integer getPresenteSpest() {
        return presenteSpest;
    }

    public void setPresenteSpest(Integer presenteSpest) {
        this.presenteSpest = presenteSpest;
    }

    public BigDecimal getPromedioSpest() {
        return promedioSpest;
    }

    public void setPromedioSpest(BigDecimal promedioSpest) {
        this.promedioSpest = promedioSpest;
    }

    public Integer getMateriaAprobadoSpest() {
        return materiaAprobadoSpest;
    }

    public void setMateriaAprobadoSpest(Integer materiaAprobadoSpest) {
        this.materiaAprobadoSpest = materiaAprobadoSpest;
    }

    public Integer getCantidadMateriaSpest() {
        return cantidadMateriaSpest;
    }

    public void setCantidadMateriaSpest(Integer cantidadMateriaSpest) {
        this.cantidadMateriaSpest = cantidadMateriaSpest;
    }

    public Boolean getActivoSpest() {
        return activoSpest;
    }

    public void setActivoSpest(Boolean activoSpest) {
        this.activoSpest = activoSpest;
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

    public SprEstadoEstudio getIdeSpese() {
        return ideSpese;
    }

    public void setIdeSpese(SprEstadoEstudio ideSpese) {
        this.ideSpese = ideSpese;
    }

    public SprBasePostulante getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(SprBasePostulante ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    public GthTipoEspecialidad getIdeGttes() {
        return ideGttes;
    }

    public void setIdeGttes(GthTipoEspecialidad ideGttes) {
        this.ideGttes = ideGttes;
    }

    public GthTipoEducacion getIdeGtted() {
        return ideGtted;
    }

    public void setIdeGtted(GthTipoEducacion ideGtted) {
        this.ideGtted = ideGtted;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpest != null ? ideSpest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprEstudios)) {
            return false;
        }
        SprEstudios other = (SprEstudios) object;
        if ((this.ideSpest == null && other.ideSpest != null) || (this.ideSpest != null && !this.ideSpest.equals(other.ideSpest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprEstudios[ ideSpest=" + ideSpest + " ]";
    }
    
}
