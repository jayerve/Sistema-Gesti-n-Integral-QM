/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "gth_tipo_empleado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoEmpleado.findAll", query = "SELECT g FROM GthTipoEmpleado g"),
    @NamedQuery(name = "GthTipoEmpleado.findByIdeGttem", query = "SELECT g FROM GthTipoEmpleado g WHERE g.ideGttem = :ideGttem"),
    @NamedQuery(name = "GthTipoEmpleado.findByDetalleGttem", query = "SELECT g FROM GthTipoEmpleado g WHERE g.detalleGttem = :detalleGttem"),
    @NamedQuery(name = "GthTipoEmpleado.findByProcesoCoreGttem", query = "SELECT g FROM GthTipoEmpleado g WHERE g.procesoCoreGttem = :procesoCoreGttem"),
    @NamedQuery(name = "GthTipoEmpleado.findByActivoGttem", query = "SELECT g FROM GthTipoEmpleado g WHERE g.activoGttem = :activoGttem"),
    @NamedQuery(name = "GthTipoEmpleado.findByUsuarioIngre", query = "SELECT g FROM GthTipoEmpleado g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoEmpleado.findByFechaIngre", query = "SELECT g FROM GthTipoEmpleado g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoEmpleado.findByUsuarioActua", query = "SELECT g FROM GthTipoEmpleado g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoEmpleado.findByFechaActua", query = "SELECT g FROM GthTipoEmpleado g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoEmpleado.findByHoraIngre", query = "SELECT g FROM GthTipoEmpleado g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoEmpleado.findByHoraActua", query = "SELECT g FROM GthTipoEmpleado g WHERE g.horaActua = :horaActua")})
public class GthTipoEmpleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttem", nullable = false)
    private Integer ideGttem;
    @Size(max = 50)
    @Column(name = "detalle_gttem", length = 50)
    private String detalleGttem;
    @Size(max = 50)
    @Column(name = "proceso_core_gttem", length = 50)
    private String procesoCoreGttem;
    @Column(name = "activo_gttem")
    private Boolean activoGttem;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttem")
    private List<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParList;
    @OneToMany(mappedBy = "ideGttem")
    private List<NrhDetalleTipoNomina> nrhDetalleTipoNominaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttem")
    private List<GenPartidaGrupoCargo> genPartidaGrupoCargoList;

    public GthTipoEmpleado() {
    }

    public GthTipoEmpleado(Integer ideGttem) {
        this.ideGttem = ideGttem;
    }

    public Integer getIdeGttem() {
        return ideGttem;
    }

    public void setIdeGttem(Integer ideGttem) {
        this.ideGttem = ideGttem;
    }

    public String getDetalleGttem() {
        return detalleGttem;
    }

    public void setDetalleGttem(String detalleGttem) {
        this.detalleGttem = detalleGttem;
    }

    public String getProcesoCoreGttem() {
        return procesoCoreGttem;
    }

    public void setProcesoCoreGttem(String procesoCoreGttem) {
        this.procesoCoreGttem = procesoCoreGttem;
    }

    public Boolean getActivoGttem() {
        return activoGttem;
    }

    public void setActivoGttem(Boolean activoGttem) {
        this.activoGttem = activoGttem;
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

    public List<GenEmpleadosDepartamentoPar> getGenEmpleadosDepartamentoParList() {
        return genEmpleadosDepartamentoParList;
    }

    public void setGenEmpleadosDepartamentoParList(List<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParList) {
        this.genEmpleadosDepartamentoParList = genEmpleadosDepartamentoParList;
    }

    public List<NrhDetalleTipoNomina> getNrhDetalleTipoNominaList() {
        return nrhDetalleTipoNominaList;
    }

    public void setNrhDetalleTipoNominaList(List<NrhDetalleTipoNomina> nrhDetalleTipoNominaList) {
        this.nrhDetalleTipoNominaList = nrhDetalleTipoNominaList;
    }

    public List<GenPartidaGrupoCargo> getGenPartidaGrupoCargoList() {
        return genPartidaGrupoCargoList;
    }

    public void setGenPartidaGrupoCargoList(List<GenPartidaGrupoCargo> genPartidaGrupoCargoList) {
        this.genPartidaGrupoCargoList = genPartidaGrupoCargoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttem != null ? ideGttem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoEmpleado)) {
            return false;
        }
        GthTipoEmpleado other = (GthTipoEmpleado) object;
        if ((this.ideGttem == null && other.ideGttem != null) || (this.ideGttem != null && !this.ideGttem.equals(other.ideGttem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoEmpleado[ ideGttem=" + ideGttem + " ]";
    }
    
}
