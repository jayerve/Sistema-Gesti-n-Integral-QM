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
@Table(name = "pre_fuente_financiamiento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreFuenteFinanciamiento.findAll", query = "SELECT p FROM PreFuenteFinanciamiento p"),
    @NamedQuery(name = "PreFuenteFinanciamiento.findByIdePrfuf", query = "SELECT p FROM PreFuenteFinanciamiento p WHERE p.idePrfuf = :idePrfuf"),
    @NamedQuery(name = "PreFuenteFinanciamiento.findByDetallePrfuf", query = "SELECT p FROM PreFuenteFinanciamiento p WHERE p.detallePrfuf = :detallePrfuf"),
    @NamedQuery(name = "PreFuenteFinanciamiento.findByActivoPrfuf", query = "SELECT p FROM PreFuenteFinanciamiento p WHERE p.activoPrfuf = :activoPrfuf"),
    @NamedQuery(name = "PreFuenteFinanciamiento.findByUsuarioIngre", query = "SELECT p FROM PreFuenteFinanciamiento p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreFuenteFinanciamiento.findByFechaIngre", query = "SELECT p FROM PreFuenteFinanciamiento p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreFuenteFinanciamiento.findByHoraIngre", query = "SELECT p FROM PreFuenteFinanciamiento p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreFuenteFinanciamiento.findByUsuarioActua", query = "SELECT p FROM PreFuenteFinanciamiento p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreFuenteFinanciamiento.findByFechaActua", query = "SELECT p FROM PreFuenteFinanciamiento p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreFuenteFinanciamiento.findByHoraActua", query = "SELECT p FROM PreFuenteFinanciamiento p WHERE p.horaActua = :horaActua")})
public class PreFuenteFinanciamiento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prfuf", nullable = false)
    private Long idePrfuf;
    @Size(max = 100)
    @Column(name = "detalle_prfuf", length = 100)
    private String detallePrfuf;
    @Column(name = "activo_prfuf")
    private Boolean activoPrfuf;
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
    @OneToMany(mappedBy = "idePrfuf")
    private List<PrePoaFinanciamiento> prePoaFinanciamientoList;

    public PreFuenteFinanciamiento() {
    }

    public PreFuenteFinanciamiento(Long idePrfuf) {
        this.idePrfuf = idePrfuf;
    }

    public Long getIdePrfuf() {
        return idePrfuf;
    }

    public void setIdePrfuf(Long idePrfuf) {
        this.idePrfuf = idePrfuf;
    }

    public String getDetallePrfuf() {
        return detallePrfuf;
    }

    public void setDetallePrfuf(String detallePrfuf) {
        this.detallePrfuf = detallePrfuf;
    }

    public Boolean getActivoPrfuf() {
        return activoPrfuf;
    }

    public void setActivoPrfuf(Boolean activoPrfuf) {
        this.activoPrfuf = activoPrfuf;
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

    public List<PrePoaFinanciamiento> getPrePoaFinanciamientoList() {
        return prePoaFinanciamientoList;
    }

    public void setPrePoaFinanciamientoList(List<PrePoaFinanciamiento> prePoaFinanciamientoList) {
        this.prePoaFinanciamientoList = prePoaFinanciamientoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrfuf != null ? idePrfuf.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreFuenteFinanciamiento)) {
            return false;
        }
        PreFuenteFinanciamiento other = (PreFuenteFinanciamiento) object;
        if ((this.idePrfuf == null && other.idePrfuf != null) || (this.idePrfuf != null && !this.idePrfuf.equals(other.idePrfuf))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreFuenteFinanciamiento[ idePrfuf=" + idePrfuf + " ]";
    }
    
}
