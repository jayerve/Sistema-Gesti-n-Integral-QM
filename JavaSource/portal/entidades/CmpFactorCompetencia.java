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
@Table(name = "cmp_factor_competencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CmpFactorCompetencia.findAll", query = "SELECT c FROM CmpFactorCompetencia c"),
    @NamedQuery(name = "CmpFactorCompetencia.findByIdeCmfac", query = "SELECT c FROM CmpFactorCompetencia c WHERE c.ideCmfac = :ideCmfac"),
    @NamedQuery(name = "CmpFactorCompetencia.findByDetalleCmfac", query = "SELECT c FROM CmpFactorCompetencia c WHERE c.detalleCmfac = :detalleCmfac"),
    @NamedQuery(name = "CmpFactorCompetencia.findByActivoCmfac", query = "SELECT c FROM CmpFactorCompetencia c WHERE c.activoCmfac = :activoCmfac"),
    @NamedQuery(name = "CmpFactorCompetencia.findByUsuarioIngre", query = "SELECT c FROM CmpFactorCompetencia c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CmpFactorCompetencia.findByFechaIngre", query = "SELECT c FROM CmpFactorCompetencia c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CmpFactorCompetencia.findByUsuarioActua", query = "SELECT c FROM CmpFactorCompetencia c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CmpFactorCompetencia.findByFechaActua", query = "SELECT c FROM CmpFactorCompetencia c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CmpFactorCompetencia.findByHoraIngre", query = "SELECT c FROM CmpFactorCompetencia c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CmpFactorCompetencia.findByHoraActua", query = "SELECT c FROM CmpFactorCompetencia c WHERE c.horaActua = :horaActua")})
public class CmpFactorCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cmfac", nullable = false)
    private Integer ideCmfac;
    @Size(max = 4000)
    @Column(name = "detalle_cmfac", length = 4000)
    private String detalleCmfac;
    @Column(name = "activo_cmfac")
    private Boolean activoCmfac;
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
    @OneToMany(mappedBy = "ideCmfac")
    private List<CmpDetalleCompetencia> cmpDetalleCompetenciaList;
    @JoinColumn(name = "ide_spfac", referencedColumnName = "ide_spfac")
    @ManyToOne
    private SprFactor ideSpfac;
    @JoinColumn(name = "ide_evfae", referencedColumnName = "ide_evfae")
    @ManyToOne
    private EvlFactorEvaluacion ideEvfae;
    @OneToMany(mappedBy = "cmpIdeCmfac")
    private List<CmpFactorCompetencia> cmpFactorCompetenciaList;
    @JoinColumn(name = "cmp_ide_cmfac", referencedColumnName = "ide_cmfac")
    @ManyToOne
    private CmpFactorCompetencia cmpIdeCmfac;

    public CmpFactorCompetencia() {
    }

    public CmpFactorCompetencia(Integer ideCmfac) {
        this.ideCmfac = ideCmfac;
    }

    public Integer getIdeCmfac() {
        return ideCmfac;
    }

    public void setIdeCmfac(Integer ideCmfac) {
        this.ideCmfac = ideCmfac;
    }

    public String getDetalleCmfac() {
        return detalleCmfac;
    }

    public void setDetalleCmfac(String detalleCmfac) {
        this.detalleCmfac = detalleCmfac;
    }

    public Boolean getActivoCmfac() {
        return activoCmfac;
    }

    public void setActivoCmfac(Boolean activoCmfac) {
        this.activoCmfac = activoCmfac;
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

    public List<CmpDetalleCompetencia> getCmpDetalleCompetenciaList() {
        return cmpDetalleCompetenciaList;
    }

    public void setCmpDetalleCompetenciaList(List<CmpDetalleCompetencia> cmpDetalleCompetenciaList) {
        this.cmpDetalleCompetenciaList = cmpDetalleCompetenciaList;
    }

    public SprFactor getIdeSpfac() {
        return ideSpfac;
    }

    public void setIdeSpfac(SprFactor ideSpfac) {
        this.ideSpfac = ideSpfac;
    }

    public EvlFactorEvaluacion getIdeEvfae() {
        return ideEvfae;
    }

    public void setIdeEvfae(EvlFactorEvaluacion ideEvfae) {
        this.ideEvfae = ideEvfae;
    }

    public List<CmpFactorCompetencia> getCmpFactorCompetenciaList() {
        return cmpFactorCompetenciaList;
    }

    public void setCmpFactorCompetenciaList(List<CmpFactorCompetencia> cmpFactorCompetenciaList) {
        this.cmpFactorCompetenciaList = cmpFactorCompetenciaList;
    }

    public CmpFactorCompetencia getCmpIdeCmfac() {
        return cmpIdeCmfac;
    }

    public void setCmpIdeCmfac(CmpFactorCompetencia cmpIdeCmfac) {
        this.cmpIdeCmfac = cmpIdeCmfac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCmfac != null ? ideCmfac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CmpFactorCompetencia)) {
            return false;
        }
        CmpFactorCompetencia other = (CmpFactorCompetencia) object;
        if ((this.ideCmfac == null && other.ideCmfac != null) || (this.ideCmfac != null && !this.ideCmfac.equals(other.ideCmfac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CmpFactorCompetencia[ ideCmfac=" + ideCmfac + " ]";
    }
    
}
