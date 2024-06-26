/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
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
@Table(name = "cpp_detalle_eval_taller", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppDetalleEvalTaller.findAll", query = "SELECT c FROM CppDetalleEvalTaller c"),
    @NamedQuery(name = "CppDetalleEvalTaller.findByIdeCpdet", query = "SELECT c FROM CppDetalleEvalTaller c WHERE c.ideCpdet = :ideCpdet"),
    @NamedQuery(name = "CppDetalleEvalTaller.findByActivoCpdet", query = "SELECT c FROM CppDetalleEvalTaller c WHERE c.activoCpdet = :activoCpdet"),
    @NamedQuery(name = "CppDetalleEvalTaller.findByUsuarioIngre", query = "SELECT c FROM CppDetalleEvalTaller c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppDetalleEvalTaller.findByFechaIngre", query = "SELECT c FROM CppDetalleEvalTaller c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppDetalleEvalTaller.findByHoraIngre", query = "SELECT c FROM CppDetalleEvalTaller c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppDetalleEvalTaller.findByUsuarioActua", query = "SELECT c FROM CppDetalleEvalTaller c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppDetalleEvalTaller.findByFechaActua", query = "SELECT c FROM CppDetalleEvalTaller c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppDetalleEvalTaller.findByHoraActua", query = "SELECT c FROM CppDetalleEvalTaller c WHERE c.horaActua = :horaActua")})
public class CppDetalleEvalTaller implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpdet", nullable = false)
    private Integer ideCpdet;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpdet", nullable = false)
    private boolean activoCpdet;
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
    @JoinColumn(name = "ide_cppae", referencedColumnName = "ide_cppae")
    @ManyToOne
    private CppParametroEvaluacion ideCppae;
    @JoinColumn(name = "ide_cpevt", referencedColumnName = "ide_cpevt")
    @ManyToOne
    private CppEvaluacionTaller ideCpevt;
    @JoinColumn(name = "ide_cpeqe", referencedColumnName = "ide_cpeqe")
    @ManyToOne
    private CppEquivalenciaEvento ideCpeqe;

    public CppDetalleEvalTaller() {
    }

    public CppDetalleEvalTaller(Integer ideCpdet) {
        this.ideCpdet = ideCpdet;
    }

    public CppDetalleEvalTaller(Integer ideCpdet, boolean activoCpdet) {
        this.ideCpdet = ideCpdet;
        this.activoCpdet = activoCpdet;
    }

    public Integer getIdeCpdet() {
        return ideCpdet;
    }

    public void setIdeCpdet(Integer ideCpdet) {
        this.ideCpdet = ideCpdet;
    }

    public boolean getActivoCpdet() {
        return activoCpdet;
    }

    public void setActivoCpdet(boolean activoCpdet) {
        this.activoCpdet = activoCpdet;
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

    public CppParametroEvaluacion getIdeCppae() {
        return ideCppae;
    }

    public void setIdeCppae(CppParametroEvaluacion ideCppae) {
        this.ideCppae = ideCppae;
    }

    public CppEvaluacionTaller getIdeCpevt() {
        return ideCpevt;
    }

    public void setIdeCpevt(CppEvaluacionTaller ideCpevt) {
        this.ideCpevt = ideCpevt;
    }

    public CppEquivalenciaEvento getIdeCpeqe() {
        return ideCpeqe;
    }

    public void setIdeCpeqe(CppEquivalenciaEvento ideCpeqe) {
        this.ideCpeqe = ideCpeqe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpdet != null ? ideCpdet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppDetalleEvalTaller)) {
            return false;
        }
        CppDetalleEvalTaller other = (CppDetalleEvalTaller) object;
        if ((this.ideCpdet == null && other.ideCpdet != null) || (this.ideCpdet != null && !this.ideCpdet.equals(other.ideCpdet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppDetalleEvalTaller[ ideCpdet=" + ideCpdet + " ]";
    }
    
}
