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
@Table(name = "sri_deducibles", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SriDeducibles.findAll", query = "SELECT s FROM SriDeducibles s"),
    @NamedQuery(name = "SriDeducibles.findByIdeSrded", query = "SELECT s FROM SriDeducibles s WHERE s.ideSrded = :ideSrded"),
    @NamedQuery(name = "SriDeducibles.findByDetalleSrded", query = "SELECT s FROM SriDeducibles s WHERE s.detalleSrded = :detalleSrded"),
    @NamedQuery(name = "SriDeducibles.findByFraccionBasicaSrded", query = "SELECT s FROM SriDeducibles s WHERE s.fraccionBasicaSrded = :fraccionBasicaSrded"),
    @NamedQuery(name = "SriDeducibles.findByObservacionesSrded", query = "SELECT s FROM SriDeducibles s WHERE s.observacionesSrded = :observacionesSrded"),
    @NamedQuery(name = "SriDeducibles.findByAlternoSriSrded", query = "SELECT s FROM SriDeducibles s WHERE s.alternoSriSrded = :alternoSriSrded"),
    @NamedQuery(name = "SriDeducibles.findByActivoSrded", query = "SELECT s FROM SriDeducibles s WHERE s.activoSrded = :activoSrded"),
    @NamedQuery(name = "SriDeducibles.findByUsuarioIngre", query = "SELECT s FROM SriDeducibles s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriDeducibles.findByFechaIngre", query = "SELECT s FROM SriDeducibles s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriDeducibles.findByHoraIngre", query = "SELECT s FROM SriDeducibles s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SriDeducibles.findByUsuarioActua", query = "SELECT s FROM SriDeducibles s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriDeducibles.findByFechaActua", query = "SELECT s FROM SriDeducibles s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriDeducibles.findByHoraActua", query = "SELECT s FROM SriDeducibles s WHERE s.horaActua = :horaActua")})
public class SriDeducibles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_srded", nullable = false)
    private Integer ideSrded;
    @Size(max = 50)
    @Column(name = "detalle_srded", length = 50)
    private String detalleSrded;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "fraccion_basica_srded", precision = 12, scale = 3)
    private BigDecimal fraccionBasicaSrded;
    @Size(max = 1000)
    @Column(name = "observaciones_srded", length = 1000)
    private String observacionesSrded;
    @Column(name = "alterno_sri_srded")
    private Integer alternoSriSrded;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_srded", nullable = false)
    private boolean activoSrded;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @JoinColumn(name = "ide_srimr", referencedColumnName = "ide_srimr")
    @ManyToOne
    private SriImpuestoRenta ideSrimr;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideSrded")
    private List<SriDeduciblesEmpleado> sriDeduciblesEmpleadoList;

    public SriDeducibles() {
    }

    public SriDeducibles(Integer ideSrded) {
        this.ideSrded = ideSrded;
    }

    public SriDeducibles(Integer ideSrded, boolean activoSrded) {
        this.ideSrded = ideSrded;
        this.activoSrded = activoSrded;
    }

    public Integer getIdeSrded() {
        return ideSrded;
    }

    public void setIdeSrded(Integer ideSrded) {
        this.ideSrded = ideSrded;
    }

    public String getDetalleSrded() {
        return detalleSrded;
    }

    public void setDetalleSrded(String detalleSrded) {
        this.detalleSrded = detalleSrded;
    }

    public BigDecimal getFraccionBasicaSrded() {
        return fraccionBasicaSrded;
    }

    public void setFraccionBasicaSrded(BigDecimal fraccionBasicaSrded) {
        this.fraccionBasicaSrded = fraccionBasicaSrded;
    }

    public String getObservacionesSrded() {
        return observacionesSrded;
    }

    public void setObservacionesSrded(String observacionesSrded) {
        this.observacionesSrded = observacionesSrded;
    }

    public Integer getAlternoSriSrded() {
        return alternoSriSrded;
    }

    public void setAlternoSriSrded(Integer alternoSriSrded) {
        this.alternoSriSrded = alternoSriSrded;
    }

    public boolean getActivoSrded() {
        return activoSrded;
    }

    public void setActivoSrded(boolean activoSrded) {
        this.activoSrded = activoSrded;
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

    public SriImpuestoRenta getIdeSrimr() {
        return ideSrimr;
    }

    public void setIdeSrimr(SriImpuestoRenta ideSrimr) {
        this.ideSrimr = ideSrimr;
    }

    public List<SriDeduciblesEmpleado> getSriDeduciblesEmpleadoList() {
        return sriDeduciblesEmpleadoList;
    }

    public void setSriDeduciblesEmpleadoList(List<SriDeduciblesEmpleado> sriDeduciblesEmpleadoList) {
        this.sriDeduciblesEmpleadoList = sriDeduciblesEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrded != null ? ideSrded.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriDeducibles)) {
            return false;
        }
        SriDeducibles other = (SriDeducibles) object;
        if ((this.ideSrded == null && other.ideSrded != null) || (this.ideSrded != null && !this.ideSrded.equals(other.ideSrded))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriDeducibles[ ideSrded=" + ideSrded + " ]";
    }
    
}
