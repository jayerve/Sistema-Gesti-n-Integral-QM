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
@Table(name = "gen_mes", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GenMes.findAll", query = "SELECT g FROM GenMes g"),
    @NamedQuery(name = "GenMes.findByIdeGemes", query = "SELECT g FROM GenMes g WHERE g.ideGemes = :ideGemes"),
    @NamedQuery(name = "GenMes.findByDetalleGemes", query = "SELECT g FROM GenMes g WHERE g.detalleGemes = :detalleGemes"),
    @NamedQuery(name = "GenMes.findByActivoGemes", query = "SELECT g FROM GenMes g WHERE g.activoGemes = :activoGemes"),
    @NamedQuery(name = "GenMes.findByUsuarioIngre", query = "SELECT g FROM GenMes g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GenMes.findByFechaIngre", query = "SELECT g FROM GenMes g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GenMes.findByHoraIngre", query = "SELECT g FROM GenMes g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GenMes.findByUsuarioActua", query = "SELECT g FROM GenMes g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GenMes.findByFechaActua", query = "SELECT g FROM GenMes g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GenMes.findByHoraActua", query = "SELECT g FROM GenMes g WHERE g.horaActua = :horaActua"),
    @NamedQuery(name = "GenMes.findByBloqueadoGemes", query = "SELECT g FROM GenMes g WHERE g.bloqueadoGemes = :bloqueadoGemes"),
    @NamedQuery(name = "GenMes.findByReformaGemes", query = "SELECT g FROM GenMes g WHERE g.reformaGemes = :reformaGemes")})
public class GenMes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_gemes", nullable = false)
    private Long ideGemes;
    @Size(max = 50)
    @Column(name = "detalle_gemes", length = 50)
    private String detalleGemes;
    @Column(name = "activo_gemes")
    private Boolean activoGemes;
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
    @Column(name = "bloqueado_gemes")
    private Boolean bloqueadoGemes;
    @Column(name = "reforma_gemes")
    private Boolean reformaGemes;
    @OneToMany(mappedBy = "ideGemes")
    private List<PrePoaMes> prePoaMesList;
    @OneToMany(mappedBy = "ideGemes")
    private List<ContPeriodoCuatrimestre> contPeriodoCuatrimestreList;
    @OneToMany(mappedBy = "ideGemes")
    private List<PrePoaReforma> prePoaReformaList;
    @OneToMany(mappedBy = "ideGemes")
    private List<PreReformaMes> preReformaMesList;
    @OneToMany(mappedBy = "ideGemes")
    private List<ContMovimiento> contMovimientoList;

    public GenMes() {
    }

    public GenMes(Long ideGemes) {
        this.ideGemes = ideGemes;
    }

    public Long getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(Long ideGemes) {
        this.ideGemes = ideGemes;
    }

    public String getDetalleGemes() {
        return detalleGemes;
    }

    public void setDetalleGemes(String detalleGemes) {
        this.detalleGemes = detalleGemes;
    }

    public Boolean getActivoGemes() {
        return activoGemes;
    }

    public void setActivoGemes(Boolean activoGemes) {
        this.activoGemes = activoGemes;
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

    public Boolean getBloqueadoGemes() {
        return bloqueadoGemes;
    }

    public void setBloqueadoGemes(Boolean bloqueadoGemes) {
        this.bloqueadoGemes = bloqueadoGemes;
    }

    public Boolean getReformaGemes() {
        return reformaGemes;
    }

    public void setReformaGemes(Boolean reformaGemes) {
        this.reformaGemes = reformaGemes;
    }

    public List<PrePoaMes> getPrePoaMesList() {
        return prePoaMesList;
    }

    public void setPrePoaMesList(List<PrePoaMes> prePoaMesList) {
        this.prePoaMesList = prePoaMesList;
    }

    public List<ContPeriodoCuatrimestre> getContPeriodoCuatrimestreList() {
        return contPeriodoCuatrimestreList;
    }

    public void setContPeriodoCuatrimestreList(List<ContPeriodoCuatrimestre> contPeriodoCuatrimestreList) {
        this.contPeriodoCuatrimestreList = contPeriodoCuatrimestreList;
    }

    public List<PrePoaReforma> getPrePoaReformaList() {
        return prePoaReformaList;
    }

    public void setPrePoaReformaList(List<PrePoaReforma> prePoaReformaList) {
        this.prePoaReformaList = prePoaReformaList;
    }

    public List<PreReformaMes> getPreReformaMesList() {
        return preReformaMesList;
    }

    public void setPreReformaMesList(List<PreReformaMes> preReformaMesList) {
        this.preReformaMesList = preReformaMesList;
    }

    public List<ContMovimiento> getContMovimientoList() {
        return contMovimientoList;
    }

    public void setContMovimientoList(List<ContMovimiento> contMovimientoList) {
        this.contMovimientoList = contMovimientoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGemes != null ? ideGemes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GenMes)) {
            return false;
        }
        GenMes other = (GenMes) object;
        if ((this.ideGemes == null && other.ideGemes != null) || (this.ideGemes != null && !this.ideGemes.equals(other.ideGemes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GenMes[ ideGemes=" + ideGemes + " ]";
    }
    
}
