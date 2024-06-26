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
@Table(name = "sis_reglas_clave", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SisReglasClave.findAll", query = "SELECT s FROM SisReglasClave s"),
    @NamedQuery(name = "SisReglasClave.findByIdeRecl", query = "SELECT s FROM SisReglasClave s WHERE s.ideRecl = :ideRecl"),
    @NamedQuery(name = "SisReglasClave.findByNomRecl", query = "SELECT s FROM SisReglasClave s WHERE s.nomRecl = :nomRecl"),
    @NamedQuery(name = "SisReglasClave.findByLongitudMinimaRecl", query = "SELECT s FROM SisReglasClave s WHERE s.longitudMinimaRecl = :longitudMinimaRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumCaracEspeRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numCaracEspeRecl = :numCaracEspeRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumMayusRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numMayusRecl = :numMayusRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumMinuscRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numMinuscRecl = :numMinuscRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumNumerosRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numNumerosRecl = :numNumerosRecl"),
    @NamedQuery(name = "SisReglasClave.findByIntentosRecl", query = "SELECT s FROM SisReglasClave s WHERE s.intentosRecl = :intentosRecl"),
    @NamedQuery(name = "SisReglasClave.findByLongitudLoginRecl", query = "SELECT s FROM SisReglasClave s WHERE s.longitudLoginRecl = :longitudLoginRecl"),
    @NamedQuery(name = "SisReglasClave.findByNumValidaAnteriorRecl", query = "SELECT s FROM SisReglasClave s WHERE s.numValidaAnteriorRecl = :numValidaAnteriorRecl"),
    @NamedQuery(name = "SisReglasClave.findByUsuarioIngre", query = "SELECT s FROM SisReglasClave s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SisReglasClave.findByFechaIngre", query = "SELECT s FROM SisReglasClave s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SisReglasClave.findByUsuarioActua", query = "SELECT s FROM SisReglasClave s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SisReglasClave.findByFechaActua", query = "SELECT s FROM SisReglasClave s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SisReglasClave.findByHoraIngre", query = "SELECT s FROM SisReglasClave s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SisReglasClave.findByHoraActua", query = "SELECT s FROM SisReglasClave s WHERE s.horaActua = :horaActua")})
public class SisReglasClave implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_recl", nullable = false)
    private Integer ideRecl;
    @Size(max = 50)
    @Column(name = "nom_recl", length = 50)
    private String nomRecl;
    @Column(name = "longitud_minima_recl")
    private Integer longitudMinimaRecl;
    @Column(name = "num_carac_espe_recl")
    private Integer numCaracEspeRecl;
    @Column(name = "num_mayus_recl")
    private Integer numMayusRecl;
    @Column(name = "num_minusc_recl")
    private Integer numMinuscRecl;
    @Column(name = "num_numeros_recl")
    private Integer numNumerosRecl;
    @Column(name = "intentos_recl")
    private Integer intentosRecl;
    @Column(name = "longitud_login_recl")
    private Integer longitudLoginRecl;
    @Column(name = "num_valida_anterior_recl")
    private Integer numValidaAnteriorRecl;
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
    @JoinColumn(name = "ide_empr", referencedColumnName = "ide_empr")
    @ManyToOne
    private SisEmpresa ideEmpr;

    public SisReglasClave() {
    }

    public SisReglasClave(Integer ideRecl) {
        this.ideRecl = ideRecl;
    }

    public Integer getIdeRecl() {
        return ideRecl;
    }

    public void setIdeRecl(Integer ideRecl) {
        this.ideRecl = ideRecl;
    }

    public String getNomRecl() {
        return nomRecl;
    }

    public void setNomRecl(String nomRecl) {
        this.nomRecl = nomRecl;
    }

    public Integer getLongitudMinimaRecl() {
        return longitudMinimaRecl;
    }

    public void setLongitudMinimaRecl(Integer longitudMinimaRecl) {
        this.longitudMinimaRecl = longitudMinimaRecl;
    }

    public Integer getNumCaracEspeRecl() {
        return numCaracEspeRecl;
    }

    public void setNumCaracEspeRecl(Integer numCaracEspeRecl) {
        this.numCaracEspeRecl = numCaracEspeRecl;
    }

    public Integer getNumMayusRecl() {
        return numMayusRecl;
    }

    public void setNumMayusRecl(Integer numMayusRecl) {
        this.numMayusRecl = numMayusRecl;
    }

    public Integer getNumMinuscRecl() {
        return numMinuscRecl;
    }

    public void setNumMinuscRecl(Integer numMinuscRecl) {
        this.numMinuscRecl = numMinuscRecl;
    }

    public Integer getNumNumerosRecl() {
        return numNumerosRecl;
    }

    public void setNumNumerosRecl(Integer numNumerosRecl) {
        this.numNumerosRecl = numNumerosRecl;
    }

    public Integer getIntentosRecl() {
        return intentosRecl;
    }

    public void setIntentosRecl(Integer intentosRecl) {
        this.intentosRecl = intentosRecl;
    }

    public Integer getLongitudLoginRecl() {
        return longitudLoginRecl;
    }

    public void setLongitudLoginRecl(Integer longitudLoginRecl) {
        this.longitudLoginRecl = longitudLoginRecl;
    }

    public Integer getNumValidaAnteriorRecl() {
        return numValidaAnteriorRecl;
    }

    public void setNumValidaAnteriorRecl(Integer numValidaAnteriorRecl) {
        this.numValidaAnteriorRecl = numValidaAnteriorRecl;
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

    public SisEmpresa getIdeEmpr() {
        return ideEmpr;
    }

    public void setIdeEmpr(SisEmpresa ideEmpr) {
        this.ideEmpr = ideEmpr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRecl != null ? ideRecl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SisReglasClave)) {
            return false;
        }
        SisReglasClave other = (SisReglasClave) object;
        if ((this.ideRecl == null && other.ideRecl != null) || (this.ideRecl != null && !this.ideRecl.equals(other.ideRecl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SisReglasClave[ ideRecl=" + ideRecl + " ]";
    }
    
}
