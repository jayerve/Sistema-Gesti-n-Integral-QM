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
@Table(name = "cont_tiket_viaje", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "ContTiketViaje.findAll", query = "SELECT c FROM ContTiketViaje c"),
    @NamedQuery(name = "ContTiketViaje.findByIdeCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.ideCotiv = :ideCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByDetalleViajeCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.detalleViajeCotiv = :detalleViajeCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByFechaPartidaCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.fechaPartidaCotiv = :fechaPartidaCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByFechaRetornoCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.fechaRetornoCotiv = :fechaRetornoCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByNumMemoCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.numMemoCotiv = :numMemoCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByNumMemoPagadoCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.numMemoPagadoCotiv = :numMemoPagadoCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByFechaVencePagoCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.fechaVencePagoCotiv = :fechaVencePagoCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByFechaPagadoCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.fechaPagadoCotiv = :fechaPagadoCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByRazonAnuladoCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.razonAnuladoCotiv = :razonAnuladoCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByFechaAnuladoCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.fechaAnuladoCotiv = :fechaAnuladoCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByActivoCotiv", query = "SELECT c FROM ContTiketViaje c WHERE c.activoCotiv = :activoCotiv"),
    @NamedQuery(name = "ContTiketViaje.findByUsuarioIngre", query = "SELECT c FROM ContTiketViaje c WHERE c.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "ContTiketViaje.findByFechaIngre", query = "SELECT c FROM ContTiketViaje c WHERE c.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "ContTiketViaje.findByHoraIngre", query = "SELECT c FROM ContTiketViaje c WHERE c.horaIngre = :horaIngre"),
    @NamedQuery(name = "ContTiketViaje.findByUsuarioActua", query = "SELECT c FROM ContTiketViaje c WHERE c.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "ContTiketViaje.findByFechaActua", query = "SELECT c FROM ContTiketViaje c WHERE c.fechaActua = :fechaActua"),
    @NamedQuery(name = "ContTiketViaje.findByHoraActua", query = "SELECT c FROM ContTiketViaje c WHERE c.horaActua = :horaActua")})
public class ContTiketViaje implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_cotiv", nullable = false)
    private Long ideCotiv;
    @Size(max = 2147483647)
    @Column(name = "detalle_viaje_cotiv", length = 2147483647)
    private String detalleViajeCotiv;
    @Column(name = "fecha_partida_cotiv")
    @Temporal(TemporalType.DATE)
    private Date fechaPartidaCotiv;
    @Column(name = "fecha_retorno_cotiv")
    @Temporal(TemporalType.DATE)
    private Date fechaRetornoCotiv;
    @Size(max = 50)
    @Column(name = "num_memo_cotiv", length = 50)
    private String numMemoCotiv;
    @Size(max = 50)
    @Column(name = "num_memo_pagado_cotiv", length = 50)
    private String numMemoPagadoCotiv;
    @Column(name = "fecha_vence_pago_cotiv")
    @Temporal(TemporalType.DATE)
    private Date fechaVencePagoCotiv;
    @Column(name = "fecha_pagado_cotiv")
    @Temporal(TemporalType.DATE)
    private Date fechaPagadoCotiv;
    @Size(max = 2147483647)
    @Column(name = "razon_anulado_cotiv", length = 2147483647)
    private String razonAnuladoCotiv;
    @Column(name = "fecha_anulado_cotiv")
    @Temporal(TemporalType.DATE)
    private Date fechaAnuladoCotiv;
    @Column(name = "activo_cotiv")
    private Boolean activoCotiv;
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
    @JoinColumn(name = "ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica ideGedip;
    @JoinColumn(name = "gen_ide_gedip", referencedColumnName = "ide_gedip")
    @ManyToOne
    private GenDivisionPolitica genIdeGedip;
    @JoinColumn(name = "ide_cotit", referencedColumnName = "ide_cotit")
    @ManyToOne
    private ContTipoTransporte ideCotit;
    @JoinColumn(name = "ide_coest", referencedColumnName = "ide_coest")
    @ManyToOne
    private ContEstado ideCoest;
    @JoinColumn(name = "ide_coasv", referencedColumnName = "ide_coasv")
    @ManyToOne
    private ContAsuntoViaje ideCoasv;

    public ContTiketViaje() {
    }

    public ContTiketViaje(Long ideCotiv) {
        this.ideCotiv = ideCotiv;
    }

    public Long getIdeCotiv() {
        return ideCotiv;
    }

    public void setIdeCotiv(Long ideCotiv) {
        this.ideCotiv = ideCotiv;
    }

    public String getDetalleViajeCotiv() {
        return detalleViajeCotiv;
    }

    public void setDetalleViajeCotiv(String detalleViajeCotiv) {
        this.detalleViajeCotiv = detalleViajeCotiv;
    }

    public Date getFechaPartidaCotiv() {
        return fechaPartidaCotiv;
    }

    public void setFechaPartidaCotiv(Date fechaPartidaCotiv) {
        this.fechaPartidaCotiv = fechaPartidaCotiv;
    }

    public Date getFechaRetornoCotiv() {
        return fechaRetornoCotiv;
    }

    public void setFechaRetornoCotiv(Date fechaRetornoCotiv) {
        this.fechaRetornoCotiv = fechaRetornoCotiv;
    }

    public String getNumMemoCotiv() {
        return numMemoCotiv;
    }

    public void setNumMemoCotiv(String numMemoCotiv) {
        this.numMemoCotiv = numMemoCotiv;
    }

    public String getNumMemoPagadoCotiv() {
        return numMemoPagadoCotiv;
    }

    public void setNumMemoPagadoCotiv(String numMemoPagadoCotiv) {
        this.numMemoPagadoCotiv = numMemoPagadoCotiv;
    }

    public Date getFechaVencePagoCotiv() {
        return fechaVencePagoCotiv;
    }

    public void setFechaVencePagoCotiv(Date fechaVencePagoCotiv) {
        this.fechaVencePagoCotiv = fechaVencePagoCotiv;
    }

    public Date getFechaPagadoCotiv() {
        return fechaPagadoCotiv;
    }

    public void setFechaPagadoCotiv(Date fechaPagadoCotiv) {
        this.fechaPagadoCotiv = fechaPagadoCotiv;
    }

    public String getRazonAnuladoCotiv() {
        return razonAnuladoCotiv;
    }

    public void setRazonAnuladoCotiv(String razonAnuladoCotiv) {
        this.razonAnuladoCotiv = razonAnuladoCotiv;
    }

    public Date getFechaAnuladoCotiv() {
        return fechaAnuladoCotiv;
    }

    public void setFechaAnuladoCotiv(Date fechaAnuladoCotiv) {
        this.fechaAnuladoCotiv = fechaAnuladoCotiv;
    }

    public Boolean getActivoCotiv() {
        return activoCotiv;
    }

    public void setActivoCotiv(Boolean activoCotiv) {
        this.activoCotiv = activoCotiv;
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

    public GenDivisionPolitica getIdeGedip() {
        return ideGedip;
    }

    public void setIdeGedip(GenDivisionPolitica ideGedip) {
        this.ideGedip = ideGedip;
    }

    public GenDivisionPolitica getGenIdeGedip() {
        return genIdeGedip;
    }

    public void setGenIdeGedip(GenDivisionPolitica genIdeGedip) {
        this.genIdeGedip = genIdeGedip;
    }

    public ContTipoTransporte getIdeCotit() {
        return ideCotit;
    }

    public void setIdeCotit(ContTipoTransporte ideCotit) {
        this.ideCotit = ideCotit;
    }

    public ContEstado getIdeCoest() {
        return ideCoest;
    }

    public void setIdeCoest(ContEstado ideCoest) {
        this.ideCoest = ideCoest;
    }

    public ContAsuntoViaje getIdeCoasv() {
        return ideCoasv;
    }

    public void setIdeCoasv(ContAsuntoViaje ideCoasv) {
        this.ideCoasv = ideCoasv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCotiv != null ? ideCotiv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContTiketViaje)) {
            return false;
        }
        ContTiketViaje other = (ContTiketViaje) object;
        if ((this.ideCotiv == null && other.ideCotiv != null) || (this.ideCotiv != null && !this.ideCotiv.equals(other.ideCotiv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.ContTiketViaje[ ideCotiv=" + ideCotiv + " ]";
    }
    
}
