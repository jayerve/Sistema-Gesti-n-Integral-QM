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
@Table(name = "sbs_tipo_cargo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsTipoCargo.findAll", query = "SELECT s FROM SbsTipoCargo s"),
    @NamedQuery(name = "SbsTipoCargo.findByIdeSbtic", query = "SELECT s FROM SbsTipoCargo s WHERE s.ideSbtic = :ideSbtic"),
    @NamedQuery(name = "SbsTipoCargo.findByDetalleSbtic", query = "SELECT s FROM SbsTipoCargo s WHERE s.detalleSbtic = :detalleSbtic"),
    @NamedQuery(name = "SbsTipoCargo.findByCodigoSbtic", query = "SELECT s FROM SbsTipoCargo s WHERE s.codigoSbtic = :codigoSbtic"),
    @NamedQuery(name = "SbsTipoCargo.findByActivoSbcar", query = "SELECT s FROM SbsTipoCargo s WHERE s.activoSbcar = :activoSbcar"),
    @NamedQuery(name = "SbsTipoCargo.findByUsuarioIngre", query = "SELECT s FROM SbsTipoCargo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsTipoCargo.findByFechaIngre", query = "SELECT s FROM SbsTipoCargo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsTipoCargo.findByHoraIngre", query = "SELECT s FROM SbsTipoCargo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsTipoCargo.findByUsuarioActua", query = "SELECT s FROM SbsTipoCargo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsTipoCargo.findByFechaActua", query = "SELECT s FROM SbsTipoCargo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsTipoCargo.findByHoraActua", query = "SELECT s FROM SbsTipoCargo s WHERE s.horaActua = :horaActua")})
public class SbsTipoCargo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbtic", nullable = false)
    private Integer ideSbtic;
    @Size(max = 50)
    @Column(name = "detalle_sbtic", length = 50)
    private String detalleSbtic;
    @Size(max = 50)
    @Column(name = "codigo_sbtic", length = 50)
    private String codigoSbtic;
    @Column(name = "activo_sbcar")
    private Boolean activoSbcar;
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
    @OneToMany(mappedBy = "ideSbtic")
    private List<SbsArchivoOnce> sbsArchivoOnceList;
    @OneToMany(mappedBy = "ideSbtic")
    private List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList;
    @OneToMany(mappedBy = "ideSbtic")
    private List<SbsCargo> sbsCargoList;

    public SbsTipoCargo() {
    }

    public SbsTipoCargo(Integer ideSbtic) {
        this.ideSbtic = ideSbtic;
    }

    public Integer getIdeSbtic() {
        return ideSbtic;
    }

    public void setIdeSbtic(Integer ideSbtic) {
        this.ideSbtic = ideSbtic;
    }

    public String getDetalleSbtic() {
        return detalleSbtic;
    }

    public void setDetalleSbtic(String detalleSbtic) {
        this.detalleSbtic = detalleSbtic;
    }

    public String getCodigoSbtic() {
        return codigoSbtic;
    }

    public void setCodigoSbtic(String codigoSbtic) {
        this.codigoSbtic = codigoSbtic;
    }

    public Boolean getActivoSbcar() {
        return activoSbcar;
    }

    public void setActivoSbcar(Boolean activoSbcar) {
        this.activoSbcar = activoSbcar;
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

    public List<SbsArchivoVeinteUno> getSbsArchivoVeinteUnoList() {
        return sbsArchivoVeinteUnoList;
    }

    public void setSbsArchivoVeinteUnoList(List<SbsArchivoVeinteUno> sbsArchivoVeinteUnoList) {
        this.sbsArchivoVeinteUnoList = sbsArchivoVeinteUnoList;
    }

    public List<SbsCargo> getSbsCargoList() {
        return sbsCargoList;
    }

    public void setSbsCargoList(List<SbsCargo> sbsCargoList) {
        this.sbsCargoList = sbsCargoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbtic != null ? ideSbtic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsTipoCargo)) {
            return false;
        }
        SbsTipoCargo other = (SbsTipoCargo) object;
        if ((this.ideSbtic == null && other.ideSbtic != null) || (this.ideSbtic != null && !this.ideSbtic.equals(other.ideSbtic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsTipoCargo[ ideSbtic=" + ideSbtic + " ]";
    }
    
}
