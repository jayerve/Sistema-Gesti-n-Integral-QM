/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package portal.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "sao_grado_peligro", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoGradoPeligro.findAll", query = "SELECT s FROM SaoGradoPeligro s"),
    @NamedQuery(name = "SaoGradoPeligro.findByIdeSagrp", query = "SELECT s FROM SaoGradoPeligro s WHERE s.ideSagrp = :ideSagrp"),
    @NamedQuery(name = "SaoGradoPeligro.findByDetalleSagrp", query = "SELECT s FROM SaoGradoPeligro s WHERE s.detalleSagrp = :detalleSagrp"),
    @NamedQuery(name = "SaoGradoPeligro.findByValorSagrp", query = "SELECT s FROM SaoGradoPeligro s WHERE s.valorSagrp = :valorSagrp"),
    @NamedQuery(name = "SaoGradoPeligro.findByActivoSagrp", query = "SELECT s FROM SaoGradoPeligro s WHERE s.activoSagrp = :activoSagrp"),
    @NamedQuery(name = "SaoGradoPeligro.findByUsuarioIngre", query = "SELECT s FROM SaoGradoPeligro s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoGradoPeligro.findByFechaIngre", query = "SELECT s FROM SaoGradoPeligro s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoGradoPeligro.findByHoraIngre", query = "SELECT s FROM SaoGradoPeligro s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoGradoPeligro.findByUsuarioActua", query = "SELECT s FROM SaoGradoPeligro s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoGradoPeligro.findByFechaActua", query = "SELECT s FROM SaoGradoPeligro s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoGradoPeligro.findByHoraActua", query = "SELECT s FROM SaoGradoPeligro s WHERE s.horaActua = :horaActua")})
public class SaoGradoPeligro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sagrp", nullable = false)
    private Integer ideSagrp;
    @Size(max = 100)
    @Column(name = "detalle_sagrp", length = 100)
    private String detalleSagrp;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_sagrp", precision = 12, scale = 2)
    private BigDecimal valorSagrp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sagrp", nullable = false)
    private boolean activoSagrp;
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
    @OneToMany(mappedBy = "saoIdeSagrp2")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList;
    @OneToMany(mappedBy = "saoIdeSagrp")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList1;
    @OneToMany(mappedBy = "ideSagrp")
    private List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList2;
    @OneToMany(mappedBy = "saoIdeSagrp")
    private List<SaoGradoPeligro> saoGradoPeligroList;
    @JoinColumn(name = "sao_ide_sagrp", referencedColumnName = "ide_sagrp")
    @ManyToOne
    private SaoGradoPeligro saoIdeSagrp;

    public SaoGradoPeligro() {
    }

    public SaoGradoPeligro(Integer ideSagrp) {
        this.ideSagrp = ideSagrp;
    }

    public SaoGradoPeligro(Integer ideSagrp, boolean activoSagrp) {
        this.ideSagrp = ideSagrp;
        this.activoSagrp = activoSagrp;
    }

    public Integer getIdeSagrp() {
        return ideSagrp;
    }

    public void setIdeSagrp(Integer ideSagrp) {
        this.ideSagrp = ideSagrp;
    }

    public String getDetalleSagrp() {
        return detalleSagrp;
    }

    public void setDetalleSagrp(String detalleSagrp) {
        this.detalleSagrp = detalleSagrp;
    }

    public BigDecimal getValorSagrp() {
        return valorSagrp;
    }

    public void setValorSagrp(BigDecimal valorSagrp) {
        this.valorSagrp = valorSagrp;
    }

    public boolean getActivoSagrp() {
        return activoSagrp;
    }

    public void setActivoSagrp(boolean activoSagrp) {
        this.activoSagrp = activoSagrp;
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

    public List<SaoDetalleMatrizRiesgo> getSaoDetalleMatrizRiesgoList() {
        return saoDetalleMatrizRiesgoList;
    }

    public void setSaoDetalleMatrizRiesgoList(List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList) {
        this.saoDetalleMatrizRiesgoList = saoDetalleMatrizRiesgoList;
    }

    public List<SaoDetalleMatrizRiesgo> getSaoDetalleMatrizRiesgoList1() {
        return saoDetalleMatrizRiesgoList1;
    }

    public void setSaoDetalleMatrizRiesgoList1(List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList1) {
        this.saoDetalleMatrizRiesgoList1 = saoDetalleMatrizRiesgoList1;
    }

    public List<SaoDetalleMatrizRiesgo> getSaoDetalleMatrizRiesgoList2() {
        return saoDetalleMatrizRiesgoList2;
    }

    public void setSaoDetalleMatrizRiesgoList2(List<SaoDetalleMatrizRiesgo> saoDetalleMatrizRiesgoList2) {
        this.saoDetalleMatrizRiesgoList2 = saoDetalleMatrizRiesgoList2;
    }

    public List<SaoGradoPeligro> getSaoGradoPeligroList() {
        return saoGradoPeligroList;
    }

    public void setSaoGradoPeligroList(List<SaoGradoPeligro> saoGradoPeligroList) {
        this.saoGradoPeligroList = saoGradoPeligroList;
    }

    public SaoGradoPeligro getSaoIdeSagrp() {
        return saoIdeSagrp;
    }

    public void setSaoIdeSagrp(SaoGradoPeligro saoIdeSagrp) {
        this.saoIdeSagrp = saoIdeSagrp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSagrp != null ? ideSagrp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoGradoPeligro)) {
            return false;
        }
        SaoGradoPeligro other = (SaoGradoPeligro) object;
        if ((this.ideSagrp == null && other.ideSagrp != null) || (this.ideSagrp != null && !this.ideSagrp.equals(other.ideSagrp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoGradoPeligro[ ideSagrp=" + ideSagrp + " ]";
    }
    
}
