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
@Table(name = "tes_archivo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesArchivo.findAll", query = "SELECT t FROM TesArchivo t"),
    @NamedQuery(name = "TesArchivo.findByIdeTearc", query = "SELECT t FROM TesArchivo t WHERE t.ideTearc = :ideTearc"),
    @NamedQuery(name = "TesArchivo.findByFechaTearc", query = "SELECT t FROM TesArchivo t WHERE t.fechaTearc = :fechaTearc"),
    @NamedQuery(name = "TesArchivo.findByObservacionesTearc", query = "SELECT t FROM TesArchivo t WHERE t.observacionesTearc = :observacionesTearc"),
    @NamedQuery(name = "TesArchivo.findByFotoTearc", query = "SELECT t FROM TesArchivo t WHERE t.fotoTearc = :fotoTearc"),
    @NamedQuery(name = "TesArchivo.findByActivoTearc", query = "SELECT t FROM TesArchivo t WHERE t.activoTearc = :activoTearc"),
    @NamedQuery(name = "TesArchivo.findByUsuarioIngre", query = "SELECT t FROM TesArchivo t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesArchivo.findByFechaIngre", query = "SELECT t FROM TesArchivo t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesArchivo.findByHoraIngre", query = "SELECT t FROM TesArchivo t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesArchivo.findByUsuarioActua", query = "SELECT t FROM TesArchivo t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesArchivo.findByFechaActua", query = "SELECT t FROM TesArchivo t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesArchivo.findByHoraActua", query = "SELECT t FROM TesArchivo t WHERE t.horaActua = :horaActua")})
public class TesArchivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tearc", nullable = false)
    private Long ideTearc;
    @Column(name = "fecha_tearc")
    @Temporal(TemporalType.DATE)
    private Date fechaTearc;
    @Size(max = 2147483647)
    @Column(name = "observaciones_tearc", length = 2147483647)
    private String observacionesTearc;
    @Size(max = 250)
    @Column(name = "foto_tearc", length = 250)
    private String fotoTearc;
    @Column(name = "activo_tearc")
    private Boolean activoTearc;
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
    @JoinColumn(name = "ide_tepol", referencedColumnName = "ide_tepol")
    @ManyToOne
    private TesPoliza ideTepol;

    public TesArchivo() {
    }

    public TesArchivo(Long ideTearc) {
        this.ideTearc = ideTearc;
    }

    public Long getIdeTearc() {
        return ideTearc;
    }

    public void setIdeTearc(Long ideTearc) {
        this.ideTearc = ideTearc;
    }

    public Date getFechaTearc() {
        return fechaTearc;
    }

    public void setFechaTearc(Date fechaTearc) {
        this.fechaTearc = fechaTearc;
    }

    public String getObservacionesTearc() {
        return observacionesTearc;
    }

    public void setObservacionesTearc(String observacionesTearc) {
        this.observacionesTearc = observacionesTearc;
    }

    public String getFotoTearc() {
        return fotoTearc;
    }

    public void setFotoTearc(String fotoTearc) {
        this.fotoTearc = fotoTearc;
    }

    public Boolean getActivoTearc() {
        return activoTearc;
    }

    public void setActivoTearc(Boolean activoTearc) {
        this.activoTearc = activoTearc;
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

    public TesPoliza getIdeTepol() {
        return ideTepol;
    }

    public void setIdeTepol(TesPoliza ideTepol) {
        this.ideTepol = ideTepol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTearc != null ? ideTearc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesArchivo)) {
            return false;
        }
        TesArchivo other = (TesArchivo) object;
        if ((this.ideTearc == null && other.ideTearc != null) || (this.ideTearc != null && !this.ideTearc.equals(other.ideTearc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesArchivo[ ideTearc=" + ideTearc + " ]";
    }
    
}
