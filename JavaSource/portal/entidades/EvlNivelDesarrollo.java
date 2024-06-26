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
@Table(name = "evl_nivel_desarrollo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlNivelDesarrollo.findAll", query = "SELECT e FROM EvlNivelDesarrollo e"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByIdeEvnid", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.ideEvnid = :ideEvnid"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByDetalleEvnid", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.detalleEvnid = :detalleEvnid"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByValorEvnid", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.valorEvnid = :valorEvnid"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByPorIniCumpliEvnid", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.porIniCumpliEvnid = :porIniCumpliEvnid"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByPorFinCumpliEvnid", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.porFinCumpliEvnid = :porFinCumpliEvnid"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByActivoEvnid", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.activoEvnid = :activoEvnid"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByUsuarioIngre", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByFechaIngre", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByUsuarioActua", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByFechaActua", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByHoraIngre", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlNivelDesarrollo.findByHoraActua", query = "SELECT e FROM EvlNivelDesarrollo e WHERE e.horaActua = :horaActua")})
public class EvlNivelDesarrollo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evnid", nullable = false)
    private Integer ideEvnid;
    @Size(max = 50)
    @Column(name = "detalle_evnid", length = 50)
    private String detalleEvnid;
    @Column(name = "valor_evnid")
    private Integer valorEvnid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "por_ini_cumpli_evnid", precision = 12, scale = 2)
    private BigDecimal porIniCumpliEvnid;
    @Column(name = "por_fin_cumpli_evnid", precision = 12, scale = 2)
    private BigDecimal porFinCumpliEvnid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evnid", nullable = false)
    private boolean activoEvnid;
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
    @OneToMany(mappedBy = "ideEvnid")
    private List<EvlOtraCompetencia> evlOtraCompetenciaList;

    public EvlNivelDesarrollo() {
    }

    public EvlNivelDesarrollo(Integer ideEvnid) {
        this.ideEvnid = ideEvnid;
    }

    public EvlNivelDesarrollo(Integer ideEvnid, boolean activoEvnid) {
        this.ideEvnid = ideEvnid;
        this.activoEvnid = activoEvnid;
    }

    public Integer getIdeEvnid() {
        return ideEvnid;
    }

    public void setIdeEvnid(Integer ideEvnid) {
        this.ideEvnid = ideEvnid;
    }

    public String getDetalleEvnid() {
        return detalleEvnid;
    }

    public void setDetalleEvnid(String detalleEvnid) {
        this.detalleEvnid = detalleEvnid;
    }

    public Integer getValorEvnid() {
        return valorEvnid;
    }

    public void setValorEvnid(Integer valorEvnid) {
        this.valorEvnid = valorEvnid;
    }

    public BigDecimal getPorIniCumpliEvnid() {
        return porIniCumpliEvnid;
    }

    public void setPorIniCumpliEvnid(BigDecimal porIniCumpliEvnid) {
        this.porIniCumpliEvnid = porIniCumpliEvnid;
    }

    public BigDecimal getPorFinCumpliEvnid() {
        return porFinCumpliEvnid;
    }

    public void setPorFinCumpliEvnid(BigDecimal porFinCumpliEvnid) {
        this.porFinCumpliEvnid = porFinCumpliEvnid;
    }

    public boolean getActivoEvnid() {
        return activoEvnid;
    }

    public void setActivoEvnid(boolean activoEvnid) {
        this.activoEvnid = activoEvnid;
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

    public List<EvlOtraCompetencia> getEvlOtraCompetenciaList() {
        return evlOtraCompetenciaList;
    }

    public void setEvlOtraCompetenciaList(List<EvlOtraCompetencia> evlOtraCompetenciaList) {
        this.evlOtraCompetenciaList = evlOtraCompetenciaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEvnid != null ? ideEvnid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlNivelDesarrollo)) {
            return false;
        }
        EvlNivelDesarrollo other = (EvlNivelDesarrollo) object;
        if ((this.ideEvnid == null && other.ideEvnid != null) || (this.ideEvnid != null && !this.ideEvnid.equals(other.ideEvnid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlNivelDesarrollo[ ideEvnid=" + ideEvnid + " ]";
    }
    
}
