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
@Table(name = "crc_prioridad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrcPrioridad.findAll", query = "SELECT c FROM CrcPrioridad c"),
    @NamedQuery(name = "CrcPrioridad.findByIdeCrpri", query = "SELECT c FROM CrcPrioridad c WHERE c.ideCrpri = :ideCrpri"),
    @NamedQuery(name = "CrcPrioridad.findByDetalleCrpri", query = "SELECT c FROM CrcPrioridad c WHERE c.detalleCrpri = :detalleCrpri"),
    @NamedQuery(name = "CrcPrioridad.findByActivoCrpri", query = "SELECT c FROM CrcPrioridad c WHERE c.activoCrpri = :activoCrpri"),
    @NamedQuery(name = "CrcPrioridad.findByUsuarioIngre", query = "SELECT c FROM CrcPrioridad c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrcPrioridad.findByFechaIngre", query = "SELECT c FROM CrcPrioridad c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrcPrioridad.findByHoraIngre", query = "SELECT c FROM CrcPrioridad c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrcPrioridad.findByUsuarioActua", query = "SELECT c FROM CrcPrioridad c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrcPrioridad.findByFechaActua", query = "SELECT c FROM CrcPrioridad c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrcPrioridad.findByHoraActua", query = "SELECT c FROM CrcPrioridad c WHERE c.horaActua = :horaActua")})
public class CrcPrioridad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_crpri", nullable = false)
    private Integer ideCrpri;
    @Size(max = 50)
    @Column(name = "detalle_crpri", length = 50)
    private String detalleCrpri;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_crpri", nullable = false)
    private boolean activoCrpri;
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
    @OneToMany(mappedBy = "ideCrpri")
    private List<CrcDetalleEvento> crcDetalleEventoList;

    public CrcPrioridad() {
    }

    public CrcPrioridad(Integer ideCrpri) {
        this.ideCrpri = ideCrpri;
    }

    public CrcPrioridad(Integer ideCrpri, boolean activoCrpri) {
        this.ideCrpri = ideCrpri;
        this.activoCrpri = activoCrpri;
    }

    public Integer getIdeCrpri() {
        return ideCrpri;
    }

    public void setIdeCrpri(Integer ideCrpri) {
        this.ideCrpri = ideCrpri;
    }

    public String getDetalleCrpri() {
        return detalleCrpri;
    }

    public void setDetalleCrpri(String detalleCrpri) {
        this.detalleCrpri = detalleCrpri;
    }

    public boolean getActivoCrpri() {
        return activoCrpri;
    }

    public void setActivoCrpri(boolean activoCrpri) {
        this.activoCrpri = activoCrpri;
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
        hash += (ideCrpri != null ? ideCrpri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrcPrioridad)) {
            return false;
        }
        CrcPrioridad other = (CrcPrioridad) object;
        if ((this.ideCrpri == null && other.ideCrpri != null) || (this.ideCrpri != null && !this.ideCrpri.equals(other.ideCrpri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrcPrioridad[ ideCrpri=" + ideCrpri + " ]";
    }
    
}
