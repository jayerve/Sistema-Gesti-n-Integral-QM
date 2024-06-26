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
@Table(name = "gen_beneficiario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenBeneficiario.findAll", query = "SELECT g FROM GenBeneficiario g"),
    @NamedQuery(name = "GenBeneficiario.findByIdeGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.ideGeben = :ideGeben"),
    @NamedQuery(name = "GenBeneficiario.findByTitularGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.titularGeben = :titularGeben"),
    @NamedQuery(name = "GenBeneficiario.findByFechaIngresoGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.fechaIngresoGeben = :fechaIngresoGeben"),
    @NamedQuery(name = "GenBeneficiario.findByRepresentanteLegalGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.representanteLegalGeben = :representanteLegalGeben"),
    @NamedQuery(name = "GenBeneficiario.findByDireccionRepresentanteGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.direccionRepresentanteGeben = :direccionRepresentanteGeben"),
    @NamedQuery(name = "GenBeneficiario.findByTelefonoRepresentanteGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.telefonoRepresentanteGeben = :telefonoRepresentanteGeben"),
    @NamedQuery(name = "GenBeneficiario.findByDocumentoRepresentanteGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.documentoRepresentanteGeben = :documentoRepresentanteGeben"),
    @NamedQuery(name = "GenBeneficiario.findByDocumentoTitularGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.documentoTitularGeben = :documentoTitularGeben"),
    @NamedQuery(name = "GenBeneficiario.findByActivoGeben", query = "SELECT g FROM GenBeneficiario g WHERE g.activoGeben = :activoGeben"),
    @NamedQuery(name = "GenBeneficiario.findByUsuarioIngre", query = "SELECT g FROM GenBeneficiario g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenBeneficiario.findByFechaIngre", query = "SELECT g FROM GenBeneficiario g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenBeneficiario.findByUsuarioActua", query = "SELECT g FROM GenBeneficiario g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenBeneficiario.findByFechaActua", query = "SELECT g FROM GenBeneficiario g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenBeneficiario.findByHoraIngre", query = "SELECT g FROM GenBeneficiario g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenBeneficiario.findByHoraActua", query = "SELECT g FROM GenBeneficiario g WHERE g.horaActua = :horaActua")})
public class GenBeneficiario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geben", nullable = false)
    private Integer ideGeben;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "titular_geben", nullable = false, length = 100)
    private String titularGeben;
    @Column(name = "fecha_ingreso_geben")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoGeben;
    @Size(max = 100)
    @Column(name = "representante_legal_geben", length = 100)
    private String representanteLegalGeben;
    @Size(max = 1000)
    @Column(name = "direccion_representante_geben", length = 1000)
    private String direccionRepresentanteGeben;
    @Size(max = 50)
    @Column(name = "telefono_representante_geben", length = 50)
    private String telefonoRepresentanteGeben;
    @Size(max = 15)
    @Column(name = "documento_representante_geben", length = 15)
    private String documentoRepresentanteGeben;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "documento_titular_geben", nullable = false, length = 15)
    private String documentoTitularGeben;
    @Column(name = "activo_geben")
    private Boolean activoGeben;
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
    @OneToMany(mappedBy = "ideGeben")
    private List<CntMovimientoContable> cntMovimientoContableList;
    @OneToMany(mappedBy = "ideGeben")
    private List<GthDireccion> gthDireccionList;
    @OneToMany(mappedBy = "ideGeben")
    private List<GthTelefono> gthTelefonoList;
    @OneToMany(mappedBy = "ideGeben")
    private List<NrhRubroAsiento> nrhRubroAsientoList;
    @OneToMany(mappedBy = "ideGeben")
    private List<SaoBienes> saoBienesList;
    @OneToMany(mappedBy = "ideGeben")
    private List<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoList;
    @OneToMany(mappedBy = "ideGeben")
    private List<SaoMatrizRiesgo> saoMatrizRiesgoList;
    @OneToMany(mappedBy = "ideGeben")
    private List<GthCorreo> gthCorreoList;
    @OneToMany(mappedBy = "ideGeben")
    private List<SprPresupuestoBeneficiario> sprPresupuestoBeneficiarioList;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi", nullable = false)
    @ManyToOne(optional = false)
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "gth_ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad gthIdeGttdi;
    @JoinColumn(name = "ide_gttae", referencedColumnName = "ide_gttae", nullable = false)
    @ManyToOne(optional = false)
    private GthTipoActividadEconomica ideGttae;
    @JoinColumn(name = "ide_getic", referencedColumnName = "ide_getic", nullable = false)
    @ManyToOne(optional = false)
    private GenTipoContribuyente ideGetic;
    @OneToMany(mappedBy = "ideGeben")
    private List<NrhRetencionJudicial> nrhRetencionJudicialList;

    public GenBeneficiario() {
    }

    public GenBeneficiario(Integer ideGeben) {
        this.ideGeben = ideGeben;
    }

    public GenBeneficiario(Integer ideGeben, String titularGeben, String documentoTitularGeben) {
        this.ideGeben = ideGeben;
        this.titularGeben = titularGeben;
        this.documentoTitularGeben = documentoTitularGeben;
    }

    public Integer getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(Integer ideGeben) {
        this.ideGeben = ideGeben;
    }

    public String getTitularGeben() {
        return titularGeben;
    }

    public void setTitularGeben(String titularGeben) {
        this.titularGeben = titularGeben;
    }

    public Date getFechaIngresoGeben() {
        return fechaIngresoGeben;
    }

    public void setFechaIngresoGeben(Date fechaIngresoGeben) {
        this.fechaIngresoGeben = fechaIngresoGeben;
    }

    public String getRepresentanteLegalGeben() {
        return representanteLegalGeben;
    }

    public void setRepresentanteLegalGeben(String representanteLegalGeben) {
        this.representanteLegalGeben = representanteLegalGeben;
    }

    public String getDireccionRepresentanteGeben() {
        return direccionRepresentanteGeben;
    }

    public void setDireccionRepresentanteGeben(String direccionRepresentanteGeben) {
        this.direccionRepresentanteGeben = direccionRepresentanteGeben;
    }

    public String getTelefonoRepresentanteGeben() {
        return telefonoRepresentanteGeben;
    }

    public void setTelefonoRepresentanteGeben(String telefonoRepresentanteGeben) {
        this.telefonoRepresentanteGeben = telefonoRepresentanteGeben;
    }

    public String getDocumentoRepresentanteGeben() {
        return documentoRepresentanteGeben;
    }

    public void setDocumentoRepresentanteGeben(String documentoRepresentanteGeben) {
        this.documentoRepresentanteGeben = documentoRepresentanteGeben;
    }

    public String getDocumentoTitularGeben() {
        return documentoTitularGeben;
    }

    public void setDocumentoTitularGeben(String documentoTitularGeben) {
        this.documentoTitularGeben = documentoTitularGeben;
    }

    public Boolean getActivoGeben() {
        return activoGeben;
    }

    public void setActivoGeben(Boolean activoGeben) {
        this.activoGeben = activoGeben;
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

    public List<CntMovimientoContable> getCntMovimientoContableList() {
        return cntMovimientoContableList;
    }

    public void setCntMovimientoContableList(List<CntMovimientoContable> cntMovimientoContableList) {
        this.cntMovimientoContableList = cntMovimientoContableList;
    }

    public List<GthDireccion> getGthDireccionList() {
        return gthDireccionList;
    }

    public void setGthDireccionList(List<GthDireccion> gthDireccionList) {
        this.gthDireccionList = gthDireccionList;
    }

    public List<GthTelefono> getGthTelefonoList() {
        return gthTelefonoList;
    }

    public void setGthTelefonoList(List<GthTelefono> gthTelefonoList) {
        this.gthTelefonoList = gthTelefonoList;
    }

    public List<NrhRubroAsiento> getNrhRubroAsientoList() {
        return nrhRubroAsientoList;
    }

    public void setNrhRubroAsientoList(List<NrhRubroAsiento> nrhRubroAsientoList) {
        this.nrhRubroAsientoList = nrhRubroAsientoList;
    }

    public List<SaoBienes> getSaoBienesList() {
        return saoBienesList;
    }

    public void setSaoBienesList(List<SaoBienes> saoBienesList) {
        this.saoBienesList = saoBienesList;
    }

    public List<GthCuentaBancariaEmpleado> getGthCuentaBancariaEmpleadoList() {
        return gthCuentaBancariaEmpleadoList;
    }

    public void setGthCuentaBancariaEmpleadoList(List<GthCuentaBancariaEmpleado> gthCuentaBancariaEmpleadoList) {
        this.gthCuentaBancariaEmpleadoList = gthCuentaBancariaEmpleadoList;
    }

    public List<SaoMatrizRiesgo> getSaoMatrizRiesgoList() {
        return saoMatrizRiesgoList;
    }

    public void setSaoMatrizRiesgoList(List<SaoMatrizRiesgo> saoMatrizRiesgoList) {
        this.saoMatrizRiesgoList = saoMatrizRiesgoList;
    }

    public List<GthCorreo> getGthCorreoList() {
        return gthCorreoList;
    }

    public void setGthCorreoList(List<GthCorreo> gthCorreoList) {
        this.gthCorreoList = gthCorreoList;
    }

    public List<SprPresupuestoBeneficiario> getSprPresupuestoBeneficiarioList() {
        return sprPresupuestoBeneficiarioList;
    }

    public void setSprPresupuestoBeneficiarioList(List<SprPresupuestoBeneficiario> sprPresupuestoBeneficiarioList) {
        this.sprPresupuestoBeneficiarioList = sprPresupuestoBeneficiarioList;
    }

    public GthTipoDocumentoIdentidad getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(GthTipoDocumentoIdentidad ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public GthTipoDocumentoIdentidad getGthIdeGttdi() {
        return gthIdeGttdi;
    }

    public void setGthIdeGttdi(GthTipoDocumentoIdentidad gthIdeGttdi) {
        this.gthIdeGttdi = gthIdeGttdi;
    }

    public GthTipoActividadEconomica getIdeGttae() {
        return ideGttae;
    }

    public void setIdeGttae(GthTipoActividadEconomica ideGttae) {
        this.ideGttae = ideGttae;
    }

    public GenTipoContribuyente getIdeGetic() {
        return ideGetic;
    }

    public void setIdeGetic(GenTipoContribuyente ideGetic) {
        this.ideGetic = ideGetic;
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
        hash += (ideGeben != null ? ideGeben.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenBeneficiario)) {
            return false;
        }
        GenBeneficiario other = (GenBeneficiario) object;
        if ((this.ideGeben == null && other.ideGeben != null) || (this.ideGeben != null && !this.ideGeben.equals(other.ideGeben))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenBeneficiario[ ideGeben=" + ideGeben + " ]";
    }
    
}
