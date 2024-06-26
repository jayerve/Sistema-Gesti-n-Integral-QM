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
@Table(name = "tes_tipo_proveedor", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesTipoProveedor.findAll", query = "SELECT t FROM TesTipoProveedor t"),
    @NamedQuery(name = "TesTipoProveedor.findByIdeTetpp", query = "SELECT t FROM TesTipoProveedor t WHERE t.ideTetpp = :ideTetpp"),
    @NamedQuery(name = "TesTipoProveedor.findByDetalleTetpp", query = "SELECT t FROM TesTipoProveedor t WHERE t.detalleTetpp = :detalleTetpp"),
    @NamedQuery(name = "TesTipoProveedor.findByActivoTetpp", query = "SELECT t FROM TesTipoProveedor t WHERE t.activoTetpp = :activoTetpp"),
    @NamedQuery(name = "TesTipoProveedor.findByUsuarioIngre", query = "SELECT t FROM TesTipoProveedor t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesTipoProveedor.findByFechaIngre", query = "SELECT t FROM TesTipoProveedor t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesTipoProveedor.findByHoraIngre", query = "SELECT t FROM TesTipoProveedor t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesTipoProveedor.findByUsuarioActua", query = "SELECT t FROM TesTipoProveedor t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesTipoProveedor.findByFechaActua", query = "SELECT t FROM TesTipoProveedor t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesTipoProveedor.findByHoraActua", query = "SELECT t FROM TesTipoProveedor t WHERE t.horaActua = :horaActua")})
public class TesTipoProveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tetpp", nullable = false)
    private Long ideTetpp;
    @Size(max = 100)
    @Column(name = "detalle_tetpp", length = 100)
    private String detalleTetpp;
    @Column(name = "activo_tetpp")
    private Boolean activoTetpp;
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
    @OneToMany(mappedBy = "ideTetpp")
    private List<TesProveedor> tesProveedorList;

    public TesTipoProveedor() {
    }

    public TesTipoProveedor(Long ideTetpp) {
        this.ideTetpp = ideTetpp;
    }

    public Long getIdeTetpp() {
        return ideTetpp;
    }

    public void setIdeTetpp(Long ideTetpp) {
        this.ideTetpp = ideTetpp;
    }

    public String getDetalleTetpp() {
        return detalleTetpp;
    }

    public void setDetalleTetpp(String detalleTetpp) {
        this.detalleTetpp = detalleTetpp;
    }

    public Boolean getActivoTetpp() {
        return activoTetpp;
    }

    public void setActivoTetpp(Boolean activoTetpp) {
        this.activoTetpp = activoTetpp;
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

    public List<TesProveedor> getTesProveedorList() {
        return tesProveedorList;
    }

    public void setTesProveedorList(List<TesProveedor> tesProveedorList) {
        this.tesProveedorList = tesProveedorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTetpp != null ? ideTetpp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesTipoProveedor)) {
            return false;
        }
        TesTipoProveedor other = (TesTipoProveedor) object;
        if ((this.ideTetpp == null && other.ideTetpp != null) || (this.ideTetpp != null && !this.ideTetpp.equals(other.ideTetpp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesTipoProveedor[ ideTetpp=" + ideTetpp + " ]";
    }
    
}
