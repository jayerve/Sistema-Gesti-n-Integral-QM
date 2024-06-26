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
@Table(name = "nrh_tipo_rol", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhTipoRol.findAll", query = "SELECT n FROM NrhTipoRol n"),
    @NamedQuery(name = "NrhTipoRol.findByIdeNrtit", query = "SELECT n FROM NrhTipoRol n WHERE n.ideNrtit = :ideNrtit"),
    @NamedQuery(name = "NrhTipoRol.findByDetalleNrtit", query = "SELECT n FROM NrhTipoRol n WHERE n.detalleNrtit = :detalleNrtit"),
    @NamedQuery(name = "NrhTipoRol.findByNroDiasComercialNrtit", query = "SELECT n FROM NrhTipoRol n WHERE n.nroDiasComercialNrtit = :nroDiasComercialNrtit"),
    @NamedQuery(name = "NrhTipoRol.findByActivoNrtir", query = "SELECT n FROM NrhTipoRol n WHERE n.activoNrtir = :activoNrtir"),
    @NamedQuery(name = "NrhTipoRol.findByUsuarioIngre", query = "SELECT n FROM NrhTipoRol n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhTipoRol.findByFechaIngre", query = "SELECT n FROM NrhTipoRol n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhTipoRol.findByUsuarioActua", query = "SELECT n FROM NrhTipoRol n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhTipoRol.findByFechaActua", query = "SELECT n FROM NrhTipoRol n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhTipoRol.findByHoraIngre", query = "SELECT n FROM NrhTipoRol n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhTipoRol.findByHoraActua", query = "SELECT n FROM NrhTipoRol n WHERE n.horaActua = :horaActua")})
public class NrhTipoRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrtit", nullable = false)
    private Integer ideNrtit;
    @Size(max = 50)
    @Column(name = "detalle_nrtit", length = 50)
    private String detalleNrtit;
    @Column(name = "nro_dias_comercial_nrtit")
    private Integer nroDiasComercialNrtit;
    @Column(name = "activo_nrtir")
    private Boolean activoNrtir;
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
    @OneToMany(mappedBy = "ideNrtit")
    private List<GenPeridoRol> genPeridoRolList;
    @OneToMany(mappedBy = "ideNrtit")
    private List<NrhDetalleTipoNomina> nrhDetalleTipoNominaList;

    public NrhTipoRol() {
    }

    public NrhTipoRol(Integer ideNrtit) {
        this.ideNrtit = ideNrtit;
    }

    public Integer getIdeNrtit() {
        return ideNrtit;
    }

    public void setIdeNrtit(Integer ideNrtit) {
        this.ideNrtit = ideNrtit;
    }

    public String getDetalleNrtit() {
        return detalleNrtit;
    }

    public void setDetalleNrtit(String detalleNrtit) {
        this.detalleNrtit = detalleNrtit;
    }

    public Integer getNroDiasComercialNrtit() {
        return nroDiasComercialNrtit;
    }

    public void setNroDiasComercialNrtit(Integer nroDiasComercialNrtit) {
        this.nroDiasComercialNrtit = nroDiasComercialNrtit;
    }

    public Boolean getActivoNrtir() {
        return activoNrtir;
    }

    public void setActivoNrtir(Boolean activoNrtir) {
        this.activoNrtir = activoNrtir;
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

    public List<GenPeridoRol> getGenPeridoRolList() {
        return genPeridoRolList;
    }

    public void setGenPeridoRolList(List<GenPeridoRol> genPeridoRolList) {
        this.genPeridoRolList = genPeridoRolList;
    }

    public List<NrhDetalleTipoNomina> getNrhDetalleTipoNominaList() {
        return nrhDetalleTipoNominaList;
    }

    public void setNrhDetalleTipoNominaList(List<NrhDetalleTipoNomina> nrhDetalleTipoNominaList) {
        this.nrhDetalleTipoNominaList = nrhDetalleTipoNominaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrtit != null ? ideNrtit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhTipoRol)) {
            return false;
        }
        NrhTipoRol other = (NrhTipoRol) object;
        if ((this.ideNrtit == null && other.ideNrtit != null) || (this.ideNrtit != null && !this.ideNrtit.equals(other.ideNrtit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhTipoRol[ ideNrtit=" + ideNrtit + " ]";
    }
    
}
