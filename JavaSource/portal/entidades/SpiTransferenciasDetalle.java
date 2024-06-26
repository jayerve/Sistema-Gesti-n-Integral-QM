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
@Table(name = "spi_transferencias_detalle", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SpiTransferenciasDetalle.findAll", query = "SELECT s FROM SpiTransferenciasDetalle s"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByIdeSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.ideSptrd = :ideSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findBySecuencialSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.secuencialSptrd = :secuencialSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByCodOrigenPagoSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.codOrigenPagoSptrd = :codOrigenPagoSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByCodigoMonedaSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.codigoMonedaSptrd = :codigoMonedaSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByMontoTransferidoSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.montoTransferidoSptrd = :montoTransferidoSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByCodConceptoPagoSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.codConceptoPagoSptrd = :codConceptoPagoSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByCodBancoSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.codBancoSptrd = :codBancoSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByNroCuentaSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.nroCuentaSptrd = :nroCuentaSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByTipoCuentaSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.tipoCuentaSptrd = :tipoCuentaSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByEmpleadoSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.empleadoSptrd = :empleadoSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByCedulaSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.cedulaSptrd = :cedulaSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByActivoSptrd", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.activoSptrd = :activoSptrd"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByUsuarioIngre", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByFechaIngre", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByUsuarioActua", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByFechaActua", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByHoraIngre", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SpiTransferenciasDetalle.findByHoraActua", query = "SELECT s FROM SpiTransferenciasDetalle s WHERE s.horaActua = :horaActua")})
public class SpiTransferenciasDetalle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sptrd", nullable = false)
    private Integer ideSptrd;
    @Column(name = "secuencial_sptrd")
    private Integer secuencialSptrd;
    @Column(name = "cod_origen_pago_sptrd")
    private Integer codOrigenPagoSptrd;
    @Column(name = "codigo_moneda_sptrd")
    private Integer codigoMonedaSptrd;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto_transferido_sptrd", precision = 12, scale = 2)
    private BigDecimal montoTransferidoSptrd;
    @Column(name = "cod_concepto_pago_sptrd")
    private Integer codConceptoPagoSptrd;
    @Size(max = 50)
    @Column(name = "cod_banco_sptrd", length = 50)
    private String codBancoSptrd;
    @Size(max = 50)
    @Column(name = "nro_cuenta_sptrd", length = 50)
    private String nroCuentaSptrd;
    @Column(name = "tipo_cuenta_sptrd")
    private Integer tipoCuentaSptrd;
    @Size(max = 100)
    @Column(name = "empleado_sptrd", length = 100)
    private String empleadoSptrd;
    @Size(max = 13)
    @Column(name = "cedula_sptrd", length = 13)
    private String cedulaSptrd;
    @Column(name = "activo_sptrd")
    private Boolean activoSptrd;
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
    @JoinColumn(name = "ide_sptra", referencedColumnName = "ide_sptra")
    @ManyToOne
    private SpiTransferencias ideSptra;

    public SpiTransferenciasDetalle() {
    }

    public SpiTransferenciasDetalle(Integer ideSptrd) {
        this.ideSptrd = ideSptrd;
    }

    public Integer getIdeSptrd() {
        return ideSptrd;
    }

    public void setIdeSptrd(Integer ideSptrd) {
        this.ideSptrd = ideSptrd;
    }

    public Integer getSecuencialSptrd() {
        return secuencialSptrd;
    }

    public void setSecuencialSptrd(Integer secuencialSptrd) {
        this.secuencialSptrd = secuencialSptrd;
    }

    public Integer getCodOrigenPagoSptrd() {
        return codOrigenPagoSptrd;
    }

    public void setCodOrigenPagoSptrd(Integer codOrigenPagoSptrd) {
        this.codOrigenPagoSptrd = codOrigenPagoSptrd;
    }

    public Integer getCodigoMonedaSptrd() {
        return codigoMonedaSptrd;
    }

    public void setCodigoMonedaSptrd(Integer codigoMonedaSptrd) {
        this.codigoMonedaSptrd = codigoMonedaSptrd;
    }

    public BigDecimal getMontoTransferidoSptrd() {
        return montoTransferidoSptrd;
    }

    public void setMontoTransferidoSptrd(BigDecimal montoTransferidoSptrd) {
        this.montoTransferidoSptrd = montoTransferidoSptrd;
    }

    public Integer getCodConceptoPagoSptrd() {
        return codConceptoPagoSptrd;
    }

    public void setCodConceptoPagoSptrd(Integer codConceptoPagoSptrd) {
        this.codConceptoPagoSptrd = codConceptoPagoSptrd;
    }

    public String getCodBancoSptrd() {
        return codBancoSptrd;
    }

    public void setCodBancoSptrd(String codBancoSptrd) {
        this.codBancoSptrd = codBancoSptrd;
    }

    public String getNroCuentaSptrd() {
        return nroCuentaSptrd;
    }

    public void setNroCuentaSptrd(String nroCuentaSptrd) {
        this.nroCuentaSptrd = nroCuentaSptrd;
    }

    public Integer getTipoCuentaSptrd() {
        return tipoCuentaSptrd;
    }

    public void setTipoCuentaSptrd(Integer tipoCuentaSptrd) {
        this.tipoCuentaSptrd = tipoCuentaSptrd;
    }

    public String getEmpleadoSptrd() {
        return empleadoSptrd;
    }

    public void setEmpleadoSptrd(String empleadoSptrd) {
        this.empleadoSptrd = empleadoSptrd;
    }

    public String getCedulaSptrd() {
        return cedulaSptrd;
    }

    public void setCedulaSptrd(String cedulaSptrd) {
        this.cedulaSptrd = cedulaSptrd;
    }

    public Boolean getActivoSptrd() {
        return activoSptrd;
    }

    public void setActivoSptrd(Boolean activoSptrd) {
        this.activoSptrd = activoSptrd;
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

    public SpiTransferencias getIdeSptra() {
        return ideSptra;
    }

    public void setIdeSptra(SpiTransferencias ideSptra) {
        this.ideSptra = ideSptra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSptrd != null ? ideSptrd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SpiTransferenciasDetalle)) {
            return false;
        }
        SpiTransferenciasDetalle other = (SpiTransferenciasDetalle) object;
        if ((this.ideSptrd == null && other.ideSptrd != null) || (this.ideSptrd != null && !this.ideSptrd.equals(other.ideSptrd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SpiTransferenciasDetalle[ ideSptrd=" + ideSptrd + " ]";
    }
    
}
