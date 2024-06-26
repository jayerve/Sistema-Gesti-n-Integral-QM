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
@Table(name = "gen_accion_empleado_depa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenAccionEmpleadoDepa.findAll", query = "SELECT g FROM GenAccionEmpleadoDepa g"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByIdeGeaed", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.ideGeaed = :ideGeaed"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByDetalleGeaed", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.detalleGeaed = :detalleGeaed"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByActivoGeaed", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.activoGeaed = :activoGeaed"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByUsuarioIngre", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByFechaIngre", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByUsuarioActua", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByFechaActua", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByFiniquitoContratoGeaed", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.finiquitoContratoGeaed = :finiquitoContratoGeaed"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByHoraIngre", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenAccionEmpleadoDepa.findByHoraActua", query = "SELECT g FROM GenAccionEmpleadoDepa g WHERE g.horaActua = :horaActua")})
public class GenAccionEmpleadoDepa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_geaed", nullable = false)
    private Integer ideGeaed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "detalle_geaed", nullable = false, length = 100)
    private String detalleGeaed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_geaed", nullable = false)
    private boolean activoGeaed;
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
    @Column(name = "finiquito_contrato_geaed")
    private Boolean finiquitoContratoGeaed;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideGeaed")
    private List<GenAccionMotivoEmpleado> genAccionMotivoEmpleadoList;

    public GenAccionEmpleadoDepa() {
    }

    public GenAccionEmpleadoDepa(Integer ideGeaed) {
        this.ideGeaed = ideGeaed;
    }

    public GenAccionEmpleadoDepa(Integer ideGeaed, String detalleGeaed, boolean activoGeaed) {
        this.ideGeaed = ideGeaed;
        this.detalleGeaed = detalleGeaed;
        this.activoGeaed = activoGeaed;
    }

    public Integer getIdeGeaed() {
        return ideGeaed;
    }

    public void setIdeGeaed(Integer ideGeaed) {
        this.ideGeaed = ideGeaed;
    }

    public String getDetalleGeaed() {
        return detalleGeaed;
    }

    public void setDetalleGeaed(String detalleGeaed) {
        this.detalleGeaed = detalleGeaed;
    }

    public boolean getActivoGeaed() {
        return activoGeaed;
    }

    public void setActivoGeaed(boolean activoGeaed) {
        this.activoGeaed = activoGeaed;
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

    public Boolean getFiniquitoContratoGeaed() {
        return finiquitoContratoGeaed;
    }

    public void setFiniquitoContratoGeaed(Boolean finiquitoContratoGeaed) {
        this.finiquitoContratoGeaed = finiquitoContratoGeaed;
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

    public List<GenAccionMotivoEmpleado> getGenAccionMotivoEmpleadoList() {
        return genAccionMotivoEmpleadoList;
    }

    public void setGenAccionMotivoEmpleadoList(List<GenAccionMotivoEmpleado> genAccionMotivoEmpleadoList) {
        this.genAccionMotivoEmpleadoList = genAccionMotivoEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGeaed != null ? ideGeaed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenAccionEmpleadoDepa)) {
            return false;
        }
        GenAccionEmpleadoDepa other = (GenAccionEmpleadoDepa) object;
        if ((this.ideGeaed == null && other.ideGeaed != null) || (this.ideGeaed != null && !this.ideGeaed.equals(other.ideGeaed))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenAccionEmpleadoDepa[ ideGeaed=" + ideGeaed + " ]";
    }
    
}
