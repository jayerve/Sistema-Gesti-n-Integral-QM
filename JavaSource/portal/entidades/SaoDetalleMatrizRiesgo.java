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
@Table(name = "sao_detalle_matriz_riesgo", catalog = "sampu", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findAll", query = "SELECT s FROM SaoDetalleMatrizRiesgo s"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByIdeSadmr", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.ideSadmr = :ideSadmr"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByNroHormbresSadmr", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.nroHormbresSadmr = :nroHormbresSadmr"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByNroMujeresSadmr", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.nroMujeresSadmr = :nroMujeresSadmr"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByNroDiscapacitadoSadmr", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.nroDiscapacitadoSadmr = :nroDiscapacitadoSadmr"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByDetaFacReisgoSadmr", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.detaFacReisgoSadmr = :detaFacReisgoSadmr"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByAnexoSadmr", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.anexoSadmr = :anexoSadmr"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByCumpleLegalSadmar", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.cumpleLegalSadmar = :cumpleLegalSadmar"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByObservaRefLegalSadmar", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.observaRefLegalSadmar = :observaRefLegalSadmar"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByDetaAccionTomarSadmar", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.detaAccionTomarSadmar = :detaAccionTomarSadmar"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByFechaFinSadmar", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.fechaFinSadmar = :fechaFinSadmar"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByActivoSadmr", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.activoSadmr = :activoSadmr"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByUsuarioIngre", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.usuarioIngre = :usuarioIngre"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByFechaIngre", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.fechaIngre = :fechaIngre"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByHoraIngre", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.horaIngre = :horaIngre"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByUsuarioActua", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.usuarioActua = :usuarioActua"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByFechaActua", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.fechaActua = :fechaActua"),
    @NamedQuery(name = "SaoDetalleMatrizRiesgo.findByHoraActua", query = "SELECT s FROM SaoDetalleMatrizRiesgo s WHERE s.horaActua = :horaActua")})
public class SaoDetalleMatrizRiesgo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_sadmr", nullable = false)
    private Integer ideSadmr;
    @Column(name = "nro_hormbres_sadmr")
    private Integer nroHormbresSadmr;
    @Column(name = "nro_mujeres_sadmr")
    private Integer nroMujeresSadmr;
    @Column(name = "nro_discapacitado_sadmr")
    private Integer nroDiscapacitadoSadmr;
    @Size(max = 1000)
    @Column(name = "deta_fac_reisgo_sadmr", length = 1000)
    private String detaFacReisgoSadmr;
    @Size(max = 1000)
    @Column(name = "anexo_sadmr", length = 1000)
    private String anexoSadmr;
    @Column(name = "cumple_legal_sadmar")
    private Integer cumpleLegalSadmar;
    @Size(max = 1000)
    @Column(name = "observa_ref_legal_sadmar", length = 1000)
    private String observaRefLegalSadmar;
    @Size(max = 4000)
    @Column(name = "deta_accion_tomar_sadmar", length = 4000)
    private String detaAccionTomarSadmar;
    @Column(name = "fecha_fin_sadmar")
    @Temporal(TemporalType.DATE)
    private Date fechaFinSadmar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo_sadmr", nullable = false)
    private boolean activoSadmr;
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
    @JoinColumn(name = "ide_samar", referencedColumnName = "ide_samar")
    @ManyToOne
    private SaoMatrizRiesgo ideSamar;
    @JoinColumn(name = "ide_sainw", referencedColumnName = "ide_sainw")
    @ManyToOne
    private SaoIndiceWfine ideSainw;
    @JoinColumn(name = "sao_ide_sagrp2", referencedColumnName = "ide_sagrp")
    @ManyToOne
    private SaoGradoPeligro saoIdeSagrp2;
    @JoinColumn(name = "sao_ide_sagrp", referencedColumnName = "ide_sagrp")
    @ManyToOne
    private SaoGradoPeligro saoIdeSagrp;
    @JoinColumn(name = "ide_sagrp", referencedColumnName = "ide_sagrp")
    @ManyToOne
    private SaoGradoPeligro ideSagrp;
    @JoinColumn(name = "ide_safac", referencedColumnName = "ide_safac")
    @ManyToOne
    private SaoFactorControl ideSafac;
    @JoinColumn(name = "ide_saclr", referencedColumnName = "ide_saclr")
    @ManyToOne
    private SaoClasificacionRiesgos ideSaclr;
    @JoinColumn(name = "sao_ide_saclr", referencedColumnName = "ide_saclr")
    @ManyToOne
    private SaoClasificacionRiesgos saoIdeSaclr;
    @JoinColumn(name = "gen_ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar genIdeGeedp;
    @JoinColumn(name = "ide_geedp", referencedColumnName = "ide_geedp")
    @ManyToOne
    private GenEmpleadosDepartamentoPar ideGeedp;

    public SaoDetalleMatrizRiesgo() {
    }

    public SaoDetalleMatrizRiesgo(Integer ideSadmr) {
        this.ideSadmr = ideSadmr;
    }

    public SaoDetalleMatrizRiesgo(Integer ideSadmr, boolean activoSadmr) {
        this.ideSadmr = ideSadmr;
        this.activoSadmr = activoSadmr;
    }

    public Integer getIdeSadmr() {
        return ideSadmr;
    }

    public void setIdeSadmr(Integer ideSadmr) {
        this.ideSadmr = ideSadmr;
    }

    public Integer getNroHormbresSadmr() {
        return nroHormbresSadmr;
    }

    public void setNroHormbresSadmr(Integer nroHormbresSadmr) {
        this.nroHormbresSadmr = nroHormbresSadmr;
    }

    public Integer getNroMujeresSadmr() {
        return nroMujeresSadmr;
    }

    public void setNroMujeresSadmr(Integer nroMujeresSadmr) {
        this.nroMujeresSadmr = nroMujeresSadmr;
    }

    public Integer getNroDiscapacitadoSadmr() {
        return nroDiscapacitadoSadmr;
    }

    public void setNroDiscapacitadoSadmr(Integer nroDiscapacitadoSadmr) {
        this.nroDiscapacitadoSadmr = nroDiscapacitadoSadmr;
    }

    public String getDetaFacReisgoSadmr() {
        return detaFacReisgoSadmr;
    }

    public void setDetaFacReisgoSadmr(String detaFacReisgoSadmr) {
        this.detaFacReisgoSadmr = detaFacReisgoSadmr;
    }

    public String getAnexoSadmr() {
        return anexoSadmr;
    }

    public void setAnexoSadmr(String anexoSadmr) {
        this.anexoSadmr = anexoSadmr;
    }

    public Integer getCumpleLegalSadmar() {
        return cumpleLegalSadmar;
    }

    public void setCumpleLegalSadmar(Integer cumpleLegalSadmar) {
        this.cumpleLegalSadmar = cumpleLegalSadmar;
    }

    public String getObservaRefLegalSadmar() {
        return observaRefLegalSadmar;
    }

    public void setObservaRefLegalSadmar(String observaRefLegalSadmar) {
        this.observaRefLegalSadmar = observaRefLegalSadmar;
    }

    public String getDetaAccionTomarSadmar() {
        return detaAccionTomarSadmar;
    }

    public void setDetaAccionTomarSadmar(String detaAccionTomarSadmar) {
        this.detaAccionTomarSadmar = detaAccionTomarSadmar;
    }

    public Date getFechaFinSadmar() {
        return fechaFinSadmar;
    }

    public void setFechaFinSadmar(Date fechaFinSadmar) {
        this.fechaFinSadmar = fechaFinSadmar;
    }

    public boolean getActivoSadmr() {
        return activoSadmr;
    }

    public void setActivoSadmr(boolean activoSadmr) {
        this.activoSadmr = activoSadmr;
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

    public SaoMatrizRiesgo getIdeSamar() {
        return ideSamar;
    }

    public void setIdeSamar(SaoMatrizRiesgo ideSamar) {
        this.ideSamar = ideSamar;
    }

    public SaoIndiceWfine getIdeSainw() {
        return ideSainw;
    }

    public void setIdeSainw(SaoIndiceWfine ideSainw) {
        this.ideSainw = ideSainw;
    }

    public SaoGradoPeligro getSaoIdeSagrp2() {
        return saoIdeSagrp2;
    }

    public void setSaoIdeSagrp2(SaoGradoPeligro saoIdeSagrp2) {
        this.saoIdeSagrp2 = saoIdeSagrp2;
    }

    public SaoGradoPeligro getSaoIdeSagrp() {
        return saoIdeSagrp;
    }

    public void setSaoIdeSagrp(SaoGradoPeligro saoIdeSagrp) {
        this.saoIdeSagrp = saoIdeSagrp;
    }

    public SaoGradoPeligro getIdeSagrp() {
        return ideSagrp;
    }

    public void setIdeSagrp(SaoGradoPeligro ideSagrp) {
        this.ideSagrp = ideSagrp;
    }

    public SaoFactorControl getIdeSafac() {
        return ideSafac;
    }

    public void setIdeSafac(SaoFactorControl ideSafac) {
        this.ideSafac = ideSafac;
    }

    public SaoClasificacionRiesgos getIdeSaclr() {
        return ideSaclr;
    }

    public void setIdeSaclr(SaoClasificacionRiesgos ideSaclr) {
        this.ideSaclr = ideSaclr;
    }

    public SaoClasificacionRiesgos getSaoIdeSaclr() {
        return saoIdeSaclr;
    }

    public void setSaoIdeSaclr(SaoClasificacionRiesgos saoIdeSaclr) {
        this.saoIdeSaclr = saoIdeSaclr;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSadmr != null ? ideSadmr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaoDetalleMatrizRiesgo)) {
            return false;
        }
        SaoDetalleMatrizRiesgo other = (SaoDetalleMatrizRiesgo) object;
        if ((this.ideSadmr == null && other.ideSadmr != null) || (this.ideSadmr != null && !this.ideSadmr.equals(other.ideSadmr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "portal.entidades.SaoDetalleMatrizRiesgo[ ideSadmr=" + ideSadmr + " ]";
    }
    
}
