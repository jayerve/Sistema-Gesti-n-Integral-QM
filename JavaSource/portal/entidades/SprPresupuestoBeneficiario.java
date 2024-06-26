/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "spr_presupuesto_beneficiario", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SprPresupuestoBeneficiario.findAll", query = "SELECT s FROM SprPresupuestoBeneficiario s"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByIdeSpprb", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.ideSpprb = :ideSpprb"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByValorSinIvaSpprb", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.valorSinIvaSpprb = :valorSinIvaSpprb"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByValorIvaSpprb", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.valorIvaSpprb = :valorIvaSpprb"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByPartidaPresupSpprb", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.partidaPresupSpprb = :partidaPresupSpprb"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByActivoSpprb", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.activoSpprb = :activoSpprb"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByUsuarioIngre", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByFechaIngre", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByHoraIngre", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByUsuarioActua", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByFechaActua", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SprPresupuestoBeneficiario.findByHoraActua", query = "SELECT s FROM SprPresupuestoBeneficiario s WHERE s.horaActua = :horaActua")})
public class SprPresupuestoBeneficiario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_spprb", nullable = false)
    private Integer ideSpprb;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_sin_iva_spprb", precision = 12, scale = 2)
    private BigDecimal valorSinIvaSpprb;
    @Column(name = "valor_iva_spprb", precision = 12, scale = 2)
    private BigDecimal valorIvaSpprb;
    @Size(max = 50)
    @Column(name = "partida_presup_spprb", length = 50)
    private String partidaPresupSpprb;
    @Column(name = "activo_spprb")
    private Boolean activoSpprb;
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
    @JoinColumn(name = "ide_spprp", referencedColumnName = "ide_spprp")
    @ManyToOne
    private SprPresupuestoPuesto ideSpprp;
    @JoinColumn(name = "ide_geben", referencedColumnName = "ide_geben")
    @ManyToOne
    private GenBeneficiario ideGeben;

    public SprPresupuestoBeneficiario() {
    }

    public SprPresupuestoBeneficiario(Integer ideSpprb) {
        this.ideSpprb = ideSpprb;
    }

    public Integer getIdeSpprb() {
        return ideSpprb;
    }

    public void setIdeSpprb(Integer ideSpprb) {
        this.ideSpprb = ideSpprb;
    }

    public BigDecimal getValorSinIvaSpprb() {
        return valorSinIvaSpprb;
    }

    public void setValorSinIvaSpprb(BigDecimal valorSinIvaSpprb) {
        this.valorSinIvaSpprb = valorSinIvaSpprb;
    }

    public BigDecimal getValorIvaSpprb() {
        return valorIvaSpprb;
    }

    public void setValorIvaSpprb(BigDecimal valorIvaSpprb) {
        this.valorIvaSpprb = valorIvaSpprb;
    }

    public String getPartidaPresupSpprb() {
        return partidaPresupSpprb;
    }

    public void setPartidaPresupSpprb(String partidaPresupSpprb) {
        this.partidaPresupSpprb = partidaPresupSpprb;
    }

    public Boolean getActivoSpprb() {
        return activoSpprb;
    }

    public void setActivoSpprb(Boolean activoSpprb) {
        this.activoSpprb = activoSpprb;
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

    public SprPresupuestoPuesto getIdeSpprp() {
        return ideSpprp;
    }

    public void setIdeSpprp(SprPresupuestoPuesto ideSpprp) {
        this.ideSpprp = ideSpprp;
    }

    public GenBeneficiario getIdeGeben() {
        return ideGeben;
    }

    public void setIdeGeben(GenBeneficiario ideGeben) {
        this.ideGeben = ideGeben;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSpprb != null ? ideSpprb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SprPresupuestoBeneficiario)) {
            return false;
        }
        SprPresupuestoBeneficiario other = (SprPresupuestoBeneficiario) object;
        if ((this.ideSpprb == null && other.ideSpprb != null) || (this.ideSpprb != null && !this.ideSpprb.equals(other.ideSpprb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SprPresupuestoBeneficiario[ ideSpprb=" + ideSpprb + " ]";
    }
    
}
