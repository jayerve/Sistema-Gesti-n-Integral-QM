/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "gen_grupo_cargo_area", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenGrupoCargoArea.findAll", query = "SELECT g FROM GenGrupoCargoArea g"),
    @NamedQuery(name = "GenGrupoCargoArea.findByIdeGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.ideGegca = :ideGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByIdeGedep", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.ideGedep = :ideGedep"),
    @NamedQuery(name = "GenGrupoCargoArea.findByTituloCargoGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.tituloCargoGegca = :tituloCargoGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByMisionGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.misionGegca = :misionGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByDeAcuerdoaGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.deAcuerdoaGegca = :deAcuerdoaGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByConFindeGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.conFindeGegca = :conFindeGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByActivoGegca", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.activoGegca = :activoGegca"),
    @NamedQuery(name = "GenGrupoCargoArea.findByUsuarioIngre", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenGrupoCargoArea.findByFechaIngre", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenGrupoCargoArea.findByUsuarioActua", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenGrupoCargoArea.findByFechaActua", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenGrupoCargoArea.findByHoraIngre", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenGrupoCargoArea.findByHoraActua", query = "SELECT g FROM GenGrupoCargoArea g WHERE g.horaActua = :horaActua")})
public class GenGrupoCargoArea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gegca", nullable = false)
    private Integer ideGegca;
    @Column(name = "ide_gedep")
    private Integer ideGedep;
    @Size(max = 100)
    @Column(name = "titulo_cargo_gegca", length = 100)
    private String tituloCargoGegca;
    @Size(max = 4000)
    @Column(name = "mision_gegca", length = 4000)
    private String misionGegca;
    @Size(max = 4000)
    @Column(name = "de_acuerdoa_gegca", length = 4000)
    private String deAcuerdoaGegca;
    @Size(max = 4000)
    @Column(name = "con_finde_gegca", length = 4000)
    private String conFindeGegca;
    @Column(name = "activo_gegca")
    private Boolean activoGegca;
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
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideGegca")
    private List<CmpDetalleCompetencia> cmpDetalleCompetenciaList;
    @OneToMany(mappedBy = "ideGegca")
    private List<SprDetalleSolicitudPuesto> sprDetalleSolicitudPuestoList;
    @JoinColumns({
        @JoinColumn(name = "ide_gegro", referencedColumnName = "ide_gegro"),
        @JoinColumn(name = "ide_gecaf", referencedColumnName = "ide_gecaf")})
    @ManyToOne
    private GenGrupoCargo genGrupoCargo;

    public GenGrupoCargoArea() {
    }

    public GenGrupoCargoArea(Integer ideGegca) {
        this.ideGegca = ideGegca;
    }

    public Integer getIdeGegca() {
        return ideGegca;
    }

    public void setIdeGegca(Integer ideGegca) {
        this.ideGegca = ideGegca;
    }

    public Integer getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(Integer ideGedep) {
        this.ideGedep = ideGedep;
    }

    public String getTituloCargoGegca() {
        return tituloCargoGegca;
    }

    public void setTituloCargoGegca(String tituloCargoGegca) {
        this.tituloCargoGegca = tituloCargoGegca;
    }

    public String getMisionGegca() {
        return misionGegca;
    }

    public void setMisionGegca(String misionGegca) {
        this.misionGegca = misionGegca;
    }

    public String getDeAcuerdoaGegca() {
        return deAcuerdoaGegca;
    }

    public void setDeAcuerdoaGegca(String deAcuerdoaGegca) {
        this.deAcuerdoaGegca = deAcuerdoaGegca;
    }

    public String getConFindeGegca() {
        return conFindeGegca;
    }

    public void setConFindeGegca(String conFindeGegca) {
        this.conFindeGegca = conFindeGegca;
    }

    public Boolean getActivoGegca() {
        return activoGegca;
    }

    public void setActivoGegca(Boolean activoGegca) {
        this.activoGegca = activoGegca;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public List<CmpDetalleCompetencia> getCmpDetalleCompetenciaList() {
        return cmpDetalleCompetenciaList;
    }

    public void setCmpDetalleCompetenciaList(List<CmpDetalleCompetencia> cmpDetalleCompetenciaList) {
        this.cmpDetalleCompetenciaList = cmpDetalleCompetenciaList;
    }

    public List<SprDetalleSolicitudPuesto> getSprDetalleSolicitudPuestoList() {
        return sprDetalleSolicitudPuestoList;
    }

    public void setSprDetalleSolicitudPuestoList(List<SprDetalleSolicitudPuesto> sprDetalleSolicitudPuestoList) {
        this.sprDetalleSolicitudPuestoList = sprDetalleSolicitudPuestoList;
    }

    public GenGrupoCargo getGenGrupoCargo() {
        return genGrupoCargo;
    }

    public void setGenGrupoCargo(GenGrupoCargo genGrupoCargo) {
        this.genGrupoCargo = genGrupoCargo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGegca != null ? ideGegca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenGrupoCargoArea)) {
            return false;
        }
        GenGrupoCargoArea other = (GenGrupoCargoArea) object;
        if ((this.ideGegca == null && other.ideGegca != null) || (this.ideGegca != null && !this.ideGegca.equals(other.ideGegca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenGrupoCargoArea[ ideGegca=" + ideGegca + " ]";
    }
    
}
