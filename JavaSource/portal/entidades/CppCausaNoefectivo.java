/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "cpp_causa_noefectivo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppCausaNoefectivo.findAll", query = "SELECT c FROM CppCausaNoefectivo c"),
    @NamedQuery(name = "CppCausaNoefectivo.findByIdeCpcan", query = "SELECT c FROM CppCausaNoefectivo c WHERE c.ideCpcan = :ideCpcan"),
    @NamedQuery(name = "CppCausaNoefectivo.findByDetalleCppcan", query = "SELECT c FROM CppCausaNoefectivo c WHERE c.detalleCppcan = :detalleCppcan"),
    @NamedQuery(name = "CppCausaNoefectivo.findByActivoCpcan", query = "SELECT c FROM CppCausaNoefectivo c WHERE c.activoCpcan = :activoCpcan"),
    @NamedQuery(name = "CppCausaNoefectivo.findByUsuarioIngre", query = "SELECT c FROM CppCausaNoefectivo c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppCausaNoefectivo.findByFechaIngre", query = "SELECT c FROM CppCausaNoefectivo c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppCausaNoefectivo.findByHoraIngre", query = "SELECT c FROM CppCausaNoefectivo c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppCausaNoefectivo.findByUsuarioActua", query = "SELECT c FROM CppCausaNoefectivo c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppCausaNoefectivo.findByFechaActua", query = "SELECT c FROM CppCausaNoefectivo c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppCausaNoefectivo.findByHoraActua", query = "SELECT c FROM CppCausaNoefectivo c WHERE c.horaActua = :horaActua")})
public class CppCausaNoefectivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpcan", nullable = false)
    private Integer ideCpcan;
    @Size(max = 100)
    @Column(name = "detalle_cppcan", length = 100)
    private String detalleCppcan;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpcan", nullable = false)
    private boolean activoCpcan;
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
    @OneToMany(mappedBy = "ideCpcan")
    private List<CppDetalleNoefectivo> cppDetalleNoefectivoList;

    public CppCausaNoefectivo() {
    }

    public CppCausaNoefectivo(Integer ideCpcan) {
        this.ideCpcan = ideCpcan;
    }

    public CppCausaNoefectivo(Integer ideCpcan, boolean activoCpcan) {
        this.ideCpcan = ideCpcan;
        this.activoCpcan = activoCpcan;
    }

    public Integer getIdeCpcan() {
        return ideCpcan;
    }

    public void setIdeCpcan(Integer ideCpcan) {
        this.ideCpcan = ideCpcan;
    }

    public String getDetalleCppcan() {
        return detalleCppcan;
    }

    public void setDetalleCppcan(String detalleCppcan) {
        this.detalleCppcan = detalleCppcan;
    }

    public boolean getActivoCpcan() {
        return activoCpcan;
    }

    public void setActivoCpcan(boolean activoCpcan) {
        this.activoCpcan = activoCpcan;
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

    public List<CppDetalleNoefectivo> getCppDetalleNoefectivoList() {
        return cppDetalleNoefectivoList;
    }

    public void setCppDetalleNoefectivoList(List<CppDetalleNoefectivo> cppDetalleNoefectivoList) {
        this.cppDetalleNoefectivoList = cppDetalleNoefectivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpcan != null ? ideCpcan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppCausaNoefectivo)) {
            return false;
        }
        CppCausaNoefectivo other = (CppCausaNoefectivo) object;
        if ((this.ideCpcan == null && other.ideCpcan != null) || (this.ideCpcan != null && !this.ideCpcan.equals(other.ideCpcan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppCausaNoefectivo[ ideCpcan=" + ideCpcan + " ]";
    }
    
}
