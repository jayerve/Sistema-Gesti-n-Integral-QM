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
@Table(name = "gen_institucion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenInstitucion.findAll", query = "SELECT g FROM GenInstitucion g"),
    @NamedQuery(name = "GenInstitucion.findByIdeGeins", query = "SELECT g FROM GenInstitucion g WHERE g.ideGeins = :ideGeins"),
    @NamedQuery(name = "GenInstitucion.findByDetalleGeins", query = "SELECT g FROM GenInstitucion g WHERE g.detalleGeins = :detalleGeins"),
    @NamedQuery(name = "GenInstitucion.findByCodigoBancoGeins", query = "SELECT g FROM GenInstitucion g WHERE g.codigoBancoGeins = :codigoBancoGeins"),
    @NamedQuery(name = "GenInstitucion.findByActivoGeins", query = "SELECT g FROM GenInstitucion g WHERE g.activoGeins = :activoGeins"),
    @NamedQuery(name = "GenInstitucion.findByUsuarioIngre", query = "SELECT g FROM GenInstitucion g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenInstitucion.findByFechaIngre", query = "SELECT g FROM GenInstitucion g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenInstitucion.findByUsuarioActua", query = "SELECT g FROM GenInstitucion g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenInstitucion.findByFechaActua", query = "SELECT g FROM GenInstitucion g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenInstitucion.findByHoraIngre", query = "SELECT g FROM GenInstitucion g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenInstitucion.findByHoraActua", query = "SELECT g FROM GenInstitucion g WHERE g.horaActua = :horaActua")})
public class GenInstitucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geins", nullable = false)
    private Integer ideGeins;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "detalle_geins", nullable = false, length = 50)
    private String detalleGeins;
    @Size(max = 10)
    @Column(name = "codigo_banco_geins", length = 10)
    private String codigoBancoGeins;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_geins", nullable = false)
    private boolean activoGeins;
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
    @OneToMany(mappedBy = "ideGeins")
    private List<ContConvenio> contConvenioList;
    @OneToMany(mappedBy = "ideGeins")
    private List<CppCapacitacion> cppCapacitacionList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeins")
    private List<GthSeguroVida> gthSeguroVidaList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthTransporteViatico> gthTransporteViaticoList;
    @OneToMany(mappedBy = "ideGeins")
    private List<TesPoliza> tesPolizaList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthIdiomaEmpleado> gthIdiomaEmpleadoList;
    @OneToMany(mappedBy = "ideGeins")
    private List<FacFactura> facFacturaList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthMembresiaEmpleado> gthMembresiaEmpleadoList;
    @JoinColumn(name = "ide_getii", referencedColumnName = "ide_getii", nullable = false)
    @ManyToOne(optional = false)
    private GenTipoInstitucion ideGetii;
    @OneToMany(mappedBy = "genIdeGeins")
    private List<GenInstitucion> genInstitucionList;
    @JoinColumn(name = "gen_ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion genIdeGeins;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeins")
    private List<NrhPrecancelacion> nrhPrecancelacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGeins")
    private List<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoList;
    @OneToMany(mappedBy = "ideGeins")
    private List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList;
    @OneToMany(mappedBy = "ideGeins")
    private List<NrhAnticipoAbono> nrhAnticipoAbonoList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthCapacitacionEmpleado> gthCapacitacionEmpleadoList;
    @OneToMany(mappedBy = "ideGeins")
    private List<SprEstudios> sprEstudiosList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthEducacionEmpleado> gthEducacionEmpleadoList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthExperienciaLaboralEmplea> gthExperienciaLaboralEmpleaList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthViaticos> gthViaticosList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthVehiculoEmpleado> gthVehiculoEmpleadoList;
    @OneToMany(mappedBy = "ideGeins")
    private List<GthInversionEmpleado> gthInversionEmpleadoList;
    @OneToMany(mappedBy = "ideGeins")
    private List<SprCapacitacion> sprCapacitacionList;
    @OneToMany(mappedBy = "ideGeins")
    private List<NrhRetencionJudicial> nrhRetencionJudicialList;

    public GenInstitucion() {
    }

    public GenInstitucion(Integer ideGeins) {
        this.ideGeins = ideGeins;
    }

    public GenInstitucion(Integer ideGeins, String detalleGeins, boolean activoGeins) {
        this.ideGeins = ideGeins;
        this.detalleGeins = detalleGeins;
        this.activoGeins = activoGeins;
    }

    public Integer getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(Integer ideGeins) {
        this.ideGeins = ideGeins;
    }

    public String getDetalleGeins() {
        return detalleGeins;
    }

    public void setDetalleGeins(String detalleGeins) {
        this.detalleGeins = detalleGeins;
    }

    public String getCodigoBancoGeins() {
        return codigoBancoGeins;
    }

    public void setCodigoBancoGeins(String codigoBancoGeins) {
        this.codigoBancoGeins = codigoBancoGeins;
    }

    public boolean getActivoGeins() {
        return activoGeins;
    }

    public void setActivoGeins(boolean activoGeins) {
        this.activoGeins = activoGeins;
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

    public List<ContConvenio> getContConvenioList() {
        return contConvenioList;
    }

    public void setContConvenioList(List<ContConvenio> contConvenioList) {
        this.contConvenioList = contConvenioList;
    }

    public List<CppCapacitacion> getCppCapacitacionList() {
        return cppCapacitacionList;
    }

    public void setCppCapacitacionList(List<CppCapacitacion> cppCapacitacionList) {
        this.cppCapacitacionList = cppCapacitacionList;
    }

    public List<GenDetalleEmpleadoDepartame> getGenDetalleEmpleadoDepartameList() {
        return genDetalleEmpleadoDepartameList;
    }

    public void setGenDetalleEmpleadoDepartameList(List<GenDetalleEmpleadoDepartame> genDetalleEmpleadoDepartameList) {
        this.genDetalleEmpleadoDepartameList = genDetalleEmpleadoDepartameList;
    }

    public List<GthEndeudamientoEmpleado> getGthEndeudamientoEmpleadoList() {
        return gthEndeudamientoEmpleadoList;
    }

    public void setGthEndeudamientoEmpleadoList(List<GthEndeudamientoEmpleado> gthEndeudamientoEmpleadoList) {
        this.gthEndeudamientoEmpleadoList = gthEndeudamientoEmpleadoList;
    }

    public List<GthSeguroVida> getGthSeguroVidaList() {
        return gthSeguroVidaList;
    }

    public void setGthSeguroVidaList(List<GthSeguroVida> gthSeguroVidaList) {
        this.gthSeguroVidaList = gthSeguroVidaList;
    }

    public List<GthTransporteViatico> getGthTransporteViaticoList() {
        return gthTransporteViaticoList;
    }

    public void setGthTransporteViaticoList(List<GthTransporteViatico> gthTransporteViaticoList) {
        this.gthTransporteViaticoList = gthTransporteViaticoList;
    }

    public List<TesPoliza> getTesPolizaList() {
        return tesPolizaList;
    }

    public void setTesPolizaList(List<TesPoliza> tesPolizaList) {
        this.tesPolizaList = tesPolizaList;
    }

    public List<GthIdiomaEmpleado> getGthIdiomaEmpleadoList() {
        return gthIdiomaEmpleadoList;
    }

    public void setGthIdiomaEmpleadoList(List<GthIdiomaEmpleado> gthIdiomaEmpleadoList) {
        this.gthIdiomaEmpleadoList = gthIdiomaEmpleadoList;
    }

    public List<FacFactura> getFacFacturaList() {
        return facFacturaList;
    }

    public void setFacFacturaList(List<FacFactura> facFacturaList) {
        this.facFacturaList = facFacturaList;
    }

    public List<GthMembresiaEmpleado> getGthMembresiaEmpleadoList() {
        return gthMembresiaEmpleadoList;
    }

    public void setGthMembresiaEmpleadoList(List<GthMembresiaEmpleado> gthMembresiaEmpleadoList) {
        this.gthMembresiaEmpleadoList = gthMembresiaEmpleadoList;
    }

    public GenTipoInstitucion getIdeGetii() {
        return ideGetii;
    }

    public void setIdeGetii(GenTipoInstitucion ideGetii) {
        this.ideGetii = ideGetii;
    }

    public List<GenInstitucion> getGenInstitucionList() {
        return genInstitucionList;
    }

    public void setGenInstitucionList(List<GenInstitucion> genInstitucionList) {
        this.genInstitucionList = genInstitucionList;
    }

    public GenInstitucion getGenIdeGeins() {
        return genIdeGeins;
    }

    public void setGenIdeGeins(GenInstitucion genIdeGeins) {
        this.genIdeGeins = genIdeGeins;
    }

    public List<NrhPrecancelacion> getNrhPrecancelacionList() {
        return nrhPrecancelacionList;
    }

    public void setNrhPrecancelacionList(List<NrhPrecancelacion> nrhPrecancelacionList) {
        this.nrhPrecancelacionList = nrhPrecancelacionList;
    }

    public List<GthCuentaBancariaEmpleado> getGthCuentaBancariaEmpleadoList() {
        return gthCuentaBancariaEmpleadoList;
    }

    public void setGthCuentaBancariaEmpleadoList(List<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoList) {
        this.gthCuentaBancariaEmpleadoList = gthCuentaBancariaEmpleadoList;
    }

    public List<NrhBeneficioEmpleado> getNrhBeneficioEmpleadoList() {
        return nrhBeneficioEmpleadoList;
    }

    public void setNrhBeneficioEmpleadoList(List<NrhBeneficioEmpleado> nrhBeneficioEmpleadoList) {
        this.nrhBeneficioEmpleadoList = nrhBeneficioEmpleadoList;
    }

    public List<NrhAnticipoAbono> getNrhAnticipoAbonoList() {
        return nrhAnticipoAbonoList;
    }

    public void setNrhAnticipoAbonoList(List<NrhAnticipoAbono> nrhAnticipoAbonoList) {
        this.nrhAnticipoAbonoList = nrhAnticipoAbonoList;
    }

    public List<GthExperienciaDocenteEmplea> getGthExperienciaDocenteEmpleaList() {
        return gthExperienciaDocenteEmpleaList;
    }

    public void setGthExperienciaDocenteEmpleaList(List<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaList) {
        this.gthExperienciaDocenteEmpleaList = gthExperienciaDocenteEmpleaList;
    }

    public List<GthCapacitacionEmpleado> getGthCapacitacionEmpleadoList() {
        return gthCapacitacionEmpleadoList;
    }

    public void setGthCapacitacionEmpleadoList(List<GthCapacitacionEmpleado> gthCapacitacionEmpleadoList) {
        this.gthCapacitacionEmpleadoList = gthCapacitacionEmpleadoList;
    }

    public List<SprEstudios> getSprEstudiosList() {
        return sprEstudiosList;
    }

    public void setSprEstudiosList(List<SprEstudios> sprEstudiosList) {
        this.sprEstudiosList = sprEstudiosList;
    }

    public List<GthEducacionEmpleado> getGthEducacionEmpleadoList() {
        return gthEducacionEmpleadoList;
    }

    public void setGthEducacionEmpleadoList(List<GthEducacionEmpleado> gthEducacionEmpleadoList) {
        this.gthEducacionEmpleadoList = gthEducacionEmpleadoList;
    }

    public List<GthExperienciaLaboralEmplea> getGthExperienciaLaboralEmpleaList() {
        return gthExperienciaLaboralEmpleaList;
    }

    public void setGthExperienciaLaboralEmpleaList(List<GthExperienciaLaboralEmplea> gthExperienciaLaboralEmpleaList) {
        this.gthExperienciaLaboralEmpleaList = gthExperienciaLaboralEmpleaList;
    }

    public List<GthViaticos> getGthViaticosList() {
        return gthViaticosList;
    }

    public void setGthViaticosList(List<GthViaticos> gthViaticosList) {
        this.gthViaticosList = gthViaticosList;
    }

    public List<GthVehiculoEmpleado> getGthVehiculoEmpleadoList() {
        return gthVehiculoEmpleadoList;
    }

    public void setGthVehiculoEmpleadoList(List<GthVehiculoEmpleado> gthVehiculoEmpleadoList) {
        this.gthVehiculoEmpleadoList = gthVehiculoEmpleadoList;
    }

    public List<GthInversionEmpleado> getGthInversionEmpleadoList() {
        return gthInversionEmpleadoList;
    }

    public void setGthInversionEmpleadoList(List<GthInversionEmpleado> gthInversionEmpleadoList) {
        this.gthInversionEmpleadoList = gthInversionEmpleadoList;
    }

    public List<SprCapacitacion> getSprCapacitacionList() {
        return sprCapacitacionList;
    }

    public void setSprCapacitacionList(List<SprCapacitacion> sprCapacitacionList) {
        this.sprCapacitacionList = sprCapacitacionList;
    }

    public List<NrhRetencionJudicial> getNrhRetencionJudicialList() {
        return nrhRetencionJudicialList;
    }

    public void setNrhRetencionJudicialList(List<NrhRetencionJudicial> nrhRetencionJudicialList) {
        this.nrhRetencionJudicialList = nrhRetencionJudicialList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeins != null ? ideGeins.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenInstitucion)) {
            return false;
        }
        GenInstitucion other = (GenInstitucion) object;
        if ((this.ideGeins == null && other.ideGeins != null) || (this.ideGeins != null && !this.ideGeins.equals(other.ideGeins))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenInstitucion[ ideGeins=" + ideGeins + " ]";
    }
    
}
