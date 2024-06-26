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
@Table(name = "spr_postulante", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprPostulante.findAll", query = "SELECT s FROM SprPostulante s"),
    @NamedQuery(name = "SprPostulante.findByIdeSppos", query = "SELECT s FROM SprPostulante s WHERE s.ideSppos = :ideSppos"),
    @NamedQuery(name = "SprPostulante.findByActivoSppos", query = "SELECT s FROM SprPostulante s WHERE s.activoSppos = :activoSppos"),
    @NamedQuery(name = "SprPostulante.findByUsuarioIngre", query = "SELECT s FROM SprPostulante s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprPostulante.findByFechaIngre", query = "SELECT s FROM SprPostulante s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprPostulante.findByHoraIngre", query = "SELECT s FROM SprPostulante s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprPostulante.findByUsuarioActua", query = "SELECT s FROM SprPostulante s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprPostulante.findByFechaActua", query = "SELECT s FROM SprPostulante s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprPostulante.findByHoraActua", query = "SELECT s FROM SprPostulante s WHERE s.horaActua = :horaActua")})
public class SprPostulante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sppos", nullable = false)
    private Integer ideSppos;
    @Column(name = "activo_sppos")
    private Boolean activoSppos;
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
    @JoinColumn(name = "ide_spsoa", referencedColumnName = "ide_spsoa")
    @ManyToOne
    private SprSolicitudAprobacion ideSpsoa;
    @JoinColumn(name = "ide_spdsp", referencedColumnName = "ide_spdsp")
    @ManyToOne
    private SprDetalleSolicitudPuesto ideSpdsp;
    @JoinColumn(name = "ide_spbap", referencedColumnName = "ide_spbap")
    @ManyToOne
    private SprBasePostulante ideSpbap;
    @OneToMany(mappedBy = "ideSppos")
    private List<SprResultado> sprResultadoList;
    @OneToMany(mappedBy = "ideSppos")
    private List<SprCalificacion> sprCalificacionList;

    public SprPostulante() {
    }

    public SprPostulante(Integer ideSppos) {
        this.ideSppos = ideSppos;
    }

    public Integer getIdeSppos() {
        return ideSppos;
    }

    public void setIdeSppos(Integer ideSppos) {
        this.ideSppos = ideSppos;
    }

    public Boolean getActivoSppos() {
        return activoSppos;
    }

    public void setActivoSppos(Boolean activoSppos) {
        this.activoSppos = activoSppos;
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

    public SprSolicitudAprobacion getIdeSpsoa() {
        return ideSpsoa;
    }

    public void setIdeSpsoa(SprSolicitudAprobacion ideSpsoa) {
        this.ideSpsoa = ideSpsoa;
    }

    public SprDetalleSolicitudPuesto getIdeSpdsp() {
        return ideSpdsp;
    }

    public void setIdeSpdsp(SprDetalleSolicitudPuesto ideSpdsp) {
        this.ideSpdsp = ideSpdsp;
    }

    public SprBasePostulante getIdeSpbap() {
        return ideSpbap;
    }

    public void setIdeSpbap(SprBasePostulante ideSpbap) {
        this.ideSpbap = ideSpbap;
    }

    public List<SprResultado> getSprResultadoList() {
        return sprResultadoList;
    }

    public void setSprResultadoList(List<SprResultado> sprResultadoList) {
        this.sprResultadoList = sprResultadoList;
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
        hash += (ideSppos != null ? ideSppos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprPostulante)) {
            return false;
        }
        SprPostulante other = (SprPostulante) object;
        if ((this.ideSppos == null && other.ideSppos != null) || (this.ideSppos != null && !this.ideSppos.equals(other.ideSppos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprPostulante[ ideSppos=" + ideSppos + " ]";
    }
    
}
