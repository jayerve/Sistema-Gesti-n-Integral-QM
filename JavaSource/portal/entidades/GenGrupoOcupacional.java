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
@Table(name = "gen_grupo_ocupacional", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenGrupoOcupacional.findAll", query = "SELECT g FROM GenGrupoOcupacional g"),
    @NamedQuery(name = "GenGrupoOcupacional.findByIdeGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.ideGegro = :ideGegro"),
    @NamedQuery(name = "GenGrupoOcupacional.findByDetalleGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.detalleGegro = :detalleGegro"),
    @NamedQuery(name = "GenGrupoOcupacional.findBySiglasGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.siglasGegro = :siglasGegro"),
    @NamedQuery(name = "GenGrupoOcupacional.findByActivoGegro", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.activoGegro = :activoGegro"),
    @NamedQuery(name = "GenGrupoOcupacional.findByUsuarioIngre", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenGrupoOcupacional.findByFechaIngre", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenGrupoOcupacional.findByUsuarioActua", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenGrupoOcupacional.findByFechaActua", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenGrupoOcupacional.findByHoraIngre", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenGrupoOcupacional.findByHoraActua", query = "SELECT g FROM GenGrupoOcupacional g WHERE g.horaActua = :horaActua")})
public class GenGrupoOcupacional implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gegro", nullable = false)
    private Integer ideGegro;
    @Size(max = 100)
    @Column(name = "detalle_gegro", length = 100)
    private String detalleGegro;
    @Size(max = 50)
    @Column(name = "siglas_gegro", length = 50)
    private String siglasGegro;
    @Column(name = "activo_gegro")
    private Boolean activoGegro;
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
    @JoinColumn(name = "ide_gtniv", referencedColumnName = "ide_gtniv")
    @ManyToOne
    private GthNivelViatico ideGtniv;

    public GenGrupoOcupacional() {
    }

    public GenGrupoOcupacional(Integer ideGegro) {
        this.ideGegro = ideGegro;
    }

    public Integer getIdeGegro() {
        return ideGegro;
    }

    public void setIdeGegro(Integer ideGegro) {
        this.ideGegro = ideGegro;
    }

    public String getDetalleGegro() {
        return detalleGegro;
    }

    public void setDetalleGegro(String detalleGegro) {
        this.detalleGegro = detalleGegro;
    }

    public String getSiglasGegro() {
        return siglasGegro;
    }

    public void setSiglasGegro(String siglasGegro) {
        this.siglasGegro = siglasGegro;
    }

    public Boolean getActivoGegro() {
        return activoGegro;
    }

    public void setActivoGegro(Boolean activoGegro) {
        this.activoGegro = activoGegro;
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

    public GthNivelViatico getIdeGtniv() {
        return ideGtniv;
    }

    public void setIdeGtniv(GthNivelViatico ideGtniv) {
        this.ideGtniv = ideGtniv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGegro != null ? ideGegro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenGrupoOcupacional)) {
            return false;
        }
        GenGrupoOcupacional other = (GenGrupoOcupacional) object;
        if ((this.ideGegro == null && other.ideGegro != null) || (this.ideGegro != null && !this.ideGegro.equals(other.ideGegro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenGrupoOcupacional[ ideGegro=" + ideGegro + " ]";
    }
    
}
