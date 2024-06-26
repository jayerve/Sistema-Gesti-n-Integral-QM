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
@Table(name = "pre_archivo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreArchivo.findAll", query = "SELECT p FROM PreArchivo p"),
    @NamedQuery(name = "PreArchivo.findByIdePrarc", query = "SELECT p FROM PreArchivo p WHERE p.idePrarc = :idePrarc"),
    @NamedQuery(name = "PreArchivo.findByFechaPrarc", query = "SELECT p FROM PreArchivo p WHERE p.fechaPrarc = :fechaPrarc"),
    @NamedQuery(name = "PreArchivo.findByObservacionesPrarc", query = "SELECT p FROM PreArchivo p WHERE p.observacionesPrarc = :observacionesPrarc"),
    @NamedQuery(name = "PreArchivo.findByFotoPrarc", query = "SELECT p FROM PreArchivo p WHERE p.fotoPrarc = :fotoPrarc"),
    @NamedQuery(name = "PreArchivo.findByActivoPrarc", query = "SELECT p FROM PreArchivo p WHERE p.activoPrarc = :activoPrarc"),
    @NamedQuery(name = "PreArchivo.findByUsuarioIngre", query = "SELECT p FROM PreArchivo p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreArchivo.findByFechaIngre", query = "SELECT p FROM PreArchivo p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreArchivo.findByHoraIngre", query = "SELECT p FROM PreArchivo p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreArchivo.findByUsuarioActua", query = "SELECT p FROM PreArchivo p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreArchivo.findByFechaActua", query = "SELECT p FROM PreArchivo p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreArchivo.findByHoraActua", query = "SELECT p FROM PreArchivo p WHERE p.horaActua = :horaActua")})
public class PreArchivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prarc", nullable = false)
    private Long idePrarc;
    @Column(name = "fecha_prarc")
    @Temporal(TemporalType.DATE)
    private Date fechaPrarc;
    @Size(max = 2147483647)
    @Column(name = "observaciones_prarc", length = 2147483647)
    private String observacionesPrarc;
    @Size(max = 250)
    @Column(name = "foto_prarc", length = 250)
    private String fotoPrarc;
    @Column(name = "activo_prarc")
    private Boolean activoPrarc;
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
    @JoinColumn(name = "ide_prpoa", referencedColumnName = "ide_prpoa")
    @ManyToOne
    private PrePoa idePrpoa;
    @JoinColumn(name = "ide_prpac", referencedColumnName = "ide_prpac")
    @ManyToOne
    private PrePac idePrpac;
    @JoinColumn(name = "ide_prcon", referencedColumnName = "ide_prcon")
    @ManyToOne
    private PreContrato idePrcon;
    @JoinColumn(name = "ide_prcop", referencedColumnName = "ide_prcop")
    @ManyToOne
    private PreContratacionPublica idePrcop;

    public PreArchivo() {
    }

    public PreArchivo(Long idePrarc) {
        this.idePrarc = idePrarc;
    }

    public Long getIdePrarc() {
        return idePrarc;
    }

    public void setIdePrarc(Long idePrarc) {
        this.idePrarc = idePrarc;
    }

    public Date getFechaPrarc() {
        return fechaPrarc;
    }

    public void setFechaPrarc(Date fechaPrarc) {
        this.fechaPrarc = fechaPrarc;
    }

    public String getObservacionesPrarc() {
        return observacionesPrarc;
    }

    public void setObservacionesPrarc(String observacionesPrarc) {
        this.observacionesPrarc = observacionesPrarc;
    }

    public String getFotoPrarc() {
        return fotoPrarc;
    }

    public void setFotoPrarc(String fotoPrarc) {
        this.fotoPrarc = fotoPrarc;
    }

    public Boolean getActivoPrarc() {
        return activoPrarc;
    }

    public void setActivoPrarc(Boolean activoPrarc) {
        this.activoPrarc = activoPrarc;
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

    public PrePoa getIdePrpoa() {
        return idePrpoa;
    }

    public void setIdePrpoa(PrePoa idePrpoa) {
        this.idePrpoa = idePrpoa;
    }

    public PrePac getIdePrpac() {
        return idePrpac;
    }

    public void setIdePrpac(PrePac idePrpac) {
        this.idePrpac = idePrpac;
    }

    public PreContrato getIdePrcon() {
        return idePrcon;
    }

    public void setIdePrcon(PreContrato idePrcon) {
        this.idePrcon = idePrcon;
    }

    public PreContratacionPublica getIdePrcop() {
        return idePrcop;
    }

    public void setIdePrcop(PreContratacionPublica idePrcop) {
        this.idePrcop = idePrcop;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrarc != null ? idePrarc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreArchivo)) {
            return false;
        }
        PreArchivo other = (PreArchivo) object;
        if ((this.idePrarc == null && other.idePrarc != null) || (this.idePrarc != null && !this.idePrarc.equals(other.idePrarc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreArchivo[ idePrarc=" + idePrarc + " ]";
    }
    
}
