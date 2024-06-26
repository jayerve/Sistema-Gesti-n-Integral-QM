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
@Table(name = "sbs_archivo_veinte_tres", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsArchivoVeinteTres.findAll", query = "SELECT s FROM SbsArchivoVeinteTres s"),
    @NamedQuery(name = "SbsArchivoVeinteTres.findByIdeSbavt", query = "SELECT s FROM SbsArchivoVeinteTres s WHERE s.ideSbavt = :ideSbavt"),
    @NamedQuery(name = "SbsArchivoVeinteTres.findByActivoSbavt", query = "SELECT s FROM SbsArchivoVeinteTres s WHERE s.activoSbavt = :activoSbavt"),
    @NamedQuery(name = "SbsArchivoVeinteTres.findByUsuarioIngre", query = "SELECT s FROM SbsArchivoVeinteTres s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsArchivoVeinteTres.findByFechaIngre", query = "SELECT s FROM SbsArchivoVeinteTres s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsArchivoVeinteTres.findByHoraIngre", query = "SELECT s FROM SbsArchivoVeinteTres s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsArchivoVeinteTres.findByUsuarioActua", query = "SELECT s FROM SbsArchivoVeinteTres s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsArchivoVeinteTres.findByFechaActua", query = "SELECT s FROM SbsArchivoVeinteTres s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsArchivoVeinteTres.findByHoraActua", query = "SELECT s FROM SbsArchivoVeinteTres s WHERE s.horaActua = :horaActua")})
public class SbsArchivoVeinteTres implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbavt", nullable = false)
    private Integer ideSbavt;
    @Column(name = "activo_sbavt")
    private Boolean activoSbavt;
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
    @JoinColumn(name = "ide_sbpec", referencedColumnName = "ide_sbpec")
    @ManyToOne
    private SbsPeriodoCatastro ideSbpec;
    @JoinColumn(name = "ide_gttpr", referencedColumnName = "ide_gttpr")
    @ManyToOne
    private GthTipoParentescoRelacion ideGttpr;
    @JoinColumn(name = "ide_gtfam", referencedColumnName = "ide_gtfam")
    @ManyToOne
    private GthFamiliar ideGtfam;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_gtcon", referencedColumnName = "ide_gtcon")
    @ManyToOne
    private GthConyuge ideGtcon;
    @JoinColumn(name = "ide_gtcaf", referencedColumnName = "ide_gtcaf")
    @ManyToOne
    private GthCargasFamiliares ideGtcaf;

    public SbsArchivoVeinteTres() {
    }

    public SbsArchivoVeinteTres(Integer ideSbavt) {
        this.ideSbavt = ideSbavt;
    }

    public Integer getIdeSbavt() {
        return ideSbavt;
    }

    public void setIdeSbavt(Integer ideSbavt) {
        this.ideSbavt = ideSbavt;
    }

    public Boolean getActivoSbavt() {
        return activoSbavt;
    }

    public void setActivoSbavt(Boolean activoSbavt) {
        this.activoSbavt = activoSbavt;
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

    public SbsPeriodoCatastro getIdeSbpec() {
        return ideSbpec;
    }

    public void setIdeSbpec(SbsPeriodoCatastro ideSbpec) {
        this.ideSbpec = ideSbpec;
    }

    public GthTipoParentescoRelacion getIdeGttpr() {
        return ideGttpr;
    }

    public void setIdeGttpr(GthTipoParentescoRelacion ideGttpr) {
        this.ideGttpr = ideGttpr;
    }

    public GthFamiliar getIdeGtfam() {
        return ideGtfam;
    }

    public void setIdeGtfam(GthFamiliar ideGtfam) {
        this.ideGtfam = ideGtfam;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GthConyuge getIdeGtcon() {
        return ideGtcon;
    }

    public void setIdeGtcon(GthConyuge ideGtcon) {
        this.ideGtcon = ideGtcon;
    }

    public GthCargasFamiliares getIdeGtcaf() {
        return ideGtcaf;
    }

    public void setIdeGtcaf(GthCargasFamiliares ideGtcaf) {
        this.ideGtcaf = ideGtcaf;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbavt != null ? ideSbavt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsArchivoVeinteTres)) {
            return false;
        }
        SbsArchivoVeinteTres other = (SbsArchivoVeinteTres) object;
        if ((this.ideSbavt == null && other.ideSbavt != null) || (this.ideSbavt != null && !this.ideSbavt.equals(other.ideSbavt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsArchivoVeinteTres[ ideSbavt=" + ideSbavt + " ]";
    }
    
}
