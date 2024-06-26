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
@Table(name = "crc_detalle_evento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CrcDetalleEvento.findAll", query = "SELECT c FROM CrcDetalleEvento c"),
    @NamedQuery(name = "CrcDetalleEvento.findByIdeCrdee", query = "SELECT c FROM CrcDetalleEvento c WHERE c.ideCrdee = :ideCrdee"),
    @NamedQuery(name = "CrcDetalleEvento.findByDetalleCrdee", query = "SELECT c FROM CrcDetalleEvento c WHERE c.detalleCrdee = :detalleCrdee"),
    @NamedQuery(name = "CrcDetalleEvento.findByFechaInicioCrdee", query = "SELECT c FROM CrcDetalleEvento c WHERE c.fechaInicioCrdee = :fechaInicioCrdee"),
    @NamedQuery(name = "CrcDetalleEvento.findByHoraInicioCrdee", query = "SELECT c FROM CrcDetalleEvento c WHERE c.horaInicioCrdee = :horaInicioCrdee"),
    @NamedQuery(name = "CrcDetalleEvento.findByFechaFinCrdee", query = "SELECT c FROM CrcDetalleEvento c WHERE c.fechaFinCrdee = :fechaFinCrdee"),
    @NamedQuery(name = "CrcDetalleEvento.findByHoraFinCrdee", query = "SELECT c FROM CrcDetalleEvento c WHERE c.horaFinCrdee = :horaFinCrdee"),
    @NamedQuery(name = "CrcDetalleEvento.findByLugarCrdee", query = "SELECT c FROM CrcDetalleEvento c WHERE c.lugarCrdee = :lugarCrdee"),
    @NamedQuery(name = "CrcDetalleEvento.findByActivoCrdee", query = "SELECT c FROM CrcDetalleEvento c WHERE c.activoCrdee = :activoCrdee"),
    @NamedQuery(name = "CrcDetalleEvento.findByUsuarioIngre", query = "SELECT c FROM CrcDetalleEvento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CrcDetalleEvento.findByFechaIngre", query = "SELECT c FROM CrcDetalleEvento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CrcDetalleEvento.findByHoraIngre", query = "SELECT c FROM CrcDetalleEvento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CrcDetalleEvento.findByUsuarioActua", query = "SELECT c FROM CrcDetalleEvento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CrcDetalleEvento.findByFechaActua", query = "SELECT c FROM CrcDetalleEvento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CrcDetalleEvento.findByHoraActua", query = "SELECT c FROM CrcDetalleEvento c WHERE c.horaActua = :horaActua")})
public class CrcDetalleEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_crdee", nullable = false)
    private Integer ideCrdee;
    @Size(max = 100)
    @Column(name = "detalle_crdee", length = 100)
    private String detalleCrdee;
    @Column(name = "fecha_inicio_crdee")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioCrdee;
    @Column(name = "hora_inicio_crdee")
    @Temporal(TemporalType.DATE)
    private Date horaInicioCrdee;
    @Column(name = "fecha_fin_crdee")
    @Temporal(TemporalType.DATE)
    private Date fechaFinCrdee;
    @Column(name = "hora_fin_crdee")
    @Temporal(TemporalType.DATE)
    private Date horaFinCrdee;
    @Size(max = 1000)
    @Column(name = "lugar_crdee", length = 1000)
    private String lugarCrdee;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_crdee", nullable = false)
    private boolean activoCrdee;
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
    @JoinColumn(name = "ide_crtic", referencedColumnName = "ide_crtic")
    @ManyToOne
    private CrcTipoCalendario ideCrtic;
    @JoinColumn(name = "ide_crpri", referencedColumnName = "ide_crpri")
    @ManyToOne
    private CrcPrioridad ideCrpri;
    @JoinColumn(name = "ide_creve", referencedColumnName = "ide_creve")
    @ManyToOne
    private CrcEvento ideCreve;
    @JoinColumn(name = "ide_crese", referencedColumnName = "ide_crese")
    @ManyToOne
    private CrcEstadoEvento ideCrese;
    @OneToMany(mappedBy = "ideCrdee")
    private List<CrcAsistenteEvento> crcAsistenteEventoList;

    public CrcDetalleEvento() {
    }

    public CrcDetalleEvento(Integer ideCrdee) {
        this.ideCrdee = ideCrdee;
    }

    public CrcDetalleEvento(Integer ideCrdee, boolean activoCrdee) {
        this.ideCrdee = ideCrdee;
        this.activoCrdee = activoCrdee;
    }

    public Integer getIdeCrdee() {
        return ideCrdee;
    }

    public void setIdeCrdee(Integer ideCrdee) {
        this.ideCrdee = ideCrdee;
    }

    public String getDetalleCrdee() {
        return detalleCrdee;
    }

    public void setDetalleCrdee(String detalleCrdee) {
        this.detalleCrdee = detalleCrdee;
    }

    public Date getFechaInicioCrdee() {
        return fechaInicioCrdee;
    }

    public void setFechaInicioCrdee(Date fechaInicioCrdee) {
        this.fechaInicioCrdee = fechaInicioCrdee;
    }

    public Date getHoraInicioCrdee() {
        return horaInicioCrdee;
    }

    public void setHoraInicioCrdee(Date horaInicioCrdee) {
        this.horaInicioCrdee = horaInicioCrdee;
    }

    public Date getFechaFinCrdee() {
        return fechaFinCrdee;
    }

    public void setFechaFinCrdee(Date fechaFinCrdee) {
        this.fechaFinCrdee = fechaFinCrdee;
    }

    public Date getHoraFinCrdee() {
        return horaFinCrdee;
    }

    public void setHoraFinCrdee(Date horaFinCrdee) {
        this.horaFinCrdee = horaFinCrdee;
    }

    public String getLugarCrdee() {
        return lugarCrdee;
    }

    public void setLugarCrdee(String lugarCrdee) {
        this.lugarCrdee = lugarCrdee;
    }

    public boolean getActivoCrdee() {
        return activoCrdee;
    }

    public void setActivoCrdee(boolean activoCrdee) {
        this.activoCrdee = activoCrdee;
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

    public CrcTipoCalendario getIdeCrtic() {
        return ideCrtic;
    }

    public void setIdeCrtic(CrcTipoCalendario ideCrtic) {
        this.ideCrtic = ideCrtic;
    }

    public CrcPrioridad getIdeCrpri() {
        return ideCrpri;
    }

    public void setIdeCrpri(CrcPrioridad ideCrpri) {
        this.ideCrpri = ideCrpri;
    }

    public CrcEvento getIdeCreve() {
        return ideCreve;
    }

    public void setIdeCreve(CrcEvento ideCreve) {
        this.ideCreve = ideCreve;
    }

    public CrcEstadoEvento getIdeCrese() {
        return ideCrese;
    }

    public void setIdeCrese(CrcEstadoEvento ideCrese) {
        this.ideCrese = ideCrese;
    }

    public List<CrcAsistenteEvento> getCrcAsistenteEventoList() {
        return crcAsistenteEventoList;
    }

    public void setCrcAsistenteEventoList(List<CrcAsistenteEvento> crcAsistenteEventoList) {
        this.crcAsistenteEventoList = crcAsistenteEventoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCrdee != null ? ideCrdee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CrcDetalleEvento)) {
            return false;
        }
        CrcDetalleEvento other = (CrcDetalleEvento) object;
        if ((this.ideCrdee == null && other.ideCrdee != null) || (this.ideCrdee != null && !this.ideCrdee.equals(other.ideCrdee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CrcDetalleEvento[ ideCrdee=" + ideCrdee + " ]";
    }
    
}
