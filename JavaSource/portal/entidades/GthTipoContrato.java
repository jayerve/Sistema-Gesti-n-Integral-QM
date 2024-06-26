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
@Table(name = "gth_tipo_contrato", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthTipoContrato.findAll", query = "SELECT g FROM GthTipoContrato g"),
    @NamedQuery(name = "GthTipoContrato.findByIdeGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.ideGttco = :ideGttco"),
    @NamedQuery(name = "GthTipoContrato.findByDetalleGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.detalleGttco = :detalleGttco"),
    @NamedQuery(name = "GthTipoContrato.findByDiaFincGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.diaFincGttco = :diaFincGttco"),
    @NamedQuery(name = "GthTipoContrato.findByActivoGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.activoGttco = :activoGttco"),
    @NamedQuery(name = "GthTipoContrato.findByUsuarioIngre", query = "SELECT g FROM GthTipoContrato g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthTipoContrato.findByFechaIngre", query = "SELECT g FROM GthTipoContrato g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthTipoContrato.findByUsuarioActua", query = "SELECT g FROM GthTipoContrato g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthTipoContrato.findByFechaActua", query = "SELECT g FROM GthTipoContrato g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthTipoContrato.findByHoraIngre", query = "SELECT g FROM GthTipoContrato g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthTipoContrato.findByHoraActua", query = "SELECT g FROM GthTipoContrato g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GthTipoContrato.findByAnticipoGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.anticipoGttco = :anticipoGttco"),
    @NamedQuery(name = "GthTipoContrato.findByGaranteGttco", query = "SELECT g FROM GthTipoContrato g WHERE g.garanteGttco = :garanteGttco")})
public class GthTipoContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gttco", nullable = false)
    private Integer ideGttco;
    @Size(max = 50)
    @Column(name = "detalle_gttco", length = 50)
    private String detalleGttco;
    @Column(name = "dia_finc_gttco")
    private Integer diaFincGttco;
    @Column(name = "activo_gttco")
    private Boolean activoGttco;
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
    @Column(name = "anticipo_gttco")
    private Boolean anticipoGttco;
    @Column(name = "garante_gttco")
    private Boolean garanteGttco;
    @JoinColumn(name = "ide_sbrel", referencedColumnName = "ide_sbrel")
    @ManyToOne
    private SbsRelacionLaboral ideSbrel;
    @JoinColumn(name = "ide_gecae", referencedColumnName = "ide_gecae")
    @ManyToOne
    private GenCategoriaEstatus ideGecae;
    @OneToMany(mappedBy = "ideGttco")
    private List<SprCargoContratacion> sprCargoContratacionList;
    @OneToMany(mappedBy = "ideGttco")
    private List<NrhCondicionAnticipo> nrhCondicionAnticipoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGttco")
    private List<GenEmpleadosDepartamentoPar> genEmpleadosDepartamentoParList;
    @OneToMany(mappedBy = "ideGttco")
    private List<NrhDetalleTipoNomina> nrhDetalleTipoNominaList;
    @OneToMany(mappedBy = "ideGttco")
    private List<NrhRolDetalleEscenario> nrhRolDetalleEscenarioList;

    public GthTipoContrato() {
    }

    public GthTipoContrato(Integer ideGttco) {
        this.ideGttco = ideGttco;
    }

    public Integer getIdeGttco() {
        return ideGttco;
    }

    public void setIdeGttco(Integer ideGttco) {
        this.ideGttco = ideGttco;
    }

    public String getDetalleGttco() {
        return detalleGttco;
    }

    public void setDetalleGttco(String detalleGttco) {
        this.detalleGttco = detalleGttco;
    }

    public Integer getDiaFincGttco() {
        return diaFincGttco;
    }

    public void setDiaFincGttco(Integer diaFincGttco) {
        this.diaFincGttco = diaFincGttco;
    }

    public Boolean getActivoGttco() {
        return activoGttco;
    }

    public void setActivoGttco(Boolean activoGttco) {
        this.activoGttco = activoGttco;
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

    public Boolean getAnticipoGttco() {
        return anticipoGttco;
    }

    public void setAnticipoGttco(Boolean anticipoGttco) {
        this.anticipoGttco = anticipoGttco;
    }

    public Boolean getGaranteGttco() {
        return garanteGttco;
    }

    public void setGaranteGttco(Boolean garanteGttco) {
        this.garanteGttco = garanteGttco;
    }

    public SbsRelacionLaboral getIdeSbrel() {
        return ideSbrel;
    }

    public void setIdeSbrel(SbsRelacionLaboral ideSbrel) {
        this.ideSbrel = ideSbrel;
    }

    public GenCategoriaEstatus getIdeGecae() {
        return ideGecae;
    }

    public void setIdeGecae(GenCategoriaEstatus ideGecae) {
        this.ideGecae = ideGecae;
    }

    public List<SprCargoContratacion> getSprCargoContratacionList() {
        return sprCargoContratacionList;
    }

    public void setSprCargoContratacionList(List<SprCargoContratacion> sprCargoContratacionList) {
        this.sprCargoContratacionList = sprCargoContratacionList;
    }

    public List<NrhCondicionAnticipo> getNrhCondicionAnticipoList() {
        return nrhCondicionAnticipoList;
    }

    public void setNrhCondicionAnticipoList(List<NrhCondicionAnticipo> nrhCondicionAnticipoList) {
        this.nrhCondicionAnticipoList = nrhCondicionAnticipoList;
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

    public List<NrhRolDetalleEscenario> getNrhRolDetalleEscenarioList() {
        return nrhRolDetalleEscenarioList;
    }

    public void setNrhRolDetalleEscenarioList(List<NrhRolDetalleEscenario> nrhRolDetalleEscenarioList) {
        this.nrhRolDetalleEscenarioList = nrhRolDetalleEscenarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGttco != null ? ideGttco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthTipoContrato)) {
            return false;
        }
        GthTipoContrato other = (GthTipoContrato) object;
        if ((this.ideGttco == null && other.ideGttco != null) || (this.ideGttco != null && !this.ideGttco.equals(other.ideGttco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthTipoContrato[ ideGttco=" + ideGttco + " ]";
    }
    
}
