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
@Table(name = "sao_clasificacion_riesgos", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoClasificacionRiesgos.findAll", query = "SELECT s FROM SaoClasificacionRiesgos s"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByIdeSaclr", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.ideSaclr = :ideSaclr"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByDetalleSaclr", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.detalleSaclr = :detalleSaclr"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByCodigoSaclr", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.codigoSaclr = :codigoSaclr"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByObservacionSaclr", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.observacionSaclr = :observacionSaclr"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByActivoSaclr", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.activoSaclr = :activoSaclr"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByUsuarioIngre", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByFechaIngre", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByHoraIngre", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByUsuarioActua", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByFechaActua", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoClasificacionRiesgos.findByHoraActua", query = "SELECT s FROM SaoClasificacionRiesgos s WHERE s.horaActua = :horaActua")})
public class SaoClasificacionRiesgos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_saclr", nullable = false)
    private Integer ideSaclr;
    @Size(max = 100)
    @Column(name = "detalle_saclr", length = 100)
    private String detalleSaclr;
    @Size(max = 50)
    @Column(name = "codigo_saclr", length = 50)
    private String codigoSaclr;
    @Size(max = 4000)
    @Column(name = "observacion_saclr", length = 4000)
    private String observacionSaclr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_saclr", nullable = false)
    private boolean activoSaclr;
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
    @OneToMany(mappedBy = "ideSaclr")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList;
    @OneToMany(mappedBy = "saoIdeSaclr")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList1;
    @OneToMany(mappedBy = "ideSaclr")
    private List<SaoFactorControl> saoFactorControlList;
    @OneToMany(mappedBy = "saoIdeSaclr")
    private List<SaoClasificacionRiesgos> saoClasificacionRiesgosList;
    @JoinColumn(name = "sao_ide_saclr", referencedColumnName = "ide_saclr")
    @ManyToOne
    private SaoClasificacionRiesgos saoIdeSaclr;

    public SaoClasificacionRiesgos() {
    }

    public SaoClasificacionRiesgos(Integer ideSaclr) {
        this.ideSaclr = ideSaclr;
    }

    public SaoClasificacionRiesgos(Integer ideSaclr, boolean activoSaclr) {
        this.ideSaclr = ideSaclr;
        this.activoSaclr = activoSaclr;
    }

    public Integer getIdeSaclr() {
        return ideSaclr;
    }

    public void setIdeSaclr(Integer ideSaclr) {
        this.ideSaclr = ideSaclr;
    }

    public String getDetalleSaclr() {
        return detalleSaclr;
    }

    public void setDetalleSaclr(String detalleSaclr) {
        this.detalleSaclr = detalleSaclr;
    }

    public String getCodigoSaclr() {
        return codigoSaclr;
    }

    public void setCodigoSaclr(String codigoSaclr) {
        this.codigoSaclr = codigoSaclr;
    }

    public String getObservacionSaclr() {
        return observacionSaclr;
    }

    public void setObservacionSaclr(String observacionSaclr) {
        this.observacionSaclr = observacionSaclr;
    }

    public boolean getActivoSaclr() {
        return activoSaclr;
    }

    public void setActivoSaclr(boolean activoSaclr) {
        this.activoSaclr = activoSaclr;
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

    public List<SaoDetalleMatrizRiesgo> getSaoDetalleMatrizRiesgoList() {
        return saoDetalleMatrizRiesgoList;
    }

    public void setSaoDetalleMatrizRiesgoList(List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList) {
        this.saoDetalleMatrizRiesgoList = saoDetalleMatrizRiesgoList;
    }

    public List<SaoDetalleMatrizRiesgo> getSaoDetalleMatrizRiesgoList1() {
        return saoDetalleMatrizRiesgoList1;
    }

    public void setSaoDetalleMatrizRiesgoList1(List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList1) {
        this.saoDetalleMatrizRiesgoList1 = saoDetalleMatrizRiesgoList1;
    }

    public List<SaoFactorControl> getSaoFactorControlList() {
        return saoFactorControlList;
    }

    public void setSaoFactorControlList(List<SaoFactorControl> saoFactorControlList) {
        this.saoFactorControlList = saoFactorControlList;
    }

    public List<SaoClasificacionRiesgos> getSaoClasificacionRiesgosList() {
        return saoClasificacionRiesgosList;
    }

    public void setSaoClasificacionRiesgosList(List<SaoClasificacionRiesgos> saoClasificacionRiesgosList) {
        this.saoClasificacionRiesgosList = saoClasificacionRiesgosList;
    }

    public SaoClasificacionRiesgos getSaoIdeSaclr() {
        return saoIdeSaclr;
    }

    public void setSaoIdeSaclr(SaoClasificacionRiesgos saoIdeSaclr) {
        this.saoIdeSaclr = saoIdeSaclr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSaclr != null ? ideSaclr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoClasificacionRiesgos)) {
            return false;
        }
        SaoClasificacionRiesgos other = (SaoClasificacionRiesgos) object;
        if ((this.ideSaclr == null && other.ideSaclr != null) || (this.ideSaclr != null && !this.ideSaclr.equals(other.ideSaclr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoClasificacionRiesgos[ ideSaclr=" + ideSaclr + " ]";
    }
    
}
