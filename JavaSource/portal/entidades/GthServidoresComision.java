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
@Table(name = "gth_servidores_comision", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthServidoresComision.findAll", query = "SELECT g FROM GthServidoresComision g"),
    @NamedQuery(name = "GthServidoresComision.findByIdeGtsec", query = "SELECT g FROM GthServidoresComision g WHERE g.ideGtsec = :ideGtsec"),
    @NamedQuery(name = "GthServidoresComision.findByObservacionGtsec", query = "SELECT g FROM GthServidoresComision g WHERE g.observacionGtsec = :observacionGtsec"),
    @NamedQuery(name = "GthServidoresComision.findByActivoGtsec", query = "SELECT g FROM GthServidoresComision g WHERE g.activoGtsec = :activoGtsec"),
    @NamedQuery(name = "GthServidoresComision.findByUsuarioIngre", query = "SELECT g FROM GthServidoresComision g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthServidoresComision.findByFechaIngre", query = "SELECT g FROM GthServidoresComision g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthServidoresComision.findByUsuarioActua", query = "SELECT g FROM GthServidoresComision g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthServidoresComision.findByFechaActua", query = "SELECT g FROM GthServidoresComision g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthServidoresComision.findByHoraIngre", query = "SELECT g FROM GthServidoresComision g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthServidoresComision.findByHoraActua", query = "SELECT g FROM GthServidoresComision g WHERE g.horaActua = :horaActua")})
public class GthServidoresComision implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtsec", nullable = false)
    private Integer ideGtsec;
    @Size(max = 1000)
    @Column(name = "observacion_gtsec", length = 1000)
    private String observacionGtsec;
    @Column(name = "activo_gtsec")
    private Boolean activoGtsec;
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
    @JoinColumn(name = "ide_gtvia", referencedColumnName = "ide_gtvia")
    @ManyToOne
    private GthViaticos ideGtvia;

    public GthServidoresComision() {
    }

    public GthServidoresComision(Integer ideGtsec) {
        this.ideGtsec = ideGtsec;
    }

    public Integer getIdeGtsec() {
        return ideGtsec;
    }

    public void setIdeGtsec(Integer ideGtsec) {
        this.ideGtsec = ideGtsec;
    }

    public String getObservacionGtsec() {
        return observacionGtsec;
    }

    public void setObservacionGtsec(String observacionGtsec) {
        this.observacionGtsec = observacionGtsec;
    }

    public Boolean getActivoGtsec() {
        return activoGtsec;
    }

    public void setActivoGtsec(Boolean activoGtsec) {
        this.activoGtsec = activoGtsec;
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

    public GthViaticos getIdeGtvia() {
        return ideGtvia;
    }

    public void setIdeGtvia(GthViaticos ideGtvia) {
        this.ideGtvia = ideGtvia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtsec != null ? ideGtsec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthServidoresComision)) {
            return false;
        }
        GthServidoresComision other = (GthServidoresComision) object;
        if ((this.ideGtsec == null && other.ideGtsec != null) || (this.ideGtsec != null && !this.ideGtsec.equals(other.ideGtsec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthServidoresComision[ ideGtsec=" + ideGtsec + " ]";
    }
    
}
