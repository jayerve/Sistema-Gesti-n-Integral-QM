/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "sao_detalle_cert_externo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoDetalleCertExterno.findAll", query = "SELECT s FROM SaoDetalleCertExterno s"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByIdeSadce", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.ideSadce = :ideSadce"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByFechaEmisionSadce", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.fechaEmisionSadce = :fechaEmisionSadce"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByObservacionSadce", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.observacionSadce = :observacionSadce"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByArchivoSadce", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.archivoSadce = :archivoSadce"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByActivoSadce", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.activoSadce = :activoSadce"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByUsuarioIngre", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByFechaIngre", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByUsuarioActua", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByFechaActua", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByHoraIngre", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoDetalleCertExterno.findByHoraActua", query = "SELECT s FROM SaoDetalleCertExterno s WHERE s.horaActua = :horaActua")})
public class SaoDetalleCertExterno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sadce", nullable = false)
    private Integer ideSadce;
    @Column(name = "fecha_emision_sadce")
    @Temporal(TemporalType.DATE)
    private Date fechaEmisionSadce;
    @Size(max = 1000)
    @Column(name = "observacion_sadce", length = 1000)
    private String observacionSadce;
    @Size(max = 100)
    @Column(name = "archivo_sadce", length = 100)
    private String archivoSadce;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sadce", nullable = false)
    private boolean activoSadce;
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
    @JoinColumn(name = "ide_sacee", referencedColumnName = "ide_sacee")
    @ManyToOne
    private SaoCertificadoExterno ideSacee;
    @JoinColumn(name = "ide_sacet", referencedColumnName = "ide_sacet")
    @ManyToOne
    private SaoCertifcadoTipo ideSacet;

    public SaoDetalleCertExterno() {
    }

    public SaoDetalleCertExterno(Integer ideSadce) {
        this.ideSadce = ideSadce;
    }

    public SaoDetalleCertExterno(Integer ideSadce, boolean activoSadce) {
        this.ideSadce = ideSadce;
        this.activoSadce = activoSadce;
    }

    public Integer getIdeSadce() {
        return ideSadce;
    }

    public void setIdeSadce(Integer ideSadce) {
        this.ideSadce = ideSadce;
    }

    public Date getFechaEmisionSadce() {
        return fechaEmisionSadce;
    }

    public void setFechaEmisionSadce(Date fechaEmisionSadce) {
        this.fechaEmisionSadce = fechaEmisionSadce;
    }

    public String getObservacionSadce() {
        return observacionSadce;
    }

    public void setObservacionSadce(String observacionSadce) {
        this.observacionSadce = observacionSadce;
    }

    public String getArchivoSadce() {
        return archivoSadce;
    }

    public void setArchivoSadce(String archivoSadce) {
        this.archivoSadce = archivoSadce;
    }

    public boolean getActivoSadce() {
        return activoSadce;
    }

    public void setActivoSadce(boolean activoSadce) {
        this.activoSadce = activoSadce;
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

    public SaoCertificadoExterno getIdeSacee() {
        return ideSacee;
    }

    public void setIdeSacee(SaoCertificadoExterno ideSacee) {
        this.ideSacee = ideSacee;
    }

    public SaoCertifcadoTipo getIdeSacet() {
        return ideSacet;
    }

    public void setIdeSacet(SaoCertifcadoTipo ideSacet) {
        this.ideSacet = ideSacet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSadce != null ? ideSadce.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoDetalleCertExterno)) {
            return false;
        }
        SaoDetalleCertExterno other = (SaoDetalleCertExterno) object;
        if ((this.ideSadce == null && other.ideSadce != null) || (this.ideSadce != null && !this.ideSadce.equals(other.ideSadce))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoDetalleCertExterno[ ideSadce=" + ideSadce + " ]";
    }
    
}
