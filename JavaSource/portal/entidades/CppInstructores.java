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
@Table(name = "cpp_instructores", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "CppInstructores.findAll", query = "SELECT c FROM CppInstructores c"),
    @NamedQuery(name = "CppInstructores.findByIdeCpins", query = "SELECT c FROM CppInstructores c WHERE c.ideCpins = :ideCpins"),
    @NamedQuery(name = "CppInstructores.findByInstructorCpins", query = "SELECT c FROM CppInstructores c WHERE c.instructorCpins = :instructorCpins"),
    @NamedQuery(name = "CppInstructores.findByCorreoCpins", query = "SELECT c FROM CppInstructores c WHERE c.correoCpins = :correoCpins"),
    @NamedQuery(name = "CppInstructores.findByTelefonoCpins", query = "SELECT c FROM CppInstructores c WHERE c.telefonoCpins = :telefonoCpins"),
    @NamedQuery(name = "CppInstructores.findByActivoCpins", query = "SELECT c FROM CppInstructores c WHERE c.activoCpins = :activoCpins"),
    @NamedQuery(name = "CppInstructores.findByUsuarioIngre", query = "SELECT c FROM CppInstructores c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "CppInstructores.findByFechaIngre", query = "SELECT c FROM CppInstructores c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "CppInstructores.findByHoraIngre", query = "SELECT c FROM CppInstructores c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "CppInstructores.findByUsuarioActua", query = "SELECT c FROM CppInstructores c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "CppInstructores.findByFechaActua", query = "SELECT c FROM CppInstructores c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "CppInstructores.findByHoraActua", query = "SELECT c FROM CppInstructores c WHERE c.horaActua = :horaActua")})
public class CppInstructores implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cpins", nullable = false)
    private Integer ideCpins;
    @Size(max = 100)
    @Column(name = "instructor_cpins", length = 100)
    private String instructorCpins;
    @Size(max = 100)
    @Column(name = "correo_cpins", length = 100)
    private String correoCpins;
    @Size(max = 50)
    @Column(name = "telefono_cpins", length = 50)
    private String telefonoCpins;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_cpins", nullable = false)
    private boolean activoCpins;
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
    @JoinColumn(name = "ide_cpcap", referencedColumnName = "ide_cpcap")
    @ManyToOne
    private CppCapacitacion ideCpcap;

    public CppInstructores() {
    }

    public CppInstructores(Integer ideCpins) {
        this.ideCpins = ideCpins;
    }

    public CppInstructores(Integer ideCpins, boolean activoCpins) {
        this.ideCpins = ideCpins;
        this.activoCpins = activoCpins;
    }

    public Integer getIdeCpins() {
        return ideCpins;
    }

    public void setIdeCpins(Integer ideCpins) {
        this.ideCpins = ideCpins;
    }

    public String getInstructorCpins() {
        return instructorCpins;
    }

    public void setInstructorCpins(String instructorCpins) {
        this.instructorCpins = instructorCpins;
    }

    public String getCorreoCpins() {
        return correoCpins;
    }

    public void setCorreoCpins(String correoCpins) {
        this.correoCpins = correoCpins;
    }

    public String getTelefonoCpins() {
        return telefonoCpins;
    }

    public void setTelefonoCpins(String telefonoCpins) {
        this.telefonoCpins = telefonoCpins;
    }

    public boolean getActivoCpins() {
        return activoCpins;
    }

    public void setActivoCpins(boolean activoCpins) {
        this.activoCpins = activoCpins;
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

    public CppCapacitacion getIdeCpcap() {
        return ideCpcap;
    }

    public void setIdeCpcap(CppCapacitacion ideCpcap) {
        this.ideCpcap = ideCpcap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCpins != null ? ideCpins.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CppInstructores)) {
            return false;
        }
        CppInstructores other = (CppInstructores) object;
        if ((this.ideCpins == null && other.ideCpins != null) || (this.ideCpins != null && !this.ideCpins.equals(other.ideCpins))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.CppInstructores[ ideCpins=" + ideCpins + " ]";
    }
    
}
