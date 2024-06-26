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
@Table(name = "sao_propiedad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoPropiedad.findAll", query = "SELECT s FROM SaoPropiedad s"),
    @NamedQuery(name = "SaoPropiedad.findByIdeSapro", query = "SELECT s FROM SaoPropiedad s WHERE s.ideSapro = :ideSapro"),
    @NamedQuery(name = "SaoPropiedad.findByDetalleSapro", query = "SELECT s FROM SaoPropiedad s WHERE s.detalleSapro = :detalleSapro"),
    @NamedQuery(name = "SaoPropiedad.findByActivoSapro", query = "SELECT s FROM SaoPropiedad s WHERE s.activoSapro = :activoSapro"),
    @NamedQuery(name = "SaoPropiedad.findByUsuarioIngre", query = "SELECT s FROM SaoPropiedad s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoPropiedad.findByFechaIngre", query = "SELECT s FROM SaoPropiedad s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoPropiedad.findByHoraIngre", query = "SELECT s FROM SaoPropiedad s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoPropiedad.findByUsuarioActua", query = "SELECT s FROM SaoPropiedad s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoPropiedad.findByFechaActua", query = "SELECT s FROM SaoPropiedad s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoPropiedad.findByHoraActua", query = "SELECT s FROM SaoPropiedad s WHERE s.horaActua = :horaActua")})
public class SaoPropiedad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sapro", nullable = false)
    private Integer ideSapro;
    @Size(max = 100)
    @Column(name = "detalle_sapro", length = 100)
    private String detalleSapro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sapro", nullable = false)
    private boolean activoSapro;
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
    @OneToMany(mappedBy = "ideSapro")
    private List<SaoBienes> saoBienesList;

    public SaoPropiedad() {
    }

    public SaoPropiedad(Integer ideSapro) {
        this.ideSapro = ideSapro;
    }

    public SaoPropiedad(Integer ideSapro, boolean activoSapro) {
        this.ideSapro = ideSapro;
        this.activoSapro = activoSapro;
    }

    public Integer getIdeSapro() {
        return ideSapro;
    }

    public void setIdeSapro(Integer ideSapro) {
        this.ideSapro = ideSapro;
    }

    public String getDetalleSapro() {
        return detalleSapro;
    }

    public void setDetalleSapro(String detalleSapro) {
        this.detalleSapro = detalleSapro;
    }

    public boolean getActivoSapro() {
        return activoSapro;
    }

    public void setActivoSapro(boolean activoSapro) {
        this.activoSapro = activoSapro;
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
        hash += (ideSapro != null ? ideSapro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoPropiedad)) {
            return false;
        }
        SaoPropiedad other = (SaoPropiedad) object;
        if ((this.ideSapro == null && other.ideSapro != null) || (this.ideSapro != null && !this.ideSapro.equals(other.ideSapro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoPropiedad[ ideSapro=" + ideSapro + " ]";
    }
    
}
