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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "pre_contratacion_partida", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreContratacionPartida.findAll", query = "SELECT p FROM PreContratacionPartida p"),
    @NamedQuery(name = "PreContratacionPartida.findByIdePrcoa", query = "SELECT p FROM PreContratacionPartida p WHERE p.idePrcoa = :idePrcoa"),
    @NamedQuery(name = "PreContratacionPartida.findByActivoPrcoa", query = "SELECT p FROM PreContratacionPartida p WHERE p.activoPrcoa = :activoPrcoa"),
    @NamedQuery(name = "PreContratacionPartida.findByUsuarioIngre", query = "SELECT p FROM PreContratacionPartida p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreContratacionPartida.findByFechaIngre", query = "SELECT p FROM PreContratacionPartida p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreContratacionPartida.findByHoraIngre", query = "SELECT p FROM PreContratacionPartida p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreContratacionPartida.findByUsuarioActua", query = "SELECT p FROM PreContratacionPartida p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreContratacionPartida.findByFechaActua", query = "SELECT p FROM PreContratacionPartida p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreContratacionPartida.findByHoraActua", query = "SELECT p FROM PreContratacionPartida p WHERE p.horaActua = :horaActua")})
public class PreContratacionPartida implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prcoa", nullable = false)
    private Long idePrcoa;
    @Column(name = "activo_prcoa")
    private Boolean activoPrcoa;
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
    @JoinColumn(name = "ide_prpro", referencedColumnName = "ide_prpro")
    @ManyToOne
    private PrePrograma idePrpro;
    @JoinColumn(name = "ide_prcop", referencedColumnName = "ide_prcop")
    @ManyToOne
    private PreContratacionPublica idePrcop;

    public PreContratacionPartida() {
    }

    public PreContratacionPartida(Long idePrcoa) {
        this.idePrcoa = idePrcoa;
    }

    public Long getIdePrcoa() {
        return idePrcoa;
    }

    public void setIdePrcoa(Long idePrcoa) {
        this.idePrcoa = idePrcoa;
    }

    public Boolean getActivoPrcoa() {
        return activoPrcoa;
    }

    public void setActivoPrcoa(Boolean activoPrcoa) {
        this.activoPrcoa = activoPrcoa;
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

    public PrePrograma getIdePrpro() {
        return idePrpro;
    }

    public void setIdePrpro(PrePrograma idePrpro) {
        this.idePrpro = idePrpro;
    }

    public PreContratacionPublica getIdePrcop() {
        return idePrcop;
    }

    public void setIdePrcop(PreContratacionPublica idePrcop) {
        this.idePrcop = idePrcop;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrcoa != null ? idePrcoa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreContratacionPartida)) {
            return false;
        }
        PreContratacionPartida other = (PreContratacionPartida) object;
        if ((this.idePrcoa == null && other.idePrcoa != null) || (this.idePrcoa != null && !this.idePrcoa.equals(other.idePrcoa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreContratacionPartida[ idePrcoa=" + idePrcoa + " ]";
    }
    
}
