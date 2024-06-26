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
@Table(name = "sao_ubicacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoUbicacion.findAll", query = "SELECT s FROM SaoUbicacion s"),
    @NamedQuery(name = "SaoUbicacion.findByIdeSaubi", query = "SELECT s FROM SaoUbicacion s WHERE s.ideSaubi = :ideSaubi"),
    @NamedQuery(name = "SaoUbicacion.findByDetalleSaubi", query = "SELECT s FROM SaoUbicacion s WHERE s.detalleSaubi = :detalleSaubi"),
    @NamedQuery(name = "SaoUbicacion.findByActivoSaubi", query = "SELECT s FROM SaoUbicacion s WHERE s.activoSaubi = :activoSaubi"),
    @NamedQuery(name = "SaoUbicacion.findByUsuarioIngre", query = "SELECT s FROM SaoUbicacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoUbicacion.findByFechaIngre", query = "SELECT s FROM SaoUbicacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoUbicacion.findByHoraIngre", query = "SELECT s FROM SaoUbicacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoUbicacion.findByUsuarioActua", query = "SELECT s FROM SaoUbicacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoUbicacion.findByFechaActua", query = "SELECT s FROM SaoUbicacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoUbicacion.findByHoraActua", query = "SELECT s FROM SaoUbicacion s WHERE s.horaActua = :horaActua")})
public class SaoUbicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_saubi", nullable = false)
    private Integer ideSaubi;
    @Size(max = 100)
    @Column(name = "detalle_saubi", length = 100)
    private String detalleSaubi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_saubi", nullable = false)
    private boolean activoSaubi;
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
    @OneToMany(mappedBy = "ideSaubi")
    private List<SaoUbicacionSucursal> saoUbicacionSucursalList;
    @OneToMany(mappedBy = "saoIdeSaubi")
    private List<SaoUbicacion> saoUbicacionList;
    @JoinColumn(name = "sao_ide_saubi", referencedColumnName = "ide_saubi")
    @ManyToOne
    private SaoUbicacion saoIdeSaubi;
    @JoinColumn(name = "ide_satiu", referencedColumnName = "ide_satiu")
    @ManyToOne
    private SaoTipoUbicacion ideSatiu;

    public SaoUbicacion() {
    }

    public SaoUbicacion(Integer ideSaubi) {
        this.ideSaubi = ideSaubi;
    }

    public SaoUbicacion(Integer ideSaubi, boolean activoSaubi) {
        this.ideSaubi = ideSaubi;
        this.activoSaubi = activoSaubi;
    }

    public Integer getIdeSaubi() {
        return ideSaubi;
    }

    public void setIdeSaubi(Integer ideSaubi) {
        this.ideSaubi = ideSaubi;
    }

    public String getDetalleSaubi() {
        return detalleSaubi;
    }

    public void setDetalleSaubi(String detalleSaubi) {
        this.detalleSaubi = detalleSaubi;
    }

    public boolean getActivoSaubi() {
        return activoSaubi;
    }

    public void setActivoSaubi(boolean activoSaubi) {
        this.activoSaubi = activoSaubi;
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

    public List<SaoUbicacionSucursal> getSaoUbicacionSucursalList() {
        return saoUbicacionSucursalList;
    }

    public void setSaoUbicacionSucursalList(List<SaoUbicacionSucursal> saoUbicacionSucursalList) {
        this.saoUbicacionSucursalList = saoUbicacionSucursalList;
    }

    public List<SaoUbicacion> getSaoUbicacionList() {
        return saoUbicacionList;
    }

    public void setSaoUbicacionList(List<SaoUbicacion> saoUbicacionList) {
        this.saoUbicacionList = saoUbicacionList;
    }

    public SaoUbicacion getSaoIdeSaubi() {
        return saoIdeSaubi;
    }

    public void setSaoIdeSaubi(SaoUbicacion saoIdeSaubi) {
        this.saoIdeSaubi = saoIdeSaubi;
    }

    public SaoTipoUbicacion getIdeSatiu() {
        return ideSatiu;
    }

    public void setIdeSatiu(SaoTipoUbicacion ideSatiu) {
        this.ideSatiu = ideSatiu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSaubi != null ? ideSaubi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoUbicacion)) {
            return false;
        }
        SaoUbicacion other = (SaoUbicacion) object;
        if ((this.ideSaubi == null && other.ideSaubi != null) || (this.ideSaubi != null && !this.ideSaubi.equals(other.ideSaubi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoUbicacion[ ideSaubi=" + ideSaubi + " ]";
    }
    
}
