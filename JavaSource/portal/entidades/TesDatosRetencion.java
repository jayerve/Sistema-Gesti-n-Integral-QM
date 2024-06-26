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
@Table(name = "tes_datos_retencion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesDatosRetencion.findAll", query = "SELECT t FROM TesDatosRetencion t"),
    @NamedQuery(name = "TesDatosRetencion.findByIdeTedar", query = "SELECT t FROM TesDatosRetencion t WHERE t.ideTedar = :ideTedar"),
    @NamedQuery(name = "TesDatosRetencion.findByIdeSucu", query = "SELECT t FROM TesDatosRetencion t WHERE t.ideSucu = :ideSucu"),
    @NamedQuery(name = "TesDatosRetencion.findByIdeGedep", query = "SELECT t FROM TesDatosRetencion t WHERE t.ideGedep = :ideGedep"),
    @NamedQuery(name = "TesDatosRetencion.findByIdeComov", query = "SELECT t FROM TesDatosRetencion t WHERE t.ideComov = :ideComov"),
    @NamedQuery(name = "TesDatosRetencion.findByFechaEmisionTedar", query = "SELECT t FROM TesDatosRetencion t WHERE t.fechaEmisionTedar = :fechaEmisionTedar"),
    @NamedQuery(name = "TesDatosRetencion.findByObservacionTedar", query = "SELECT t FROM TesDatosRetencion t WHERE t.observacionTedar = :observacionTedar"),
    @NamedQuery(name = "TesDatosRetencion.findByNumRetencionTedar", query = "SELECT t FROM TesDatosRetencion t WHERE t.numRetencionTedar = :numRetencionTedar"),
    @NamedQuery(name = "TesDatosRetencion.findByAutorizacionTedar", query = "SELECT t FROM TesDatosRetencion t WHERE t.autorizacionTedar = :autorizacionTedar"),
    @NamedQuery(name = "TesDatosRetencion.findByTipoRetencionTedar", query = "SELECT t FROM TesDatosRetencion t WHERE t.tipoRetencionTedar = :tipoRetencionTedar"),
    @NamedQuery(name = "TesDatosRetencion.findByActivoTedar", query = "SELECT t FROM TesDatosRetencion t WHERE t.activoTedar = :activoTedar"),
    @NamedQuery(name = "TesDatosRetencion.findByUsuarioIngre", query = "SELECT t FROM TesDatosRetencion t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesDatosRetencion.findByFechaIngre", query = "SELECT t FROM TesDatosRetencion t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesDatosRetencion.findByHoraIngre", query = "SELECT t FROM TesDatosRetencion t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesDatosRetencion.findByUsuarioActua", query = "SELECT t FROM TesDatosRetencion t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesDatosRetencion.findByFechaActua", query = "SELECT t FROM TesDatosRetencion t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesDatosRetencion.findByHoraActua", query = "SELECT t FROM TesDatosRetencion t WHERE t.horaActua = :horaActua")})
public class TesDatosRetencion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tedar", nullable = false)
    private Long ideTedar;
    @Column(name = "ide_sucu")
    private BigInteger ideSucu;
    @Column(name = "ide_gedep")
    private BigInteger ideGedep;
    @Column(name = "ide_comov")
    private BigInteger ideComov;
    @Column(name = "fecha_emision_tedar")
    @Temporal(TemporalType.DATE)
    private Date fechaEmisionTedar;
    @Size(max = 2147483647)
    @Column(name = "observacion_tedar", length = 2147483647)
    private String observacionTedar;
    @Size(max = 20)
    @Column(name = "num_retencion_tedar", length = 20)
    private String numRetencionTedar;
    @Size(max = 20)
    @Column(name = "autorizacion_tedar", length = 20)
    private String autorizacionTedar;
    @Column(name = "tipo_retencion_tedar")
    private BigInteger tipoRetencionTedar;
    @Column(name = "activo_tedar")
    private Boolean activoTedar;
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
    @OneToMany(mappedBy = "ideTedar")
    private List<FacFactura> facFacturaList;
    @JoinColumn(name = "ide_teesr", referencedColumnName = "ide_teesr")
    @ManyToOne
    private TesEstadoRetencion ideTeesr;

    public TesDatosRetencion() {
    }

    public TesDatosRetencion(Long ideTedar) {
        this.ideTedar = ideTedar;
    }

    public Long getIdeTedar() {
        return ideTedar;
    }

    public void setIdeTedar(Long ideTedar) {
        this.ideTedar = ideTedar;
    }

    public BigInteger getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(BigInteger ideSucu) {
        this.ideSucu = ideSucu;
    }

    public BigInteger getIdeGedep() {
        return ideGedep;
    }

    public void setIdeGedep(BigInteger ideGedep) {
        this.ideGedep = ideGedep;
    }

    public BigInteger getIdeComov() {
        return ideComov;
    }

    public void setIdeComov(BigInteger ideComov) {
        this.ideComov = ideComov;
    }

    public Date getFechaEmisionTedar() {
        return fechaEmisionTedar;
    }

    public void setFechaEmisionTedar(Date fechaEmisionTedar) {
        this.fechaEmisionTedar = fechaEmisionTedar;
    }

    public String getObservacionTedar() {
        return observacionTedar;
    }

    public void setObservacionTedar(String observacionTedar) {
        this.observacionTedar = observacionTedar;
    }

    public String getNumRetencionTedar() {
        return numRetencionTedar;
    }

    public void setNumRetencionTedar(String numRetencionTedar) {
        this.numRetencionTedar = numRetencionTedar;
    }

    public String getAutorizacionTedar() {
        return autorizacionTedar;
    }

    public void setAutorizacionTedar(String autorizacionTedar) {
        this.autorizacionTedar = autorizacionTedar;
    }

    public BigInteger getTipoRetencionTedar() {
        return tipoRetencionTedar;
    }

    public void setTipoRetencionTedar(BigInteger tipoRetencionTedar) {
        this.tipoRetencionTedar = tipoRetencionTedar;
    }

    public Boolean getActivoTedar() {
        return activoTedar;
    }

    public void setActivoTedar(Boolean activoTedar) {
        this.activoTedar = activoTedar;
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

    public List<FacFactura> getFacFacturaList() {
        return facFacturaList;
    }

    public void setFacFacturaList(List<FacFactura> facFacturaList) {
        this.facFacturaList = facFacturaList;
    }

    public TesEstadoRetencion getIdeTeesr() {
        return ideTeesr;
    }

    public void setIdeTeesr(TesEstadoRetencion ideTeesr) {
        this.ideTeesr = ideTeesr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTedar != null ? ideTedar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesDatosRetencion)) {
            return false;
        }
        TesDatosRetencion other = (TesDatosRetencion) object;
        if ((this.ideTedar == null && other.ideTedar != null) || (this.ideTedar != null && !this.ideTedar.equals(other.ideTedar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesDatosRetencion[ ideTedar=" + ideTedar + " ]";
    }
    
}
