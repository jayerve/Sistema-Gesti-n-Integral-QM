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
@Table(name = "spr_estado_estudio", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprEstadoEstudio.findAll", query = "SELECT s FROM SprEstadoEstudio s"),
    @NamedQuery(name = "SprEstadoEstudio.findByIdeSpese", query = "SELECT s FROM SprEstadoEstudio s WHERE s.ideSpese = :ideSpese"),
    @NamedQuery(name = "SprEstadoEstudio.findByDetalleSpese", query = "SELECT s FROM SprEstadoEstudio s WHERE s.detalleSpese = :detalleSpese"),
    @NamedQuery(name = "SprEstadoEstudio.findByActivoSpest", query = "SELECT s FROM SprEstadoEstudio s WHERE s.activoSpest = :activoSpest"),
    @NamedQuery(name = "SprEstadoEstudio.findByUsuarioIngre", query = "SELECT s FROM SprEstadoEstudio s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprEstadoEstudio.findByFechaIngre", query = "SELECT s FROM SprEstadoEstudio s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprEstadoEstudio.findByHoraIngre", query = "SELECT s FROM SprEstadoEstudio s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprEstadoEstudio.findByUsuarioActua", query = "SELECT s FROM SprEstadoEstudio s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprEstadoEstudio.findByFechaActua", query = "SELECT s FROM SprEstadoEstudio s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprEstadoEstudio.findByHoraActua", query = "SELECT s FROM SprEstadoEstudio s WHERE s.horaActua = :horaActua")})
public class SprEstadoEstudio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spese", nullable = false)
    private Integer ideSpese;
    @Size(max = 50)
    @Column(name = "detalle_spese", length = 50)
    private String detalleSpese;
    @Column(name = "activo_spest")
    private Boolean activoSpest;
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
    @OneToMany(mappedBy = "ideSpese")
    private List<SprEstudios> sprEstudiosList;

    public SprEstadoEstudio() {
    }

    public SprEstadoEstudio(Integer ideSpese) {
        this.ideSpese = ideSpese;
    }

    public Integer getIdeSpese() {
        return ideSpese;
    }

    public void setIdeSpese(Integer ideSpese) {
        this.ideSpese = ideSpese;
    }

    public String getDetalleSpese() {
        return detalleSpese;
    }

    public void setDetalleSpese(String detalleSpese) {
        this.detalleSpese = detalleSpese;
    }

    public Boolean getActivoSpest() {
        return activoSpest;
    }

    public void setActivoSpest(Boolean activoSpest) {
        this.activoSpest = activoSpest;
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

    public List<SprEstudios> getSprEstudiosList() {
        return sprEstudiosList;
    }

    public void setSprEstudiosList(List<SprEstudios> sprEstudiosList) {
        this.sprEstudiosList = sprEstudiosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpese != null ? ideSpese.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprEstadoEstudio)) {
            return false;
        }
        SprEstadoEstudio other = (SprEstadoEstudio) object;
        if ((this.ideSpese == null && other.ideSpese != null) || (this.ideSpese != null && !this.ideSpese.equals(other.ideSpese))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprEstadoEstudio[ ideSpese=" + ideSpese + " ]";
    }
    
}
