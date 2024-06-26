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
@Table(name = "fac_venta_lugar", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "FacVentaLugar.findAll", query = "SELECT f FROM FacVentaLugar f"),
    @NamedQuery(name = "FacVentaLugar.findByIdeFavel", query = "SELECT f FROM FacVentaLugar f WHERE f.ideFavel = :ideFavel"),
    @NamedQuery(name = "FacVentaLugar.findByActivoFavel", query = "SELECT f FROM FacVentaLugar f WHERE f.activoFavel = :activoFavel"),
    @NamedQuery(name = "FacVentaLugar.findByUsuarioIngre", query = "SELECT f FROM FacVentaLugar f WHERE f.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "FacVentaLugar.findByFechaIngre", query = "SELECT f FROM FacVentaLugar f WHERE f.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "FacVentaLugar.findByHoraIngre", query = "SELECT f FROM FacVentaLugar f WHERE f.horaIngre = :horaIngre"),
    @NamedQuery(name = "FacVentaLugar.findByUsuarioActua", query = "SELECT f FROM FacVentaLugar f WHERE f.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "FacVentaLugar.findByFechaActua", query = "SELECT f FROM FacVentaLugar f WHERE f.fechaActua = :fechaActua"),
    @NamedQuery(name = "FacVentaLugar.findByHoraActua", query = "SELECT f FROM FacVentaLugar f WHERE f.horaActua = :horaActua")})
public class FacVentaLugar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_favel", nullable = false)
    private Long ideFavel;
    @Column(name = "activo_favel")
    private Boolean activoFavel;
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
    @JoinColumn(name = "ide_falug", referencedColumnName = "ide_falug")
    @ManyToOne
    private FacLugar ideFalug;
    @JoinColumn(name = "ide_bogrm", referencedColumnName = "ide_bogrm")
    @ManyToOne
    private BodtGrupoMaterial ideBogrm;

    public FacVentaLugar() {
    }

    public FacVentaLugar(Long ideFavel) {
        this.ideFavel = ideFavel;
    }

    public Long getIdeFavel() {
        return ideFavel;
    }

    public void setIdeFavel(Long ideFavel) {
        this.ideFavel = ideFavel;
    }

    public Boolean getActivoFavel() {
        return activoFavel;
    }

    public void setActivoFavel(Boolean activoFavel) {
        this.activoFavel = activoFavel;
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

    public FacLugar getIdeFalug() {
        return ideFalug;
    }

    public void setIdeFalug(FacLugar ideFalug) {
        this.ideFalug = ideFalug;
    }

    public BodtGrupoMaterial getIdeBogrm() {
        return ideBogrm;
    }

    public void setIdeBogrm(BodtGrupoMaterial ideBogrm) {
        this.ideBogrm = ideBogrm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFavel != null ? ideFavel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacVentaLugar)) {
            return false;
        }
        FacVentaLugar other = (FacVentaLugar) object;
        if ((this.ideFavel == null && other.ideFavel != null) || (this.ideFavel != null && !this.ideFavel.equals(other.ideFavel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.FacVentaLugar[ ideFavel=" + ideFavel + " ]";
    }
    
}
