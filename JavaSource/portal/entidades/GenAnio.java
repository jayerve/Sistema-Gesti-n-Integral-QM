/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "gen_anio", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenAnio.findAll", query = "SELECT g FROM GenAnio g"),
    @NamedQuery(name = "GenAnio.findByIdeGeani", query = "SELECT g FROM GenAnio g WHERE g.ideGeani = :ideGeani"),
    @NamedQuery(name = "GenAnio.findByDetalleGeani", query = "SELECT g FROM GenAnio g WHERE g.detalleGeani = :detalleGeani"),
    @NamedQuery(name = "GenAnio.findByActivoGeani", query = "SELECT g FROM GenAnio g WHERE g.activoGeani = :activoGeani"),
    @NamedQuery(name = "GenAnio.findByUsuarioIngre", query = "SELECT g FROM GenAnio g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenAnio.findByFechaIngre", query = "SELECT g FROM GenAnio g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenAnio.findByUsuarioActua", query = "SELECT g FROM GenAnio g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenAnio.findByFechaActua", query = "SELECT g FROM GenAnio g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenAnio.findByHoraIngre", query = "SELECT g FROM GenAnio g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenAnio.findByHoraActua", query = "SELECT g FROM GenAnio g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenAnio.findByBloqueadoGeani", query = "SELECT g FROM GenAnio g WHERE g.bloqueadoGeani = :bloqueadoGeani")})
public class GenAnio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geani", nullable = false)
    private Integer ideGeani;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "detalle_geani", nullable = false, length = 50)
    private String detalleGeani;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_geani", nullable = false)
    private boolean activoGeani;
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
    @Column(name = "bloqueado_geani")
    private Boolean bloqueadoGeani;
    @OneToMany(mappedBy = "ideGeani")
    private List<ContCierreAno> contCierreAnoList;
    @OneToMany(mappedBy = "ideGeani")
    private List<BodtBodega> bodtBodegaList;
    @OneToMany(mappedBy = "ideGeani")
    private List<PreTramite> preTramiteList;
    @OneToMany(mappedBy = "ideGeani")
    private List<BodtInventario> bodtInventarioList;
    @OneToMany(mappedBy = "genIdeGeani")
    private List<BodtInventario> bodtInventarioList1;
    @OneToMany(mappedBy = "ideGeani")
    private List<GthCuentaAnticipo> gthCuentaAnticipoList;
    @OneToMany(mappedBy = "ideGeani")
    private List<BodtConceptoEgreso> bodtConceptoEgresoList;
    @OneToMany(mappedBy = "ideGeani")
    private List<PrePac> prePacList;
    @OneToMany(mappedBy = "ideGeani")
    private List<BodtAplicaInventarioMaterial> bodtAplicaInventarioMaterialList;
    @OneToMany(mappedBy = "ideGeani")
    private List<SriConfiguracionGeneral> sriConfiguracionGeneralList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genAnio")
    private List<GenPeriodo> genPeriodoList;
    @OneToMany(mappedBy = "ideGeani")
    private List<PreAnual> preAnualList;
    @OneToMany(mappedBy = "ideGeani")
    private List<CppPresupuesto> cppPresupuestoList;
    @OneToMany(mappedBy = "ideGeani")
    private List<ContVigente> contVigenteList;
    @OneToMany(mappedBy = "ideGeani")
    private List<NrhRolEscenario> nrhRolEscenarioList;
    @OneToMany(mappedBy = "ideGeani")
    private List<PrePoa> prePoaList;

    public GenAnio() {
    }

    public GenAnio(Integer ideGeani) {
        this.ideGeani = ideGeani;
    }

    public GenAnio(Integer ideGeani, String detalleGeani, boolean activoGeani) {
        this.ideGeani = ideGeani;
        this.detalleGeani = detalleGeani;
        this.activoGeani = activoGeani;
    }

    public Integer getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(Integer ideGeani) {
        this.ideGeani = ideGeani;
    }

    public String getDetalleGeani() {
        return detalleGeani;
    }

    public void setDetalleGeani(String detalleGeani) {
        this.detalleGeani = detalleGeani;
    }

    public boolean getActivoGeani() {
        return activoGeani;
    }

    public void setActivoGeani(boolean activoGeani) {
        this.activoGeani = activoGeani;
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

    public Boolean getBloqueadoGeani() {
        return bloqueadoGeani;
    }

    public void setBloqueadoGeani(Boolean bloqueadoGeani) {
        this.bloqueadoGeani = bloqueadoGeani;
    }

    public List<ContCierreAno> getContCierreAnoList() {
        return contCierreAnoList;
    }

    public void setContCierreAnoList(List<ContCierreAno> contCierreAnoList) {
        this.contCierreAnoList = contCierreAnoList;
    }

    public List<BodtBodega> getBodtBodegaList() {
        return bodtBodegaList;
    }

    public void setBodtBodegaList(List<BodtBodega> bodtBodegaList) {
        this.bodtBodegaList = bodtBodegaList;
    }

    public List<PreTramite> getPreTramiteList() {
        return preTramiteList;
    }

    public void setPreTramiteList(List<PreTramite> preTramiteList) {
        this.preTramiteList = preTramiteList;
    }

    public List<BodtInventario> getBodtInventarioList() {
        return bodtInventarioList;
    }

    public void setBodtInventarioList(List<BodtInventario> bodtInventarioList) {
        this.bodtInventarioList = bodtInventarioList;
    }

    public List<BodtInventario> getBodtInventarioList1() {
        return bodtInventarioList1;
    }

    public void setBodtInventarioList1(List<BodtInventario> bodtInventarioList1) {
        this.bodtInventarioList1 = bodtInventarioList1;
    }

    public List<GthCuentaAnticipo> getGthCuentaAnticipoList() {
        return gthCuentaAnticipoList;
    }

    public void setGthCuentaAnticipoList(List<GthCuentaAnticipo> gthCuentaAnticipoList) {
        this.gthCuentaAnticipoList = gthCuentaAnticipoList;
    }

    public List<BodtConceptoEgreso> getBodtConceptoEgresoList() {
        return bodtConceptoEgresoList;
    }

    public void setBodtConceptoEgresoList(List<BodtConceptoEgreso> bodtConceptoEgresoList) {
        this.bodtConceptoEgresoList = bodtConceptoEgresoList;
    }

    public List<PrePac> getPrePacList() {
        return prePacList;
    }

    public void setPrePacList(List<PrePac> prePacList) {
        this.prePacList = prePacList;
    }

    public List<BodtAplicaInventarioMaterial> getBodtAplicaInventarioMaterialList() {
        return bodtAplicaInventarioMaterialList;
    }

    public void setBodtAplicaInventarioMaterialList(List<BodtAplicaInventarioMaterial> bodtAplicaInventarioMaterialList) {
        this.bodtAplicaInventarioMaterialList = bodtAplicaInventarioMaterialList;
    }

    public List<SriConfiguracionGeneral> getSriConfiguracionGeneralList() {
        return sriConfiguracionGeneralList;
    }

    public void setSriConfiguracionGeneralList(List<SriConfiguracionGeneral> sriConfiguracionGeneralList) {
        this.sriConfiguracionGeneralList = sriConfiguracionGeneralList;
    }

    public List<GenPeriodo> getGenPeriodoList() {
        return genPeriodoList;
    }

    public void setGenPeriodoList(List<GenPeriodo> genPeriodoList) {
        this.genPeriodoList = genPeriodoList;
    }

    public List<PreAnual> getPreAnualList() {
        return preAnualList;
    }

    public void setPreAnualList(List<PreAnual> preAnualList) {
        this.preAnualList = preAnualList;
    }

    public List<CppPresupuesto> getCppPresupuestoList() {
        return cppPresupuestoList;
    }

    public void setCppPresupuestoList(List<CppPresupuesto> cppPresupuestoList) {
        this.cppPresupuestoList = cppPresupuestoList;
    }

    public List<ContVigente> getContVigenteList() {
        return contVigenteList;
    }

    public void setContVigenteList(List<ContVigente> contVigenteList) {
        this.contVigenteList = contVigenteList;
    }

    public List<NrhRolEscenario> getNrhRolEscenarioList() {
        return nrhRolEscenarioList;
    }

    public void setNrhRolEscenarioList(List<NrhRolEscenario> nrhRolEscenarioList) {
        this.nrhRolEscenarioList = nrhRolEscenarioList;
    }

    public List<PrePoa> getPrePoaList() {
        return prePoaList;
    }

    public void setPrePoaList(List<PrePoa> prePoaList) {
        this.prePoaList = prePoaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeani != null ? ideGeani.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenAnio)) {
            return false;
        }
        GenAnio other = (GenAnio) object;
        if ((this.ideGeani == null && other.ideGeani != null) || (this.ideGeani != null && !this.ideGeani.equals(other.ideGeani))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenAnio[ ideGeani=" + ideGeani + " ]";
    }
    
}
