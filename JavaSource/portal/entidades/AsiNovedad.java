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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "asi_novedad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AsiNovedad.findAll", query = "SELECT a FROM AsiNovedad a"),
    @NamedQuery(name = "AsiNovedad.findByIdeAsnov", query = "SELECT a FROM AsiNovedad a WHERE a.ideAsnov = :ideAsnov"),
    @NamedQuery(name = "AsiNovedad.findByFechaInicioAsnov", query = "SELECT a FROM AsiNovedad a WHERE a.fechaInicioAsnov = :fechaInicioAsnov"),
    @NamedQuery(name = "AsiNovedad.findByFechaFinAsnov", query = "SELECT a FROM AsiNovedad a WHERE a.fechaFinAsnov = :fechaFinAsnov"),
    @NamedQuery(name = "AsiNovedad.findByObservacionAsnov", query = "SELECT a FROM AsiNovedad a WHERE a.observacionAsnov = :observacionAsnov"),
    @NamedQuery(name = "AsiNovedad.findByActivoAsnov", query = "SELECT a FROM AsiNovedad a WHERE a.activoAsnov = :activoAsnov"),
    @NamedQuery(name = "AsiNovedad.findByUsuarioIngre", query = "SELECT a FROM AsiNovedad a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AsiNovedad.findByFechaIngre", query = "SELECT a FROM AsiNovedad a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AsiNovedad.findByUsuarioActua", query = "SELECT a FROM AsiNovedad a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AsiNovedad.findByFechaActua", query = "SELECT a FROM AsiNovedad a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AsiNovedad.findByHoraIngre", query = "SELECT a FROM AsiNovedad a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AsiNovedad.findByHoraActua", query = "SELECT a FROM AsiNovedad a WHERE a.horaActua = :horaActua")})
public class AsiNovedad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_asnov", nullable = false)
    private Integer ideAsnov;
    @Column(name = "fecha_inicio_asnov")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioAsnov;
    @Column(name = "fecha_fin_asnov")
    @Temporal(TemporalType.DATE)
    private Date fechaFinAsnov;
    @Size(max = 100)
    @Column(name = "observacion_asnov", length = 100)
    private String observacionAsnov;
    @Column(name = "activo_asnov")
    private Boolean activoAsnov;
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
    @OneToMany(mappedBy = "ideAsnov")
    private List<AsiNovedadDetalle> asiNovedadDetalleList;
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;
    @OneToMany(mappedBy = "ideAsnov")
    private List<AsiValidaAsistencia> asiValidaAsistenciaList;

    public AsiNovedad() {
    }

    public AsiNovedad(Integer ideAsnov) {
        this.ideAsnov = ideAsnov;
    }

    public Integer getIdeAsnov() {
        return ideAsnov;
    }

    public void setIdeAsnov(Integer ideAsnov) {
        this.ideAsnov = ideAsnov;
    }

    public Date getFechaInicioAsnov() {
        return fechaInicioAsnov;
    }

    public void setFechaInicioAsnov(Date fechaInicioAsnov) {
        this.fechaInicioAsnov = fechaInicioAsnov;
    }

    public Date getFechaFinAsnov() {
        return fechaFinAsnov;
    }

    public void setFechaFinAsnov(Date fechaFinAsnov) {
        this.fechaFinAsnov = fechaFinAsnov;
    }

    public String getObservacionAsnov() {
        return observacionAsnov;
    }

    public void setObservacionAsnov(String observacionAsnov) {
        this.observacionAsnov = observacionAsnov;
    }

    public Boolean getActivoAsnov() {
        return activoAsnov;
    }

    public void setActivoAsnov(Boolean activoAsnov) {
        this.activoAsnov = activoAsnov;
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

    public List<AsiNovedadDetalle> getAsiNovedadDetalleList() {
        return asiNovedadDetalleList;
    }

    public void setAsiNovedadDetalleList(List<AsiNovedadDetalle> asiNovedadDetalleList) {
        this.asiNovedadDetalleList = asiNovedadDetalleList;
    }

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    public List<AsiValidaAsistencia> getAsiValidaAsistenciaList() {
        return asiValidaAsistenciaList;
    }

    public void setAsiValidaAsistenciaList(List<AsiValidaAsistencia> asiValidaAsistenciaList) {
        this.asiValidaAsistenciaList = asiValidaAsistenciaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAsnov != null ? ideAsnov.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsiNovedad)) {
            return false;
        }
        AsiNovedad other = (AsiNovedad) object;
        if ((this.ideAsnov == null && other.ideAsnov != null) || (this.ideAsnov != null && !this.ideAsnov.equals(other.ideAsnov))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AsiNovedad[ ideAsnov=" + ideAsnov + " ]";
    }
    
}
