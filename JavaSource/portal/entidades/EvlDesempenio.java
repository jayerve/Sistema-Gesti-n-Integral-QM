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
@Table(name = "evl_desempenio", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlDesempenio.findAll", query = "SELECT e FROM EvlDesempenio e"),
    @NamedQuery(name = "EvlDesempenio.findByIdeEvdes", query = "SELECT e FROM EvlDesempenio e WHERE e.ideEvdes = :ideEvdes"),
    @NamedQuery(name = "EvlDesempenio.findByFechaDesdeEvdes", query = "SELECT e FROM EvlDesempenio e WHERE e.fechaDesdeEvdes = :fechaDesdeEvdes"),
    @NamedQuery(name = "EvlDesempenio.findByFechaHastaEvdes", query = "SELECT e FROM EvlDesempenio e WHERE e.fechaHastaEvdes = :fechaHastaEvdes"),
    @NamedQuery(name = "EvlDesempenio.findByTituloProfesionalEvdes", query = "SELECT e FROM EvlDesempenio e WHERE e.tituloProfesionalEvdes = :tituloProfesionalEvdes"),
    @NamedQuery(name = "EvlDesempenio.findByActivoEvdes", query = "SELECT e FROM EvlDesempenio e WHERE e.activoEvdes = :activoEvdes"),
    @NamedQuery(name = "EvlDesempenio.findByUsuarioIngre", query = "SELECT e FROM EvlDesempenio e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlDesempenio.findByFechaIngre", query = "SELECT e FROM EvlDesempenio e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlDesempenio.findByUsuarioActua", query = "SELECT e FROM EvlDesempenio e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlDesempenio.findByFechaActua", query = "SELECT e FROM EvlDesempenio e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlDesempenio.findByHoraIngre", query = "SELECT e FROM EvlDesempenio e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlDesempenio.findByHoraActua", query = "SELECT e FROM EvlDesempenio e WHERE e.horaActua = :horaActua")})
public class EvlDesempenio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evdes", nullable = false)
    private Integer ideEvdes;
    @Column(name = "fecha_desde_evdes")
    @Temporal(TemporalType.DATE)
    private Date fechaDesdeEvdes;
    @Column(name = "fecha_hasta_evdes")
    @Temporal(TemporalType.DATE)
    private Date fechaHastaEvdes;
    @Size(max = 50)
    @Column(name = "titulo_profesional_evdes", length = 50)
    private String tituloProfesionalEvdes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evdes", nullable = false)
    private boolean activoEvdes;
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
    @OneToMany(mappedBy = "ideEvdes")
    private List<EvlEvaluadores> evlEvaluadoresList;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;

    public EvlDesempenio() {
    }

    public EvlDesempenio(Integer ideEvdes) {
        this.ideEvdes = ideEvdes;
    }

    public EvlDesempenio(Integer ideEvdes, boolean activoEvdes) {
        this.ideEvdes = ideEvdes;
        this.activoEvdes = activoEvdes;
    }

    public Integer getIdeEvdes() {
        return ideEvdes;
    }

    public void setIdeEvdes(Integer ideEvdes) {
        this.ideEvdes = ideEvdes;
    }

    public Date getFechaDesdeEvdes() {
        return fechaDesdeEvdes;
    }

    public void setFechaDesdeEvdes(Date fechaDesdeEvdes) {
        this.fechaDesdeEvdes = fechaDesdeEvdes;
    }

    public Date getFechaHastaEvdes() {
        return fechaHastaEvdes;
    }

    public void setFechaHastaEvdes(Date fechaHastaEvdes) {
        this.fechaHastaEvdes = fechaHastaEvdes;
    }

    public String getTituloProfesionalEvdes() {
        return tituloProfesionalEvdes;
    }

    public void setTituloProfesionalEvdes(String tituloProfesionalEvdes) {
        this.tituloProfesionalEvdes = tituloProfesionalEvdes;
    }

    public boolean getActivoEvdes() {
        return activoEvdes;
    }

    public void setActivoEvdes(boolean activoEvdes) {
        this.activoEvdes = activoEvdes;
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

    public List<EvlEvaluadores> getEvlEvaluadoresList() {
        return evlEvaluadoresList;
    }

    public void setEvlEvaluadoresList(List<EvlEvaluadores> evlEvaluadoresList) {
        this.evlEvaluadoresList = evlEvaluadoresList;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEvdes != null ? ideEvdes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlDesempenio)) {
            return false;
        }
        EvlDesempenio other = (EvlDesempenio) object;
        if ((this.ideEvdes == null && other.ideEvdes != null) || (this.ideEvdes != null && !this.ideEvdes.equals(other.ideEvdes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlDesempenio[ ideEvdes=" + ideEvdes + " ]";
    }
    
}
