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
@Table(name = "sri_impuesto_renta", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SriImpuestoRenta.findAll", query = "SELECT s FROM SriImpuestoRenta s"),
    @NamedQuery(name = "SriImpuestoRenta.findByIdeSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.ideSrimr = :ideSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByDetalleSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.detalleSrimr = :detalleSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByFechaInicioSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.fechaInicioSrimr = :fechaInicioSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByFechaFinSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.fechaFinSrimr = :fechaFinSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByActivoSrimr", query = "SELECT s FROM SriImpuestoRenta s WHERE s.activoSrimr = :activoSrimr"),
    @NamedQuery(name = "SriImpuestoRenta.findByUsuarioIngre", query = "SELECT s FROM SriImpuestoRenta s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SriImpuestoRenta.findByFechaIngre", query = "SELECT s FROM SriImpuestoRenta s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SriImpuestoRenta.findByHoraIngre", query = "SELECT s FROM SriImpuestoRenta s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SriImpuestoRenta.findByUsuarioActua", query = "SELECT s FROM SriImpuestoRenta s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SriImpuestoRenta.findByFechaActua", query = "SELECT s FROM SriImpuestoRenta s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SriImpuestoRenta.findByHoraActua", query = "SELECT s FROM SriImpuestoRenta s WHERE s.horaActua = :horaActua")})
public class SriImpuestoRenta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_srimr", nullable = false)
    private Integer ideSrimr;
    @Size(max = 50)
    @Column(name = "detalle_srimr", length = 50)
    private String detalleSrimr;
    @Column(name = "fecha_inicio_srimr")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioSrimr;
    @Column(name = "fecha_fin_srimr")
    @Temporal(TemporalType.DATE)
    private Date fechaFinSrimr;
    @Column(name = "activo_srimr")
    private Boolean activoSrimr;
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
    @OneToMany(mappedBy = "ideSrimr")
    private List<SriProyeccionIngres> sriProyeccionIngresList;
    @OneToMany(mappedBy = "ideSrimr")
    private List<SriDetalleImpuestoRenta> sriDetalleImpuestoRentaList;
    @OneToMany(mappedBy = "ideSrimr")
    private List<SriDeducibles> sriDeduciblesList;

    public SriImpuestoRenta() {
    }

    public SriImpuestoRenta(Integer ideSrimr) {
        this.ideSrimr = ideSrimr;
    }

    public Integer getIdeSrimr() {
        return ideSrimr;
    }

    public void setIdeSrimr(Integer ideSrimr) {
        this.ideSrimr = ideSrimr;
    }

    public String getDetalleSrimr() {
        return detalleSrimr;
    }

    public void setDetalleSrimr(String detalleSrimr) {
        this.detalleSrimr = detalleSrimr;
    }

    public Date getFechaInicioSrimr() {
        return fechaInicioSrimr;
    }

    public void setFechaInicioSrimr(Date fechaInicioSrimr) {
        this.fechaInicioSrimr = fechaInicioSrimr;
    }

    public Date getFechaFinSrimr() {
        return fechaFinSrimr;
    }

    public void setFechaFinSrimr(Date fechaFinSrimr) {
        this.fechaFinSrimr = fechaFinSrimr;
    }

    public Boolean getActivoSrimr() {
        return activoSrimr;
    }

    public void setActivoSrimr(Boolean activoSrimr) {
        this.activoSrimr = activoSrimr;
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

    public List<SriProyeccionIngres> getSriProyeccionIngresList() {
        return sriProyeccionIngresList;
    }

    public void setSriProyeccionIngresList(List<SriProyeccionIngres> sriProyeccionIngresList) {
        this.sriProyeccionIngresList = sriProyeccionIngresList;
    }

    public List<SriDetalleImpuestoRenta> getSriDetalleImpuestoRentaList() {
        return sriDetalleImpuestoRentaList;
    }

    public void setSriDetalleImpuestoRentaList(List<SriDetalleImpuestoRenta> sriDetalleImpuestoRentaList) {
        this.sriDetalleImpuestoRentaList = sriDetalleImpuestoRentaList;
    }

    public List<SriDeducibles> getSriDeduciblesList() {
        return sriDeduciblesList;
    }

    public void setSriDeduciblesList(List<SriDeducibles> sriDeduciblesList) {
        this.sriDeduciblesList = sriDeduciblesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSrimr != null ? ideSrimr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SriImpuestoRenta)) {
            return false;
        }
        SriImpuestoRenta other = (SriImpuestoRenta) object;
        if ((this.ideSrimr == null && other.ideSrimr != null) || (this.ideSrimr != null && !this.ideSrimr.equals(other.ideSrimr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SriImpuestoRenta[ ideSrimr=" + ideSrimr + " ]";
    }
    
}
