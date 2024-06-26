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
@Table(name = "sao_estado", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoEstado.findAll", query = "SELECT s FROM SaoEstado s"),
    @NamedQuery(name = "SaoEstado.findByIdeSaest", query = "SELECT s FROM SaoEstado s WHERE s.ideSaest = :ideSaest"),
    @NamedQuery(name = "SaoEstado.findByDetalleSaest", query = "SELECT s FROM SaoEstado s WHERE s.detalleSaest = :detalleSaest"),
    @NamedQuery(name = "SaoEstado.findByActivoSaest", query = "SELECT s FROM SaoEstado s WHERE s.activoSaest = :activoSaest"),
    @NamedQuery(name = "SaoEstado.findByUsuarioIngre", query = "SELECT s FROM SaoEstado s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoEstado.findByFechaIngre", query = "SELECT s FROM SaoEstado s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoEstado.findByHoraIngre", query = "SELECT s FROM SaoEstado s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoEstado.findByUsuarioActua", query = "SELECT s FROM SaoEstado s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoEstado.findByFechaActua", query = "SELECT s FROM SaoEstado s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoEstado.findByHoraActua", query = "SELECT s FROM SaoEstado s WHERE s.horaActua = :horaActua")})
public class SaoEstado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_saest", nullable = false)
    private Integer ideSaest;
    @Size(max = 100)
    @Column(name = "detalle_saest", length = 100)
    private String detalleSaest;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_saest", nullable = false)
    private boolean activoSaest;
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
    @OneToMany(mappedBy = "ideSaest")
    private List<SaoBienes> saoBienesList;

    public SaoEstado() {
    }

    public SaoEstado(Integer ideSaest) {
        this.ideSaest = ideSaest;
    }

    public SaoEstado(Integer ideSaest, boolean activoSaest) {
        this.ideSaest = ideSaest;
        this.activoSaest = activoSaest;
    }

    public Integer getIdeSaest() {
        return ideSaest;
    }

    public void setIdeSaest(Integer ideSaest) {
        this.ideSaest = ideSaest;
    }

    public String getDetalleSaest() {
        return detalleSaest;
    }

    public void setDetalleSaest(String detalleSaest) {
        this.detalleSaest = detalleSaest;
    }

    public boolean getActivoSaest() {
        return activoSaest;
    }

    public void setActivoSaest(boolean activoSaest) {
        this.activoSaest = activoSaest;
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

    public List<SaoBienes> getSaoBienesList() {
        return saoBienesList;
    }

    public void setSaoBienesList(List<SaoBienes> saoBienesList) {
        this.saoBienesList = saoBienesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSaest != null ? ideSaest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoEstado)) {
            return false;
        }
        SaoEstado other = (SaoEstado) object;
        if ((this.ideSaest == null && other.ideSaest != null) || (this.ideSaest != null && !this.ideSaest.equals(other.ideSaest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoEstado[ ideSaest=" + ideSaest + " ]";
    }
    
}
