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
@Table(name = "spr_cargo_presupuesto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprCargoPresupuesto.findAll", query = "SELECT s FROM SprCargoPresupuesto s"),
    @NamedQuery(name = "SprCargoPresupuesto.findByIdeSpcap", query = "SELECT s FROM SprCargoPresupuesto s WHERE s.ideSpcap = :ideSpcap"),
    @NamedQuery(name = "SprCargoPresupuesto.findByActivoSpcap", query = "SELECT s FROM SprCargoPresupuesto s WHERE s.activoSpcap = :activoSpcap"),
    @NamedQuery(name = "SprCargoPresupuesto.findByUsuarioIngre", query = "SELECT s FROM SprCargoPresupuesto s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprCargoPresupuesto.findByFechaIngre", query = "SELECT s FROM SprCargoPresupuesto s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprCargoPresupuesto.findByHoraIngre", query = "SELECT s FROM SprCargoPresupuesto s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprCargoPresupuesto.findByUsuarioActua", query = "SELECT s FROM SprCargoPresupuesto s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprCargoPresupuesto.findByFechaActua", query = "SELECT s FROM SprCargoPresupuesto s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprCargoPresupuesto.findByHoraActua", query = "SELECT s FROM SprCargoPresupuesto s WHERE s.horaActua = :horaActua")})
public class SprCargoPresupuesto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spcap", nullable = false)
    private Integer ideSpcap;
    @Column(name = "activo_spcap")
    private Boolean activoSpcap;
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
    @JoinColumn(name = "ide_spprp", referencedColumnName = "ide_spprp")
    @ManyToOne
    private SprPresupuestoPuesto ideSpprp;
    @JoinColumn(name = "ide_spdsp", referencedColumnName = "ide_spdsp")
    @ManyToOne
    private SprDetalleSolicitudPuesto ideSpdsp;

    public SprCargoPresupuesto() {
    }

    public SprCargoPresupuesto(Integer ideSpcap) {
        this.ideSpcap = ideSpcap;
    }

    public Integer getIdeSpcap() {
        return ideSpcap;
    }

    public void setIdeSpcap(Integer ideSpcap) {
        this.ideSpcap = ideSpcap;
    }

    public Boolean getActivoSpcap() {
        return activoSpcap;
    }

    public void setActivoSpcap(Boolean activoSpcap) {
        this.activoSpcap = activoSpcap;
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

    public SprPresupuestoPuesto getIdeSpprp() {
        return ideSpprp;
    }

    public void setIdeSpprp(SprPresupuestoPuesto ideSpprp) {
        this.ideSpprp = ideSpprp;
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
        hash += (ideSpcap != null ? ideSpcap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprCargoPresupuesto)) {
            return false;
        }
        SprCargoPresupuesto other = (SprCargoPresupuesto) object;
        if ((this.ideSpcap == null && other.ideSpcap != null) || (this.ideSpcap != null && !this.ideSpcap.equals(other.ideSpcap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprCargoPresupuesto[ ideSpcap=" + ideSpcap + " ]";
    }
    
}
