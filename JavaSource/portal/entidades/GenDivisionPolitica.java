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
@Table(name = "gen_division_politica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenDivisionPolitica.findAll", query = "SELECT g FROM GenDivisionPolitica g"),
    @NamedQuery(name = "GenDivisionPolitica.findByIdeGedip", query = "SELECT g FROM GenDivisionPolitica g WHERE g.ideGedip = :ideGedip"),
    @NamedQuery(name = "GenDivisionPolitica.findByDetalleGedip", query = "SELECT g FROM GenDivisionPolitica g WHERE g.detalleGedip = :detalleGedip"),
    @NamedQuery(name = "GenDivisionPolitica.findByCoeficienteGedip", query = "SELECT g FROM GenDivisionPolitica g WHERE g.coeficienteGedip = :coeficienteGedip"),
    @NamedQuery(name = "GenDivisionPolitica.findByCodigoSriGedip", query = "SELECT g FROM GenDivisionPolitica g WHERE g.codigoSriGedip = :codigoSriGedip"),
    @NamedQuery(name = "GenDivisionPolitica.findByUsuarioIngre", query = "SELECT g FROM GenDivisionPolitica g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenDivisionPolitica.findByFechaIngre", query = "SELECT g FROM GenDivisionPolitica g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenDivisionPolitica.findByUsuarioActua", query = "SELECT g FROM GenDivisionPolitica g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenDivisionPolitica.findByFechaActua", query = "SELECT g FROM GenDivisionPolitica g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenDivisionPolitica.findByHoraIngre", query = "SELECT g FROM GenDivisionPolitica g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenDivisionPolitica.findByHoraActua", query = "SELECT g FROM GenDivisionPolitica g WHERE g.horaActua = :horaActua")})
public class GenDivisionPolitica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gedip", nullable = false)
    private Integer ideGedip;
    @Size(max = 50)
    @Column(name = "detalle_gedip", length = 50)
    private String detalleGedip;
    @Column(name = "coeficiente_gedip")
    private Integer coeficienteGedip;
    @Size(max = 50)
    @Column(name = "codigo_sri_gedip", length = 50)
    private String codigoSriGedip;
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
    @OneToMany(mappedBy = "ideGedip")
    private List<CppCapacitacion> cppCapacitacionList;
    @OneToMany(mappedBy = "ideGedip")
    private List<TesComprobantePago> tesComprobantePagoList;
    @OneToMany(mappedBy = "ideGedip")
    private List<GthDireccion> gthDireccionList;
    @OneToMany(mappedBy = "ideGedip")
    private List<SprExperienciaLaboral> sprExperienciaLaboralList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGedip")
    private List<GthEmpleado> gthEmpleadoList;
    @JoinColumn(name = "ide_getdp", referencedColumnName = "ide_getdp")
    @ManyToOne
    private GenTipoDivisionPolitica ideGetdp;
    @JoinColumn(name = "ide_gereg", referencedColumnName = "ide_gereg")
    @ManyToOne
    private GenRegion ideGereg;
    @OneToMany(mappedBy = "genIdeGedip")
    private List<GenDivisionPolitica> genDivisionPoliticaList;
    @JoinColumn(name = "gen_ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica genIdeGedip;
    @OneToMany(mappedBy = "ideGedip")
    private List<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaList;
    @OneToMany(mappedBy = "ideGedip")
    private List<RecClientes> recClientesList;
    @OneToMany(mappedBy = "ideGedip")
    private List<GthCapacitacionEmpleado> gthCapacitacionEmpleadoList;
    @OneToMany(mappedBy = "ideGedip")
    private List<SprEstudios> sprEstudiosList;
    @OneToMany(mappedBy = "ideGedip")
    private List<ContTiketViaje> contTiketViajeList;
    @OneToMany(mappedBy = "genIdeGedip")
    private List<ContTiketViaje> contTiketViajeList1;
    @OneToMany(mappedBy = "ideGedip")
    private List<GthEducacionEmpleado> gthEducacionEmpleadoList;
    @OneToMany(mappedBy = "ideGedip")
    private List<GthViaticos> gthViaticosList;
    @OneToMany(mappedBy = "ideGedip")
    private List<SisSucursal> sisSucursalList;
    @OneToMany(mappedBy = "ideGedip")
    private List<SprCapacitacion> sprCapacitacionList;

    public GenDivisionPolitica() {
    }

    public GenDivisionPolitica(Integer ideGedip) {
        this.ideGedip = ideGedip;
    }

    public Integer getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(Integer ideGedip) {
        this.ideGedip = ideGedip;
    }

    public String getDetalleGedip() {
        return detalleGedip;
    }

    public void setDetalleGedip(String detalleGedip) {
        this.detalleGedip = detalleGedip;
    }

    public Integer getCoeficienteGedip() {
        return coeficienteGedip;
    }

    public void setCoeficienteGedip(Integer coeficienteGedip) {
        this.coeficienteGedip = coeficienteGedip;
    }

    public String getCodigoSriGedip() {
        return codigoSriGedip;
    }

    public void setCodigoSriGedip(String codigoSriGedip) {
        this.codigoSriGedip = codigoSriGedip;
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

    public List<CppCapacitacion> getCppCapacitacionList() {
        return cppCapacitacionList;
    }

    public void setCppCapacitacionList(List<CppCapacitacion> cppCapacitacionList) {
        this.cppCapacitacionList = cppCapacitacionList;
    }

    public List<TesComprobantePago> getTesComprobantePagoList() {
        return tesComprobantePagoList;
    }

    public void setTesComprobantePagoList(List<TesComprobantePago> tesComprobantePagoList) {
        this.tesComprobantePagoList = tesComprobantePagoList;
    }

    public List<GthDireccion> getGthDireccionList() {
        return gthDireccionList;
    }

    public void setGthDireccionList(List<GthDireccion> gthDireccionList) {
        this.gthDireccionList = gthDireccionList;
    }

    public List<SprExperienciaLaboral> getSprExperienciaLaboralList() {
        return sprExperienciaLaboralList;
    }

    public void setSprExperienciaLaboralList(List<SprExperienciaLaboral> sprExperienciaLaboralList) {
        this.sprExperienciaLaboralList = sprExperienciaLaboralList;
    }

    public List<GthEmpleado> getGthEmpleadoList() {
        return gthEmpleadoList;
    }

    public void setGthEmpleadoList(List<GthEmpleado> gthEmpleadoList) {
        this.gthEmpleadoList = gthEmpleadoList;
    }

    public GenTipoDivisionPolitica getIdeGetdp() {
        return ideGetdp;
    }

    public void setIdeGetdp(GenTipoDivisionPolitica ideGetdp) {
        this.ideGetdp = ideGetdp;
    }

    public GenRegion getIdeGereg() {
        return ideGereg;
    }

    public void setIdeGereg(GenRegion ideGereg) {
        this.ideGereg = ideGereg;
    }

    public List<GenDivisionPolitica> getGenDivisionPoliticaList() {
        return genDivisionPoliticaList;
    }

    public void setGenDivisionPoliticaList(List<GenDivisionPolitica> genDivisionPoliticaList) {
        this.genDivisionPoliticaList = genDivisionPoliticaList;
    }

    public GenDivisionPolitica getGenIdeGedip() {
        return genIdeGedip;
    }

    public void setGenIdeGedip(GenDivisionPolitica genIdeGedip) {
        this.genIdeGedip = genIdeGedip;
    }

    public List<GthExperienciaDocenteEmplea> getGthExperienciaDocenteEmpleaList() {
        return gthExperienciaDocenteEmpleaList;
    }

    public void setGthExperienciaDocenteEmpleaList(List<GthExperienciaDocenteEmplea> gthExperienciaDocenteEmpleaList) {
        this.gthExperienciaDocenteEmpleaList = gthExperienciaDocenteEmpleaList;
    }

    public List<RecClientes> getRecClientesList() {
        return recClientesList;
    }

    public void setRecClientesList(List<RecClientes> recClientesList) {
        this.recClientesList = recClientesList;
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

    public List<ContTiketViaje> getContTiketViajeList() {
        return contTiketViajeList;
    }

    public void setContTiketViajeList(List<ContTiketViaje> contTiketViajeList) {
        this.contTiketViajeList = contTiketViajeList;
    }

    public List<ContTiketViaje> getContTiketViajeList1() {
        return contTiketViajeList1;
    }

    public void setContTiketViajeList1(List<ContTiketViaje> contTiketViajeList1) {
        this.contTiketViajeList1 = contTiketViajeList1;
    }

    public List<GthEducacionEmpleado> getGthEducacionEmpleadoList() {
        return gthEducacionEmpleadoList;
    }

    public void setGthEducacionEmpleadoList(List<GthEducacionEmpleado> gthEducacionEmpleadoList) {
        this.gthEducacionEmpleadoList = gthEducacionEmpleadoList;
    }

    public List<GthViaticos> getGthViaticosList() {
        return gthViaticosList;
    }

    public void setGthViaticosList(List<GthViaticos> gthViaticosList) {
        this.gthViaticosList = gthViaticosList;
    }

    public List<SisSucursal> getSisSucursalList() {
        return sisSucursalList;
    }

    public void setSisSucursalList(List<SisSucursal> sisSucursalList) {
        this.sisSucursalList = sisSucursalList;
    }

    public List<SprCapacitacion> getSprCapacitacionList() {
        return sprCapacitacionList;
    }

    public void setSprCapacitacionList(List<SprCapacitacion> sprCapacitacionList) {
        this.sprCapacitacionList = sprCapacitacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGedip != null ? ideGedip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenDivisionPolitica)) {
            return false;
        }
        GenDivisionPolitica other = (GenDivisionPolitica) object;
        if ((this.ideGedip == null && other.ideGedip != null) || (this.ideGedip != null && !this.ideGedip.equals(other.ideGedip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenDivisionPolitica[ ideGedip=" + ideGedip + " ]";
    }
    
}
