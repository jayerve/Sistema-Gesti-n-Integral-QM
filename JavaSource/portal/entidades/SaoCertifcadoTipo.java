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
@Table(name = "sao_certifcado_tipo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoCertifcadoTipo.findAll", query = "SELECT s FROM SaoCertifcadoTipo s"),
    @NamedQuery(name = "SaoCertifcadoTipo.findByIdeSacet", query = "SELECT s FROM SaoCertifcadoTipo s WHERE s.ideSacet = :ideSacet"),
    @NamedQuery(name = "SaoCertifcadoTipo.findByDetalleSacet", query = "SELECT s FROM SaoCertifcadoTipo s WHERE s.detalleSacet = :detalleSacet"),
    @NamedQuery(name = "SaoCertifcadoTipo.findByActivoSacet", query = "SELECT s FROM SaoCertifcadoTipo s WHERE s.activoSacet = :activoSacet"),
    @NamedQuery(name = "SaoCertifcadoTipo.findByUsuarioIngre", query = "SELECT s FROM SaoCertifcadoTipo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoCertifcadoTipo.findByFechaIngre", query = "SELECT s FROM SaoCertifcadoTipo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoCertifcadoTipo.findByUsuarioActua", query = "SELECT s FROM SaoCertifcadoTipo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoCertifcadoTipo.findByFechaActua", query = "SELECT s FROM SaoCertifcadoTipo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoCertifcadoTipo.findByHoraIngre", query = "SELECT s FROM SaoCertifcadoTipo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoCertifcadoTipo.findByHoraActua", query = "SELECT s FROM SaoCertifcadoTipo s WHERE s.horaActua = :horaActua")})
public class SaoCertifcadoTipo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sacet", nullable = false)
    private Integer ideSacet;
    @Size(max = 50)
    @Column(name = "detalle_sacet", length = 50)
    private String detalleSacet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sacet", nullable = false)
    private boolean activoSacet;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideSacet")
    private List<SaoDetalleCertExterno> saoDetalleCertExternoList;

    public SaoCertifcadoTipo() {
    }

    public SaoCertifcadoTipo(Integer ideSacet) {
        this.ideSacet = ideSacet;
    }

    public SaoCertifcadoTipo(Integer ideSacet, boolean activoSacet) {
        this.ideSacet = ideSacet;
        this.activoSacet = activoSacet;
    }

    public Integer getIdeSacet() {
        return ideSacet;
    }

    public void setIdeSacet(Integer ideSacet) {
        this.ideSacet = ideSacet;
    }

    public String getDetalleSacet() {
        return detalleSacet;
    }

    public void setDetalleSacet(String detalleSacet) {
        this.detalleSacet = detalleSacet;
    }

    public boolean getActivoSacet() {
        return activoSacet;
    }

    public void setActivoSacet(boolean activoSacet) {
        this.activoSacet = activoSacet;
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

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public List<SaoDetalleCertExterno> getSaoDetalleCertExternoList() {
        return saoDetalleCertExternoList;
    }

    public void setSaoDetalleCertExternoList(List<SaoDetalleCertExterno> saoDetalleCertExternoList) {
        this.saoDetalleCertExternoList = saoDetalleCertExternoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSacet != null ? ideSacet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoCertifcadoTipo)) {
            return false;
        }
        SaoCertifcadoTipo other = (SaoCertifcadoTipo) object;
        if ((this.ideSacet == null && other.ideSacet != null) || (this.ideSacet != null && !this.ideSacet.equals(other.ideSacet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoCertifcadoTipo[ ideSacet=" + ideSacet + " ]";
    }
    
}
