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
@Table(name = "sao_color", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoColor.findAll", query = "SELECT s FROM SaoColor s"),
    @NamedQuery(name = "SaoColor.findByIdeSacol", query = "SELECT s FROM SaoColor s WHERE s.ideSacol = :ideSacol"),
    @NamedQuery(name = "SaoColor.findByDetalleSacol", query = "SELECT s FROM SaoColor s WHERE s.detalleSacol = :detalleSacol"),
    @NamedQuery(name = "SaoColor.findByActivoSacol", query = "SELECT s FROM SaoColor s WHERE s.activoSacol = :activoSacol"),
    @NamedQuery(name = "SaoColor.findByUsuarioIngre", query = "SELECT s FROM SaoColor s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoColor.findByFechaIngre", query = "SELECT s FROM SaoColor s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoColor.findByHoraIngre", query = "SELECT s FROM SaoColor s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoColor.findByUsuarioActua", query = "SELECT s FROM SaoColor s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoColor.findByFechaActua", query = "SELECT s FROM SaoColor s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoColor.findByHoraActua", query = "SELECT s FROM SaoColor s WHERE s.horaActua = :horaActua")})
public class SaoColor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sacol", nullable = false)
    private Integer ideSacol;
    @Size(max = 100)
    @Column(name = "detalle_sacol", length = 100)
    private String detalleSacol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sacol", nullable = false)
    private boolean activoSacol;
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
    @OneToMany(mappedBy = "ideSacol")
    private List<SaoDotacionUniforme> saoDotacionUniformeList;

    public SaoColor() {
    }

    public SaoColor(Integer ideSacol) {
        this.ideSacol = ideSacol;
    }

    public SaoColor(Integer ideSacol, boolean activoSacol) {
        this.ideSacol = ideSacol;
        this.activoSacol = activoSacol;
    }

    public Integer getIdeSacol() {
        return ideSacol;
    }

    public void setIdeSacol(Integer ideSacol) {
        this.ideSacol = ideSacol;
    }

    public String getDetalleSacol() {
        return detalleSacol;
    }

    public void setDetalleSacol(String detalleSacol) {
        this.detalleSacol = detalleSacol;
    }

    public boolean getActivoSacol() {
        return activoSacol;
    }

    public void setActivoSacol(boolean activoSacol) {
        this.activoSacol = activoSacol;
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

    public List<SaoDotacionUniforme> getSaoDotacionUniformeList() {
        return saoDotacionUniformeList;
    }

    public void setSaoDotacionUniformeList(List<SaoDotacionUniforme> saoDotacionUniformeList) {
        this.saoDotacionUniformeList = saoDotacionUniformeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSacol != null ? ideSacol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoColor)) {
            return false;
        }
        SaoColor other = (SaoColor) object;
        if ((this.ideSacol == null && other.ideSacol != null) || (this.ideSacol != null && !this.ideSacol.equals(other.ideSacol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoColor[ ideSacol=" + ideSacol + " ]";
    }
    
}
