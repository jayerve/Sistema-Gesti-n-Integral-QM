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
@Table(name = "sis_usuario_clave", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisUsuarioClave.findAll", query = "SELECT s FROM SisUsuarioClave s"),
    @NamedQuery(name = "SisUsuarioClave.findByIdeUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.ideUscl = :ideUscl"),
    @NamedQuery(name = "SisUsuarioClave.findByFechaRegistroUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.fechaRegistroUscl = :fechaRegistroUscl"),
    @NamedQuery(name = "SisUsuarioClave.findByFechaVenceUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.fechaVenceUscl = :fechaVenceUscl"),
    @NamedQuery(name = "SisUsuarioClave.findByClaveUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.claveUscl = :claveUscl"),
    @NamedQuery(name = "SisUsuarioClave.findByActivoUscl", query = "SELECT s FROM SisUsuarioClave s WHERE s.activoUscl = :activoUscl"),
    @NamedQuery(name = "SisUsuarioClave.findByUsuarioIngre", query = "SELECT s FROM SisUsuarioClave s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisUsuarioClave.findByFechaIngre", query = "SELECT s FROM SisUsuarioClave s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisUsuarioClave.findByUsuarioActua", query = "SELECT s FROM SisUsuarioClave s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisUsuarioClave.findByFechaActua", query = "SELECT s FROM SisUsuarioClave s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisUsuarioClave.findByHoraIngre", query = "SELECT s FROM SisUsuarioClave s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisUsuarioClave.findByHoraActua", query = "SELECT s FROM SisUsuarioClave s WHERE s.horaActua = :horaActua")})
public class SisUsuarioClave implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_uscl", nullable = false)
    private Integer ideUscl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro_uscl", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaRegistroUscl;
    @Column(name = "fecha_vence_uscl")
    @Temporal(TemporalType.DATE)
    private Date fechaVenceUscl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "clave_uscl", nullable = false, length = 50)
    private String claveUscl;
    @Column(name = "activo_uscl")
    private Boolean activoUscl;
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
    @JoinColumn(name = "ide_usua", referencedColumnName = "ide_usua")
    @ManyToOne
    private SisUsuario ideUsua;
    @JoinColumn(name = "ide_pecl", referencedColumnName = "ide_pecl")
    @ManyToOne
    private SisPeriodoClave idePecl;

    public SisUsuarioClave() {
    }

    public SisUsuarioClave(Integer ideUscl) {
        this.ideUscl = ideUscl;
    }

    public SisUsuarioClave(Integer ideUscl, Date fechaRegistroUscl, String claveUscl) {
        this.ideUscl = ideUscl;
        this.fechaRegistroUscl = fechaRegistroUscl;
        this.claveUscl = claveUscl;
    }

    public Integer getIdeUscl() {
        return ideUscl;
    }

    public void setIdeUscl(Integer ideUscl) {
        this.ideUscl = ideUscl;
    }

    public Date getFechaRegistroUscl() {
        return fechaRegistroUscl;
    }

    public void setFechaRegistroUscl(Date fechaRegistroUscl) {
        this.fechaRegistroUscl = fechaRegistroUscl;
    }

    public Date getFechaVenceUscl() {
        return fechaVenceUscl;
    }

    public void setFechaVenceUscl(Date fechaVenceUscl) {
        this.fechaVenceUscl = fechaVenceUscl;
    }

    public String getClaveUscl() {
        return claveUscl;
    }

    public void setClaveUscl(String claveUscl) {
        this.claveUscl = claveUscl;
    }

    public Boolean getActivoUscl() {
        return activoUscl;
    }

    public void setActivoUscl(Boolean activoUscl) {
        this.activoUscl = activoUscl;
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

    public SisUsuario getIdeUsua() {
        return ideUsua;
    }

    public void setIdeUsua(SisUsuario ideUsua) {
        this.ideUsua = ideUsua;
    }

    public SisPeriodoClave getIdePecl() {
        return idePecl;
    }

    public void setIdePecl(SisPeriodoClave idePecl) {
        this.idePecl = idePecl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideUscl != null ? ideUscl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisUsuarioClave)) {
            return false;
        }
        SisUsuarioClave other = (SisUsuarioClave) object;
        if ((this.ideUscl == null && other.ideUscl != null) || (this.ideUscl != null && !this.ideUscl.equals(other.ideUscl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisUsuarioClave[ ideUscl=" + ideUscl + " ]";
    }
    
}
