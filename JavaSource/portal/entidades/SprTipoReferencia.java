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
@Table(name = "spr_tipo_referencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprTipoReferencia.findAll", query = "SELECT s FROM SprTipoReferencia s"),
    @NamedQuery(name = "SprTipoReferencia.findByIdeSptir", query = "SELECT s FROM SprTipoReferencia s WHERE s.ideSptir = :ideSptir"),
    @NamedQuery(name = "SprTipoReferencia.findByDetalleSptir", query = "SELECT s FROM SprTipoReferencia s WHERE s.detalleSptir = :detalleSptir"),
    @NamedQuery(name = "SprTipoReferencia.findByActivoSpest", query = "SELECT s FROM SprTipoReferencia s WHERE s.activoSpest = :activoSpest"),
    @NamedQuery(name = "SprTipoReferencia.findByUsuarioIngre", query = "SELECT s FROM SprTipoReferencia s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprTipoReferencia.findByFechaIngre", query = "SELECT s FROM SprTipoReferencia s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprTipoReferencia.findByHoraIngre", query = "SELECT s FROM SprTipoReferencia s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprTipoReferencia.findByUsuarioActua", query = "SELECT s FROM SprTipoReferencia s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprTipoReferencia.findByFechaActua", query = "SELECT s FROM SprTipoReferencia s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprTipoReferencia.findByHoraActua", query = "SELECT s FROM SprTipoReferencia s WHERE s.horaActua = :horaActua")})
public class SprTipoReferencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sptir", nullable = false)
    private Integer ideSptir;
    @Size(max = 100)
    @Column(name = "detalle_sptir", length = 100)
    private String detalleSptir;
    @Column(name = "activo_spest")
    private Boolean activoSpest;
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
    @OneToMany(mappedBy = "ideSptir")
    private List<SprReferencias> sprReferenciasList;

    public SprTipoReferencia() {
    }

    public SprTipoReferencia(Integer ideSptir) {
        this.ideSptir = ideSptir;
    }

    public Integer getIdeSptir() {
        return ideSptir;
    }

    public void setIdeSptir(Integer ideSptir) {
        this.ideSptir = ideSptir;
    }

    public String getDetalleSptir() {
        return detalleSptir;
    }

    public void setDetalleSptir(String detalleSptir) {
        this.detalleSptir = detalleSptir;
    }

    public Boolean getActivoSpest() {
        return activoSpest;
    }

    public void setActivoSpest(Boolean activoSpest) {
        this.activoSpest = activoSpest;
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

    public List<SprReferencias> getSprReferenciasList() {
        return sprReferenciasList;
    }

    public void setSprReferenciasList(List<SprReferencias> sprReferenciasList) {
        this.sprReferenciasList = sprReferenciasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSptir != null ? ideSptir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprTipoReferencia)) {
            return false;
        }
        SprTipoReferencia other = (SprTipoReferencia) object;
        if ((this.ideSptir == null && other.ideSptir != null) || (this.ideSptir != null && !this.ideSptir.equals(other.ideSptir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprTipoReferencia[ ideSptir=" + ideSptir + " ]";
    }
    
}
