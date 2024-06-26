/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "pre_contrato", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "PreContrato.findAll", query = "SELECT p FROM PreContrato p"),
    @NamedQuery(name = "PreContrato.findByIdePrcon", query = "SELECT p FROM PreContrato p WHERE p.idePrcon = :idePrcon"),
    @NamedQuery(name = "PreContrato.findByFechaFirmaPrcon", query = "SELECT p FROM PreContrato p WHERE p.fechaFirmaPrcon = :fechaFirmaPrcon"),
    @NamedQuery(name = "PreContrato.findByFechaInicioPrcon", query = "SELECT p FROM PreContrato p WHERE p.fechaInicioPrcon = :fechaInicioPrcon"),
    @NamedQuery(name = "PreContrato.findByFechaFinPrcon", query = "SELECT p FROM PreContrato p WHERE p.fechaFinPrcon = :fechaFinPrcon"),
    @NamedQuery(name = "PreContrato.findByFechaCierrePrcon", query = "SELECT p FROM PreContrato p WHERE p.fechaCierrePrcon = :fechaCierrePrcon"),
    @NamedQuery(name = "PreContrato.findByNumeroContratoPrcon", query = "SELECT p FROM PreContrato p WHERE p.numeroContratoPrcon = :numeroContratoPrcon"),
    @NamedQuery(name = "PreContrato.findByObservacionPrcon", query = "SELECT p FROM PreContrato p WHERE p.observacionPrcon = :observacionPrcon"),
    @NamedQuery(name = "PreContrato.findByActivoPrcon", query = "SELECT p FROM PreContrato p WHERE p.activoPrcon = :activoPrcon"),
    @NamedQuery(name = "PreContrato.findByUsuarioIngre", query = "SELECT p FROM PreContrato p WHERE p.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "PreContrato.findByFechaIngre", query = "SELECT p FROM PreContrato p WHERE p.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "PreContrato.findByHoraIngre", query = "SELECT p FROM PreContrato p WHERE p.horaIngre = :horaIngre"),
    @NamedQuery(name = "PreContrato.findByUsuarioActua", query = "SELECT p FROM PreContrato p WHERE p.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "PreContrato.findByFechaActua", query = "SELECT p FROM PreContrato p WHERE p.fechaActua = :fechaActua"),
    @NamedQuery(name = "PreContrato.findByHoraActua", query = "SELECT p FROM PreContrato p WHERE p.horaActua = :horaActua")})
public class PreContrato implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_prcon", nullable = false)
    private Long idePrcon;
    @Column(name = "fecha_firma_prcon")
    @Temporal(TemporalType.DATE)
    private Date fechaFirmaPrcon;
    @Column(name = "fecha_inicio_prcon")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioPrcon;
    @Column(name = "fecha_fin_prcon")
    @Temporal(TemporalType.DATE)
    private Date fechaFinPrcon;
    @Column(name = "fecha_cierre_prcon")
    @Temporal(TemporalType.DATE)
    private Date fechaCierrePrcon;
    @Size(max = 50)
    @Column(name = "numero_contrato_prcon", length = 50)
    private String numeroContratoPrcon;
    @Size(max = 2147483647)
    @Column(name = "observacion_prcon", length = 2147483647)
    private String observacionPrcon;
    @Column(name = "activo_prcon")
    private Boolean activoPrcon;
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
    @OneToMany(mappedBy = "idePrcon")
    private List<PreComparecienteContrato> preComparecienteContratoList;
    @JoinColumn(name = "ide_prcop", referencedColumnName = "ide_prcop")
    @ManyToOne
    private PreContratacionPublica idePrcop;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @OneToMany(mappedBy = "idePrcon")
    private List<TesPoliza> tesPolizaList;
    @OneToMany(mappedBy = "idePrcon")
    private List<PreArchivo> preArchivoList;
    @OneToMany(mappedBy = "idePrcon")
    private List<PreAdministradorContrato> preAdministradorContratoList;

    public PreContrato() {
    }

    public PreContrato(Long idePrcon) {
        this.idePrcon = idePrcon;
    }

    public Long getIdePrcon() {
        return idePrcon;
    }

    public void setIdePrcon(Long idePrcon) {
        this.idePrcon = idePrcon;
    }

    public Date getFechaFirmaPrcon() {
        return fechaFirmaPrcon;
    }

    public void setFechaFirmaPrcon(Date fechaFirmaPrcon) {
        this.fechaFirmaPrcon = fechaFirmaPrcon;
    }

    public Date getFechaInicioPrcon() {
        return fechaInicioPrcon;
    }

    public void setFechaInicioPrcon(Date fechaInicioPrcon) {
        this.fechaInicioPrcon = fechaInicioPrcon;
    }

    public Date getFechaFinPrcon() {
        return fechaFinPrcon;
    }

    public void setFechaFinPrcon(Date fechaFinPrcon) {
        this.fechaFinPrcon = fechaFinPrcon;
    }

    public Date getFechaCierrePrcon() {
        return fechaCierrePrcon;
    }

    public void setFechaCierrePrcon(Date fechaCierrePrcon) {
        this.fechaCierrePrcon = fechaCierrePrcon;
    }

    public String getNumeroContratoPrcon() {
        return numeroContratoPrcon;
    }

    public void setNumeroContratoPrcon(String numeroContratoPrcon) {
        this.numeroContratoPrcon = numeroContratoPrcon;
    }

    public String getObservacionPrcon() {
        return observacionPrcon;
    }

    public void setObservacionPrcon(String observacionPrcon) {
        this.observacionPrcon = observacionPrcon;
    }

    public Boolean getActivoPrcon() {
        return activoPrcon;
    }

    public void setActivoPrcon(Boolean activoPrcon) {
        this.activoPrcon = activoPrcon;
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

    public List<PreComparecienteContrato> getPreComparecienteContratoList() {
        return preComparecienteContratoList;
    }

    public void setPreComparecienteContratoList(List<PreComparecienteContrato> preComparecienteContratoList) {
        this.preComparecienteContratoList = preComparecienteContratoList;
    }

    public PreContratacionPublica getIdePrcop() {
        return idePrcop;
    }

    public void setIdePrcop(PreContratacionPublica idePrcop) {
        this.idePrcop = idePrcop;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public List<TesPoliza> getTesPolizaList() {
        return tesPolizaList;
    }

    public void setTesPolizaList(List<TesPoliza> tesPolizaList) {
        this.tesPolizaList = tesPolizaList;
    }

    public List<PreArchivo> getPreArchivoList() {
        return preArchivoList;
    }

    public void setPreArchivoList(List<PreArchivo> preArchivoList) {
        this.preArchivoList = preArchivoList;
    }

    public List<PreAdministradorContrato> getPreAdministradorContratoList() {
        return preAdministradorContratoList;
    }

    public void setPreAdministradorContratoList(List<PreAdministradorContrato> preAdministradorContratoList) {
        this.preAdministradorContratoList = preAdministradorContratoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePrcon != null ? idePrcon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreContrato)) {
            return false;
        }
        PreContrato other = (PreContrato) object;
        if ((this.idePrcon == null && other.idePrcon != null) || (this.idePrcon != null && !this.idePrcon.equals(other.idePrcon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.PreContrato[ idePrcon=" + idePrcon + " ]";
    }
    
}
