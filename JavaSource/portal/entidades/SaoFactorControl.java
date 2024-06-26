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
@Table(name = "sao_factor_control", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoFactorControl.findAll", query = "SELECT s FROM SaoFactorControl s"),
    @NamedQuery(name = "SaoFactorControl.findByIdeSafac", query = "SELECT s FROM SaoFactorControl s WHERE s.ideSafac = :ideSafac"),
    @NamedQuery(name = "SaoFactorControl.findByFactorCalifSafac", query = "SELECT s FROM SaoFactorControl s WHERE s.factorCalifSafac = :factorCalifSafac"),
    @NamedQuery(name = "SaoFactorControl.findByValRequeridoSafac", query = "SELECT s FROM SaoFactorControl s WHERE s.valRequeridoSafac = :valRequeridoSafac"),
    @NamedQuery(name = "SaoFactorControl.findByReporteRequeridoSafac", query = "SELECT s FROM SaoFactorControl s WHERE s.reporteRequeridoSafac = :reporteRequeridoSafac"),
    @NamedQuery(name = "SaoFactorControl.findByActivoSafac", query = "SELECT s FROM SaoFactorControl s WHERE s.activoSafac = :activoSafac"),
    @NamedQuery(name = "SaoFactorControl.findByUsuarioIngre", query = "SELECT s FROM SaoFactorControl s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoFactorControl.findByFechaIngre", query = "SELECT s FROM SaoFactorControl s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoFactorControl.findByHoraIngre", query = "SELECT s FROM SaoFactorControl s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoFactorControl.findByUsuarioActua", query = "SELECT s FROM SaoFactorControl s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoFactorControl.findByFechaActua", query = "SELECT s FROM SaoFactorControl s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoFactorControl.findByHoraActua", query = "SELECT s FROM SaoFactorControl s WHERE s.horaActua = :horaActua")})
public class SaoFactorControl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_safac", nullable = false)
    private Integer ideSafac;
    @Size(max = 100)
    @Column(name = "factor_calif_safac", length = 100)
    private String factorCalifSafac;
    @Size(max = 50)
    @Column(name = "val_requerido_safac", length = 50)
    private String valRequeridoSafac;
    @Size(max = 50)
    @Column(name = "reporte_requerido_safac", length = 50)
    private String reporteRequeridoSafac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_safac", nullable = false)
    private boolean activoSafac;
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
    @OneToMany(mappedBy = "ideSafac")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList;
    @JoinColumn(name = "ide_saclr", referencedColumnName = "ide_saclr")
    @ManyToOne
    private SaoClasificacionRiesgos ideSaclr;

    public SaoFactorControl() {
    }

    public SaoFactorControl(Integer ideSafac) {
        this.ideSafac = ideSafac;
    }

    public SaoFactorControl(Integer ideSafac, boolean activoSafac) {
        this.ideSafac = ideSafac;
        this.activoSafac = activoSafac;
    }

    public Integer getIdeSafac() {
        return ideSafac;
    }

    public void setIdeSafac(Integer ideSafac) {
        this.ideSafac = ideSafac;
    }

    public String getFactorCalifSafac() {
        return factorCalifSafac;
    }

    public void setFactorCalifSafac(String factorCalifSafac) {
        this.factorCalifSafac = factorCalifSafac;
    }

    public String getValRequeridoSafac() {
        return valRequeridoSafac;
    }

    public void setValRequeridoSafac(String valRequeridoSafac) {
        this.valRequeridoSafac = valRequeridoSafac;
    }

    public String getReporteRequeridoSafac() {
        return reporteRequeridoSafac;
    }

    public void setReporteRequeridoSafac(String reporteRequeridoSafac) {
        this.reporteRequeridoSafac = reporteRequeridoSafac;
    }

    public boolean getActivoSafac() {
        return activoSafac;
    }

    public void setActivoSafac(boolean activoSafac) {
        this.activoSafac = activoSafac;
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

    public SaoClasificacionRiesgos getIdeSaclr() {
        return ideSaclr;
    }

    public void setIdeSaclr(SaoClasificacionRiesgos ideSaclr) {
        this.ideSaclr = ideSaclr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSafac != null ? ideSafac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoFactorControl)) {
            return false;
        }
        SaoFactorControl other = (SaoFactorControl) object;
        if ((this.ideSafac == null && other.ideSafac != null) || (this.ideSafac != null && !this.ideSafac.equals(other.ideSafac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoFactorControl[ ideSafac=" + ideSafac + " ]";
    }
    
}
