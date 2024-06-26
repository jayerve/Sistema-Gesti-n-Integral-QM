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
@Table(name = "cont_parametro_modulo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContParametroModulo.findAll", query = "SELECT c FROM ContParametroModulo c"),
    @NamedQuery(name = "ContParametroModulo.findByIdeCopam", query = "SELECT c FROM ContParametroModulo c WHERE c.ideCopam = :ideCopam"),
    @NamedQuery(name = "ContParametroModulo.findByActivoCopam", query = "SELECT c FROM ContParametroModulo c WHERE c.activoCopam = :activoCopam"),
    @NamedQuery(name = "ContParametroModulo.findByUsuarioIngre", query = "SELECT c FROM ContParametroModulo c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContParametroModulo.findByFechaIngre", query = "SELECT c FROM ContParametroModulo c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContParametroModulo.findByHoraIngre", query = "SELECT c FROM ContParametroModulo c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContParametroModulo.findByUsuarioActua", query = "SELECT c FROM ContParametroModulo c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContParametroModulo.findByFechaActua", query = "SELECT c FROM ContParametroModulo c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContParametroModulo.findByHoraActua", query = "SELECT c FROM ContParametroModulo c WHERE c.horaActua = :horaActua")})
public class ContParametroModulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_copam", nullable = false)
    private Long ideCopam;
    @Column(name = "activo_copam")
    private Boolean activoCopam;
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
    @JoinColumn(name = "ide_gemod", referencedColumnName = "ide_gemod")
    @ManyToOne
    private GenModulo ideGemod;
    @JoinColumn(name = "ide_copag", referencedColumnName = "ide_copag")
    @ManyToOne
    private ContParametrosGeneral ideCopag;

    public ContParametroModulo() {
    }

    public ContParametroModulo(Long ideCopam) {
        this.ideCopam = ideCopam;
    }

    public Long getIdeCopam() {
        return ideCopam;
    }

    public void setIdeCopam(Long ideCopam) {
        this.ideCopam = ideCopam;
    }

    public Boolean getActivoCopam() {
        return activoCopam;
    }

    public void setActivoCopam(Boolean activoCopam) {
        this.activoCopam = activoCopam;
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

    public GenModulo getIdeGemod() {
        return ideGemod;
    }

    public void setIdeGemod(GenModulo ideGemod) {
        this.ideGemod = ideGemod;
    }

    public ContParametrosGeneral getIdeCopag() {
        return ideCopag;
    }

    public void setIdeCopag(ContParametrosGeneral ideCopag) {
        this.ideCopag = ideCopag;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCopam != null ? ideCopam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContParametroModulo)) {
            return false;
        }
        ContParametroModulo other = (ContParametroModulo) object;
        if ((this.ideCopam == null && other.ideCopam != null) || (this.ideCopam != null && !this.ideCopam.equals(other.ideCopam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContParametroModulo[ ideCopam=" + ideCopam + " ]";
    }
    
}
