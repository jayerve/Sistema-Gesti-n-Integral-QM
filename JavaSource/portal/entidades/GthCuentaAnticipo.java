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
@Table(name = "gth_cuenta_anticipo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthCuentaAnticipo.findAll", query = "SELECT g FROM GthCuentaAnticipo g"),
    @NamedQuery(name = "GthCuentaAnticipo.findByIdeGtcua", query = "SELECT g FROM GthCuentaAnticipo g WHERE g.ideGtcua = :ideGtcua"),
    @NamedQuery(name = "GthCuentaAnticipo.findByActivoGtcua", query = "SELECT g FROM GthCuentaAnticipo g WHERE g.activoGtcua = :activoGtcua"),
    @NamedQuery(name = "GthCuentaAnticipo.findByUsuarioIngre", query = "SELECT g FROM GthCuentaAnticipo g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthCuentaAnticipo.findByFechaIngre", query = "SELECT g FROM GthCuentaAnticipo g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthCuentaAnticipo.findByHoraIngre", query = "SELECT g FROM GthCuentaAnticipo g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthCuentaAnticipo.findByUsuarioActua", query = "SELECT g FROM GthCuentaAnticipo g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthCuentaAnticipo.findByFechaActua", query = "SELECT g FROM GthCuentaAnticipo g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthCuentaAnticipo.findByHoraActua", query = "SELECT g FROM GthCuentaAnticipo g WHERE g.horaActua = :horaActua")})
public class GthCuentaAnticipo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtcua", nullable = false)
    private Long ideGtcua;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_gtcua", nullable = false)
    private boolean activoGtcua;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @JoinColumn(name = "ide_cocac", referencedColumnName = "ide_cocac")
    @ManyToOne
    private ContCatalogoCuenta ideCocac;

    public GthCuentaAnticipo() {
    }

    public GthCuentaAnticipo(Long ideGtcua) {
        this.ideGtcua = ideGtcua;
    }

    public GthCuentaAnticipo(Long ideGtcua, boolean activoGtcua) {
        this.ideGtcua = ideGtcua;
        this.activoGtcua = activoGtcua;
    }

    public Long getIdeGtcua() {
        return ideGtcua;
    }

    public void setIdeGtcua(Long ideGtcua) {
        this.ideGtcua = ideGtcua;
    }

    public boolean getActivoGtcua() {
        return activoGtcua;
    }

    public void setActivoGtcua(boolean activoGtcua) {
        this.activoGtcua = activoGtcua;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
    }

    public ContCatalogoCuenta getIdeCocac() {
        return ideCocac;
    }

    public void setIdeCocac(ContCatalogoCuenta ideCocac) {
        this.ideCocac = ideCocac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtcua != null ? ideGtcua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthCuentaAnticipo)) {
            return false;
        }
        GthCuentaAnticipo other = (GthCuentaAnticipo) object;
        if ((this.ideGtcua == null && other.ideGtcua != null) || (this.ideGtcua != null && !this.ideGtcua.equals(other.ideGtcua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthCuentaAnticipo[ ideGtcua=" + ideGtcua + " ]";
    }
    
}
