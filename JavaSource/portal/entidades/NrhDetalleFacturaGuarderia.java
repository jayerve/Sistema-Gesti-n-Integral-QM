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
import javax.persistence.JoinColumns;
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
@Table(name = "nrh_detalle_factura_guarderia", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findAll", query = "SELECT n FROM NrhDetalleFacturaGuarderia n"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByIdeNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.ideNrdfg = :ideNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByFechaBeneficioNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.fechaBeneficioNrdfg = :fechaBeneficioNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByFechaPagadoNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.fechaPagadoNrdfg = :fechaPagadoNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByEntregaFacturaNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.entregaFacturaNrdfg = :entregaFacturaNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByNroFacturaNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.nroFacturaNrdfg = :nroFacturaNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByValorFacturaNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.valorFacturaNrdfg = :valorFacturaNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByValorPagarNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.valorPagarNrdfg = :valorPagarNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByFechaRegistroNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.fechaRegistroNrdfg = :fechaRegistroNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByPagadoNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.pagadoNrdfg = :pagadoNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByObservacionNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.observacionNrdfg = :observacionNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByActivoNrdfg", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.activoNrdfg = :activoNrdfg"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByUsuarioIngre", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByFechaIngre", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByUsuarioActua", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByFechaActua", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.fechaActua = :fechaActua"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByHoraIngre", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.horaIngre = :horaIngre"),
    @NamedQuery(name = "NrhDetalleFacturaGuarderia.findByHoraActua", query = "SELECT n FROM NrhDetalleFacturaGuarderia n WHERE n.horaActua = :horaActua")})
public class NrhDetalleFacturaGuarderia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nrdfg", nullable = false)
    private Integer ideNrdfg;
    @Column(name = "fecha_beneficio_nrdfg")
    @Temporal(TemporalType.DATE)
    private Date fechaBeneficioNrdfg;
    @Column(name = "fecha_pagado_nrdfg")
    @Temporal(TemporalType.DATE)
    private Date fechaPagadoNrdfg;
    @Column(name = "entrega_factura_nrdfg")
    private Integer entregaFacturaNrdfg;
    @Size(max = 50)
    @Column(name = "nro_factura_nrdfg", length = 50)
    private String nroFacturaNrdfg;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_factura_nrdfg", precision = 12, scale = 3)
    private BigDecimal valorFacturaNrdfg;
    @Column(name = "valor_pagar_nrdfg", precision = 12, scale = 3)
    private BigDecimal valorPagarNrdfg;
    @Column(name = "fecha_registro_nrdfg")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistroNrdfg;
    @Column(name = "pagado_nrdfg")
    private Integer pagadoNrdfg;
    @Size(max = 4000)
    @Column(name = "observacion_nrdfg", length = 4000)
    private String observacionNrdfg;
    @Column(name = "activo_nrdfg")
    private Boolean activoNrdfg;
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
    @JoinColumn(name = "ide_nrrub", referencedColumnName = "ide_nrrub")
    @ManyToOne
    private NrhRubro ideNrrub;
    @JoinColumn(name = "ide_nrhig", referencedColumnName = "ide_nrhig")
    @ManyToOne
    private NrhHijoGuarderia ideNrhig;
    @JoinColumn(name = "ide_nrbee", referencedColumnName = "ide_nrbee")
    @ManyToOne
    private NrhBeneficioEmpleado ideNrbee;
    @JoinColumns({
        @JoinColumn(name = "ide_gemes", referencedColumnName = "ide_gemes"),
        @JoinColumn(name = "ide_geani", referencedColumnName = "ide_geani")})
    @ManyToOne
    private GenPeriodo genPeriodo;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;
    @JoinColumn(name = "gen_ide_geedp2", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp2;

    public NrhDetalleFacturaGuarderia() {
    }

    public NrhDetalleFacturaGuarderia(Integer ideNrdfg) {
        this.ideNrdfg = ideNrdfg;
    }

    public Integer getIdeNrdfg() {
        return ideNrdfg;
    }

    public void setIdeNrdfg(Integer ideNrdfg) {
        this.ideNrdfg = ideNrdfg;
    }

    public Date getFechaBeneficioNrdfg() {
        return fechaBeneficioNrdfg;
    }

    public void setFechaBeneficioNrdfg(Date fechaBeneficioNrdfg) {
        this.fechaBeneficioNrdfg = fechaBeneficioNrdfg;
    }

    public Date getFechaPagadoNrdfg() {
        return fechaPagadoNrdfg;
    }

    public void setFechaPagadoNrdfg(Date fechaPagadoNrdfg) {
        this.fechaPagadoNrdfg = fechaPagadoNrdfg;
    }

    public Integer getEntregaFacturaNrdfg() {
        return entregaFacturaNrdfg;
    }

    public void setEntregaFacturaNrdfg(Integer entregaFacturaNrdfg) {
        this.entregaFacturaNrdfg = entregaFacturaNrdfg;
    }

    public String getNroFacturaNrdfg() {
        return nroFacturaNrdfg;
    }

    public void setNroFacturaNrdfg(String nroFacturaNrdfg) {
        this.nroFacturaNrdfg = nroFacturaNrdfg;
    }

    public BigDecimal getValorFacturaNrdfg() {
        return valorFacturaNrdfg;
    }

    public void setValorFacturaNrdfg(BigDecimal valorFacturaNrdfg) {
        this.valorFacturaNrdfg = valorFacturaNrdfg;
    }

    public BigDecimal getValorPagarNrdfg() {
        return valorPagarNrdfg;
    }

    public void setValorPagarNrdfg(BigDecimal valorPagarNrdfg) {
        this.valorPagarNrdfg = valorPagarNrdfg;
    }

    public Date getFechaRegistroNrdfg() {
        return fechaRegistroNrdfg;
    }

    public void setFechaRegistroNrdfg(Date fechaRegistroNrdfg) {
        this.fechaRegistroNrdfg = fechaRegistroNrdfg;
    }

    public Integer getPagadoNrdfg() {
        return pagadoNrdfg;
    }

    public void setPagadoNrdfg(Integer pagadoNrdfg) {
        this.pagadoNrdfg = pagadoNrdfg;
    }

    public String getObservacionNrdfg() {
        return observacionNrdfg;
    }

    public void setObservacionNrdfg(String observacionNrdfg) {
        this.observacionNrdfg = observacionNrdfg;
    }

    public Boolean getActivoNrdfg() {
        return activoNrdfg;
    }

    public void setActivoNrdfg(Boolean activoNrdfg) {
        this.activoNrdfg = activoNrdfg;
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

    public NrhRubro getIdeNrrub() {
        return ideNrrub;
    }

    public void setIdeNrrub(NrhRubro ideNrrub) {
        this.ideNrrub = ideNrrub;
    }

    public NrhHijoGuarderia getIdeNrhig() {
        return ideNrhig;
    }

    public void setIdeNrhig(NrhHijoGuarderia ideNrhig) {
        this.ideNrhig = ideNrhig;
    }

    public NrhBeneficioEmpleado getIdeNrbee() {
        return ideNrbee;
    }

    public void setIdeNrbee(NrhBeneficioEmpleado ideNrbee) {
        this.ideNrbee = ideNrbee;
    }

    public GenPeriodo getGenPeriodo() {
        return genPeriodo;
    }

    public void setGenPeriodo(GenPeriodo genPeriodo) {
        this.genPeriodo = genPeriodo;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp() {
        return genIdeGeedp;
    }

    public void setGenIdeGeedp(GenEmpleadosDepartamentoPar genIdeGeedp) {
        this.genIdeGeedp = genIdeGeedp;
    }

    public GenEmpleadosDepartamentoPar getIdeGeedp() {
        return ideGeedp;
    }

    public void setIdeGeedp(GenEmpleadosDepartamentoPar ideGeedp) {
        this.ideGeedp = ideGeedp;
    }

    public GenEmpleadosDepartamentoPar getGenIdeGeedp2() {
        return genIdeGeedp2;
    }

    public void setGenIdeGeedp2(GenEmpleadosDepartamentoPar genIdeGeedp2) {
        this.genIdeGeedp2 = genIdeGeedp2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNrdfg != null ? ideNrdfg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NrhDetalleFacturaGuarderia)) {
            return false;
        }
        NrhDetalleFacturaGuarderia other = (NrhDetalleFacturaGuarderia) object;
        if ((this.ideNrdfg == null && other.ideNrdfg != null) || (this.ideNrdfg != null && !this.ideNrdfg.equals(other.ideNrdfg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.NrhDetalleFacturaGuarderia[ ideNrdfg=" + ideNrdfg + " ]";
    }
    
}
