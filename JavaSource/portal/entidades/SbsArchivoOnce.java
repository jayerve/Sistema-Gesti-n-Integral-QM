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
@Table(name = "sbs_archivo_once", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsArchivoOnce.findAll", query = "SELECT s FROM SbsArchivoOnce s"),
    @NamedQuery(name = "SbsArchivoOnce.findByIdeSbaro", query = "SELECT s FROM SbsArchivoOnce s WHERE s.ideSbaro = :ideSbaro"),
    @NamedQuery(name = "SbsArchivoOnce.findByTituloProfesionalSbaro", query = "SELECT s FROM SbsArchivoOnce s WHERE s.tituloProfesionalSbaro = :tituloProfesionalSbaro"),
    @NamedQuery(name = "SbsArchivoOnce.findByActivoSbaro", query = "SELECT s FROM SbsArchivoOnce s WHERE s.activoSbaro = :activoSbaro"),
    @NamedQuery(name = "SbsArchivoOnce.findByUsuarioIngre", query = "SELECT s FROM SbsArchivoOnce s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsArchivoOnce.findByFechaIngre", query = "SELECT s FROM SbsArchivoOnce s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsArchivoOnce.findByHoraIngre", query = "SELECT s FROM SbsArchivoOnce s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsArchivoOnce.findByUsuarioActua", query = "SELECT s FROM SbsArchivoOnce s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsArchivoOnce.findByFechaActua", query = "SELECT s FROM SbsArchivoOnce s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsArchivoOnce.findByHoraActua", query = "SELECT s FROM SbsArchivoOnce s WHERE s.horaActua = :horaActua")})
public class SbsArchivoOnce implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbaro", nullable = false)
    private Integer ideSbaro;
    @Size(max = 50)
    @Column(name = "titulo_profesional_sbaro", length = 50)
    private String tituloProfesionalSbaro;
    @Column(name = "activo_sbaro")
    private Boolean activoSbaro;
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
    @JoinColumn(name = "ide_sbtic", referencedColumnName = "ide_sbtic")
    @ManyToOne
    private SbsTipoCargo ideSbtic;
    @JoinColumn(name = "ide_sbrel", referencedColumnName = "ide_sbrel")
    @ManyToOne
    private SbsRelacionLaboral ideSbrel;
    @JoinColumn(name = "ide_sbpec", referencedColumnName = "ide_sbpec")
    @ManyToOne
    private SbsPeriodoCatastro ideSbpec;
    @JoinColumn(name = "ide_sbofi", referencedColumnName = "ide_sbofi")
    @ManyToOne
    private SbsOficina ideSbofi;
    @JoinColumn(name = "ide_gttdi", referencedColumnName = "ide_gttdi")
    @ManyToOne
    private GthTipoDocumentoIdentidad ideGttdi;
    @JoinColumn(name = "ide_gtnac", referencedColumnName = "ide_gtnac")
    @ManyToOne
    private GthNacionalidad ideGtnac;
    @JoinColumn(name = "ide_gtgen", referencedColumnName = "ide_gtgen")
    @ManyToOne
    private GthGenero ideGtgen;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;

    public SbsArchivoOnce() {
    }

    public SbsArchivoOnce(Integer ideSbaro) {
        this.ideSbaro = ideSbaro;
    }

    public Integer getIdeSbaro() {
        return ideSbaro;
    }

    public void setIdeSbaro(Integer ideSbaro) {
        this.ideSbaro = ideSbaro;
    }

    public String getTituloProfesionalSbaro() {
        return tituloProfesionalSbaro;
    }

    public void setTituloProfesionalSbaro(String tituloProfesionalSbaro) {
        this.tituloProfesionalSbaro = tituloProfesionalSbaro;
    }

    public Boolean getActivoSbaro() {
        return activoSbaro;
    }

    public void setActivoSbaro(Boolean activoSbaro) {
        this.activoSbaro = activoSbaro;
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

    public SbsTipoCargo getIdeSbtic() {
        return ideSbtic;
    }

    public void setIdeSbtic(SbsTipoCargo ideSbtic) {
        this.ideSbtic = ideSbtic;
    }

    public SbsRelacionLaboral getIdeSbrel() {
        return ideSbrel;
    }

    public void setIdeSbrel(SbsRelacionLaboral ideSbrel) {
        this.ideSbrel = ideSbrel;
    }

    public SbsPeriodoCatastro getIdeSbpec() {
        return ideSbpec;
    }

    public void setIdeSbpec(SbsPeriodoCatastro ideSbpec) {
        this.ideSbpec = ideSbpec;
    }

    public SbsOficina getIdeSbofi() {
        return ideSbofi;
    }

    public void setIdeSbofi(SbsOficina ideSbofi) {
        this.ideSbofi = ideSbofi;
    }

    public GthTipoDocumentoIdentidad getIdeGttdi() {
        return ideGttdi;
    }

    public void setIdeGttdi(GthTipoDocumentoIdentidad ideGttdi) {
        this.ideGttdi = ideGttdi;
    }

    public GthNacionalidad getIdeGtnac() {
        return ideGtnac;
    }

    public void setIdeGtnac(GthNacionalidad ideGtnac) {
        this.ideGtnac = ideGtnac;
    }

    public GthGenero getIdeGtgen() {
        return ideGtgen;
    }

    public void setIdeGtgen(GthGenero ideGtgen) {
        this.ideGtgen = ideGtgen;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbaro != null ? ideSbaro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsArchivoOnce)) {
            return false;
        }
        SbsArchivoOnce other = (SbsArchivoOnce) object;
        if ((this.ideSbaro == null && other.ideSbaro != null) || (this.ideSbaro != null && !this.ideSbaro.equals(other.ideSbaro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsArchivoOnce[ ideSbaro=" + ideSbaro + " ]";
    }
    
}
