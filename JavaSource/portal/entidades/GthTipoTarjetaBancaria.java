/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "gth_tipo_tarjeta_bancaria", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoTarjetaBancaria.findAll", query = "SELECT g FROM GthTipoTarjetaBancaria g"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByIdeGtttb", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.ideGtttb = :ideGtttb"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByDetalleGtttb", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.detalleGtttb = :detalleGtttb"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByActivoGtttb", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.activoGtttb = :activoGtttb"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByUsuarioIngre", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByFechaIngre", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByUsuarioActua", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByFechaActua", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByHoraIngre", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoTarjetaBancaria.findByHoraActua", query = "SELECT g FROM GthTipoTarjetaBancaria g WHERE g.horaActua = :horaActua")})
public class GthTipoTarjetaBancaria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gtttb", nullable = false)
    private Integer ideGtttb;
    @Size(max = 50)
    @Column(name = "detalle_gtttb", length = 50)
    private String detalleGtttb;
    @Column(name = "activo_gtttb")
    private Boolean activoGtttb;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gthTipoTarjetaBancaria")
    private List<GthTarjetaBancaria> gthTarjetaBancariaList;

    public GthTipoTarjetaBancaria() {
    }

    public GthTipoTarjetaBancaria(Integer ideGtttb) {
        this.ideGtttb = ideGtttb;
    }

    public Integer getIdeGtttb() {
        return ideGtttb;
    }

    public void setIdeGtttb(Integer ideGtttb) {
        this.ideGtttb = ideGtttb;
    }

    public String getDetalleGtttb() {
        return detalleGtttb;
    }

    public void setDetalleGtttb(String detalleGtttb) {
        this.detalleGtttb = detalleGtttb;
    }

    public Boolean getActivoGtttb() {
        return activoGtttb;
    }

    public void setActivoGtttb(Boolean activoGtttb) {
        this.activoGtttb = activoGtttb;
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

    public List<GthTarjetaBancaria> getGthTarjetaBancariaList() {
        return gthTarjetaBancariaList;
    }

    public void setGthTarjetaBancariaList(List<GthTarjetaBancaria> gthTarjetaBancariaList) {
        this.gthTarjetaBancariaList = gthTarjetaBancariaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGtttb != null ? ideGtttb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoTarjetaBancaria)) {
            return false;
        }
        GthTipoTarjetaBancaria other = (GthTipoTarjetaBancaria) object;
        if ((this.ideGtttb == null && other.ideGtttb != null) || (this.ideGtttb != null && !this.ideGtttb.equals(other.ideGtttb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoTarjetaBancaria[ ideGtttb=" + ideGtttb + " ]";
    }
    
}
