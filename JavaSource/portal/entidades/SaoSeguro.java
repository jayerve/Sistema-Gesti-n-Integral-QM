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
@Table(name = "sao_seguro", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoSeguro.findAll", query = "SELECT s FROM SaoSeguro s"),
    @NamedQuery(name = "SaoSeguro.findByIdeSaseg", query = "SELECT s FROM SaoSeguro s WHERE s.ideSaseg = :ideSaseg"),
    @NamedQuery(name = "SaoSeguro.findByDetalleSaseg", query = "SELECT s FROM SaoSeguro s WHERE s.detalleSaseg = :detalleSaseg"),
    @NamedQuery(name = "SaoSeguro.findByActivoSaseg", query = "SELECT s FROM SaoSeguro s WHERE s.activoSaseg = :activoSaseg"),
    @NamedQuery(name = "SaoSeguro.findByUsuarioIngre", query = "SELECT s FROM SaoSeguro s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoSeguro.findByFechaIngre", query = "SELECT s FROM SaoSeguro s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoSeguro.findByHoraIngre", query = "SELECT s FROM SaoSeguro s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoSeguro.findByUsuarioActua", query = "SELECT s FROM SaoSeguro s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoSeguro.findByFechaActua", query = "SELECT s FROM SaoSeguro s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoSeguro.findByHoraActua", query = "SELECT s FROM SaoSeguro s WHERE s.horaActua = :horaActua")})
public class SaoSeguro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_saseg", nullable = false)
    private Integer ideSaseg;
    @Size(max = 100)
    @Column(name = "detalle_saseg", length = 100)
    private String detalleSaseg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_saseg", nullable = false)
    private boolean activoSaseg;
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
    @OneToMany(mappedBy = "ideSaseg")
    private List<SaoBienes> saoBienesList;

    public SaoSeguro() {
    }

    public SaoSeguro(Integer ideSaseg) {
        this.ideSaseg = ideSaseg;
    }

    public SaoSeguro(Integer ideSaseg, boolean activoSaseg) {
        this.ideSaseg = ideSaseg;
        this.activoSaseg = activoSaseg;
    }

    public Integer getIdeSaseg() {
        return ideSaseg;
    }

    public void setIdeSaseg(Integer ideSaseg) {
        this.ideSaseg = ideSaseg;
    }

    public String getDetalleSaseg() {
        return detalleSaseg;
    }

    public void setDetalleSaseg(String detalleSaseg) {
        this.detalleSaseg = detalleSaseg;
    }

    public boolean getActivoSaseg() {
        return activoSaseg;
    }

    public void setActivoSaseg(boolean activoSaseg) {
        this.activoSaseg = activoSaseg;
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
        hash += (ideSaseg != null ? ideSaseg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoSeguro)) {
            return false;
        }
        SaoSeguro other = (SaoSeguro) object;
        if ((this.ideSaseg == null && other.ideSaseg != null) || (this.ideSaseg != null && !this.ideSaseg.equals(other.ideSaseg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoSeguro[ ideSaseg=" + ideSaseg + " ]";
    }
    
}
