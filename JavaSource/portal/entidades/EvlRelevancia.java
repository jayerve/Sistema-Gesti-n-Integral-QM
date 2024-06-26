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
@Table(name = "evl_relevancia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "EvlRelevancia.findAll", query = "SELECT e FROM EvlRelevancia e"),
    @NamedQuery(name = "EvlRelevancia.findByIdeEvrel", query = "SELECT e FROM EvlRelevancia e WHERE e.ideEvrel = :ideEvrel"),
    @NamedQuery(name = "EvlRelevancia.findByDetalleEvrel", query = "SELECT e FROM EvlRelevancia e WHERE e.detalleEvrel = :detalleEvrel"),
    @NamedQuery(name = "EvlRelevancia.findByActivoEvrel", query = "SELECT e FROM EvlRelevancia e WHERE e.activoEvrel = :activoEvrel"),
    @NamedQuery(name = "EvlRelevancia.findByUsuarioIngre", query = "SELECT e FROM EvlRelevancia e WHERE e.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "EvlRelevancia.findByFechaIngre", query = "SELECT e FROM EvlRelevancia e WHERE e.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "EvlRelevancia.findByUsuarioActua", query = "SELECT e FROM EvlRelevancia e WHERE e.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "EvlRelevancia.findByFechaActua", query = "SELECT e FROM EvlRelevancia e WHERE e.fechaActua = :fechaActua"),
    @NamedQuery(name = "EvlRelevancia.findByHoraIngre", query = "SELECT e FROM EvlRelevancia e WHERE e.horaIngre = :horaIngre"),
    @NamedQuery(name = "EvlRelevancia.findByHoraActua", query = "SELECT e FROM EvlRelevancia e WHERE e.horaActua = :horaActua")})
public class EvlRelevancia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_evrel", nullable = false)
    private Integer ideEvrel;
    @Size(max = 50)
    @Column(name = "detalle_evrel", length = 50)
    private String detalleEvrel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_evrel", nullable = false)
    private boolean activoEvrel;
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
    @OneToMany(mappedBy = "ideEvrel")
    private List<EvlCompetenciaRelevancia> evlCompetenciaRelevanciaList;
    @OneToMany(mappedBy = "ideEvrel")
    private List<EvlOtraCompetencia> evlOtraCompetenciaList;

    public EvlRelevancia() {
    }

    public EvlRelevancia(Integer ideEvrel) {
        this.ideEvrel = ideEvrel;
    }

    public EvlRelevancia(Integer ideEvrel, boolean activoEvrel) {
        this.ideEvrel = ideEvrel;
        this.activoEvrel = activoEvrel;
    }

    public Integer getIdeEvrel() {
        return ideEvrel;
    }

    public void setIdeEvrel(Integer ideEvrel) {
        this.ideEvrel = ideEvrel;
    }

    public String getDetalleEvrel() {
        return detalleEvrel;
    }

    public void setDetalleEvrel(String detalleEvrel) {
        this.detalleEvrel = detalleEvrel;
    }

    public boolean getActivoEvrel() {
        return activoEvrel;
    }

    public void setActivoEvrel(boolean activoEvrel) {
        this.activoEvrel = activoEvrel;
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

    public List<EvlCompetenciaRelevancia> getEvlCompetenciaRelevanciaList() {
        return evlCompetenciaRelevanciaList;
    }

    public void setEvlCompetenciaRelevanciaList(List<EvlCompetenciaRelevancia> evlCompetenciaRelevanciaList) {
        this.evlCompetenciaRelevanciaList = evlCompetenciaRelevanciaList;
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
        hash += (ideEvrel != null ? ideEvrel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvlRelevancia)) {
            return false;
        }
        EvlRelevancia other = (EvlRelevancia) object;
        if ((this.ideEvrel == null && other.ideEvrel != null) || (this.ideEvrel != null && !this.ideEvrel.equals(other.ideEvrel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.EvlRelevancia[ ideEvrel=" + ideEvrel + " ]";
    }
    
}
