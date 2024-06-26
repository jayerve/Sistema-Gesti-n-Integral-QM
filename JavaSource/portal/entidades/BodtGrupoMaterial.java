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
@Table(name = "bodt_grupo_material", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtGrupoMaterial.findAll", query = "SELECT b FROM BodtGrupoMaterial b"),
    @NamedQuery(name = "BodtGrupoMaterial.findByIdeBogrm", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.ideBogrm = :ideBogrm"),
    @NamedQuery(name = "BodtGrupoMaterial.findByDetalleBogrm", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.detalleBogrm = :detalleBogrm"),
    @NamedQuery(name = "BodtGrupoMaterial.findByActivoBogrm", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.activoBogrm = :activoBogrm"),
    @NamedQuery(name = "BodtGrupoMaterial.findByUsuarioIngre", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtGrupoMaterial.findByFechaIngre", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtGrupoMaterial.findByHoraIngre", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtGrupoMaterial.findByUsuarioActua", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtGrupoMaterial.findByFechaActua", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtGrupoMaterial.findByHoraActua", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.horaActua = :horaActua"),
    @NamedQuery(name = "BodtGrupoMaterial.findByAutorizacionSriBogrm", query = "SELECT b FROM BodtGrupoMaterial b WHERE b.autorizacionSriBogrm = :autorizacionSriBogrm")})
public class BodtGrupoMaterial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_bogrm", nullable = false)
    private Long ideBogrm;
    @Size(max = 50)
    @Column(name = "detalle_bogrm", length = 50)
    private String detalleBogrm;
    @Column(name = "activo_bogrm")
    private Boolean activoBogrm;
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
    @Size(max = 20)
    @Column(name = "autorizacion_sri_bogrm", length = 20)
    private String autorizacionSriBogrm;
    @OneToMany(mappedBy = "ideBogrm")
    private List<BodtMaterial> bodtMaterialList;
    @OneToMany(mappedBy = "ideBogrm")
    private List<FacDatosFactura> facDatosFacturaList;
    @OneToMany(mappedBy = "ideBogrm")
    private List<FacVentaLugar> facVentaLugarList;

    public BodtGrupoMaterial() {
    }

    public BodtGrupoMaterial(Long ideBogrm) {
        this.ideBogrm = ideBogrm;
    }

    public Long getIdeBogrm() {
        return ideBogrm;
    }

    public void setIdeBogrm(Long ideBogrm) {
        this.ideBogrm = ideBogrm;
    }

    public String getDetalleBogrm() {
        return detalleBogrm;
    }

    public void setDetalleBogrm(String detalleBogrm) {
        this.detalleBogrm = detalleBogrm;
    }

    public Boolean getActivoBogrm() {
        return activoBogrm;
    }

    public void setActivoBogrm(Boolean activoBogrm) {
        this.activoBogrm = activoBogrm;
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

    public String getAutorizacionSriBogrm() {
        return autorizacionSriBogrm;
    }

    public void setAutorizacionSriBogrm(String autorizacionSriBogrm) {
        this.autorizacionSriBogrm = autorizacionSriBogrm;
    }

    public List<BodtMaterial> getBodtMaterialList() {
        return bodtMaterialList;
    }

    public void setBodtMaterialList(List<BodtMaterial> bodtMaterialList) {
        this.bodtMaterialList = bodtMaterialList;
    }

    public List<FacDatosFactura> getFacDatosFacturaList() {
        return facDatosFacturaList;
    }

    public void setFacDatosFacturaList(List<FacDatosFactura> facDatosFacturaList) {
        this.facDatosFacturaList = facDatosFacturaList;
    }

    public List<FacVentaLugar> getFacVentaLugarList() {
        return facVentaLugarList;
    }

    public void setFacVentaLugarList(List<FacVentaLugar> facVentaLugarList) {
        this.facVentaLugarList = facVentaLugarList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBogrm != null ? ideBogrm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtGrupoMaterial)) {
            return false;
        }
        BodtGrupoMaterial other = (BodtGrupoMaterial) object;
        if ((this.ideBogrm == null && other.ideBogrm != null) || (this.ideBogrm != null && !this.ideBogrm.equals(other.ideBogrm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtGrupoMaterial[ ideBogrm=" + ideBogrm + " ]";
    }
    
}
