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
@Table(name = "sao_tipo_consulta", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoTipoConsulta.findAll", query = "SELECT s FROM SaoTipoConsulta s"),
    @NamedQuery(name = "SaoTipoConsulta.findByIdeSatic", query = "SELECT s FROM SaoTipoConsulta s WHERE s.ideSatic = :ideSatic"),
    @NamedQuery(name = "SaoTipoConsulta.findByDetalleSatic", query = "SELECT s FROM SaoTipoConsulta s WHERE s.detalleSatic = :detalleSatic"),
    @NamedQuery(name = "SaoTipoConsulta.findByActivoSatic", query = "SELECT s FROM SaoTipoConsulta s WHERE s.activoSatic = :activoSatic"),
    @NamedQuery(name = "SaoTipoConsulta.findByUsuarioIngre", query = "SELECT s FROM SaoTipoConsulta s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoTipoConsulta.findByFechaIngre", query = "SELECT s FROM SaoTipoConsulta s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoTipoConsulta.findByUsuarioActua", query = "SELECT s FROM SaoTipoConsulta s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoTipoConsulta.findByFechaActua", query = "SELECT s FROM SaoTipoConsulta s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoTipoConsulta.findByHoraIngre", query = "SELECT s FROM SaoTipoConsulta s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoTipoConsulta.findByHoraActua", query = "SELECT s FROM SaoTipoConsulta s WHERE s.horaActua = :horaActua")})
public class SaoTipoConsulta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_satic", nullable = false)
    private Integer ideSatic;
    @Size(max = 100)
    @Column(name = "detalle_satic", length = 100)
    private String detalleSatic;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_satic", nullable = false)
    private boolean activoSatic;
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
    @OneToMany(mappedBy = "ideSatic")
    private List<SaoFichaMedica> saoFichaMedicaList;

    public SaoTipoConsulta() {
    }

    public SaoTipoConsulta(Integer ideSatic) {
        this.ideSatic = ideSatic;
    }

    public SaoTipoConsulta(Integer ideSatic, boolean activoSatic) {
        this.ideSatic = ideSatic;
        this.activoSatic = activoSatic;
    }

    public Integer getIdeSatic() {
        return ideSatic;
    }

    public void setIdeSatic(Integer ideSatic) {
        this.ideSatic = ideSatic;
    }

    public String getDetalleSatic() {
        return detalleSatic;
    }

    public void setDetalleSatic(String detalleSatic) {
        this.detalleSatic = detalleSatic;
    }

    public boolean getActivoSatic() {
        return activoSatic;
    }

    public void setActivoSatic(boolean activoSatic) {
        this.activoSatic = activoSatic;
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

    public List<SaoFichaMedica> getSaoFichaMedicaList() {
        return saoFichaMedicaList;
    }

    public void setSaoFichaMedicaList(List<SaoFichaMedica> saoFichaMedicaList) {
        this.saoFichaMedicaList = saoFichaMedicaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSatic != null ? ideSatic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoTipoConsulta)) {
            return false;
        }
        SaoTipoConsulta other = (SaoTipoConsulta) object;
        if ((this.ideSatic == null && other.ideSatic != null) || (this.ideSatic != null && !this.ideSatic.equals(other.ideSatic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoTipoConsulta[ ideSatic=" + ideSatic + " ]";
    }
    
}
