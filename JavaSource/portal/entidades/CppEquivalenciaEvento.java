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
@Table(name = "cpp_equivalencia_evento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppEquivalenciaEvento.findAll", query = "SELECT c FROM CppEquivalenciaEvento c"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByIdeCpeqe", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.ideCpeqe = :ideCpeqe"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByDetalleCpeqe", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.detalleCpeqe = :detalleCpeqe"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByEquivalenciaCpeqe", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.equivalenciaCpeqe = :equivalenciaCpeqe"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByActivoCpeqe", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.activoCpeqe = :activoCpeqe"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByUsuarioIngre", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByFechaIngre", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByHoraIngre", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByUsuarioActua", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByFechaActua", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppEquivalenciaEvento.findByHoraActua", query = "SELECT c FROM CppEquivalenciaEvento c WHERE c.horaActua = :horaActua")})
public class CppEquivalenciaEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpeqe", nullable = false)
    private Integer ideCpeqe;
    @Size(max = 50)
    @Column(name = "detalle_cpeqe", length = 50)
    private String detalleCpeqe;
    @Column(name = "equivalencia_cpeqe")
    private Integer equivalenciaCpeqe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpeqe", nullable = false)
    private boolean activoCpeqe;
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
    @OneToMany(mappedBy = "ideCpeqe")
    private List<CppDetalleEvalPersepcion> cppDetalleEvalPersepcionList;
    @OneToMany(mappedBy = "ideCpeqe")
    private List<CppDetalleEvalEvento> cppDetalleEvalEventoList;
    @OneToMany(mappedBy = "ideCpeqe")
    private List<CppDetalleEvalTaller> cppDetalleEvalTallerList;

    public CppEquivalenciaEvento() {
    }

    public CppEquivalenciaEvento(Integer ideCpeqe) {
        this.ideCpeqe = ideCpeqe;
    }

    public CppEquivalenciaEvento(Integer ideCpeqe, boolean activoCpeqe) {
        this.ideCpeqe = ideCpeqe;
        this.activoCpeqe = activoCpeqe;
    }

    public Integer getIdeCpeqe() {
        return ideCpeqe;
    }

    public void setIdeCpeqe(Integer ideCpeqe) {
        this.ideCpeqe = ideCpeqe;
    }

    public String getDetalleCpeqe() {
        return detalleCpeqe;
    }

    public void setDetalleCpeqe(String detalleCpeqe) {
        this.detalleCpeqe = detalleCpeqe;
    }

    public Integer getEquivalenciaCpeqe() {
        return equivalenciaCpeqe;
    }

    public void setEquivalenciaCpeqe(Integer equivalenciaCpeqe) {
        this.equivalenciaCpeqe = equivalenciaCpeqe;
    }

    public boolean getActivoCpeqe() {
        return activoCpeqe;
    }

    public void setActivoCpeqe(boolean activoCpeqe) {
        this.activoCpeqe = activoCpeqe;
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

    public List<CppDetalleEvalPersepcion> getCppDetalleEvalPersepcionList() {
        return cppDetalleEvalPersepcionList;
    }

    public void setCppDetalleEvalPersepcionList(List<CppDetalleEvalPersepcion> cppDetalleEvalPersepcionList) {
        this.cppDetalleEvalPersepcionList = cppDetalleEvalPersepcionList;
    }

    public List<CppDetalleEvalEvento> getCppDetalleEvalEventoList() {
        return cppDetalleEvalEventoList;
    }

    public void setCppDetalleEvalEventoList(List<CppDetalleEvalEvento> cppDetalleEvalEventoList) {
        this.cppDetalleEvalEventoList = cppDetalleEvalEventoList;
    }

    public List<CppDetalleEvalTaller> getCppDetalleEvalTallerList() {
        return cppDetalleEvalTallerList;
    }

    public void setCppDetalleEvalTallerList(List<CppDetalleEvalTaller> cppDetalleEvalTallerList) {
        this.cppDetalleEvalTallerList = cppDetalleEvalTallerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpeqe != null ? ideCpeqe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppEquivalenciaEvento)) {
            return false;
        }
        CppEquivalenciaEvento other = (CppEquivalenciaEvento) object;
        if ((this.ideCpeqe == null && other.ideCpeqe != null) || (this.ideCpeqe != null && !this.ideCpeqe.equals(other.ideCpeqe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppEquivalenciaEvento[ ideCpeqe=" + ideCpeqe + " ]";
    }
    
}
