/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "afi_seguro", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiSeguro.findAll", query = "SELECT a FROM AfiSeguro a"),
    @NamedQuery(name = "AfiSeguro.findByIdeAfseg", query = "SELECT a FROM AfiSeguro a WHERE a.ideAfseg = :ideAfseg"),
    @NamedQuery(name = "AfiSeguro.findByPorcentajeAsegurableAfseg", query = "SELECT a FROM AfiSeguro a WHERE a.porcentajeAsegurableAfseg = :porcentajeAsegurableAfseg"),
    @NamedQuery(name = "AfiSeguro.findByDetalleAfseg", query = "SELECT a FROM AfiSeguro a WHERE a.detalleAfseg = :detalleAfseg"),
    @NamedQuery(name = "AfiSeguro.findByActivoAfseg", query = "SELECT a FROM AfiSeguro a WHERE a.activoAfseg = :activoAfseg"),
    @NamedQuery(name = "AfiSeguro.findByUsuarioIngre", query = "SELECT a FROM AfiSeguro a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiSeguro.findByFechaIngre", query = "SELECT a FROM AfiSeguro a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiSeguro.findByHoraIngre", query = "SELECT a FROM AfiSeguro a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiSeguro.findByUsuarioActua", query = "SELECT a FROM AfiSeguro a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiSeguro.findByFechaActua", query = "SELECT a FROM AfiSeguro a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiSeguro.findByHoraActua", query = "SELECT a FROM AfiSeguro a WHERE a.horaActua = :horaActua")})
public class AfiSeguro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_afseg", nullable = false)
    private Integer ideAfseg;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcentaje_asegurable_afseg", precision = 10, scale = 2)
    private BigDecimal porcentajeAsegurableAfseg;
    @Size(max = 50)
    @Column(name = "detalle_afseg", length = 50)
    private String detalleAfseg;
    @Column(name = "activo_afseg")
    private Boolean activoAfseg;
    @Size(max = 50)
    @Column(name = "usuario_ingre", length = 50)
    private String usuarioIngre;
    @Column(name = "fecha_ingre")
    @Temporal(TemporalType.DATE)
    private Date fechaIngre;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Size(max = 50)
    @Column(name = "usuario_actua", length = 50)
    private String usuarioActua;
    @Column(name = "fecha_actua")
    @Temporal(TemporalType.DATE)
    private Date fechaActua;
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @OneToMany(mappedBy = "ideAfseg")
    private List<AfiActivo> afiActivoList;

    public AfiSeguro() {
    }

    public AfiSeguro(Integer ideAfseg) {
        this.ideAfseg = ideAfseg;
    }

    public Integer getIdeAfseg() {
        return ideAfseg;
    }

    public void setIdeAfseg(Integer ideAfseg) {
        this.ideAfseg = ideAfseg;
    }

    public BigDecimal getPorcentajeAsegurableAfseg() {
        return porcentajeAsegurableAfseg;
    }

    public void setPorcentajeAsegurableAfseg(BigDecimal porcentajeAsegurableAfseg) {
        this.porcentajeAsegurableAfseg = porcentajeAsegurableAfseg;
    }

    public String getDetalleAfseg() {
        return detalleAfseg;
    }

    public void setDetalleAfseg(String detalleAfseg) {
        this.detalleAfseg = detalleAfseg;
    }

    public Boolean getActivoAfseg() {
        return activoAfseg;
    }

    public void setActivoAfseg(Boolean activoAfseg) {
        this.activoAfseg = activoAfseg;
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

    public List<AfiActivo> getAfiActivoList() {
        return afiActivoList;
    }

    public void setAfiActivoList(List<AfiActivo> afiActivoList) {
        this.afiActivoList = afiActivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAfseg != null ? ideAfseg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiSeguro)) {
            return false;
        }
        AfiSeguro other = (AfiSeguro) object;
        if ((this.ideAfseg == null && other.ideAfseg != null) || (this.ideAfseg != null && !this.ideAfseg.equals(other.ideAfseg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiSeguro[ ideAfseg=" + ideAfseg + " ]";
    }
    
}
