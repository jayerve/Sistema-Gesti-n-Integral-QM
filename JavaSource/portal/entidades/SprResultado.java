/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "spr_resultado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprResultado.findAll", query = "SELECT s FROM SprResultado s"),
    @NamedQuery(name = "SprResultado.findByIdeSpres", query = "SELECT s FROM SprResultado s WHERE s.ideSpres = :ideSpres"),
    @NamedQuery(name = "SprResultado.findByTotalPuntosSpres", query = "SELECT s FROM SprResultado s WHERE s.totalPuntosSpres = :totalPuntosSpres"),
    @NamedQuery(name = "SprResultado.findByAprobadoSpres", query = "SELECT s FROM SprResultado s WHERE s.aprobadoSpres = :aprobadoSpres"),
    @NamedQuery(name = "SprResultado.findByActivoSpres", query = "SELECT s FROM SprResultado s WHERE s.activoSpres = :activoSpres"),
    @NamedQuery(name = "SprResultado.findByUsuarioIngre", query = "SELECT s FROM SprResultado s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprResultado.findByFechaIngre", query = "SELECT s FROM SprResultado s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprResultado.findByHoraIngre", query = "SELECT s FROM SprResultado s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprResultado.findByUsuarioActua", query = "SELECT s FROM SprResultado s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprResultado.findByFechaActua", query = "SELECT s FROM SprResultado s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprResultado.findByHoraActua", query = "SELECT s FROM SprResultado s WHERE s.horaActua = :horaActua")})
public class SprResultado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spres", nullable = false)
    private Integer ideSpres;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_puntos_spres", precision = 12, scale = 2)
    private BigDecimal totalPuntosSpres;
    @Column(name = "aprobado_spres")
    private Integer aprobadoSpres;
    @Column(name = "activo_spres")
    private Boolean activoSpres;
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
    @JoinColumn(name = "ide_sppos", referencedColumnName = "ide_sppos")
    @ManyToOne
    private SprPostulante ideSppos;
    @JoinColumn(name = "ide_spfac", referencedColumnName = "ide_spfac")
    @ManyToOne
    private SprFactor ideSpfac;
    @JoinColumn(name = "ide_spdsp", referencedColumnName = "ide_spdsp")
    @ManyToOne
    private SprDetalleSolicitudPuesto ideSpdsp;

    public SprResultado() {
    }

    public SprResultado(Integer ideSpres) {
        this.ideSpres = ideSpres;
    }

    public Integer getIdeSpres() {
        return ideSpres;
    }

    public void setIdeSpres(Integer ideSpres) {
        this.ideSpres = ideSpres;
    }

    public BigDecimal getTotalPuntosSpres() {
        return totalPuntosSpres;
    }

    public void setTotalPuntosSpres(BigDecimal totalPuntosSpres) {
        this.totalPuntosSpres = totalPuntosSpres;
    }

    public Integer getAprobadoSpres() {
        return aprobadoSpres;
    }

    public void setAprobadoSpres(Integer aprobadoSpres) {
        this.aprobadoSpres = aprobadoSpres;
    }

    public Boolean getActivoSpres() {
        return activoSpres;
    }

    public void setActivoSpres(Boolean activoSpres) {
        this.activoSpres = activoSpres;
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

    public SprPostulante getIdeSppos() {
        return ideSppos;
    }

    public void setIdeSppos(SprPostulante ideSppos) {
        this.ideSppos = ideSppos;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpres != null ? ideSpres.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprResultado)) {
            return false;
        }
        SprResultado other = (SprResultado) object;
        if ((this.ideSpres == null && other.ideSpres != null) || (this.ideSpres != null && !this.ideSpres.equals(other.ideSpres))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprResultado[ ideSpres=" + ideSpres + " ]";
    }
    
}
