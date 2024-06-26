/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "gth_persona_emergencia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "GthPersonaEmergencia.findAll", query = "SELECT g FROM GthPersonaEmergencia g"),
    @NamedQuery(name = "GthPersonaEmergencia.findByIdeGtpee", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.gthPersonaEmergenciaPK.ideGtpee = :ideGtpee"),
    @NamedQuery(name = "GthPersonaEmergencia.findByIdeGtemp", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.gthPersonaEmergenciaPK.ideGtemp = :ideGtemp"),
    @NamedQuery(name = "GthPersonaEmergencia.findByPrimerNombreGtpee", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.primerNombreGtpee = :primerNombreGtpee"),
    @NamedQuery(name = "GthPersonaEmergencia.findBySegundoNombreGtpee", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.segundoNombreGtpee = :segundoNombreGtpee"),
    @NamedQuery(name = "GthPersonaEmergencia.findByApellidoPaternoGtpee", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.apellidoPaternoGtpee = :apellidoPaternoGtpee"),
    @NamedQuery(name = "GthPersonaEmergencia.findByApellidoMaternoGtpee", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.apellidoMaternoGtpee = :apellidoMaternoGtpee"),
    @NamedQuery(name = "GthPersonaEmergencia.findByActivoGtpee", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.activoGtpee = :activoGtpee"),
    @NamedQuery(name = "GthPersonaEmergencia.findByUsuarioIngre", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "GthPersonaEmergencia.findByFechaIngre", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "GthPersonaEmergencia.findByUsuarioActua", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "GthPersonaEmergencia.findByFechaActua", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.fechaActua = :fechaActua"),
    @NamedQuery(name = "GthPersonaEmergencia.findByHoraIngre", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.horaIngre = :horaIngre"),
    @NamedQuery(name = "GthPersonaEmergencia.findByHoraActua", query = "SELECT g FROM GthPersonaEmergencia g WHERE g.horaActua = :horaActua")})
public class GthPersonaEmergencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GthPersonaEmergenciaPK gthPersonaEmergenciaPK;
    @Size(max = 20)
    @Column(name = "primer_nombre_gtpee", length = 20)
    private String primerNombreGtpee;
    @Size(max = 20)
    @Column(name = "segundo_nombre_gtpee", length = 20)
    private String segundoNombreGtpee;
    @Size(max = 20)
    @Column(name = "apellido_paterno_gtpee", length = 20)
    private String apellidoPaternoGtpee;
    @Size(max = 20)
    @Column(name = "apellido_materno_gtpee", length = 20)
    private String apellidoMaternoGtpee;
    @Column(name = "activo_gtpee")
    private Boolean activoGtpee;
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
    @OneToMany(mappedBy = "gthPersonaEmergencia")
    private List<GthDireccion> gthDireccionList;
    @OneToMany(mappedBy = "gthPersonaEmergencia")
    private List<GthTelefono> gthTelefonoList;
    @JoinColumn(name = "ide_gttpr", referencedColumnName = "ide_gttpr")
    @ManyToOne
    private GthTipoParentescoRelacion ideGttpr;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GthEmpleado gthEmpleado;

    public GthPersonaEmergencia() {
    }

    public GthPersonaEmergencia(GthPersonaEmergenciaPK gthPersonaEmergenciaPK) {
        this.gthPersonaEmergenciaPK = gthPersonaEmergenciaPK;
    }

    public GthPersonaEmergencia(int ideGtpee, int ideGtemp) {
        this.gthPersonaEmergenciaPK = new GthPersonaEmergenciaPK(ideGtpee, ideGtemp);
    }

    public GthPersonaEmergenciaPK getGthPersonaEmergenciaPK() {
        return gthPersonaEmergenciaPK;
    }

    public void setGthPersonaEmergenciaPK(GthPersonaEmergenciaPK gthPersonaEmergenciaPK) {
        this.gthPersonaEmergenciaPK = gthPersonaEmergenciaPK;
    }

    public String getPrimerNombreGtpee() {
        return primerNombreGtpee;
    }

    public void setPrimerNombreGtpee(String primerNombreGtpee) {
        this.primerNombreGtpee = primerNombreGtpee;
    }

    public String getSegundoNombreGtpee() {
        return segundoNombreGtpee;
    }

    public void setSegundoNombreGtpee(String segundoNombreGtpee) {
        this.segundoNombreGtpee = segundoNombreGtpee;
    }

    public String getApellidoPaternoGtpee() {
        return apellidoPaternoGtpee;
    }

    public void setApellidoPaternoGtpee(String apellidoPaternoGtpee) {
        this.apellidoPaternoGtpee = apellidoPaternoGtpee;
    }

    public String getApellidoMaternoGtpee() {
        return apellidoMaternoGtpee;
    }

    public void setApellidoMaternoGtpee(String apellidoMaternoGtpee) {
        this.apellidoMaternoGtpee = apellidoMaternoGtpee;
    }

    public Boolean getActivoGtpee() {
        return activoGtpee;
    }

    public void setActivoGtpee(Boolean activoGtpee) {
        this.activoGtpee = activoGtpee;
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

    public List<GthDireccion> getGthDireccionList() {
        return gthDireccionList;
    }

    public void setGthDireccionList(List<GthDireccion> gthDireccionList) {
        this.gthDireccionList = gthDireccionList;
    }

    public List<GthTelefono> getGthTelefonoList() {
        return gthTelefonoList;
    }

    public void setGthTelefonoList(List<GthTelefono> gthTelefonoList) {
        this.gthTelefonoList = gthTelefonoList;
    }

    public GthTipoParentescoRelacion getIdeGttpr() {
        return ideGttpr;
    }

    public void setIdeGttpr(GthTipoParentescoRelacion ideGttpr) {
        this.ideGttpr = ideGttpr;
    }

    public GthEmpleado getGthEmpleado() {
        return gthEmpleado;
    }

    public void setGthEmpleado(GthEmpleado gthEmpleado) {
        this.gthEmpleado = gthEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gthPersonaEmergenciaPK != null ? gthPersonaEmergenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GthPersonaEmergencia)) {
            return false;
        }
        GthPersonaEmergencia other = (GthPersonaEmergencia) object;
        if ((this.gthPersonaEmergenciaPK == null && other.gthPersonaEmergenciaPK != null) || (this.gthPersonaEmergenciaPK != null && !this.gthPersonaEmergenciaPK.equals(other.gthPersonaEmergenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.GthPersonaEmergencia[ gthPersonaEmergenciaPK=" + gthPersonaEmergenciaPK + " ]";
    }
    
}
