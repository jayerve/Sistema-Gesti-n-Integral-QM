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
@Table(name = "asi_feriado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiFeriado.findAll", query = "SELECT a FROM AsiFeriado a"),
    @NamedQuery(name = "AsiFeriado.findByIdeAsfer", query = "SELECT a FROM AsiFeriado a WHERE a.ideAsfer = :ideAsfer"),
    @NamedQuery(name = "AsiFeriado.findByIdeSucu", query = "SELECT a FROM AsiFeriado a WHERE a.ideSucu = :ideSucu"),
    @NamedQuery(name = "AsiFeriado.findByActivoAsfer", query = "SELECT a FROM AsiFeriado a WHERE a.activoAsfer = :activoAsfer"),
    @NamedQuery(name = "AsiFeriado.findByUsuarioIngre", query = "SELECT a FROM AsiFeriado a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiFeriado.findByFechaIngre", query = "SELECT a FROM AsiFeriado a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiFeriado.findByUsuarioActua", query = "SELECT a FROM AsiFeriado a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiFeriado.findByFechaActua", query = "SELECT a FROM AsiFeriado a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiFeriado.findByHoraIngre", query = "SELECT a FROM AsiFeriado a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiFeriado.findByHoraActua", query = "SELECT a FROM AsiFeriado a WHERE a.horaActua = :horaActua")})
public class AsiFeriado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asfer", nullable = false)
    private Integer ideAsfer;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "activo_asfer")
    private Boolean activoAsfer;
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
    @JoinColumn(name = "ide_asgri", referencedColumnName = "ide_asgri")
    @ManyToOne
    private AsiGrupoIntervalo ideAsgri;

    public AsiFeriado() {
    }

    public AsiFeriado(Integer ideAsfer) {
        this.ideAsfer = ideAsfer;
    }

    public Integer getIdeAsfer() {
        return ideAsfer;
    }

    public void setIdeAsfer(Integer ideAsfer) {
        this.ideAsfer = ideAsfer;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Boolean getActivoAsfer() {
        return activoAsfer;
    }

    public void setActivoAsfer(Boolean activoAsfer) {
        this.activoAsfer = activoAsfer;
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

    public AsiGrupoIntervalo getIdeAsgri() {
        return ideAsgri;
    }

    public void setIdeAsgri(AsiGrupoIntervalo ideAsgri) {
        this.ideAsgri = ideAsgri;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsfer != null ? ideAsfer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiFeriado)) {
            return false;
        }
        AsiFeriado other = (AsiFeriado) object;
        if ((this.ideAsfer == null && other.ideAsfer != null) || (this.ideAsfer != null && !this.ideAsfer.equals(other.ideAsfer))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiFeriado[ ideAsfer=" + ideAsfer + " ]";
    }
    
}
