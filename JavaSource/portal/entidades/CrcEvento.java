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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "crc_evento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrcEvento.findAll", query = "SELECT c FROM CrcEvento c"),
    @NamedQuery(name = "CrcEvento.findByIdeCreve", query = "SELECT c FROM CrcEvento c WHERE c.ideCreve = :ideCreve"),
    @NamedQuery(name = "CrcEvento.findByDetalleCreve", query = "SELECT c FROM CrcEvento c WHERE c.detalleCreve = :detalleCreve"),
    @NamedQuery(name = "CrcEvento.findByActivoCreve", query = "SELECT c FROM CrcEvento c WHERE c.activoCreve = :activoCreve"),
    @NamedQuery(name = "CrcEvento.findByUsuarioIngre", query = "SELECT c FROM CrcEvento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrcEvento.findByFechaIngre", query = "SELECT c FROM CrcEvento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrcEvento.findByHoraIngre", query = "SELECT c FROM CrcEvento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrcEvento.findByUsuarioActua", query = "SELECT c FROM CrcEvento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrcEvento.findByFechaActua", query = "SELECT c FROM CrcEvento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrcEvento.findByHoraActua", query = "SELECT c FROM CrcEvento c WHERE c.horaActua = :horaActua")})
public class CrcEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_creve", nullable = false)
    private Integer ideCreve;
    @Size(max = 100)
    @Column(name = "detalle_creve", length = 100)
    private String detalleCreve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_creve", nullable = false)
    private boolean activoCreve;
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
    @OneToMany(mappedBy = "ideCreve")
    private List<CrcDetalleEvento> crcDetalleEventoList;
    @JoinColumn(name = "ide_crgrc", referencedColumnName = "ide_crgrc")
    @ManyToOne
    private CrcGrupoCalendario ideCrgrc;

    public CrcEvento() {
    }

    public CrcEvento(Integer ideCreve) {
        this.ideCreve = ideCreve;
    }

    public CrcEvento(Integer ideCreve, boolean activoCreve) {
        this.ideCreve = ideCreve;
        this.activoCreve = activoCreve;
    }

    public Integer getIdeCreve() {
        return ideCreve;
    }

    public void setIdeCreve(Integer ideCreve) {
        this.ideCreve = ideCreve;
    }

    public String getDetalleCreve() {
        return detalleCreve;
    }

    public void setDetalleCreve(String detalleCreve) {
        this.detalleCreve = detalleCreve;
    }

    public boolean getActivoCreve() {
        return activoCreve;
    }

    public void setActivoCreve(boolean activoCreve) {
        this.activoCreve = activoCreve;
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

    public CrcGrupoCalendario getIdeCrgrc() {
        return ideCrgrc;
    }

    public void setIdeCrgrc(CrcGrupoCalendario ideCrgrc) {
        this.ideCrgrc = ideCrgrc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCreve != null ? ideCreve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrcEvento)) {
            return false;
        }
        CrcEvento other = (CrcEvento) object;
        if ((this.ideCreve == null && other.ideCreve != null) || (this.ideCreve != null && !this.ideCreve.equals(other.ideCreve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrcEvento[ ideCreve=" + ideCreve + " ]";
    }
    
}
