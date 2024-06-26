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
@Table(name = "crt_detalle_actividad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrtDetalleActividad.findAll", query = "SELECT c FROM CrtDetalleActividad c"),
    @NamedQuery(name = "CrtDetalleActividad.findByIdeCrd", query = "SELECT c FROM CrtDetalleActividad c WHERE c.ideCrd = :ideCrd"),
    @NamedQuery(name = "CrtDetalleActividad.findByDetalleCrd", query = "SELECT c FROM CrtDetalleActividad c WHERE c.detalleCrd = :detalleCrd"),
    @NamedQuery(name = "CrtDetalleActividad.findByUsuarioIngre", query = "SELECT c FROM CrtDetalleActividad c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrtDetalleActividad.findByFechaIngre", query = "SELECT c FROM CrtDetalleActividad c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrtDetalleActividad.findByHoraIngre", query = "SELECT c FROM CrtDetalleActividad c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrtDetalleActividad.findByUsuarioActua", query = "SELECT c FROM CrtDetalleActividad c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrtDetalleActividad.findByFechaActua", query = "SELECT c FROM CrtDetalleActividad c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrtDetalleActividad.findByHoraActua", query = "SELECT c FROM CrtDetalleActividad c WHERE c.horaActua = :horaActua"),
    @NamedQuery(name = "CrtDetalleActividad.findByFechaInicio", query = "SELECT c FROM CrtDetalleActividad c WHERE c.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "CrtDetalleActividad.findByFechaFinal", query = "SELECT c FROM CrtDetalleActividad c WHERE c.fechaFinal = :fechaFinal"),
    @NamedQuery(name = "CrtDetalleActividad.findByFechaInicioCrd", query = "SELECT c FROM CrtDetalleActividad c WHERE c.fechaInicioCrd = :fechaInicioCrd"),
    @NamedQuery(name = "CrtDetalleActividad.findByFechaFinCrd", query = "SELECT c FROM CrtDetalleActividad c WHERE c.fechaFinCrd = :fechaFinCrd")})
public class CrtDetalleActividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_crd", nullable = false)
    private Integer ideCrd;
    @Size(max = 2147483647)
    @Column(name = "detalle_crd", length = 2147483647)
    private String detalleCrd;
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
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.DATE)
    private Date fechaFinal;
    @Column(name = "fecha_inicio_crd")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioCrd;
    @Column(name = "fecha_fin_crd")
    @Temporal(TemporalType.DATE)
    private Date fechaFinCrd;
    @JoinColumn(name = "ide_cra", referencedColumnName = "ide_cra")
    @ManyToOne
    private CrtActividad ideCra;

    public CrtDetalleActividad() {
    }

    public CrtDetalleActividad(Integer ideCrd) {
        this.ideCrd = ideCrd;
    }

    public Integer getIdeCrd() {
        return ideCrd;
    }

    public void setIdeCrd(Integer ideCrd) {
        this.ideCrd = ideCrd;
    }

    public String getDetalleCrd() {
        return detalleCrd;
    }

    public void setDetalleCrd(String detalleCrd) {
        this.detalleCrd = detalleCrd;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Date getFechaInicioCrd() {
        return fechaInicioCrd;
    }

    public void setFechaInicioCrd(Date fechaInicioCrd) {
        this.fechaInicioCrd = fechaInicioCrd;
    }

    public Date getFechaFinCrd() {
        return fechaFinCrd;
    }

    public void setFechaFinCrd(Date fechaFinCrd) {
        this.fechaFinCrd = fechaFinCrd;
    }

    public CrtActividad getIdeCra() {
        return ideCra;
    }

    public void setIdeCra(CrtActividad ideCra) {
        this.ideCra = ideCra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCrd != null ? ideCrd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrtDetalleActividad)) {
            return false;
        }
        CrtDetalleActividad other = (CrtDetalleActividad) object;
        if ((this.ideCrd == null && other.ideCrd != null) || (this.ideCrd != null && !this.ideCrd.equals(other.ideCrd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrtDetalleActividad[ ideCrd=" + ideCrd + " ]";
    }
    
}
