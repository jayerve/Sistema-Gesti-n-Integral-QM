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
@Table(name = "pre_tipo_contratacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreTipoContratacion.findAll", query = "SELECT p FROM PreTipoContratacion p"),
    @NamedQuery(name = "PreTipoContratacion.findByIdePrtpc", query = "SELECT p FROM PreTipoContratacion p WHERE p.idePrtpc = :idePrtpc"),
    @NamedQuery(name = "PreTipoContratacion.findByDetallePrtpc", query = "SELECT p FROM PreTipoContratacion p WHERE p.detallePrtpc = :detallePrtpc"),
    @NamedQuery(name = "PreTipoContratacion.findByActivoPrtpc", query = "SELECT p FROM PreTipoContratacion p WHERE p.activoPrtpc = :activoPrtpc"),
    @NamedQuery(name = "PreTipoContratacion.findByUsuarioIngre", query = "SELECT p FROM PreTipoContratacion p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreTipoContratacion.findByFechaIngre", query = "SELECT p FROM PreTipoContratacion p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreTipoContratacion.findByHoraIngre", query = "SELECT p FROM PreTipoContratacion p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreTipoContratacion.findByUsuarioActua", query = "SELECT p FROM PreTipoContratacion p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreTipoContratacion.findByFechaActua", query = "SELECT p FROM PreTipoContratacion p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreTipoContratacion.findByHoraActua", query = "SELECT p FROM PreTipoContratacion p WHERE p.horaActua = :horaActua")})
public class PreTipoContratacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prtpc", nullable = false)
    private Long idePrtpc;
    @Size(max = 50)
    @Column(name = "detalle_prtpc", length = 50)
    private String detallePrtpc;
    @Column(name = "activo_prtpc")
    private Boolean activoPrtpc;
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
    @OneToMany(mappedBy = "idePrtpc")
    private List<PreContratacionPublica> preContratacionPublicaList;

    public PreTipoContratacion() {
    }

    public PreTipoContratacion(Long idePrtpc) {
        this.idePrtpc = idePrtpc;
    }

    public Long getIdePrtpc() {
        return idePrtpc;
    }

    public void setIdePrtpc(Long idePrtpc) {
        this.idePrtpc = idePrtpc;
    }

    public String getDetallePrtpc() {
        return detallePrtpc;
    }

    public void setDetallePrtpc(String detallePrtpc) {
        this.detallePrtpc = detallePrtpc;
    }

    public Boolean getActivoPrtpc() {
        return activoPrtpc;
    }

    public void setActivoPrtpc(Boolean activoPrtpc) {
        this.activoPrtpc = activoPrtpc;
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

    public List<PreContratacionPublica> getPreContratacionPublicaList() {
        return preContratacionPublicaList;
    }

    public void setPreContratacionPublicaList(List<PreContratacionPublica> preContratacionPublicaList) {
        this.preContratacionPublicaList = preContratacionPublicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrtpc != null ? idePrtpc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreTipoContratacion)) {
            return false;
        }
        PreTipoContratacion other = (PreTipoContratacion) object;
        if ((this.idePrtpc == null && other.idePrtpc != null) || (this.idePrtpc != null && !this.idePrtpc.equals(other.idePrtpc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreTipoContratacion[ idePrtpc=" + idePrtpc + " ]";
    }
    
}
