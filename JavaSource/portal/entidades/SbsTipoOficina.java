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
@Table(name = "sbs_tipo_oficina", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsTipoOficina.findAll", query = "SELECT s FROM SbsTipoOficina s"),
    @NamedQuery(name = "SbsTipoOficina.findByIdeSbtio", query = "SELECT s FROM SbsTipoOficina s WHERE s.ideSbtio = :ideSbtio"),
    @NamedQuery(name = "SbsTipoOficina.findByDetalleSbtio", query = "SELECT s FROM SbsTipoOficina s WHERE s.detalleSbtio = :detalleSbtio"),
    @NamedQuery(name = "SbsTipoOficina.findByActivoSbtio", query = "SELECT s FROM SbsTipoOficina s WHERE s.activoSbtio = :activoSbtio"),
    @NamedQuery(name = "SbsTipoOficina.findByUsuarioIngre", query = "SELECT s FROM SbsTipoOficina s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsTipoOficina.findByFechaIngre", query = "SELECT s FROM SbsTipoOficina s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsTipoOficina.findByHoraIngre", query = "SELECT s FROM SbsTipoOficina s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsTipoOficina.findByUsuarioActua", query = "SELECT s FROM SbsTipoOficina s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsTipoOficina.findByFechaActua", query = "SELECT s FROM SbsTipoOficina s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsTipoOficina.findByHoraActua", query = "SELECT s FROM SbsTipoOficina s WHERE s.horaActua = :horaActua")})
public class SbsTipoOficina implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sbtio", nullable = false)
    private Integer ideSbtio;
    @Size(max = 50)
    @Column(name = "detalle_sbtio", length = 50)
    private String detalleSbtio;
    @Column(name = "activo_sbtio")
    private Boolean activoSbtio;
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
    @OneToMany(mappedBy = "ideSbtio")
    private List<SbsOficina> sbsOficinaList;

    public SbsTipoOficina() {
    }

    public SbsTipoOficina(Integer ideSbtio) {
        this.ideSbtio = ideSbtio;
    }

    public Integer getIdeSbtio() {
        return ideSbtio;
    }

    public void setIdeSbtio(Integer ideSbtio) {
        this.ideSbtio = ideSbtio;
    }

    public String getDetalleSbtio() {
        return detalleSbtio;
    }

    public void setDetalleSbtio(String detalleSbtio) {
        this.detalleSbtio = detalleSbtio;
    }

    public Boolean getActivoSbtio() {
        return activoSbtio;
    }

    public void setActivoSbtio(Boolean activoSbtio) {
        this.activoSbtio = activoSbtio;
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

    public List<SbsOficina> getSbsOficinaList() {
        return sbsOficinaList;
    }

    public void setSbsOficinaList(List<SbsOficina> sbsOficinaList) {
        this.sbsOficinaList = sbsOficinaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSbtio != null ? ideSbtio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsTipoOficina)) {
            return false;
        }
        SbsTipoOficina other = (SbsTipoOficina) object;
        if ((this.ideSbtio == null && other.ideSbtio != null) || (this.ideSbtio != null && !this.ideSbtio.equals(other.ideSbtio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsTipoOficina[ ideSbtio=" + ideSbtio + " ]";
    }
    
}
