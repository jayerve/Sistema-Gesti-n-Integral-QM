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
@Table(name = "afi_actividad", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "AfiActividad.findAll", query = "SELECT a FROM AfiActividad a"),
    @NamedQuery(name = "AfiActividad.findByIdeAfacd", query = "SELECT a FROM AfiActividad a WHERE a.ideAfacd = :ideAfacd"),
    @NamedQuery(name = "AfiActividad.findByDetalleAfacd", query = "SELECT a FROM AfiActividad a WHERE a.detalleAfacd = :detalleAfacd"),
    @NamedQuery(name = "AfiActividad.findByActivoAfacd", query = "SELECT a FROM AfiActividad a WHERE a.activoAfacd = :activoAfacd"),
    @NamedQuery(name = "AfiActividad.findByUsuarioIngre", query = "SELECT a FROM AfiActividad a WHERE a.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "AfiActividad.findByFechaIngre", query = "SELECT a FROM AfiActividad a WHERE a.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "AfiActividad.findByHoraIngre", query = "SELECT a FROM AfiActividad a WHERE a.horaIngre = :horaIngre"),
    @NamedQuery(name = "AfiActividad.findByUsuarioActua", query = "SELECT a FROM AfiActividad a WHERE a.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "AfiActividad.findByFechaActua", query = "SELECT a FROM AfiActividad a WHERE a.fechaActua = :fechaActua"),
    @NamedQuery(name = "AfiActividad.findByHoraActua", query = "SELECT a FROM AfiActividad a WHERE a.horaActua = :horaActua")})
public class AfiActividad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_afacd", nullable = false)
    private Integer ideAfacd;
    @Size(max = 50)
    @Column(name = "detalle_afacd", length = 50)
    private String detalleAfacd;
    @Column(name = "activo_afacd")
    private Boolean activoAfacd;
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
    @OneToMany(mappedBy = "ideAfacd")
    private List<AfiActivo> afiActivoList;

    public AfiActividad() {
    }

    public AfiActividad(Integer ideAfacd) {
        this.ideAfacd = ideAfacd;
    }

    public Integer getIdeAfacd() {
        return ideAfacd;
    }

    public void setIdeAfacd(Integer ideAfacd) {
        this.ideAfacd = ideAfacd;
    }

    public String getDetalleAfacd() {
        return detalleAfacd;
    }

    public void setDetalleAfacd(String detalleAfacd) {
        this.detalleAfacd = detalleAfacd;
    }

    public Boolean getActivoAfacd() {
        return activoAfacd;
    }

    public void setActivoAfacd(Boolean activoAfacd) {
        this.activoAfacd = activoAfacd;
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
        hash += (ideAfacd != null ? ideAfacd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AfiActividad)) {
            return false;
        }
        AfiActividad other = (AfiActividad) object;
        if ((this.ideAfacd == null && other.ideAfacd != null) || (this.ideAfacd != null && !this.ideAfacd.equals(other.ideAfacd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.AfiActividad[ ideAfacd=" + ideAfacd + " ]";
    }
    
}
