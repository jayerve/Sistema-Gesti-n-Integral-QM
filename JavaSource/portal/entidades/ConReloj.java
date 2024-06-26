/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "con_reloj", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ConReloj.findAll", query = "SELECT c FROM ConReloj c"),
    @NamedQuery(name = "ConReloj.findByIdeCorel", query = "SELECT c FROM ConReloj c WHERE c.ideCorel = :ideCorel"),
    @NamedQuery(name = "ConReloj.findByIdeSucu", query = "SELECT c FROM ConReloj c WHERE c.ideSucu = :ideSucu"),
    @NamedQuery(name = "ConReloj.findByDetalleCorel", query = "SELECT c FROM ConReloj c WHERE c.detalleCorel = :detalleCorel"),
    @NamedQuery(name = "ConReloj.findByUbicacionCorel", query = "SELECT c FROM ConReloj c WHERE c.ubicacionCorel = :ubicacionCorel"),
    @NamedQuery(name = "ConReloj.findByActivoCorel", query = "SELECT c FROM ConReloj c WHERE c.activoCorel = :activoCorel"),
    @NamedQuery(name = "ConReloj.findByUsuarioIngre", query = "SELECT c FROM ConReloj c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ConReloj.findByFechaIngre", query = "SELECT c FROM ConReloj c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ConReloj.findByUsuarioActua", query = "SELECT c FROM ConReloj c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ConReloj.findByFechaActua", query = "SELECT c FROM ConReloj c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ConReloj.findByHoraIngre", query = "SELECT c FROM ConReloj c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ConReloj.findByHoraActua", query = "SELECT c FROM ConReloj c WHERE c.horaActua = :horaActua")})
public class ConReloj implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_corel", nullable = false)
    private Integer ideCorel;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Size(max = 50)
    @Column(name = "detalle_corel", length = 50)
    private String detalleCorel;
    @Size(max = 200)
    @Column(name = "ubicacion_corel", length = 200)
    private String ubicacionCorel;
    @Column(name = "activo_corel")
    private Boolean activoCorel;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideCorel")
    private List<ConBiometricoMarcaciones> conBiometricoMarcacionesList;

    public ConReloj() {
    }

    public ConReloj(Integer ideCorel) {
        this.ideCorel = ideCorel;
    }

    public Integer getIdeCorel() {
        return ideCorel;
    }

    public void setIdeCorel(Integer ideCorel) {
        this.ideCorel = ideCorel;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public String getDetalleCorel() {
        return detalleCorel;
    }

    public void setDetalleCorel(String detalleCorel) {
        this.detalleCorel = detalleCorel;
    }

    public String getUbicacionCorel() {
        return ubicacionCorel;
    }

    public void setUbicacionCorel(String ubicacionCorel) {
        this.ubicacionCorel = ubicacionCorel;
    }

    public Boolean getActivoCorel() {
        return activoCorel;
    }

    public void setActivoCorel(Boolean activoCorel) {
        this.activoCorel = activoCorel;
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

    public List<ConBiometricoMarcaciones> getConBiometricoMarcacionesList() {
        return conBiometricoMarcacionesList;
    }

    public void setConBiometricoMarcacionesList(List<ConBiometricoMarcaciones> conBiometricoMarcacionesList) {
        this.conBiometricoMarcacionesList = conBiometricoMarcacionesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCorel != null ? ideCorel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConReloj)) {
            return false;
        }
        ConReloj other = (ConReloj) object;
        if ((this.ideCorel == null && other.ideCorel != null) || (this.ideCorel != null && !this.ideCorel.equals(other.ideCorel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ConReloj[ ideCorel=" + ideCorel + " ]";
    }
    
}
