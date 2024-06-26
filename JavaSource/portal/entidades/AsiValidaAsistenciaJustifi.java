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
@Table(name = "asi_valida_asistencia_justifi", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findAll", query = "SELECT a FROM AsiValidaAsistenciaJustifi a"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByIdeAsvaj", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.ideAsvaj = :ideAsvaj"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByDetalleAspej", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.detalleAspej = :detalleAspej"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByFechaAsvaj", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.fechaAsvaj = :fechaAsvaj"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByArchivoAsvaj", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.archivoAsvaj = :archivoAsvaj"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByActivoAsvaj", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.activoAsvaj = :activoAsvaj"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByUsuarioIngre", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByFechaIngre", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByUsuarioActua", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByFechaActua", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByHoraIngre", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiValidaAsistenciaJustifi.findByHoraActua", query = "SELECT a FROM AsiValidaAsistenciaJustifi a WHERE a.horaActua = :horaActua")})
public class AsiValidaAsistenciaJustifi implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asvaj", nullable = false)
    private Integer ideAsvaj;
    @Size(max = 4000)
    @Column(name = "detalle_aspej", length = 4000)
    private String detalleAspej;
    @Column(name = "fecha_asvaj")
    @Temporal(TemporalType.DATE)
    private Date fechaAsvaj;
    @Size(max = 100)
    @Column(name = "archivo_asvaj", length = 100)
    private String archivoAsvaj;
    @Column(name = "activo_asvaj")
    private Boolean activoAsvaj;
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
    @JoinColumn(name = "ide_asvaa", referencedColumnName = "ide_asvaa")
    @ManyToOne
    private AsiValidaAsistencia ideAsvaa;

    public AsiValidaAsistenciaJustifi() {
    }

    public AsiValidaAsistenciaJustifi(Integer ideAsvaj) {
        this.ideAsvaj = ideAsvaj;
    }

    public Integer getIdeAsvaj() {
        return ideAsvaj;
    }

    public void setIdeAsvaj(Integer ideAsvaj) {
        this.ideAsvaj = ideAsvaj;
    }

    public String getDetalleAspej() {
        return detalleAspej;
    }

    public void setDetalleAspej(String detalleAspej) {
        this.detalleAspej = detalleAspej;
    }

    public Date getFechaAsvaj() {
        return fechaAsvaj;
    }

    public void setFechaAsvaj(Date fechaAsvaj) {
        this.fechaAsvaj = fechaAsvaj;
    }

    public String getArchivoAsvaj() {
        return archivoAsvaj;
    }

    public void setArchivoAsvaj(String archivoAsvaj) {
        this.archivoAsvaj = archivoAsvaj;
    }

    public Boolean getActivoAsvaj() {
        return activoAsvaj;
    }

    public void setActivoAsvaj(Boolean activoAsvaj) {
        this.activoAsvaj = activoAsvaj;
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

    public AsiValidaAsistencia getIdeAsvaa() {
        return ideAsvaa;
    }

    public void setIdeAsvaa(AsiValidaAsistencia ideAsvaa) {
        this.ideAsvaa = ideAsvaa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsvaj != null ? ideAsvaj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiValidaAsistenciaJustifi)) {
            return false;
        }
        AsiValidaAsistenciaJustifi other = (AsiValidaAsistenciaJustifi) object;
        if ((this.ideAsvaj == null && other.ideAsvaj != null) || (this.ideAsvaj != null && !this.ideAsvaj.equals(other.ideAsvaj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiValidaAsistenciaJustifi[ ideAsvaj=" + ideAsvaj + " ]";
    }
    
}
