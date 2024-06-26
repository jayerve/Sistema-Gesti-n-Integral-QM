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
@Table(name = "cnt_tipo_comprobante", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CntTipoComprobante.findAll", query = "SELECT c FROM CntTipoComprobante c"),
    @NamedQuery(name = "CntTipoComprobante.findByIdeCntic", query = "SELECT c FROM CntTipoComprobante c WHERE c.ideCntic = :ideCntic"),
    @NamedQuery(name = "CntTipoComprobante.findByDetalleCntic", query = "SELECT c FROM CntTipoComprobante c WHERE c.detalleCntic = :detalleCntic"),
    @NamedQuery(name = "CntTipoComprobante.findByActivoCntic", query = "SELECT c FROM CntTipoComprobante c WHERE c.activoCntic = :activoCntic"),
    @NamedQuery(name = "CntTipoComprobante.findByUsuarioIngre", query = "SELECT c FROM CntTipoComprobante c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CntTipoComprobante.findByFechaIngre", query = "SELECT c FROM CntTipoComprobante c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CntTipoComprobante.findByHoraIngre", query = "SELECT c FROM CntTipoComprobante c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CntTipoComprobante.findByUsuarioActua", query = "SELECT c FROM CntTipoComprobante c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CntTipoComprobante.findByFechaActua", query = "SELECT c FROM CntTipoComprobante c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CntTipoComprobante.findByHoraActua", query = "SELECT c FROM CntTipoComprobante c WHERE c.horaActua = :horaActua")})
public class CntTipoComprobante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cntic", nullable = false)
    private Integer ideCntic;
    @Size(max = 100)
    @Column(name = "detalle_cntic", length = 100)
    private String detalleCntic;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cntic", nullable = false)
    private boolean activoCntic;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideCntic")
    private List<CntMovimientoContable> cntMovimientoContableList;

    public CntTipoComprobante() {
    }

    public CntTipoComprobante(Integer ideCntic) {
        this.ideCntic = ideCntic;
    }

    public CntTipoComprobante(Integer ideCntic, boolean activoCntic) {
        this.ideCntic = ideCntic;
        this.activoCntic = activoCntic;
    }

    public Integer getIdeCntic() {
        return ideCntic;
    }

    public void setIdeCntic(Integer ideCntic) {
        this.ideCntic = ideCntic;
    }

    public String getDetalleCntic() {
        return detalleCntic;
    }

    public void setDetalleCntic(String detalleCntic) {
        this.detalleCntic = detalleCntic;
    }

    public boolean getActivoCntic() {
        return activoCntic;
    }

    public void setActivoCntic(boolean activoCntic) {
        this.activoCntic = activoCntic;
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

    public List<CntMovimientoContable> getCntMovimientoContableList() {
        return cntMovimientoContableList;
    }

    public void setCntMovimientoContableList(List<CntMovimientoContable> cntMovimientoContableList) {
        this.cntMovimientoContableList = cntMovimientoContableList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCntic != null ? ideCntic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CntTipoComprobante)) {
            return false;
        }
        CntTipoComprobante other = (CntTipoComprobante) object;
        if ((this.ideCntic == null && other.ideCntic != null) || (this.ideCntic != null && !this.ideCntic.equals(other.ideCntic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CntTipoComprobante[ ideCntic=" + ideCntic + " ]";
    }
    
}
