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
@Table(name = "gth_union_libre", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthUnionLibre.findAll", query = "SELECT g FROM GthUnionLibre g"),
    @NamedQuery(name = "GthUnionLibre.findByIdeGtunl", query = "SELECT g FROM GthUnionLibre g WHERE g.ideGtunl = :ideGtunl"),
    @NamedQuery(name = "GthUnionLibre.findByAutoridadNotariaGtunl", query = "SELECT g FROM GthUnionLibre g WHERE g.autoridadNotariaGtunl = :autoridadNotariaGtunl"),
    @NamedQuery(name = "GthUnionLibre.findByLugarFechaGtunl", query = "SELECT g FROM GthUnionLibre g WHERE g.lugarFechaGtunl = :lugarFechaGtunl"),
    @NamedQuery(name = "GthUnionLibre.findByObservacionGtunl", query = "SELECT g FROM GthUnionLibre g WHERE g.observacionGtunl = :observacionGtunl"),
    @NamedQuery(name = "GthUnionLibre.findByActivoGtunl", query = "SELECT g FROM GthUnionLibre g WHERE g.activoGtunl = :activoGtunl"),
    @NamedQuery(name = "GthUnionLibre.findByUsuarioIngre", query = "SELECT g FROM GthUnionLibre g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthUnionLibre.findByFechaIngre", query = "SELECT g FROM GthUnionLibre g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthUnionLibre.findByUsuarioActua", query = "SELECT g FROM GthUnionLibre g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthUnionLibre.findByFechaActua", query = "SELECT g FROM GthUnionLibre g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthUnionLibre.findByHoraIngre", query = "SELECT g FROM GthUnionLibre g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthUnionLibre.findByHoraActua", query = "SELECT g FROM GthUnionLibre g WHERE g.horaActua = :horaActua")})
public class GthUnionLibre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtunl", nullable = false)
    private Integer ideGtunl;
    @Size(max = 50)
    @Column(name = "autoridad_notaria_gtunl", length = 50)
    private String autoridadNotariaGtunl;
    @Size(max = 100)
    @Column(name = "lugar_fecha_gtunl", length = 100)
    private String lugarFechaGtunl;
    @Size(max = 4000)
    @Column(name = "observacion_gtunl", length = 4000)
    private String observacionGtunl;
    @Column(name = "activo_gtunl")
    private Boolean activoGtunl;
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
    @JoinColumn(name = "ide_gtcon", referencedColumnName = "ide_gtcon")
    @ManyToOne
    private GthConyuge ideGtcon;

    public GthUnionLibre() {
    }

    public GthUnionLibre(Integer ideGtunl) {
        this.ideGtunl = ideGtunl;
    }

    public Integer getIdeGtunl() {
        return ideGtunl;
    }

    public void setIdeGtunl(Integer ideGtunl) {
        this.ideGtunl = ideGtunl;
    }

    public String getAutoridadNotariaGtunl() {
        return autoridadNotariaGtunl;
    }

    public void setAutoridadNotariaGtunl(String autoridadNotariaGtunl) {
        this.autoridadNotariaGtunl = autoridadNotariaGtunl;
    }

    public String getLugarFechaGtunl() {
        return lugarFechaGtunl;
    }

    public void setLugarFechaGtunl(String lugarFechaGtunl) {
        this.lugarFechaGtunl = lugarFechaGtunl;
    }

    public String getObservacionGtunl() {
        return observacionGtunl;
    }

    public void setObservacionGtunl(String observacionGtunl) {
        this.observacionGtunl = observacionGtunl;
    }

    public Boolean getActivoGtunl() {
        return activoGtunl;
    }

    public void setActivoGtunl(Boolean activoGtunl) {
        this.activoGtunl = activoGtunl;
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

    public GthConyuge getIdeGtcon() {
        return ideGtcon;
    }

    public void setIdeGtcon(GthConyuge ideGtcon) {
        this.ideGtcon = ideGtcon;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtunl != null ? ideGtunl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthUnionLibre)) {
            return false;
        }
        GthUnionLibre other = (GthUnionLibre) object;
        if ((this.ideGtunl == null && other.ideGtunl != null) || (this.ideGtunl != null && !this.ideGtunl.equals(other.ideGtunl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthUnionLibre[ ideGtunl=" + ideGtunl + " ]";
    }
    
}
