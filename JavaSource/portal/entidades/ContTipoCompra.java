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
@Table(name = "cont_tipo_compra", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContTipoCompra.findAll", query = "SELECT c FROM ContTipoCompra c"),
    @NamedQuery(name = "ContTipoCompra.findByIdeCotio", query = "SELECT c FROM ContTipoCompra c WHERE c.ideCotio = :ideCotio"),
    @NamedQuery(name = "ContTipoCompra.findByDetalleCotio", query = "SELECT c FROM ContTipoCompra c WHERE c.detalleCotio = :detalleCotio"),
    @NamedQuery(name = "ContTipoCompra.findByActivoCotio", query = "SELECT c FROM ContTipoCompra c WHERE c.activoCotio = :activoCotio"),
    @NamedQuery(name = "ContTipoCompra.findByUsuarioIngre", query = "SELECT c FROM ContTipoCompra c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContTipoCompra.findByFechaIngre", query = "SELECT c FROM ContTipoCompra c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContTipoCompra.findByHoraIngre", query = "SELECT c FROM ContTipoCompra c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContTipoCompra.findByUsuarioActua", query = "SELECT c FROM ContTipoCompra c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContTipoCompra.findByFechaActua", query = "SELECT c FROM ContTipoCompra c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContTipoCompra.findByHoraActua", query = "SELECT c FROM ContTipoCompra c WHERE c.horaActua = :horaActua")})
public class ContTipoCompra implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cotio", nullable = false)
    private Long ideCotio;
    @Size(max = 50)
    @Column(name = "detalle_cotio", length = 50)
    private String detalleCotio;
    @Column(name = "activo_cotio")
    private Boolean activoCotio;
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
    @OneToMany(mappedBy = "ideCotio")
    private List<PrePac> prePacList;

    public ContTipoCompra() {
    }

    public ContTipoCompra(Long ideCotio) {
        this.ideCotio = ideCotio;
    }

    public Long getIdeCotio() {
        return ideCotio;
    }

    public void setIdeCotio(Long ideCotio) {
        this.ideCotio = ideCotio;
    }

    public String getDetalleCotio() {
        return detalleCotio;
    }

    public void setDetalleCotio(String detalleCotio) {
        this.detalleCotio = detalleCotio;
    }

    public Boolean getActivoCotio() {
        return activoCotio;
    }

    public void setActivoCotio(Boolean activoCotio) {
        this.activoCotio = activoCotio;
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

    public List<PrePac> getPrePacList() {
        return prePacList;
    }

    public void setPrePacList(List<PrePac> prePacList) {
        this.prePacList = prePacList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCotio != null ? ideCotio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContTipoCompra)) {
            return false;
        }
        ContTipoCompra other = (ContTipoCompra) object;
        if ((this.ideCotio == null && other.ideCotio != null) || (this.ideCotio != null && !this.ideCotio.equals(other.ideCotio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContTipoCompra[ ideCotio=" + ideCotio + " ]";
    }
    
}
