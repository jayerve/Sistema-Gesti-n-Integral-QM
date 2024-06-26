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
@Table(name = "spr_archivo_postulante", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprArchivoPostulante.findAll", query = "SELECT s FROM SprArchivoPostulante s"),
    @NamedQuery(name = "SprArchivoPostulante.findByIdeSparp", query = "SELECT s FROM SprArchivoPostulante s WHERE s.ideSparp = :ideSparp"),
    @NamedQuery(name = "SprArchivoPostulante.findByArchivoSparp", query = "SELECT s FROM SprArchivoPostulante s WHERE s.archivoSparp = :archivoSparp"),
    @NamedQuery(name = "SprArchivoPostulante.findByDetalleSparp", query = "SELECT s FROM SprArchivoPostulante s WHERE s.detalleSparp = :detalleSparp"),
    @NamedQuery(name = "SprArchivoPostulante.findByFechaArchivoSparp", query = "SELECT s FROM SprArchivoPostulante s WHERE s.fechaArchivoSparp = :fechaArchivoSparp"),
    @NamedQuery(name = "SprArchivoPostulante.findByActivoSparp", query = "SELECT s FROM SprArchivoPostulante s WHERE s.activoSparp = :activoSparp"),
    @NamedQuery(name = "SprArchivoPostulante.findByUsuarioIngre", query = "SELECT s FROM SprArchivoPostulante s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprArchivoPostulante.findByFechaIngre", query = "SELECT s FROM SprArchivoPostulante s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprArchivoPostulante.findByHoraIngre", query = "SELECT s FROM SprArchivoPostulante s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprArchivoPostulante.findByUsuarioActua", query = "SELECT s FROM SprArchivoPostulante s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprArchivoPostulante.findByFechaActua", query = "SELECT s FROM SprArchivoPostulante s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprArchivoPostulante.findByHoraActua", query = "SELECT s FROM SprArchivoPostulante s WHERE s.horaActua = :horaActua")})
public class SprArchivoPostulante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sparp", nullable = false)
    private Integer ideSparp;
    @Size(max = 100)
    @Column(name = "archivo_sparp", length = 100)
    private String archivoSparp;
    @Size(max = 4000)
    @Column(name = "detalle_sparp", length = 4000)
    private String detalleSparp;
    @Column(name = "fecha_archivo_sparp")
    @Temporal(TemporalType.DATE)
    private Date fechaArchivoSparp;
    @Column(name = "activo_sparp")
    private Boolean activoSparp;
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
    @JoinColumn(name = "ide_spbap", referencedColumnName = "ide_spbap")
    @ManyToOne
    private SprBasePostulante ideSpbap;

    public SprArchivoPostulante() {
    }

    public SprArchivoPostulante(Integer ideSparp) {
        this.ideSparp = ideSparp;
    }

    public Integer getIdeSparp() {
        return ideSparp;
    }

    public void setIdeSparp(Integer ideSparp) {
        this.ideSparp = ideSparp;
    }

    public String getArchivoSparp() {
        return archivoSparp;
    }

    public void setArchivoSparp(String archivoSparp) {
        this.archivoSparp = archivoSparp;
    }

    public String getDetalleSparp() {
        return detalleSparp;
    }

    public void setDetalleSparp(String detalleSparp) {
        this.detalleSparp = detalleSparp;
    }

    public Date getFechaArchivoSparp() {
        return fechaArchivoSparp;
    }

    public void setFechaArchivoSparp(Date fechaArchivoSparp) {
        this.fechaArchivoSparp = fechaArchivoSparp;
    }

    public Boolean getActivoSparp() {
        return activoSparp;
    }

    public void setActivoSparp(Boolean activoSparp) {
        this.activoSparp = activoSparp;
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

    public SprBasePostulante getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(SprBasePostulante ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSparp != null ? ideSparp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprArchivoPostulante)) {
            return false;
        }
        SprArchivoPostulante other = (SprArchivoPostulante) object;
        if ((this.ideSparp == null && other.ideSparp != null) || (this.ideSparp != null && !this.ideSparp.equals(other.ideSparp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprArchivoPostulante[ ideSparp=" + ideSparp + " ]";
    }
    
}
