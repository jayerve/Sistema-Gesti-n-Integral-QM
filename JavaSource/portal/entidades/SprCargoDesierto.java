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
@Table(name = "spr_cargo_desierto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprCargoDesierto.findAll", query = "SELECT s FROM SprCargoDesierto s"),
    @NamedQuery(name = "SprCargoDesierto.findByIdeSpcad", query = "SELECT s FROM SprCargoDesierto s WHERE s.ideSpcad = :ideSpcad"),
    @NamedQuery(name = "SprCargoDesierto.findByNroDocumentoSpcad", query = "SELECT s FROM SprCargoDesierto s WHERE s.nroDocumentoSpcad = :nroDocumentoSpcad"),
    @NamedQuery(name = "SprCargoDesierto.findByFechaSpcad", query = "SELECT s FROM SprCargoDesierto s WHERE s.fechaSpcad = :fechaSpcad"),
    @NamedQuery(name = "SprCargoDesierto.findByDetalleSpcad", query = "SELECT s FROM SprCargoDesierto s WHERE s.detalleSpcad = :detalleSpcad"),
    @NamedQuery(name = "SprCargoDesierto.findByActivoSpcad", query = "SELECT s FROM SprCargoDesierto s WHERE s.activoSpcad = :activoSpcad"),
    @NamedQuery(name = "SprCargoDesierto.findByUsuarioIngre", query = "SELECT s FROM SprCargoDesierto s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprCargoDesierto.findByFechaIngre", query = "SELECT s FROM SprCargoDesierto s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprCargoDesierto.findByHoraIngre", query = "SELECT s FROM SprCargoDesierto s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprCargoDesierto.findByUsuarioActua", query = "SELECT s FROM SprCargoDesierto s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprCargoDesierto.findByFechaActua", query = "SELECT s FROM SprCargoDesierto s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprCargoDesierto.findByHoraActua", query = "SELECT s FROM SprCargoDesierto s WHERE s.horaActua = :horaActua")})
public class SprCargoDesierto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spcad", nullable = false)
    private Integer ideSpcad;
    @Size(max = 50)
    @Column(name = "nro_documento_spcad", length = 50)
    private String nroDocumentoSpcad;
    @Column(name = "fecha_spcad")
    @Temporal(TemporalType.DATE)
    private Date fechaSpcad;
    @Size(max = 4000)
    @Column(name = "detalle_spcad", length = 4000)
    private String detalleSpcad;
    @Column(name = "activo_spcad")
    private Boolean activoSpcad;
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

    public SprCargoDesierto() {
    }

    public SprCargoDesierto(Integer ideSpcad) {
        this.ideSpcad = ideSpcad;
    }

    public Integer getIdeSpcad() {
        return ideSpcad;
    }

    public void setIdeSpcad(Integer ideSpcad) {
        this.ideSpcad = ideSpcad;
    }

    public String getNroDocumentoSpcad() {
        return nroDocumentoSpcad;
    }

    public void setNroDocumentoSpcad(String nroDocumentoSpcad) {
        this.nroDocumentoSpcad = nroDocumentoSpcad;
    }

    public Date getFechaSpcad() {
        return fechaSpcad;
    }

    public void setFechaSpcad(Date fechaSpcad) {
        this.fechaSpcad = fechaSpcad;
    }

    public String getDetalleSpcad() {
        return detalleSpcad;
    }

    public void setDetalleSpcad(String detalleSpcad) {
        this.detalleSpcad = detalleSpcad;
    }

    public Boolean getActivoSpcad() {
        return activoSpcad;
    }

    public void setActivoSpcad(Boolean activoSpcad) {
        this.activoSpcad = activoSpcad;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpcad != null ? ideSpcad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprCargoDesierto)) {
            return false;
        }
        SprCargoDesierto other = (SprCargoDesierto) object;
        if ((this.ideSpcad == null && other.ideSpcad != null) || (this.ideSpcad != null && !this.ideSpcad.equals(other.ideSpcad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprCargoDesierto[ ideSpcad=" + ideSpcad + " ]";
    }
    
}
