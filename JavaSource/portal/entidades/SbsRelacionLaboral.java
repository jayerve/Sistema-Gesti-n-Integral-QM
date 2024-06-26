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
@Table(name = "sbs_relacion_laboral", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsRelacionLaboral.findAll", query = "SELECT s FROM SbsRelacionLaboral s"),
    @NamedQuery(name = "SbsRelacionLaboral.findByIdeSbrel", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.ideSbrel = :ideSbrel"),
    @NamedQuery(name = "SbsRelacionLaboral.findByDetalleSbrel", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.detalleSbrel = :detalleSbrel"),
    @NamedQuery(name = "SbsRelacionLaboral.findByCodigoSbsSbrel", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.codigoSbsSbrel = :codigoSbsSbrel"),
    @NamedQuery(name = "SbsRelacionLaboral.findByActivoSbrel", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.activoSbrel = :activoSbrel"),
    @NamedQuery(name = "SbsRelacionLaboral.findByUsuarioIngre", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsRelacionLaboral.findByFechaIngre", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsRelacionLaboral.findByHoraIngre", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsRelacionLaboral.findByUsuarioActua", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsRelacionLaboral.findByFechaActua", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsRelacionLaboral.findByHoraActua", query = "SELECT s FROM SbsRelacionLaboral s WHERE s.horaActua = :horaActua")})
public class SbsRelacionLaboral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbrel", nullable = false)
    private Integer ideSbrel;
    @Size(max = 50)
    @Column(name = "detalle_sbrel", length = 50)
    private String detalleSbrel;
    @Size(max = 50)
    @Column(name = "codigo_sbs_sbrel", length = 50)
    private String codigoSbsSbrel;
    @Column(name = "activo_sbrel")
    private Boolean activoSbrel;
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
    @OneToMany(mappedBy = "ideSbrel")
    private List<SbsArchivoOnce> sbsArchivoOnceList;
    @OneToMany(mappedBy = "ideSbrel")
    private List<GthTipoContrato> gthTipoContratoList;

    public SbsRelacionLaboral() {
    }

    public SbsRelacionLaboral(Integer ideSbrel) {
        this.ideSbrel = ideSbrel;
    }

    public Integer getIdeSbrel() {
        return ideSbrel;
    }

    public void setIdeSbrel(Integer ideSbrel) {
        this.ideSbrel = ideSbrel;
    }

    public String getDetalleSbrel() {
        return detalleSbrel;
    }

    public void setDetalleSbrel(String detalleSbrel) {
        this.detalleSbrel = detalleSbrel;
    }

    public String getCodigoSbsSbrel() {
        return codigoSbsSbrel;
    }

    public void setCodigoSbsSbrel(String codigoSbsSbrel) {
        this.codigoSbsSbrel = codigoSbsSbrel;
    }

    public Boolean getActivoSbrel() {
        return activoSbrel;
    }

    public void setActivoSbrel(Boolean activoSbrel) {
        this.activoSbrel = activoSbrel;
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

    public List<SbsArchivoOnce> getSbsArchivoOnceList() {
        return sbsArchivoOnceList;
    }

    public void setSbsArchivoOnceList(List<SbsArchivoOnce> sbsArchivoOnceList) {
        this.sbsArchivoOnceList = sbsArchivoOnceList;
    }

    public List<GthTipoContrato> getGthTipoContratoList() {
        return gthTipoContratoList;
    }

    public void setGthTipoContratoList(List<GthTipoContrato> gthTipoContratoList) {
        this.gthTipoContratoList = gthTipoContratoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbrel != null ? ideSbrel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsRelacionLaboral)) {
            return false;
        }
        SbsRelacionLaboral other = (SbsRelacionLaboral) object;
        if ((this.ideSbrel == null && other.ideSbrel != null) || (this.ideSbrel != null && !this.ideSbrel.equals(other.ideSbrel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsRelacionLaboral[ ideSbrel=" + ideSbrel + " ]";
    }
    
}
