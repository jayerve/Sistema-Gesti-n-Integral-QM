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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "cpp_evalua_resul_jefe", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppEvaluaResulJefe.findAll", query = "SELECT c FROM CppEvaluaResulJefe c"),
    @NamedQuery(name = "CppEvaluaResulJefe.findByIdeCperj", query = "SELECT c FROM CppEvaluaResulJefe c WHERE c.ideCperj = :ideCperj"),
    @NamedQuery(name = "CppEvaluaResulJefe.findByActivoCperj", query = "SELECT c FROM CppEvaluaResulJefe c WHERE c.activoCperj = :activoCperj"),
    @NamedQuery(name = "CppEvaluaResulJefe.findByPorcentajeCperj", query = "SELECT c FROM CppEvaluaResulJefe c WHERE c.porcentajeCperj = :porcentajeCperj"),
    @NamedQuery(name = "CppEvaluaResulJefe.findByUsuarioIngre", query = "SELECT c FROM CppEvaluaResulJefe c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppEvaluaResulJefe.findByFechaIngre", query = "SELECT c FROM CppEvaluaResulJefe c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppEvaluaResulJefe.findByHoraIngre", query = "SELECT c FROM CppEvaluaResulJefe c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppEvaluaResulJefe.findByUsuarioActua", query = "SELECT c FROM CppEvaluaResulJefe c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppEvaluaResulJefe.findByFechaActua", query = "SELECT c FROM CppEvaluaResulJefe c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppEvaluaResulJefe.findByHoraActua", query = "SELECT c FROM CppEvaluaResulJefe c WHERE c.horaActua = :horaActua")})
public class CppEvaluaResulJefe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cperj", nullable = false)
    private Integer ideCperj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cperj", nullable = false)
    private boolean activoCperj;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_cperj", precision = 12, scale = 2)
    private BigDecimal porcentajeCperj;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.DATE)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.DATE)
    private Date horaActua;
    @JoinColumn(name = "ide_cpree", referencedColumnName = "ide_cpree")
    @ManyToOne
    private CppResultadoEvaluacion ideCpree;
    @JoinColumn(name = "ide_cpevj", referencedColumnName = "ide_cpevj")
    @ManyToOne
    private CppEvaluacionJefe ideCpevj;

    public CppEvaluaResulJefe() {
    }

    public CppEvaluaResulJefe(Integer ideCperj) {
        this.ideCperj = ideCperj;
    }

    public CppEvaluaResulJefe(Integer ideCperj, boolean activoCperj) {
        this.ideCperj = ideCperj;
        this.activoCperj = activoCperj;
    }

    public Integer getIdeCperj() {
        return ideCperj;
    }

    public void setIdeCperj(Integer ideCperj) {
        this.ideCperj = ideCperj;
    }

    public boolean getActivoCperj() {
        return activoCperj;
    }

    public void setActivoCperj(boolean activoCperj) {
        this.activoCperj = activoCperj;
    }

    public BigDecimal getPorcentajeCperj() {
        return porcentajeCperj;
    }

    public void setPorcentajeCperj(BigDecimal porcentajeCperj) {
        this.porcentajeCperj = porcentajeCperj;
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

    public CppResultadoEvaluacion getIdeCpree() {
        return ideCpree;
    }

    public void setIdeCpree(CppResultadoEvaluacion ideCpree) {
        this.ideCpree = ideCpree;
    }

    public CppEvaluacionJefe getIdeCpevj() {
        return ideCpevj;
    }

    public void setIdeCpevj(CppEvaluacionJefe ideCpevj) {
        this.ideCpevj = ideCpevj;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCperj != null ? ideCperj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppEvaluaResulJefe)) {
            return false;
        }
        CppEvaluaResulJefe other = (CppEvaluaResulJefe) object;
        if ((this.ideCperj == null && other.ideCperj != null) || (this.ideCperj != null && !this.ideCperj.equals(other.ideCperj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppEvaluaResulJefe[ ideCperj=" + ideCperj + " ]";
    }
    
}
