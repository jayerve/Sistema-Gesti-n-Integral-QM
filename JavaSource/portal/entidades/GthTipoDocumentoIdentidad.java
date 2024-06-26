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
@Table(name = "gth_tipo_documento_identidad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findAll", query = "SELECT g FROM GthTipoDocumentoIdentidad g"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByIdeGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.ideGttdi = :ideGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByDetalleGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.detalleGttdi = :detalleGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByCodigoSbsGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.codigoSbsGttdi = :codigoSbsGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByCodigoSriGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.codigoSriGttdi = :codigoSriGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByActivoGttdi", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.activoGttdi = :activoGttdi"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByUsuarioIngre", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByFechaIngre", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByUsuarioActua", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByFechaActua", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByHoraIngre", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoDocumentoIdentidad.findByHoraActua", query = "SELECT g FROM GthTipoDocumentoIdentidad g WHERE g.horaActua = :horaActua")})
public class GthTipoDocumentoIdentidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttdi", nullable = false)
    private Integer ideGttdi;
    @Size(max = 50)
    @Column(name = "detalle_gttdi", length = 50)
    private String detalleGttdi;
    @Size(max = 50)
    @Column(name = "codigo_sbs_gttdi", length = 50)
    private String codigoSbsGttdi;
    @Size(max = 50)
    @Column(name = "codigo_sri_gttdi", length = 50)
    private String codigoSriGttdi;
    @Column(name = "activo_gttdi")
    private Boolean activoGttdi;
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
    @OneToMany(mappedBy = "ideGttdi")
    private List<SbsArchivoOnce> sbsArchivoOnceList;
    @OneToMany(mappedBy = "ideGttdi")
    private List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList;
    @OneToMany(mappedBy = "ideGttdi")
    private List<SbsArchivoCuarentaUno> sbsArchivoCuarentaUnoList;
    @OneToMany(mappedBy = "ideGttdi")
    private List<GthConyuge> gthConyugeList;
    @OneToMany(mappedBy = "ideGttdi")
    private List<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttdi")
    private List<GthEmpleado> gthEmpleadoList;
    @OneToMany(mappedBy = "ideGttdi")
    private List<NrhGarante> nrhGaranteList;
    @OneToMany(mappedBy = "gthIdeGttdi")
    private List<NrhGarante> nrhGaranteList1;
    @OneToMany(mappedBy = "ideGttdi")
    private List<RecClientes> recClientesList;
    @OneToMany(mappedBy = "gthIdeGttdi")
    private List<RecClientes> recClientesList1;
    @OneToMany(mappedBy = "ideGttdi")
    private List<SprBasePostulante> sprBasePostulanteList;
    @OneToMany(mappedBy = "ideGttdi")
    private List<GthFamiliar> gthFamiliarList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttdi")
    private List<GthCargasFamiliares> gthCargasFamiliaresList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttdi")
    private List<GenBeneficiario> genBeneficiarioList;
    @OneToMany(mappedBy = "gthIdeGttdi")
    private List<GenBeneficiario> genBeneficiarioList1;

    public GthTipoDocumentoIdentidad() {
    }

    public GthTipoDocumentoIdentidad(Integer ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public Integer getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(Integer ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public String getDetalleGttdi() {
        return detalleGttdi;
    }

    public void setDetalleGttdi(String detalleGttdi) {
        this.detalleGttdi = detalleGttdi;
    }

    public String getCodigoSbsGttdi() {
        return codigoSbsGttdi;
    }

    public void setCodigoSbsGttdi(String codigoSbsGttdi) {
        this.codigoSbsGttdi = codigoSbsGttdi;
    }

    public String getCodigoSriGttdi() {
        return codigoSriGttdi;
    }

    public void setCodigoSriGttdi(String codigoSriGttdi) {
        this.codigoSriGttdi = codigoSriGttdi;
    }

    public Boolean getActivoGttdi() {
        return activoGttdi;
    }

    public void setActivoGttdi(Boolean activoGttdi) {
        this.activoGttdi = activoGttdi;
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

    public List<SbsArchivoOnce> getSbsArchivoOnceList() {
        return sbsArchivoOnceList;
    }

    public void setSbsArchivoOnceList(List<SbsArchivoOnce> sbsArchivoOnceList) {
        this.sbsArchivoOnceList = sbsArchivoOnceList;
    }

    public List<SbsArchivoVeinteUno> getSbsArchivoVeinteUnoList() {
        return sbsArchivoVeinteUnoList;
    }

    public void setSbsArchivoVeinteUnoList(List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList) {
        this.sbsArchivoVeinteUnoList = sbsArchivoVeinteUnoList;
    }

    public List<SbsArchivoCuarentaUno> getSbsArchivoCuarentaUnoList() {
        return sbsArchivoCuarentaUnoList;
    }

    public void setSbsArchivoCuarentaUnoList(List<SbsArchivoCuarentaUno> sbsArchivoCuarentaUnoList) {
        this.sbsArchivoCuarentaUnoList = sbsArchivoCuarentaUnoList;
    }

    public List<GthConyuge> getGthConyugeList() {
        return gthConyugeList;
    }

    public void setGthConyugeList(List<GthConyuge> gthConyugeList) {
        this.gthConyugeList = gthConyugeList;
    }

    public List<GthParticipaNegocioEmplea> getGthParticipaNegocioEmpleaList() {
        return gthParticipaNegocioEmpleaList;
    }

    public void setGthParticipaNegocioEmpleaList(List<GthParticipaNegocioEmplea> gthParticipaNegocioEmpleaList) {
        this.gthParticipaNegocioEmpleaList = gthParticipaNegocioEmpleaList;
    }

    public List<GthEmpleado> getGthEmpleadoList() {
        return gthEmpleadoList;
    }

    public void setGthEmpleadoList(List<GthEmpleado> gthEmpleadoList) {
        this.gthEmpleadoList = gthEmpleadoList;
    }

    public List<NrhGarante> getNrhGaranteList() {
        return nrhGaranteList;
    }

    public void setNrhGaranteList(List<NrhGarante> nrhGaranteList) {
        this.nrhGaranteList = nrhGaranteList;
    }

    public List<NrhGarante> getNrhGaranteList1() {
        return nrhGaranteList1;
    }

    public void setNrhGaranteList1(List<NrhGarante> nrhGaranteList1) {
        this.nrhGaranteList1 = nrhGaranteList1;
    }

    public List<RecClientes> getRecClientesList() {
        return recClientesList;
    }

    public void setRecClientesList(List<RecClientes> recClientesList) {
        this.recClientesList = recClientesList;
    }

    public List<RecClientes> getRecClientesList1() {
        return recClientesList1;
    }

    public void setRecClientesList1(List<RecClientes> recClientesList1) {
        this.recClientesList1 = recClientesList1;
    }

    public List<SprBasePostulante> getSprBasePostulanteList() {
        return sprBasePostulanteList;
    }

    public void setSprBasePostulanteList(List<SprBasePostulante> sprBasePostulanteList) {
        this.sprBasePostulanteList = sprBasePostulanteList;
    }

    public List<GthFamiliar> getGthFamiliarList() {
        return gthFamiliarList;
    }

    public void setGthFamiliarList(List<GthFamiliar> gthFamiliarList) {
        this.gthFamiliarList = gthFamiliarList;
    }

    public List<GthCargasFamiliares> getGthCargasFamiliaresList() {
        return gthCargasFamiliaresList;
    }

    public void setGthCargasFamiliaresList(List<GthCargasFamiliares> gthCargasFamiliaresList) {
        this.gthCargasFamiliaresList = gthCargasFamiliaresList;
    }

    public List<GenBeneficiario> getGenBeneficiarioList() {
        return genBeneficiarioList;
    }

    public void setGenBeneficiarioList(List<GenBeneficiario> genBeneficiarioList) {
        this.genBeneficiarioList = genBeneficiarioList;
    }

    public List<GenBeneficiario> getGenBeneficiarioList1() {
        return genBeneficiarioList1;
    }

    public void setGenBeneficiarioList1(List<GenBeneficiario> genBeneficiarioList1) {
        this.genBeneficiarioList1 = genBeneficiarioList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttdi != null ? ideGttdi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoDocumentoIdentidad)) {
            return false;
        }
        GthTipoDocumentoIdentidad other = (GthTipoDocumentoIdentidad) object;
        if ((this.ideGttdi == null && other.ideGttdi != null) || (this.ideGttdi != null && !this.ideGttdi.equals(other.ideGttdi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoDocumentoIdentidad[ ideGttdi=" + ideGttdi + " ]";
    }
    
}
