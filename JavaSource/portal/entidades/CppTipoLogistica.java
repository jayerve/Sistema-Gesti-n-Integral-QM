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
@Table(name = "cpp_tipo_logistica", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppTipoLogistica.findAll", query = "SELECT c FROM CppTipoLogistica c"),
    @NamedQuery(name = "CppTipoLogistica.findByIdeCptil", query = "SELECT c FROM CppTipoLogistica c WHERE c.ideCptil = :ideCptil"),
    @NamedQuery(name = "CppTipoLogistica.findByDetalleCptil", query = "SELECT c FROM CppTipoLogistica c WHERE c.detalleCptil = :detalleCptil"),
    @NamedQuery(name = "CppTipoLogistica.findByActivoCptil", query = "SELECT c FROM CppTipoLogistica c WHERE c.activoCptil = :activoCptil"),
    @NamedQuery(name = "CppTipoLogistica.findByUsuarioIngre", query = "SELECT c FROM CppTipoLogistica c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppTipoLogistica.findByFechaIngre", query = "SELECT c FROM CppTipoLogistica c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppTipoLogistica.findByHoraIngre", query = "SELECT c FROM CppTipoLogistica c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppTipoLogistica.findByUsuarioActua", query = "SELECT c FROM CppTipoLogistica c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppTipoLogistica.findByFechaActua", query = "SELECT c FROM CppTipoLogistica c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppTipoLogistica.findByHoraActua", query = "SELECT c FROM CppTipoLogistica c WHERE c.horaActua = :horaActua")})
public class CppTipoLogistica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cptil", nullable = false)
    private Integer ideCptil;
    @Size(max = 50)
    @Column(name = "detalle_cptil", length = 50)
    private String detalleCptil;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cptil", nullable = false)
    private boolean activoCptil;
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
    @OneToMany(mappedBy = "ideCptil")
    private List<CppLogistica> cppLogisticaList;

    public CppTipoLogistica() {
    }

    public CppTipoLogistica(Integer ideCptil) {
        this.ideCptil = ideCptil;
    }

    public CppTipoLogistica(Integer ideCptil, boolean activoCptil) {
        this.ideCptil = ideCptil;
        this.activoCptil = activoCptil;
    }

    public Integer getIdeCptil() {
        return ideCptil;
    }

    public void setIdeCptil(Integer ideCptil) {
        this.ideCptil = ideCptil;
    }

    public String getDetalleCptil() {
        return detalleCptil;
    }

    public void setDetalleCptil(String detalleCptil) {
        this.detalleCptil = detalleCptil;
    }

    public boolean getActivoCptil() {
        return activoCptil;
    }

    public void setActivoCptil(boolean activoCptil) {
        this.activoCptil = activoCptil;
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

    public List<CppLogistica> getCppLogisticaList() {
        return cppLogisticaList;
    }

    public void setCppLogisticaList(List<CppLogistica> cppLogisticaList) {
        this.cppLogisticaList = cppLogisticaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCptil != null ? ideCptil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppTipoLogistica)) {
            return false;
        }
        CppTipoLogistica other = (CppTipoLogistica) object;
        if ((this.ideCptil == null && other.ideCptil != null) || (this.ideCptil != null && !this.ideCptil.equals(other.ideCptil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppTipoLogistica[ ideCptil=" + ideCptil + " ]";
    }

}
