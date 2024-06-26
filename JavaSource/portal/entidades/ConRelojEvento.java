/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "con_reloj_evento", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ConRelojEvento.findAll", query = "SELECT c FROM ConRelojEvento c"),
    @NamedQuery(name = "ConRelojEvento.findByIdeCoree", query = "SELECT c FROM ConRelojEvento c WHERE c.ideCoree = :ideCoree"),
    @NamedQuery(name = "ConRelojEvento.findByIdeSucu", query = "SELECT c FROM ConRelojEvento c WHERE c.ideSucu = :ideSucu"),
    @NamedQuery(name = "ConRelojEvento.findByDetalleCoree", query = "SELECT c FROM ConRelojEvento c WHERE c.detalleCoree = :detalleCoree"),
    @NamedQuery(name = "ConRelojEvento.findByAlternoCoree", query = "SELECT c FROM ConRelojEvento c WHERE c.alternoCoree = :alternoCoree"),
    @NamedQuery(name = "ConRelojEvento.findByActivoCoree", query = "SELECT c FROM ConRelojEvento c WHERE c.activoCoree = :activoCoree"),
    @NamedQuery(name = "ConRelojEvento.findByUsuarioIngre", query = "SELECT c FROM ConRelojEvento c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ConRelojEvento.findByFechaIngre", query = "SELECT c FROM ConRelojEvento c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ConRelojEvento.findByUsuarioActua", query = "SELECT c FROM ConRelojEvento c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ConRelojEvento.findByFechaActua", query = "SELECT c FROM ConRelojEvento c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ConRelojEvento.findByHoraActua", query = "SELECT c FROM ConRelojEvento c WHERE c.horaActua = :horaActua"),
    @NamedQuery(name = "ConRelojEvento.findByHoraIngre", query = "SELECT c FROM ConRelojEvento c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ConRelojEvento.findByAlmuerzoCoree", query = "SELECT c FROM ConRelojEvento c WHERE c.almuerzoCoree = :almuerzoCoree"),
    @NamedQuery(name = "ConRelojEvento.findByEntradaCoree", query = "SELECT c FROM ConRelojEvento c WHERE c.entradaCoree = :entradaCoree")})
public class ConRelojEvento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_coree", nullable = false)
    private Integer ideCoree;
    @Column(name = "ide_sucu")
    private Integer ideSucu;
    @Size(max = 50)
    @Column(name = "detalle_coree", length = 50)
    private String detalleCoree;
    @Size(max = 50)
    @Column(name = "alterno_coree", length = 50)
    private String alternoCoree;
    @Column(name = "activo_coree")
    private Boolean activoCoree;
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
    @Column(name = "hora_actua")
    @Temporal(TemporalType.TIME)
    private Date horaActua;
    @Column(name = "hora_ingre")
    @Temporal(TemporalType.TIME)
    private Date horaIngre;
    @Column(name = "almuerzo_coree")
    private Boolean almuerzoCoree;
    @Column(name = "entrada_coree")
    private Boolean entradaCoree;

    public ConRelojEvento() {
    }

    public ConRelojEvento(Integer ideCoree) {
        this.ideCoree = ideCoree;
    }

    public Integer getIdeCoree() {
        return ideCoree;
    }

    public void setIdeCoree(Integer ideCoree) {
        this.ideCoree = ideCoree;
    }

    public Integer getIdeSucu() {
        return ideSucu;
    }

    public void setIdeSucu(Integer ideSucu) {
        this.ideSucu = ideSucu;
    }

    public String getDetalleCoree() {
        return detalleCoree;
    }

    public void setDetalleCoree(String detalleCoree) {
        this.detalleCoree = detalleCoree;
    }

    public String getAlternoCoree() {
        return alternoCoree;
    }

    public void setAlternoCoree(String alternoCoree) {
        this.alternoCoree = alternoCoree;
    }

    public Boolean getActivoCoree() {
        return activoCoree;
    }

    public void setActivoCoree(Boolean activoCoree) {
        this.activoCoree = activoCoree;
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

    public Date getHoraActua() {
        return horaActua;
    }

    public void setHoraActua(Date horaActua) {
        this.horaActua = horaActua;
    }

    public Date getHoraIngre() {
        return horaIngre;
    }

    public void setHoraIngre(Date horaIngre) {
        this.horaIngre = horaIngre;
    }

    public Boolean getAlmuerzoCoree() {
        return almuerzoCoree;
    }

    public void setAlmuerzoCoree(Boolean almuerzoCoree) {
        this.almuerzoCoree = almuerzoCoree;
    }

    public Boolean getEntradaCoree() {
        return entradaCoree;
    }

    public void setEntradaCoree(Boolean entradaCoree) {
        this.entradaCoree = entradaCoree;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCoree != null ? ideCoree.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConRelojEvento)) {
            return false;
        }
        ConRelojEvento other = (ConRelojEvento) object;
        if ((this.ideCoree == null && other.ideCoree != null) || (this.ideCoree != null && !this.ideCoree.equals(other.ideCoree))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ConRelojEvento[ ideCoree=" + ideCoree + " ]";
    }
    
}
