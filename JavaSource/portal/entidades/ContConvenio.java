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
@Table(name = "cont_convenio", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContConvenio.findAll", query = "SELECT c FROM ContConvenio c"),
    @NamedQuery(name = "ContConvenio.findByIdeCocon", query = "SELECT c FROM ContConvenio c WHERE c.ideCocon = :ideCocon"),
    @NamedQuery(name = "ContConvenio.findByDetalleContratoCocon", query = "SELECT c FROM ContConvenio c WHERE c.detalleContratoCocon = :detalleContratoCocon"),
    @NamedQuery(name = "ContConvenio.findByFechaInicioCocon", query = "SELECT c FROM ContConvenio c WHERE c.fechaInicioCocon = :fechaInicioCocon"),
    @NamedQuery(name = "ContConvenio.findByFechaFinCocon", query = "SELECT c FROM ContConvenio c WHERE c.fechaFinCocon = :fechaFinCocon"),
    @NamedQuery(name = "ContConvenio.findByNumContratoCocon", query = "SELECT c FROM ContConvenio c WHERE c.numContratoCocon = :numContratoCocon"),
    @NamedQuery(name = "ContConvenio.findByValorCocon", query = "SELECT c FROM ContConvenio c WHERE c.valorCocon = :valorCocon"),
    @NamedQuery(name = "ContConvenio.findByRazonAnuladoCocon", query = "SELECT c FROM ContConvenio c WHERE c.razonAnuladoCocon = :razonAnuladoCocon"),
    @NamedQuery(name = "ContConvenio.findByFechaAnuladoCocon", query = "SELECT c FROM ContConvenio c WHERE c.fechaAnuladoCocon = :fechaAnuladoCocon"),
    @NamedQuery(name = "ContConvenio.findByActivoCocon", query = "SELECT c FROM ContConvenio c WHERE c.activoCocon = :activoCocon"),
    @NamedQuery(name = "ContConvenio.findByUsuarioIngre", query = "SELECT c FROM ContConvenio c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContConvenio.findByFechaIngre", query = "SELECT c FROM ContConvenio c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContConvenio.findByHoraIngre", query = "SELECT c FROM ContConvenio c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContConvenio.findByUsuarioActua", query = "SELECT c FROM ContConvenio c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContConvenio.findByFechaActua", query = "SELECT c FROM ContConvenio c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContConvenio.findByHoraActua", query = "SELECT c FROM ContConvenio c WHERE c.horaActua = :horaActua")})
public class ContConvenio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cocon", nullable = false)
    private Long ideCocon;
    @Size(max = 2147483647)
    @Column(name = "detalle_contrato_cocon", length = 2147483647)
    private String detalleContratoCocon;
    @Column(name = "fecha_inicio_cocon")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioCocon;
    @Column(name = "fecha_fin_cocon")
    @Temporal(TemporalType.DATE)
    private Date fechaFinCocon;
    @Size(max = 50)
    @Column(name = "num_contrato_cocon", length = 50)
    private String numContratoCocon;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_cocon", precision = 10, scale = 2)
    private BigDecimal valorCocon;
    @Size(max = 2147483647)
    @Column(name = "razon_anulado_cocon", length = 2147483647)
    private String razonAnuladoCocon;
    @Column(name = "fecha_anulado_cocon")
    @Temporal(TemporalType.DATE)
    private Date fechaAnuladoCocon;
    @Column(name = "activo_cocon")
    private Boolean activoCocon;
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
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;
    @JoinColumn(name = "ide_cotie", referencedColumnName = "ide_cotie")
    @ManyToOne
    private ContTipoConvenio ideCotie;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @OneToMany(mappedBy = "conIdeCocon")
    private List<ContConvenio> contConvenioList;
    @JoinColumn(name = "con_ide_cocon", referencedColumnName = "ide_cocon")
    @ManyToOne
    private ContConvenio conIdeCocon;
    @OneToMany(mappedBy = "ideCocon")
    private List<ContResponsableConvenio> contResponsableConvenioList;

    public ContConvenio() {
    }

    public ContConvenio(Long ideCocon) {
        this.ideCocon = ideCocon;
    }

    public Long getIdeCocon() {
        return ideCocon;
    }

    public void setIdeCocon(Long ideCocon) {
        this.ideCocon = ideCocon;
    }

    public String getDetalleContratoCocon() {
        return detalleContratoCocon;
    }

    public void setDetalleContratoCocon(String detalleContratoCocon) {
        this.detalleContratoCocon = detalleContratoCocon;
    }

    public Date getFechaInicioCocon() {
        return fechaInicioCocon;
    }

    public void setFechaInicioCocon(Date fechaInicioCocon) {
        this.fechaInicioCocon = fechaInicioCocon;
    }

    public Date getFechaFinCocon() {
        return fechaFinCocon;
    }

    public void setFechaFinCocon(Date fechaFinCocon) {
        this.fechaFinCocon = fechaFinCocon;
    }

    public String getNumContratoCocon() {
        return numContratoCocon;
    }

    public void setNumContratoCocon(String numContratoCocon) {
        this.numContratoCocon = numContratoCocon;
    }

    public BigDecimal getValorCocon() {
        return valorCocon;
    }

    public void setValorCocon(BigDecimal valorCocon) {
        this.valorCocon = valorCocon;
    }

    public String getRazonAnuladoCocon() {
        return razonAnuladoCocon;
    }

    public void setRazonAnuladoCocon(String razonAnuladoCocon) {
        this.razonAnuladoCocon = razonAnuladoCocon;
    }

    public Date getFechaAnuladoCocon() {
        return fechaAnuladoCocon;
    }

    public void setFechaAnuladoCocon(Date fechaAnuladoCocon) {
        this.fechaAnuladoCocon = fechaAnuladoCocon;
    }

    public Boolean getActivoCocon() {
        return activoCocon;
    }

    public void setActivoCocon(Boolean activoCocon) {
        this.activoCocon = activoCocon;
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

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    public ContTipoConvenio getIdeCotie() {
        return ideCotie;
    }

    public void setIdeCotie(ContTipoConvenio ideCotie) {
        this.ideCotie = ideCotie;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public List<ContConvenio> getContConvenioList() {
        return contConvenioList;
    }

    public void setContConvenioList(List<ContConvenio> contConvenioList) {
        this.contConvenioList = contConvenioList;
    }

    public ContConvenio getConIdeCocon() {
        return conIdeCocon;
    }

    public void setConIdeCocon(ContConvenio conIdeCocon) {
        this.conIdeCocon = conIdeCocon;
    }

    public List<ContResponsableConvenio> getContResponsableConvenioList() {
        return contResponsableConvenioList;
    }

    public void setContResponsableConvenioList(List<ContResponsableConvenio> contResponsableConvenioList) {
        this.contResponsableConvenioList = contResponsableConvenioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCocon != null ? ideCocon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContConvenio)) {
            return false;
        }
        ContConvenio other = (ContConvenio) object;
        if ((this.ideCocon == null && other.ideCocon != null) || (this.ideCocon != null && !this.ideCocon.equals(other.ideCocon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContConvenio[ ideCocon=" + ideCocon + " ]";
    }
    
}
