/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "nrh_salario_basico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhSalarioBasico.findAll", query = "SELECT n FROM NrhSalarioBasico n"),
    @NamedQuery(name = "NrhSalarioBasico.findByIdeNrsab", query = "SELECT n FROM NrhSalarioBasico n WHERE n.ideNrsab = :ideNrsab"),
    @NamedQuery(name = "NrhSalarioBasico.findByFechaInicioNrsab", query = "SELECT n FROM NrhSalarioBasico n WHERE n.fechaInicioNrsab = :fechaInicioNrsab"),
    @NamedQuery(name = "NrhSalarioBasico.findByFechaFinNrsab", query = "SELECT n FROM NrhSalarioBasico n WHERE n.fechaFinNrsab = :fechaFinNrsab"),
    @NamedQuery(name = "NrhSalarioBasico.findByValorSalarioNrsab", query = "SELECT n FROM NrhSalarioBasico n WHERE n.valorSalarioNrsab = :valorSalarioNrsab"),
    @NamedQuery(name = "NrhSalarioBasico.findByPorcentajeGuarderiaNrsab", query = "SELECT n FROM NrhSalarioBasico n WHERE n.porcentajeGuarderiaNrsab = :porcentajeGuarderiaNrsab"),
    @NamedQuery(name = "NrhSalarioBasico.findByActivoNrsab", query = "SELECT n FROM NrhSalarioBasico n WHERE n.activoNrsab = :activoNrsab"),
    @NamedQuery(name = "NrhSalarioBasico.findByUsuarioIngre", query = "SELECT n FROM NrhSalarioBasico n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhSalarioBasico.findByFechaIngre", query = "SELECT n FROM NrhSalarioBasico n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhSalarioBasico.findByUsuarioActua", query = "SELECT n FROM NrhSalarioBasico n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhSalarioBasico.findByFechaActua", query = "SELECT n FROM NrhSalarioBasico n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhSalarioBasico.findByHoraIngre", query = "SELECT n FROM NrhSalarioBasico n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhSalarioBasico.findByHoraActua", query = "SELECT n FROM NrhSalarioBasico n WHERE n.horaActua = :horaActua")})
public class NrhSalarioBasico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrsab", nullable = false)
    private Integer ideNrsab;
    @Column(name = "fecha_inicio_nrsab")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioNrsab;
    @Column(name = "fecha_fin_nrsab")
    @Temporal(TemporalType.DATE)
    private Date fechaFinNrsab;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_salario_nrsab", precision = 12, scale = 3)
    private BigDecimal valorSalarioNrsab;
    @Column(name = "porcentaje_guarderia_nrsab", precision = 5, scale = 2)
    private BigDecimal porcentajeGuarderiaNrsab;
    @Column(name = "activo_nrsab")
    private Boolean activoNrsab;
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

    public NrhSalarioBasico() {
    }

    public NrhSalarioBasico(Integer ideNrsab) {
        this.ideNrsab = ideNrsab;
    }

    public Integer getIdeNrsab() {
        return ideNrsab;
    }

    public void setIdeNrsab(Integer ideNrsab) {
        this.ideNrsab = ideNrsab;
    }

    public Date getFechaInicioNrsab() {
        return fechaInicioNrsab;
    }

    public void setFechaInicioNrsab(Date fechaInicioNrsab) {
        this.fechaInicioNrsab = fechaInicioNrsab;
    }

    public Date getFechaFinNrsab() {
        return fechaFinNrsab;
    }

    public void setFechaFinNrsab(Date fechaFinNrsab) {
        this.fechaFinNrsab = fechaFinNrsab;
    }

    public BigDecimal getValorSalarioNrsab() {
        return valorSalarioNrsab;
    }

    public void setValorSalarioNrsab(BigDecimal valorSalarioNrsab) {
        this.valorSalarioNrsab = valorSalarioNrsab;
    }

    public BigDecimal getPorcentajeGuarderiaNrsab() {
        return porcentajeGuarderiaNrsab;
    }

    public void setPorcentajeGuarderiaNrsab(BigDecimal porcentajeGuarderiaNrsab) {
        this.porcentajeGuarderiaNrsab = porcentajeGuarderiaNrsab;
    }

    public Boolean getActivoNrsab() {
        return activoNrsab;
    }

    public void setActivoNrsab(Boolean activoNrsab) {
        this.activoNrsab = activoNrsab;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrsab != null ? ideNrsab.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhSalarioBasico)) {
            return false;
        }
        NrhSalarioBasico other = (NrhSalarioBasico) object;
        if ((this.ideNrsab == null && other.ideNrsab != null) || (this.ideNrsab != null && !this.ideNrsab.equals(other.ideNrsab))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhSalarioBasico[ ideNrsab=" + ideNrsab + " ]";
    }
    
}
