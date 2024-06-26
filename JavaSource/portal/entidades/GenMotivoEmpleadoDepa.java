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
@Table(name = "gen_motivo_empleado_depa", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findAll", query = "SELECT g FROM GenMotivoEmpleadoDepa g"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByIdeGemed", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.ideGemed = :ideGemed"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByDetalleGemed", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.detalleGemed = :detalleGemed"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByDetalleReporteGemed", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.detalleReporteGemed = :detalleReporteGemed"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByActivoGemed", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.activoGemed = :activoGemed"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByUsuarioIngre", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByFechaIngre", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByUsuarioActua", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByFechaActua", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByHoraIngre", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenMotivoEmpleadoDepa.findByHoraActua", query = "SELECT g FROM GenMotivoEmpleadoDepa g WHERE g.horaActua = :horaActua")})
public class GenMotivoEmpleadoDepa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gemed", nullable = false)
    private Integer ideGemed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "detalle_gemed", nullable = false, length = 100)
    private String detalleGemed;
    @Size(max = 4000)
    @Column(name = "detalle_reporte_gemed", length = 4000)
    private String detalleReporteGemed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_gemed", nullable = false)
    private boolean activoGemed;
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
    @OneToMany(mappedBy = "ideGemed")
    private List<GenAccionMotivoEmpleado> genAccionMotivoEmpleadoList;

    public GenMotivoEmpleadoDepa() {
    }

    public GenMotivoEmpleadoDepa(Integer ideGemed) {
        this.ideGemed = ideGemed;
    }

    public GenMotivoEmpleadoDepa(Integer ideGemed, String detalleGemed, boolean activoGemed) {
        this.ideGemed = ideGemed;
        this.detalleGemed = detalleGemed;
        this.activoGemed = activoGemed;
    }

    public Integer getIdeGemed() {
        return ideGemed;
    }

    public void setIdeGemed(Integer ideGemed) {
        this.ideGemed = ideGemed;
    }

    public String getDetalleGemed() {
        return detalleGemed;
    }

    public void setDetalleGemed(String detalleGemed) {
        this.detalleGemed = detalleGemed;
    }

    public String getDetalleReporteGemed() {
        return detalleReporteGemed;
    }

    public void setDetalleReporteGemed(String detalleReporteGemed) {
        this.detalleReporteGemed = detalleReporteGemed;
    }

    public boolean getActivoGemed() {
        return activoGemed;
    }

    public void setActivoGemed(boolean activoGemed) {
        this.activoGemed = activoGemed;
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

    public List<GenAccionMotivoEmpleado> getGenAccionMotivoEmpleadoList() {
        return genAccionMotivoEmpleadoList;
    }

    public void setGenAccionMotivoEmpleadoList(List<GenAccionMotivoEmpleado> genAccionMotivoEmpleadoList) {
        this.genAccionMotivoEmpleadoList = genAccionMotivoEmpleadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemed != null ? ideGemed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenMotivoEmpleadoDepa)) {
            return false;
        }
        GenMotivoEmpleadoDepa other = (GenMotivoEmpleadoDepa) object;
        if ((this.ideGemed == null && other.ideGemed != null) || (this.ideGemed != null && !this.ideGemed.equals(other.ideGemed))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenMotivoEmpleadoDepa[ ideGemed=" + ideGemed + " ]";
    }
    
}
