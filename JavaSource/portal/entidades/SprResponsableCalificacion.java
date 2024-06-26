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
@Table(name = "spr_responsable_calificacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprResponsableCalificacion.findAll", query = "SELECT s FROM SprResponsableCalificacion s"),
    @NamedQuery(name = "SprResponsableCalificacion.findByIdeSprec", query = "SELECT s FROM SprResponsableCalificacion s WHERE s.ideSprec = :ideSprec"),
    @NamedQuery(name = "SprResponsableCalificacion.findByActivoSprec", query = "SELECT s FROM SprResponsableCalificacion s WHERE s.activoSprec = :activoSprec"),
    @NamedQuery(name = "SprResponsableCalificacion.findByUsuarioIngre", query = "SELECT s FROM SprResponsableCalificacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprResponsableCalificacion.findByFechaIngre", query = "SELECT s FROM SprResponsableCalificacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprResponsableCalificacion.findByHoraIngre", query = "SELECT s FROM SprResponsableCalificacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprResponsableCalificacion.findByUsuarioActua", query = "SELECT s FROM SprResponsableCalificacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprResponsableCalificacion.findByFechaActua", query = "SELECT s FROM SprResponsableCalificacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprResponsableCalificacion.findByHoraActua", query = "SELECT s FROM SprResponsableCalificacion s WHERE s.horaActua = :horaActua")})
public class SprResponsableCalificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sprec", nullable = false)
    private Integer ideSprec;
    @Column(name = "activo_sprec")
    private Boolean activoSprec;
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
    @JoinColumn(name = "ide_sptir", referencedColumnName = "ide_sptir")
    @ManyToOne
    private SprTipoResponsable ideSptir;
    @JoinColumn(name = "ide_spsop", referencedColumnName = "ide_spsop")
    @ManyToOne
    private SprSolicitudPuesto ideSpsop;
    @JoinColumn(name = "ide_spfac", referencedColumnName = "ide_spfac")
    @ManyToOne
    private SprFactor ideSpfac;
    @JoinColumn(name = "ide_spdsp", referencedColumnName = "ide_spdsp")
    @ManyToOne
    private SprDetalleSolicitudPuesto ideSpdsp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @OneToMany(mappedBy = "ideSprec")
    private List<SprCalificacion> sprCalificacionList;

    public SprResponsableCalificacion() {
    }

    public SprResponsableCalificacion(Integer ideSprec) {
        this.ideSprec = ideSprec;
    }

    public Integer getIdeSprec() {
        return ideSprec;
    }

    public void setIdeSprec(Integer ideSprec) {
        this.ideSprec = ideSprec;
    }

    public Boolean getActivoSprec() {
        return activoSprec;
    }

    public void setActivoSprec(Boolean activoSprec) {
        this.activoSprec = activoSprec;
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

    public SprTipoResponsable getIdeSptir() {
        return ideSptir;
    }

    public void setIdeSptir(SprTipoResponsable ideSptir) {
        this.ideSptir = ideSptir;
    }

    public SprSolicitudPuesto getIdeSpsop() {
        return ideSpsop;
    }

    public void setIdeSpsop(SprSolicitudPuesto ideSpsop) {
        this.ideSpsop = ideSpsop;
    }

    public SprFactor getIdeSpfac() {
        return ideSpfac;
    }

    public void setIdeSpfac(SprFactor ideSpfac) {
        this.ideSpfac = ideSpfac;
    }

    public SprDetalleSolicitudPuesto getIdeSpdsp() {
        return ideSpdsp;
    }

    public void setIdeSpdsp(SprDetalleSolicitudPuesto ideSpdsp) {
        this.ideSpdsp = ideSpdsp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public List<SprCalificacion> getSprCalificacionList() {
        return sprCalificacionList;
    }

    public void setSprCalificacionList(List<SprCalificacion> sprCalificacionList) {
        this.sprCalificacionList = sprCalificacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSprec != null ? ideSprec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprResponsableCalificacion)) {
            return false;
        }
        SprResponsableCalificacion other = (SprResponsableCalificacion) object;
        if ((this.ideSprec == null && other.ideSprec != null) || (this.ideSprec != null && !this.ideSprec.equals(other.ideSprec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprResponsableCalificacion[ ideSprec=" + ideSprec + " ]";
    }
    
}
