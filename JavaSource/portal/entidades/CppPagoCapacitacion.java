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
@Table(name = "cpp_pago_capacitacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppPagoCapacitacion.findAll", query = "SELECT c FROM CppPagoCapacitacion c"),
    @NamedQuery(name = "CppPagoCapacitacion.findByIdeCppac", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.ideCppac = :ideCppac"),
    @NamedQuery(name = "CppPagoCapacitacion.findByMemoPagoCppac", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.memoPagoCppac = :memoPagoCppac"),
    @NamedQuery(name = "CppPagoCapacitacion.findByFechaPagoCppac", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.fechaPagoCppac = :fechaPagoCppac"),
    @NamedQuery(name = "CppPagoCapacitacion.findByValorPagoCppac", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.valorPagoCppac = :valorPagoCppac"),
    @NamedQuery(name = "CppPagoCapacitacion.findByActivoCppac", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.activoCppac = :activoCppac"),
    @NamedQuery(name = "CppPagoCapacitacion.findByUsuarioIngre", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppPagoCapacitacion.findByFechaIngre", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppPagoCapacitacion.findByHoraIngre", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppPagoCapacitacion.findByUsuarioActua", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppPagoCapacitacion.findByFechaActua", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppPagoCapacitacion.findByHoraActua", query = "SELECT c FROM CppPagoCapacitacion c WHERE c.horaActua = :horaActua")})
public class CppPagoCapacitacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cppac", nullable = false)
    private Integer ideCppac;
    @Size(max = 50)
    @Column(name = "memo_pago_cppac", length = 50)
    private String memoPagoCppac;
    @Column(name = "fecha_pago_cppac")
    @Temporal(TemporalType.DATE)
    private Date fechaPagoCppac;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_pago_cppac", precision = 12, scale = 2)
    private BigDecimal valorPagoCppac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cppac", nullable = false)
    private boolean activoCppac;
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
    @JoinColumn(name = "ide_cpesp", referencedColumnName = "ide_cpesp")
    @ManyToOne
    private CppEstadoPago ideCpesp;
    @JoinColumn(name = "ide_cpcap", referencedColumnName = "ide_cpcap")
    @ManyToOne
    private CppCapacitacion ideCpcap;

    public CppPagoCapacitacion() {
    }

    public CppPagoCapacitacion(Integer ideCppac) {
        this.ideCppac = ideCppac;
    }

    public CppPagoCapacitacion(Integer ideCppac, boolean activoCppac) {
        this.ideCppac = ideCppac;
        this.activoCppac = activoCppac;
    }

    public Integer getIdeCppac() {
        return ideCppac;
    }

    public void setIdeCppac(Integer ideCppac) {
        this.ideCppac = ideCppac;
    }

    public String getMemoPagoCppac() {
        return memoPagoCppac;
    }

    public void setMemoPagoCppac(String memoPagoCppac) {
        this.memoPagoCppac = memoPagoCppac;
    }

    public Date getFechaPagoCppac() {
        return fechaPagoCppac;
    }

    public void setFechaPagoCppac(Date fechaPagoCppac) {
        this.fechaPagoCppac = fechaPagoCppac;
    }

    public BigDecimal getValorPagoCppac() {
        return valorPagoCppac;
    }

    public void setValorPagoCppac(BigDecimal valorPagoCppac) {
        this.valorPagoCppac = valorPagoCppac;
    }

    public boolean getActivoCppac() {
        return activoCppac;
    }

    public void setActivoCppac(boolean activoCppac) {
        this.activoCppac = activoCppac;
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

    public CppEstadoPago getIdeCpesp() {
        return ideCpesp;
    }

    public void setIdeCpesp(CppEstadoPago ideCpesp) {
        this.ideCpesp = ideCpesp;
    }

    public CppCapacitacion getIdeCpcap() {
        return ideCpcap;
    }

    public void setIdeCpcap(CppCapacitacion ideCpcap) {
        this.ideCpcap = ideCpcap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCppac != null ? ideCppac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppPagoCapacitacion)) {
            return false;
        }
        CppPagoCapacitacion other = (CppPagoCapacitacion) object;
        if ((this.ideCppac == null && other.ideCppac != null) || (this.ideCppac != null && !this.ideCppac.equals(other.ideCppac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppPagoCapacitacion[ ideCppac=" + ideCppac + " ]";
    }
    
}
