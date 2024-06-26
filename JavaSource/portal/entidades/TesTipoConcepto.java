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
@Table(name = "tes_tipo_concepto", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesTipoConcepto.findAll", query = "SELECT t FROM TesTipoConcepto t"),
    @NamedQuery(name = "TesTipoConcepto.findByIdeTetic", query = "SELECT t FROM TesTipoConcepto t WHERE t.ideTetic = :ideTetic"),
    @NamedQuery(name = "TesTipoConcepto.findByDetalleTetic", query = "SELECT t FROM TesTipoConcepto t WHERE t.detalleTetic = :detalleTetic"),
    @NamedQuery(name = "TesTipoConcepto.findByFechaPagoTetic", query = "SELECT t FROM TesTipoConcepto t WHERE t.fechaPagoTetic = :fechaPagoTetic"),
    @NamedQuery(name = "TesTipoConcepto.findByActivoTetic", query = "SELECT t FROM TesTipoConcepto t WHERE t.activoTetic = :activoTetic"),
    @NamedQuery(name = "TesTipoConcepto.findByUsuarioIngre", query = "SELECT t FROM TesTipoConcepto t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesTipoConcepto.findByFechaIngre", query = "SELECT t FROM TesTipoConcepto t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesTipoConcepto.findByHoraIngre", query = "SELECT t FROM TesTipoConcepto t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesTipoConcepto.findByUsuarioActua", query = "SELECT t FROM TesTipoConcepto t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesTipoConcepto.findByFechaActua", query = "SELECT t FROM TesTipoConcepto t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesTipoConcepto.findByHoraActua", query = "SELECT t FROM TesTipoConcepto t WHERE t.horaActua = :horaActua")})
public class TesTipoConcepto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tetic", nullable = false)
    private Long ideTetic;
    @Size(max = 2147483647)
    @Column(name = "detalle_tetic", length = 2147483647)
    private String detalleTetic;
    @Column(name = "fecha_pago_tetic")
    @Temporal(TemporalType.DATE)
    private Date fechaPagoTetic;
    @Column(name = "activo_tetic")
    private Boolean activoTetic;
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
    @OneToMany(mappedBy = "ideTetic")
    private List<TesComprobantePago> tesComprobantePagoList;

    public TesTipoConcepto() {
    }

    public TesTipoConcepto(Long ideTetic) {
        this.ideTetic = ideTetic;
    }

    public Long getIdeTetic() {
        return ideTetic;
    }

    public void setIdeTetic(Long ideTetic) {
        this.ideTetic = ideTetic;
    }

    public String getDetalleTetic() {
        return detalleTetic;
    }

    public void setDetalleTetic(String detalleTetic) {
        this.detalleTetic = detalleTetic;
    }

    public Date getFechaPagoTetic() {
        return fechaPagoTetic;
    }

    public void setFechaPagoTetic(Date fechaPagoTetic) {
        this.fechaPagoTetic = fechaPagoTetic;
    }

    public Boolean getActivoTetic() {
        return activoTetic;
    }

    public void setActivoTetic(Boolean activoTetic) {
        this.activoTetic = activoTetic;
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

    public List<TesComprobantePago> getTesComprobantePagoList() {
        return tesComprobantePagoList;
    }

    public void setTesComprobantePagoList(List<TesComprobantePago> tesComprobantePagoList) {
        this.tesComprobantePagoList = tesComprobantePagoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTetic != null ? ideTetic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesTipoConcepto)) {
            return false;
        }
        TesTipoConcepto other = (TesTipoConcepto) object;
        if ((this.ideTetic == null && other.ideTetic != null) || (this.ideTetic != null && !this.ideTetic.equals(other.ideTetic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesTipoConcepto[ ideTetic=" + ideTetic + " ]";
    }
    
}
