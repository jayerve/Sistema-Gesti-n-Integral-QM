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
@Table(name = "gth_nivel_viatico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthNivelViatico.findAll", query = "SELECT g FROM GthNivelViatico g"),
    @NamedQuery(name = "GthNivelViatico.findByIdeGtniv", query = "SELECT g FROM GthNivelViatico g WHERE g.ideGtniv = :ideGtniv"),
    @NamedQuery(name = "GthNivelViatico.findByDetalleGtniv", query = "SELECT g FROM GthNivelViatico g WHERE g.detalleGtniv = :detalleGtniv"),
    @NamedQuery(name = "GthNivelViatico.findByObservacionGtniv", query = "SELECT g FROM GthNivelViatico g WHERE g.observacionGtniv = :observacionGtniv"),
    @NamedQuery(name = "GthNivelViatico.findByActivoGtniv", query = "SELECT g FROM GthNivelViatico g WHERE g.activoGtniv = :activoGtniv"),
    @NamedQuery(name = "GthNivelViatico.findByUsuarioIngre", query = "SELECT g FROM GthNivelViatico g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthNivelViatico.findByFechaIngre", query = "SELECT g FROM GthNivelViatico g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthNivelViatico.findByUsuarioActua", query = "SELECT g FROM GthNivelViatico g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthNivelViatico.findByFechaActua", query = "SELECT g FROM GthNivelViatico g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthNivelViatico.findByHoraIngre", query = "SELECT g FROM GthNivelViatico g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthNivelViatico.findByHoraActua", query = "SELECT g FROM GthNivelViatico g WHERE g.horaActua = :horaActua")})
public class GthNivelViatico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtniv", nullable = false)
    private Integer ideGtniv;
    @Size(max = 50)
    @Column(name = "detalle_gtniv", length = 50)
    private String detalleGtniv;
    @Size(max = 1000)
    @Column(name = "observacion_gtniv", length = 1000)
    private String observacionGtniv;
    @Column(name = "activo_gtniv")
    private Boolean activoGtniv;
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
    @OneToMany(mappedBy = "ideGtniv")
    private List<GenGrupoOcupacional> genGrupoOcupacionalList;
    @OneToMany(mappedBy = "ideGtniv")
    private List<GthNivelZonaViatico> gthNivelZonaViaticoList;

    public GthNivelViatico() {
    }

    public GthNivelViatico(Integer ideGtniv) {
        this.ideGtniv = ideGtniv;
    }

    public Integer getIdeGtniv() {
        return ideGtniv;
    }

    public void setIdeGtniv(Integer ideGtniv) {
        this.ideGtniv = ideGtniv;
    }

    public String getDetalleGtniv() {
        return detalleGtniv;
    }

    public void setDetalleGtniv(String detalleGtniv) {
        this.detalleGtniv = detalleGtniv;
    }

    public String getObservacionGtniv() {
        return observacionGtniv;
    }

    public void setObservacionGtniv(String observacionGtniv) {
        this.observacionGtniv = observacionGtniv;
    }

    public Boolean getActivoGtniv() {
        return activoGtniv;
    }

    public void setActivoGtniv(Boolean activoGtniv) {
        this.activoGtniv = activoGtniv;
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

    public List<GenGrupoOcupacional> getGenGrupoOcupacionalList() {
        return genGrupoOcupacionalList;
    }

    public void setGenGrupoOcupacionalList(List<GenGrupoOcupacional> genGrupoOcupacionalList) {
        this.genGrupoOcupacionalList = genGrupoOcupacionalList;
    }

    public List<GthNivelZonaViatico> getGthNivelZonaViaticoList() {
        return gthNivelZonaViaticoList;
    }

    public void setGthNivelZonaViaticoList(List<GthNivelZonaViatico> gthNivelZonaViaticoList) {
        this.gthNivelZonaViaticoList = gthNivelZonaViaticoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtniv != null ? ideGtniv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthNivelViatico)) {
            return false;
        }
        GthNivelViatico other = (GthNivelViatico) object;
        if ((this.ideGtniv == null && other.ideGtniv != null) || (this.ideGtniv != null && !this.ideGtniv.equals(other.ideGtniv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthNivelViatico[ ideGtniv=" + ideGtniv + " ]";
    }
    
}
