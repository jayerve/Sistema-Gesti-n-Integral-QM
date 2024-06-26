/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "rec_clientes", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "RecClientes.findAll", query = "SELECT r FROM RecClientes r"),
    @NamedQuery(name = "RecClientes.findByIdeRecli", query = "SELECT r FROM RecClientes r WHERE r.ideRecli = :ideRecli"),
    @NamedQuery(name = "RecClientes.findByNombreComercialRecli", query = "SELECT r FROM RecClientes r WHERE r.nombreComercialRecli = :nombreComercialRecli"),
    @NamedQuery(name = "RecClientes.findByRucComercialRecli", query = "SELECT r FROM RecClientes r WHERE r.rucComercialRecli = :rucComercialRecli"),
    @NamedQuery(name = "RecClientes.findByRepresentanteLegalRecli", query = "SELECT r FROM RecClientes r WHERE r.representanteLegalRecli = :representanteLegalRecli"),
    @NamedQuery(name = "RecClientes.findByRucRepresentanteRecli", query = "SELECT r FROM RecClientes r WHERE r.rucRepresentanteRecli = :rucRepresentanteRecli"),
    @NamedQuery(name = "RecClientes.findByFechaIngresoRecli", query = "SELECT r FROM RecClientes r WHERE r.fechaIngresoRecli = :fechaIngresoRecli"),
    @NamedQuery(name = "RecClientes.findByFechaNacimientoRecli", query = "SELECT r FROM RecClientes r WHERE r.fechaNacimientoRecli = :fechaNacimientoRecli"),
    @NamedQuery(name = "RecClientes.findByEstadoRecli", query = "SELECT r FROM RecClientes r WHERE r.estadoRecli = :estadoRecli"),
    @NamedQuery(name = "RecClientes.findByDiscapacitadoRecli", query = "SELECT r FROM RecClientes r WHERE r.discapacitadoRecli = :discapacitadoRecli"),
    @NamedQuery(name = "RecClientes.findByFotoRecli", query = "SELECT r FROM RecClientes r WHERE r.fotoRecli = :fotoRecli"),
    @NamedQuery(name = "RecClientes.findByActivoRecli", query = "SELECT r FROM RecClientes r WHERE r.activoRecli = :activoRecli"),
    @NamedQuery(name = "RecClientes.findByUsuarioIngre", query = "SELECT r FROM RecClientes r WHERE r.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "RecClientes.findByFechaIngre", query = "SELECT r FROM RecClientes r WHERE r.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "RecClientes.findByHoraIngre", query = "SELECT r FROM RecClientes r WHERE r.horaIngre = :horaIngre"),
    @NamedQuery(name = "RecClientes.findByUsuarioActua", query = "SELECT r FROM RecClientes r WHERE r.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "RecClientes.findByFechaActua", query = "SELECT r FROM RecClientes r WHERE r.fechaActua = :fechaActua"),
    @NamedQuery(name = "RecClientes.findByHoraActua", query = "SELECT r FROM RecClientes r WHERE r.horaActua = :horaActua"),
    @NamedQuery(name = "RecClientes.findByMatrizSucursalRecli", query = "SELECT r FROM RecClientes r WHERE r.matrizSucursalRecli = :matrizSucursalRecli"),
    @NamedQuery(name = "RecClientes.findByFacturaDatosRecli", query = "SELECT r FROM RecClientes r WHERE r.facturaDatosRecli = :facturaDatosRecli"),
    @NamedQuery(name = "RecClientes.findByNroEstablecimientoRecli", query = "SELECT r FROM RecClientes r WHERE r.nroEstablecimientoRecli = :nroEstablecimientoRecli"),
    @NamedQuery(name = "RecClientes.findByCodigoZonaRecli", query = "SELECT r FROM RecClientes r WHERE r.codigoZonaRecli = :codigoZonaRecli"),
    @NamedQuery(name = "RecClientes.findByCoordxRecli", query = "SELECT r FROM RecClientes r WHERE r.coordxRecli = :coordxRecli"),
    @NamedQuery(name = "RecClientes.findByCoordyRecli", query = "SELECT r FROM RecClientes r WHERE r.coordyRecli = :coordyRecli"),
    @NamedQuery(name = "RecClientes.findByAplicaMtarifaRecli", query = "SELECT r FROM RecClientes r WHERE r.aplicaMtarifaRecli = :aplicaMtarifaRecli"),
    @NamedQuery(name = "RecClientes.findByTelefonoFacturaRecli", query = "SELECT r FROM RecClientes r WHERE r.telefonoFacturaRecli = :telefonoFacturaRecli"),
    @NamedQuery(name = "RecClientes.findByNroContratoRecli", query = "SELECT r FROM RecClientes r WHERE r.nroContratoRecli = :nroContratoRecli"),
    @NamedQuery(name = "RecClientes.findByEstimadoDesechoRecl", query = "SELECT r FROM RecClientes r WHERE r.estimadoDesechoRecl = :estimadoDesechoRecl"),
    @NamedQuery(name = "RecClientes.findByNumGeneradorDesechoRecli", query = "SELECT r FROM RecClientes r WHERE r.numGeneradorDesechoRecli = :numGeneradorDesechoRecli"),
    @NamedQuery(name = "RecClientes.findByRazonSocialRecli", query = "SELECT r FROM RecClientes r WHERE r.razonSocialRecli = :razonSocialRecli")})
public class RecClientes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_recli", nullable = false)
    private Long ideRecli;
    @Size(max = 100)
    @Column(name = "nombre_comercial_recli", length = 100)
    private String nombreComercialRecli;
    @Size(max = 13)
    @Column(name = "ruc_comercial_recli", length = 13)
    private String rucComercialRecli;
    @Size(max = 100)
    @Column(name = "representante_legal_recli", length = 100)
    private String representanteLegalRecli;
    @Size(max = 13)
    @Column(name = "ruc_representante_recli", length = 13)
    private String rucRepresentanteRecli;
    @Column(name = "fecha_ingreso_recli")
    @Temporal(TemporalType.DATE)
    private Date fechaIngresoRecli;
    @Column(name = "fecha_nacimiento_recli")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimientoRecli;
    @Column(name = "estado_recli")
    private Boolean estadoRecli;
    @Column(name = "discapacitado_recli")
    private Boolean discapacitadoRecli;
    @Size(max = 250)
    @Column(name = "foto_recli", length = 250)
    private String fotoRecli;
    @Column(name = "activo_recli")
    private Boolean activoRecli;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "matriz_sucursal_recli")
    private Integer matrizSucursalRecli;
    @Column(name = "factura_datos_recli")
    private Integer facturaDatosRecli;
    @Column(name = "nro_establecimiento_recli")
    private Integer nroEstablecimientoRecli;
    @Size(max = 50)
    @Column(name = "codigo_zona_recli", length = 50)
    private String codigoZonaRecli;
    @Size(max = 50)
    @Column(name = "coordx_recli", length = 50)
    private String coordxRecli;
    @Size(max = 50)
    @Column(name = "coordy_recli", length = 50)
    private String coordyRecli;
    @Column(name = "aplica_mtarifa_recli")
    private Boolean aplicaMtarifaRecli;
    @Size(max = 50)
    @Column(name = "telefono_factura_recli", length = 50)
    private String telefonoFacturaRecli;
    @Size(max = 50)
    @Column(name = "nro_contrato_recli", length = 50)
    private String nroContratoRecli;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "estimado_desecho_recl", precision = 10, scale = 2)
    private BigDecimal estimadoDesechoRecl;
    @Size(max = 50)
    @Column(name = "num_generador_desecho_recli", length = 50)
    private String numGeneradorDesechoRecli;
    @Size(max = 100)
    @Column(name = "razon_social_recli", length = 100)
    private String razonSocialRecli;
    @OneToMany(mappedBy = "ideRecli")
    private List<RecClienteTelefono> recClienteTelefonoList;
    @OneToMany(mappedBy = "ideRecli")
    private List<GenModuloHabilitado> genModuloHabilitadoList;
    @OneToMany(mappedBy = "ideRecli")
    private List<RecClienteDireccion> recClienteDireccionList;
    @OneToMany(mappedBy = "ideRecli")
    private List<TesClienteTarifa> tesClienteTarifaList;
    @OneToMany(mappedBy = "ideRecli")
    private List<FacFactura> facFacturaList;
    @OneToMany(mappedBy = "ideRecli")
    private List<RecClienteEmail> recClienteEmailList;
    @JoinColumn(name = "ide_tetar", referencedColumnName = "ide_tetar")
    @ManyToOne
    private TesTarifas ideTetar;
    @JoinColumn(name = "ide_retic", referencedColumnName = "ide_retic")
    @ManyToOne
    private RecTipoContribuyente ideRetic;
    @JoinColumn(name = "ide_retil", referencedColumnName = "ide_retil")
    @ManyToOne
    private RecTipoCliente ideRetil;
    @JoinColumn(name = "ide_retia", referencedColumnName = "ide_retia")
    @ManyToOne
    private RecTipoAsistencia ideRetia;
    @OneToMany(mappedBy = "recIdeRecli")
    private List<RecClientes> recClientesList;
    @JoinColumn(name = "rec_ide_recli", referencedColumnName = "ide_recli")
    @ManyToOne
    private RecClientes recIdeRecli;
    @JoinColumn(name = "ide_reclr", referencedColumnName = "ide_reclr")
    @ManyToOne
    private RecClienteRuta ideReclr;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "gth_ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad gthIdeGttdi;
    @JoinColumn(name = "ide_gtgen", referencedColumnName = "ide_gtgen")
    @ManyToOne
    private GthGenero ideGtgen;
    @JoinColumn(name = "ide_gtesc", referencedColumnName = "ide_gtesc")
    @ManyToOne
    private GthEstadoCivil ideGtesc;
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;
    @OneToMany(mappedBy = "ideRecli")
    private List<RecClienteArchivo> recClienteArchivoList;

    public RecClientes() {
    }

    public RecClientes(Long ideRecli) {
        this.ideRecli = ideRecli;
    }

    public Long getIdeRecli() {
        return ideRecli;
    }

    public void setIdeRecli(Long ideRecli) {
        this.ideRecli = ideRecli;
    }

    public String getNombreComercialRecli() {
        return nombreComercialRecli;
    }

    public void setNombreComercialRecli(String nombreComercialRecli) {
        this.nombreComercialRecli = nombreComercialRecli;
    }

    public String getRucComercialRecli() {
        return rucComercialRecli;
    }

    public void setRucComercialRecli(String rucComercialRecli) {
        this.rucComercialRecli = rucComercialRecli;
    }

    public String getRepresentanteLegalRecli() {
        return representanteLegalRecli;
    }

    public void setRepresentanteLegalRecli(String representanteLegalRecli) {
        this.representanteLegalRecli = representanteLegalRecli;
    }

    public String getRucRepresentanteRecli() {
        return rucRepresentanteRecli;
    }

    public void setRucRepresentanteRecli(String rucRepresentanteRecli) {
        this.rucRepresentanteRecli = rucRepresentanteRecli;
    }

    public Date getFechaIngresoRecli() {
        return fechaIngresoRecli;
    }

    public void setFechaIngresoRecli(Date fechaIngresoRecli) {
        this.fechaIngresoRecli = fechaIngresoRecli;
    }

    public Date getFechaNacimientoRecli() {
        return fechaNacimientoRecli;
    }

    public void setFechaNacimientoRecli(Date fechaNacimientoRecli) {
        this.fechaNacimientoRecli = fechaNacimientoRecli;
    }

    public Boolean getEstadoRecli() {
        return estadoRecli;
    }

    public void setEstadoRecli(Boolean estadoRecli) {
        this.estadoRecli = estadoRecli;
    }

    public Boolean getDiscapacitadoRecli() {
        return discapacitadoRecli;
    }

    public void setDiscapacitadoRecli(Boolean discapacitadoRecli) {
        this.discapacitadoRecli = discapacitadoRecli;
    }

    public String getFotoRecli() {
        return fotoRecli;
    }

    public void setFotoRecli(String fotoRecli) {
        this.fotoRecli = fotoRecli;
    }

    public Boolean getActivoRecli() {
        return activoRecli;
    }

    public void setActivoRecli(Boolean activoRecli) {
        this.activoRecli = activoRecli;
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

    public Integer getMatrizSucursalRecli() {
        return matrizSucursalRecli;
    }

    public void setMatrizSucursalRecli(Integer matrizSucursalRecli) {
        this.matrizSucursalRecli = matrizSucursalRecli;
    }

    public Integer getFacturaDatosRecli() {
        return facturaDatosRecli;
    }

    public void setFacturaDatosRecli(Integer facturaDatosRecli) {
        this.facturaDatosRecli = facturaDatosRecli;
    }

    public Integer getNroEstablecimientoRecli() {
        return nroEstablecimientoRecli;
    }

    public void setNroEstablecimientoRecli(Integer nroEstablecimientoRecli) {
        this.nroEstablecimientoRecli = nroEstablecimientoRecli;
    }

    public String getCodigoZonaRecli() {
        return codigoZonaRecli;
    }

    public void setCodigoZonaRecli(String codigoZonaRecli) {
        this.codigoZonaRecli = codigoZonaRecli;
    }

    public String getCoordxRecli() {
        return coordxRecli;
    }

    public void setCoordxRecli(String coordxRecli) {
        this.coordxRecli = coordxRecli;
    }

    public String getCoordyRecli() {
        return coordyRecli;
    }

    public void setCoordyRecli(String coordyRecli) {
        this.coordyRecli = coordyRecli;
    }

    public Boolean getAplicaMtarifaRecli() {
        return aplicaMtarifaRecli;
    }

    public void setAplicaMtarifaRecli(Boolean aplicaMtarifaRecli) {
        this.aplicaMtarifaRecli = aplicaMtarifaRecli;
    }

    public String getTelefonoFacturaRecli() {
        return telefonoFacturaRecli;
    }

    public void setTelefonoFacturaRecli(String telefonoFacturaRecli) {
        this.telefonoFacturaRecli = telefonoFacturaRecli;
    }

    public String getNroContratoRecli() {
        return nroContratoRecli;
    }

    public void setNroContratoRecli(String nroContratoRecli) {
        this.nroContratoRecli = nroContratoRecli;
    }

    public BigDecimal getEstimadoDesechoRecl() {
        return estimadoDesechoRecl;
    }

    public void setEstimadoDesechoRecl(BigDecimal estimadoDesechoRecl) {
        this.estimadoDesechoRecl = estimadoDesechoRecl;
    }

    public String getNumGeneradorDesechoRecli() {
        return numGeneradorDesechoRecli;
    }

    public void setNumGeneradorDesechoRecli(String numGeneradorDesechoRecli) {
        this.numGeneradorDesechoRecli = numGeneradorDesechoRecli;
    }

    public String getRazonSocialRecli() {
        return razonSocialRecli;
    }

    public void setRazonSocialRecli(String razonSocialRecli) {
        this.razonSocialRecli = razonSocialRecli;
    }

    public List<RecClienteTelefono> getRecClienteTelefonoList() {
        return recClienteTelefonoList;
    }

    public void setRecClienteTelefonoList(List<RecClienteTelefono> recClienteTelefonoList) {
        this.recClienteTelefonoList = recClienteTelefonoList;
    }

    public List<GenModuloHabilitado> getGenModuloHabilitadoList() {
        return genModuloHabilitadoList;
    }

    public void setGenModuloHabilitadoList(List<GenModuloHabilitado> genModuloHabilitadoList) {
        this.genModuloHabilitadoList = genModuloHabilitadoList;
    }

    public List<RecClienteDireccion> getRecClienteDireccionList() {
        return recClienteDireccionList;
    }

    public void setRecClienteDireccionList(List<RecClienteDireccion> recClienteDireccionList) {
        this.recClienteDireccionList = recClienteDireccionList;
    }

    public List<TesClienteTarifa> getTesClienteTarifaList() {
        return tesClienteTarifaList;
    }

    public void setTesClienteTarifaList(List<TesClienteTarifa> tesClienteTarifaList) {
        this.tesClienteTarifaList = tesClienteTarifaList;
    }

    public List<FacFactura> getFacFacturaList() {
        return facFacturaList;
    }

    public void setFacFacturaList(List<FacFactura> facFacturaList) {
        this.facFacturaList = facFacturaList;
    }

    public List<RecClienteEmail> getRecClienteEmailList() {
        return recClienteEmailList;
    }

    public void setRecClienteEmailList(List<RecClienteEmail> recClienteEmailList) {
        this.recClienteEmailList = recClienteEmailList;
    }

    public TesTarifas getIdeTetar() {
        return ideTetar;
    }

    public void setIdeTetar(TesTarifas ideTetar) {
        this.ideTetar = ideTetar;
    }

    public RecTipoContribuyente getIdeRetic() {
        return ideRetic;
    }

    public void setIdeRetic(RecTipoContribuyente ideRetic) {
        this.ideRetic = ideRetic;
    }

    public RecTipoCliente getIdeRetil() {
        return ideRetil;
    }

    public void setIdeRetil(RecTipoCliente ideRetil) {
        this.ideRetil = ideRetil;
    }

    public RecTipoAsistencia getIdeRetia() {
        return ideRetia;
    }

    public void setIdeRetia(RecTipoAsistencia ideRetia) {
        this.ideRetia = ideRetia;
    }

    public List<RecClientes> getRecClientesList() {
        return recClientesList;
    }

    public void setRecClientesList(List<RecClientes> recClientesList) {
        this.recClientesList = recClientesList;
    }

    public RecClientes getRecIdeRecli() {
        return recIdeRecli;
    }

    public void setRecIdeRecli(RecClientes recIdeRecli) {
        this.recIdeRecli = recIdeRecli;
    }

    public RecClienteRuta getIdeReclr() {
        return ideReclr;
    }

    public void setIdeReclr(RecClienteRuta ideReclr) {
        this.ideReclr = ideReclr;
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

    public GthGenero getIdeGtgen() {
        return ideGtgen;
    }

    public void setIdeGtgen(GthGenero ideGtgen) {
        this.ideGtgen = ideGtgen;
    }

    public GthEstadoCivil getIdeGtesc() {
        return ideGtesc;
    }

    public void setIdeGtesc(GthEstadoCivil ideGtesc) {
        this.ideGtesc = ideGtesc;
    }

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    public List<RecClienteArchivo> getRecClienteArchivoList() {
        return recClienteArchivoList;
    }

    public void setRecClienteArchivoList(List<RecClienteArchivo> recClienteArchivoList) {
        this.recClienteArchivoList = recClienteArchivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRecli != null ? ideRecli.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RecClientes)) {
            return false;
        }
        RecClientes other = (RecClientes) object;
        if ((this.ideRecli == null && other.ideRecli != null) || (this.ideRecli != null && !this.ideRecli.equals(other.ideRecli))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.RecClientes[ ideRecli=" + ideRecli + " ]";
    }
    
}
