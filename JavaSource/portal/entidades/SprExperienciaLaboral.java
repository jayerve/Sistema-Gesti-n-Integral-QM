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
@Table(name = "spr_experiencia_laboral", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprExperienciaLaboral.findAll", query = "SELECT s FROM SprExperienciaLaboral s"),
    @NamedQuery(name = "SprExperienciaLaboral.findByIdeSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.ideSpexl = :ideSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByTituloPuestoSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.tituloPuestoSpexl = :tituloPuestoSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByEmpresaSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.empresaSpexl = :empresaSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByFechaInicioSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.fechaInicioSpexl = :fechaInicioSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByFechaFinSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.fechaFinSpexl = :fechaFinSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByPresenteSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.presenteSpexl = :presenteSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByResponsabilidadesSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.responsabilidadesSpexl = :responsabilidadesSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByPersonasCargoSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.personasCargoSpexl = :personasCargoSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByManejoPresupuestoSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.manejoPresupuestoSpexl = :manejoPresupuestoSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByActivoSpexl", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.activoSpexl = :activoSpexl"),
    @NamedQuery(name = "SprExperienciaLaboral.findByUsuarioIngre", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprExperienciaLaboral.findByFechaIngre", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprExperienciaLaboral.findByHoraIngre", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprExperienciaLaboral.findByUsuarioActua", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprExperienciaLaboral.findByFechaActua", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprExperienciaLaboral.findByHoraActua", query = "SELECT s FROM SprExperienciaLaboral s WHERE s.horaActua = :horaActua")})
public class SprExperienciaLaboral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spexl", nullable = false)
    private Integer ideSpexl;
    @Size(max = 100)
    @Column(name = "titulo_puesto_spexl", length = 100)
    private String tituloPuestoSpexl;
    @Size(max = 100)
    @Column(name = "empresa_spexl", length = 100)
    private String empresaSpexl;
    @Column(name = "fecha_inicio_spexl")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioSpexl;
    @Column(name = "fecha_fin_spexl")
    @Temporal(TemporalType.DATE)
    private Date fechaFinSpexl;
    @Column(name = "presente_spexl")
    private Integer presenteSpexl;
    @Size(max = 4000)
    @Column(name = "responsabilidades_spexl", length = 4000)
    private String responsabilidadesSpexl;
    @Column(name = "personas_cargo_spexl")
    private Integer personasCargoSpexl;
    @Column(name = "manejo_presupuesto_spexl")
    private Integer manejoPresupuestoSpexl;
    @Column(name = "activo_spexl")
    private Boolean activoSpexl;
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
    @JoinColumn(name = "ide_spsub", referencedColumnName = "ide_spsub")
    @ManyToOne
    private SprSubarea ideSpsub;
    @JoinColumn(name = "ide_spind", referencedColumnName = "ide_spind")
    @ManyToOne
    private SprIndustria ideSpind;
    @JoinColumn(name = "ide_spbap", referencedColumnName = "ide_spbap")
    @ManyToOne
    private SprBasePostulante ideSpbap;
    @JoinColumn(name = "ide_spare", referencedColumnName = "ide_spare")
    @ManyToOne
    private SprArea ideSpare;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;

    public SprExperienciaLaboral() {
    }

    public SprExperienciaLaboral(Integer ideSpexl) {
        this.ideSpexl = ideSpexl;
    }

    public Integer getIdeSpexl() {
        return ideSpexl;
    }

    public void setIdeSpexl(Integer ideSpexl) {
        this.ideSpexl = ideSpexl;
    }

    public String getTituloPuestoSpexl() {
        return tituloPuestoSpexl;
    }

    public void setTituloPuestoSpexl(String tituloPuestoSpexl) {
        this.tituloPuestoSpexl = tituloPuestoSpexl;
    }

    public String getEmpresaSpexl() {
        return empresaSpexl;
    }

    public void setEmpresaSpexl(String empresaSpexl) {
        this.empresaSpexl = empresaSpexl;
    }

    public Date getFechaInicioSpexl() {
        return fechaInicioSpexl;
    }

    public void setFechaInicioSpexl(Date fechaInicioSpexl) {
        this.fechaInicioSpexl = fechaInicioSpexl;
    }

    public Date getFechaFinSpexl() {
        return fechaFinSpexl;
    }

    public void setFechaFinSpexl(Date fechaFinSpexl) {
        this.fechaFinSpexl = fechaFinSpexl;
    }

    public Integer getPresenteSpexl() {
        return presenteSpexl;
    }

    public void setPresenteSpexl(Integer presenteSpexl) {
        this.presenteSpexl = presenteSpexl;
    }

    public String getResponsabilidadesSpexl() {
        return responsabilidadesSpexl;
    }

    public void setResponsabilidadesSpexl(String responsabilidadesSpexl) {
        this.responsabilidadesSpexl = responsabilidadesSpexl;
    }

    public Integer getPersonasCargoSpexl() {
        return personasCargoSpexl;
    }

    public void setPersonasCargoSpexl(Integer personasCargoSpexl) {
        this.personasCargoSpexl = personasCargoSpexl;
    }

    public Integer getManejoPresupuestoSpexl() {
        return manejoPresupuestoSpexl;
    }

    public void setManejoPresupuestoSpexl(Integer manejoPresupuestoSpexl) {
        this.manejoPresupuestoSpexl = manejoPresupuestoSpexl;
    }

    public Boolean getActivoSpexl() {
        return activoSpexl;
    }

    public void setActivoSpexl(Boolean activoSpexl) {
        this.activoSpexl = activoSpexl;
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

    public SprSubarea getIdeSpsub() {
        return ideSpsub;
    }

    public void setIdeSpsub(SprSubarea ideSpsub) {
        this.ideSpsub = ideSpsub;
    }

    public SprIndustria getIdeSpind() {
        return ideSpind;
    }

    public void setIdeSpind(SprIndustria ideSpind) {
        this.ideSpind = ideSpind;
    }

    public SprBasePostulante getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(SprBasePostulante ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    public SprArea getIdeSpare() {
        return ideSpare;
    }

    public void setIdeSpare(SprArea ideSpare) {
        this.ideSpare = ideSpare;
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
        hash += (ideSpexl != null ? ideSpexl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprExperienciaLaboral)) {
            return false;
        }
        SprExperienciaLaboral other = (SprExperienciaLaboral) object;
        if ((this.ideSpexl == null && other.ideSpexl != null) || (this.ideSpexl != null && !this.ideSpexl.equals(other.ideSpexl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprExperienciaLaboral[ ideSpexl=" + ideSpexl + " ]";
    }
    
}
