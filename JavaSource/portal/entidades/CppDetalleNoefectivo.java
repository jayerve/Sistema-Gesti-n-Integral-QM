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
@Table(name = "cpp_detalle_noefectivo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppDetalleNoefectivo.findAll", query = "SELECT c FROM CppDetalleNoefectivo c"),
    @NamedQuery(name = "CppDetalleNoefectivo.findByIdeCpden", query = "SELECT c FROM CppDetalleNoefectivo c WHERE c.ideCpden = :ideCpden"),
    @NamedQuery(name = "CppDetalleNoefectivo.findBySiNoCpden", query = "SELECT c FROM CppDetalleNoefectivo c WHERE c.siNoCpden = :siNoCpden"),
    @NamedQuery(name = "CppDetalleNoefectivo.findByActivoCpden", query = "SELECT c FROM CppDetalleNoefectivo c WHERE c.activoCpden = :activoCpden"),
    @NamedQuery(name = "CppDetalleNoefectivo.findByUsuarioIngre", query = "SELECT c FROM CppDetalleNoefectivo c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppDetalleNoefectivo.findByFechaIngre", query = "SELECT c FROM CppDetalleNoefectivo c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppDetalleNoefectivo.findByHoraIngre", query = "SELECT c FROM CppDetalleNoefectivo c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppDetalleNoefectivo.findByUsuarioActua", query = "SELECT c FROM CppDetalleNoefectivo c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppDetalleNoefectivo.findByFechaActua", query = "SELECT c FROM CppDetalleNoefectivo c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppDetalleNoefectivo.findByHoraActua", query = "SELECT c FROM CppDetalleNoefectivo c WHERE c.horaActua = :horaActua")})
public class CppDetalleNoefectivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpden", nullable = false)
    private Integer ideCpden;
    @Column(name = "si_no_cpden")
    private Integer siNoCpden;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpden", nullable = false)
    private boolean activoCpden;
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
    @JoinColumn(name = "ide_cpevj", referencedColumnName = "ide_cpevj")
    @ManyToOne
    private CppEvaluacionJefe ideCpevj;
    @JoinColumn(name = "ide_cpcan", referencedColumnName = "ide_cpcan")
    @ManyToOne
    private CppCausaNoefectivo ideCpcan;

    public CppDetalleNoefectivo() {
    }

    public CppDetalleNoefectivo(Integer ideCpden) {
        this.ideCpden = ideCpden;
    }

    public CppDetalleNoefectivo(Integer ideCpden, boolean activoCpden) {
        this.ideCpden = ideCpden;
        this.activoCpden = activoCpden;
    }

    public Integer getIdeCpden() {
        return ideCpden;
    }

    public void setIdeCpden(Integer ideCpden) {
        this.ideCpden = ideCpden;
    }

    public Integer getSiNoCpden() {
        return siNoCpden;
    }

    public void setSiNoCpden(Integer siNoCpden) {
        this.siNoCpden = siNoCpden;
    }

    public boolean getActivoCpden() {
        return activoCpden;
    }

    public void setActivoCpden(boolean activoCpden) {
        this.activoCpden = activoCpden;
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

    public CppEvaluacionJefe getIdeCpevj() {
        return ideCpevj;
    }

    public void setIdeCpevj(CppEvaluacionJefe ideCpevj) {
        this.ideCpevj = ideCpevj;
    }

    public CppCausaNoefectivo getIdeCpcan() {
        return ideCpcan;
    }

    public void setIdeCpcan(CppCausaNoefectivo ideCpcan) {
        this.ideCpcan = ideCpcan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpden != null ? ideCpden.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppDetalleNoefectivo)) {
            return false;
        }
        CppDetalleNoefectivo other = (CppDetalleNoefectivo) object;
        if ((this.ideCpden == null && other.ideCpden != null) || (this.ideCpden != null && !this.ideCpden.equals(other.ideCpden))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppDetalleNoefectivo[ ideCpden=" + ideCpden + " ]";
    }
    
}
