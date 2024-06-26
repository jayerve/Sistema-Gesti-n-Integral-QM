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
@Table(name = "con_conexion_biometrico", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ConConexionBiometrico.findAll", query = "SELECT c FROM ConConexionBiometrico c"),
    @NamedQuery(name = "ConConexionBiometrico.findByIdeCocob", query = "SELECT c FROM ConConexionBiometrico c WHERE c.ideCocob = :ideCocob"),
    @NamedQuery(name = "ConConexionBiometrico.findByUserCocob", query = "SELECT c FROM ConConexionBiometrico c WHERE c.userCocob = :userCocob"),
    @NamedQuery(name = "ConConexionBiometrico.findByPasswordCocob", query = "SELECT c FROM ConConexionBiometrico c WHERE c.passwordCocob = :passwordCocob"),
    @NamedQuery(name = "ConConexionBiometrico.findByPuertoCocob", query = "SELECT c FROM ConConexionBiometrico c WHERE c.puertoCocob = :puertoCocob"),
    @NamedQuery(name = "ConConexionBiometrico.findByHostCocob", query = "SELECT c FROM ConConexionBiometrico c WHERE c.hostCocob = :hostCocob"),
    @NamedQuery(name = "ConConexionBiometrico.findBySidCocob", query = "SELECT c FROM ConConexionBiometrico c WHERE c.sidCocob = :sidCocob"),
    @NamedQuery(name = "ConConexionBiometrico.findByActivoCocob", query = "SELECT c FROM ConConexionBiometrico c WHERE c.activoCocob = :activoCocob"),
    @NamedQuery(name = "ConConexionBiometrico.findByUsuarioIngre", query = "SELECT c FROM ConConexionBiometrico c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ConConexionBiometrico.findByFechaIngre", query = "SELECT c FROM ConConexionBiometrico c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ConConexionBiometrico.findByHoraIngre", query = "SELECT c FROM ConConexionBiometrico c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ConConexionBiometrico.findByUsuarioActua", query = "SELECT c FROM ConConexionBiometrico c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ConConexionBiometrico.findByFechaActua", query = "SELECT c FROM ConConexionBiometrico c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ConConexionBiometrico.findByHoraActua", query = "SELECT c FROM ConConexionBiometrico c WHERE c.horaActua = :horaActua")})
public class ConConexionBiometrico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cocob", nullable = false)
    private Integer ideCocob;
    @Size(max = 50)
    @Column(name = "user_cocob", length = 50)
    private String userCocob;
    @Size(max = 50)
    @Column(name = "password_cocob", length = 50)
    private String passwordCocob;
    @Size(max = 50)
    @Column(name = "puerto_cocob", length = 50)
    private String puertoCocob;
    @Size(max = 50)
    @Column(name = "host_cocob", length = 50)
    private String hostCocob;
    @Size(max = 50)
    @Column(name = "sid_cocob", length = 50)
    private String sidCocob;
    @Column(name = "activo_cocob")
    private Boolean activoCocob;
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

    public ConConexionBiometrico() {
    }

    public ConConexionBiometrico(Integer ideCocob) {
        this.ideCocob = ideCocob;
    }

    public Integer getIdeCocob() {
        return ideCocob;
    }

    public void setIdeCocob(Integer ideCocob) {
        this.ideCocob = ideCocob;
    }

    public String getUserCocob() {
        return userCocob;
    }

    public void setUserCocob(String userCocob) {
        this.userCocob = userCocob;
    }

    public String getPasswordCocob() {
        return passwordCocob;
    }

    public void setPasswordCocob(String passwordCocob) {
        this.passwordCocob = passwordCocob;
    }

    public String getPuertoCocob() {
        return puertoCocob;
    }

    public void setPuertoCocob(String puertoCocob) {
        this.puertoCocob = puertoCocob;
    }

    public String getHostCocob() {
        return hostCocob;
    }

    public void setHostCocob(String hostCocob) {
        this.hostCocob = hostCocob;
    }

    public String getSidCocob() {
        return sidCocob;
    }

    public void setSidCocob(String sidCocob) {
        this.sidCocob = sidCocob;
    }

    public Boolean getActivoCocob() {
        return activoCocob;
    }

    public void setActivoCocob(Boolean activoCocob) {
        this.activoCocob = activoCocob;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCocob != null ? ideCocob.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConConexionBiometrico)) {
            return false;
        }
        ConConexionBiometrico other = (ConConexionBiometrico) object;
        if ((this.ideCocob == null && other.ideCocob != null) || (this.ideCocob != null && !this.ideCocob.equals(other.ideCocob))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ConConexionBiometrico[ ideCocob=" + ideCocob + " ]";
    }
    
}
