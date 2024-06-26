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
@Table(name = "tes_proveedor", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesProveedor.findAll", query = "SELECT t FROM TesProveedor t"),
    @NamedQuery(name = "TesProveedor.findByIdeTepro", query = "SELECT t FROM TesProveedor t WHERE t.ideTepro = :ideTepro"),
    @NamedQuery(name = "TesProveedor.findByActivoTepro", query = "SELECT t FROM TesProveedor t WHERE t.activoTepro = :activoTepro"),
    @NamedQuery(name = "TesProveedor.findByUsuarioIngre", query = "SELECT t FROM TesProveedor t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesProveedor.findByFechaIngre", query = "SELECT t FROM TesProveedor t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesProveedor.findByHoraIngre", query = "SELECT t FROM TesProveedor t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesProveedor.findByUsuarioActua", query = "SELECT t FROM TesProveedor t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesProveedor.findByFechaActua", query = "SELECT t FROM TesProveedor t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesProveedor.findByHoraActua", query = "SELECT t FROM TesProveedor t WHERE t.horaActua = :horaActua"),
    @NamedQuery(name = "TesProveedor.findByNombreTepro", query = "SELECT t FROM TesProveedor t WHERE t.nombreTepro = :nombreTepro"),
    @NamedQuery(name = "TesProveedor.findByRucTepro", query = "SELECT t FROM TesProveedor t WHERE t.rucTepro = :rucTepro"),
    @NamedQuery(name = "TesProveedor.findByNomRepresentanteTepro", query = "SELECT t FROM TesProveedor t WHERE t.nomRepresentanteTepro = :nomRepresentanteTepro"),
    @NamedQuery(name = "TesProveedor.findByRucRepresentanteTepro", query = "SELECT t FROM TesProveedor t WHERE t.rucRepresentanteTepro = :rucRepresentanteTepro")})
public class TesProveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tepro", nullable = false)
    private Long ideTepro;
    @Column(name = "activo_tepro")
    private Boolean activoTepro;
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
    @Size(max = 100)
    @Column(name = "nombre_tepro", length = 100)
    private String nombreTepro;
    @Size(max = 13)
    @Column(name = "ruc_tepro", length = 13)
    private String rucTepro;
    @Size(max = 100)
    @Column(name = "nom_representante_tepro", length = 100)
    private String nomRepresentanteTepro;
    @Size(max = 13)
    @Column(name = "ruc_representante_tepro", length = 13)
    private String rucRepresentanteTepro;
    @OneToMany(mappedBy = "ideTepro")
    private List<BodtBodega> bodtBodegaList;
    @OneToMany(mappedBy = "ideTepro")
    private List<TesComprobantePago> tesComprobantePagoList;
    @OneToMany(mappedBy = "ideTepro")
    private List<PreTramite> preTramiteList;
    @OneToMany(mappedBy = "ideTepro")
    private List<PreComparecienteContrato> preComparecienteContratoList;
    @OneToMany(mappedBy = "ideTepro")
    private List<TesCorreo> tesCorreoList;
    @JoinColumn(name = "ide_tetpp", referencedColumnName = "ide_tetpp")
    @ManyToOne
    private TesTipoProveedor ideTetpp;
    @JoinColumn(name = "ide_retic", referencedColumnName = "ide_retic")
    @ManyToOne
    private RecTipoContribuyente ideRetic;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    /*@JoinColumn(name = "ide_adfac", referencedColumnName = "ide_adfac")
    @ManyToOne
    private AdqFactura ideAdfac;*/
    @OneToMany(mappedBy = "ideTepro")
    private List<AdqSolicitudCompra> adqSolicitudCompraList;
    @OneToMany(mappedBy = "ideTepro")
    private List<TesTelefono> tesTelefonoList;
    @OneToMany(mappedBy = "ideTepro")
    private List<TesDireccion> tesDireccionList;
    @OneToMany(mappedBy = "ideTepro")
    private List<AfiActivo> afiActivoList;

    public TesProveedor() {
    }

    public TesProveedor(Long ideTepro) {
        this.ideTepro = ideTepro;
    }

    public Long getIdeTepro() {
        return ideTepro;
    }

    public void setIdeTepro(Long ideTepro) {
        this.ideTepro = ideTepro;
    }

    public Boolean getActivoTepro() {
        return activoTepro;
    }

    public void setActivoTepro(Boolean activoTepro) {
        this.activoTepro = activoTepro;
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

    public String getNombreTepro() {
        return nombreTepro;
    }

    public void setNombreTepro(String nombreTepro) {
        this.nombreTepro = nombreTepro;
    }

    public String getRucTepro() {
        return rucTepro;
    }

    public void setRucTepro(String rucTepro) {
        this.rucTepro = rucTepro;
    }

    public String getNomRepresentanteTepro() {
        return nomRepresentanteTepro;
    }

    public void setNomRepresentanteTepro(String nomRepresentanteTepro) {
        this.nomRepresentanteTepro = nomRepresentanteTepro;
    }

    public String getRucRepresentanteTepro() {
        return rucRepresentanteTepro;
    }

    public void setRucRepresentanteTepro(String rucRepresentanteTepro) {
        this.rucRepresentanteTepro = rucRepresentanteTepro;
    }

    public List<BodtBodega> getBodtBodegaList() {
        return bodtBodegaList;
    }

    public void setBodtBodegaList(List<BodtBodega> bodtBodegaList) {
        this.bodtBodegaList = bodtBodegaList;
    }

    public List<TesComprobantePago> getTesComprobantePagoList() {
        return tesComprobantePagoList;
    }

    public void setTesComprobantePagoList(List<TesComprobantePago> tesComprobantePagoList) {
        this.tesComprobantePagoList = tesComprobantePagoList;
    }

    public List<PreTramite> getPreTramiteList() {
        return preTramiteList;
    }

    public void setPreTramiteList(List<PreTramite> preTramiteList) {
        this.preTramiteList = preTramiteList;
    }

    public List<PreComparecienteContrato> getPreComparecienteContratoList() {
        return preComparecienteContratoList;
    }

    public void setPreComparecienteContratoList(List<PreComparecienteContrato> preComparecienteContratoList) {
        this.preComparecienteContratoList = preComparecienteContratoList;
    }

    public List<TesCorreo> getTesCorreoList() {
        return tesCorreoList;
    }

    public void setTesCorreoList(List<TesCorreo> tesCorreoList) {
        this.tesCorreoList = tesCorreoList;
    }

    public TesTipoProveedor getIdeTetpp() {
        return ideTetpp;
    }

    public void setIdeTetpp(TesTipoProveedor ideTetpp) {
        this.ideTetpp = ideTetpp;
    }

    public RecTipoContribuyente getIdeRetic() {
        return ideRetic;
    }

    public void setIdeRetic(RecTipoContribuyente ideRetic) {
        this.ideRetic = ideRetic;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }
/*
    public AdqFactura getIdeAdfac() {
        return ideAdfac;
    }

    public void setIdeAdfac(AdqFactura ideAdfac) {
        this.ideAdfac = ideAdfac;
    }
*/
    public List<AdqSolicitudCompra> getAdqSolicitudCompraList() {
        return adqSolicitudCompraList;
    }

    public void setAdqSolicitudCompraList(List<AdqSolicitudCompra> adqSolicitudCompraList) {
        this.adqSolicitudCompraList = adqSolicitudCompraList;
    }

    public List<TesTelefono> getTesTelefonoList() {
        return tesTelefonoList;
    }

    public void setTesTelefonoList(List<TesTelefono> tesTelefonoList) {
        this.tesTelefonoList = tesTelefonoList;
    }

    public List<TesDireccion> getTesDireccionList() {
        return tesDireccionList;
    }

    public void setTesDireccionList(List<TesDireccion> tesDireccionList) {
        this.tesDireccionList = tesDireccionList;
    }

    public List<AfiActivo> getAfiActivoList() {
        return afiActivoList;
    }

    public void setAfiActivoList(List<AfiActivo> afiActivoList) {
        this.afiActivoList = afiActivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTepro != null ? ideTepro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesProveedor)) {
            return false;
        }
        TesProveedor other = (TesProveedor) object;
        if ((this.ideTepro == null && other.ideTepro != null) || (this.ideTepro != null && !this.ideTepro.equals(other.ideTepro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesProveedor[ ideTepro=" + ideTepro + " ]";
    }
    
}
