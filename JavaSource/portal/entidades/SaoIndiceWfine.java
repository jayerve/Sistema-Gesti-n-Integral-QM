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
@Table(name = "sao_indice_wfine", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoIndiceWfine.findAll", query = "SELECT s FROM SaoIndiceWfine s"),
    @NamedQuery(name = "SaoIndiceWfine.findByIdeSainw", query = "SELECT s FROM SaoIndiceWfine s WHERE s.ideSainw = :ideSainw"),
    @NamedQuery(name = "SaoIndiceWfine.findByInterpretacionSainw", query = "SELECT s FROM SaoIndiceWfine s WHERE s.interpretacionSainw = :interpretacionSainw"),
    @NamedQuery(name = "SaoIndiceWfine.findByRangoIniSainw", query = "SELECT s FROM SaoIndiceWfine s WHERE s.rangoIniSainw = :rangoIniSainw"),
    @NamedQuery(name = "SaoIndiceWfine.findByRangoFinSainw", query = "SELECT s FROM SaoIndiceWfine s WHERE s.rangoFinSainw = :rangoFinSainw"),
    @NamedQuery(name = "SaoIndiceWfine.findByActivoSainw", query = "SELECT s FROM SaoIndiceWfine s WHERE s.activoSainw = :activoSainw"),
    @NamedQuery(name = "SaoIndiceWfine.findByUsuarioIngre", query = "SELECT s FROM SaoIndiceWfine s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoIndiceWfine.findByFechaIngre", query = "SELECT s FROM SaoIndiceWfine s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoIndiceWfine.findByHoraIngre", query = "SELECT s FROM SaoIndiceWfine s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoIndiceWfine.findByUsuarioActua", query = "SELECT s FROM SaoIndiceWfine s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoIndiceWfine.findByFechaActua", query = "SELECT s FROM SaoIndiceWfine s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoIndiceWfine.findByHoraActua", query = "SELECT s FROM SaoIndiceWfine s WHERE s.horaActua = :horaActua")})
public class SaoIndiceWfine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sainw", nullable = false)
    private Integer ideSainw;
    @Size(max = 50)
    @Column(name = "interpretacion_sainw", length = 50)
    private String interpretacionSainw;
    @Column(name = "rango_ini_sainw")
    private Integer rangoIniSainw;
    @Column(name = "rango_fin_sainw")
    private Integer rangoFinSainw;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sainw", nullable = false)
    private boolean activoSainw;
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
    @OneToMany(mappedBy = "ideSainw")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList;

    public SaoIndiceWfine() {
    }

    public SaoIndiceWfine(Integer ideSainw) {
        this.ideSainw = ideSainw;
    }

    public SaoIndiceWfine(Integer ideSainw, boolean activoSainw) {
        this.ideSainw = ideSainw;
        this.activoSainw = activoSainw;
    }

    public Integer getIdeSainw() {
        return ideSainw;
    }

    public void setIdeSainw(Integer ideSainw) {
        this.ideSainw = ideSainw;
    }

    public String getInterpretacionSainw() {
        return interpretacionSainw;
    }

    public void setInterpretacionSainw(String interpretacionSainw) {
        this.interpretacionSainw = interpretacionSainw;
    }

    public Integer getRangoIniSainw() {
        return rangoIniSainw;
    }

    public void setRangoIniSainw(Integer rangoIniSainw) {
        this.rangoIniSainw = rangoIniSainw;
    }

    public Integer getRangoFinSainw() {
        return rangoFinSainw;
    }

    public void setRangoFinSainw(Integer rangoFinSainw) {
        this.rangoFinSainw = rangoFinSainw;
    }

    public boolean getActivoSainw() {
        return activoSainw;
    }

    public void setActivoSainw(boolean activoSainw) {
        this.activoSainw = activoSainw;
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

    public List<SaoDetalleMatrizRiesgo> getSaoDetalleMatrizRiesgoList() {
        return saoDetalleMatrizRiesgoList;
    }

    public void setSaoDetalleMatrizRiesgoList(List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList) {
        this.saoDetalleMatrizRiesgoList = saoDetalleMatrizRiesgoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSainw != null ? ideSainw.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoIndiceWfine)) {
            return false;
        }
        SaoIndiceWfine other = (SaoIndiceWfine) object;
        if ((this.ideSainw == null && other.ideSainw != null) || (this.ideSainw != null && !this.ideSainw.equals(other.ideSainw))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoIndiceWfine[ ideSainw=" + ideSainw + " ]";
    }
    
}
