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
@Table(name = "pre_compareciente_contrato", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreComparecienteContrato.findAll", query = "SELECT p FROM PreComparecienteContrato p"),
    @NamedQuery(name = "PreComparecienteContrato.findByIdePrcoc", query = "SELECT p FROM PreComparecienteContrato p WHERE p.idePrcoc = :idePrcoc"),
    @NamedQuery(name = "PreComparecienteContrato.findByPrimerNombrePrcoc", query = "SELECT p FROM PreComparecienteContrato p WHERE p.primerNombrePrcoc = :primerNombrePrcoc"),
    @NamedQuery(name = "PreComparecienteContrato.findBySegundoNombrePrcoc", query = "SELECT p FROM PreComparecienteContrato p WHERE p.segundoNombrePrcoc = :segundoNombrePrcoc"),
    @NamedQuery(name = "PreComparecienteContrato.findByApellidoPaternoPrcoc", query = "SELECT p FROM PreComparecienteContrato p WHERE p.apellidoPaternoPrcoc = :apellidoPaternoPrcoc"),
    @NamedQuery(name = "PreComparecienteContrato.findByApellidoMaternoPrcoc", query = "SELECT p FROM PreComparecienteContrato p WHERE p.apellidoMaternoPrcoc = :apellidoMaternoPrcoc"),
    @NamedQuery(name = "PreComparecienteContrato.findByActivoPrcoc", query = "SELECT p FROM PreComparecienteContrato p WHERE p.activoPrcoc = :activoPrcoc"),
    @NamedQuery(name = "PreComparecienteContrato.findByUsuarioIngre", query = "SELECT p FROM PreComparecienteContrato p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreComparecienteContrato.findByFechaIngre", query = "SELECT p FROM PreComparecienteContrato p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreComparecienteContrato.findByHoraIngre", query = "SELECT p FROM PreComparecienteContrato p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreComparecienteContrato.findByUsuarioActua", query = "SELECT p FROM PreComparecienteContrato p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreComparecienteContrato.findByFechaActua", query = "SELECT p FROM PreComparecienteContrato p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreComparecienteContrato.findByHoraActua", query = "SELECT p FROM PreComparecienteContrato p WHERE p.horaActua = :horaActua")})
public class PreComparecienteContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prcoc", nullable = false)
    private Long idePrcoc;
    @Size(max = 50)
    @Column(name = "primer_nombre_prcoc", length = 50)
    private String primerNombrePrcoc;
    @Size(max = 50)
    @Column(name = "segundo_nombre_prcoc", length = 50)
    private String segundoNombrePrcoc;
    @Size(max = 50)
    @Column(name = "apellido_paterno_prcoc", length = 50)
    private String apellidoPaternoPrcoc;
    @Size(max = 50)
    @Column(name = "apellido_materno_prcoc", length = 50)
    private String apellidoMaternoPrcoc;
    @Column(name = "activo_prcoc")
    private Boolean activoPrcoc;
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
    @JoinColumn(name = "ide_tepro", referencedColumnName = "ide_tepro")
    @ManyToOne
    private TesProveedor ideTepro;
    @JoinColumn(name = "ide_prtio", referencedColumnName = "ide_prtio")
    @ManyToOne
    private PreTipoCompareciente idePrtio;
    @JoinColumn(name = "ide_prcon", referencedColumnName = "ide_prcon")
    @ManyToOne
    private PreContrato idePrcon;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_getip", referencedColumnName = "ide_getip")
    @ManyToOne
    private GenTipoPersona ideGetip;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;

    public PreComparecienteContrato() {
    }

    public PreComparecienteContrato(Long idePrcoc) {
        this.idePrcoc = idePrcoc;
    }

    public Long getIdePrcoc() {
        return idePrcoc;
    }

    public void setIdePrcoc(Long idePrcoc) {
        this.idePrcoc = idePrcoc;
    }

    public String getPrimerNombrePrcoc() {
        return primerNombrePrcoc;
    }

    public void setPrimerNombrePrcoc(String primerNombrePrcoc) {
        this.primerNombrePrcoc = primerNombrePrcoc;
    }

    public String getSegundoNombrePrcoc() {
        return segundoNombrePrcoc;
    }

    public void setSegundoNombrePrcoc(String segundoNombrePrcoc) {
        this.segundoNombrePrcoc = segundoNombrePrcoc;
    }

    public String getApellidoPaternoPrcoc() {
        return apellidoPaternoPrcoc;
    }

    public void setApellidoPaternoPrcoc(String apellidoPaternoPrcoc) {
        this.apellidoPaternoPrcoc = apellidoPaternoPrcoc;
    }

    public String getApellidoMaternoPrcoc() {
        return apellidoMaternoPrcoc;
    }

    public void setApellidoMaternoPrcoc(String apellidoMaternoPrcoc) {
        this.apellidoMaternoPrcoc = apellidoMaternoPrcoc;
    }

    public Boolean getActivoPrcoc() {
        return activoPrcoc;
    }

    public void setActivoPrcoc(Boolean activoPrcoc) {
        this.activoPrcoc = activoPrcoc;
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

    public TesProveedor getIdeTepro() {
        return ideTepro;
    }

    public void setIdeTepro(TesProveedor ideTepro) {
        this.ideTepro = ideTepro;
    }

    public PreTipoCompareciente getIdePrtio() {
        return idePrtio;
    }

    public void setIdePrtio(PreTipoCompareciente idePrtio) {
        this.idePrtio = idePrtio;
    }

    public PreContrato getIdePrcon() {
        return idePrcon;
    }

    public void setIdePrcon(PreContrato idePrcon) {
        this.idePrcon = idePrcon;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrcoc != null ? idePrcoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreComparecienteContrato)) {
            return false;
        }
        PreComparecienteContrato other = (PreComparecienteContrato) object;
        if ((this.idePrcoc == null && other.idePrcoc != null) || (this.idePrcoc != null && !this.idePrcoc.equals(other.idePrcoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreComparecienteContrato[ idePrcoc=" + idePrcoc + " ]";
    }
    
}
