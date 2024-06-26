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
@Table(name = "cont_tipo_convenio", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContTipoConvenio.findAll", query = "SELECT c FROM ContTipoConvenio c"),
    @NamedQuery(name = "ContTipoConvenio.findByIdeCotie", query = "SELECT c FROM ContTipoConvenio c WHERE c.ideCotie = :ideCotie"),
    @NamedQuery(name = "ContTipoConvenio.findByDetalleCotie", query = "SELECT c FROM ContTipoConvenio c WHERE c.detalleCotie = :detalleCotie"),
    @NamedQuery(name = "ContTipoConvenio.findByActivoCotie", query = "SELECT c FROM ContTipoConvenio c WHERE c.activoCotie = :activoCotie"),
    @NamedQuery(name = "ContTipoConvenio.findByUsuarioIngre", query = "SELECT c FROM ContTipoConvenio c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContTipoConvenio.findByFechaIngre", query = "SELECT c FROM ContTipoConvenio c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContTipoConvenio.findByHoraIngre", query = "SELECT c FROM ContTipoConvenio c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContTipoConvenio.findByUsuarioActua", query = "SELECT c FROM ContTipoConvenio c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContTipoConvenio.findByFechaActua", query = "SELECT c FROM ContTipoConvenio c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContTipoConvenio.findByHoraActua", query = "SELECT c FROM ContTipoConvenio c WHERE c.horaActua = :horaActua")})
public class ContTipoConvenio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cotie", nullable = false)
    private Long ideCotie;
    @Size(max = 100)
    @Column(name = "detalle_cotie", length = 100)
    private String detalleCotie;
    @Column(name = "activo_cotie")
    private Boolean activoCotie;
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
    @OneToMany(mappedBy = "ideCotie")
    private List<ContConvenio> contConvenioList;

    public ContTipoConvenio() {
    }

    public ContTipoConvenio(Long ideCotie) {
        this.ideCotie = ideCotie;
    }

    public Long getIdeCotie() {
        return ideCotie;
    }

    public void setIdeCotie(Long ideCotie) {
        this.ideCotie = ideCotie;
    }

    public String getDetalleCotie() {
        return detalleCotie;
    }

    public void setDetalleCotie(String detalleCotie) {
        this.detalleCotie = detalleCotie;
    }

    public Boolean getActivoCotie() {
        return activoCotie;
    }

    public void setActivoCotie(Boolean activoCotie) {
        this.activoCotie = activoCotie;
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

    public List<ContConvenio> getContConvenioList() {
        return contConvenioList;
    }

    public void setContConvenioList(List<ContConvenio> contConvenioList) {
        this.contConvenioList = contConvenioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCotie != null ? ideCotie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContTipoConvenio)) {
            return false;
        }
        ContTipoConvenio other = (ContTipoConvenio) object;
        if ((this.ideCotie == null && other.ideCotie != null) || (this.ideCotie != null && !this.ideCotie.equals(other.ideCotie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContTipoConvenio[ ideCotie=" + ideCotie + " ]";
    }
    
}
