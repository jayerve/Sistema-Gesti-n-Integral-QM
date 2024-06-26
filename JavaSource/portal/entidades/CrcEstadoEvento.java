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
@Table(name = "crc_estado_evento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrcEstadoEvento.findAll", query = "SELECT c FROM CrcEstadoEvento c"),
    @NamedQuery(name = "CrcEstadoEvento.findByIdeCrese", query = "SELECT c FROM CrcEstadoEvento c WHERE c.ideCrese = :ideCrese"),
    @NamedQuery(name = "CrcEstadoEvento.findByDetalleCrese", query = "SELECT c FROM CrcEstadoEvento c WHERE c.detalleCrese = :detalleCrese"),
    @NamedQuery(name = "CrcEstadoEvento.findByActivoCrese", query = "SELECT c FROM CrcEstadoEvento c WHERE c.activoCrese = :activoCrese"),
    @NamedQuery(name = "CrcEstadoEvento.findByUsuarioIngre", query = "SELECT c FROM CrcEstadoEvento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrcEstadoEvento.findByFechaIngre", query = "SELECT c FROM CrcEstadoEvento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrcEstadoEvento.findByHoraIngre", query = "SELECT c FROM CrcEstadoEvento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrcEstadoEvento.findByUsuarioActua", query = "SELECT c FROM CrcEstadoEvento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrcEstadoEvento.findByFechaActua", query = "SELECT c FROM CrcEstadoEvento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrcEstadoEvento.findByHoraActua", query = "SELECT c FROM CrcEstadoEvento c WHERE c.horaActua = :horaActua")})
public class CrcEstadoEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_crese", nullable = false)
    private Integer ideCrese;
    @Size(max = 50)
    @Column(name = "detalle_crese", length = 50)
    private String detalleCrese;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_crese", nullable = false)
    private boolean activoCrese;
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
    @OneToMany(mappedBy = "ideCrese")
    private List<CrcDetalleEvento> crcDetalleEventoList;

    public CrcEstadoEvento() {
    }

    public CrcEstadoEvento(Integer ideCrese) {
        this.ideCrese = ideCrese;
    }

    public CrcEstadoEvento(Integer ideCrese, boolean activoCrese) {
        this.ideCrese = ideCrese;
        this.activoCrese = activoCrese;
    }

    public Integer getIdeCrese() {
        return ideCrese;
    }

    public void setIdeCrese(Integer ideCrese) {
        this.ideCrese = ideCrese;
    }

    public String getDetalleCrese() {
        return detalleCrese;
    }

    public void setDetalleCrese(String detalleCrese) {
        this.detalleCrese = detalleCrese;
    }

    public boolean getActivoCrese() {
        return activoCrese;
    }

    public void setActivoCrese(boolean activoCrese) {
        this.activoCrese = activoCrese;
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

    public List<CrcDetalleEvento> getCrcDetalleEventoList() {
        return crcDetalleEventoList;
    }

    public void setCrcDetalleEventoList(List<CrcDetalleEvento> crcDetalleEventoList) {
        this.crcDetalleEventoList = crcDetalleEventoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCrese != null ? ideCrese.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrcEstadoEvento)) {
            return false;
        }
        CrcEstadoEvento other = (CrcEstadoEvento) object;
        if ((this.ideCrese == null && other.ideCrese != null) || (this.ideCrese != null && !this.ideCrese.equals(other.ideCrese))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrcEstadoEvento[ ideCrese=" + ideCrese + " ]";
    }
    
}
