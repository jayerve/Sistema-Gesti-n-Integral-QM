/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "gth_tarjeta_bancaria", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTarjetaBancaria.findAll", query = "SELECT g FROM GthTarjetaBancaria g"),
    @NamedQuery(name = "GthTarjetaBancaria.findByIdeGttab", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.gthTarjetaBancariaPK.ideGttab = :ideGttab"),
    @NamedQuery(name = "GthTarjetaBancaria.findByIdeGtttb", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.gthTarjetaBancariaPK.ideGtttb = :ideGtttb"),
    @NamedQuery(name = "GthTarjetaBancaria.findByDetalleGttab", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.detalleGttab = :detalleGttab"),
    @NamedQuery(name = "GthTarjetaBancaria.findByActivoGttab", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.activoGttab = :activoGttab"),
    @NamedQuery(name = "GthTarjetaBancaria.findByUsuarioIngre", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTarjetaBancaria.findByFechaIngre", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTarjetaBancaria.findByUsuarioActua", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTarjetaBancaria.findByFechaActua", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTarjetaBancaria.findByHoraIngre", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTarjetaBancaria.findByHoraActua", query = "SELECT g FROM GthTarjetaBancaria g WHERE g.horaActua = :horaActua")})
public class GthTarjetaBancaria implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GthTarjetaBancariaPK gthTarjetaBancariaPK;
    @Size(max = 50)
    @Column(name = "detalle_gttab", length = 50)
    private String detalleGttab;
    @Column(name = "activo_gttab")
    private Boolean activoGttab;
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
    @JoinColumn(name = "ide_gtttb", referencedColumnName = "ide_gtttb", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GthTipoTarjetaBancaria gthTipoTarjetaBancaria;
    @OneToMany(mappedBy = "gthTarjetaBancaria")
    private List<GthTarjetaCreditoEmpleado> gthTarjetaCreditoEmpleadoList;

    public GthTarjetaBancaria() {
    }

    public GthTarjetaBancaria(GthTarjetaBancariaPK gthTarjetaBancariaPK) {
        this.gthTarjetaBancariaPK = gthTarjetaBancariaPK;
    }

    public GthTarjetaBancaria(int ideGttab, int ideGtttb) {
        this.gthTarjetaBancariaPK = new GthTarjetaBancariaPK(ideGttab, ideGtttb);
    }

    public GthTarjetaBancariaPK getGthTarjetaBancariaPK() {
        return gthTarjetaBancariaPK;
    }

    public void setGthTarjetaBancariaPK(GthTarjetaBancariaPK gthTarjetaBancariaPK) {
        this.gthTarjetaBancariaPK = gthTarjetaBancariaPK;
    }

    public String getDetalleGttab() {
        return detalleGttab;
    }

    public void setDetalleGttab(String detalleGttab) {
        this.detalleGttab = detalleGttab;
    }

    public Boolean getActivoGttab() {
        return activoGttab;
    }

    public void setActivoGttab(Boolean activoGttab) {
        this.activoGttab = activoGttab;
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

    public GthTipoTarjetaBancaria getGthTipoTarjetaBancaria() {
        return gthTipoTarjetaBancaria;
    }

    public void setGthTipoTarjetaBancaria(GthTipoTarjetaBancaria gthTipoTarjetaBancaria) {
        this.gthTipoTarjetaBancaria = gthTipoTarjetaBancaria;
    }

    public List<GthTarjetaCreditoEmpleado> getGthTarjetaCreditoEmpleadoList() {
        return gthTarjetaCreditoEmpleadoList;
    }

    public void setGthTarjetaCreditoEmpleadoList(List<GthTarjetaCreditoEmpleado> gthTarjetaCreditoEmpleadoList) {
        this.gthTarjetaCreditoEmpleadoList = gthTarjetaCreditoEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gthTarjetaBancariaPK != null ? gthTarjetaBancariaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTarjetaBancaria)) {
            return false;
        }
        GthTarjetaBancaria other = (GthTarjetaBancaria) object;
        if ((this.gthTarjetaBancariaPK == null && other.gthTarjetaBancariaPK != null) || (this.gthTarjetaBancariaPK != null && !this.gthTarjetaBancariaPK.equals(other.gthTarjetaBancariaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTarjetaBancaria[ gthTarjetaBancariaPK=" + gthTarjetaBancariaPK + " ]";
    }
    
}
