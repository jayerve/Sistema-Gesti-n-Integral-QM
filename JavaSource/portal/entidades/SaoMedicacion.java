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
@Table(name = "sao_medicacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoMedicacion.findAll", query = "SELECT s FROM SaoMedicacion s"),
    @NamedQuery(name = "SaoMedicacion.findByIdeSamed", query = "SELECT s FROM SaoMedicacion s WHERE s.ideSamed = :ideSamed"),
    @NamedQuery(name = "SaoMedicacion.findByDetalleSamed", query = "SELECT s FROM SaoMedicacion s WHERE s.detalleSamed = :detalleSamed"),
    @NamedQuery(name = "SaoMedicacion.findByActivoSamed", query = "SELECT s FROM SaoMedicacion s WHERE s.activoSamed = :activoSamed"),
    @NamedQuery(name = "SaoMedicacion.findByUsuarioIngre", query = "SELECT s FROM SaoMedicacion s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoMedicacion.findByFechaIngre", query = "SELECT s FROM SaoMedicacion s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoMedicacion.findByUsuarioActua", query = "SELECT s FROM SaoMedicacion s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoMedicacion.findByFechaActua", query = "SELECT s FROM SaoMedicacion s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoMedicacion.findByHoraIngre", query = "SELECT s FROM SaoMedicacion s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoMedicacion.findByHoraActua", query = "SELECT s FROM SaoMedicacion s WHERE s.horaActua = :horaActua")})
public class SaoMedicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_samed", nullable = false)
    private Integer ideSamed;
    @Size(max = 100)
    @Column(name = "detalle_samed", length = 100)
    private String detalleSamed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_samed", nullable = false)
    private boolean activoSamed;
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
    @OneToMany(mappedBy = "ideSamed")
    private List<SaoRecetaMedica> saoRecetaMedicaList;

    public SaoMedicacion() {
    }

    public SaoMedicacion(Integer ideSamed) {
        this.ideSamed = ideSamed;
    }

    public SaoMedicacion(Integer ideSamed, boolean activoSamed) {
        this.ideSamed = ideSamed;
        this.activoSamed = activoSamed;
    }

    public Integer getIdeSamed() {
        return ideSamed;
    }

    public void setIdeSamed(Integer ideSamed) {
        this.ideSamed = ideSamed;
    }

    public String getDetalleSamed() {
        return detalleSamed;
    }

    public void setDetalleSamed(String detalleSamed) {
        this.detalleSamed = detalleSamed;
    }

    public boolean getActivoSamed() {
        return activoSamed;
    }

    public void setActivoSamed(boolean activoSamed) {
        this.activoSamed = activoSamed;
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

    public List<SaoRecetaMedica> getSaoRecetaMedicaList() {
        return saoRecetaMedicaList;
    }

    public void setSaoRecetaMedicaList(List<SaoRecetaMedica> saoRecetaMedicaList) {
        this.saoRecetaMedicaList = saoRecetaMedicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSamed != null ? ideSamed.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoMedicacion)) {
            return false;
        }
        SaoMedicacion other = (SaoMedicacion) object;
        if ((this.ideSamed == null && other.ideSamed != null) || (this.ideSamed != null && !this.ideSamed.equals(other.ideSamed))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoMedicacion[ ideSamed=" + ideSamed + " ]";
    }
    
}
