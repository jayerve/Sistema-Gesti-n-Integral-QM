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
@Table(name = "sbs_lavado_activos", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SbsLavadoActivos.findAll", query = "SELECT s FROM SbsLavadoActivos s"),
    @NamedQuery(name = "SbsLavadoActivos.findByIdeSblaa", query = "SELECT s FROM SbsLavadoActivos s WHERE s.ideSblaa = :ideSblaa"),
    @NamedQuery(name = "SbsLavadoActivos.findByFechaInicioSblaa", query = "SELECT s FROM SbsLavadoActivos s WHERE s.fechaInicioSblaa = :fechaInicioSblaa"),
    @NamedQuery(name = "SbsLavadoActivos.findByFechaFinSblaa", query = "SELECT s FROM SbsLavadoActivos s WHERE s.fechaFinSblaa = :fechaFinSblaa"),
    @NamedQuery(name = "SbsLavadoActivos.findByFechaCorteSblaa", query = "SELECT s FROM SbsLavadoActivos s WHERE s.fechaCorteSblaa = :fechaCorteSblaa"),
    @NamedQuery(name = "SbsLavadoActivos.findByActivoSblaa", query = "SELECT s FROM SbsLavadoActivos s WHERE s.activoSblaa = :activoSblaa"),
    @NamedQuery(name = "SbsLavadoActivos.findByUsuarioIngre", query = "SELECT s FROM SbsLavadoActivos s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SbsLavadoActivos.findByFechaIngre", query = "SELECT s FROM SbsLavadoActivos s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SbsLavadoActivos.findByHoraIngre", query = "SELECT s FROM SbsLavadoActivos s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SbsLavadoActivos.findByUsuarioActua", query = "SELECT s FROM SbsLavadoActivos s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SbsLavadoActivos.findByFechaActua", query = "SELECT s FROM SbsLavadoActivos s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SbsLavadoActivos.findByHoraActua", query = "SELECT s FROM SbsLavadoActivos s WHERE s.horaActua = :horaActua")})
public class SbsLavadoActivos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sblaa", nullable = false)
    private Integer ideSblaa;
    @Column(name = "fecha_inicio_sblaa")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioSblaa;
    @Column(name = "fecha_fin_sblaa")
    @Temporal(TemporalType.DATE)
    private Date fechaFinSblaa;
    @Column(name = "fecha_corte_sblaa")
    @Temporal(TemporalType.DATE)
    private Date fechaCorteSblaa;
    @Column(name = "activo_sblaa")
    private Boolean activoSblaa;
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
    @OneToMany(mappedBy = "ideSblaa")
    private List<SbsArchivoEdiez> sbsArchivoEdiezList;

    public SbsLavadoActivos() {
    }

    public SbsLavadoActivos(Integer ideSblaa) {
        this.ideSblaa = ideSblaa;
    }

    public Integer getIdeSblaa() {
        return ideSblaa;
    }

    public void setIdeSblaa(Integer ideSblaa) {
        this.ideSblaa = ideSblaa;
    }

    public Date getFechaInicioSblaa() {
        return fechaInicioSblaa;
    }

    public void setFechaInicioSblaa(Date fechaInicioSblaa) {
        this.fechaInicioSblaa = fechaInicioSblaa;
    }

    public Date getFechaFinSblaa() {
        return fechaFinSblaa;
    }

    public void setFechaFinSblaa(Date fechaFinSblaa) {
        this.fechaFinSblaa = fechaFinSblaa;
    }

    public Date getFechaCorteSblaa() {
        return fechaCorteSblaa;
    }

    public void setFechaCorteSblaa(Date fechaCorteSblaa) {
        this.fechaCorteSblaa = fechaCorteSblaa;
    }

    public Boolean getActivoSblaa() {
        return activoSblaa;
    }

    public void setActivoSblaa(Boolean activoSblaa) {
        this.activoSblaa = activoSblaa;
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

    public List<SbsArchivoEdiez> getSbsArchivoEdiezList() {
        return sbsArchivoEdiezList;
    }

    public void setSbsArchivoEdiezList(List<SbsArchivoEdiez> sbsArchivoEdiezList) {
        this.sbsArchivoEdiezList = sbsArchivoEdiezList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSblaa != null ? ideSblaa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SbsLavadoActivos)) {
            return false;
        }
        SbsLavadoActivos other = (SbsLavadoActivos) object;
        if ((this.ideSblaa == null && other.ideSblaa != null) || (this.ideSblaa != null && !this.ideSblaa.equals(other.ideSblaa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SbsLavadoActivos[ ideSblaa=" + ideSblaa + " ]";
    }
    
}
