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
@Table(name = "cont_estado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContEstado.findAll", query = "SELECT c FROM ContEstado c"),
    @NamedQuery(name = "ContEstado.findByIdeCoest", query = "SELECT c FROM ContEstado c WHERE c.ideCoest = :ideCoest"),
    @NamedQuery(name = "ContEstado.findByDetalleCoest", query = "SELECT c FROM ContEstado c WHERE c.detalleCoest = :detalleCoest"),
    @NamedQuery(name = "ContEstado.findByActivoCoest", query = "SELECT c FROM ContEstado c WHERE c.activoCoest = :activoCoest"),
    @NamedQuery(name = "ContEstado.findByUsuarioIngre", query = "SELECT c FROM ContEstado c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContEstado.findByFechaIngre", query = "SELECT c FROM ContEstado c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContEstado.findByHoraIngre", query = "SELECT c FROM ContEstado c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContEstado.findByUsuarioActua", query = "SELECT c FROM ContEstado c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContEstado.findByFechaActua", query = "SELECT c FROM ContEstado c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContEstado.findByHoraActua", query = "SELECT c FROM ContEstado c WHERE c.horaActua = :horaActua")})
public class ContEstado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_coest", nullable = false)
    private Long ideCoest;
    @Size(max = 50)
    @Column(name = "detalle_coest", length = 50)
    private String detalleCoest;
    @Column(name = "activo_coest")
    private Boolean activoCoest;
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
    @OneToMany(mappedBy = "ideCoest")
    private List<ContConvenio> contConvenioList;
    @OneToMany(mappedBy = "ideCoest")
    private List<BodtBodega> bodtBodegaList;
    @OneToMany(mappedBy = "ideCoest")
    private List<TesComprobantePago> tesComprobantePagoList;
    @OneToMany(mappedBy = "ideCoest")
    private List<PreTramite> preTramiteList;
    @OneToMany(mappedBy = "ideCoest")
    private List<PreContrato> preContratoList;
    @OneToMany(mappedBy = "ideCoest")
    private List<TesProveedor> tesProveedorList;
    @OneToMany(mappedBy = "ideCoest")
    private List<TesPoliza> tesPolizaList;
    @OneToMany(mappedBy = "ideCoest")
    private List<AfiCustodio> afiCustodioList;
    @OneToMany(mappedBy = "ideCoest")
    private List<FacFactura> facFacturaList;
    @OneToMany(mappedBy = "ideCoest")
    private List<PreContratacionPublica> preContratacionPublicaList;
    @OneToMany(mappedBy = "ideCoest")
    private List<PrePac> prePacList;
    @OneToMany(mappedBy = "ideCoest")
    private List<PrePoaReforma> prePoaReformaList;
    @OneToMany(mappedBy = "ideCoest")
    private List<TesRenovacionPoliza> tesRenovacionPolizaList;
    @OneToMany(mappedBy = "ideCoest")
    private List<ContTiketViaje> contTiketViajeList;
    @OneToMany(mappedBy = "ideCoest")
    private List<GenModuloEstado> genModuloEstadoList;
    @OneToMany(mappedBy = "ideCoest")
    private List<PrePoa> prePoaList;
    @OneToMany(mappedBy = "ideCoest")
    private List<PrePoaFinanciamiento> prePoaFinanciamientoList;

    public ContEstado() {
    }

    public ContEstado(Long ideCoest) {
        this.ideCoest = ideCoest;
    }

    public Long getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(Long ideCoest) {
        this.ideCoest = ideCoest;
    }

    public String getDetalleCoest() {
        return detalleCoest;
    }

    public void setDetalleCoest(String detalleCoest) {
        this.detalleCoest = detalleCoest;
    }

    public Boolean getActivoCoest() {
        return activoCoest;
    }

    public void setActivoCoest(Boolean activoCoest) {
        this.activoCoest = activoCoest;
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

    public List<ContConvenio> getContConvenioList() {
        return contConvenioList;
    }

    public void setContConvenioList(List<ContConvenio> contConvenioList) {
        this.contConvenioList = contConvenioList;
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

    public List<PreContrato> getPreContratoList() {
        return preContratoList;
    }

    public void setPreContratoList(List<PreContrato> preContratoList) {
        this.preContratoList = preContratoList;
    }

    public List<TesProveedor> getTesProveedorList() {
        return tesProveedorList;
    }

    public void setTesProveedorList(List<TesProveedor> tesProveedorList) {
        this.tesProveedorList = tesProveedorList;
    }

    public List<TesPoliza> getTesPolizaList() {
        return tesPolizaList;
    }

    public void setTesPolizaList(List<TesPoliza> tesPolizaList) {
        this.tesPolizaList = tesPolizaList;
    }

    public List<AfiCustodio> getAfiCustodioList() {
        return afiCustodioList;
    }

    public void setAfiCustodioList(List<AfiCustodio> afiCustodioList) {
        this.afiCustodioList = afiCustodioList;
    }

    public List<FacFactura> getFacFacturaList() {
        return facFacturaList;
    }

    public void setFacFacturaList(List<FacFactura> facFacturaList) {
        this.facFacturaList = facFacturaList;
    }

    public List<PreContratacionPublica> getPreContratacionPublicaList() {
        return preContratacionPublicaList;
    }

    public void setPreContratacionPublicaList(List<PreContratacionPublica> preContratacionPublicaList) {
        this.preContratacionPublicaList = preContratacionPublicaList;
    }

    public List<PrePac> getPrePacList() {
        return prePacList;
    }

    public void setPrePacList(List<PrePac> prePacList) {
        this.prePacList = prePacList;
    }

    public List<PrePoaReforma> getPrePoaReformaList() {
        return prePoaReformaList;
    }

    public void setPrePoaReformaList(List<PrePoaReforma> prePoaReformaList) {
        this.prePoaReformaList = prePoaReformaList;
    }

    public List<TesRenovacionPoliza> getTesRenovacionPolizaList() {
        return tesRenovacionPolizaList;
    }

    public void setTesRenovacionPolizaList(List<TesRenovacionPoliza> tesRenovacionPolizaList) {
        this.tesRenovacionPolizaList = tesRenovacionPolizaList;
    }

    public List<ContTiketViaje> getContTiketViajeList() {
        return contTiketViajeList;
    }

    public void setContTiketViajeList(List<ContTiketViaje> contTiketViajeList) {
        this.contTiketViajeList = contTiketViajeList;
    }

    public List<GenModuloEstado> getGenModuloEstadoList() {
        return genModuloEstadoList;
    }

    public void setGenModuloEstadoList(List<GenModuloEstado> genModuloEstadoList) {
        this.genModuloEstadoList = genModuloEstadoList;
    }

    public List<PrePoa> getPrePoaList() {
        return prePoaList;
    }

    public void setPrePoaList(List<PrePoa> prePoaList) {
        this.prePoaList = prePoaList;
    }

    public List<PrePoaFinanciamiento> getPrePoaFinanciamientoList() {
        return prePoaFinanciamientoList;
    }

    public void setPrePoaFinanciamientoList(List<PrePoaFinanciamiento> prePoaFinanciamientoList) {
        this.prePoaFinanciamientoList = prePoaFinanciamientoList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCoest != null ? ideCoest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContEstado)) {
            return false;
        }
        ContEstado other = (ContEstado) object;
        if ((this.ideCoest == null && other.ideCoest != null) || (this.ideCoest != null && !this.ideCoest.equals(other.ideCoest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContEstado[ ideCoest=" + ideCoest + " ]";
    }
    
}
