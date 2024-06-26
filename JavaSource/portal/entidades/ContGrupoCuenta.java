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
@Table(name = "cont_grupo_cuenta", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContGrupoCuenta.findAll", query = "SELECT c FROM ContGrupoCuenta c"),
    @NamedQuery(name = "ContGrupoCuenta.findByIdeCogrc", query = "SELECT c FROM ContGrupoCuenta c WHERE c.ideCogrc = :ideCogrc"),
    @NamedQuery(name = "ContGrupoCuenta.findByDetalleCogrc", query = "SELECT c FROM ContGrupoCuenta c WHERE c.detalleCogrc = :detalleCogrc"),
    @NamedQuery(name = "ContGrupoCuenta.findByActivoCogrc", query = "SELECT c FROM ContGrupoCuenta c WHERE c.activoCogrc = :activoCogrc"),
    @NamedQuery(name = "ContGrupoCuenta.findByUsuarioIngre", query = "SELECT c FROM ContGrupoCuenta c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContGrupoCuenta.findByFechaIngre", query = "SELECT c FROM ContGrupoCuenta c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContGrupoCuenta.findByHoraIngre", query = "SELECT c FROM ContGrupoCuenta c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContGrupoCuenta.findByUsuarioActua", query = "SELECT c FROM ContGrupoCuenta c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContGrupoCuenta.findByFechaActua", query = "SELECT c FROM ContGrupoCuenta c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContGrupoCuenta.findByHoraActua", query = "SELECT c FROM ContGrupoCuenta c WHERE c.horaActua = :horaActua")})
public class ContGrupoCuenta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cogrc", nullable = false)
    private Long ideCogrc;
    @Size(max = 100)
    @Column(name = "detalle_cogrc", length = 100)
    private String detalleCogrc;
    @Column(name = "activo_cogrc")
    private Boolean activoCogrc;
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
    @OneToMany(mappedBy = "ideCogrc")
    private List<ContCatalogoCuenta> contCatalogoCuentaList;

    public ContGrupoCuenta() {
    }

    public ContGrupoCuenta(Long ideCogrc) {
        this.ideCogrc = ideCogrc;
    }

    public Long getIdeCogrc() {
        return ideCogrc;
    }

    public void setIdeCogrc(Long ideCogrc) {
        this.ideCogrc = ideCogrc;
    }

    public String getDetalleCogrc() {
        return detalleCogrc;
    }

    public void setDetalleCogrc(String detalleCogrc) {
        this.detalleCogrc = detalleCogrc;
    }

    public Boolean getActivoCogrc() {
        return activoCogrc;
    }

    public void setActivoCogrc(Boolean activoCogrc) {
        this.activoCogrc = activoCogrc;
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

    public List<ContCatalogoCuenta> getContCatalogoCuentaList() {
        return contCatalogoCuentaList;
    }

    public void setContCatalogoCuentaList(List<ContCatalogoCuenta> contCatalogoCuentaList) {
        this.contCatalogoCuentaList = contCatalogoCuentaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCogrc != null ? ideCogrc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContGrupoCuenta)) {
            return false;
        }
        ContGrupoCuenta other = (ContGrupoCuenta) object;
        if ((this.ideCogrc == null && other.ideCogrc != null) || (this.ideCogrc != null && !this.ideCogrc.equals(other.ideCogrc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContGrupoCuenta[ ideCogrc=" + ideCogrc + " ]";
    }
    
}
