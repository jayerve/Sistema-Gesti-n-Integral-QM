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
@Table(name = "nrh_detalle_tipo_nomina", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhDetalleTipoNomina.findAll", query = "SELECT n FROM NrhDetalleTipoNomina n"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByIdeNrdtn", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.ideNrdtn = :ideNrdtn"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByIdeSucu", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.ideSucu = :ideSucu"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByActivoNrdtn", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.activoNrdtn = :activoNrdtn"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByUsuarioIngre", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByFechaIngre", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByUsuarioActua", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByFechaActua", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByHoraIngre", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhDetalleTipoNomina.findByHoraActua", query = "SELECT n FROM NrhDetalleTipoNomina n WHERE n.horaActua = :horaActua")})
public class NrhDetalleTipoNomina implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrdtn", nullable = false)
    private Integer ideNrdtn;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Column(name = "activo_nrdtn")
    private Boolean activoNrdtn;
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
    @OneToMany(mappedBy = "ideNrdtn")
    private List<CntDetalleMovimiento> cntDetalleMovimientoList;
    @JoinColumn(name = "ide_nrtit", referencedColumnName = "ide_nrtit")
    @ManyToOne
    private NrhTipoRol ideNrtit;
    @JoinColumn(name = "ide_nrtin", referencedColumnName = "ide_nrtin")
    @ManyToOne
    private NrhTipoNomina ideNrtin;
    @JoinColumn(name = "ide_gttem", referencedColumnName = "ide_gttem")
    @ManyToOne
    private GthTipoEmpleado ideGttem;
    @JoinColumn(name = "ide_gttco", referencedColumnName = "ide_gttco")
    @ManyToOne
    private GthTipoContrato ideGttco;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrdtn")
    private List<NrhRol> nrhRolList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNrdtn")
    private List<NrhDetalleRubro> nrhDetalleRubroList;
    @OneToMany(mappedBy = "ideNrdtn")
    private List<NrhRolDetalleEscenario> nrhRolDetalleEscenarioList;

    public NrhDetalleTipoNomina() {
    }

    public NrhDetalleTipoNomina(Integer ideNrdtn) {
        this.ideNrdtn = ideNrdtn;
    }

    public Integer getIdeNrdtn() {
        return ideNrdtn;
    }

    public void setIdeNrdtn(Integer ideNrdtn) {
        this.ideNrdtn = ideNrdtn;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public Boolean getActivoNrdtn() {
        return activoNrdtn;
    }

    public void setActivoNrdtn(Boolean activoNrdtn) {
        this.activoNrdtn = activoNrdtn;
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

    public List<CntDetalleMovimiento> getCntDetalleMovimientoList() {
        return cntDetalleMovimientoList;
    }

    public void setCntDetalleMovimientoList(List<CntDetalleMovimiento> cntDetalleMovimientoList) {
        this.cntDetalleMovimientoList = cntDetalleMovimientoList;
    }

    public NrhTipoRol getIdeNrtit() {
        return ideNrtit;
    }

    public void setIdeNrtit(NrhTipoRol ideNrtit) {
        this.ideNrtit = ideNrtit;
    }

    public NrhTipoNomina getIdeNrtin() {
        return ideNrtin;
    }

    public void setIdeNrtin(NrhTipoNomina ideNrtin) {
        this.ideNrtin = ideNrtin;
    }

    public GthTipoEmpleado getIdeGttem() {
        return ideGttem;
    }

    public void setIdeGttem(GthTipoEmpleado ideGttem) {
        this.ideGttem = ideGttem;
    }

    public GthTipoContrato getIdeGttco() {
        return ideGttco;
    }

    public void setIdeGttco(GthTipoContrato ideGttco) {
        this.ideGttco = ideGttco;
    }

    public List<NrhRol> getNrhRolList() {
        return nrhRolList;
    }

    public void setNrhRolList(List<NrhRol> nrhRolList) {
        this.nrhRolList = nrhRolList;
    }

    public List<NrhDetalleRubro> getNrhDetalleRubroList() {
        return nrhDetalleRubroList;
    }

    public void setNrhDetalleRubroList(List<NrhDetalleRubro> nrhDetalleRubroList) {
        this.nrhDetalleRubroList = nrhDetalleRubroList;
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
        hash += (ideNrdtn != null ? ideNrdtn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhDetalleTipoNomina)) {
            return false;
        }
        NrhDetalleTipoNomina other = (NrhDetalleTipoNomina) object;
        if ((this.ideNrdtn == null && other.ideNrdtn != null) || (this.ideNrdtn != null && !this.ideNrdtn.equals(other.ideNrdtn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhDetalleTipoNomina[ ideNrdtn=" + ideNrdtn + " ]";
    }
    
}
