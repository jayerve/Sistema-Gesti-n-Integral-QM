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
@Table(name = "pre_documento_habilitante", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreDocumentoHabilitante.findAll", query = "SELECT p FROM PreDocumentoHabilitante p"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByIdePrdoh", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.idePrdoh = :idePrdoh"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByNumDocumentoPrdoh", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.numDocumentoPrdoh = :numDocumentoPrdoh"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByFechaDocumentoPrdoh", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.fechaDocumentoPrdoh = :fechaDocumentoPrdoh"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByActivoPrdoh", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.activoPrdoh = :activoPrdoh"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByUsuarioIngre", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByFechaIngre", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByHoraIngre", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByUsuarioActua", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByFechaActua", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreDocumentoHabilitante.findByHoraActua", query = "SELECT p FROM PreDocumentoHabilitante p WHERE p.horaActua = :horaActua")})
public class PreDocumentoHabilitante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prdoh", nullable = false)
    private Long idePrdoh;
    @Size(max = 50)
    @Column(name = "num_documento_prdoh", length = 50)
    private String numDocumentoPrdoh;
    @Size(max = 2147483647)
    @Column(name = "fecha_documento_prdoh", length = 2147483647)
    private String fechaDocumentoPrdoh;
    @Column(name = "activo_prdoh")
    private Boolean activoPrdoh;
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
    @JoinColumn(name = "ide_prtra", referencedColumnName = "ide_prtra")
    @ManyToOne
    private PreTramite idePrtra;
    @JoinColumn(name = "ide_gemdo", referencedColumnName = "ide_gemdo")
    @ManyToOne
    private GenModuloDocumento ideGemdo;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;

    public PreDocumentoHabilitante() {
    }

    public PreDocumentoHabilitante(Long idePrdoh) {
        this.idePrdoh = idePrdoh;
    }

    public Long getIdePrdoh() {
        return idePrdoh;
    }

    public void setIdePrdoh(Long idePrdoh) {
        this.idePrdoh = idePrdoh;
    }

    public String getNumDocumentoPrdoh() {
        return numDocumentoPrdoh;
    }

    public void setNumDocumentoPrdoh(String numDocumentoPrdoh) {
        this.numDocumentoPrdoh = numDocumentoPrdoh;
    }

    public String getFechaDocumentoPrdoh() {
        return fechaDocumentoPrdoh;
    }

    public void setFechaDocumentoPrdoh(String fechaDocumentoPrdoh) {
        this.fechaDocumentoPrdoh = fechaDocumentoPrdoh;
    }

    public Boolean getActivoPrdoh() {
        return activoPrdoh;
    }

    public void setActivoPrdoh(Boolean activoPrdoh) {
        this.activoPrdoh = activoPrdoh;
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

    public PreTramite getIdePrtra() {
        return idePrtra;
    }

    public void setIdePrtra(PreTramite idePrtra) {
        this.idePrtra = idePrtra;
    }

    public GenModuloDocumento getIdeGemdo() {
        return ideGemdo;
    }

    public void setIdeGemdo(GenModuloDocumento ideGemdo) {
        this.ideGemdo = ideGemdo;
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
        hash += (idePrdoh != null ? idePrdoh.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreDocumentoHabilitante)) {
            return false;
        }
        PreDocumentoHabilitante other = (PreDocumentoHabilitante) object;
        if ((this.idePrdoh == null && other.idePrdoh != null) || (this.idePrdoh != null && !this.idePrdoh.equals(other.idePrdoh))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreDocumentoHabilitante[ idePrdoh=" + idePrdoh + " ]";
    }
    
}
