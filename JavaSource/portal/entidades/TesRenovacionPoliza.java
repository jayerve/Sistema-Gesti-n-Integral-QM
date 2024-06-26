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
@Table(name = "tes_renovacion_poliza", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "TesRenovacionPoliza.findAll", query = "SELECT t FROM TesRenovacionPoliza t"),
    @NamedQuery(name = "TesRenovacionPoliza.findByIdeTerep", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.ideTerep = :ideTerep"),
    @NamedQuery(name = "TesRenovacionPoliza.findByFechaDesdeTerep", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.fechaDesdeTerep = :fechaDesdeTerep"),
    @NamedQuery(name = "TesRenovacionPoliza.findByFechaHastaTerep", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.fechaHastaTerep = :fechaHastaTerep"),
    @NamedQuery(name = "TesRenovacionPoliza.findByNumeroTerep", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.numeroTerep = :numeroTerep"),
    @NamedQuery(name = "TesRenovacionPoliza.findByNumeroOficioTerep", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.numeroOficioTerep = :numeroOficioTerep"),
    @NamedQuery(name = "TesRenovacionPoliza.findByActivoTerep", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.activoTerep = :activoTerep"),
    @NamedQuery(name = "TesRenovacionPoliza.findByUsuarioIngre", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "TesRenovacionPoliza.findByFechaIngre", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "TesRenovacionPoliza.findByHoraIngre", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.horaIngre = :horaIngre"),
    @NamedQuery(name = "TesRenovacionPoliza.findByUsuarioActua", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "TesRenovacionPoliza.findByFechaActua", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.fechaActua = :fechaActua"),
    @NamedQuery(name = "TesRenovacionPoliza.findByHoraActua", query = "SELECT t FROM TesRenovacionPoliza t WHERE t.horaActua = :horaActua")})
public class TesRenovacionPoliza implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_terep", nullable = false)
    private Long ideTerep;
    @Column(name = "fecha_desde_terep")
    @Temporal(TemporalType.DATE)
    private Date fechaDesdeTerep;
    @Column(name = "fecha_hasta_terep")
    @Temporal(TemporalType.DATE)
    private Date fechaHastaTerep;
    @Column(name = "numero_terep")
    private BigInteger numeroTerep;
    @Size(max = 100)
    @Column(name = "numero_oficio_terep", length = 100)
    private String numeroOficioTerep;
    @Column(name = "activo_terep")
    private Boolean activoTerep;
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
    @JoinColumn(name = "ide_tepol", referencedColumnName = "ide_tepol")
    @ManyToOne
    private TesPoliza ideTepol;
    @JoinColumn(name = "ide_copag", referencedColumnName = "ide_copag")
    @ManyToOne
    private ContParametrosGeneral ideCopag;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;

    public TesRenovacionPoliza() {
    }

    public TesRenovacionPoliza(Long ideTerep) {
        this.ideTerep = ideTerep;
    }

    public Long getIdeTerep() {
        return ideTerep;
    }

    public void setIdeTerep(Long ideTerep) {
        this.ideTerep = ideTerep;
    }

    public Date getFechaDesdeTerep() {
        return fechaDesdeTerep;
    }

    public void setFechaDesdeTerep(Date fechaDesdeTerep) {
        this.fechaDesdeTerep = fechaDesdeTerep;
    }

    public Date getFechaHastaTerep() {
        return fechaHastaTerep;
    }

    public void setFechaHastaTerep(Date fechaHastaTerep) {
        this.fechaHastaTerep = fechaHastaTerep;
    }

    public BigInteger getNumeroTerep() {
        return numeroTerep;
    }

    public void setNumeroTerep(BigInteger numeroTerep) {
        this.numeroTerep = numeroTerep;
    }

    public String getNumeroOficioTerep() {
        return numeroOficioTerep;
    }

    public void setNumeroOficioTerep(String numeroOficioTerep) {
        this.numeroOficioTerep = numeroOficioTerep;
    }

    public Boolean getActivoTerep() {
        return activoTerep;
    }

    public void setActivoTerep(Boolean activoTerep) {
        this.activoTerep = activoTerep;
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

    public TesPoliza getIdeTepol() {
        return ideTepol;
    }

    public void setIdeTepol(TesPoliza ideTepol) {
        this.ideTepol = ideTepol;
    }

    public ContParametrosGeneral getIdeCopag() {
        return ideCopag;
    }

    public void setIdeCopag(ContParametrosGeneral ideCopag) {
        this.ideCopag = ideCopag;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTerep != null ? ideTerep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TesRenovacionPoliza)) {
            return false;
        }
        TesRenovacionPoliza other = (TesRenovacionPoliza) object;
        if ((this.ideTerep == null && other.ideTerep != null) || (this.ideTerep != null && !this.ideTerep.equals(other.ideTerep))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.TesRenovacionPoliza[ ideTerep=" + ideTerep + " ]";
    }
    
}
