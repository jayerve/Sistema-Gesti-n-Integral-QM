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
@Table(name = "cont_servicio_basico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContServicioBasico.findAll", query = "SELECT c FROM ContServicioBasico c"),
    @NamedQuery(name = "ContServicioBasico.findByIdeCoseb", query = "SELECT c FROM ContServicioBasico c WHERE c.ideCoseb = :ideCoseb"),
    @NamedQuery(name = "ContServicioBasico.findByDetalleCoseb", query = "SELECT c FROM ContServicioBasico c WHERE c.detalleCoseb = :detalleCoseb"),
    @NamedQuery(name = "ContServicioBasico.findByActivoCoseb", query = "SELECT c FROM ContServicioBasico c WHERE c.activoCoseb = :activoCoseb"),
    @NamedQuery(name = "ContServicioBasico.findByUsuarioIngre", query = "SELECT c FROM ContServicioBasico c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContServicioBasico.findByFechaIngre", query = "SELECT c FROM ContServicioBasico c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContServicioBasico.findByHoraIngre", query = "SELECT c FROM ContServicioBasico c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContServicioBasico.findByUsuarioActua", query = "SELECT c FROM ContServicioBasico c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContServicioBasico.findByFechaActua", query = "SELECT c FROM ContServicioBasico c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContServicioBasico.findByHoraActua", query = "SELECT c FROM ContServicioBasico c WHERE c.horaActua = :horaActua")})
public class ContServicioBasico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_coseb", nullable = false)
    private Long ideCoseb;
    @Size(max = 50)
    @Column(name = "detalle_coseb", length = 50)
    private String detalleCoseb;
    @Column(name = "activo_coseb")
    private Boolean activoCoseb;
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
    @OneToMany(mappedBy = "ideCoseb")
    private List<ContSuministro> contSuministroList;

    public ContServicioBasico() {
    }

    public ContServicioBasico(Long ideCoseb) {
        this.ideCoseb = ideCoseb;
    }

    public Long getIdeCoseb() {
        return ideCoseb;
    }

    public void setIdeCoseb(Long ideCoseb) {
        this.ideCoseb = ideCoseb;
    }

    public String getDetalleCoseb() {
        return detalleCoseb;
    }

    public void setDetalleCoseb(String detalleCoseb) {
        this.detalleCoseb = detalleCoseb;
    }

    public Boolean getActivoCoseb() {
        return activoCoseb;
    }

    public void setActivoCoseb(Boolean activoCoseb) {
        this.activoCoseb = activoCoseb;
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

    public List<ContSuministro> getContSuministroList() {
        return contSuministroList;
    }

    public void setContSuministroList(List<ContSuministro> contSuministroList) {
        this.contSuministroList = contSuministroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCoseb != null ? ideCoseb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContServicioBasico)) {
            return false;
        }
        ContServicioBasico other = (ContServicioBasico) object;
        if ((this.ideCoseb == null && other.ideCoseb != null) || (this.ideCoseb != null && !this.ideCoseb.equals(other.ideCoseb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContServicioBasico[ ideCoseb=" + ideCoseb + " ]";
    }
    
}
