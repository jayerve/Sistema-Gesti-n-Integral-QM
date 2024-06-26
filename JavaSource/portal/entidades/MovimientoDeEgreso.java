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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author awbecerra
 */
@Entity
@Table(name = "movimiento_de_egreso", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "MovimientoDeEgreso.findAll", query = "SELECT m FROM MovimientoDeEgreso m"),
    @NamedQuery(name = "MovimientoDeEgreso.findByIdeMovimientoDEgreso", query = "SELECT m FROM MovimientoDeEgreso m WHERE m.ideMovimientoDEgreso = :ideMovimientoDEgreso"),
    @NamedQuery(name = "MovimientoDeEgreso.findByFecha", query = "SELECT m FROM MovimientoDeEgreso m WHERE m.fecha = :fecha"),
    @NamedQuery(name = "MovimientoDeEgreso.findByReferencia", query = "SELECT m FROM MovimientoDeEgreso m WHERE m.referencia = :referencia"),
    @NamedQuery(name = "MovimientoDeEgreso.findByBeneficiario", query = "SELECT m FROM MovimientoDeEgreso m WHERE m.beneficiario = :beneficiario"),
    @NamedQuery(name = "MovimientoDeEgreso.findByConcepto", query = "SELECT m FROM MovimientoDeEgreso m WHERE m.concepto = :concepto"),
    @NamedQuery(name = "MovimientoDeEgreso.findByDebe", query = "SELECT m FROM MovimientoDeEgreso m WHERE m.debe = :debe"),
    @NamedQuery(name = "MovimientoDeEgreso.findByHaber", query = "SELECT m FROM MovimientoDeEgreso m WHERE m.haber = :haber"),
    @NamedQuery(name = "MovimientoDeEgreso.findBySaldo", query = "SELECT m FROM MovimientoDeEgreso m WHERE m.saldo = :saldo"),
    @NamedQuery(name = "MovimientoDeEgreso.findByCuenta", query = "SELECT m FROM MovimientoDeEgreso m WHERE m.cuenta = :cuenta")})
public class MovimientoDeEgreso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ide_movimiento_d_egreso", nullable = false)
    private Integer ideMovimientoDEgreso;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 30)
    @Column(name = "referencia", length = 30)
    private String referencia;
    @Size(max = 500)
    @Column(name = "beneficiario", length = 500)
    private String beneficiario;
    @Size(max = 500)
    @Column(name = "concepto", length = 500)
    private String concepto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debe", precision = 15, scale = 2)
    private BigDecimal debe;
    @Column(name = "haber", precision = 15, scale = 2)
    private BigDecimal haber;
    @Column(name = "saldo", precision = 15, scale = 2)
    private BigDecimal saldo;
    @Size(max = 500)
    @Column(name = "cuenta", length = 500)
    private String cuenta;

    public MovimientoDeEgreso() {
    }

    public MovimientoDeEgreso(Integer ideMovimientoDEgreso) {
        this.ideMovimientoDEgreso = ideMovimientoDEgreso;
    }

    public Integer getIdeMovimientoDEgreso() {
        return ideMovimientoDEgreso;
    }

    public void setIdeMovimientoDEgreso(Integer ideMovimientoDEgreso) {
        this.ideMovimientoDEgreso = ideMovimientoDEgreso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideMovimientoDEgreso != null ? ideMovimientoDEgreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientoDeEgreso)) {
            return false;
        }
        MovimientoDeEgreso other = (MovimientoDeEgreso) object;
        if ((this.ideMovimientoDEgreso == null && other.ideMovimientoDEgreso != null) || (this.ideMovimientoDEgreso != null && !this.ideMovimientoDEgreso.equals(other.ideMovimientoDEgreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.MovimientoDeEgreso[ ideMovimientoDEgreso=" + ideMovimientoDEgreso + " ]";
    }
    
}
