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
@Table(name = "cpp_perfil", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppPerfil.findAll", query = "SELECT c FROM CppPerfil c"),
    @NamedQuery(name = "CppPerfil.findByIdeCpper", query = "SELECT c FROM CppPerfil c WHERE c.ideCpper = :ideCpper"),
    @NamedQuery(name = "CppPerfil.findByIdeGedep", query = "SELECT c FROM CppPerfil c WHERE c.ideGedep = :ideGedep"),
    @NamedQuery(name = "CppPerfil.findByActivoCpper", query = "SELECT c FROM CppPerfil c WHERE c.activoCpper = :activoCpper"),
    @NamedQuery(name = "CppPerfil.findByUsuarioIngre", query = "SELECT c FROM CppPerfil c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppPerfil.findByFechaIngre", query = "SELECT c FROM CppPerfil c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppPerfil.findByHoraIngre", query = "SELECT c FROM CppPerfil c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppPerfil.findByUsuarioActua", query = "SELECT c FROM CppPerfil c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppPerfil.findByFechaActua", query = "SELECT c FROM CppPerfil c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppPerfil.findByHoraActua", query = "SELECT c FROM CppPerfil c WHERE c.horaActua = :horaActua")})
public class CppPerfil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpper", nullable = false)
    private Integer ideCpper;
    @Column(name = "ide_gedep")
    private Integer ideGedep;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpper", nullable = false)
    private boolean activoCpper;
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
    @OneToMany(mappedBy = "ideCpper")
    private List<CppPlanCapacitacion> cppPlanCapacitacionList;
    @JoinColumn(name = "ide_geuna", referencedColumnName = "ide_geuna")
    @ManyToOne
    private GenUnidadAdministrativa ideGeuna;
    @JoinColumns({
        @JoinColumn(name = "ide_gegro", referencedColumnName = "ide_gegro"),
        @JoinColumn(name = "ide_gecaf", referencedColumnName = "ide_gecaf")})
    @ManyToOne
    private GenGrupoCargo genGrupoCargo;
    @JoinColumn(name = "ide_cppre", referencedColumnName = "ide_cppre")
    @ManyToOne
    private CppPresupuesto ideCppre;
    @OneToMany(mappedBy = "ideCpper")
    private List<CppCapacitaRequerida> cppCapacitaRequeridaList;

    public CppPerfil() {
    }

    public CppPerfil(Integer ideCpper) {
        this.ideCpper = ideCpper;
    }

    public CppPerfil(Integer ideCpper, boolean activoCpper) {
        this.ideCpper = ideCpper;
        this.activoCpper = activoCpper;
    }

    public Integer getIdeCpper() {
        return ideCpper;
    }

    public void setIdeCpper(Integer ideCpper) {
        this.ideCpper = ideCpper;
    }

    public Integer getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(Integer ideGedep) {
        this.ideGedep = ideGedep;
    }

    public boolean getActivoCpper() {
        return activoCpper;
    }

    public void setActivoCpper(boolean activoCpper) {
        this.activoCpper = activoCpper;
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

    public List<CppPlanCapacitacion> getCppPlanCapacitacionList() {
        return cppPlanCapacitacionList;
    }

    public void setCppPlanCapacitacionList(List<CppPlanCapacitacion> cppPlanCapacitacionList) {
        this.cppPlanCapacitacionList = cppPlanCapacitacionList;
    }

    public GenUnidadAdministrativa getIdeGeuna() {
        return ideGeuna;
    }

    public void setIdeGeuna(GenUnidadAdministrativa ideGeuna) {
        this.ideGeuna = ideGeuna;
    }

    public GenGrupoCargo getGenGrupoCargo() {
        return genGrupoCargo;
    }

    public void setGenGrupoCargo(GenGrupoCargo genGrupoCargo) {
        this.genGrupoCargo = genGrupoCargo;
    }

    public CppPresupuesto getIdeCppre() {
        return ideCppre;
    }

    public void setIdeCppre(CppPresupuesto ideCppre) {
        this.ideCppre = ideCppre;
    }

    public List<CppCapacitaRequerida> getCppCapacitaRequeridaList() {
        return cppCapacitaRequeridaList;
    }

    public void setCppCapacitaRequeridaList(List<CppCapacitaRequerida> cppCapacitaRequeridaList) {
        this.cppCapacitaRequeridaList = cppCapacitaRequeridaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpper != null ? ideCpper.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppPerfil)) {
            return false;
        }
        CppPerfil other = (CppPerfil) object;
        if ((this.ideCpper == null && other.ideCpper != null) || (this.ideCpper != null && !this.ideCpper.equals(other.ideCpper))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppPerfil[ ideCpper=" + ideCpper + " ]";
    }
    
}
