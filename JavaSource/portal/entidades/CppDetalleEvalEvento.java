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
@Table(name = "cpp_detalle_eval_evento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppDetalleEvalEvento.findAll", query = "SELECT c FROM CppDetalleEvalEvento c"),
    @NamedQuery(name = "CppDetalleEvalEvento.findByIdeCpdee", query = "SELECT c FROM CppDetalleEvalEvento c WHERE c.ideCpdee = :ideCpdee"),
    @NamedQuery(name = "CppDetalleEvalEvento.findByActivoCpdee", query = "SELECT c FROM CppDetalleEvalEvento c WHERE c.activoCpdee = :activoCpdee"),
    @NamedQuery(name = "CppDetalleEvalEvento.findByUsuarioIngre", query = "SELECT c FROM CppDetalleEvalEvento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppDetalleEvalEvento.findByFechaIngre", query = "SELECT c FROM CppDetalleEvalEvento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppDetalleEvalEvento.findByHoraIngre", query = "SELECT c FROM CppDetalleEvalEvento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppDetalleEvalEvento.findByUsuarioActua", query = "SELECT c FROM CppDetalleEvalEvento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppDetalleEvalEvento.findByFechaActua", query = "SELECT c FROM CppDetalleEvalEvento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppDetalleEvalEvento.findByHoraActua", query = "SELECT c FROM CppDetalleEvalEvento c WHERE c.horaActua = :horaActua")})
public class CppDetalleEvalEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpdee", nullable = false)
    private Integer ideCpdee;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpdee", nullable = false)
    private boolean activoCpdee;
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
    @JoinColumn(name = "ide_cpmoe", referencedColumnName = "ide_cpmoe")
    @ManyToOne
    private CppMotivoEvaluacion ideCpmoe;
    @JoinColumn(name = "ide_cpeve", referencedColumnName = "ide_cpeve")
    @ManyToOne
    private CppEvaluacionEvento ideCpeve;
    @JoinColumn(name = "ide_cpeqe", referencedColumnName = "ide_cpeqe")
    @ManyToOne
    private CppEquivalenciaEvento ideCpeqe;

    public CppDetalleEvalEvento() {
    }

    public CppDetalleEvalEvento(Integer ideCpdee) {
        this.ideCpdee = ideCpdee;
    }

    public CppDetalleEvalEvento(Integer ideCpdee, boolean activoCpdee) {
        this.ideCpdee = ideCpdee;
        this.activoCpdee = activoCpdee;
    }

    public Integer getIdeCpdee() {
        return ideCpdee;
    }

    public void setIdeCpdee(Integer ideCpdee) {
        this.ideCpdee = ideCpdee;
    }

    public boolean getActivoCpdee() {
        return activoCpdee;
    }

    public void setActivoCpdee(boolean activoCpdee) {
        this.activoCpdee = activoCpdee;
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

    public CppMotivoEvaluacion getIdeCpmoe() {
        return ideCpmoe;
    }

    public void setIdeCpmoe(CppMotivoEvaluacion ideCpmoe) {
        this.ideCpmoe = ideCpmoe;
    }

    public CppEvaluacionEvento getIdeCpeve() {
        return ideCpeve;
    }

    public void setIdeCpeve(CppEvaluacionEvento ideCpeve) {
        this.ideCpeve = ideCpeve;
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
        hash += (ideCpdee != null ? ideCpdee.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppDetalleEvalEvento)) {
            return false;
        }
        CppDetalleEvalEvento other = (CppDetalleEvalEvento) object;
        if ((this.ideCpdee == null && other.ideCpdee != null) || (this.ideCpdee != null && !this.ideCpdee.equals(other.ideCpdee))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppDetalleEvalEvento[ ideCpdee=" + ideCpdee + " ]";
    }
    
}
