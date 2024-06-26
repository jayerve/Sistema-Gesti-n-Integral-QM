/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "spr_factor", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprFactor.findAll", query = "SELECT s FROM SprFactor s"),
    @NamedQuery(name = "SprFactor.findByIdeSpfac", query = "SELECT s FROM SprFactor s WHERE s.ideSpfac = :ideSpfac"),
    @NamedQuery(name = "SprFactor.findByDetalleSpfac", query = "SELECT s FROM SprFactor s WHERE s.detalleSpfac = :detalleSpfac"),
    @NamedQuery(name = "SprFactor.findByExtracurricularSpfac", query = "SELECT s FROM SprFactor s WHERE s.extracurricularSpfac = :extracurricularSpfac"),
    @NamedQuery(name = "SprFactor.findByPuntosSpfac", query = "SELECT s FROM SprFactor s WHERE s.puntosSpfac = :puntosSpfac"),
    @NamedQuery(name = "SprFactor.findByTipoSpfac", query = "SELECT s FROM SprFactor s WHERE s.tipoSpfac = :tipoSpfac"),
    @NamedQuery(name = "SprFactor.findByActivoSpfac", query = "SELECT s FROM SprFactor s WHERE s.activoSpfac = :activoSpfac"),
    @NamedQuery(name = "SprFactor.findByUsuarioIngre", query = "SELECT s FROM SprFactor s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprFactor.findByFechaIngre", query = "SELECT s FROM SprFactor s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprFactor.findByHoraIngre", query = "SELECT s FROM SprFactor s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprFactor.findByUsuarioActua", query = "SELECT s FROM SprFactor s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprFactor.findByFechaActua", query = "SELECT s FROM SprFactor s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprFactor.findByHoraActua", query = "SELECT s FROM SprFactor s WHERE s.horaActua = :horaActua")})
public class SprFactor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spfac", nullable = false)
    private Integer ideSpfac;
    @Size(max = 100)
    @Column(name = "detalle_spfac", length = 100)
    private String detalleSpfac;
    @Column(name = "extracurricular_spfac")
    private Integer extracurricularSpfac;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "puntos_spfac", precision = 12, scale = 2)
    private BigDecimal puntosSpfac;
    @Column(name = "tipo_spfac")
    private Integer tipoSpfac;
    @Column(name = "activo_spfac")
    private Boolean activoSpfac;
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
    @OneToMany(mappedBy = "ideSpfac")
    private List<SprFactorPonderacion> sprFactorPonderacionList;
    @OneToMany(mappedBy = "ideSpfac")
    private List<SprResponsableCalificacion> sprResponsableCalificacionList;
    @OneToMany(mappedBy = "ideSpfac")
    private List<CmpFactorCompetencia> cmpFactorCompetenciaList;
    @OneToMany(mappedBy = "sprIdeSpfac")
    private List<SprFactor> sprFactorList;
    @JoinColumn(name = "spr_ide_spfac", referencedColumnName = "ide_spfac")
    @ManyToOne
    private SprFactor sprIdeSpfac;
    @OneToMany(mappedBy = "ideSpfac")
    private List<SprResultado> sprResultadoList;
    @OneToMany(mappedBy = "ideSpfac")
    private List<SprCalificacion> sprCalificacionList;
    @OneToMany(mappedBy = "ideSpfac")
    private List<SprGrupoFactor> sprGrupoFactorList;

    public SprFactor() {
    }

    public SprFactor(Integer ideSpfac) {
        this.ideSpfac = ideSpfac;
    }

    public Integer getIdeSpfac() {
        return ideSpfac;
    }

    public void setIdeSpfac(Integer ideSpfac) {
        this.ideSpfac = ideSpfac;
    }

    public String getDetalleSpfac() {
        return detalleSpfac;
    }

    public void setDetalleSpfac(String detalleSpfac) {
        this.detalleSpfac = detalleSpfac;
    }

    public Integer getExtracurricularSpfac() {
        return extracurricularSpfac;
    }

    public void setExtracurricularSpfac(Integer extracurricularSpfac) {
        this.extracurricularSpfac = extracurricularSpfac;
    }

    public BigDecimal getPuntosSpfac() {
        return puntosSpfac;
    }

    public void setPuntosSpfac(BigDecimal puntosSpfac) {
        this.puntosSpfac = puntosSpfac;
    }

    public Integer getTipoSpfac() {
        return tipoSpfac;
    }

    public void setTipoSpfac(Integer tipoSpfac) {
        this.tipoSpfac = tipoSpfac;
    }

    public Boolean getActivoSpfac() {
        return activoSpfac;
    }

    public void setActivoSpfac(Boolean activoSpfac) {
        this.activoSpfac = activoSpfac;
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

    public List<SprFactorPonderacion> getSprFactorPonderacionList() {
        return sprFactorPonderacionList;
    }

    public void setSprFactorPonderacionList(List<SprFactorPonderacion> sprFactorPonderacionList) {
        this.sprFactorPonderacionList = sprFactorPonderacionList;
    }

    public List<SprResponsableCalificacion> getSprResponsableCalificacionList() {
        return sprResponsableCalificacionList;
    }

    public void setSprResponsableCalificacionList(List<SprResponsableCalificacion> sprResponsableCalificacionList) {
        this.sprResponsableCalificacionList = sprResponsableCalificacionList;
    }

    public List<CmpFactorCompetencia> getCmpFactorCompetenciaList() {
        return cmpFactorCompetenciaList;
    }

    public void setCmpFactorCompetenciaList(List<CmpFactorCompetencia> cmpFactorCompetenciaList) {
        this.cmpFactorCompetenciaList = cmpFactorCompetenciaList;
    }

    public List<SprFactor> getSprFactorList() {
        return sprFactorList;
    }

    public void setSprFactorList(List<SprFactor> sprFactorList) {
        this.sprFactorList = sprFactorList;
    }

    public SprFactor getSprIdeSpfac() {
        return sprIdeSpfac;
    }

    public void setSprIdeSpfac(SprFactor sprIdeSpfac) {
        this.sprIdeSpfac = sprIdeSpfac;
    }

    public List<SprResultado> getSprResultadoList() {
        return sprResultadoList;
    }

    public void setSprResultadoList(List<SprResultado> sprResultadoList) {
        this.sprResultadoList = sprResultadoList;
    }

    public List<SprCalificacion> getSprCalificacionList() {
        return sprCalificacionList;
    }

    public void setSprCalificacionList(List<SprCalificacion> sprCalificacionList) {
        this.sprCalificacionList = sprCalificacionList;
    }

    public List<SprGrupoFactor> getSprGrupoFactorList() {
        return sprGrupoFactorList;
    }

    public void setSprGrupoFactorList(List<SprGrupoFactor> sprGrupoFactorList) {
        this.sprGrupoFactorList = sprGrupoFactorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpfac != null ? ideSpfac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprFactor)) {
            return false;
        }
        SprFactor other = (SprFactor) object;
        if ((this.ideSpfac == null && other.ideSpfac != null) || (this.ideSpfac != null && !this.ideSpfac.equals(other.ideSpfac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprFactor[ ideSpfac=" + ideSpfac + " ]";
    }
    
}
