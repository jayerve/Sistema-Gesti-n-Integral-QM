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
@Table(name = "spr_cargo_contratacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprCargoContratacion.findAll", query = "SELECT s FROM SprCargoContratacion s"),
    @NamedQuery(name = "SprCargoContratacion.findByIdeSpcac", query = "SELECT s FROM SprCargoContratacion s WHERE s.ideSpcac = :ideSpcac"),
    @NamedQuery(name = "SprCargoContratacion.findByIdeGepap", query = "SELECT s FROM SprCargoContratacion s WHERE s.ideGepap = :ideGepap"),
    @NamedQuery(name = "SprCargoContratacion.findByActivoSpcac", query = "SELECT s FROM SprCargoContratacion s WHERE s.activoSpcac = :activoSpcac"),
    @NamedQuery(name = "SprCargoContratacion.findByUsuarioIngre", query = "SELECT s FROM SprCargoContratacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprCargoContratacion.findByFechaIngre", query = "SELECT s FROM SprCargoContratacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprCargoContratacion.findByHoraIngre", query = "SELECT s FROM SprCargoContratacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprCargoContratacion.findByUsuarioActua", query = "SELECT s FROM SprCargoContratacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprCargoContratacion.findByFechaActua", query = "SELECT s FROM SprCargoContratacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprCargoContratacion.findByHoraActua", query = "SELECT s FROM SprCargoContratacion s WHERE s.horaActua = :horaActua")})
public class SprCargoContratacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spcac", nullable = false)
    private Integer ideSpcac;
    @Column(name = "ide_gepap")
    private Integer ideGepap;
    @Column(name = "activo_spcac")
    private Boolean activoSpcac;
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
    @JoinColumn(name = "ide_spdsp", referencedColumnName = "ide_spdsp")
    @ManyToOne
    private SprDetalleSolicitudPuesto ideSpdsp;
    @JoinColumn(name = "ide_gttco", referencedColumnName = "ide_gttco")
    @ManyToOne
    private GthTipoContrato ideGttco;

    public SprCargoContratacion() {
    }

    public SprCargoContratacion(Integer ideSpcac) {
        this.ideSpcac = ideSpcac;
    }

    public Integer getIdeSpcac() {
        return ideSpcac;
    }

    public void setIdeSpcac(Integer ideSpcac) {
        this.ideSpcac = ideSpcac;
    }

    public Integer getIdeGepap() {
        return ideGepap;
    }

    public void setIdeGepap(Integer ideGepap) {
        this.ideGepap = ideGepap;
    }

    public Boolean getActivoSpcac() {
        return activoSpcac;
    }

    public void setActivoSpcac(Boolean activoSpcac) {
        this.activoSpcac = activoSpcac;
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

    public SprDetalleSolicitudPuesto getIdeSpdsp() {
        return ideSpdsp;
    }

    public void setIdeSpdsp(SprDetalleSolicitudPuesto ideSpdsp) {
        this.ideSpdsp = ideSpdsp;
    }

    public GthTipoContrato getIdeGttco() {
        return ideGttco;
    }

    public void setIdeGttco(GthTipoContrato ideGttco) {
        this.ideGttco = ideGttco;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpcac != null ? ideSpcac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprCargoContratacion)) {
            return false;
        }
        SprCargoContratacion other = (SprCargoContratacion) object;
        if ((this.ideSpcac == null && other.ideSpcac != null) || (this.ideSpcac != null && !this.ideSpcac.equals(other.ideSpcac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprCargoContratacion[ ideSpcac=" + ideSpcac + " ]";
    }
    
}
