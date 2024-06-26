/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "cont_responsable_convenio", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContResponsableConvenio.findAll", query = "SELECT c FROM ContResponsableConvenio c"),
    @NamedQuery(name = "ContResponsableConvenio.findByIdeCorec", query = "SELECT c FROM ContResponsableConvenio c WHERE c.ideCorec = :ideCorec"),
    @NamedQuery(name = "ContResponsableConvenio.findByIdeGecaf", query = "SELECT c FROM ContResponsableConvenio c WHERE c.ideGecaf = :ideGecaf"),
    @NamedQuery(name = "ContResponsableConvenio.findByPrimerNombreCorec", query = "SELECT c FROM ContResponsableConvenio c WHERE c.primerNombreCorec = :primerNombreCorec"),
    @NamedQuery(name = "ContResponsableConvenio.findBySegundoNombreCorec", query = "SELECT c FROM ContResponsableConvenio c WHERE c.segundoNombreCorec = :segundoNombreCorec"),
    @NamedQuery(name = "ContResponsableConvenio.findByApellidoPaternoCorec", query = "SELECT c FROM ContResponsableConvenio c WHERE c.apellidoPaternoCorec = :apellidoPaternoCorec"),
    @NamedQuery(name = "ContResponsableConvenio.findByApellidoMaternoCorec", query = "SELECT c FROM ContResponsableConvenio c WHERE c.apellidoMaternoCorec = :apellidoMaternoCorec"),
    @NamedQuery(name = "ContResponsableConvenio.findByActivoCorec", query = "SELECT c FROM ContResponsableConvenio c WHERE c.activoCorec = :activoCorec"),
    @NamedQuery(name = "ContResponsableConvenio.findByUsuarioIngre", query = "SELECT c FROM ContResponsableConvenio c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContResponsableConvenio.findByFechaIngre", query = "SELECT c FROM ContResponsableConvenio c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContResponsableConvenio.findByHoraIngre", query = "SELECT c FROM ContResponsableConvenio c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContResponsableConvenio.findByUsuarioActua", query = "SELECT c FROM ContResponsableConvenio c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContResponsableConvenio.findByFechaActua", query = "SELECT c FROM ContResponsableConvenio c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContResponsableConvenio.findByHoraActua", query = "SELECT c FROM ContResponsableConvenio c WHERE c.horaActua = :horaActua")})
public class ContResponsableConvenio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_corec", nullable = false)
    private Long ideCorec;
    @Column(name = "ide_gecaf")
    private BigInteger ideGecaf;
    @Size(max = 50)
    @Column(name = "primer_nombre_corec", length = 50)
    private String primerNombreCorec;
    @Size(max = 50)
    @Column(name = "segundo_nombre_corec", length = 50)
    private String segundoNombreCorec;
    @Size(max = 50)
    @Column(name = "apellido_paterno_corec", length = 50)
    private String apellidoPaternoCorec;
    @Size(max = 50)
    @Column(name = "apellido_materno_corec", length = 50)
    private String apellidoMaternoCorec;
    @Column(name = "activo_corec")
    private Boolean activoCorec;
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
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_getip", referencedColumnName = "ide_getip")
    @ManyToOne
    private GenTipoPersona ideGetip;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "ide_cocon", referencedColumnName = "ide_cocon")
    @ManyToOne
    private ContConvenio ideCocon;

    public ContResponsableConvenio() {
    }

    public ContResponsableConvenio(Long ideCorec) {
        this.ideCorec = ideCorec;
    }

    public Long getIdeCorec() {
        return ideCorec;
    }

    public void setIdeCorec(Long ideCorec) {
        this.ideCorec = ideCorec;
    }

    public BigInteger getIdeGecaf() {
        return ideGecaf;
    }

    public void setIdeGecaf(BigInteger ideGecaf) {
        this.ideGecaf = ideGecaf;
    }

    public String getPrimerNombreCorec() {
        return primerNombreCorec;
    }

    public void setPrimerNombreCorec(String primerNombreCorec) {
        this.primerNombreCorec = primerNombreCorec;
    }

    public String getSegundoNombreCorec() {
        return segundoNombreCorec;
    }

    public void setSegundoNombreCorec(String segundoNombreCorec) {
        this.segundoNombreCorec = segundoNombreCorec;
    }

    public String getApellidoPaternoCorec() {
        return apellidoPaternoCorec;
    }

    public void setApellidoPaternoCorec(String apellidoPaternoCorec) {
        this.apellidoPaternoCorec = apellidoPaternoCorec;
    }

    public String getApellidoMaternoCorec() {
        return apellidoMaternoCorec;
    }

    public void setApellidoMaternoCorec(String apellidoMaternoCorec) {
        this.apellidoMaternoCorec = apellidoMaternoCorec;
    }

    public Boolean getActivoCorec() {
        return activoCorec;
    }

    public void setActivoCorec(Boolean activoCorec) {
        this.activoCorec = activoCorec;
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

    public GthEmpleado getIdeGtemp() {
        return ideGtemp;
    }

    public void setIdeGtemp(GthEmpleado ideGtemp) {
        this.ideGtemp = ideGtemp;
    }

    public GenTipoPersona getIdeGetip() {
        return ideGetip;
    }

    public void setIdeGetip(GenTipoPersona ideGetip) {
        this.ideGetip = ideGetip;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public ContConvenio getIdeCocon() {
        return ideCocon;
    }

    public void setIdeCocon(ContConvenio ideCocon) {
        this.ideCocon = ideCocon;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCorec != null ? ideCorec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContResponsableConvenio)) {
            return false;
        }
        ContResponsableConvenio other = (ContResponsableConvenio) object;
        if ((this.ideCorec == null && other.ideCorec != null) || (this.ideCorec != null && !this.ideCorec.equals(other.ideCorec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContResponsableConvenio[ ideCorec=" + ideCorec + " ]";
    }
    
}
