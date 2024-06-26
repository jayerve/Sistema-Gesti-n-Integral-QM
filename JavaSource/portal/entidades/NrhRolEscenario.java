/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "nrh_rol_escenario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhRolEscenario.findAll", query = "SELECT n FROM NrhRolEscenario n"),
    @NamedQuery(name = "NrhRolEscenario.findByIdeNrroe", query = "SELECT n FROM NrhRolEscenario n WHERE n.ideNrroe = :ideNrroe"),
    @NamedQuery(name = "NrhRolEscenario.findByDetalleNrroe", query = "SELECT n FROM NrhRolEscenario n WHERE n.detalleNrroe = :detalleNrroe"),
    @NamedQuery(name = "NrhRolEscenario.findByFechaEscenarioNrroe", query = "SELECT n FROM NrhRolEscenario n WHERE n.fechaEscenarioNrroe = :fechaEscenarioNrroe"),
    @NamedQuery(name = "NrhRolEscenario.findByPorVariacionNrroe", query = "SELECT n FROM NrhRolEscenario n WHERE n.porVariacionNrroe = :porVariacionNrroe"),
    @NamedQuery(name = "NrhRolEscenario.findByActivoNrroe", query = "SELECT n FROM NrhRolEscenario n WHERE n.activoNrroe = :activoNrroe"),
    @NamedQuery(name = "NrhRolEscenario.findByUsuarioIngre", query = "SELECT n FROM NrhRolEscenario n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhRolEscenario.findByFechaIngre", query = "SELECT n FROM NrhRolEscenario n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhRolEscenario.findByUsuarioActua", query = "SELECT n FROM NrhRolEscenario n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhRolEscenario.findByFechaActua", query = "SELECT n FROM NrhRolEscenario n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhRolEscenario.findByHoraIngre", query = "SELECT n FROM NrhRolEscenario n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhRolEscenario.findByHoraActua", query = "SELECT n FROM NrhRolEscenario n WHERE n.horaActua = :horaActua"),
    @NamedQuery(name = "NrhRolEscenario.findByAprobadoNrroe", query = "SELECT n FROM NrhRolEscenario n WHERE n.aprobadoNrroe = :aprobadoNrroe")})
public class NrhRolEscenario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrroe", nullable = false)
    private Integer ideNrroe;
    @Size(max = 1000)
    @Column(name = "detalle_nrroe", length = 1000)
    private String detalleNrroe;
    @Column(name = "fecha_escenario_nrroe")
    @Temporal(TemporalType.DATE)
    private Date fechaEscenarioNrroe;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "por_variacion_nrroe", precision = 5, scale = 2)
    private BigDecimal porVariacionNrroe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_nrroe", nullable = false)
    private boolean activoNrroe;
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
    @Column(name = "aprobado_nrroe")
    private Boolean aprobadoNrroe;
    @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")
    @ManyToOne
    private GenAnio ideGeani;
    @OneToMany(mappedBy = "ideNrroe")
    private List<NrhRolDetalleEscenario> nrhRolDetalleEscenarioList;

    public NrhRolEscenario() {
    }

    public NrhRolEscenario(Integer ideNrroe) {
        this.ideNrroe = ideNrroe;
    }

    public NrhRolEscenario(Integer ideNrroe, boolean activoNrroe) {
        this.ideNrroe = ideNrroe;
        this.activoNrroe = activoNrroe;
    }

    public Integer getIdeNrroe() {
        return ideNrroe;
    }

    public void setIdeNrroe(Integer ideNrroe) {
        this.ideNrroe = ideNrroe;
    }

    public String getDetalleNrroe() {
        return detalleNrroe;
    }

    public void setDetalleNrroe(String detalleNrroe) {
        this.detalleNrroe = detalleNrroe;
    }

    public Date getFechaEscenarioNrroe() {
        return fechaEscenarioNrroe;
    }

    public void setFechaEscenarioNrroe(Date fechaEscenarioNrroe) {
        this.fechaEscenarioNrroe = fechaEscenarioNrroe;
    }

    public BigDecimal getPorVariacionNrroe() {
        return porVariacionNrroe;
    }

    public void setPorVariacionNrroe(BigDecimal porVariacionNrroe) {
        this.porVariacionNrroe = porVariacionNrroe;
    }

    public boolean getActivoNrroe() {
        return activoNrroe;
    }

    public void setActivoNrroe(boolean activoNrroe) {
        this.activoNrroe = activoNrroe;
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

    public Boolean getAprobadoNrroe() {
        return aprobadoNrroe;
    }

    public void setAprobadoNrroe(Boolean aprobadoNrroe) {
        this.aprobadoNrroe = aprobadoNrroe;
    }

    public GenAnio getIdeGeani() {
        return ideGeani;
    }

    public void setIdeGeani(GenAnio ideGeani) {
        this.ideGeani = ideGeani;
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
        hash += (ideNrroe != null ? ideNrroe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhRolEscenario)) {
            return false;
        }
        NrhRolEscenario other = (NrhRolEscenario) object;
        if ((this.ideNrroe == null && other.ideNrroe != null) || (this.ideNrroe != null && !this.ideNrroe.equals(other.ideNrroe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhRolEscenario[ ideNrroe=" + ideNrroe + " ]";
    }
    
}
