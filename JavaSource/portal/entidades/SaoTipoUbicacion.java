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
@Table(name = "sao_tipo_ubicacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoTipoUbicacion.findAll", query = "SELECT s FROM SaoTipoUbicacion s"),
    @NamedQuery(name = "SaoTipoUbicacion.findByIdeSatiu", query = "SELECT s FROM SaoTipoUbicacion s WHERE s.ideSatiu = :ideSatiu"),
    @NamedQuery(name = "SaoTipoUbicacion.findByDetalleSatiu", query = "SELECT s FROM SaoTipoUbicacion s WHERE s.detalleSatiu = :detalleSatiu"),
    @NamedQuery(name = "SaoTipoUbicacion.findByActivoSatiu", query = "SELECT s FROM SaoTipoUbicacion s WHERE s.activoSatiu = :activoSatiu"),
    @NamedQuery(name = "SaoTipoUbicacion.findByUsuarioIngre", query = "SELECT s FROM SaoTipoUbicacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoTipoUbicacion.findByFechaIngre", query = "SELECT s FROM SaoTipoUbicacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoTipoUbicacion.findByHoraIngre", query = "SELECT s FROM SaoTipoUbicacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoTipoUbicacion.findByUsuarioActua", query = "SELECT s FROM SaoTipoUbicacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoTipoUbicacion.findByFechaActua", query = "SELECT s FROM SaoTipoUbicacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoTipoUbicacion.findByHoraActua", query = "SELECT s FROM SaoTipoUbicacion s WHERE s.horaActua = :horaActua")})
public class SaoTipoUbicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_satiu", nullable = false)
    private Integer ideSatiu;
    @Size(max = 100)
    @Column(name = "detalle_satiu", length = 100)
    private String detalleSatiu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_satiu", nullable = false)
    private boolean activoSatiu;
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
    @OneToMany(mappedBy = "ideSatiu")
    private List<SaoUbicacion> saoUbicacionList;

    public SaoTipoUbicacion() {
    }

    public SaoTipoUbicacion(Integer ideSatiu) {
        this.ideSatiu = ideSatiu;
    }

    public SaoTipoUbicacion(Integer ideSatiu, boolean activoSatiu) {
        this.ideSatiu = ideSatiu;
        this.activoSatiu = activoSatiu;
    }

    public Integer getIdeSatiu() {
        return ideSatiu;
    }

    public void setIdeSatiu(Integer ideSatiu) {
        this.ideSatiu = ideSatiu;
    }

    public String getDetalleSatiu() {
        return detalleSatiu;
    }

    public void setDetalleSatiu(String detalleSatiu) {
        this.detalleSatiu = detalleSatiu;
    }

    public boolean getActivoSatiu() {
        return activoSatiu;
    }

    public void setActivoSatiu(boolean activoSatiu) {
        this.activoSatiu = activoSatiu;
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

    public List<SaoUbicacion> getSaoUbicacionList() {
        return saoUbicacionList;
    }

    public void setSaoUbicacionList(List<SaoUbicacion> saoUbicacionList) {
        this.saoUbicacionList = saoUbicacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSatiu != null ? ideSatiu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoTipoUbicacion)) {
            return false;
        }
        SaoTipoUbicacion other = (SaoTipoUbicacion) object;
        if ((this.ideSatiu == null && other.ideSatiu != null) || (this.ideSatiu != null && !this.ideSatiu.equals(other.ideSatiu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoTipoUbicacion[ ideSatiu=" + ideSatiu + " ]";
    }
    
}
