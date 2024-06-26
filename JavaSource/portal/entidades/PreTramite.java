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
@Table(name = "pre_tramite", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreTramite.findAll", query = "SELECT p FROM PreTramite p"),
    @NamedQuery(name = "PreTramite.findByIdePrtra", query = "SELECT p FROM PreTramite p WHERE p.idePrtra = :idePrtra"),
    @NamedQuery(name = "PreTramite.findByFechaTramitePrtra", query = "SELECT p FROM PreTramite p WHERE p.fechaTramitePrtra = :fechaTramitePrtra"),
    @NamedQuery(name = "PreTramite.findByFechaOficioPrtra", query = "SELECT p FROM PreTramite p WHERE p.fechaOficioPrtra = :fechaOficioPrtra"),
    @NamedQuery(name = "PreTramite.findByNumeroOficioPrtra", query = "SELECT p FROM PreTramite p WHERE p.numeroOficioPrtra = :numeroOficioPrtra"),
    @NamedQuery(name = "PreTramite.findByObservacionesPrtra", query = "SELECT p FROM PreTramite p WHERE p.observacionesPrtra = :observacionesPrtra"),
    @NamedQuery(name = "PreTramite.findByActivoPrtra", query = "SELECT p FROM PreTramite p WHERE p.activoPrtra = :activoPrtra"),
    @NamedQuery(name = "PreTramite.findByUsuarioIngre", query = "SELECT p FROM PreTramite p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreTramite.findByFechaIngre", query = "SELECT p FROM PreTramite p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreTramite.findByHoraIngre", query = "SELECT p FROM PreTramite p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreTramite.findByUsuarioActua", query = "SELECT p FROM PreTramite p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreTramite.findByFechaActua", query = "SELECT p FROM PreTramite p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreTramite.findByHoraActua", query = "SELECT p FROM PreTramite p WHERE p.horaActua = :horaActua")})
public class PreTramite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prtra", nullable = false)
    private Long idePrtra;
    @Column(name = "fecha_tramite_prtra")
    @Temporal(TemporalType.DATE)
    private Date fechaTramitePrtra;
    @Column(name = "fecha_oficio_prtra")
    @Temporal(TemporalType.DATE)
    private Date fechaOficioPrtra;
    @Size(max = 50)
    @Column(name = "numero_oficio_prtra", length = 50)
    private String numeroOficioPrtra;
    @Size(max = 2147483647)
    @Column(name = "observaciones_prtra", length = 2147483647)
    private String observacionesPrtra;
    @Column(name = "activo_prtra")
    private Boolean activoPrtra;
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
    @OneToMany(mappedBy = "idePrtra")
    private List<TesComprobantePago> tesComprobantePagoList;
    @JoinColumn(name = "ide_tepro", referencedColumnName = "ide_tepro")
    @ManyToOne
    private TesProveedor ideTepro;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumn(name = "gen_ide_geedp2", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp2;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "gen_ide_geedp3", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp3;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @JoinColumn(name = "ide_copag", referencedColumnName = "ide_copag")
    @ManyToOne
    private ContParametrosGeneral ideCopag;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @OneToMany(mappedBy = "idePrtra")
    private List<AdqSolicitudCompra> adqSolicitudCompraList;
    @OneToMany(mappedBy = "idePrtra")
    private List<PreArchivo> preArchivoList;
    @OneToMany(mappedBy = "idePrtra")
    private List<PreDocumentoHabilitante> preDocumentoHabilitanteList;

    public PreTramite() {
    }

    public PreTramite(Long idePrtra) {
        this.idePrtra = idePrtra;
    }

    public Long getIdePrtra() {
        return idePrtra;
    }

    public void setIdePrtra(Long idePrtra) {
        this.idePrtra = idePrtra;
    }

    public Date getFechaTramitePrtra() {
        return fechaTramitePrtra;
    }

    public void setFechaTramitePrtra(Date fechaTramitePrtra) {
        this.fechaTramitePrtra = fechaTramitePrtra;
    }

    public Date getFechaOficioPrtra() {
        return fechaOficioPrtra;
    }

    public void setFechaOficioPrtra(Date fechaOficioPrtra) {
        this.fechaOficioPrtra = fechaOficioPrtra;
    }

    public String getNumeroOficioPrtra() {
        return numeroOficioPrtra;
    }

    public void setNumeroOficioPrtra(String numeroOficioPrtra) {
        this.numeroOficioPrtra = numeroOficioPrtra;
    }

    public String getObservacionesPrtra() {
        return observacionesPrtra;
    }

    public void setObservacionesPrtra(String observacionesPrtra) {
        this.observacionesPrtra = observacionesPrtra;
    }

    public Boolean getActivoPrtra() {
        return activoPrtra;
    }

    public void setActivoPrtra(Boolean activoPrtra) {
        this.activoPrtra = activoPrtra;
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

    public List<TesComprobantePago> getTesComprobantePagoList() {
        return tesComprobantePagoList;
    }

    public void setTesComprobantePagoList(List<TesComprobantePago> tesComprobantePagoList) {
        this.tesComprobantePagoList = tesComprobantePagoList;
    }

    public TesProveedor getIdeTepro() {
        return ideTepro;
    }

    public void setIdeTepro(TesProveedor ideTepro) {
        this.ideTepro = ideTepro;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp() {
        return genIdeGeedp;
    }

    public void setGenIdeGeedp(GenEmpleadosDepartamentoPar genIdeGeedp) {
        this.genIdeGeedp = genIdeGeedp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp2() {
        return genIdeGeedp2;
    }

    public void setGenIdeGeedp2(GenEmpleadosDepartamentoPar genIdeGeedp2) {
        this.genIdeGeedp2 = genIdeGeedp2;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp3() {
        return genIdeGeedp3;
    }

    public void setGenIdeGeedp3(GenEmpleadosDepartamentoPar genIdeGeedp3) {
        this.genIdeGeedp3 = genIdeGeedp3;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    public ContParametrosGeneral getIdeCopag() {
        return ideCopag;
    }

    public void setIdeCopag(ContParametrosGeneral ideCopag) {
        this.ideCopag = ideCopag;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public List<AdqSolicitudCompra> getAdqSolicitudCompraList() {
        return adqSolicitudCompraList;
    }

    public void setAdqSolicitudCompraList(List<AdqSolicitudCompra> adqSolicitudCompraList) {
        this.adqSolicitudCompraList = adqSolicitudCompraList;
    }

    public List<PreArchivo> getPreArchivoList() {
        return preArchivoList;
    }

    public void setPreArchivoList(List<PreArchivo> preArchivoList) {
        this.preArchivoList = preArchivoList;
    }

    public List<PreDocumentoHabilitante> getPreDocumentoHabilitanteList() {
        return preDocumentoHabilitanteList;
    }

    public void setPreDocumentoHabilitanteList(List<PreDocumentoHabilitante> preDocumentoHabilitanteList) {
        this.preDocumentoHabilitanteList = preDocumentoHabilitanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrtra != null ? idePrtra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreTramite)) {
            return false;
        }
        PreTramite other = (PreTramite) object;
        if ((this.idePrtra == null && other.idePrtra != null) || (this.idePrtra != null && !this.idePrtra.equals(other.idePrtra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreTramite[ idePrtra=" + idePrtra + " ]";
    }
    
}
