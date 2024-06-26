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
@Table(name = "bodt_destino", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "BodtDestino.findAll", query = "SELECT b FROM BodtDestino b"),
    @NamedQuery(name = "BodtDestino.findByIdeBodes", query = "SELECT b FROM BodtDestino b WHERE b.ideBodes = :ideBodes"),
    @NamedQuery(name = "BodtDestino.findByDetalleBodes", query = "SELECT b FROM BodtDestino b WHERE b.detalleBodes = :detalleBodes"),
    @NamedQuery(name = "BodtDestino.findByActivoBodes", query = "SELECT b FROM BodtDestino b WHERE b.activoBodes = :activoBodes"),
    @NamedQuery(name = "BodtDestino.findByUsuarioIngre", query = "SELECT b FROM BodtDestino b WHERE b.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "BodtDestino.findByFechaIngre", query = "SELECT b FROM BodtDestino b WHERE b.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "BodtDestino.findByHoraIngre", query = "SELECT b FROM BodtDestino b WHERE b.horaIngre = :horaIngre"),
    @NamedQuery(name = "BodtDestino.findByUsuarioActua", query = "SELECT b FROM BodtDestino b WHERE b.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "BodtDestino.findByFechaActua", query = "SELECT b FROM BodtDestino b WHERE b.fechaActua = :fechaActua"),
    @NamedQuery(name = "BodtDestino.findByHoraActua", query = "SELECT b FROM BodtDestino b WHERE b.horaActua = :horaActua")})
public class BodtDestino implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_bodes", nullable = false)
    private Long ideBodes;
    @Size(max = 250)
    @Column(name = "detalle_bodes", length = 250)
    private String detalleBodes;
    @Column(name = "activo_bodes")
    private Boolean activoBodes;
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
    @OneToMany(mappedBy = "ideBodes")
    private List<BodtConceptoEgreso> bodtConceptoEgresoList;

    public BodtDestino() {
    }

    public BodtDestino(Long ideBodes) {
        this.ideBodes = ideBodes;
    }

    public Long getIdeBodes() {
        return ideBodes;
    }

    public void setIdeBodes(Long ideBodes) {
        this.ideBodes = ideBodes;
    }

    public String getDetalleBodes() {
        return detalleBodes;
    }

    public void setDetalleBodes(String detalleBodes) {
        this.detalleBodes = detalleBodes;
    }

    public Boolean getActivoBodes() {
        return activoBodes;
    }

    public void setActivoBodes(Boolean activoBodes) {
        this.activoBodes = activoBodes;
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

    public List<BodtConceptoEgreso> getBodtConceptoEgresoList() {
        return bodtConceptoEgresoList;
    }

    public void setBodtConceptoEgresoList(List<BodtConceptoEgreso> bodtConceptoEgresoList) {
        this.bodtConceptoEgresoList = bodtConceptoEgresoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideBodes != null ? ideBodes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BodtDestino)) {
            return false;
        }
        BodtDestino other = (BodtDestino) object;
        if ((this.ideBodes == null && other.ideBodes != null) || (this.ideBodes != null && !this.ideBodes.equals(other.ideBodes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.BodtDestino[ ideBodes=" + ideBodes + " ]";
    }
    
}
