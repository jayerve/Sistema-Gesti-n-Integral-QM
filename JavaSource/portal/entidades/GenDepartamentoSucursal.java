/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "gen_departamento_sucursal", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenDepartamentoSucursal.findAll", query = "SELECT g FROM GenDepartamentoSucursal g"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByIdeSucu", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.genDepartamentoSucursalPK.ideSucu = :ideSucu"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByIdeGedep", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.genDepartamentoSucursalPK.ideGedep = :ideGedep"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByIdeGeare", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.genDepartamentoSucursalPK.ideGeare = :ideGeare"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByActivoGedes", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.activoGedes = :activoGedes"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByUsuarioIngre", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByFechaIngre", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByUsuarioActua", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByFechaActua", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByHoraIngre", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenDepartamentoSucursal.findByHoraActua", query = "SELECT g FROM GenDepartamentoSucursal g WHERE g.horaActua = :horaActua")})
public class GenDepartamentoSucursal implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GenDepartamentoSucursalPK genDepartamentoSucursalPK;
    @Column(name = "activo_gedes")
    private Boolean activoGedes;
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
    @OneToMany(mappedBy = "genDepartamentoSucursal")
    private List<SaoMatrizRiesgo> saoMatrizRiesgoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genDepartamentoSucursal")
    private List<GenPartidaGrupoCargo> genPartidaGrupoCargoList;

    public GenDepartamentoSucursal() {
    }

    public GenDepartamentoSucursal(GenDepartamentoSucursalPK genDepartamentoSucursalPK) {
        this.genDepartamentoSucursalPK = genDepartamentoSucursalPK;
    }

    public GenDepartamentoSucursal(int ideSucu, int ideGedep, int ideGeare) {
        this.genDepartamentoSucursalPK = new GenDepartamentoSucursalPK(ideSucu, ideGedep, ideGeare);
    }

    public GenDepartamentoSucursalPK getGenDepartamentoSucursalPK() {
        return genDepartamentoSucursalPK;
    }

    public void setGenDepartamentoSucursalPK(GenDepartamentoSucursalPK genDepartamentoSucursalPK) {
        this.genDepartamentoSucursalPK = genDepartamentoSucursalPK;
    }

    public Boolean getActivoGedes() {
        return activoGedes;
    }

    public void setActivoGedes(Boolean activoGedes) {
        this.activoGedes = activoGedes;
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

    public List<SaoMatrizRiesgo> getSaoMatrizRiesgoList() {
        return saoMatrizRiesgoList;
    }

    public void setSaoMatrizRiesgoList(List<SaoMatrizRiesgo> saoMatrizRiesgoList) {
        this.saoMatrizRiesgoList = saoMatrizRiesgoList;
    }

    public List<GenPartidaGrupoCargo> getGenPartidaGrupoCargoList() {
        return genPartidaGrupoCargoList;
    }

    public void setGenPartidaGrupoCargoList(List<GenPartidaGrupoCargo> genPartidaGrupoCargoList) {
        this.genPartidaGrupoCargoList = genPartidaGrupoCargoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genDepartamentoSucursalPK != null ? genDepartamentoSucursalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenDepartamentoSucursal)) {
            return false;
        }
        GenDepartamentoSucursal other = (GenDepartamentoSucursal) object;
        if ((this.genDepartamentoSucursalPK == null && other.genDepartamentoSucursalPK != null) || (this.genDepartamentoSucursalPK != null && !this.genDepartamentoSucursalPK.equals(other.genDepartamentoSucursalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenDepartamentoSucursal[ genDepartamentoSucursalPK=" + genDepartamentoSucursalPK + " ]";
    }
    
}
