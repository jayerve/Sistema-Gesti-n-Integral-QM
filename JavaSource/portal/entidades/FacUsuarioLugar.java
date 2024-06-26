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
@Table(name = "fac_usuario_lugar", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "FacUsuarioLugar.findAll", query = "SELECT f FROM FacUsuarioLugar f"),
    @NamedQuery(name = "FacUsuarioLugar.findByIdeFausl", query = "SELECT f FROM FacUsuarioLugar f WHERE f.ideFausl = :ideFausl"),
    @NamedQuery(name = "FacUsuarioLugar.findByActivoFausl", query = "SELECT f FROM FacUsuarioLugar f WHERE f.activoFausl = :activoFausl"),
    @NamedQuery(name = "FacUsuarioLugar.findByUsuarioIngre", query = "SELECT f FROM FacUsuarioLugar f WHERE f.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "FacUsuarioLugar.findByFechaIngre", query = "SELECT f FROM FacUsuarioLugar f WHERE f.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "FacUsuarioLugar.findByHoraIngre", query = "SELECT f FROM FacUsuarioLugar f WHERE f.horaIngre = :horaIngre"),
    @NamedQuery(name = "FacUsuarioLugar.findByUsuarioActua", query = "SELECT f FROM FacUsuarioLugar f WHERE f.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "FacUsuarioLugar.findByFechaActua", query = "SELECT f FROM FacUsuarioLugar f WHERE f.fechaActua = :fechaActua"),
    @NamedQuery(name = "FacUsuarioLugar.findByHoraActua", query = "SELECT f FROM FacUsuarioLugar f WHERE f.horaActua = :horaActua")})
public class FacUsuarioLugar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_fausl", nullable = false)
    private Long ideFausl;
    @Column(name = "activo_fausl")
    private Boolean activoFausl;
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
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;
    @JoinColumn(name = "ide_falug", referencedColumnName = "ide_falug")
    @ManyToOne
    private FacLugar ideFalug;

    public FacUsuarioLugar() {
    }

    public FacUsuarioLugar(Long ideFausl) {
        this.ideFausl = ideFausl;
    }

    public Long getIdeFausl() {
        return ideFausl;
    }

    public void setIdeFausl(Long ideFausl) {
        this.ideFausl = ideFausl;
    }

    public Boolean getActivoFausl() {
        return activoFausl;
    }

    public void setActivoFausl(Boolean activoFausl) {
        this.activoFausl = activoFausl;
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

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    public FacLugar getIdeFalug() {
        return ideFalug;
    }

    public void setIdeFalug(FacLugar ideFalug) {
        this.ideFalug = ideFalug;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFausl != null ? ideFausl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacUsuarioLugar)) {
            return false;
        }
        FacUsuarioLugar other = (FacUsuarioLugar) object;
        if ((this.ideFausl == null && other.ideFausl != null) || (this.ideFausl != null && !this.ideFausl.equals(other.ideFausl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.FacUsuarioLugar[ ideFausl=" + ideFausl + " ]";
    }
    
}
