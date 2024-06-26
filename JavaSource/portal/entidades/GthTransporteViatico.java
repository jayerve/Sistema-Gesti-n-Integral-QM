/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "gth_transporte_viatico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTransporteViatico.findAll", query = "SELECT g FROM GthTransporteViatico g"),
    @NamedQuery(name = "GthTransporteViatico.findByIdeGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.ideGttrv = :ideGttrv"),
    @NamedQuery(name = "GthTransporteViatico.findByRutaGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.rutaGttrv = :rutaGttrv"),
    @NamedQuery(name = "GthTransporteViatico.findByFechaSalidaGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.fechaSalidaGttrv = :fechaSalidaGttrv"),
    @NamedQuery(name = "GthTransporteViatico.findByFechaLlegadaGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.fechaLlegadaGttrv = :fechaLlegadaGttrv"),
    @NamedQuery(name = "GthTransporteViatico.findByInstitucionalGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.institucionalGttrv = :institucionalGttrv"),
    @NamedQuery(name = "GthTransporteViatico.findByPlacaGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.placaGttrv = :placaGttrv"),
    @NamedQuery(name = "GthTransporteViatico.findByObservacionGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.observacionGttrv = :observacionGttrv"),
    @NamedQuery(name = "GthTransporteViatico.findByActivoGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.activoGttrv = :activoGttrv"),
    @NamedQuery(name = "GthTransporteViatico.findByUsuarioIngre", query = "SELECT g FROM GthTransporteViatico g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTransporteViatico.findByFechaIngre", query = "SELECT g FROM GthTransporteViatico g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTransporteViatico.findByUsuarioActua", query = "SELECT g FROM GthTransporteViatico g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTransporteViatico.findByFechaActua", query = "SELECT g FROM GthTransporteViatico g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTransporteViatico.findByHoraIngre", query = "SELECT g FROM GthTransporteViatico g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTransporteViatico.findByHoraActua", query = "SELECT g FROM GthTransporteViatico g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthTransporteViatico.findByHoraSalidaGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.horaSalidaGttrv = :horaSalidaGttrv"),
    @NamedQuery(name = "GthTransporteViatico.findByHoraLlegadaGttrv", query = "SELECT g FROM GthTransporteViatico g WHERE g.horaLlegadaGttrv = :horaLlegadaGttrv")})
public class GthTransporteViatico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttrv", nullable = false)
    private Integer ideGttrv;
    @Size(max = 50)
    @Column(name = "ruta_gttrv", length = 50)
    private String rutaGttrv;
    @Column(name = "fecha_salida_gttrv")
    @Temporal(TemporalType.DATE)
    private Date fechaSalidaGttrv;
    @Column(name = "fecha_llegada_gttrv")
    @Temporal(TemporalType.DATE)
    private Date fechaLlegadaGttrv;
    @Column(name = "institucional_gttrv")
    private Integer institucionalGttrv;
    @Size(max = 50)
    @Column(name = "placa_gttrv", length = 50)
    private String placaGttrv;
    @Size(max = 1000)
    @Column(name = "observacion_gttrv", length = 1000)
    private String observacionGttrv;
    @Column(name = "activo_gttrv")
    private Boolean activoGttrv;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "hora_salida_gttrv")
    @Temporal(TemporalType.TIME)
    private Date horaSalidaGttrv;
    @Column(name = "hora_llegada_gttrv")
    @Temporal(TemporalType.TIME)
    private Date horaLlegadaGttrv;
    @JoinColumn(name = "ide_gtvia", referencedColumnName = "ide_gtvia")
    @ManyToOne
    private GthViaticos ideGtvia;
    @JoinColumn(name = "ide_gtttv", referencedColumnName = "ide_gtttv")
    @ManyToOne
    private GthTipoTransporteViatico ideGtttv;
    @JoinColumn(name = "ide_geins", referencedColumnName = "ide_geins")
    @ManyToOne
    private GenInstitucion ideGeins;

    public GthTransporteViatico() {
    }

    public GthTransporteViatico(Integer ideGttrv) {
        this.ideGttrv = ideGttrv;
    }

    public Integer getIdeGttrv() {
        return ideGttrv;
    }

    public void setIdeGttrv(Integer ideGttrv) {
        this.ideGttrv = ideGttrv;
    }

    public String getRutaGttrv() {
        return rutaGttrv;
    }

    public void setRutaGttrv(String rutaGttrv) {
        this.rutaGttrv = rutaGttrv;
    }

    public Date getFechaSalidaGttrv() {
        return fechaSalidaGttrv;
    }

    public void setFechaSalidaGttrv(Date fechaSalidaGttrv) {
        this.fechaSalidaGttrv = fechaSalidaGttrv;
    }

    public Date getFechaLlegadaGttrv() {
        return fechaLlegadaGttrv;
    }

    public void setFechaLlegadaGttrv(Date fechaLlegadaGttrv) {
        this.fechaLlegadaGttrv = fechaLlegadaGttrv;
    }

    public Integer getInstitucionalGttrv() {
        return institucionalGttrv;
    }

    public void setInstitucionalGttrv(Integer institucionalGttrv) {
        this.institucionalGttrv = institucionalGttrv;
    }

    public String getPlacaGttrv() {
        return placaGttrv;
    }

    public void setPlacaGttrv(String placaGttrv) {
        this.placaGttrv = placaGttrv;
    }

    public String getObservacionGttrv() {
        return observacionGttrv;
    }

    public void setObservacionGttrv(String observacionGttrv) {
        this.observacionGttrv = observacionGttrv;
    }

    public Boolean getActivoGttrv() {
        return activoGttrv;
    }

    public void setActivoGttrv(Boolean activoGttrv) {
        this.activoGttrv = activoGttrv;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public Date getHoraSalidaGttrv() {
        return horaSalidaGttrv;
    }

    public void setHoraSalidaGttrv(Date horaSalidaGttrv) {
        this.horaSalidaGttrv = horaSalidaGttrv;
    }

    public Date getHoraLlegadaGttrv() {
        return horaLlegadaGttrv;
    }

    public void setHoraLlegadaGttrv(Date horaLlegadaGttrv) {
        this.horaLlegadaGttrv = horaLlegadaGttrv;
    }

    public GthViaticos getIdeGtvia() {
        return ideGtvia;
    }

    public void setIdeGtvia(GthViaticos ideGtvia) {
        this.ideGtvia = ideGtvia;
    }

    public GthTipoTransporteViatico getIdeGtttv() {
        return ideGtttv;
    }

    public void setIdeGtttv(GthTipoTransporteViatico ideGtttv) {
        this.ideGtttv = ideGtttv;
    }

    public GenInstitucion getIdeGeins() {
        return ideGeins;
    }

    public void setIdeGeins(GenInstitucion ideGeins) {
        this.ideGeins = ideGeins;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttrv != null ? ideGttrv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTransporteViatico)) {
            return false;
        }
        GthTransporteViatico other = (GthTransporteViatico) object;
        if ((this.ideGttrv == null && other.ideGttrv != null) || (this.ideGttrv != null && !this.ideGttrv.equals(other.ideGttrv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTransporteViatico[ ideGttrv=" + ideGttrv + " ]";
    }
    
}
