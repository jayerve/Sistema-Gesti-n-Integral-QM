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
@Table(name = "pre_sub_actividad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreSubActividad.findAll", query = "SELECT p FROM PreSubActividad p"),
    @NamedQuery(name = "PreSubActividad.findByIdePrsua", query = "SELECT p FROM PreSubActividad p WHERE p.idePrsua = :idePrsua"),
    @NamedQuery(name = "PreSubActividad.findByDetallePrsua", query = "SELECT p FROM PreSubActividad p WHERE p.detallePrsua = :detallePrsua"),
    @NamedQuery(name = "PreSubActividad.findByActivoPrsua", query = "SELECT p FROM PreSubActividad p WHERE p.activoPrsua = :activoPrsua"),
    @NamedQuery(name = "PreSubActividad.findByUsuarioIngre", query = "SELECT p FROM PreSubActividad p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreSubActividad.findByFechaIngre", query = "SELECT p FROM PreSubActividad p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreSubActividad.findByHoraIngre", query = "SELECT p FROM PreSubActividad p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreSubActividad.findByUsuarioActua", query = "SELECT p FROM PreSubActividad p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreSubActividad.findByFechaActua", query = "SELECT p FROM PreSubActividad p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreSubActividad.findByHoraActua", query = "SELECT p FROM PreSubActividad p WHERE p.horaActua = :horaActua")})
public class PreSubActividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prsua", nullable = false)
    private Long idePrsua;
    @Size(max = 50)
    @Column(name = "detalle_prsua", length = 50)
    private String detallePrsua;
    @Column(name = "activo_prsua")
    private Boolean activoPrsua;
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
    @OneToMany(mappedBy = "idePrsua")
    private List<PrePoa> prePoaList;

    public PreSubActividad() {
    }

    public PreSubActividad(Long idePrsua) {
        this.idePrsua = idePrsua;
    }

    public Long getIdePrsua() {
        return idePrsua;
    }

    public void setIdePrsua(Long idePrsua) {
        this.idePrsua = idePrsua;
    }

    public String getDetallePrsua() {
        return detallePrsua;
    }

    public void setDetallePrsua(String detallePrsua) {
        this.detallePrsua = detallePrsua;
    }

    public Boolean getActivoPrsua() {
        return activoPrsua;
    }

    public void setActivoPrsua(Boolean activoPrsua) {
        this.activoPrsua = activoPrsua;
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

    public List<PrePoa> getPrePoaList() {
        return prePoaList;
    }

    public void setPrePoaList(List<PrePoa> prePoaList) {
        this.prePoaList = prePoaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrsua != null ? idePrsua.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreSubActividad)) {
            return false;
        }
        PreSubActividad other = (PreSubActividad) object;
        if ((this.idePrsua == null && other.idePrsua != null) || (this.idePrsua != null && !this.idePrsua.equals(other.idePrsua))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreSubActividad[ idePrsua=" + idePrsua + " ]";
    }
    
}
