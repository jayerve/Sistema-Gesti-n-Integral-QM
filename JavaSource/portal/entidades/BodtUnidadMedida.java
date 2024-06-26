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
@Table(name = "bodt_unidad_medida", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtUnidadMedida.findAll", query = "SELECT b FROM BodtUnidadMedida b"),
    @NamedQuery(name = "BodtUnidadMedida.findByIdeBounm", query = "SELECT b FROM BodtUnidadMedida b WHERE b.ideBounm = :ideBounm"),
    @NamedQuery(name = "BodtUnidadMedida.findByDetalleBounm", query = "SELECT b FROM BodtUnidadMedida b WHERE b.detalleBounm = :detalleBounm"),
    @NamedQuery(name = "BodtUnidadMedida.findByActivoBounm", query = "SELECT b FROM BodtUnidadMedida b WHERE b.activoBounm = :activoBounm"),
    @NamedQuery(name = "BodtUnidadMedida.findByUsuarioIngre", query = "SELECT b FROM BodtUnidadMedida b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtUnidadMedida.findByFechaIngre", query = "SELECT b FROM BodtUnidadMedida b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtUnidadMedida.findByHoraIngre", query = "SELECT b FROM BodtUnidadMedida b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtUnidadMedida.findByUsuarioActua", query = "SELECT b FROM BodtUnidadMedida b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtUnidadMedida.findByFechaActua", query = "SELECT b FROM BodtUnidadMedida b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtUnidadMedida.findByHoraActua", query = "SELECT b FROM BodtUnidadMedida b WHERE b.horaActua = :horaActua"),
    @NamedQuery(name = "BodtUnidadMedida.findByAbreviaturaBounm", query = "SELECT b FROM BodtUnidadMedida b WHERE b.abreviaturaBounm = :abreviaturaBounm")})
public class BodtUnidadMedida implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_bounm", nullable = false)
    private Long ideBounm;
    @Size(max = 50)
    @Column(name = "detalle_bounm", length = 50)
    private String detalleBounm;
    @Column(name = "activo_bounm")
    private Boolean activoBounm;
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
    @Size(max = 20)
    @Column(name = "abreviatura_bounm", length = 20)
    private String abreviaturaBounm;
    @OneToMany(mappedBy = "ideBounm")
    private List<BodtMaterial> bodtMaterialList;
    @OneToMany(mappedBy = "ideBounm")
    private List<PrePac> prePacList;

    public BodtUnidadMedida() {
    }

    public BodtUnidadMedida(Long ideBounm) {
        this.ideBounm = ideBounm;
    }

    public Long getIdeBounm() {
        return ideBounm;
    }

    public void setIdeBounm(Long ideBounm) {
        this.ideBounm = ideBounm;
    }

    public String getDetalleBounm() {
        return detalleBounm;
    }

    public void setDetalleBounm(String detalleBounm) {
        this.detalleBounm = detalleBounm;
    }

    public Boolean getActivoBounm() {
        return activoBounm;
    }

    public void setActivoBounm(Boolean activoBounm) {
        this.activoBounm = activoBounm;
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

    public String getAbreviaturaBounm() {
        return abreviaturaBounm;
    }

    public void setAbreviaturaBounm(String abreviaturaBounm) {
        this.abreviaturaBounm = abreviaturaBounm;
    }

    public List<BodtMaterial> getBodtMaterialList() {
        return bodtMaterialList;
    }

    public void setBodtMaterialList(List<BodtMaterial> bodtMaterialList) {
        this.bodtMaterialList = bodtMaterialList;
    }

    public List<PrePac> getPrePacList() {
        return prePacList;
    }

    public void setPrePacList(List<PrePac> prePacList) {
        this.prePacList = prePacList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBounm != null ? ideBounm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtUnidadMedida)) {
            return false;
        }
        BodtUnidadMedida other = (BodtUnidadMedida) object;
        if ((this.ideBounm == null && other.ideBounm != null) || (this.ideBounm != null && !this.ideBounm.equals(other.ideBounm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtUnidadMedida[ ideBounm=" + ideBounm + " ]";
    }
    
}
