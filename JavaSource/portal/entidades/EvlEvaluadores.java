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
@Table(name = "evl_evaluadores", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlEvaluadores.findAll", query = "SELECT e FROM EvlEvaluadores e"),
    @NamedQuery(name = "EvlEvaluadores.findByIdeEveva", query = "SELECT e FROM EvlEvaluadores e WHERE e.ideEveva = :ideEveva"),
    @NamedQuery(name = "EvlEvaluadores.findByFechaEveva", query = "SELECT e FROM EvlEvaluadores e WHERE e.fechaEveva = :fechaEveva"),
    @NamedQuery(name = "EvlEvaluadores.findByFechaEvaluacionEveva", query = "SELECT e FROM EvlEvaluadores e WHERE e.fechaEvaluacionEveva = :fechaEvaluacionEveva"),
    @NamedQuery(name = "EvlEvaluadores.findByPorPesoEveva", query = "SELECT e FROM EvlEvaluadores e WHERE e.porPesoEveva = :porPesoEveva"),
    @NamedQuery(name = "EvlEvaluadores.findByActivoEveva", query = "SELECT e FROM EvlEvaluadores e WHERE e.activoEveva = :activoEveva"),
    @NamedQuery(name = "EvlEvaluadores.findByUsuarioIngre", query = "SELECT e FROM EvlEvaluadores e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlEvaluadores.findByFechaIngre", query = "SELECT e FROM EvlEvaluadores e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlEvaluadores.findByUsuarioActua", query = "SELECT e FROM EvlEvaluadores e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlEvaluadores.findByFechaActua", query = "SELECT e FROM EvlEvaluadores e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlEvaluadores.findByHoraIngre", query = "SELECT e FROM EvlEvaluadores e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlEvaluadores.findByHoraActua", query = "SELECT e FROM EvlEvaluadores e WHERE e.horaActua = :horaActua")})
public class EvlEvaluadores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_eveva", nullable = false)
    private Integer ideEveva;
    @Column(name = "fecha_eveva")
    @Temporal(TemporalType.DATE)
    private Date fechaEveva;
    @Column(name = "fecha_evaluacion_eveva")
    @Temporal(TemporalType.DATE)
    private Date fechaEvaluacionEveva;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "por_peso_eveva", precision = 5, scale = 2)
    private BigDecimal porPesoEveva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_eveva", nullable = false)
    private boolean activoEveva;
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
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_evdes", referencedColumnName = "ide_evdes")
    @ManyToOne
    private EvlDesempenio ideEvdes;
    @OneToMany(mappedBy = "ideEveva")
    private List<EvlOtraCompetencia> evlOtraCompetenciaList;
    @OneToMany(mappedBy = "ideEveva")
    private List<EvlResultado> evlResultadoList;
    @OneToMany(mappedBy = "ideEveva")
    private List<EvlActividadPuesto> evlActividadPuestoList;

    public EvlEvaluadores() {
    }

    public EvlEvaluadores(Integer ideEveva) {
        this.ideEveva = ideEveva;
    }

    public EvlEvaluadores(Integer ideEveva, boolean activoEveva) {
        this.ideEveva = ideEveva;
        this.activoEveva = activoEveva;
    }

    public Integer getIdeEveva() {
        return ideEveva;
    }

    public void setIdeEveva(Integer ideEveva) {
        this.ideEveva = ideEveva;
    }

    public Date getFechaEveva() {
        return fechaEveva;
    }

    public void setFechaEveva(Date fechaEveva) {
        this.fechaEveva = fechaEveva;
    }

    public Date getFechaEvaluacionEveva() {
        return fechaEvaluacionEveva;
    }

    public void setFechaEvaluacionEveva(Date fechaEvaluacionEveva) {
        this.fechaEvaluacionEveva = fechaEvaluacionEveva;
    }

    public BigDecimal getPorPesoEveva() {
        return porPesoEveva;
    }

    public void setPorPesoEveva(BigDecimal porPesoEveva) {
        this.porPesoEveva = porPesoEveva;
    }

    public boolean getActivoEveva() {
        return activoEveva;
    }

    public void setActivoEveva(boolean activoEveva) {
        this.activoEveva = activoEveva;
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

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public EvlDesempenio getIdeEvdes() {
        return ideEvdes;
    }

    public void setIdeEvdes(EvlDesempenio ideEvdes) {
        this.ideEvdes = ideEvdes;
    }

    public List<EvlOtraCompetencia> getEvlOtraCompetenciaList() {
        return evlOtraCompetenciaList;
    }

    public void setEvlOtraCompetenciaList(List<EvlOtraCompetencia> evlOtraCompetenciaList) {
        this.evlOtraCompetenciaList = evlOtraCompetenciaList;
    }

    public List<EvlResultado> getEvlResultadoList() {
        return evlResultadoList;
    }

    public void setEvlResultadoList(List<EvlResultado> evlResultadoList) {
        this.evlResultadoList = evlResultadoList;
    }

    public List<EvlActividadPuesto> getEvlActividadPuestoList() {
        return evlActividadPuestoList;
    }

    public void setEvlActividadPuestoList(List<EvlActividadPuesto> evlActividadPuestoList) {
        this.evlActividadPuestoList = evlActividadPuestoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEveva != null ? ideEveva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlEvaluadores)) {
            return false;
        }
        EvlEvaluadores other = (EvlEvaluadores) object;
        if ((this.ideEveva == null && other.ideEveva != null) || (this.ideEveva != null && !this.ideEveva.equals(other.ideEveva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlEvaluadores[ ideEveva=" + ideEveva + " ]";
    }
    
}
