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
@Table(name = "pre_responsable_contratacion", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreResponsableContratacion.findAll", query = "SELECT p FROM PreResponsableContratacion p"),
    @NamedQuery(name = "PreResponsableContratacion.findByIdePrrec", query = "SELECT p FROM PreResponsableContratacion p WHERE p.idePrrec = :idePrrec"),
    @NamedQuery(name = "PreResponsableContratacion.findByActivoPrrec", query = "SELECT p FROM PreResponsableContratacion p WHERE p.activoPrrec = :activoPrrec"),
    @NamedQuery(name = "PreResponsableContratacion.findByUsuarioIngre", query = "SELECT p FROM PreResponsableContratacion p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreResponsableContratacion.findByFechaIngre", query = "SELECT p FROM PreResponsableContratacion p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreResponsableContratacion.findByHoraIngre", query = "SELECT p FROM PreResponsableContratacion p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreResponsableContratacion.findByUsuarioActua", query = "SELECT p FROM PreResponsableContratacion p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreResponsableContratacion.findByFechaActua", query = "SELECT p FROM PreResponsableContratacion p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreResponsableContratacion.findByHoraActua", query = "SELECT p FROM PreResponsableContratacion p WHERE p.horaActua = :horaActua")})
public class PreResponsableContratacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prrec", nullable = false)
    private Long idePrrec;
    @Column(name = "activo_prrec")
    private Boolean activoPrrec;
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
    @JoinColumn(name = "ide_prcop", referencedColumnName = "ide_prcop")
    @ManyToOne
    private PreContratacionPublica idePrcop;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;

    public PreResponsableContratacion() {
    }

    public PreResponsableContratacion(Long idePrrec) {
        this.idePrrec = idePrrec;
    }

    public Long getIdePrrec() {
        return idePrrec;
    }

    public void setIdePrrec(Long idePrrec) {
        this.idePrrec = idePrrec;
    }

    public Boolean getActivoPrrec() {
        return activoPrrec;
    }

    public void setActivoPrrec(Boolean activoPrrec) {
        this.activoPrrec = activoPrrec;
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

    public PreContratacionPublica getIdePrcop() {
        return idePrcop;
    }

    public void setIdePrcop(PreContratacionPublica idePrcop) {
        this.idePrcop = idePrcop;
    }

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrrec != null ? idePrrec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreResponsableContratacion)) {
            return false;
        }
        PreResponsableContratacion other = (PreResponsableContratacion) object;
        if ((this.idePrrec == null && other.idePrrec != null) || (this.idePrrec != null && !this.idePrrec.equals(other.idePrrec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreResponsableContratacion[ idePrrec=" + idePrrec + " ]";
    }
    
}
