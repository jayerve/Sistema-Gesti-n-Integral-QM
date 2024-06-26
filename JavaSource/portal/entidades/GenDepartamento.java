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
@Table(name = "gen_departamento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenDepartamento.findAll", query = "SELECT g FROM GenDepartamento g"),
    @NamedQuery(name = "GenDepartamento.findByIdeGedep", query = "SELECT g FROM GenDepartamento g WHERE g.ideGedep = :ideGedep"),
    @NamedQuery(name = "GenDepartamento.findByIdeGeare", query = "SELECT g FROM GenDepartamento g WHERE g.ideGeare = :ideGeare"),
    @NamedQuery(name = "GenDepartamento.findByDetalleGedep", query = "SELECT g FROM GenDepartamento g WHERE g.detalleGedep = :detalleGedep"),
    @NamedQuery(name = "GenDepartamento.findByTipoGedep", query = "SELECT g FROM GenDepartamento g WHERE g.tipoGedep = :tipoGedep"),
    @NamedQuery(name = "GenDepartamento.findByNivelGedep", query = "SELECT g FROM GenDepartamento g WHERE g.nivelGedep = :nivelGedep"),
    @NamedQuery(name = "GenDepartamento.findByNivelOrganicoGedep", query = "SELECT g FROM GenDepartamento g WHERE g.nivelOrganicoGedep = :nivelOrganicoGedep"),
    @NamedQuery(name = "GenDepartamento.findByPosicionHijosGedep", query = "SELECT g FROM GenDepartamento g WHERE g.posicionHijosGedep = :posicionHijosGedep"),
    @NamedQuery(name = "GenDepartamento.findByOrdenGedep", query = "SELECT g FROM GenDepartamento g WHERE g.ordenGedep = :ordenGedep"),
    @NamedQuery(name = "GenDepartamento.findByOrdenImprimeGedep", query = "SELECT g FROM GenDepartamento g WHERE g.ordenImprimeGedep = :ordenImprimeGedep"),
    @NamedQuery(name = "GenDepartamento.findByActivoGedep", query = "SELECT g FROM GenDepartamento g WHERE g.activoGedep = :activoGedep"),
    @NamedQuery(name = "GenDepartamento.findByUsuarioIngre", query = "SELECT g FROM GenDepartamento g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenDepartamento.findByFechaIngre", query = "SELECT g FROM GenDepartamento g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenDepartamento.findByUsuarioActua", query = "SELECT g FROM GenDepartamento g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenDepartamento.findByFechaActua", query = "SELECT g FROM GenDepartamento g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenDepartamento.findByHoraIngre", query = "SELECT g FROM GenDepartamento g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenDepartamento.findByHoraActua", query = "SELECT g FROM GenDepartamento g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenDepartamento.findByAbreviaturaGedep", query = "SELECT g FROM GenDepartamento g WHERE g.abreviaturaGedep = :abreviaturaGedep")})
public class GenDepartamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gedep", nullable = false)
    private Integer ideGedep;
    @Column(name = "ide_geare")
    private Integer ideGeare;
    @Size(max = 100)
    @Column(name = "detalle_gedep", length = 100)
    private String detalleGedep;
    @Size(max = 50)
    @Column(name = "tipo_gedep", length = 50)
    private String tipoGedep;
    @Column(name = "nivel_gedep")
    private Integer nivelGedep;
    @Column(name = "nivel_organico_gedep")
    private Integer nivelOrganicoGedep;
    @Size(max = 50)
    @Column(name = "posicion_hijos_gedep", length = 50)
    private String posicionHijosGedep;
    @Column(name = "orden_gedep")
    private Integer ordenGedep;
    @Column(name = "orden_imprime_gedep")
    private Integer ordenImprimeGedep;
    @Column(name = "activo_gedep")
    private Boolean activoGedep;
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
    @Size(max = 20)
    @Column(name = "abreviatura_gedep", length = 20)
    private String abreviaturaGedep;
    @OneToMany(mappedBy = "genIdeGedep")
    private List<GenDepartamento> genDepartamentoList;
    @JoinColumn(name = "gen_ide_gedep", referencedColumnName = "ide_gedep")
    @ManyToOne
    private GenDepartamento genIdeGedep;

    public GenDepartamento() {
    }

    public GenDepartamento(Integer ideGedep) {
        this.ideGedep = ideGedep;
    }

    public Integer getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(Integer ideGedep) {
        this.ideGedep = ideGedep;
    }

    public Integer getIdeGeare() {
        return ideGeare;
    }

    public void setIdeGeare(Integer ideGeare) {
        this.ideGeare = ideGeare;
    }

    public String getDetalleGedep() {
        return detalleGedep;
    }

    public void setDetalleGedep(String detalleGedep) {
        this.detalleGedep = detalleGedep;
    }

    public String getTipoGedep() {
        return tipoGedep;
    }

    public void setTipoGedep(String tipoGedep) {
        this.tipoGedep = tipoGedep;
    }

    public Integer getNivelGedep() {
        return nivelGedep;
    }

    public void setNivelGedep(Integer nivelGedep) {
        this.nivelGedep = nivelGedep;
    }

    public Integer getNivelOrganicoGedep() {
        return nivelOrganicoGedep;
    }

    public void setNivelOrganicoGedep(Integer nivelOrganicoGedep) {
        this.nivelOrganicoGedep = nivelOrganicoGedep;
    }

    public String getPosicionHijosGedep() {
        return posicionHijosGedep;
    }

    public void setPosicionHijosGedep(String posicionHijosGedep) {
        this.posicionHijosGedep = posicionHijosGedep;
    }

    public Integer getOrdenGedep() {
        return ordenGedep;
    }

    public void setOrdenGedep(Integer ordenGedep) {
        this.ordenGedep = ordenGedep;
    }

    public Integer getOrdenImprimeGedep() {
        return ordenImprimeGedep;
    }

    public void setOrdenImprimeGedep(Integer ordenImprimeGedep) {
        this.ordenImprimeGedep = ordenImprimeGedep;
    }

    public Boolean getActivoGedep() {
        return activoGedep;
    }

    public void setActivoGedep(Boolean activoGedep) {
        this.activoGedep = activoGedep;
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

    public String getAbreviaturaGedep() {
        return abreviaturaGedep;
    }

    public void setAbreviaturaGedep(String abreviaturaGedep) {
        this.abreviaturaGedep = abreviaturaGedep;
    }

    public List<GenDepartamento> getGenDepartamentoList() {
        return genDepartamentoList;
    }

    public void setGenDepartamentoList(List<GenDepartamento> genDepartamentoList) {
        this.genDepartamentoList = genDepartamentoList;
    }

    public GenDepartamento getGenIdeGedep() {
        return genIdeGedep;
    }

    public void setGenIdeGedep(GenDepartamento genIdeGedep) {
        this.genIdeGedep = genIdeGedep;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGedep != null ? ideGedep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenDepartamento)) {
            return false;
        }
        GenDepartamento other = (GenDepartamento) object;
        if ((this.ideGedep == null && other.ideGedep != null) || (this.ideGedep != null && !this.ideGedep.equals(other.ideGedep))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenDepartamento[ ideGedep=" + ideGedep + " ]";
    }
    
}
