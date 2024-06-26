/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "afi_custodio", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiCustodio.findAll", query = "SELECT a FROM AfiCustodio a"),
    @NamedQuery(name = "AfiCustodio.findByIdeAfcus", query = "SELECT a FROM AfiCustodio a WHERE a.ideAfcus = :ideAfcus"),
    @NamedQuery(name = "AfiCustodio.findByDetalleAfcus", query = "SELECT a FROM AfiCustodio a WHERE a.detalleAfcus = :detalleAfcus"),
    @NamedQuery(name = "AfiCustodio.findByFechaEntregaAfcus", query = "SELECT a FROM AfiCustodio a WHERE a.fechaEntregaAfcus = :fechaEntregaAfcus"),
    @NamedQuery(name = "AfiCustodio.findByFechaDescargoAfcus", query = "SELECT a FROM AfiCustodio a WHERE a.fechaDescargoAfcus = :fechaDescargoAfcus"),
    @NamedQuery(name = "AfiCustodio.findByNumeroActaAfcus", query = "SELECT a FROM AfiCustodio a WHERE a.numeroActaAfcus = :numeroActaAfcus"),
    @NamedQuery(name = "AfiCustodio.findByRazonDescargoAfcus", query = "SELECT a FROM AfiCustodio a WHERE a.razonDescargoAfcus = :razonDescargoAfcus"),
    @NamedQuery(name = "AfiCustodio.findByCodBarraAfcus", query = "SELECT a FROM AfiCustodio a WHERE a.codBarraAfcus = :codBarraAfcus"),
    @NamedQuery(name = "AfiCustodio.findByNroSecuencialAfcus", query = "SELECT a FROM AfiCustodio a WHERE a.nroSecuencialAfcus = :nroSecuencialAfcus"),
    @NamedQuery(name = "AfiCustodio.findByActivoAfcus", query = "SELECT a FROM AfiCustodio a WHERE a.activoAfcus = :activoAfcus"),
    @NamedQuery(name = "AfiCustodio.findByUsuarioIngre", query = "SELECT a FROM AfiCustodio a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiCustodio.findByFechaIngre", query = "SELECT a FROM AfiCustodio a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiCustodio.findByHoraIngre", query = "SELECT a FROM AfiCustodio a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiCustodio.findByUsuarioActua", query = "SELECT a FROM AfiCustodio a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiCustodio.findByFechaActua", query = "SELECT a FROM AfiCustodio a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiCustodio.findByHoraActua", query = "SELECT a FROM AfiCustodio a WHERE a.horaActua = :horaActua")})
public class AfiCustodio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_afcus", nullable = false)
    private Long ideAfcus;
    @Size(max = 50)
    @Column(name = "detalle_afcus", length = 50)
    private String detalleAfcus;
    @Column(name = "fecha_entrega_afcus")
    @Temporal(TemporalType.DATE)
    private Date fechaEntregaAfcus;
    @Column(name = "fecha_descargo_afcus")
    @Temporal(TemporalType.DATE)
    private Date fechaDescargoAfcus;
    @Size(max = 20)
    @Column(name = "numero_acta_afcus", length = 20)
    private String numeroActaAfcus;
    @Size(max = 2147483647)
    @Column(name = "razon_descargo_afcus", length = 2147483647)
    private String razonDescargoAfcus;
    @Size(max = 50)
    @Column(name = "cod_barra_afcus", length = 50)
    private String codBarraAfcus;
    @Column(name = "nro_secuencial_afcus")
    private BigInteger nroSecuencialAfcus;
    @Column(name = "activo_afcus")
    private Boolean activoAfcus;
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
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @OneToMany(mappedBy = "afiIdeAfcus")
    private List<AfiCustodio> afiCustodioList;
    @JoinColumn(name = "afi_ide_afcus", referencedColumnName = "ide_afcus")
    @ManyToOne
    private AfiCustodio afiIdeAfcus;
    @JoinColumn(name = "ide_afact", referencedColumnName = "ide_afact")
    @ManyToOne
    private AfiActivo ideAfact;

    public AfiCustodio() {
    }

    public AfiCustodio(Long ideAfcus) {
        this.ideAfcus = ideAfcus;
    }

    public Long getIdeAfcus() {
        return ideAfcus;
    }

    public void setIdeAfcus(Long ideAfcus) {
        this.ideAfcus = ideAfcus;
    }

    public String getDetalleAfcus() {
        return detalleAfcus;
    }

    public void setDetalleAfcus(String detalleAfcus) {
        this.detalleAfcus = detalleAfcus;
    }

    public Date getFechaEntregaAfcus() {
        return fechaEntregaAfcus;
    }

    public void setFechaEntregaAfcus(Date fechaEntregaAfcus) {
        this.fechaEntregaAfcus = fechaEntregaAfcus;
    }

    public Date getFechaDescargoAfcus() {
        return fechaDescargoAfcus;
    }

    public void setFechaDescargoAfcus(Date fechaDescargoAfcus) {
        this.fechaDescargoAfcus = fechaDescargoAfcus;
    }

    public String getNumeroActaAfcus() {
        return numeroActaAfcus;
    }

    public void setNumeroActaAfcus(String numeroActaAfcus) {
        this.numeroActaAfcus = numeroActaAfcus;
    }

    public String getRazonDescargoAfcus() {
        return razonDescargoAfcus;
    }

    public void setRazonDescargoAfcus(String razonDescargoAfcus) {
        this.razonDescargoAfcus = razonDescargoAfcus;
    }

    public String getCodBarraAfcus() {
        return codBarraAfcus;
    }

    public void setCodBarraAfcus(String codBarraAfcus) {
        this.codBarraAfcus = codBarraAfcus;
    }

    public BigInteger getNroSecuencialAfcus() {
        return nroSecuencialAfcus;
    }

    public void setNroSecuencialAfcus(BigInteger nroSecuencialAfcus) {
        this.nroSecuencialAfcus = nroSecuencialAfcus;
    }

    public Boolean getActivoAfcus() {
        return activoAfcus;
    }

    public void setActivoAfcus(Boolean activoAfcus) {
        this.activoAfcus = activoAfcus;
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

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public List<AfiCustodio> getAfiCustodioList() {
        return afiCustodioList;
    }

    public void setAfiCustodioList(List<AfiCustodio> afiCustodioList) {
        this.afiCustodioList = afiCustodioList;
    }

    public AfiCustodio getAfiIdeAfcus() {
        return afiIdeAfcus;
    }

    public void setAfiIdeAfcus(AfiCustodio afiIdeAfcus) {
        this.afiIdeAfcus = afiIdeAfcus;
    }

    public AfiActivo getIdeAfact() {
        return ideAfact;
    }

    public void setIdeAfact(AfiActivo ideAfact) {
        this.ideAfact = ideAfact;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAfcus != null ? ideAfcus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiCustodio)) {
            return false;
        }
        AfiCustodio other = (AfiCustodio) object;
        if ((this.ideAfcus == null && other.ideAfcus != null) || (this.ideAfcus != null && !this.ideAfcus.equals(other.ideAfcus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiCustodio[ ideAfcus=" + ideAfcus + " ]";
    }
    
}
