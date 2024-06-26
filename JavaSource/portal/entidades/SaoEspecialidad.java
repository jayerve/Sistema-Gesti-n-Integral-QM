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
@Table(name = "sao_especialidad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoEspecialidad.findAll", query = "SELECT s FROM SaoEspecialidad s"),
    @NamedQuery(name = "SaoEspecialidad.findByIdeSaesp", query = "SELECT s FROM SaoEspecialidad s WHERE s.ideSaesp = :ideSaesp"),
    @NamedQuery(name = "SaoEspecialidad.findByDetalleSaesp", query = "SELECT s FROM SaoEspecialidad s WHERE s.detalleSaesp = :detalleSaesp"),
    @NamedQuery(name = "SaoEspecialidad.findByActivoSaesp", query = "SELECT s FROM SaoEspecialidad s WHERE s.activoSaesp = :activoSaesp"),
    @NamedQuery(name = "SaoEspecialidad.findByUsuarioIngre", query = "SELECT s FROM SaoEspecialidad s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoEspecialidad.findByFechaIngre", query = "SELECT s FROM SaoEspecialidad s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoEspecialidad.findByUsuarioActua", query = "SELECT s FROM SaoEspecialidad s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoEspecialidad.findByFechaActua", query = "SELECT s FROM SaoEspecialidad s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoEspecialidad.findByHoraIngre", query = "SELECT s FROM SaoEspecialidad s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoEspecialidad.findByHoraActua", query = "SELECT s FROM SaoEspecialidad s WHERE s.horaActua = :horaActua")})
public class SaoEspecialidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_saesp", nullable = false)
    private Integer ideSaesp;
    @Size(max = 100)
    @Column(name = "detalle_saesp", length = 100)
    private String detalleSaesp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_saesp", nullable = false)
    private boolean activoSaesp;
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
    @OneToMany(mappedBy = "ideSaesp")
    private List<SaoFichaMedica> saoFichaMedicaList;

    public SaoEspecialidad() {
    }

    public SaoEspecialidad(Integer ideSaesp) {
        this.ideSaesp = ideSaesp;
    }

    public SaoEspecialidad(Integer ideSaesp, boolean activoSaesp) {
        this.ideSaesp = ideSaesp;
        this.activoSaesp = activoSaesp;
    }

    public Integer getIdeSaesp() {
        return ideSaesp;
    }

    public void setIdeSaesp(Integer ideSaesp) {
        this.ideSaesp = ideSaesp;
    }

    public String getDetalleSaesp() {
        return detalleSaesp;
    }

    public void setDetalleSaesp(String detalleSaesp) {
        this.detalleSaesp = detalleSaesp;
    }

    public boolean getActivoSaesp() {
        return activoSaesp;
    }

    public void setActivoSaesp(boolean activoSaesp) {
        this.activoSaesp = activoSaesp;
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
        hash += (ideSaesp != null ? ideSaesp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoEspecialidad)) {
            return false;
        }
        SaoEspecialidad other = (SaoEspecialidad) object;
        if ((this.ideSaesp == null && other.ideSaesp != null) || (this.ideSaesp != null && !this.ideSaesp.equals(other.ideSaesp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoEspecialidad[ ideSaesp=" + ideSaesp + " ]";
    }
    
}
