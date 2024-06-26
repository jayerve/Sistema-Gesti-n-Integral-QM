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
@Table(name = "pre_contrato_administrador", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreAdministradorContrato.findAll", query = "SELECT p FROM PreAdministradorContrato p"),
    @NamedQuery(name = "PreAdministradorContrato.findByIdePradc", query = "SELECT p FROM PreAdministradorContrato p WHERE p.idePradc = :idePradc"),
    @NamedQuery(name = "PreAdministradorContrato.findByFechaInicioPradc", query = "SELECT p FROM PreAdministradorContrato p WHERE p.fechaInicioPradc = :fechaInicioPradc"),
    @NamedQuery(name = "PreAdministradorContrato.findByFechaFinPradc", query = "SELECT p FROM PreAdministradorContrato p WHERE p.fechaFinPradc = :fechaFinPradc"),
    @NamedQuery(name = "PreAdministradorContrato.findByActivoPradc", query = "SELECT p FROM PreAdministradorContrato p WHERE p.activoPradc = :activoPradc"),
    @NamedQuery(name = "PreAdministradorContrato.findByUsuarioIngre", query = "SELECT p FROM PreAdministradorContrato p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreAdministradorContrato.findByFechaIngre", query = "SELECT p FROM PreAdministradorContrato p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreAdministradorContrato.findByHoraIngre", query = "SELECT p FROM PreAdministradorContrato p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreAdministradorContrato.findByUsuarioActua", query = "SELECT p FROM PreAdministradorContrato p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreAdministradorContrato.findByFechaActua", query = "SELECT p FROM PreAdministradorContrato p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreAdministradorContrato.findByHoraActua", query = "SELECT p FROM PreAdministradorContrato p WHERE p.horaActua = :horaActua"),
    @NamedQuery(name = "PreAdministradorContrato.findByNumeroMemoPradc", query = "SELECT p FROM PreAdministradorContrato p WHERE p.numeroMemoPradc = :numeroMemoPradc")})
public class PreAdministradorContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_pradc", nullable = false)
    private Long idePradc;
    @Column(name = "fecha_inicio_pradc")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioPradc;
    @Column(name = "fecha_fin_pradc")
    @Temporal(TemporalType.DATE)
    private Date fechaFinPradc;
    @Column(name = "activo_pradc")
    private Boolean activoPradc;
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
    @Size(max = 50)
    @Column(name = "numero_memo_pradc", length = 50)
    private String numeroMemoPradc;
    @JoinColumn(name = "ide_prcon", referencedColumnName = "ide_prcon")
    @ManyToOne
    private PreContrato idePrcon;
    @JoinColumn(name = "ide_gtemp", referencedColumnName = "ide_gtemp")
    @ManyToOne
    private GthEmpleado ideGtemp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;

    public PreAdministradorContrato() {
    }

    public PreAdministradorContrato(Long idePradc) {
        this.idePradc = idePradc;
    }

    public Long getIdePradc() {
        return idePradc;
    }

    public void setIdePradc(Long idePradc) {
        this.idePradc = idePradc;
    }

    public Date getFechaInicioPradc() {
        return fechaInicioPradc;
    }

    public void setFechaInicioPradc(Date fechaInicioPradc) {
        this.fechaInicioPradc = fechaInicioPradc;
    }

    public Date getFechaFinPradc() {
        return fechaFinPradc;
    }

    public void setFechaFinPradc(Date fechaFinPradc) {
        this.fechaFinPradc = fechaFinPradc;
    }

    public Boolean getActivoPradc() {
        return activoPradc;
    }

    public void setActivoPradc(Boolean activoPradc) {
        this.activoPradc = activoPradc;
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

    public String getNumeroMemoPradc() {
        return numeroMemoPradc;
    }

    public void setNumeroMemoPradc(String numeroMemoPradc) {
        this.numeroMemoPradc = numeroMemoPradc;
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

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePradc != null ? idePradc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreAdministradorContrato)) {
            return false;
        }
        PreAdministradorContrato other = (PreAdministradorContrato) object;
        if ((this.idePradc == null && other.idePradc != null) || (this.idePradc != null && !this.idePradc.equals(other.idePradc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreAdministradorContrato[ idePradc=" + idePradc + " ]";
    }
    
}
