/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "cont_periodo_cuatrimestre", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContPeriodoCuatrimestre.findAll", query = "SELECT c FROM ContPeriodoCuatrimestre c"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByIdeCopec", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.ideCopec = :ideCopec"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByGenIdeGemes", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.genIdeGemes = :genIdeGemes"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByDetalleCopec", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.detalleCopec = :detalleCopec"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByAbreviaturaCopec", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.abreviaturaCopec = :abreviaturaCopec"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByActivoCopec", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.activoCopec = :activoCopec"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByUsuarioIngre", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByFechaIngre", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByHoraIngre", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByUsuarioActua", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByFechaActua", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContPeriodoCuatrimestre.findByHoraActua", query = "SELECT c FROM ContPeriodoCuatrimestre c WHERE c.horaActua = :horaActua")})
public class ContPeriodoCuatrimestre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_copec", nullable = false)
    private Long ideCopec;
    @Column(name = "gen_ide_gemes")
    private BigInteger genIdeGemes;
    @Size(max = 50)
    @Column(name = "detalle_copec", length = 50)
    private String detalleCopec;
    @Size(max = 20)
    @Column(name = "abreviatura_copec", length = 20)
    private String abreviaturaCopec;
    @Column(name = "activo_copec")
    private Boolean activoCopec;
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
    @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes")
    @ManyToOne
    private GenMes ideGemes;
    @OneToMany(mappedBy = "ideCopec")
    private List<PrePacPeriodo> prePacPeriodoList;

    public ContPeriodoCuatrimestre() {
    }

    public ContPeriodoCuatrimestre(Long ideCopec) {
        this.ideCopec = ideCopec;
    }

    public Long getIdeCopec() {
        return ideCopec;
    }

    public void setIdeCopec(Long ideCopec) {
        this.ideCopec = ideCopec;
    }

    public BigInteger getGenIdeGemes() {
        return genIdeGemes;
    }

    public void setGenIdeGemes(BigInteger genIdeGemes) {
        this.genIdeGemes = genIdeGemes;
    }

    public String getDetalleCopec() {
        return detalleCopec;
    }

    public void setDetalleCopec(String detalleCopec) {
        this.detalleCopec = detalleCopec;
    }

    public String getAbreviaturaCopec() {
        return abreviaturaCopec;
    }

    public void setAbreviaturaCopec(String abreviaturaCopec) {
        this.abreviaturaCopec = abreviaturaCopec;
    }

    public Boolean getActivoCopec() {
        return activoCopec;
    }

    public void setActivoCopec(Boolean activoCopec) {
        this.activoCopec = activoCopec;
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

    public GenMes getIdeGemes() {
        return ideGemes;
    }

    public void setIdeGemes(GenMes ideGemes) {
        this.ideGemes = ideGemes;
    }

    public List<PrePacPeriodo> getPrePacPeriodoList() {
        return prePacPeriodoList;
    }

    public void setPrePacPeriodoList(List<PrePacPeriodo> prePacPeriodoList) {
        this.prePacPeriodoList = prePacPeriodoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCopec != null ? ideCopec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContPeriodoCuatrimestre)) {
            return false;
        }
        ContPeriodoCuatrimestre other = (ContPeriodoCuatrimestre) object;
        if ((this.ideCopec == null && other.ideCopec != null) || (this.ideCopec != null && !this.ideCopec.equals(other.ideCopec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContPeriodoCuatrimestre[ ideCopec=" + ideCopec + " ]";
    }
    
}
